package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class GuessPostDTO {
    private String guess;
    private String username;

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}