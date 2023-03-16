package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.GameState;

import javax.persistence.*;

@Entity
@Table(name = "GAME")
public class Game {
    private static final long serialVersionUID = 3L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = true)
    private Long time;

    @Column(nullable = true)
    private String currentCountry;

    @Column(nullable = true)
    private String currentPopulation;

    @Column(nullable = true)
    private String currentFlag;

    @Column(nullable = true)
    private double currentLatitude;

    @Column(nullable = true)
    private double currentLongitude;

    @Column(nullable = false)
    private GameState gameState;

    @Column(nullable = false)
    private String creatorUsername;

    public String getCurrentFlag() {
        return currentFlag;
    }

    public void setCurrentFlag(String currentFlag) {
        this.currentFlag = currentFlag;
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getCurrentCountry() {
        return currentCountry;
    }

    public void setCurrentCountry(String currentCountry) {
        this.currentCountry = currentCountry;
    }

    public String getCurrentPopulation() {
        return currentPopulation;
    }

    public void setCurrentPopulation(String currentPopulation) {
        this.currentPopulation = currentPopulation;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }


}
