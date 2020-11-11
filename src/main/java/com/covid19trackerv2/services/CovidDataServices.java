package com.covid19trackerv2.services;

import com.covid19trackerv2.model.country.Country;
import com.covid19trackerv2.model.country.CountryDoc;
import com.covid19trackerv2.model.state.StateDoc;
import com.covid19trackerv2.model.state.UsState;
import com.covid19trackerv2.repository.CountryRepository;
import com.covid19trackerv2.repository.UsStateRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class CovidDataServices {

    /* STATE CONSTANTS */
    private final String US_STATE_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports_us/";
    private final int STATE_START_YEAR = 2020;
    private final int STATE_START_MONTH = 4;
    private final int STATE_START_DAY = 12;

    /* COUNTRY CONSTANTS */
    private final String COUNTRY_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/";
    private final int COUNTRY_START_YEAR = 2020;
    private final int COUNTRY_START_MONTH = 1;
    private final int COUNTRY_START_DAY = 22;

    /* CSV HEADER CONSTANTS */
    // general
    private final String CONFIRMED = "Confirmed";
    private final String DEATHS = "Deaths";
    private final String RECOVERED = "Recovered";
    private final String ACTIVE = "Active";
    private final String INCIDENT_RATE = "Incident_Rate";
    private final String MORTALITY_AFTER_NOV_9 = "Case_Fatality_Ratio";
    // state specific
    private final String PROVINCE_STATE = "Province_State";
    private final String MORTALITY_RATE = "Mortality_Rate";
    // country specific
    private final String COUNTRY_REGION_SLASH = "Country/Region";
    private final String COUNTRY_REGION_UNDERSCORE = "Country_Region";
    private final String COUNTRY_INCIDENCE_RATE_BEFORE_NOV_9 = "Incidence_Rate";
    private final String COUNTRY_MORTALITY_BEFORE_NOV_9 = "Case-Fatality_Ratio";

    /* REPOSITORIES */
    @Autowired
    private UsStateRepository statesRepo;

    @Autowired
    private CountryRepository countryRepo;


    /* METHODS */

    // run at 0615 UTC everyday in AsyncDBLoad class
    public void fetchDailyStateStats() throws IOException, InterruptedException {
        // data lags behind one day so we minus by one day
        LocalDate date = LocalDate.now(ZoneId.of("UTC")).minusDays(1);

        System.out.println("Start daily state stats fetch");
        if (this.statesRepo.findByDate(date).isEmpty()) {
            String formattedDate = getFormattedDate();
            Iterable<CSVRecord> records = getRecords(formattedDate, US_STATE_URL);
            List<UsState> states = createStatesList(records);
            StateDoc doc = new StateDoc();
            doc.setDate(date);
            doc.setStates(states);
            System.out.println("Saving to state DB");
            this.statesRepo.save(doc);
        }
        System.out.println("End daily state stats fetch");
    }

    // run at 0615 UTC everyday in AsyncDBLoad class
    public void fetchDailyCountryStats() throws IOException, InterruptedException {
        // data lags behind one day so we minus by one day
        LocalDate date = LocalDate.now(ZoneId.of("UTC")).minusDays(1);

        System.out.println("Start daily country stats fetch");
        if (this.countryRepo.findByDate(date).isEmpty()) {
            String formattedDate = getFormattedDate();
            Iterable<CSVRecord> records = getRecords(formattedDate, COUNTRY_URL);
            List<Country> countries = createCountryList(records);
            CountryDoc doc = new CountryDoc();
            doc.setDate(date);
            doc.setCountries(countries);
            System.out.println("Saving to country DB");
            this.countryRepo.save(doc);
        }
        System.out.println("End daily country stats fetch");
    }

    // this method will be loaded concurrently in a thread at startup from AsyncDBLoad class
    public void populateDbWithStateData() throws IOException, InterruptedException {
        System.out.println("Start state DB population");
        LocalDate startDate = LocalDate.of(STATE_START_YEAR, STATE_START_MONTH, STATE_START_DAY);
        LocalDate today = LocalDate.now(ZoneId.of("UTC"));

        long daysBetween = DAYS.between(startDate, today);
        LocalDate current = startDate;
        for (long i = daysBetween; i >= 2; i--) {
            if (this.statesRepo.findByDate(current).isEmpty()) {
                String formattedDate = getFormattedDate(current);
                Iterable<CSVRecord> records = getRecords(formattedDate, US_STATE_URL);
                List<UsState> states = createStatesList(records);
                StateDoc doc = new StateDoc();
                doc.setDate(current);
                doc.setStates(states);
                this.statesRepo.save(doc);
            }
            current = current.plusDays(1);
        }
        System.out.println("exit state DB population");
    }

    // this method will be loaded concurrently in a thread at startup from AsyncDBLoad class
    public void populateDBWithCountryData() throws IOException, InterruptedException {
        System.out.println("Start country DB population");
        LocalDate startDate = LocalDate.of(COUNTRY_START_YEAR, COUNTRY_START_MONTH, COUNTRY_START_DAY);
        LocalDate today = LocalDate.now(ZoneId.of("UTC"));

        long daysBetween = DAYS.between(startDate, today);
        LocalDate current = startDate;
        for (long i = daysBetween; i >= 2; i--) {
            if (this.countryRepo.findByDate(current).isEmpty()) {
                String formattedDate = getFormattedDate(current);
                Iterable<CSVRecord> records = getRecords(formattedDate, COUNTRY_URL);
                List<Country> countries = createCountryList(records);
                CountryDoc doc = new CountryDoc();
                doc.setDate(current);
                doc.setCountries(countries);
                this.countryRepo.save(doc);
            }
            current = current.plusDays(1);
        }
        System.out.println("exit country DB population");
    }

    /* ---- Helper methods ---- */

    private Iterable<CSVRecord> getRecords(String formattedDate, String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + formattedDate + ".csv"))
                .build();

        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

        StringReader csvBodyReader = new StringReader(res.body());
        return CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(csvBodyReader);
    }

    private List<UsState> createStatesList(Iterable<CSVRecord> records) {
        List<UsState> states = new ArrayList<>();
        for (CSVRecord record : records) {
            UsState state = new UsState();
            state.setState(record.get(PROVINCE_STATE).toLowerCase());
            state.setConfirmed(getLongValueFromRecord(record.get(CONFIRMED)));
            state.setDeaths(getLongValueFromRecord(record.get(DEATHS)));
            state.setRecovered(getLongValueFromRecord(record.get(RECOVERED)));
            state.setActive(getLongValueFromRecord(record.get(ACTIVE)));
            state.setIncidentRate(getDoubleValueFromRecord(record.get(INCIDENT_RATE)));
            // Nov 9th and beyond is Case_Fatality_Ratio
            if(record.isMapped(MORTALITY_RATE)) {
                state.setMortalityRate(getDoubleValueFromRecord(record.get(MORTALITY_RATE)));
            } else {
                state.setMortalityRate(getDoubleValueFromRecord(record.get(MORTALITY_AFTER_NOV_9)));
            }

            states.add(state);
        }
        return states;
    }

    private List<Country> createCountryList(Iterable<CSVRecord> records) {
        Map<String, Country> countries = new HashMap<>();
        for (CSVRecord record : records) {
            Country country = new Country();
            if (record.isMapped(COUNTRY_REGION_SLASH)) {
                country.setCountry(record.get(COUNTRY_REGION_SLASH).toLowerCase());
            } else {
                country.setCountry(record.get(COUNTRY_REGION_UNDERSCORE).toLowerCase());
            }
            // special case where the name of south korea has changed throughout data
            if (country.getCountry().equals("korea, south") ||
                    country.getCountry().equals("republic of korea")) {
                country.setCountry("south korea");
            }
            // special case for congo countries that have parentheses in them
            // must remove them to make working with this data on the frontend easier
            country.setCountry(country.getCountry().replaceAll("[()]", ""));

            // values that are in all records
            country.setConfirmed(getLongValueFromRecord(record.get(CONFIRMED)));
            country.setDeaths(getLongValueFromRecord(record.get(DEATHS)));
            country.setRecovered(getLongValueFromRecord(record.get(RECOVERED)));

            // values that are not in all records
            if (record.isMapped(ACTIVE)) {
                country.setActive(getLongValueFromRecord(record.get(ACTIVE)));
            } else {
                country.setActive(0L);
            }
            if (record.isMapped(COUNTRY_INCIDENCE_RATE_BEFORE_NOV_9)) {
                country.setIncidentRate(getDoubleValueFromRecord(record.get(COUNTRY_INCIDENCE_RATE_BEFORE_NOV_9)));
            } else if (record.isMapped(INCIDENT_RATE)) {
                country.setIncidentRate(getDoubleValueFromRecord(record.get(INCIDENT_RATE)));
            } else {
                country.setIncidentRate(0.0);
            }
            // Nov 9th and beyond is Case_Fatality_Ratio
            if (record.isMapped(COUNTRY_MORTALITY_BEFORE_NOV_9)) {
                country.setMortalityRate(getDoubleValueFromRecord(record.get(COUNTRY_MORTALITY_BEFORE_NOV_9)));
            } else if(record.isMapped(MORTALITY_AFTER_NOV_9)) {
                country.setMortalityRate(getDoubleValueFromRecord(record.get(MORTALITY_AFTER_NOV_9)));
            } else {
                country.setMortalityRate(0.0);
            }

            // if our country is already there then add the values up
            if (countries.containsKey(country.getCountry())) {
                Country existing = countries.get(country.getCountry());
                existing.setConfirmed(existing.getConfirmed() + country.getConfirmed());
                existing.setDeaths(existing.getDeaths() + country.getDeaths());
                existing.setRecovered(existing.getRecovered() + country.getRecovered());
                existing.setActive(existing.getActive() + country.getActive());
                // carry over the incident rate and mortality rate as it is calculated already
                existing.setIncidentRate(country.getIncidentRate());
                existing.setMortalityRate(country.getMortalityRate());
                countries.put(country.getCountry(), existing);
            } else {
                countries.put(country.getCountry(), country);
            }
        }
        // return the values in the Map as an ArrayList
        return new ArrayList<>(countries.values());
    }

    private long getLongValueFromRecord(String numToParse) {
        long num = 0;
        int dotIndex = numToParse.indexOf('.');
        if (dotIndex != -1) {
            numToParse = numToParse.substring(0, dotIndex);
        }
        try {
            num = Long.parseLong(numToParse);
        } catch (NumberFormatException ignored) {
        }
        return num;
    }

    private double getDoubleValueFromRecord(String numToParse) {
        double num = 0.0;
        try {
            num = Double.parseDouble(numToParse);
        } catch (NumberFormatException ignored) {
        }
        return num;
    }

    private String getFormattedDate() {
        // data lags behind one day so we minus by one day
        LocalDate date = LocalDate.now(ZoneId.of("UTC")).minusDays(1);
        return date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
    }

    private String getFormattedDate(LocalDate current) {
        return current.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
    }

}
