package htwb.ai.steven.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;


@Document(collection = "Lyrics")
public class Lyrics {

    @Id
    @NotNull
    @JsonProperty("id")
    @XmlAttribute(name = "id")
    private String id;

    @NotNull
    @JsonProperty("lyrics")
    @XmlAttribute(name = "lyrics")
    private String lyrics;

    // default constructor for Jackson
    public Lyrics() {
    }

    public Lyrics(String id, String lyrics) {
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
