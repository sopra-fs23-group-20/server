package ch.uzh.ifi.hase.soprafs23.entityDB;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;
import ch.uzh.ifi.hase.soprafs23.entityOther.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
    }

    @Test
    void testType() {
        category.setType(CategoryEnum.POPULATION);
        assertNotNull(category.getType());
        assertEquals(CategoryEnum.POPULATION, category.getType());
    }

    @Test
    void testCapital() {
        category.setCapital("Berlin");
        assertNotNull(category.getCapital());
        assertEquals("Berlin", category.getCapital());
    }

    @Test
    void testFlag() {
        category.setFlag("test");
        assertNotNull(category.getFlag());
        assertEquals("test", category.getFlag());
    }

    @Test
    void testPopulation() {
        category.setPopulation(1000000L);
        assertEquals(1000000L, category.getPopulation());
    }

    @Test
    void testLocation() {
        Location location = new Location();
        location.setLatitude(52.5200);
        location.setLongitude(13.4050);
        category.setLocation(location);
        assertNotNull(category.getLocation());
        assertEquals(location, category.getLocation());
    }

    @Test
    void testOutline() {
        category.setOutline("sample outline");
        assertNotNull(category.getOutline());
        assertEquals("sample outline", category.getOutline());
    }
}
