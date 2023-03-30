package ch.uzh.ifi.hase.soprafs23.rest.mapper.UserTests;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entityDB.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserPutDTOTest {

    private final DTOMapper mapper = DTOMapper.INSTANCE;

    @Test
    public void testConvertUserPutDTOtoEntity() {
        // Create a UserPutDTO object
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("testUser");
        userPutDTO.setPassword("password");
        userPutDTO.setBirthday(new Date());
        userPutDTO.setStatus(UserStatus.ONLINE);
        userPutDTO.setNationality("Swiss");
        userPutDTO.setProfilePicture("testPicture");

        // Call the convertUserPutDTOtoEntity method to create a User object
        User user = mapper.convertUserPutDTOtoEntity(userPutDTO);

        // Verify that the User object was created correctly
        assertEquals(userPutDTO.getUsername(), user.getUsername());
        assertEquals(userPutDTO.getPassword(), user.getPassword());
        assertEquals(userPutDTO.getBirthday(), user.getBirthday());
        assertEquals(userPutDTO.getStatus(), user.getStatus());
        assertEquals(userPutDTO.getNationality(), user.getNationality());
        assertEquals(userPutDTO.getProfilePicture(), user.getProfilePicture());
    }

    @Test
    public void testConvertUserPutDTOtoEntity_NullInput() {
        // Call the convertUserPutDTOtoEntity method with a null input
        User user = mapper.convertUserPutDTOtoEntity(null);

        // Verify that a null value is returned
        assertNull(user);
    }
}
