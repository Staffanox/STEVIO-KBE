package htwb.ai.steven;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    private static String token;


    @Test
    public void successfulAuth() throws Exception {
        User user = new User("mmuster", "pass1234", "Maxime", "Muster");
        MvcResult result = mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andReturn();

        String body = result.getResponse().getContentAsString();
        token = body;
        Assert.assertEquals(body.length(), 17);

    }

    @Test
    public void wrongPasswordAuth() throws Exception {
        User user = new User("mmuster", "pass134", "Maxime", "Muster");
        mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andExpect(status().is(401));
    }

    @Test
    public void wrongUsernameAuth() throws Exception {
        User user = new User("muster", "pass1234", "Maxime", "Muster");
        mockMvc.perform(post("/auth/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andExpect(status().is(404));
    }

    @Test
    public void getUserNameByToken() throws Exception {
        MvcResult result = mockMvc.perform(get("/auth/" + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andReturn();

        String username = result.getResponse().getContentAsString();
        Assert.assertEquals("mmuster", username);

    }

    @Test
    public void getUserNameWithWrongToken() throws Exception {
        MvcResult result = mockMvc.perform(get("/auth/" + "lul")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andReturn();
    }


    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
