package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;
import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.constant.RegionEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "GAME")
public class Game {

    @Id
    @GeneratedValue
    private Long gameId;

    @Embedded
    private GameUser lobbyCreator;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "participants", joinColumns = @JoinColumn(name = "gameId"))
    private Set<GameUser> participants;

    @Column(nullable = true)
    private Date creationDate;
    @Column(nullable = true)
    private GameState currentState;
    @Column(nullable = true)
    private long gameEndingCriteria;
    @Column(nullable = true)
    private long roundDuration;

    @Column(nullable = true)
    private RegionEnum region;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "countriesToPlay", joinColumns = @JoinColumn(name = "gameId"))
    private Set<GameCountry> countriesToPlay;

    @Column(nullable = true)
    private long currentRound;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "categoryStackId")
    private CategoryStack remainingCategories;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "categoriesSelected", joinColumns = @JoinColumn(name = "gameId"))
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private List<CategoryEnum> categoriesSelected;

    @Embedded
    private Category currentCategory;
    @Column(nullable = true)
    private Long remainingTime;

    
    @OneToOne
    private Country currentCountry;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public GameUser getLobbyCreator() {
        return lobbyCreator;
    }

    public void setLobbyCreator(GameUser lobbyCreator) {
        this.lobbyCreator = lobbyCreator;
    }

    public Set<GameUser> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<GameUser> participants) {
        this.participants = participants;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }

    public long getGameEndingCriteria() {
        return gameEndingCriteria;
    }

    public void setGameEndingCriteria(long gameEndingCriteria) {
        this.gameEndingCriteria = gameEndingCriteria;
    }

    public long getRoundDuration() {
        return roundDuration;
    }

    public void setRoundDuration(long roundDuration) {
        this.roundDuration = roundDuration;
    }

    public RegionEnum getRegion() {
        return region;
    }

    public void setRegion(RegionEnum region) {
        this.region = region;
    }

    public Set<GameCountry> getCountriesToPlay() {
        return countriesToPlay;
    }

    public void setCountriesToPlay(Set<GameCountry> countriesToPlay) {
        this.countriesToPlay = countriesToPlay;
    }

    public long getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(long currentRound) {
        this.currentRound = currentRound;
    }

    public CategoryStack getRemainingCategories() {
        return remainingCategories;
    }

    public void setRemainingCategories(CategoryStack remainingCategories) {
        this.remainingCategories = remainingCategories;
    }

    public List<CategoryEnum> getCategoriesSelected() {
        return categoriesSelected;
    }

    public void setCategoriesSelected(List<CategoryEnum> categoriesSelected) {
        this.categoriesSelected = categoriesSelected;
    }

    public Category getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(Category currentCategory) {
        this.currentCategory = currentCategory;
    }

    public Long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(Long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public Country getCurrentCountry() {
        return currentCountry;
    }

    public void setCurrentCountry(Country currentCountry) {
        this.currentCountry = currentCountry;
    }
}
