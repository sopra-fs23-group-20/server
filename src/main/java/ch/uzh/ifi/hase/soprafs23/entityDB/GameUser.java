package ch.uzh.ifi.hase.soprafs23.entityDB;

import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "GAME_USER")
@JsonIgnoreProperties({"GAME"})
public class GameUser implements Serializable {
    @Id
    @GeneratedValue
    private Long gameUserId;
    @ManyToOne
    @JoinColumn(name = "gameId")
    @JsonBackReference
    private Game game;
    private Long userId;
    private String token;
    private String username;

    private boolean hasAlreadyGuessed;

    private Long gamePoints;

    private GameState userPlayingState;


    public boolean getHasAlreadyGuessed() {
        return hasAlreadyGuessed;
    }

    public void setHasAlreadyGuessed(boolean hasAlreadyGuessed) {
        this.hasAlreadyGuessed = hasAlreadyGuessed;
    }

    public Long getUserId() {
        return userId;
    }

    public GameState getCurrentState() {
        return userPlayingState;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Long getGameUserId() {
        return gameUserId;
    }

    public void setGameUserId(Long gameUserId) {
        this.gameUserId = gameUserId;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameState getUserPlayingState() {
        return userPlayingState;
    }

    public void setUserPlayingState(GameState userPlayingState) {
        this.userPlayingState = userPlayingState;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public static GameUser transformUserToGameUser(User user, Game game) {
        GameUser gameUser = new GameUser();
        gameUser.setGame(game);
        gameUser.setUserId(user.getUserId());
        gameUser.setUsername(user.getUsername());
        gameUser.setToken(user.getToken());
        gameUser.setGamePoints(0L);
        gameUser.setUserPlayingState(GameState.SETUP);
        return gameUser;
    }
}