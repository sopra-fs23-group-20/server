package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class GuessPostDTO {

    private String guess;

    private Long userId;

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
