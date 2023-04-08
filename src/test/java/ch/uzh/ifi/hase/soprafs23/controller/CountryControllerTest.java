package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.rest.dto.CountryGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@WebMvcTest(CountryController.class)
public class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @Test
    public void createCountries_validInput_countriesCreated() throws Exception {
        List<CountryGetDTO> countryGetDTOS = new ArrayList<>();
        CountryGetDTO country1 = new CountryGetDTO();
        country1.setName("Country1");
        country1.setPopulation(1000000L);
        CountryGetDTO country2 = new CountryGetDTO();
        country2.setName("Country2");
        country2.setPopulation(2000000L);
        countryGetDTOS.add(country1);
        countryGetDTOS.add(country2);

        doNothing().when(countryService).setAllCountries();

        mockMvc.perform(post("/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(countryGetDTOS)))
                .andExpect(status().isCreated());

        verify(countryService, times(1)).setAllCountries();
    }


    @Test
    public void getAllCountries_noCountries_returnEmptyList() throws Exception {
        given(countryService.getAllCountries()).willReturn(new ArrayList<>());

        mockMvc.perform(get("/countries")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void createCountries_noCountries_created() throws Exception {
        doNothing().when(countryService).setAllCountries();

        mockMvc.perform(post("/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isCreated());

        verify(countryService, times(1)).setAllCountries();
    }

    @Test
    public void createCountries_validInput_returnSortedCountries() throws Exception {
        List<CountryGetDTO> countryGetDTOS = new ArrayList<>();
        CountryGetDTO country1 = new CountryGetDTO();
        country1.setName("CountryB");
        country1.setPopulation(1000000L);
        CountryGetDTO country2 = new CountryGetDTO();
        country2.setName("CountryA");
        country2.setPopulation(2000000L);
        countryGetDTOS.add(country1);
        countryGetDTOS.add(country2);

        doNothing().when(countryService).setAllCountries();

        mockMvc.perform(post("/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(countryGetDTOS)))
                .andExpect(status().isCreated());

        verify(countryService, times(1)).setAllCountries();
    }


}
