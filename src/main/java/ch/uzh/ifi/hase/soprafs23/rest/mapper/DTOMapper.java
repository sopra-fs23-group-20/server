package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.Country;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Guess;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "status", target = "status")
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

    @Mapping(source = "userId", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "creation_date", target = "creation_date")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "birthday", target = "birthday")
    UserGetDTO convertEntityToUserGetDTO(User user);


    @Mapping(source = "name", target = "name")
    @Mapping(source = "population", target = "population")
    @Mapping(source = "flag", target = "flag")
    @Mapping(source = "longitude", target = "longitude")
    @Mapping(source = "latitude", target = "latitude")
    @Mapping(source = "outline", target = "outline")
    CountryGetDTO convertEntityToCountryGetDTO(Country country);


    @Mapping(source = "gameId", target = "gameId")
    @Mapping(source = "lobbyCreator", target = "lobbyCreator")
    @Mapping(source = "participants", target = "participants")
    @Mapping(source = "creationDate", target = "creationDate")
    @Mapping(source = "gameEndingCriteria", target = "gameEndingCriteria")
    @Mapping(source = "roundDuration", target = "roundDuration")
    @Mapping(source = "region", target = "region")
    @Mapping(source = "countriesToPlay", target = "countriesToPlay")
    @Mapping(source = "currentRound", target = "currentRound")
    @Mapping(source = "categoriesSelected", target = "categoriesSelected")
    @Mapping(source = "remainingCategories", target = "remainingCategories")
    @Mapping(source = "currentCategory", target = "currentCategory")
    @Mapping(source = "remainingTime", target = "remainingTime")
    @Mapping(source = "currentCountry", target = "currentCountry")
    GameGetDTO convertEntityToGameGetDTO(Game game);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "guess", target = "guess")
    Guess convertGuessPostDTOtoEntity(GuessPostDTO guessPostDTO);
}
