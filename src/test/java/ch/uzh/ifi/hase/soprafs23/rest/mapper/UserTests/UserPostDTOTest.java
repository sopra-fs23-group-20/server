package ch.uzh.ifi.hase.soprafs23.rest.mapper.UserTests;

import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.entityDB.User;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserPostDTOTest {

    @Test
    public void testToUserMapping() {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("TestUser");
        userPostDTO.setPassword("TestPassword");

        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        assertNull(user.getUserId());
        assertEquals(userPostDTO.getUsername(), user.getUsername());
        assertEquals(userPostDTO.getPassword(), user.getPassword());
    }

    @Test
    public void testToUserWithNullDTO() {
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(null);
        assertNull(user);
    }

    @Test
    public void testToUserWithNullFields() {
        UserPostDTO userPostDTO = new UserPostDTO();

        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        assertNull(user.getUserId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
    }

}
