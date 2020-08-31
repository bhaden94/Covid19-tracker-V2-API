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
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@EnableScheduling
public class CovidDataServices {

    private final String URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports_us/";

    @Autowired
    private UsStateRepository statesRepo;

    // scheduled to run at 0615 UTC everyday
    // @Scheduled(cron = "0 15 6 * * *", zone = "UTC")
    @PostConstruct
    public void fetchDailyStateStats() throws IOException, InterruptedException {
        String formattedDate = getFormattedDate();
        StateDoc doc = new StateDoc();
        List<UsState> states = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(URL + formattedDate + ".csv"))
                .build();

        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

        StringReader csvBodyReader = new StringReader(res.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(csvBodyReader);
        for(CSVRecord record : records) {
            UsState state = new UsState();
            state.setState(record.get("Province_State"));
            states.add(state);
        }
        doc.setDate(LocalDate.now(ZoneId.of("UTC")));
        doc.setStates(states);
        statesRepo.save(doc);
    }

    private String getFormattedDate() {
        LocalDate date = LocalDate.now(ZoneId.of("UTC"));
        return date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
    }

}
