package com.covid19trackerv2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class AsyncDBLoad {

    final CovidDataServices service;

    public AsyncDBLoad(CovidDataServices service) {
        this.service = service;
    }

    @PostConstruct
    public void initializeAsyncDB() throws IOException, InterruptedException {
        service.populateDBWithCountryData();
    }

    @PostConstruct
    public void initializeAsyncDB2() throws IOException, InterruptedException {
        service.populateDbWithStateData();
    }
}
