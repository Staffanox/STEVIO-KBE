package htwb.ai.steven;


import com.sun.istack.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;


@Document(collection = "Lyrics")
public class Lyrics {

    @Id
    @NotNull
    private String id;


    private String lyrics;



    public Lyrics(String id, String lyrics){
        this.id = id;
        this.lyrics = lyrics;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }
}
