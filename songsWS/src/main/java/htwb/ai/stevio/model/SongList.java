package htwb.ai.stevio.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "songlist", schema = "public")
@XmlRootElement(name = "songList")
@XmlAccessorType(XmlAccessType.FIELD)
public class SongList implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ownerid")
    private User user;

    @Column(name = "name")
    @JsonProperty("name")
    @XmlAttribute(name = "name")
    private String name;

    @Column(name = "private")
    @JsonProperty("isPrivate")
    private Boolean isPrivate;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "song_songlist", schema = "public",
            joinColumns = {@JoinColumn(name = "list_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "song_id", referencedColumnName = "id")})
    @JsonProperty("songList")
    private Set<Song> songList = new HashSet<Song>();



    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Boolean getIsPrivate() {
        return isPrivate;
    }


    public Set<Song> getSongList() {
        return songList;
    }

    @Override
    public String toString() {
        return "[id=" + this.getId() + ", user=" + user.getUserId() + ", name=" + name + ", state=" + isPrivate + ", songs:"
                + songList.toString() + "]";
    }
}
