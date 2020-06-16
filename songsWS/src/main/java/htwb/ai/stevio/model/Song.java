package htwb.ai.stevio.model;

import com.sun.istack.NotNull;
import javax.persistence.*;
import java.io.Serializable;

/*
 *
 * @author Mario Teklic
 */
@Entity
@Table(name = "song")
public class Song implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "artist")
    private String artist;

    @Column(name = "label")
    private String label;

    @Column(name = "released")
    private int released;

    public Song(){}

    public Song(int id, String title, String artist, String label, int released) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.label = label;
        this.released = released;
    }

    public Song(Builder builder){
        this.id = builder.id;
        this.title = builder.title;
        this.artist = builder.artist;
        this.label = builder.label;
        this.released = builder.released;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getReleased() {
        return released;
    }

    public void setReleased(int released) {
        this.released = released;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static final class Builder{
        private int id;
        private String title;
        private String artist;
        private String label;
        private int released;

        private Builder(){

        }

        public Builder withId(int id){
            this.id = id;
            return this;
        }

        public Builder withTitle(String title){
            this.title = title;
            return this;
        }

        public Builder withArtist(String artist){
            this.artist = artist;
            return this;
        }

        public Builder withLabel(String label){
            this.label = label;
            return this;
        }
        public Builder withReleased(int released){
            this.released = released;
            return this;
        }

        public Song build() {
            return new Song(this);
        }
    }
}

