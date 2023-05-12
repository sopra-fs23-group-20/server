package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entityDB.Country;
import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import ch.uzh.ifi.hase.soprafs23.entityOther.Guess;
import ch.uzh.ifi.hase.soprafs23.entityDB.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "birthDate", target = "birthDate")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "nationality", target = "nationality")
    @Mapping(source = "profilePicture", target = "profilePicture")
    @Mapping(source = "gamesWon", target = "gamesWon")
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

    @Mapping(source = "userId", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "creation_date", target = "creation_date")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "birthDate", target = "birthDate")
    @Mapping(source = "nationality", target = "nationality")
    @Mapping(source = "profilePicture", target = "profilePicture")
    @Mapping(source = "gamesWon", target = "gamesWon")
    UserGetDTO convertEntityToUserGetDTO(User user);


    @Mapping(source = "name", target = "name")
    @Mapping(source = "population", target = "population")
    @Mapping(source = "capital", target = "capital")
    @Mapping(source = "flag", target = "flag")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "outline", target = "outline")
    CountryGetDTO convertEntityToCountryGetDTO(Country country);


    @Mapping(source = "gameId", target = "gameId")
    @Mapping(source = "lobbyCreator", target = "lobbyCreator")
    @Mapping(source = "participants", target = "participants")
    @Mapping(source = "creationDate", target = "creationDate")
    @Mapping(source = "currentState", target = "currentState")
    @Mapping(source = "roundDuration", target = "roundDuration")
    @Mapping(source = "remainingTime", target = "remainingTime")
    @Mapping(source = "numberOfRounds", target = "numberOfRounds")
    @Mapping(source = "remainingRounds", target = "remainingRounds")
    @Mapping(source = "remainingRoundPoints", target = "remainingRoundPoints")
    @Mapping(source = "selectedRegions", target = "selectedRegions")
    @Mapping(source = "categoryStack", target = "categoryStack")
    @Mapping(source="openLobby", target="openLobby")
    @Mapping(source="difficulty", target="difficulty")
    @Mapping(source="gameMode", target="gameMode")
    @Mapping(source ="numberOfGuesses", target="numberOfGuesses")
    GameGetDTO convertEntityToGameGetDTO(Game game);


    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "guess", target = "guess")
    Guess convertGuessPostDTOtoEntity(GuessPostDTO guessPostDTO);


}
