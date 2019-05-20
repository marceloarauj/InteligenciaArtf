package insert;

@JsonDTO
public class Postagem {
    
    
    public int question_id;
    public int score;
    public String link;
    public String title;
    public String[] tags;

    public int getPostagem() {
        return question_id;
    }

    public void setPostagem(int postagem) {
        this.question_id = postagem;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
    
    
}
