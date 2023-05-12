package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entityDB.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setPassword("testPassword");
        testUser.setUsername("testUsername");
        testUser.setCreation_date(new Date());
        testUser.setStatus(UserStatus.OFFLINE);
        testUser.setNationality("testNationality");
        testUser.setBirthDate(new Date());
        testUser.setProfilePicture("testProfilePicture");
        testUser.setToken("testToken");

        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        when(userRepository.save(Mockito.any())).thenReturn(testUser);


    }

    @Test
    public void getUserByIdGeneralAuth_validIdAndAuthHeader_returnsUser() {
        // given -> a valid user ID and auth header
        Long userId = 1L;
        String authHeader = "validToken";

        // mock the UserRepository to return the test user for the auth header
        when(userRepository.findByToken(authHeader)).thenReturn(testUser);

        when(userRepository.findByUserId(userId)).thenReturn(testUser);

        // call the getUserByIdGeneralAuth function and verify that it returns the test user
        User result = userService.getUserByIdGeneralAuth(userId, authHeader);
        assertEquals(testUser, result);
    }

    @Test
    public void getUserByIdGeneralAuth_invalidAuthHeader_throwsUnauthorizedException() {
        // given -> a valid user ID and an invalid auth header
        Long userId = 1L;
        String authHeader = "invalidToken";

        // call the getUserByIdGeneralAuth function and verify that it throws an Unauthorized exception
        assertThrows(ResponseStatusException.class, () -> userService.getUserByIdGeneralAuth(userId, authHeader));
        verify(userRepository, times(1)).findByUserId(userId);
        verify(userRepository, times(1)).findByToken(authHeader);
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void getUserById_validIdAndMatchingAuthHeader_returnsUser() {
        // given -> a valid user ID and matching auth header
        Long userId = 1L;
        String authHeader = "validToken";
        when(userRepository.findByUserId(userId)).thenReturn(testUser);
        when(userRepository.findByToken(authHeader)).thenReturn(testUser);

        // when -> calling getUserById with valid inputs
        User result = userService.getUserById(userId, authHeader);

        // then -> the correct user is returned and its status is set to ONLINE
        assertEquals(testUser, result);
        assertEquals(UserStatus.ONLINE, result.getStatus());
        verify(userRepository, times(1)).flush();
    }

    @Test
    public void getUserById_invalidId_throwsNotFoundException() {
        // given -> an invalid user ID and matching auth header
        Long userId = 2L;
        String authHeader = "validToken";
        when(userRepository.findByUserId(userId)).thenReturn(null);
        when(userRepository.findByToken(authHeader)).thenReturn(testUser);

        // when -> calling getUserById with invalid ID
        assertThrows(ResponseStatusException.class, () -> userService.getUserById(userId, authHeader));

        // then -> a NotFoundException is thrown
        verify(userRepository, Mockito.never()).flush();
    }

    @Test
    public void getUserById_userIdNotMatchingToken_throwsUnauthorizedException() {
        // given -> an existing user
        User existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setUsername("testUsername");
        existingUser.setToken(UUID.randomUUID().toString());

        // given -> another existing user with a different user ID
        User anotherUser = new User();
        anotherUser.setUserId(2L);
        anotherUser.setUsername("anotherUsername");
        anotherUser.setToken(UUID.randomUUID().toString());

        // when -> userRepository is queried for a user with certain userId or token -> return the dummy users
        when(userRepository.findByUserId(Mockito.any())).thenReturn(existingUser);
        when(userRepository.findByToken(Mockito.any())).thenReturn(anotherUser);

        // when -> attempt to get user by ID with a non-matching token
        assertThrows(ResponseStatusException.class, () -> userService.getUserById(existingUser.getUserId(), anotherUser.getToken()));
    }

    @Test
    public void getUserByIdGeneralAuth_invalidToken_throwsUnauthorizedException() {
        // given -> an existing user
        User existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setUsername("testUsername");
        existingUser.setToken(UUID.randomUUID().toString());

        // given -> an invalid token
        String invalidToken = UUID.randomUUID().toString();

        // when -> userRepository is queried for a user with a certain userId or token -> return the dummy user or null
        when(userRepository.findByUserId(Mockito.any())).thenReturn(existingUser);
        when(userRepository.findByToken(Mockito.any())).thenReturn(null);

        // when -> attempt to get user by ID with an invalid token
        assertThrows(ResponseStatusException.class, () -> userService.getUserByIdGeneralAuth(existingUser.getUserId(), invalidToken));
    }


    @Test
    public void getUsers_returnsListOfUsers() {
        // mock the UserRepository to return a list of users
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());
        when(userRepository.findAll()).thenReturn(userList);

        // call the getUsers function and verify that it returns the list of users
        List<User> result = userService.getUsers();
        assertEquals(userList, result);
    }

    @Test
    public void getUsers_emptyList_returnsEmptyList() {
        // mock the UserRepository to return an empty list of users
        List<User> userList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(userList);

        // call the getUsers function and verify that it returns an empty list
        List<User> result = userService.getUsers();
        assertTrue(result.isEmpty());
    }

    @Test
    public void createUser_validInputs_success() {
        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        User createdUser = userService.createUser(testUser);

        // then
        verify(userRepository, times(1)).save(Mockito.any());


        assertEquals(testUser.getUserId(), createdUser.getUserId());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertNotNull(createdUser.getToken());
        assertEquals(UserStatus.ONLINE, createdUser.getStatus());
    }


    @Test
    public void createUser_duplicateInputs_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error
        // is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
    }

    @Test
    public void createUser_emptyUsername_throwsException() {
        // given
        User userWithEmptyUsername = new User();
        userWithEmptyUsername.setPassword("testPassword");
        userWithEmptyUsername.setUsername("");

        // when/then
        assertThrows(ResponseStatusException.class, () -> userService.createUser(userWithEmptyUsername));
    }

    @Test
    public void createUser_emptyPassword_throwsException() {
        // given
        User userWithEmptyPassword = new User();
        userWithEmptyPassword.setPassword("");
        userWithEmptyPassword.setUsername("testUsername");

        // when/then
        assertThrows(ResponseStatusException.class, () -> userService.createUser(userWithEmptyPassword));
    }

    @Test
    public void createUser_nullUsername_throwsException() {
        // given
        User userWithNullUsername = new User();
        userWithNullUsername.setPassword("testPassword");
        userWithNullUsername.setUsername(null);

        // when/then
        assertThrows(ResponseStatusException.class, () -> userService.createUser(userWithNullUsername));
    }

    @Test
    public void createUser_nullPassword_throwsException() {
        // given
        User userWithNullPassword = new User();
        userWithNullPassword.setPassword(null);
        userWithNullPassword.setUsername("testUsername");

        // when/then
        assertThrows(ResponseStatusException.class, () -> userService.createUser(userWithNullPassword));
    }

    @Test
    public void createUser_usernameWithSpace_throwsException() {
        // given
        User userWithSpaceInUsername = new User();
        userWithSpaceInUsername.setPassword("testPassword");
        userWithSpaceInUsername.setUsername("test Username");

        // when/then
        assertThrows(ResponseStatusException.class, () -> userService.createUser(userWithSpaceInUsername));
    }

    @Test
    public void createUser_userExists_throwsException() {
        // given
        User existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setPassword("testPassword");
        existingUser.setUsername("testUsername");
        existingUser.setCreation_date(new Date());

        when(userRepository.findByUsername(existingUser.getUsername())).thenReturn(existingUser);

        // when/then
        assertThrows(ResponseStatusException.class, () -> userService.createUser(existingUser));
    }

    @Test
    public void loginUser_validInputs_success() {
        // given -> an existing user
        User existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setPassword("testPassword");
        existingUser.setUsername("testUsername");
        existingUser.setCreation_date(new Date());
        existingUser.setStatus(UserStatus.OFFLINE);

        // when -> the userRepository is queried for a user with a certain username -> return the dummy
        // testUser
        when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // when -> attempt to login with valid credentials
        User loggedInUser = userService.loginUser(existingUser);

        // then -> check that user status is set to online
        verify(userRepository, times(1)).flush();
        assertEquals(UserStatus.ONLINE, loggedInUser.getStatus());
    }

    @Test
    public void loginUser_emptyUsername_throwsBadRequestException() {
        // given -> a user with an empty username
        User user = new User();
        user.setPassword("testPassword");
        user.setUsername("");

        // when -> attempt to login with empty username
        assertThrows(ResponseStatusException.class, () -> userService.loginUser(user));
    }

    @Test
    public void loginUser_emptyPassword_throwsBadRequestException() {
        // given -> a user with an empty password
        User user = new User();
        user.setPassword("");
        user.setUsername("testUsername");

        // when -> attempt to login with empty password
        assertThrows(ResponseStatusException.class, () -> userService.loginUser(user));
    }

    @Test
    public void loginUser_nonExistentUsername_throwsNotFoundException() {
        // given -> a user with a non-existent username
        User user = new User();
        user.setPassword("testPassword");
        user.setUsername("nonExistentUsername");

        // when -> userRepository is queried for a user with a certain username -> return null
        when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

        // when -> attempt to log in with non-existent username
        assertThrows(ResponseStatusException.class, () -> userService.loginUser(user));
    }

    @Test
    public void loginUser_incorrectPassword_throwsUnauthorizedException() {
        // given -> an existing user
        User existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setPassword("correctPassword");
        existingUser.setUsername("testUsername");
        existingUser.setCreation_date(new Date());
        existingUser.setStatus(UserStatus.OFFLINE);

        // given -> a user with an incorrect password
        User user = new User();
        user.setPassword("incorrectPassword");
        user.setUsername("testUsername");

        // when -> userRepository is queried for a user with a certain username -> return the existingUser
        when(userRepository.findByUsername(Mockito.any())).thenReturn(existingUser);

        // when -> attempt to log in with incorrect password
        assertThrows(ResponseStatusException.class, () -> userService.loginUser(user));
    }

    @Test
    public void loginUser_nullUsername_throwsBadRequestException() {
        // given -> a user with a null username
        User user = new User();
        user.setPassword("testPassword");
        user.setUsername(null);

        // when -> attempt to log in with null username
        assertThrows(ResponseStatusException.class, () -> userService.loginUser(user));
    }

    @Test
    public void loginUser_nullPassword_throwsBadRequestException() {
        // given -> a user with a null password
        User user = new User();
        user.setPassword(null);
        user.setUsername("testUsername");

        // when -> attempt to log in with null password
        assertThrows(ResponseStatusException.class, () -> userService.loginUser(user));
    }


    // Test case for updating a user's username
    @Test
    public void updateUser_validUsername_success() {
        // given
        User existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setUsername("oldUsername");
        existingUser.setPassword("password");
        existingUser.setToken(UUID.randomUUID().toString());

        User updatedUser = new User();
        updatedUser.setUsername("newUsername");

        when(userRepository.findByUserId(Mockito.any())).thenReturn(existingUser);
        when(userRepository.findByUsername(Mockito.any())).thenReturn(null);
        when(userRepository.findByToken(Mockito.any())).thenReturn(existingUser);
        when(userRepository.save(Mockito.any())).thenReturn(existingUser);

        // when
        boolean isUpdated = userService.updateUser(existingUser.getUserId(), existingUser.getToken(), updatedUser);

        // then
        assertTrue(isUpdated);
        assertEquals(updatedUser.getUsername(), existingUser.getUsername());
        verify(userRepository, times(1)).save(existingUser);
    }

    // Test case for updating a user's birthDate
    @Test
    public void updateUser_validBirthDate_success() {
        // given
        User existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setUsername("username");
        existingUser.setPassword("password");
        existingUser.setBirthDate(new Date(10000000));
        existingUser.setToken(UUID.randomUUID().toString());

        User updatedUser = new User();
        updatedUser.setBirthDate(new Date(20000000));

        when(userRepository.findByUserId(Mockito.any())).thenReturn(existingUser);
        when(userRepository.findByToken(Mockito.any())).thenReturn(existingUser);
        when(userRepository.save(Mockito.any())).thenReturn(existingUser);

        // when
        boolean isUpdated = userService.updateUser(existingUser.getUserId(), existingUser.getToken(), updatedUser);

        // then
        assertTrue(isUpdated);
        assertEquals(updatedUser.getBirthDate(), existingUser.getBirthDate());
        verify(userRepository, times(1)).save(existingUser);
    }

    // Test case for updating a user's password
    @Test
    public void updateUser_validPassword_success() {
        // given
        User existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setUsername("username");
        existingUser.setPassword("oldPassword");
        existingUser.setToken(UUID.randomUUID().toString());

        User updatedUser = new User();
        updatedUser.setPassword("newPassword");

        when(userRepository.findByUserId(Mockito.any())).thenReturn(existingUser);
        when(userRepository.findByToken(Mockito.any())).thenReturn(existingUser);
        when(userRepository.save(Mockito.any())).thenReturn(existingUser);

        // when
        boolean isUpdated = userService.updateUser(existingUser.getUserId(), existingUser.getToken(), updatedUser);

        // then
        assertTrue(isUpdated);
        assertEquals(updatedUser.getPassword(), existingUser.getPassword());
        verify(userRepository, times(1)).save(existingUser);
    }

    // Test case for updating a user's status
    @Test
    public void updateUser_validStatus_success() {
        // given
        User existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setUsername("username");
        existingUser.setPassword("password");
        existingUser.setStatus(UserStatus.ONLINE);
        existingUser.setToken(UUID.randomUUID().toString());

        User updatedUser = new User();
        updatedUser.setStatus(UserStatus.OFFLINE);

        when(userRepository.findByUserId(Mockito.any())).thenReturn(existingUser);
        when(userRepository.findByToken(Mockito.any())).thenReturn(existingUser);
        when(userRepository.save(Mockito.any())).thenReturn(existingUser);

        // when
        boolean isUpdated = userService.updateUser(existingUser.getUserId(), existingUser.getToken(), updatedUser);

        // then
        assertTrue(isUpdated);
        assertEquals(updatedUser.getStatus(), existingUser.getStatus());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void updateUser_changeNationality_success() {
        // given
        User originalUser = new User();
        originalUser.setUserId(1L);
        originalUser.setUsername("testUser");
        originalUser.setPassword("testPassword");
        originalUser.setNationality("originalNationality");
        originalUser.setStatus(UserStatus.ONLINE);
        originalUser.setCreation_date(new Date());
        originalUser.setToken("testToken");

        when(userRepository.findByUserId(Mockito.any())).thenReturn(originalUser);
        when(userRepository.findByToken(Mockito.any())).thenReturn(originalUser);
        when(userRepository.findByUsername(Mockito.any())).thenReturn(null);
        when(userRepository.save(Mockito.any())).thenReturn(originalUser);

        User updatedUser = new User();
        updatedUser.setNationality("newNationality");

        // when
        boolean result = userService.updateUser(1L, "testToken", updatedUser);

        // then
        assertTrue(result);
        assertEquals(updatedUser.getNationality(), originalUser.getNationality());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(userRepository, Mockito.times(1)).flush();
    }

    @Test
    public void updateUser_usernameAlreadyTaken_throwsConflict() {
        User existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setUsername("oldUsername");
        existingUser.setPassword("password");
        existingUser.setToken(UUID.randomUUID().toString());

        User updatedUser = new User();
        updatedUser.setUsername("takenUsername");

        when(userRepository.findByUserId(Mockito.any())).thenReturn(existingUser);
        when(userRepository.findByUsername(Mockito.any())).thenReturn(new User());
        when(userRepository.findByToken(Mockito.any())).thenReturn(existingUser);

        assertThrows(ResponseStatusException.class, () ->
                userService.updateUser(existingUser.getUserId(), existingUser.getToken(), updatedUser)
        );
    }

    @Test
    public void updateUser_usernameWithSpaces_throwsBadRequest() {
        User existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setUsername("oldUsername");
        existingUser.setPassword("password");
        existingUser.setToken(UUID.randomUUID().toString());

        User updatedUser = new User();
        updatedUser.setUsername("invalid username");

        when(userRepository.findByUserId(Mockito.any())).thenReturn(existingUser);
        when(userRepository.findByUsername(Mockito.any())).thenReturn(null);
        when(userRepository.findByToken(Mockito.any())).thenReturn(existingUser);

        assertThrows(ResponseStatusException.class, () ->
                userService.updateUser(existingUser.getUserId(), existingUser.getToken(), updatedUser)
        );
    }

    @Test
    public void updateUser_unauthorizedToken_throwsUnauthorized() {
        User existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setUsername("oldUsername");
        existingUser.setPassword("password");
        existingUser.setToken(UUID.randomUUID().toString());

        User updatedUser = new User();
        updatedUser.setUsername("newUsername");

        when(userRepository.findByUserId(Mockito.any())).thenReturn(existingUser);
        when(userRepository.findByUsername(Mockito.any())).thenReturn(null);
        when(userRepository.findByToken(Mockito.any())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () ->
                userService.updateUser(existingUser.getUserId(), "invalid_token", updatedUser)
        );
    }

    @Test
    public void updateUser_invalidUserId_throwsNotFound() {
        User existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setUsername("oldUsername");
        existingUser.setPassword("password");
        existingUser.setToken(UUID.randomUUID().toString());

        User updatedUser = new User();
        updatedUser.setUsername("newUsername");

        when(userRepository.findByUserId(Mockito.any())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () ->
                userService.updateUser(99L, existingUser.getToken(), updatedUser)
        );
    }
}
