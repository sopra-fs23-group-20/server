package ch.uzh.ifi.hase.soprafs23.rest.mapper.GameTests;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GamePostDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GamePostDTOTest {

    @Test
    public void testGetUserId() {
        // Given
        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setLobbyCreatorUserId("testUser");

        // When
        String userId = gamePostDTO.getLobbyCreatorUserId();

        // Then
        assertEquals("testUser", userId);
    }

    @Test
    public void testSetUserId() {
        // Given
        GamePostDTO gamePostDTO = new GamePostDTO();

        // When
        gamePostDTO.setLobbyCreatorUserId("testUser");

        // Then
        assertEquals("testUser", gamePostDTO.getLobbyCreatorUserId());
    }
}
