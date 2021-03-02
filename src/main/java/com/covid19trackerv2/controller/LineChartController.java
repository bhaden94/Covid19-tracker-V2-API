package com.covid19trackerv2.controller;

import com.covid19trackerv2.model.charts.LineChart;
import com.covid19trackerv2.repository.CountryRepository;
import com.covid19trackerv2.repository.UsStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class LineChartController {

    @Autowired
    private UsStateRepository statesRepo;

    @Autowired
    private CountryRepository countryRepo;

    @GetMapping("/state/bar_chart")
    public ResponseEntity<LineChart[]> getStateLineChart(@RequestParam(required = false) String name) {

    }
}
