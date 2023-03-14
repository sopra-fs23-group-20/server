package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.Country;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.CountryGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPutDTO;
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

    @Mapping(source = "id", target = "id")
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
    CountryGetDTO convertEntityToCountryGetDTO(Country country);
}
