package com.covid19trackerv2.services;

import com.covid19trackerv2.model.state.StateDoc;
import com.covid19trackerv2.model.state.UsState;
import com.covid19trackerv2.repository.UsStateRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@EnableScheduling
public class CovidDataServices {

    private final String US_STATE_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports_us/";
    private final int STATE_START_YEAR = 2020;
    private final int STATE_START_MONTH = 4;
    private final int STATE_START_DAY = 12;

    @Autowired
    private UsStateRepository statesRepo;

    // scheduled to run at 0615 UTC everyday
    @Scheduled(cron = "0 15 6 * * *", zone = "UTC")
    public void fetchDailyStateStats() throws IOException, InterruptedException {
        String formattedDate = getFormattedDate();
        Iterable<CSVRecord> records = getRecords(formattedDate);
        List<UsState> states = createStatesList(records);
        StateDoc doc = new StateDoc();

        doc.setDate(LocalDate.now(ZoneId.of("UTC")));
        doc.setStates(states);
        this.statesRepo.save(doc);
    }

    // this will populate the data in the database by going through all the files
    @PostConstruct
    public void populateDbWithStateData() throws IOException, InterruptedException {
        LocalDate startDate = LocalDate.of(STATE_START_YEAR, STATE_START_MONTH, STATE_START_DAY);
        LocalDate today = LocalDate.now(ZoneId.of("UTC"));

        long daysBetween = DAYS.between(startDate, today);
        LocalDate current = startDate;
        for(long i=daysBetween; i>=2; i--) {
            if(this.statesRepo.findByDate(current) == null) {
                String formattedDate = getFormattedDate(current);
                Iterable<CSVRecord> records = getRecords(formattedDate);
                List<UsState> states = createStatesList(records);
                StateDoc doc = new StateDoc();
                doc.setDate(current);
                doc.setStates(states);
                this.statesRepo.save(doc);
                current = current.plusDays(1);
            }
        }
    }

    private Iterable<CSVRecord> getRecords(String formattedDate) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(US_STATE_URL + formattedDate + ".csv"))
                .build();

        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

        StringReader csvBodyReader = new StringReader(res.body());
        return CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(csvBodyReader);
    }

    private List<UsState> createStatesList(Iterable<CSVRecord> records) {
        List<UsState> states = new ArrayList<>();
        for(CSVRecord record : records) {
            UsState state = new UsState();
            state.setState(record.get("Province_State"));
            states.add(state);
        }
        return states;
    }

    private String getFormattedDate() {
        LocalDate date = LocalDate.now(ZoneId.of("UTC"));
        return date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
    }

    private String getFormattedDate(LocalDate current) {
        return current.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
    }

}
