package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entityDB.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository("countryRepository")
public interface CountryRepository extends JpaRepository<Country, Long> {
    Country findByName(String name);
    Country findByCountryId(Long countryId);

    @Query("SELECT DISTINCT c.countryId FROM Country c")
    Set<Long> getAllCountryIds();
}
