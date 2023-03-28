package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.entityDB.Country;
import ch.uzh.ifi.hase.soprafs23.entityDB.Outline;
import ch.uzh.ifi.hase.soprafs23.entityOther.Location;
import ch.uzh.ifi.hase.soprafs23.repository.CountryRepository;
import ch.uzh.ifi.hase.soprafs23.repository.OutlineRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
@Transactional
public class CountryService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final CountryRepository countryRepository;
private final OutlineRepository outlineRepository;

    @Autowired
    public CountryService(@Qualifier("countryRepository") CountryRepository countryRepository, @Qualifier("outlineRepository") OutlineRepository outlineRepository){
        this.countryRepository = countryRepository;
        this.outlineRepository = outlineRepository;
        this.setAllCountries();
    }

    public List<Country> getAllCountries() {
        return this.countryRepository.findAll();
    }

    public Long getAllCountryIdsWithRandomId() {
        List<Long> allCountryIds = new ArrayList<>(this.countryRepository.getAllCountryIds());
                Collections.shuffle(allCountryIds);
        Long randomCountryId = allCountryIds.get(0);
        return randomCountryId;
    }


    private String getCountryOutline(String countryCode, JSONArray outlines){

        for (Object featureObj : outlines) {
            JSONObject feature = (JSONObject) featureObj;
            JSONObject properties = (JSONObject) feature.get("properties");
            if (countryCode.equals(properties.get("ISO_A3"))) {
                return  featureObj.toString();
            }
        }
        return null;
    }

    public JSONArray fetchCountriesAPI(JSONArray countries) throws IOException, ParseException{

            URL url = new URL("https://restcountries.com/v3.1/all");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            StringBuilder responseBuilder = new StringBuilder();
            while (scanner.hasNext()) {
                responseBuilder.append(scanner.nextLine());
            }
            String response = responseBuilder.toString();

            // Parse the JSON response and extract the name and population of each country
            JSONParser parser = new JSONParser();
            countries = (JSONArray) parser.parse(response);
            System.out.println("Set countries with API");
        return countries;
    }

    public void setAllCountries() {
        if (countryRepository.getAllCountryIds().size() == 0) {
            JSONArray countries = null;
            try{
                countries = fetchCountriesAPI(countries);
            }
            catch (IOException | ParseException e) {
                JSONParser parser = new JSONParser();
                try {
                    countries = (JSONArray) parser.parse(new FileReader("src/main/resources/countries.json"));
                    System.out.println("Set countries with File");
                }
                catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
            }

            try {
                JSONParser parser = new JSONParser();
                JSONObject outlines_Object = (JSONObject) parser.parse(new FileReader("src/main/resources/outlines.json"));
                JSONArray outlines = (JSONArray) outlines_Object.get("features");

                for (Object obj : countries) {
                    try {
                        JSONObject country = (JSONObject) obj;
                        String name = (String) ((JSONObject) country.get("name")).get("common");
                        long population = (long) country.get("population");
                        String flag = (String) ((JSONObject) country.get("flags")).get("svg");
                        String region = (String) country.get("region");
                        String outline = null;
                        String countryCode = null;
                        String capital;
                        List<Double> latlngList = null;
                        Set<Double> latlng = null;
                        latlngList = (List<Double>) ((JSONObject) country.get("capitalInfo")).get("latlng");
                        Location location = new Location();
                        location.setLatitude(latlngList.get(0));
                        location.setLongitude(latlngList.get(1));

                        capital = (String) ((JSONArray) country.get("capital")).get(0);
                        countryCode = (String) country.get("cca3");
                        outline = getCountryOutline(countryCode, outlines);


                        Country newCountry = new Country();
                        newCountry.setName(name);
                        newCountry.setRegion(region);
                        newCountry.setCountryCode(countryCode);
                        newCountry.setCapital(capital);
                        newCountry.setPopulation(population);
                        newCountry.setFlag(flag);
                        newCountry.setLocation(location);

                        Outline outline1 = new Outline();
                        outline1.setOutline(outline);
                        outlineRepository.saveAndFlush(outline1);
                        newCountry.setOutline(outline1);

                        countryRepository.save(newCountry);
                    }
                    catch (Exception e) {
                    }
                    countryRepository.flush();
                }
            }

            catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

}
