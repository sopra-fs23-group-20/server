package ch.uzh.ifi.hase.soprafs23.entityDB;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testUserId() {
        user.setUserId(1L);
        assertNotNull(user.getUserId());
        assertEquals(1L, user.getUserId());
    }

    @Test
    void testUsername() {
        String testUsername = "TestUser";
        user.setUsername(testUsername);
        assertNotNull(user.getUsername());
        assertEquals(testUsername, user.getUsername());
    }

    @Test
    void testCreationDate() {
        Date testCreationDate = new Date();
        user.setCreation_date(testCreationDate);
        assertNotNull(user.getCreation_date());
        assertEquals(testCreationDate, user.getCreation_date());
    }

    @Test
    void testStatus() {
        user.setStatus(UserStatus.ONLINE);
        assertNotNull(user.getStatus());
        assertEquals(UserStatus.ONLINE, user.getStatus());
    }

    @Test
    void testPassword() {
        String testPassword = "TestPassword";
        user.setPassword(testPassword);
        assertNotNull(user.getPassword());
        assertEquals(testPassword, user.getPassword());
    }

    @Test
    void testBirthday() {
        Date testBirthDate = new Date();
        user.setBirthDate(testBirthDate);
        assertNotNull(user.getBirthDate());
        assertEquals(testBirthDate, user.getBirthDate());
    }

    @Test
    void testToken() {
        String testToken = "TestToken";
        user.setToken(testToken);
        assertNotNull(user.getToken());
        assertEquals(testToken, user.getToken());
    }

    @Test
    void testNationality() {
        String testNationality = "TestNationality";
        user.setNationality(testNationality);
        assertNotNull(user.getNationality());
        assertEquals(testNationality, user.getNationality());
    }

    @Test
    void testProfilePicture() {
        String testProfilePicture = "TestProfilePicture";
        user.setProfilePicture(testProfilePicture);
        assertNotNull(user.getProfilePicture());
        assertEquals(testProfilePicture, user.getProfilePicture());
    }

    @Test
    void testGamesWon() {
        int testGamesWon = 5;
        user.setGamesWon(testGamesWon);
        assertEquals(testGamesWon, user.getGamesWon());
    }
}
