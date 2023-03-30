package ch.uzh.ifi.hase.soprafs23.rest.mapper.GuessTests;

import ch.uzh.ifi.hase.soprafs23.rest.dto.GuessPostDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GuessPostDTOTest {

    @Test
    public void testSetAndGetGuess() {
        // Given
        GuessPostDTO guessPostDTO = new GuessPostDTO();
        String expectedGuess = "apple";

        // When
        guessPostDTO.setGuess(expectedGuess);
        String actualGuess = guessPostDTO.getGuess();

        // Then
        assertEquals(expectedGuess, actualGuess);
    }

    @Test
    public void testSetAndGetUsername() {
        // Given
        GuessPostDTO guessPostDTO = new GuessPostDTO();
        String expectedUsername = "johndoe";

        // When
        guessPostDTO.setUsername(expectedUsername);
        String actualUsername = guessPostDTO.getUsername();

        // Then
        assertEquals(expectedUsername, actualUsername);
    }

}
