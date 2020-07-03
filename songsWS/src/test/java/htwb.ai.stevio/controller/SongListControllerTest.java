package htwb.ai.stevio.controller;
/*
 *
 * @author Mario Teklic
 */

import htwb.ai.stevio.dao.AuthenticatorDAO;
import htwb.ai.stevio.dao.DBSongListDAO;
import htwb.ai.stevio.dao.DBSongsDAO;
import htwb.ai.stevio.dao.DBUsersDAO;
import htwb.ai.stevio.model.Song;
import htwb.ai.stevio.model.SongList;
import htwb.ai.stevio.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Iterator;

import static htwb.ai.stevio.controller.UsersControllerTest.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



public class SongListControllerTest {

    private MockMvc mockMvcSongListController;

    private MockMvc mockMvcUserController;

    private final String PU = "SongList-TEST-PU";

    private DBSongListDAO songListDAO = new DBSongListDAO(PU);

    private DBSongsDAO songsDAO = new DBSongsDAO(PU);

    private AuthenticatorDAO authenticator = new AuthenticatorDAO();

    private DBUsersDAO usersDAO = new DBUsersDAO(PU);

    private String token = null;

    @Before
    public void init() throws Exception {
        mockMvcSongListController = MockMvcBuilders.standaloneSetup(new SongListController(songsDAO, authenticator, songListDAO)).build();
        mockMvcUserController = MockMvcBuilders.standaloneSetup(new UsersController(usersDAO, authenticator)).build();

        this.token = mockMvcUserController.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(new User("eschuler", "pass1234", "First", "Last"))))
                .andReturn().getResponse().getContentAsString();
    }


    public SongList createAndUploadSongListToDb(String owner){
        //add songs, add songlist (Daten vom aufgabenblatt)
        SongList songList = new SongList();
        Song a = new Song();
        a = a.builder().withArtist("Starship").withLabel("Grunt/RCA").withReleased(1985).withTitle("We Built This City").build();

        Song b = new Song();
        b = b.builder().withArtist("Phil Collins").withLabel("Virgin").withReleased(1985).withTitle("Sussudio").build();

        songsDAO.addSong(a);
        songsDAO.addSong(b);

        songList.addSong(a);
        songList.addSong(b);

        songList.setUser(owner);
        songList.setIsPrivate(true);
        songList.setName("ElenasPrivate");

        try {
            mockMvcSongListController.perform(post("/songLists")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(asJsonString(songList)).header(HttpHeaders.AUTHORIZATION, token));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("COULD NOT UPLOAD SONGLIST!");
        }

        //songListDAO.addSongList(songList);

        System.out.println(songListDAO.getSongList(1));

        return songList;
    }

    public SongList createSongList(String owner){
        //add songs, add songlist (Daten vom aufgabenblatt)
        SongList setSongList = new SongList();
        Song a = new Song();
        a = a.builder().withArtist("Starship").withLabel("Grunt/RCA").withReleased(1985).withTitle("We Built This City").build();

        Song b = new Song();
        b = b.builder().withArtist("Phil Collins").withLabel("Virgin").withReleased(1985).withTitle("Sussudio").build();

        songsDAO.addSong(a);
        songsDAO.addSong(b);

        setSongList.addSong(a);
        setSongList.addSong(b);

        setSongList.setUser(owner);
        setSongList.setIsPrivate(true);
        setSongList.setName("ElenasPrivate");

        return setSongList;
    }

    //TODO Als erstes post pruefen OHNE uploadSonglist-Helpermethode

    // /songLists/{id}  (songlist-id)
    @Test
    public void GETById() throws Exception {
        this.createAndUploadSongListToDb("eschuler");

       mockMvcSongListController.perform(get("/songLists/1")
               .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().is(HttpStatus.ACCEPTED.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.isPrivate").value(1))
                .andExpect(jsonPath("$.ownerId").value("eschuler"))
                .andExpect(jsonPath("$.name").value("ElenasPrivate"))
                .andExpect(jsonPath("$.isPrivate").value(true))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$[0][0].title").value("We Built This City"))
                .andExpect(jsonPath("$[0][0].artist").value("Starship"))
                .andExpect(jsonPath("$[0][0].label").value("Grunt/RCA"))
                .andExpect(jsonPath("$[0][0].released").value(1985))
               .andExpect(jsonPath("$[0][1].id").value(2))
               .andExpect(jsonPath("$[0][1].title").value("Sussudio"))
               .andExpect(jsonPath("$[0][1].artist").value("Phil Collins"))
               .andExpect(jsonPath("$[0][1].label").value("Virgin"))
               .andExpect(jsonPath("$[0][1].released").value(1985));

        this.createAndUploadSongListToDb("mmuster");

        mockMvcSongListController.perform(get("/songList/2")
                .header(HttpHeaders.AUTHORIZATION, "aToken"))
                .andExpect(status().is(HttpStatus.ACCEPTED.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML_VALUE))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].ownerId").value("mmuster"))
                .andExpect(jsonPath("$[0].name").value("ElenasPublic"))
                .andExpect(jsonPath("$[0].isPrivate").value(false))
                .andExpect(jsonPath("$[0][0].id").value(1))
                .andExpect(jsonPath("$[0][0].title").value("We Built This City"))
                .andExpect(jsonPath("$[0][0].artist").value("Starship"))
                .andExpect(jsonPath("$[0][0].label").value("Grunt/RCA"))
                .andExpect(jsonPath("$[0][0].released").value(1985))
                .andExpect(jsonPath("$[0][1].id").value(2))
                .andExpect(jsonPath("$[0][1].title").value("Sussudio"))
                .andExpect(jsonPath("$[0][1].artist").value("Phil Collins"))
                .andExpect(jsonPath("$[0][1].label").value("Virgin"))
                .andExpect(jsonPath("$[0][1].released").value(1985));
    }

    // /songLists/{userid}
    @Test
    public void GETByUserId() throws Exception {

        this.createAndUploadSongListToDb("eschuler");

        mockMvcSongListController.perform(get("/songList/eschuler")
                .header(HttpHeaders.AUTHORIZATION, "aToken"))
                .andExpect(status().is(HttpStatus.ACCEPTED.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML_VALUE))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].ownerId").value("eschuler"))
                .andExpect(jsonPath("$[0].name").value("ElenasPublic"))
                .andExpect(jsonPath("$[0].isPrivate").value(false))
                .andExpect(jsonPath("$[0][0].id").value(1))
                .andExpect(jsonPath("$[0][0].title").value("We Built This City"))
                .andExpect(jsonPath("$[0][0].artist").value("Starship"))
                .andExpect(jsonPath("$[0][0].label").value("Grunt/RCA"))
                .andExpect(jsonPath("$[0][0].released").value(1985))
                .andExpect(jsonPath("$[0][1].id").value(2))
                .andExpect(jsonPath("$[0][1].title").value("Sussudio"))
                .andExpect(jsonPath("$[0][1].artist").value("Phil Collins"))
                .andExpect(jsonPath("$[0][1].label").value("Virgin"))
                .andExpect(jsonPath("$[0][1].released").value(1985));
    }

    // /songLists
    @Test
    public void POSTSonglist() throws Exception {

        SongList songList = this.createSongList("eschuler");

        //TODO Beim LocationHeader soll die ID der erstellten Songliste zurueckgegeben werden

        mockMvcSongListController.perform(post("/songList")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(songList))
                .header(HttpHeaders.AUTHORIZATION, "aToken"));

        mockMvcSongListController.perform(get("/songList/1")
                .header(HttpHeaders.AUTHORIZATION, "aToken"))
                .andExpect(status().is(HttpStatus.ACCEPTED.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML_VALUE))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].ownerId").value("eschuler"))
                .andExpect(jsonPath("$[0].name").value("ElenasPublic"))
                .andExpect(jsonPath("$[0].isPrivate").value(false))
                .andExpect(jsonPath("$[0][0].id").value(1))
                .andExpect(jsonPath("$[0][0].title").value("We Built This City"))
                .andExpect(jsonPath("$[0][0].artist").value("Starship"))
                .andExpect(jsonPath("$[0][0].label").value("Grunt/RCA"))
                .andExpect(jsonPath("$[0][0].released").value(1985))
                .andExpect(jsonPath("$[0][1].id").value(2))
                .andExpect(jsonPath("$[0][1].title").value("Sussudio"))
                .andExpect(jsonPath("$[0][1].artist").value("Phil Collins"))
                .andExpect(jsonPath("$[0][1].label").value("Virgin"))
                .andExpect(jsonPath("$[0][1].released").value(1985));

    }

    // /songLists/{id}
    @Test
    public void DELETEById() throws Exception {

        SongList songList = this.createAndUploadSongListToDb("eschuler");

        mockMvcSongListController.perform(delete("/songList/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(songList))
                .header(HttpHeaders.AUTHORIZATION, "aToken"))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML_VALUE))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].ownerId").value("eschuler"))
                .andExpect(jsonPath("$[0].name").value("ElenasPublic"))
                .andExpect(jsonPath("$[0].isPrivate").value(false))
                .andExpect(jsonPath("$[0][0].id").value(1))
                .andExpect(jsonPath("$[0][0].title").value("We Built This City"))
                .andExpect(jsonPath("$[0][0].artist").value("Starship"))
                .andExpect(jsonPath("$[0][0].label").value("Grunt/RCA"))
                .andExpect(jsonPath("$[0][0].released").value(1985))
                .andExpect(jsonPath("$[0][1].id").value(2))
                .andExpect(jsonPath("$[0][1].title").value("Sussudio"))
                .andExpect(jsonPath("$[0][1].artist").value("Phil Collins"))
                .andExpect(jsonPath("$[0][1].label").value("Virgin"))
                .andExpect(jsonPath("$[0][1].released").value(1985));

        Assert.assertEquals(0, songListDAO.getSongList("eschuler").size());
    }
}
