package ch.uzh.ifi.hase.soprafs23.integrationTests;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entityDB.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateUsers() throws Exception {
        // create user entities
        User userEntity1 = new User();
        userEntity1.setUsername("john_doe1");
        userEntity1.setPassword("password123");

        User userEntity2 = new User();
        userEntity2.setUsername("john_doe2");
        userEntity2.setPassword("password123");


        //Makes the call to the Service
        userService.createUser(userEntity1);

        userService.createUser(userEntity2);


        // verify that the user was saved to the database
        User savedUser1 = userRepository.findByUsername(userEntity1.getUsername());
        assertEquals(userEntity1.getUsername(), savedUser1.getUsername());
        assertEquals(userEntity1.getPassword(), savedUser1.getPassword());

        User savedUser2 = userRepository.findByUsername(userEntity2.getUsername());
        assertEquals(userEntity2.getUsername(), savedUser2.getUsername());
        assertEquals(userEntity2.getPassword(), savedUser2.getPassword());
    }

    @Test
    public void testUpdateUser() throws Exception {
        // create user entity
        User userEntity = new User();
        userEntity.setUsername("john_doe");
        userEntity.setPassword("password123");
        userEntity.setNationality("US");
        userEntity.setGamesWon(10);
        userService.createUser(userEntity);

        // update user with new data
        User updatedUser = new User();
        updatedUser.setUsername("jane_doe_new");
        updatedUser.setPassword("new_password");
        updatedUser.setNationality("Canada");
        updatedUser.setGamesWon(15);

        boolean success = userService.updateUser(userEntity.getUserId(), userEntity.getToken(), updatedUser);
        assertTrue(success);

        // verify that the user was updated in the database
        User savedUser = userRepository.findByUsername(updatedUser.getUsername());
        assertNotNull(savedUser);
        assertEquals(updatedUser.getUsername(), savedUser.getUsername());
        assertEquals(updatedUser.getPassword(), savedUser.getPassword());
        assertEquals(updatedUser.getNationality(), savedUser.getNationality());
        assertEquals(updatedUser.getGamesWon(), savedUser.getGamesWon());
    }

    @Test
    public void testLoginUser() throws Exception {
        // create user entity
        User userEntity = new User();
        userEntity.setUsername("john_doe");
        userEntity.setPassword("password123");
        userEntity.setCreation_date(new Date());
        userEntity.setStatus(UserStatus.OFFLINE);
        userEntity.setToken("Test_Token");
        userEntity.setUserId(99999L);

        // add user to the database
        userRepository.save(userEntity);

        // create login user object
        User loginUser = new User();
        loginUser.setUsername("john_doe");
        loginUser.setPassword("password123");

        // login user and verify user status
        User loggedUser = userService.loginUser(loginUser);
        assertEquals(UserStatus.ONLINE, loggedUser.getStatus());

        // verify that the user's status was saved to the database
        User savedUser = userRepository.findByUsername(userEntity.getUsername());
        assertEquals(UserStatus.ONLINE, savedUser.getStatus());
    }


    // utility method to convert objects to JSON strings
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
