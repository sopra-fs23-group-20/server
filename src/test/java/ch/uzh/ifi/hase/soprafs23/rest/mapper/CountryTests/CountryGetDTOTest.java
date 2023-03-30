package ch.uzh.ifi.hase.soprafs23.rest.mapper.CountryTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ch.uzh.ifi.hase.soprafs23.entityDB.Outline;
import ch.uzh.ifi.hase.soprafs23.entityOther.Location;
import ch.uzh.ifi.hase.soprafs23.rest.dto.CountryGetDTO;
import org.junit.jupiter.api.Test;

public class CountryGetDTOTest {

    @Test
    public void testGettersAndSetters() {
        // Create a new CountryGetDTO object
        CountryGetDTO dto = new CountryGetDTO();

        // Set the properties of the object
        dto.setName("Switzerland");
        dto.setPopulation(8570000L);
        dto.setCapital("Bern");
        dto.setFlag("https://www.example.com/flags/switzerland.png");

        Outline outline = new Outline();
        dto.setOutline(outline);

        Location location = new Location();
        location.setLatitude(46.8182);
        location.setLongitude(8.2275);
        dto.setLocation(location);

        // Test the getter methods of the object
        assertEquals("Switzerland", dto.getName());
        assertEquals(8570000L, dto.getPopulation());
        assertEquals("Bern", dto.getCapital());
        assertEquals("https://www.example.com/flags/switzerland.png", dto.getFlag());
        assertNotNull(dto.getOutline());
        assertNotNull(dto.getLocation());
    }

}
