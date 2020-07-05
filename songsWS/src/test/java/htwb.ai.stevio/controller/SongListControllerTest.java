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
import static htwb.ai.stevio.controller.UsersControllerTest.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class SongListControllerTest {

    private MockMvc mockMvcSongListController;

    private MockMvc mockMvcUserController;

    private final String PU = "SongList-TEST-PU";

    private DBSongListDAO songListDAO = new DBSongListDAO(PU);

    private DBSongsDAO songsDAO = new DBSongsDAO(PU);

    private AuthenticatorDAO authenticator = new AuthenticatorDAO();

    private DBUsersDAO usersDAO = new DBUsersDAO(PU);

    private String eschulerToken = null;
    private String mmusterToken = null;

    private Song songA = null;
    private Song songB = null;

    @Before
    public void init() throws Exception {
        mockMvcSongListController = MockMvcBuilders.standaloneSetup(new SongListController(songsDAO, authenticator, songListDAO)).build();
        mockMvcUserController = MockMvcBuilders.standaloneSetup(new UsersController(usersDAO, authenticator)).build();

        this.eschulerToken = mockMvcUserController.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(new User("eschuler", "pass1234", "First", "Last"))))
                .andReturn().getResponse().getContentAsString();

        this.mmusterToken = mockMvcUserController.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(new User("mmuster", "pass1234", "First", "Last"))))
                .andReturn().getResponse().getContentAsString();
    }

    public SongList createSongList(String owner, String listName, boolean priv) {
        if (this.songA == null || this.songB == null) {
            //add songs, add songlist (Daten vom aufgabenblatt)
            this.songA = songA.builder().withArtist("Starship").withLabel("Grunt/RCA").withReleased(1985).withTitle("We Built This City").build();
            this.songB = songB.builder().withArtist("Phil Collins").withLabel("Virgin").withReleased(1985).withTitle("Sussudio").build();

            songsDAO.addSong(songA);
            songsDAO.addSong(songB);
        }

        SongList songList = new SongList();
        songList.addSong(songA);
        songList.addSong(songB);
        songList.setUser(owner);
        songList.setIsPrivate(priv);
        songList.setName(listName);

        return songList;
    }

    public void uploadSonglist(SongList songList, String token) {
        try {
            mockMvcSongListController.perform(post("/songLists")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(asJsonString(songList)).header(HttpHeaders.AUTHORIZATION, token));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("COULD NOT UPLOAD SONGLIST!");
        }
    }

    @Test
    public void GETxml() throws Exception {
        String ownerElena = "eschuler";
        String listNameElena = "ElenasPrivate";
        boolean privElena = true;

        this.uploadSonglist(this.createSongList(ownerElena, listNameElena, privElena), eschulerToken);

        System.out.println(eschulerToken + " schulerToken");
        mockMvcSongListController.perform(get("/songLists/1")
                .header(HttpHeaders.AUTHORIZATION, eschulerToken)
                .accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML_VALUE));
    }

    @Test
    public void GETjson() throws Exception {
        String ownerElena = "eschuler";
        String listNameElena = "ElenasPrivate";
        boolean privElena = true;

        this.uploadSonglist(this.createSongList(ownerElena, listNameElena, privElena), eschulerToken);

        System.out.println(eschulerToken + " schulerToken");
        mockMvcSongListController.perform(get("/songLists/1")
                .header(HttpHeaders.AUTHORIZATION, eschulerToken)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void GETunknownUser_shouldReturnNotFound() throws Exception {
        mockMvcSongListController.perform(get("/songLists?userId=unkownUser")
                .header(HttpHeaders.AUTHORIZATION, eschulerToken))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }



    // /songLists/{id}  (songlist-id)
    @Test
    public void GETById() throws Exception {
        String ownerElena = "eschuler";
        String listNameElena = "ElenasPrivate";
        boolean privElena = true;

        this.uploadSonglist(this.createSongList(ownerElena, listNameElena, privElena), eschulerToken);

        System.out.println(eschulerToken + " schulerToken");
        mockMvcSongListController.perform(get("/songLists/1")
                .header(HttpHeaders.AUTHORIZATION, eschulerToken))
                .andExpect(status().is(HttpStatus.ACCEPTED.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ownerId").value("eschuler"))
                .andExpect(jsonPath("$.name").value("ElenasPrivate"))
                .andExpect(jsonPath("$.isPrivate").value(true))
                .andExpect(jsonPath("$.songList[0].id").value(1))
                .andExpect(jsonPath("$.songList[0].title").value("We Built This City"))
                .andExpect(jsonPath("$.songList[0].artist").value("Starship"))
                .andExpect(jsonPath("$.songList[0].label").value("Grunt/RCA"))
                .andExpect(jsonPath("$.songList[0].released").value(1985))
                .andExpect(jsonPath("$.songList[1].id").value(2))
                .andExpect(jsonPath("$.songList[1].title").value("Sussudio"))
                .andExpect(jsonPath("$.songList[1].artist").value("Phil Collins"))
                .andExpect(jsonPath("$.songList[1].label").value("Virgin"))
                .andExpect(jsonPath("$.songList[1].released").value(1985));
    }

    // /songLists/{ownerId}

    /**
     * Songs sind JEDES mal in anderer Reihenfolge.. race condition?
     * @throws Exception
     */
    @Test
    public void GETByOwnerId_shouldGet2Lists_privateAndPublic() throws Exception {
        String ownerElena = "eschuler";
        String listNameElena = "ElenasPrivate";
        boolean privElena = true;
        boolean privElena_no = false;

        this.uploadSonglist(this.createSongList(ownerElena, listNameElena + "1", privElena), eschulerToken);
        this.uploadSonglist(this.createSongList(ownerElena, listNameElena + "2", privElena_no), eschulerToken);

        mockMvcSongListController.perform(get("/songLists?userId=" + ownerElena)
                .header(HttpHeaders.AUTHORIZATION, eschulerToken))
                .andExpect(status().is(HttpStatus.ACCEPTED.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].ownerId").value(ownerElena))
                .andExpect(jsonPath("$[0].name").value(listNameElena + "1"))
                .andExpect(jsonPath("$[0].isPrivate").value(true))
                //Songlist 1
                .andExpect(jsonPath("$[0].songList[0].id").value(1))
                .andExpect(jsonPath("$[0].songList[0].title").value("We Built This City"))
                .andExpect(jsonPath("$[0].songList[0].artist").value("Starship"))
                .andExpect(jsonPath("$[0].songList[0].label").value("Grunt/RCA"))
                .andExpect(jsonPath("$[0].songList[0].released").value(1985))
                .andExpect(jsonPath("$[0].songList[1].id").value(2))
                .andExpect(jsonPath("$[0].songList[1].title").value("Sussudio"))
                .andExpect(jsonPath("$[0].songList[1].artist").value("Phil Collins"))
                .andExpect(jsonPath("$[0].songList[1].label").value("Virgin"))
                .andExpect(jsonPath("$[0].songList[1].released").value(1985))
                //Songlist 2
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].ownerId").value(ownerElena))
                .andExpect(jsonPath("$[1].name").value(listNameElena + "2"))
                .andExpect(jsonPath("$[1].isPrivate").value(false))
                .andExpect(jsonPath("$[1].songList[0].id").value(1))
                .andExpect(jsonPath("$[1].songList[0].title").value("We Built This City"))
                .andExpect(jsonPath("$[1].songList[0].artist").value("Starship"))
                .andExpect(jsonPath("$[1].songList[0].label").value("Grunt/RCA"))
                .andExpect(jsonPath("$[1].songList[0].released").value(1985))
                .andExpect(jsonPath("$[1].songList[1].id").value(2))
                .andExpect(jsonPath("$[1].songList[1].title").value("Sussudio"))
                .andExpect(jsonPath("$[1].songList[1].artist").value("Phil Collins"))
                .andExpect(jsonPath("$[1].songList[1].label").value("Virgin"))
                .andExpect(jsonPath("$[1].songList[1].released").value(1985));
    }

    // /songLists/{ownerId}

    /**
     * mmuster greift auf alle playlisten von eschuler zu.
     * mmuster kann nur eine - playlist 2 - sehen, da nur diese public ist
     *
     * @throws Exception
     */
    @Test
    public void GETByForeignOwnerId_shouldGet1Lists_becauseSecondListIsPrivate() throws Exception {
        String ownerElena = "eschuler";
        String listNameElena = "ElenasPrivate";
        boolean privElena = true;
        boolean privElena_no = false;

        this.uploadSonglist(this.createSongList(ownerElena, listNameElena + "1", privElena), eschulerToken);
        this.uploadSonglist(this.createSongList(ownerElena, listNameElena + "2", privElena_no), eschulerToken);

        mockMvcSongListController.perform(get("/songLists?userId=" + ownerElena)
                .header(HttpHeaders.AUTHORIZATION, mmusterToken))
                .andExpect(status().is(HttpStatus.ACCEPTED.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].ownerId").value(ownerElena))
                .andExpect(jsonPath("$[0].name").value(listNameElena + "2"))
                .andExpect(jsonPath("$[0].isPrivate").value(false))
                .andExpect(jsonPath("$[0].songList[0].id").value(1))
                .andExpect(jsonPath("$[0].songList[0].title").value("We Built This City"))
                .andExpect(jsonPath("$[0].songList[0].artist").value("Starship"))
                .andExpect(jsonPath("$[0].songList[0].label").value("Grunt/RCA"))
                .andExpect(jsonPath("$[0].songList[0].released").value(1985))
                .andExpect(jsonPath("$[0].songList[1].id").value(2))
                .andExpect(jsonPath("$[0].songList[1].title").value("Sussudio"))
                .andExpect(jsonPath("$[0].songList[1].artist").value("Phil Collins"))
                .andExpect(jsonPath("$[0].songList[1].label").value("Virgin"))
                .andExpect(jsonPath("$[0].songList[1].released").value(1985))
                .andExpect(jsonPath("$.length()").value(1)); //Nur eine Songliste - nicht 2 duerfen geschickt werden
    }

    @Test
    public void GETByForeignId_shouldGetHttpForbidden() throws Exception {
        String ownerElena = "eschuler";
        String listNameElena = "ElenasPrivate";
        boolean privElena = true;

        this.uploadSonglist(this.createSongList(ownerElena, listNameElena, privElena), eschulerToken);

        mockMvcSongListController.perform(get("/songLists/1")
                .header(HttpHeaders.AUTHORIZATION, mmusterToken))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void GETById_shouldGetHttpAccepted() throws Exception {
        String ownerElena = "eschuler";
        String listNameElena = "ElenasPrivate";
        boolean privElena = true;

        this.uploadSonglist(this.createSongList(ownerElena, listNameElena, privElena), eschulerToken);

        mockMvcSongListController.perform(get("/songLists/1")
                .header(HttpHeaders.AUTHORIZATION, eschulerToken))
                .andExpect(status().is(HttpStatus.ACCEPTED.value()));
    }

    // /songLists
    @Test
    public void POSTSonglistGood_shouldReturnLocationHeaderWithSonglistId() throws Exception {
        String ownerElena = "eschuler";
        String listNameElena = "ElenasPrivate";
        boolean privElena = true;

        SongList songList = this.createSongList(ownerElena, listNameElena, privElena);

        //TODO Beim LocationHeader soll die ID der erstellten Songliste zurueckgegeben werden

        mockMvcSongListController.perform(post("/songLists")
                .header(HttpHeaders.AUTHORIZATION, eschulerToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(songList)))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(redirectedUrl("/songLists/1"));
    }

    // /songLists
    @Test
    public void POSTSonglistGood_shouldSafeAllSongs() throws Exception {
        String ownerElena = "eschuler";
        String listNameElena = "ElenasPrivate";
        boolean privElena = true;

        SongList songList = this.createSongList(ownerElena, listNameElena, privElena);

        //TODO Beim LocationHeader soll die ID der erstellten Songliste zurueckgegeben werden

        mockMvcSongListController.perform(post("/songLists")
                .header(HttpHeaders.AUTHORIZATION, eschulerToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(songList)))
                .andExpect(status().is(HttpStatus.CREATED.value()));

        mockMvcSongListController.perform(get("/songLists/1")
                .header(HttpHeaders.AUTHORIZATION, eschulerToken))
                .andExpect(status().is(HttpStatus.ACCEPTED.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ownerId").value(ownerElena))
                .andExpect(jsonPath("$.name").value(listNameElena))
                .andExpect(jsonPath("$.isPrivate").value(true))
                .andExpect(jsonPath("$.songList[0].id").value(1))
                .andExpect(jsonPath("$.songList[0].title").value("We Built This City"))
                .andExpect(jsonPath("$.songList[0].artist").value("Starship"))
                .andExpect(jsonPath("$.songList[0].label").value("Grunt/RCA"))
                .andExpect(jsonPath("$.songList[0].released").value(1985))
                .andExpect(jsonPath("$.songList[1].id").value(2))
                .andExpect(jsonPath("$.songList[1].title").value("Sussudio"))
                .andExpect(jsonPath("$.songList[1].artist").value("Phil Collins"))
                .andExpect(jsonPath("$.songList[1].label").value("Virgin"))
                .andExpect(jsonPath("$.songList[1].released").value(1985));
    }

    @Test
    public void POSTSonglistBad_shouldReturnBadRequest_ifOneSongNotExist() throws Exception {
            String ownerElena = "eschuler";
            String listNameElena = "ElenasPrivate";
            boolean privElena = true;

            SongList songList = this.createSongList(ownerElena, listNameElena, privElena);
            songList.addSong(new Song().builder().withArtist("AARTISTWHICHNOTEXIST").withTitle("ANOTHERTITLE").withLabel("HI").withReleased(124124).build());

            mockMvcSongListController.perform(post("/songLists")
                    .header(HttpHeaders.AUTHORIZATION, eschulerToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(asJsonString(songList)))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    // /songLists/{id}
    @Test
    public void DELETE_good() throws Exception {
        String ownerElena = "eschuler";
        String listNameElena = "ElenasPrivate";
        boolean privElena = true;
        boolean privElena_no = false;

        this.uploadSonglist(this.createSongList(ownerElena, listNameElena + "1", privElena), eschulerToken);
        this.uploadSonglist(this.createSongList(ownerElena, listNameElena + "2", privElena_no), eschulerToken);

        mockMvcSongListController.perform(delete("/songLists/1")
                .header(HttpHeaders.AUTHORIZATION, eschulerToken))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));

        mockMvcSongListController.perform(delete("/songLists/2")
                .header(HttpHeaders.AUTHORIZATION, eschulerToken))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));
    }

    // /songLists/{id}
    @Test
    public void DELETE_tryToDeleteForeignPrivateSonglist() throws Exception {
        String ownerElena = "eschuler";
        String listNameElena = "ElenasPrivate";
        boolean privElena = true;

        this.uploadSonglist(this.createSongList(ownerElena, listNameElena, privElena), eschulerToken);

        mockMvcSongListController.perform(delete("/songLists/1")
                .header(HttpHeaders.AUTHORIZATION, mmusterToken))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    // /songLists/{id}
    @Test
    public void DELETE_tryToDeleteForeignPublicSonglist() throws Exception {
        String ownerElena = "eschuler";
        String listNameElena = "ElenasPrivate";
        boolean privElena = false;

        this.uploadSonglist(this.createSongList(ownerElena, listNameElena, privElena), eschulerToken);

        mockMvcSongListController.perform(delete("/songLists/1")
                .header(HttpHeaders.AUTHORIZATION, mmusterToken))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }
}