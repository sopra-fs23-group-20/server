package ch.uzh.ifi.hase.soprafs23.entityDB;

import ch.uzh.ifi.hase.soprafs23.StatePattern.*;
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
    private Long lobbyCreatorUserId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "participants", joinColumns = @JoinColumn(name = "gameId"))
    private Set<GameUser> participants;

    @Column()
    private Date creationDate;
    @Column()
    private GameState currentState;
    @Column()
    private Long gameEndingCriteria;
    @Column()
    private Long roundDuration;
    @Column()
    private RegionEnum region;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "countriesToPlay", joinColumns = @JoinColumn(name = "gameId"))
    @Column(name = "countryId")

    private Set<Long> countriesToPlayIds;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "categoryStackId")
    private CategoryStack categoryStack;
    @Column()
    private Long remainingTime;


    private Long currentCountryId;

    private Long remainingRoundPoints;

    private Long remainingRounds;
    private Boolean randomizedHints;

    private Long numberOfRounds;
    private Boolean openLobby;


    public Boolean getRandomizedHints() {
        return randomizedHints;
    }

    public void setRandomizedHints(Boolean randomizedHints) {
        this.randomizedHints = randomizedHints;
    }


    public Long getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(Long numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public Boolean getOpenLobby() {
        return openLobby;
    }

    public void setOpenLobby(Boolean openLobby) {
        this.openLobby = openLobby;
    }

    public List<CategoryEnum> getAvailableHints() {
        return availableHints;
    }

    public void setAvailableHints(List<CategoryEnum> availableHints) {
        this.availableHints = availableHints;
    }

    @ElementCollection(targetClass = CategoryEnum.class)
    private List<CategoryEnum> availableHints;

    public Long getRemainingRoundPoints() {
        return remainingRoundPoints;
    }

    public void setRemainingRoundPoints(Long remainingRoundPoints) {
        this.remainingRoundPoints = remainingRoundPoints;
    }


    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getLobbyCreatorUserId() {
        return lobbyCreatorUserId;
    }

    public void setLobbyCreatorUserId(Long lobbyCreatorUserId) {
        this.lobbyCreatorUserId = lobbyCreatorUserId;
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
        Set<GameUser> gamesUsers = getParticipants();
        for(GameUser gameUser: gamesUsers){
            gameUser.setCurrentState(currentState);
        }
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

    public Set<Long> getCountriesToPlayIds() {
        return countriesToPlayIds;
    }

    public void setCountriesToPlayIds(Set<Long> countriesToPlay) {
        this.countriesToPlayIds = countriesToPlay;
    }


    public CategoryStack getCategoryStack() {
        return categoryStack;
    }

    public void setCategoryStack(CategoryStack categoryStack) {
        this.categoryStack = categoryStack;
    }

    public Long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(Long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public Long getCurrentCountryId() {
        return currentCountryId;
    }

    public void setCurrentCountryId(Long currentCountryId) {
        this.currentCountryId = currentCountryId;
    }


    public Long getRemainingRounds() {
        return remainingRounds;
    }

    public void setRemainingRounds(Long remainingRounds) {
        this.remainingRounds = remainingRounds;
    }

    public GameStateClass getGameStateClass() {
        GameStateClass gameStateClass = null;
        switch (this.currentState){
            case SETUP:
                gameStateClass = new SetupStateClass();
            case GUESSING:
                gameStateClass = new GuessingStateClass();
            case SCOREBOARD:
                gameStateClass = new ScoreboardStateClass();
            case ENDED:
                gameStateClass= new EndedStateClass();
        }
        return gameStateClass;
    }
}

