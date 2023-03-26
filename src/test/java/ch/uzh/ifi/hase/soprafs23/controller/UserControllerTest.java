package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entityDB.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void createUser_validInput_userCreated() throws Exception {
        // given
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");

        given(userService.createUser(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(user.getUserId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
    }


    @Test
    public void createUser_validInput_userAlreadyExists() throws Exception {
        // given
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");

        given(userService.createUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));
        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());
    }


    @Test
    public void getUserById_validInput_userFound() throws Exception {
        // given
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        given(userService.getUserByIdGeneralAuth(Mockito.anyLong(), Mockito.anyString())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/users/{userId}", user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "1");

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getUserId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
    }

    @Test
    public void getUserById_validInput_userNotFound() throws Exception {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        given(userService.getUserByIdGeneralAuth(Mockito.anyLong(), Mockito.anyString())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder getRequest = get("/users/{userId}", user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "1");

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());

    }

    @Test
    public void putUser_validInput_updateUser() throws Exception {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);


        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setStatus(UserStatus.OFFLINE);
        userPutDTO.setUsername("changedUsername");
        userPutDTO.setBirthday(new Date("01/01/2000"));

        MockHttpServletRequestBuilder putRequest = put("/users/{userId}", user.getUserId())
                .content(asJsonString(userPutDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "1");

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    public void putUser_validInput_userNotFound() throws Exception {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setStatus(UserStatus.OFFLINE);
        userPutDTO.setUsername("changedUsername");
        userPutDTO.setBirthday(new Date("01/01/2000"));

        given(userService.updateUser(Mockito.anyLong(), Mockito.anyString(), Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder putRequest = put("/users/{userId}", user.getUserId())
                .content(asJsonString(userPutDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "1");

        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound());
    }


    @Test
    public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
        // given
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);

        List<User> allUsers = Collections.singletonList(user);

        // this mocks the UserService -> we define above what the userService should
        // return when getUsers() is called
        String token = "1";
        given(userService.getUsers(token)).willReturn(allUsers);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].status", is(user.getStatus().toString())));
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