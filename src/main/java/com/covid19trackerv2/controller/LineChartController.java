package com.covid19trackerv2.controller;

import com.covid19trackerv2.model.charts.LineChart;
import com.covid19trackerv2.repository.CountryRepository;
import com.covid19trackerv2.repository.UsStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class LineChartController {

    @Autowired
    private UsStateRepository statesRepo;

    @Autowired
    private CountryRepository countryRepo;

    @GetMapping("/state/line_chart")
    public ResponseEntity<List<LineChart>> getStateLineChart(@RequestParam(required = false) String name) {
        AggregationResults<LineChart> chart =
                name == null ? statesRepo.aggregateAllStates(Sort.by(Sort.Direction.ASC, "date"))
                        : statesRepo.aggregateOneState(name.toLowerCase(), Sort.by(Sort.Direction.ASC, "date"));

        return ResponseEntity.ok().body(chart.getMappedResults());
    }

    @GetMapping("/country/line_chart")
    public ResponseEntity<List<LineChart>> getCountryLineChart(@RequestParam(required = false) String name) {
        AggregationResults<LineChart> chart =
                name == null ? countryRepo.aggregateAllCountries(Sort.by(Sort.Direction.ASC, "date"))
                        : countryRepo.aggregateOneCountry(name.toLowerCase(), Sort.by(Sort.Direction.ASC, "date"));

        return ResponseEntity.ok().body(chart.getMappedResults());
    }
}
