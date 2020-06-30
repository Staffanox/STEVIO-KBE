package htwb.ai.stevio.controller;
/*
 *
 * @author Mario Teklic
 */

import htwb.ai.stevio.dao.AuthenticatorDAO;
import htwb.ai.stevio.dao.DBSongListDAO;
import htwb.ai.stevio.dao.DBSongsDAO;
import htwb.ai.stevio.model.Song;
import htwb.ai.stevio.model.SongList;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static htwb.ai.stevio.controller.UsersControllerTest.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



public class SongListControllerTest {

    private MockMvc mockMvc;

    private DBSongListDAO songListDAO = new DBSongListDAO("SongList-TEST-PU");

    private DBSongsDAO songsDAO = new DBSongsDAO("SongList-TEST-PU");

    private AuthenticatorDAO authenticator;


    public void init(){
        //Modify authenticator: send always true for any String as token
        authenticator = Mockito.mock(AuthenticatorDAO.class);
        Mockito.when(authenticator.authenticate(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(authenticator.createToken(ArgumentMatchers.any())).thenReturn("mockitoUserToken");

        authenticator = Mockito.mock(AuthenticatorDAO.class);
        Mockito.when(authenticator.authenticate(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(authenticator.createToken(ArgumentMatchers.any())).thenReturn("mockitoUserToken");

        mockMvc = MockMvcBuilders.standaloneSetup(new SongListController(songsDAO, authenticator, songListDAO)).build();
    }


    public SongList createAndUploadSongListToDb(String owner){
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
        setSongList.setVisibility(true);

        songListDAO.addSongList(setSongList);

        return setSongList;
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
        setSongList.setVisibility(true);

        return setSongList;
    }

    // /songLists/{id}  (songlist-id)
    @Test
    public void GETById() throws Exception {
        this.init();
        this.createAndUploadSongListToDb("eschuler");

       mockMvc.perform(get("/songList/1")
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

        this.createAndUploadSongListToDb("mmuster");

        mockMvc.perform(get("/songList/2")
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
        this.init();
        this.createAndUploadSongListToDb("eschuler");

        mockMvc.perform(get("/songList/eschuler")
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
        this.init();
        SongList songList = this.createSongList("eschuler");

        //TODO Beim LocationHeader soll die ID der erstellten Songliste zurueckgegeben werden

        mockMvc.perform(post("/songList")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(songList))
                .header(HttpHeaders.AUTHORIZATION, "aToken"));

        mockMvc.perform(get("/songList/1")
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
        this.init();
        SongList songList = this.createAndUploadSongListToDb("eschuler");

        mockMvc.perform(delete("/songList/1")
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
