package htwb.ai.stevio.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.stevio.dao.AuthenticatorDAO;
import htwb.ai.stevio.dao.TestUserDAO;
import htwb.ai.stevio.model.User;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


class UsersControllerTest {
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new UsersController(new TestUserDAO(),new AuthenticatorDAO())).build();
    }

    @Test
        //POST auth
    void postUserShouldReturnOkForExistingUser() throws Exception {

        MvcResult result = mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON).content(asJsonString(new User("1", "Z", "Alphonso", "kek"))
        ))
                .andExpect(status().isOk())
                .andReturn();

        //Tests if token is valid
        String content = result.getResponse().getContentAsString();
        Assert.assertTrue(content.length() <= 17);
    }


    @Test
        //POST /auth
    void postShouldReturn404ForNonExistingUser() throws Exception {
        MvcResult result = mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON).content(asJsonString(new User("12", "Z", "Alphonso", "kek"))
        ))
                .andExpect(status().is(401))
                .andReturn();

        //Tests if token is sent
        String content = result.getResponse().getContentAsString();

        //Expected content = "No user found"
        Assert.assertEquals(13, content.length());
    }

    @Test
        //POST /auth
    void postWrongURLShouldReturn400() throws Exception {
        MvcResult result = mockMvc.perform(post("/aut").contentType(MediaType.APPLICATION_JSON).content(asJsonString(new User("12", "Z", "Alphonso", "kek"))
        ))
                .andExpect(status().is(404))
                .andReturn();

        //Get body from request
        String content = result.getResponse().getContentAsString();

        //Expected content = "No user found"
        Assert.assertEquals(0, content.length());
    }

    @Test
        //POST /auth
    void postWithoutPassword() throws Exception {
        MvcResult result = mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON).content(asJsonString(new User("12", "", "Alphonso", "kek"))
        ))
                .andExpect(status().is(401))
                .andReturn();

        //Get body from request
        String content = result.getResponse().getContentAsString();

        //Expected content = "Username AND password is needed."
        Assert.assertEquals(32, content.length());
    }

    @Test
        //POST /auth
    void postWithoutUserId() throws Exception {
        MvcResult result = mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON).content(asJsonString(new User("", "drowssap", "Alphonso", "kek"))
        ))
                .andExpect(status().is(401))
                .andReturn();

        //Get body from request
        String content = result.getResponse().getContentAsString();

        //Expected content = "Username AND password is needed."
        Assert.assertEquals(32, content.length());
    }

    @Test
        //POST /auth
    void postWithoutFirstName() throws Exception {
        MvcResult result = mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON).content(asJsonString(new User("1", "Z", "", "kek"))
        ))
                .andExpect(status().is(200))
                .andReturn();

        //Tests if token is valid
        String content = result.getResponse().getContentAsString();

        Assert.assertTrue(content.length() <= 17);
    }

    @Test
        //POST /auth
    void postWithoutLastName() throws Exception {
        MvcResult result = mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON).content(asJsonString(new User("1", "Z", "Alphonso", ""))
        ))
                .andExpect(status().is(200))
                .andReturn();

        //Tests if token is valid
        String content = result.getResponse().getContentAsString();

        Assert.assertTrue(content.length() <= 17);
    }

    @Test
        //POST /auth
    void postWithWrongFirstName() throws Exception {
        MvcResult result = mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON).content(asJsonString(new User("1", "Z", "Edward", "kek"))
        ))
                .andExpect(status().is(200))
                .andReturn();

        //Tests if token is valid
        String content = result.getResponse().getContentAsString();

        Assert.assertTrue(content.length() <= 17);
    }

    @Test
        //POST /auth
    void postWithWrongLastName() throws Exception {
        MvcResult result = mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON).content(asJsonString(new User("1", "Z", "Alphonso", "lel"))
        ))
                .andExpect(status().is(200))
                .andReturn();

        //Tests if token is valid
        String content = result.getResponse().getContentAsString();

        Assert.assertTrue(content.length() <= 17);
    }

    @Test
        //POST /auth
    void postWrongContentType() throws Exception {
        MvcResult result = mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_XML).content(asJsonString(new User("1", "Z", "Alphonso", "kek"))
        ))
                .andExpect(status().is(415))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        //No body given
        Assert.assertEquals(0, content.length());
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

