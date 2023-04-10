package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entityDB.Country;
import ch.uzh.ifi.hase.soprafs23.entityOther.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CountryRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void findCapitalByCountryId_success() {
        // given
        Country country = new Country();
        country.setName("Switzerland");
        country.setCapital("Bern");
        country.setFlag("https://flagpedia.net/data/flags/w580/ch.png");
        country.setLocation(new Location());
        entityManager.persist(country);
        entityManager.flush();

        // when
        String foundCapital = countryRepository.findCapitalByCountryId(country.getCountryId());

        // then
        assertNotNull(foundCapital);
        assertEquals(foundCapital, country.getCapital());
    }

    @Test
    public void findPopulationByCountryId_success() {
        // given
        Country country = new Country();
        country.setName("Switzerland");
        country.setCapital("Bern");
        country.setFlag("https://flagpedia.net/data/flags/w580/ch.png");
        country.setLocation(new Location());
        country.setPopulation(8591365L);
        entityManager.persist(country);
        entityManager.flush();

        // when
        Long foundPopulation = countryRepository.findPopulationByCountryId(country.getCountryId());

        // then
        assertNotNull(foundPopulation);
        assertEquals(foundPopulation, country.getPopulation());
    }

    @Test
    public void findFlagByCountryId_success() {
        // given
        Country country = new Country();
        country.setName("Switzerland");
        country.setCapital("Bern");
        country.setFlag("https://flagpedia.net/data/flags/w580/ch.png");
        country.setLocation(new Location());
        entityManager.persist(country);
        entityManager.flush();

        // when
        String foundFlag = countryRepository.findFlagByCountryId(country.getCountryId());

        // then
        assertNotNull(foundFlag);
        assertEquals(foundFlag, country.getFlag());
    }

    @Test
    public void findNameByCountryId_success() {
        // given
        Country country = new Country();
        country.setName("Switzerland");
        country.setCapital("Bern");
        country.setFlag("https://flagpedia.net/data/flags/w580/ch.png");
        country.setLocation(new Location());
        entityManager.persist(country);
        entityManager.flush();

        // when
        String foundName = countryRepository.findNameByCountryId(country.getCountryId());

        // then
        assertNotNull(foundName);
        assertEquals(foundName, country.getName());
    }

    @Test
    public void findLocationByCountryId_success() {
        // given
        Country country = new Country();
        country.setName("Switzerland");
        country.setCapital("Bern");
        country.setFlag("https://flagpedia.net/data/flags/w580/ch.png");
        Location location = new Location();
        location.setLatitude(46.8182);
        location.setLongitude(8.2275);
        country.setLocation(location);
        entityManager.persist(country);
        entityManager.flush();

        // when
        Location foundLocation = countryRepository.findLocationByCountryId(country.getCountryId());

        // then
        assertNotNull(foundLocation);
        assertEquals(foundLocation.getLatitude(), location.getLatitude());
        assertEquals(foundLocation.getLongitude(), location.getLongitude());
    }

    @Test
    public void findByCountryId_success() {
        // given
        Country country = new Country();
        country.setName("Switzerland");
        country.setCapital("Bern");
        country.setPopulation(8715000L);
        country.setFlag("https://flagpedia.net/data/flags/w580/ch.png");

        Location location = new Location();
        location.setLatitude(46.8182);
        location.setLongitude(8.2275);
        country.setLocation(location);

        entityManager.persist(country);
        entityManager.flush();

        // when
        Country found = countryRepository.findByCountryId(country.getCountryId());

        // then
        assertNotNull(found);
        assertEquals(found.getCountryId(), country.getCountryId());
        assertEquals(found.getName(), country.getName());
        assertEquals(found.getCapital(), country.getCapital());
        assertEquals(found.getPopulation(), country.getPopulation());
        assertEquals(found.getFlag(), country.getFlag());
        assertEquals(found.getLocation().getLatitude(), country.getLocation().getLatitude());
        assertEquals(found.getLocation().getLongitude(), country.getLocation().getLongitude());
    }

    @Test
    public void findByCountryId_notFound() {
        // when
        Country found = countryRepository.findByCountryId(-1L);
        // then
        assertNull(found);
    }

    @Test
    public void getAllCountryIds_success() {
        // given
        Country country1 = new Country();
        country1.setName("Switzerland");

        Country country2 = new Country();
        country2.setName("Germany");

        entityManager.persist(country1);
        entityManager.persist(country2);
        entityManager.flush();

        // when
        Set<Long> foundIds = countryRepository.getAllCountryIds();

        // then
        assertEquals(2, foundIds.size());
        assertTrue(foundIds.contains(country1.getCountryId()));
        assertTrue(foundIds.contains(country2.getCountryId()));
    }

    @Test
    public void findNameByCountryId_notFound() {
        // given
        Long countryId = -1L;

        // when
        String foundName = countryRepository.findNameByCountryId(countryId);

        // then
        assertNull(foundName);
    }

    @Test
    public void getAllCountryIds_empty() {
        // when
        Set<Long> foundIds = countryRepository.getAllCountryIds();

        // then
        assertTrue(foundIds.isEmpty());
    }

}
