package ch.uzh.ifi.hase.soprafs23.entityOther;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class GuessTest {

        @Test
        public void testSetAndGetGuess() {
            Guess guessObj = new Guess();
            String expectedGuess = "testGuess";
            guessObj.setGuess(expectedGuess);

            String actualGuess = guessObj.getGuess();

            assertEquals(expectedGuess, actualGuess);
        }

        @Test
        public void testSetAndGetUserId() {
            Guess guessObj = new Guess();
            Long expectedUserId = 1234L;
            guessObj.setUserId(expectedUserId);

            Long actualUserId = guessObj.getUserId();

            assertEquals(expectedUserId, actualUserId);
        }
    }

