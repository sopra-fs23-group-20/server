package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.*;

@Entity
@Table(name = "GAMEUSER")
public class GameUser {
    @Id
    @GeneratedValue
    private Long gameUserId;
    @ManyToOne
    @JoinColumn(name = "gameId")
    private Game game;
    private String username;
    private Long currentRoundPoints;
    private Long gamePoints;

    public Long getGameUserId() {
        return gameUserId;
    }

    public void setGameUserId(Long userId) {
        this.gameUserId = userId;
    }


    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
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
