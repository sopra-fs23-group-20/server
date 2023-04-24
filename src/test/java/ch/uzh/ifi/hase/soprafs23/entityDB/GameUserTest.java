package ch.uzh.ifi.hase.soprafs23.entityDB;

import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameUserTest {

    private GameUser gameUser;

    @BeforeEach
    void setUp() {
        gameUser = new GameUser();
    }

    @Test
    void testGameUserId() {
        gameUser.setGameUserId(1L);
        assertNotNull(gameUser.getGameUserId());
        assertEquals(1L, gameUser.getGameUserId());
    }

    @Test
    void testGame() {
        Game game = new Game();
        gameUser.setGame(game);
        assertNotNull(gameUser.getGame());
        assertEquals(game, gameUser.getGame());
    }

    @Test
    void testUserId() {
        gameUser.setUserId(1L);
        assertNotNull(gameUser.getUserId());
        assertEquals(1L, gameUser.getUserId());
    }

    @Test
    void testToken() {
        gameUser.setToken("testToken");
        assertNotNull(gameUser.getToken());
        assertEquals("testToken", gameUser.getToken());
    }

    @Test
    void testUsername() {
        gameUser.setUsername("testUser");
        assertNotNull(gameUser.getUsername());
        assertEquals("testUser", gameUser.getUsername());
    }

    @Test
    void testHasAlreadyguessed() {
        gameUser.setHasAlreadyGuessed(true);
        assertNotNull(gameUser.getHasAlreadyGuessed());
        assertEquals(true, gameUser.getHasAlreadyGuessed());
    }

    @Test
    void testGamePoints() {
        gameUser.setGamePoints(10L);
        assertNotNull(gameUser.getGamePoints());
        assertEquals(10L, gameUser.getGamePoints());
    }

    @Test
    void testUserPlayingState() {
        gameUser.setUserPlayingState(GameState.GUESSING);
        assertNotNull(gameUser.getUserPlayingState());
        assertEquals(GameState.GUESSING, gameUser.getUserPlayingState());
    }

    @Test
    void testTransformUserToGameUser() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testUser");
        user.setToken("testToken");

        Game game = new Game();

        GameUser gameUser = GameUser.transformUserToGameUser(user, game);
        assertNotNull(gameUser);
        assertEquals(user.getUserId(), gameUser.getUserId());
        assertEquals(user.getUsername(), gameUser.getUsername());
        assertEquals(user.getToken(), gameUser.getToken());
        assertEquals(0L, gameUser.getGamePoints());
        assertEquals(GameState.SETUP, gameUser.getUserPlayingState());
    }
}
