package htwb.ai.steven;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyricsService.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LyricControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    LyricsRepository lyricsRepository;


    @Before
    public void setUp() {
        String lyric = "test";

        Lyrics sampleLyrics = new Lyrics("How to Save a Life", lyric);
        Lyrics lyricsToBeDeleted = new Lyrics("Will be deleted in test", lyric);

        lyricsRepository.save(sampleLyrics);
        lyricsRepository.save(lyricsToBeDeleted);

    }

    @Test
    public void getSongsShouldReturn200AndAllSongs() throws Exception {

        MvcResult result = mockMvc.perform(get("/lyrics"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = (result.getResponse().getContentAsString());
        System.out.println(content);
    }

    @Test
    public void getSingleLyrics() throws Exception {
        String title = "How to Save a Life";
        MvcResult result = mockMvc.perform(get("/lyrics/How to Save a Life"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = (result.getResponse().getContentAsString());
        Lyrics lyrics = asLyrics(content);

        Assert.assertEquals(title, lyrics.getId());
        Assert.assertEquals("test", lyrics.getLyrics());

    }

    @Test
    public void getInvalidLyrics() throws Exception {
        mockMvc.perform(get("/lyrics/ "))
                .andExpect(status().is(404));

    }

    @Test
    public void postValidLyrics() throws Exception {
        Lyrics tempLyrics = new Lyrics("Test", "test");

        mockMvc.perform(post("/lyrics").contentType(MediaType.APPLICATION_JSON).content(asJsonString(tempLyrics)))
                .andExpect(status().is(201))
                .andReturn();


    }

    @Test
    public void postExistingLyrics() throws Exception {
        Lyrics tempLyrics = new Lyrics("How to Save a Life", "changedLyrics");

        mockMvc.perform(post("/lyrics").contentType(MediaType.APPLICATION_JSON).content(asJsonString(tempLyrics)))
                .andExpect(status().is(201))
                .andReturn();
    }
   @Test
    public void postInvalidLyrics() throws Exception {
        Lyrics tempLyrics = new Lyrics(null, null);

        mockMvc.perform(post("/lyrics").contentType(MediaType.APPLICATION_JSON).content(asJsonString(tempLyrics)))
                .andExpect(status().is(400))
                .andReturn();
    }

    @Test
    public void deleteExistingLyrics() throws Exception{
        mockMvc.perform(delete("/lyrics/Will be deleted in test"))
                .andExpect(status().is(204))
                .andReturn();
        mockMvc.perform(get("/lyrics/Will be deleted in test"))
                .andExpect(status().is(404))
                .andReturn();
    }


    @Test
    public void deleteNonExistentLyrics() throws Exception {
        mockMvc.perform(delete("/lyrics/How to take a life"))
                .andExpect(status().is(404))
                .andReturn();
    }

    @Test
    public void changeLyrics() throws Exception {
        Lyrics tempLyrics = new Lyrics("How to Save a Life", "changedLyrics");

        mockMvc.perform(put("/lyrics/How to Save a Life").contentType(MediaType.APPLICATION_JSON).content(asJsonString(tempLyrics)))
                .andExpect(status().is(204))
                .andReturn();

    }

    @Test
    public void differentLyricsInPayloadAndBody() throws Exception {
        Lyrics tempLyrics = new Lyrics("How to Sae a Life", "changedLyrics");

        mockMvc.perform(put("/lyrics/How to Save a Life").contentType(MediaType.APPLICATION_JSON).content(asJsonString(tempLyrics)))
                .andExpect(status().is(400))
                .andReturn();

    }

    @Test
    public void invalidLyricsInPut() throws Exception {
        Lyrics tempLyrics = new Lyrics("How", "changedLyrics");

        mockMvc.perform(put("/lyrics/How").contentType(MediaType.APPLICATION_JSON).content(asJsonString(tempLyrics)))
                .andExpect(status().is(400))
                .andReturn();

    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Lyrics asLyrics(String returnedObject) {
        try {
            return new ObjectMapper().readValue(returnedObject, Lyrics.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}







