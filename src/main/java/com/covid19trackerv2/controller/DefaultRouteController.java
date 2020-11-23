package com.covid19trackerv2.controller;

import com.covid19trackerv2.model.country.CountryDoc;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/")
public class DefaultRouteController {

    @GetMapping("")
    public ResponseEntity<String> defaultRoute() {
        return ResponseEntity.ok("This is the default route for the Covid-19 tracker API.");
    }

}
