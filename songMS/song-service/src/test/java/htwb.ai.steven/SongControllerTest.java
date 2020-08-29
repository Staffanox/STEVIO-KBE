package htwb.ai.steven;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SongServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SongControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getSongsShouldReturn200AndAllSongs() throws Exception {

        MvcResult result = mockMvc.perform(get("/songs"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = (result.getResponse().getContentAsString());
        List<Song> songs = Arrays.asList(new ObjectMapper().readValue(content, Song[].class));
        Assert.assertTrue(songs.size() > 0);
    }


    @Test
    public void getSecondSongShouldReturn200() throws Exception {
        mockMvc.perform(get("/songs/2"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

       @Test
    public void getMissingSongShouldReturn404() throws Exception {
        mockMvc.perform(get("/songs/1005000"))
                .andExpect(status().is(404));

    }

    @Test
    public void postValidSong() throws Exception {
        Song song = new Song(0, "7 years", "Lukas Graham", "Lukas Graham (Blue Album)", 2015);

        MvcResult response = mockMvc.perform(post("/songs").contentType(MediaType.APPLICATION_JSON).content(asJsonString(song)))
                .andExpect(status().is(201))
                .andReturn();
        String header = response.getResponse().getHeader("location");

        assert header != null;
        MvcResult getterResponse = mockMvc.perform(get(header))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        Song songInDB = asSong(getterResponse.getResponse().getContentAsString());

        Assert.assertEquals(song.getReleased(), songInDB.getReleased());
        Assert.assertEquals(song.getLabel(), songInDB.getLabel());
        Assert.assertEquals(song.getArtist(), songInDB.getArtist());
        Assert.assertEquals(song.getTitle(), songInDB.getTitle());


    }

    @Test
    public void postValidSongWithBuilder() throws Exception {
        Song song = Song.builder().withArtist("Starship").withLabel("Grunt/RCA").withReleased(1985).withTitle("We Built This City").build();
        MvcResult response = mockMvc.perform(post("/songs").contentType(MediaType.APPLICATION_JSON).content(asJsonString(song)))
                .andExpect(status().is(201))
                .andReturn();
        String header = response.getResponse().getHeader("location");

        assert header != null;
        MvcResult getterResponse = mockMvc.perform(get(header))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        Song songInDB = asSong(getterResponse.getResponse().getContentAsString());

        Assert.assertEquals(song.getReleased(), songInDB.getReleased());
        Assert.assertEquals(song.getLabel(), songInDB.getLabel());
        Assert.assertEquals(song.getArtist(), songInDB.getArtist());
        Assert.assertEquals(song.getTitle(), songInDB.getTitle());
    }


    @Test
    public void postSongWithoutTitle() throws Exception {
        Song song = new Song(0, "", "Lukas Graham", "Lukas Graham (Blue Album)", 2015);

        mockMvc.perform(post("/songs").contentType(MediaType.APPLICATION_JSON).content(asJsonString(song)))
                .andExpect(status().is(400))
                .andReturn();

    }

    @Test
    public void postSongWithoutArtist() throws Exception {
        Song song = new Song(0, "7 years", "", "Lukas Graham (Blue Album)", 2015);

        MvcResult response = mockMvc.perform(post("/songs").contentType(MediaType.APPLICATION_JSON).content(asJsonString(song)))
                .andExpect(status().is(201))
                .andReturn();

        String header = response.getResponse().getHeader("location");

        assert header != null;
        MvcResult getterResponse = mockMvc.perform(get(header))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        Song songInDB = asSong(getterResponse.getResponse().getContentAsString());

        Assert.assertEquals(song.getReleased(), songInDB.getReleased());
        Assert.assertEquals(song.getLabel(), songInDB.getLabel());
        Assert.assertEquals(song.getArtist(), songInDB.getArtist());
        Assert.assertEquals(song.getTitle(), songInDB.getTitle());

    }

    @Test
    public void postSongWithoutLabel() throws Exception {
        Song song = new Song(0, "7 years", "Lukas Graham", "", 2015);

        MvcResult response = mockMvc.perform(post("/songs").contentType(MediaType.APPLICATION_JSON).content(asJsonString(song)))
                .andExpect(status().is(201))
                .andReturn();

        String header = response.getResponse().getHeader("location");

        assert header != null;
        MvcResult getterResponse = mockMvc.perform(get(header))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        Song songInDB = asSong(getterResponse.getResponse().getContentAsString());

        Assert.assertEquals(song.getReleased(), songInDB.getReleased());
        Assert.assertEquals(song.getLabel(), songInDB.getLabel());
        Assert.assertEquals(song.getArtist(), songInDB.getArtist());
        Assert.assertEquals(song.getTitle(), songInDB.getTitle());

    }

    @Test
    public void postSongWithInvalidReleased() throws Exception {
        Song song = new Song(0, "7 years", "Lukas Graham", "Lukas Graham (Blue Album)", -1);

        mockMvc.perform(post("/songs").contentType(MediaType.APPLICATION_JSON).content(asJsonString(song)))
                .andExpect(status().is(400))
                .andReturn();

    }


    @Test
    public void postWrongUrl() throws Exception {
        Song song = new Song(0, "7 years", "Lukas Graham", "Lukas Graham (Blue Album)", 2015);

        mockMvc.perform(post("/song").contentType(MediaType.APPLICATION_JSON).content(asJsonString(song)))
                .andExpect(status().is(404))
                .andReturn();
    }


    @Test
    public void deleteValidEntry() throws Exception {

        //delete entry
        mockMvc.perform(delete("/songs/1"))
                .andExpect(status().is(204));

        //try to get it
        mockMvc.perform(get("/songs/1"))
                .andExpect(status().is(404));
    }

    @Test
    public void deleteInvalidEntry() throws Exception {
        mockMvc.perform(delete("/song/10000"))
                .andExpect(status().is(404));

    }

    @Test
    public void deleteWrongUrl() throws Exception {

        //delete entry
        mockMvc.perform(delete("/song/1"))
                .andExpect(status().is(404));

    }

    @Test
    public void putValidSong() throws Exception {
        Song changedSong = Song.builder().withId(2).withTitle("Changed").build();
        mockMvc.perform(put("/songs/2").contentType(MediaType.APPLICATION_JSON).content(asJsonString(changedSong)
        ))
                .andExpect(status().is(204));
    }

    @Test
    public void putMismatchingID() throws Exception {
        Song changedSong = Song.builder().withId(10).withTitle("Changed").build();
        mockMvc.perform(put("/songs/2").contentType(MediaType.APPLICATION_JSON).content(asJsonString(changedSong)
        ))
                .andExpect(status().is(400));
    }

    @Test
    public void putInvalidTitle() throws Exception {
        Song changedSong = Song.builder().withId(10).withTitle("").build();
        mockMvc.perform(put("/songs/2").contentType(MediaType.APPLICATION_JSON).content(asJsonString(changedSong)
        ))
                .andExpect(status().is(400));
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Song asSong(String returnedObject) {
        try {
            return new ObjectMapper().readValue(returnedObject, Song.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}


