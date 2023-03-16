package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.GameState;

public class GameGetDTO {
    private Long id;
    private Long time;
    private String currentCountry;
    private String currentPopulation;
    private GameState gameState;

    private String currentFlag;

    private double currentLatitude;

    private double currentLongitude;

    public String getCurrentFlag() {
        return currentFlag;
    }

    public void setCurrentFlag(String currentFlag) {
        this.currentFlag = currentFlag;
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public void setCurrentLatitude(double currentLatitude) {
        this.currentLatitude = currentLatitude;
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
