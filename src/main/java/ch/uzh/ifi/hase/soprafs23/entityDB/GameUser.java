package ch.uzh.ifi.hase.soprafs23.entityDB;

import javax.persistence.*;

@Embeddable
public class GameUser {

    private Long userId;
    private String username;
    private Long currentRoundPoints;
    private Long gamePoints;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getCurrentRoundPoints() {
        return currentRoundPoints;
    }

    public void setCurrentRoundPoints(Long currentRoundPoints) {
        this.currentRoundPoints = currentRoundPoints;
    }

    public Long getGamePoints() {
        return gamePoints;
    }

    public void setGamePoints(Long gamePoints) {
        this.gamePoints = gamePoints;
    }
}
