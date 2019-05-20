/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package insert;

/**
 *
 * @author Marcelo
 */
public interface Query {
    
    String INSERT_TAGS = "INSERT INTO tags(dsc_tag) VALUES(?)";
    String INSERT_POSTAGEM = "INSERT INTO postagem(id_post,score,link_post,title) VALUES(?,?,?,?)";
    String INSERT_TAG_POST = "insert into tags_postagens(id_post,id_tag)\n" +
                                "select ?,(select id_tag from tags where dsc_tag = ?)";
    
}
