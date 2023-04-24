package ch.uzh.ifi.hase.soprafs23.entityDB;

import ch.uzh.ifi.hase.soprafs23.constant.RegionEnum;
import ch.uzh.ifi.hase.soprafs23.entityOther.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CountryTest {

    private Country country;

    @BeforeEach
    void setUp() {
        country = new Country();
    }

    @Test
    void testCountryId() {
        country.setCountryId(1L);
        assertNotNull(country.getCountryId());
        assertEquals(1L, country.getCountryId());
    }

    @Test
    void testName() {
        country.setName("Switzerland");
        assertNotNull(country.getName());
        assertEquals("Switzerland", country.getName());
    }

    @Test
    void testPopulation() {
        country.setPopulation(1000000L);
        assertNotNull(country.getPopulation());
        assertEquals(1000000L, country.getPopulation());
    }

    @Test
    void testFlag() {
        country.setFlag("flag");
        assertNotNull(country.getFlag());
        assertEquals("flag", country.getFlag());
    }

    @Test
    void testLocation() {
        Location location = new Location();
        location.setLatitude(52.5200);
        location.setLongitude(13.4050);
        country.setLocation(location);
        assertNotNull(country.getLocation());
        assertEquals(location, country.getLocation());
    }

    @Test
    void testCapital() {
        country.setCapital("Berlin");
        assertNotNull(country.getCapital());
        assertEquals("Berlin", country.getCapital());
    }

    @Test
    void testOutline() {
        Outline outline = new Outline();
        outline.setOutline("sample outline");
        country.setOutline(outline);
        assertNotNull(country.getOutline());
        assertEquals(outline, country.getOutline());
    }

    @Test
    void testCountryCode() {
        country.setCountryCode("CH");
        assertNotNull(country.getCountryCode());
        assertEquals("CH", country.getCountryCode());
    }

    @Test
    void testRegion() {
        country.setRegion(RegionEnum.EUROPE);
        assertNotNull(country.getRegion());
        assertEquals(RegionEnum.EUROPE, country.getRegion());
    }
}
