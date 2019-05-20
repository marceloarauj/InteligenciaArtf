package insert;


import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marcelo
 */
public class InsertData {
    
    String usuario = "postgres";
    String password ="1234";
    String driv = "org.postgresql.Driver";
    String urlBanco = "jdbc:postgresql://179.232.28.247:5432/InteligenciaArtf";
    String urlApi = "https://api.stackexchange.com/2.2/questions?order=desc&sort=activity&site=stackoverflow";
    
    
    Connection conexao;
    Statement querys;
    
    public static void main(String[] args) {
        
        new InsertData().conectar();
    }
    
    public void conectar(){
        
        try {
           
            conexao = DriverManager.getConnection(urlBanco,usuario,password);
            querys = conexao.createStatement();
            
        }  catch (SQLException ex) {
            
        }
        // lógica de preencher os dados 10000 requisições
        String resposta="";

        for(int i =0; i < 100; i++){

            try {
            URL url = new URL(urlApi);
            HttpURLConnection conectApi = (HttpURLConnection) url.openConnection();
            conectApi.setRequestMethod("GET");
            
            conectApi.setRequestProperty("Content-Type","application/json; charset=utf-8");
            
            if(conectApi.getResponseCode() == 200){
                
                GZIPInputStream gz = new GZIPInputStream(conectApi.getInputStream());
                Reader rd = new InputStreamReader(gz);
                                
                Gson json = new Gson();
                StringBuffer string = new StringBuffer();
                
                while(true){
                    int ch = rd.read();
                    
                    if(ch == -1){
                        break;
                    }
                    string.append((char)ch);
                }
                resposta = string.toString();
               
                Objeto items = json.fromJson(resposta,Objeto.class);
                
                for(int k =0; k < items.items.length; k++){
                    //inserir tags
                    for(int j =0; j < items.items[k].getTags().length;j++){
                        
                        String tag = items.items[k].getTags()[j];
                        
                        try {
                            PreparedStatement p = conexao.prepareStatement
                                    (Query.INSERT_TAGS,Statement.RETURN_GENERATED_KEYS);
                            
                            p.setString(1, tag);
                            p.executeUpdate();
                            
                        } catch (SQLException ex) {
                            System.out.println(ex.getLocalizedMessage());
                        }
                    }
                    //inserir postagens e relacionar as tags 
                    PreparedStatement p;
                    try {
                        p = conexao.prepareStatement
                                            (Query.INSERT_POSTAGEM,Statement.RETURN_GENERATED_KEYS);
                        
                        p.setInt(1, items.items[k].getPostagem());
                        p.setInt(2, items.items[k].getScore());
                        p.setString(3, items.items[k].getLink());
                        p.setString(4, items.items[k].getTitle() );
                        
                        p.executeUpdate();
                        
                        //inserir em tags_postagens
                        p = conexao.prepareStatement
                                            (Query.INSERT_TAG_POST,Statement.RETURN_GENERATED_KEYS);
                        
                        int z =0;
                        while(z < items.items[k].getTags().length){
                            
                            
                            p.setInt(1, items.items[k].getPostagem());
                            p.setString(2,items.items[k].tags[z]);
                            
                            p.executeUpdate();
                            
                            z++;
                        }
                        
                       
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(InsertData.class.getName()).log(Level.SEVERE, null, ex);
                    }
                            
                            
                }

                rd.close();
            }
            conectApi.disconnect();
            } catch (MalformedURLException ex) {
            } catch (IOException ex) {

            } 
            
        }
        
        
        
        desconectar();
    }
    
    private void desconectar(){
       
        try {
            if(conexao != null){
                
                querys.close();
                conexao.close();
            }
            
        } catch (SQLException ex) {

        }
    }
    
}
