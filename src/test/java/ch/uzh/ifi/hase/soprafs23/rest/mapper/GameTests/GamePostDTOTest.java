package ch.uzh.ifi.hase.soprafs23.rest.mapper.GameTests;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameCreateDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameCreateDTOTest {

    @Test
    public void testGetUserId() {
        // Given
        GameCreateDTO gameCreateDTO = new GameCreateDTO();
        gameCreateDTO.setUserId("testUser");

        // When
        String userId = gameCreateDTO.getUserId();

        // Then
        assertEquals("testUser", userId);
    }

    @Test
    public void testSetUserId() {
        // Given
        GameCreateDTO gameCreateDTO = new GameCreateDTO();

        // When
        gameCreateDTO.setUserId("testUser");

        // Then
        assertEquals("testUser", gameCreateDTO.getUserId());
    }
}
