package ch.uzh.ifi.hase.soprafs23.entityDB;

import javax.persistence.*;

@Embeddable
public class GameUser {

    private Long userId;
    private String token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static GameUser transformUserToGameUser(User user){
        GameUser gameUser = new GameUser();
        gameUser.setUserId(user.getUserId());
        gameUser.setUsername(user.getUsername());
        gameUser.setToken(user.getToken());
        gameUser.setCurrentRoundPoints(0L);
        gameUser.setGamePoints(0L);
        return gameUser;
    }
}
