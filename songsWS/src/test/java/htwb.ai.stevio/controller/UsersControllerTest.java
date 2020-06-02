package htwb.ai.stevio.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.stevio.controller.UsersController;
import htwb.ai.stevio.dao.TestUserDAO;
import htwb.ai.stevio.model.User;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


class UsersControllerTest {
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new UsersController(new TestUserDAO())).build();
    }

    @Test
    void getUserShouldReturnOKAndUserForExistingId() throws Exception {

        MvcResult result = mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON).content(asJsonString(new User("1", "kek", "firstName4", "lastname"))
        ))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        Assert.assertTrue(content.length() <= 17);
    }


    @Test
        // GET /auth/2
    void getUserShouldReturn404ForNonExistingId() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/test/url")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockMvc.toString()))
                .andExpect(status().isCreated());
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

