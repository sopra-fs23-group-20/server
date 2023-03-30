package ch.uzh.ifi.hase.soprafs23.rest.mapper.UserTests;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Date;


/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */


public class UserGetDTOTest {

    @Test
    public void testGetId() {
        UserGetDTO dto = new UserGetDTO();
        dto.setId(123L);
        assertEquals(123L, dto.getId());
    }

    @Test
    public void testGetUsername() {
        UserGetDTO dto = new UserGetDTO();
        dto.setUsername("john_doe");
        assertEquals("john_doe", dto.getUsername());
    }

    @Test
    public void testGetStatus() {
        UserGetDTO dto = new UserGetDTO();
        dto.setStatus(UserStatus.ONLINE);
        assertEquals(UserStatus.ONLINE, dto.getStatus());
    }

    @Test
    public void testGetCreationDate() {
        UserGetDTO dto = new UserGetDTO();
        Date date = new Date();
        dto.setCreation_date(date);
        assertEquals(date, dto.getCreation_date());
    }

    @Test
    public void testGetBirthday() {
        UserGetDTO dto = new UserGetDTO();
        Date date = new Date();
        dto.setBirthday(date);
        assertEquals(date, dto.getBirthday());
    }

    @Test
    public void testGetNationality() {
        UserGetDTO dto = new UserGetDTO();
        dto.setNationality("Swiss");
        assertEquals("Swiss", dto.getNationality());
    }

    @Test
    public void testGetProfilePicture() {
        UserGetDTO dto = new UserGetDTO();
        dto.setProfilePicture("profile.jpg");
        assertEquals("profile.jpg", dto.getProfilePicture());
    }
}
