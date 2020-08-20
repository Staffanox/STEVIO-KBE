package htwb.ai.steven;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SongServiceApplication.class)
@AutoConfigureMockMvc
public class SongControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SongRepository songRepository;


    @Test
    public void getSongsShouldReturn200AndAllSongs() throws Exception {
        MvcResult result = mockMvc.perform(get("/songs"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = (result.getResponse().getContentAsString());
        System.out.println(content);
    }

    @Test
    public void postValidSong() throws Exception {
        Song song = new Song(1, "7 years", "Lukas Graham", "Lukas Graham (Blue Album)", 2015);

        mockMvc.perform(post("/songs").contentType(MediaType.APPLICATION_JSON).content(asJsonString(song)))
                .andExpect(status().is(201))
                .andReturn();

    }

     @Test
    public void postSongWithoutTitle() throws Exception {
        Song song = new Song(1, "", "Lukas Graham", "Lukas Graham (Blue Album)", 2015);

        mockMvc.perform(post("/song").contentType(MediaType.APPLICATION_JSON).content(asJsonString(song)))
                .andExpect(status().is(404))
                .andReturn();

    }

     @Test
    public void postSongWithoutArtist() throws Exception {
        Song song = new Song(1, "7 years", "", "Lukas Graham (Blue Album)", 2015);

        mockMvc.perform(post("/song").contentType(MediaType.APPLICATION_JSON).content(asJsonString(song)))
                .andExpect(status().is(404))
                .andReturn();

    }

       @Test
    public void postSongWithoutLabel() throws Exception {
        Song song = new Song(1, "7 years", "Lukas Graham", "", 2015);

        mockMvc.perform(post("/song").contentType(MediaType.APPLICATION_JSON).content(asJsonString(song)))
                .andExpect(status().is(404))
                .andReturn();

    }

    @Test
    public void postSongWithInvalidReleased() throws Exception {
        Song song = new Song(1, "7 years", "Lukas Graham", "Lukas Graham (Blue Album)", -1);

        mockMvc.perform(post("/songs").contentType(MediaType.APPLICATION_JSON).content(asJsonString(song)))
                .andExpect(status().is(201))
                .andReturn();

    }


     @Test
    public void postWrongUrl() throws Exception {
        Song song = new Song(1, "7 years", "Lukas Graham", "Lukas Graham (Blue Album)", 2015);

        mockMvc.perform(post("/song").contentType(MediaType.APPLICATION_JSON).content(asJsonString(song)))
                .andExpect(status().is(404))
                .andReturn();

    }








    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


