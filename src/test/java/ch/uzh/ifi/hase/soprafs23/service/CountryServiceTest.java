package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.RegionEnum;
import ch.uzh.ifi.hase.soprafs23.entityDB.Country;
import ch.uzh.ifi.hase.soprafs23.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;


    @InjectMocks
    private CountryService countryService;

    private List<Country> sampleCountries;

    @BeforeEach
    public void setUp() {
        sampleCountries = new ArrayList<>();

        Country country1 = new Country();
        country1.setCountryId(1L);
        country1.setName("Country 1");
        country1.setRegion(RegionEnum.EUROPE);
        country1.setPopulation(1000000L);

        Country country2 = new Country();
        country2.setCountryId(2L);
        country2.setName("Country 2");
        country2.setRegion(RegionEnum.ASIA);
        country2.setPopulation(2000000L);

        sampleCountries.add(country1);
        sampleCountries.add(country2);
    }

    @Test
    public void getAllCountries_returnAllCountries() {
        when(countryRepository.findAll()).thenReturn(sampleCountries);

        List<Country> result = countryService.getAllCountries();

        verify(countryRepository, times(1)).findAll();
        assertEquals(sampleCountries.size(), result.size());
    }

    @Test
    public void stringToRegionEnum_returnCorrectRegionEnum() {
        RegionEnum africa = countryService.stringToRegionEnum("Africa");
        RegionEnum asia = countryService.stringToRegionEnum("Asia");
        RegionEnum europe = countryService.stringToRegionEnum("Europe");

        assertEquals(RegionEnum.AFRICA, africa);
        assertEquals(RegionEnum.ASIA, asia);
        assertEquals(RegionEnum.EUROPE, europe);
    }


    @Test
    public void setAllCountries_repositoryEmpty_setAllCountriesCalled() {
        clearInvocations(countryRepository);
        when(countryRepository.getAllCountryIds()).thenReturn(Collections.emptySet());

        verify(countryRepository, atLeastOnce()).save(any(Country.class));
    }

    @Test
    public void stringToRegionEnum_invalidRegionString_throwIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> countryService.stringToRegionEnum("InvalidRegion"));
    }

    @Test
    public void getCountriesByContinent_nullContinent_throwIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> countryService.getCountriesByContinent(null));
    }

    @Test
    public void setAllCountries_repositoryNotEmpty_saveNotCalled() {
        Set<Long> nonEmptyCountryIds = new HashSet<>();
        nonEmptyCountryIds.add(1L);
        when(countryRepository.getAllCountryIds()).thenReturn(nonEmptyCountryIds);
        countryService.setAllCountries();
        verify(countryRepository, never()).save(any(Country.class));
    }

    @Test
    public void getCountriesByContinent_noMatchingCountries_returnEmptyList() {
        when(countryRepository.findAll()).thenReturn(sampleCountries);
        List<Country> oceaniaCountries = countryService.getCountriesByContinent(RegionEnum.OCEANIA.toString());
        assertEquals(0, oceaniaCountries.size());
    }

    @Test
    public void getAllCountries_noCountriesInRepository_returnEmptyList() {
        when(countryRepository.findAll()).thenReturn(new ArrayList<>());
        List<Country> result = countryService.getAllCountries();
        verify(countryRepository, times(1)).findAll();
        assertEquals(0, result.size());
    }

    @Test
    public void calculateTopPopulation_oneRatio_returnHighestPopulation() {
        when(countryRepository.findAll(Sort.by(Sort.Direction.DESC, "population"))).thenReturn(sampleCountries);

        Long topPopulation = countryService.calculateTopPopulation(1);

        assertEquals(2000000L, topPopulation);
    }

    @Test
    public void getCountriesByContinent_noCountriesForRegion_returnEmptyList() {
        when(countryRepository.findAll()).thenReturn(sampleCountries);

        List<Country> africaCountries = countryService.getCountriesByContinent(RegionEnum.AFRICA.toString());

        assertEquals(0, africaCountries.size());
    }


}
