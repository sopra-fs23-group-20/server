package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPutDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Random;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerAcceptanceTest {
    String token = "1234";
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("beforeEachTestPassword");
        userPostDTO.setUsername(generateRandomUsername(12));

        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(userPostDTO.getUsername())))
                .andExpect(jsonPath("$.status", is(UserStatus.ONLINE.toString())))
                .andDo(result -> {
                    token = result.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
                });
    }

    @Test
    public void postUserValid() throws Exception {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("testpaswword");
        userPostDTO.setUsername(generateRandomUsername(6));

        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(userPostDTO.getUsername())))
                .andExpect(jsonPath("$.status", is(UserStatus.ONLINE.toString())));
    }

    public static String generateRandomUsername(int length) {
        // Characters allowed in the username
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-";

        // Create a Random object to generate random numbers
        Random random = new Random();

        // Create a StringBuilder to build the username
        StringBuilder username = new StringBuilder();

        // Generate the random username by picking random characters from the allowedChars
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allowedChars.length());
            char randomChar = allowedChars.charAt(randomIndex);
            username.append(randomChar);
        }

        return "TestUser" + username.toString();
    }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e));
        }
    }

}
/*
    @Test
    public void postUserInValid() throws Exception {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("testPassword");
        userPostDTO.setUsername("beforeEachTestUsername");

        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());
    }

    @Test
    public void loginValid() throws Exception {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("beforeEachTestPassword");
        userPostDTO.setUsername("beforeEachTestUsername");

        MockHttpServletRequestBuilder postRequest = post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isAccepted());
    }

    @Test
    public void loginUnauthorized() throws Exception {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("Wrong Password");
        userPostDTO.setUsername("beforeEachTestUsername");

        MockHttpServletRequestBuilder postRequest = post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void loginNotKnown() throws Exception {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("beforeEachTestPassword");
        userPostDTO.setUsername("Unknown Username");

        MockHttpServletRequestBuilder postRequest = post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());
    }


    @Test
    public void putUserValid() throws Exception {
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("newTestUsername");


        HttpHeaders h1 = new HttpHeaders();
        h1.add("Authorization", token);

        MockHttpServletRequestBuilder putRequest = put("/users/1")
                .headers(h1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());

        MockHttpServletRequestBuilder getRequest = get("/users/1").contentType(MediaType.APPLICATION_JSON).headers(h1);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(userPutDTO.getUsername())));
    }

    @Test
    public void putUserInValid() throws Exception {
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("beforeEachTestUsername2");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("beforeEachTestPassword2");
        userPostDTO.setUsername("beforeEachTestUsername2");

        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());

        HttpHeaders h1 = new HttpHeaders();
        h1.add("Authorization", token);

        MockHttpServletRequestBuilder putRequest = put("/users/1")
                .headers(h1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));

        mockMvc.perform(putRequest)
                .andExpect(status().isConflict());

    }

    @Test
    public void putUserNoAccess() throws Exception {
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("beforeEachTestUsername2");


        HttpHeaders h1 = new HttpHeaders();
        h1.add("Authorization", "1234");

        MockHttpServletRequestBuilder putRequest = put("/users/1")
                .headers(h1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));

        mockMvc.perform(putRequest)
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void getAllUsersValid() throws Exception {
        HttpHeaders h1 = new HttpHeaders();
        h1.add("Authorization", token);

        MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON).headers(h1);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].username", is("beforeEachTestUsername")))
                .andExpect(jsonPath("$[0].status", is(UserStatus.ONLINE.toString())));
    }

    @Test
    public void getAllUsersInValid() throws Exception {
        HttpHeaders h1 = new HttpHeaders();
        h1.add("Authorization", "NotValidToken");

        MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON).headers(h1);

        mockMvc.perform(getRequest)
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getSingleUserValid() throws Exception {
        HttpHeaders h1 = new HttpHeaders();
        h1.add("Authorization", token);

        MockHttpServletRequestBuilder getRequest = get("/users/1").contentType(MediaType.APPLICATION_JSON).headers(h1);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("beforeEachTestUsername")))
                .andExpect(jsonPath("$.status", is(UserStatus.ONLINE.toString())));
    }

    @Test
    public void getSingleUserUnauthorized() throws Exception {
        HttpHeaders h1 = new HttpHeaders();
        h1.add("Authorization", "NotValidToken");

        MockHttpServletRequestBuilder getRequest = get("/users/1").contentType(MediaType.APPLICATION_JSON).headers(h1);
        mockMvc.perform(getRequest)
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getSingleUserNotFound() throws Exception {
        HttpHeaders h1 = new HttpHeaders();
        h1.add("Authorization", token);

        MockHttpServletRequestBuilder getRequest = get("/users/2").contentType(MediaType.APPLICATION_JSON).headers(h1);

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }




}
*/