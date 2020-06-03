package htwb.ai.stevio.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.stevio.controller.SongsController;
import htwb.ai.stevio.dao.TestSongDAO;
import htwb.ai.stevio.model.Song;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


class SongsControllerTest {
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new SongsController(new TestSongDAO())).build();
    }

    @Test
        // GET /songs
    void getSongsShouldReturn200AndAllSongs() throws Exception {
        MvcResult result = mockMvc.perform(get("/songs"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].title").value("7 Years"))
                .andExpect(jsonPath("$[0].artist").value("Lukas Graham"))
                .andExpect(jsonPath("$[0].label").value("Lukas Graham (Blue Album)"))
                .andExpect(jsonPath("$[0].released").value(2015))
                .andReturn();

        String content = (result.getResponse().getContentAsString());
        Assert.assertTrue(content.contains("title") && content.contains("id"));
    }

    @Test
        // GET /songs
        // DEL /songs/10
    void getAllSongsWithoutAnySongs() throws Exception {
        mockMvc.perform(delete("/songs/10"))
                .andExpect(status().is(204));
        MvcResult result = mockMvc.perform(get("/songs"))
                .andExpect(status().is(400))
                .andReturn();

        String content = (result.getResponse().getContentAsString());

        //[] is empty JSON
        Assert.assertEquals("[]", content);
    }


    @Test
        // GET /song
    void getFalseURIShouldReturn404() throws Exception {
        mockMvc.perform(get("/song"))
                .andExpect(status().is(404));

    }

    @Test
        // GET /songs/10
    void getFirstSongShouldReturn200AndSong() throws Exception {
        mockMvc.perform(get("/songs/10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("7 Years"))
                .andExpect(jsonPath("$.artist").value("Lukas Graham"))
                .andExpect(jsonPath("$.label").value("Lukas Graham (Blue Album)"))
                .andExpect(jsonPath("$.released").value(2015))
                .andReturn();
    }

    @Test
        // GET /songs/2
    void getSecondSongShouldReturn404() throws Exception {
        mockMvc.perform(get("/songs/2"))
                .andExpect(status().is(404));
    }


    @Test
        //POST /songs
    void postASongShouldReturn201() throws Exception {

        Song changedSong = Song.builder().withId(1).withTitle("Changed").build();
        mockMvc.perform(post("/songs").contentType(MediaType.APPLICATION_JSON).content(asJsonString(changedSong)
        ))
                .andExpect(status().is(201))
                .andExpect(header().string("Location", "/songs/1"));
    }

    @Test
        //POST /songs
    void postASongAsXML() throws Exception {

        Song changedSong = Song.builder().withId(1).withTitle("Changed").build();
        mockMvc.perform(post("/songs").contentType(MediaType.APPLICATION_XML).content(asJsonString(changedSong)
        ))
                .andExpect(status().is(415));
    }


    @Test
        //POST /songs
    void postSongWithExistingId() throws Exception {

        Song changedSong = Song.builder().withId(10).withTitle("Changed").build();
        mockMvc.perform(post("/songs").contentType(MediaType.APPLICATION_JSON).content(asJsonString(changedSong)
        ))
                .andExpect(status().is(201))
                .andExpect(header().string("Location", "/songs/10"));
    }


    @Test
        //POST songs
    void postSongWithoutTitleShouldReturn400() throws Exception {

        Song changedSong = Song.builder().build();
        mockMvc.perform(post("/songs").contentType(MediaType.APPLICATION_JSON).content(asJsonString(changedSong)
        ))
                .andExpect(status().is(400));
    }

    @Test
        //POST song
    void postWrongURLShouldReturn404() throws Exception {
        Song changedSong = Song.builder().withTitle("Test").withId(1).build();
        mockMvc.perform(post("/song").contentType(MediaType.APPLICATION_JSON).content(asJsonString(changedSong)
        ))
                .andExpect(status().is(404));
    }

    @Test
        //PUT /songs/10
        //GET /songs/10
    void putSongShouldReturn201() throws Exception {

        Song changedSong = Song.builder().withId(10).withTitle("Changed").build();
        mockMvc.perform(put("/songs/10").contentType(MediaType.APPLICATION_JSON).content(asJsonString(changedSong)
        ))
                .andExpect(status().is(204));

        MvcResult result = mockMvc.perform(get("/songs/10"))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        //Test if the given object and db object are equal
        Assert.assertEquals(content, asJsonString(changedSong));

    }

    @Test
        //PUT /songs/10
    void putSongAsXML() throws Exception {

        Song changedSong = Song.builder().withId(1).withTitle("Changed").build();
        mockMvc.perform(put("/songs/10").contentType(MediaType.APPLICATION_XML).content(asJsonString(changedSong)
        ))
                .andExpect(status().is(415));

    }


    @Test
        //PUT /songs/10
    void putDifferentIdInURLAndPayloadShouldReturn404() throws Exception {

        Song changedSong = Song.builder().withId(1).withTitle("Changed").build();
        mockMvc.perform(put("/songs/10").contentType(MediaType.APPLICATION_JSON).content(asJsonString(changedSong)
        ))
                .andExpect(status().is(400));

    }

    @Test
        //PUT /song/10
    void putWrongURLShouldReturn400() throws Exception {
        Song changedSong = Song.builder().withId(10).withTitle("Changed").build();
        mockMvc.perform(put("/song/10").contentType(MediaType.APPLICATION_JSON).content(asJsonString(changedSong)
        ))
                .andExpect(status().is(404));

    }

    @Test
        //DEL /songs/10
        //GET /songs/10
    void deleteShouldRemoveSongAndReturn204() throws Exception {
        mockMvc.perform(delete("/songs/10"))
                .andExpect(status().is(204));

        mockMvc.perform(get("/songs/10"))
                .andExpect(status().is(404));
    }

    @Test
        //DEL /songs/1
    void deleteWithMissingSongShouldReturn404() throws Exception {
        mockMvc.perform(delete("/songs/1"))
                .andExpect(status().is(404));
    }

    @Test
        //DEL /song/1
    void deleteWrongURLShouldReturn404() throws Exception {
        mockMvc.perform(delete("/song/1"))
                .andExpect(status().is(404));
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
