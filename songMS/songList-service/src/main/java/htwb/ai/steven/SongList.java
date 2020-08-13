package htwb.ai.steven;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "songlist", schema = "public")
@XmlRootElement(name = "songList")
@XmlAccessorType(XmlAccessType.FIELD)
public class SongList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "ownerid")
    private String ownerId;

    @Column(name = "name")
    @JsonProperty("name")
    @XmlAttribute(name = "name")
    private String name;

    @Column(name = "isprivate")
    @JsonProperty("isPrivate")
    private Boolean isPrivate;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "song_songlist", schema = "public",
            joinColumns = {@JoinColumn(name = "list_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "song_id", referencedColumnName = "id")})
    @JsonProperty("songList")
    private Set<Song> songList = new TreeSet<>();


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setUser(String user) {
        this.ownerId = user;
    }

    // muss so wegen JSON Ausgabe, kenne die Camel Case convention
    public Boolean getisPrivate() {
        return isPrivate;
    }

    public void setisPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Set<Song> getSongList() {
        return songList;
    }

    public void setSongList(Set<Song> list){
        this.songList = list;
    }

    public void addSong(Song song) {
        this.songList.add(song);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        String content = "";

        for(Song s : songList){
            content += "[ID=" + s.getId() + "][TITLE=" + s.getTitle() + "][ARTIST=" + s.getArtist() + "][LABEL=" + s.getLabel() + "][RELEASED=" + s.getReleased() + "]\n";
        }

    return content;
    }
}
