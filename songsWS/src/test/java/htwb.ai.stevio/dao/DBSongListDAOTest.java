package htwb.ai.stevio.dao;
/*
 *
 * @author Mario Teklic
 */

import htwb.ai.stevio.model.Song;
import htwb.ai.stevio.model.SongList;
import org.junit.Test;
import java.util.Iterator;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DBSongListDAOTest {

    private final String PU = "SongList-TEST-PU";

    /**
     * Uploads one Songlist with one specific owner but always same songs
     * @param songlistDAO
     * @param songsDAO
     * @param owner
     * @return
     */
    public SongList set1Songlist(DBSongListDAO songlistDAO, DBSongsDAO songsDAO, String owner){
        SongList setSongList = new SongList();
        Song a = new Song();
        a = a.builder().withArtist("artist_eins").withLabel("label_eins").withReleased(2011).withTitle("title_eins").build();

        Song b = new Song();
        b = b.builder().withArtist("artist_zwei").withLabel("label_zwei").withReleased(2012).withTitle("title_zwei").build();

        Song c = new Song();
        c = c.builder().withArtist("artist_drei").withLabel("label_drei").withReleased(2013).withTitle("title_drei").build();

        songsDAO.addSong(a);
        songsDAO.addSong(b);
        songsDAO.addSong(c);

        setSongList.addSong(a);
        setSongList.addSong(b);
        setSongList.addSong(c);

        setSongList.setUser(owner);
        setSongList.setVisibility(true);

        songlistDAO.addSongList(setSongList);

        return setSongList;
    }

    /**
     * Uploads 2 different Songlists with same owner
     * @param songlistDAO
     * @param songsDAO
     * @param owner
     * @return
     */
    public SongList set2Songlists(DBSongListDAO songlistDAO, DBSongsDAO songsDAO, String owner){
        //SONGLIST 1

        Song a1 = new Song();
        a1 = a1.builder().withArtist("artist_eins1").withLabel("label_eins1").withReleased(2011).withTitle("title_eins1").build();

        Song b1 = new Song();
        b1 = b1.builder().withArtist("artist_zwei1").withLabel("label_zwei1").withReleased(2012).withTitle("title_zwei1").build();

        Song c1 = new Song();
        c1 = c1.builder().withArtist("artist_drei1").withLabel("label_drei1").withReleased(2013).withTitle("title_drei1").build();

        songsDAO.addSong(a1);
        songsDAO.addSong(b1);
        songsDAO.addSong(c1);

        SongList setSongList1 = new SongList();

        setSongList1.addSong(a1);
        setSongList1.addSong(b1);
        setSongList1.addSong(c1);

        setSongList1.setUser(owner);
        setSongList1.setVisibility(true);

        songlistDAO.addSongList(setSongList1);

        //SONGLIST 2

        Song a2 = new Song();
        a2 = a2.builder().withArtist("artist_eins2").withLabel("label_eins2").withReleased(2011).withTitle("title_eins2").build();

        Song b2 = new Song();
        b2 = b2.builder().withArtist("artist_zwei2").withLabel("label_zwei2").withReleased(2012).withTitle("title_zwei2").build();

        Song c2 = new Song();
        c2 = c2.builder().withArtist("artist_drei2").withLabel("label_drei2").withReleased(2013).withTitle("title_drei2").build();

        songsDAO.addSong(a2);
        songsDAO.addSong(b2);
        songsDAO.addSong(c2);

        SongList setSongList2 = new SongList();

        setSongList2.addSong(a2);
        setSongList2.addSong(b2);
        setSongList2.addSong(c2);

        setSongList2.setUser(owner);
        setSongList2.setVisibility(true); //trivial

        songlistDAO.addSongList(setSongList2);

        return setSongList2;
    }

    @Test
    public void addSonglistAndVerifyThatSameSongsInGetSonglist_OnlyOneList(){
        DBSongListDAO songlistDAO = new DBSongListDAO(PU);
        DBSongsDAO songsDAO = new DBSongsDAO(PU);

        //Upload ONE songlist
       SongList setSongList = this.set1Songlist(songlistDAO, songsDAO, "owner");

       Set<Song> getSonglist = songlistDAO.getSongList("owner").get(0).getSongList();
       Set<Song> setSonglist = setSongList.getSongList();

       Iterator<Song> itSong = setSonglist.iterator();

       while(itSong.hasNext()){
           Song current = itSong.next();
           boolean isInList = false;

           for(int i = 0; i < setSonglist.size(); i++){
               if(current.equals(getSonglist.toArray()[i])){
                    isInList = true;
                    break;
               }
           }

           assertTrue(isInList);
       }
    }

    @Test
    public void addSonglistAndVerifyThatSameSongsInGetSonglist_MultipleLists(){
        DBSongListDAO songlistDAO = new DBSongListDAO(PU);
        DBSongsDAO songsDAO = new DBSongsDAO(PU);

        //Setting Songlists
        SongList setSongList2 = this.set2Songlists(songlistDAO, songsDAO,"owner");

        //Should have 2 entry
        assertTrue(songlistDAO.getSongList("owner").size() == 2);

        Set<Song> getSonglist = songlistDAO.getSongList("owner").get(1).getSongList();
        Set<Song> setSonglist = setSongList2.getSongList();

        Iterator<Song> itSong = setSonglist.iterator();

        while(itSong.hasNext()){
            Song current = itSong.next();
            boolean isInList = false;

            for(int i = 0; i < setSonglist.size(); i++){
                if(current.equals(getSonglist.toArray()[i])){
                    isInList = true;
                    break;
                }
            }
            assertTrue(isInList);
        }
    }

    @Test
    public void addSonglistsFromDifferentOwnersAndGetSonglistsFromJustOneOwner(){
        DBSongListDAO songlistDAO = new DBSongListDAO(PU);
        DBSongsDAO songsDAO = new DBSongsDAO(PU);

        //Setting Songlists
        this.set1Songlist(songlistDAO, songsDAO,"ownerOne");
        this.set1Songlist(songlistDAO, songsDAO,"ownerOne");
        this.set1Songlist(songlistDAO, songsDAO,"ownerOne");
        this.set1Songlist(songlistDAO, songsDAO,"ownerTwo");
        this.set1Songlist(songlistDAO, songsDAO,"ownerThree");
        this.set1Songlist(songlistDAO, songsDAO,"ownerFour");

        //Should have only 3 entry not 6 because ownerOne has just 3
        assertTrue(songlistDAO.getSongList("ownerOne").size() == 3);

    }

    @Test
    public void addSonglistsGetSonglistById(){
        DBSongListDAO songlistDAO = new DBSongListDAO(PU);
        DBSongsDAO songsDAO = new DBSongsDAO(PU);

        //Setting Songlists
        this.set1Songlist(songlistDAO, songsDAO,"ownerOne");
        this.set1Songlist(songlistDAO, songsDAO,"ownerTwo");
        this.set1Songlist(songlistDAO, songsDAO,"ownerThree");
        this.set1Songlist(songlistDAO, songsDAO,"ownerFour");

        //Should have only 3 entry not 6 because ownerOne has just 3
        assertTrue(songlistDAO.getSongList("ownerOne").size() == 1);
    }

    @Test
    public void idsShouldStartWith1(){
        DBSongListDAO songlistDAO = new DBSongListDAO(PU);
        DBSongsDAO songsDAO = new DBSongsDAO(PU);

        //Setting Songlists
        this.set1Songlist(songlistDAO, songsDAO,"ownerOne");
        this.set1Songlist(songlistDAO, songsDAO,"ownerTwo");
        this.set1Songlist(songlistDAO, songsDAO,"ownerThree");
        this.set1Songlist(songlistDAO, songsDAO,"ownerFour");

        assertTrue(songlistDAO.getSongList(1).getOwnerId().equals("ownerOne"));
        assertTrue(songlistDAO.getSongList(3).getOwnerId().equals("ownerThree"));
    }

    @Test
    public void addSonglistShouldReturnId(){
        DBSongListDAO songlistDAO = new DBSongListDAO(PU);
        DBSongsDAO songsDAO = new DBSongsDAO(PU);

        SongList setSongList = new SongList();
        Song a = new Song();
        a = a.builder().withArtist("artist_eins").withLabel("label_eins").withReleased(2011).withTitle("title_eins").build();

        Song b = new Song();
        b = b.builder().withArtist("artist_zwei").withLabel("label_zwei").withReleased(2012).withTitle("title_zwei").build();

        Song c = new Song();
        c = c.builder().withArtist("artist_drei").withLabel("label_drei").withReleased(2013).withTitle("title_drei").build();

        songsDAO.addSong(a);
        songsDAO.addSong(b);
        songsDAO.addSong(c);

        setSongList.addSong(a);
        setSongList.addSong(b);
        setSongList.addSong(c);

        setSongList.setUser("owner");
        setSongList.setVisibility(true);

        this.set1Songlist(songlistDAO, songsDAO, "ownerOne"); //id 1
        this.set1Songlist(songlistDAO, songsDAO, "ownerTwo"); //id 2

        int id3 = songlistDAO.addSongList(setSongList);

        assertTrue(id3 == 3);
    }

    @Test
    public void deleteSonglist(){
        DBSongListDAO songlistDAO = new DBSongListDAO(PU);
        DBSongsDAO songsDAO = new DBSongsDAO(PU);

        //Setting Songlists
        SongList songList = this.set1Songlist(songlistDAO, songsDAO,"ownerOne");
        this.set1Songlist(songlistDAO, songsDAO,"ownerTwo");
        this.set1Songlist(songlistDAO, songsDAO,"ownerThree");
        this.set1Songlist(songlistDAO, songsDAO,"ownerFour");

        //Before deleting -> size should be 1
        assertTrue(songlistDAO.getSongList("ownerOne").size() == 1);

        //Delete songlist from ownerOne
        songlistDAO.deleteSong(songList);

        //Get songlists from ownerOne -> should be empty
        assertTrue(songlistDAO.getSongList("ownerOne").size() == 0);

    }
}