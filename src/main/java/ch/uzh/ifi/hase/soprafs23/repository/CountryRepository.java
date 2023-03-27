package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entityDB.Country;
import ch.uzh.ifi.hase.soprafs23.entityDB.Outline;
import ch.uzh.ifi.hase.soprafs23.entityOther.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository("countryRepository")
public interface CountryRepository extends JpaRepository<Country, Long> {

    @Query("SELECT c.capital FROM Country c WHERE c.countryId = :countryId")
    String findCapitalByCountryId(@Param("countryId") Long countryId);

    @Query("SELECT c.population FROM Country c WHERE c.countryId = :countryId")
    Long findPopulationByCountryId(@Param("countryId") Long countryId);

    @Query("SELECT c.flag FROM Country c WHERE c.countryId = :countryId")
    String findFlagByCountryId(@Param("countryId") Long countryId);

    @Query("SELECT c.name FROM Country c WHERE c.countryId = :countryId")
    String findNameByCountryId(@Param("countryId") Long countryId);

    @Query("SELECT c.location FROM Country c WHERE c.countryId = :countryId")
    Location findLocationByCountryId(@Param("countryId") Long countryId);

    @Query("SELECT c.outline FROM Country c WHERE c.countryId = :countryId")
    Outline findOutlineByCountryId(@Param("countryId") Long countryId);

    Country findByCountryId(Long countryId);

    @Query("SELECT DISTINCT c.countryId FROM Country c")
    Set<Long> getAllCountryIds();
}
