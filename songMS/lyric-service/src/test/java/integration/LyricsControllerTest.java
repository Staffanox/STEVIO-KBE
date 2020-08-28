package integration;

import htwb.ai.steven.LyricsRepository;
import htwb.ai.steven.LyricsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyricsService.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LyricsControllerTest {

    @Autowired
    MockMvc mockMvc;

/*
    @Test
    public void getSongsShouldReturn200AndAllSongs() throws Exception {
        MvcResult result = mockMvc.perform(get("/lyrics"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = (result.getResponse().getContentAsString());
        System.out.println(content);
    }*/
}
