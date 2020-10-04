package com.covid19trackerv2.services;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class AsyncDBLoad extends Thread {

    final CovidDataServices service;

    Thread.UncaughtExceptionHandler exceptionHandler =
            (th, ex) -> System.out.println("Uncaught exception: " + ex.getMessage()
                    + "\nIn Thread: " + th.getName());

    public AsyncDBLoad(CovidDataServices service) {
        this.service = service;
    }

    @PostConstruct
    public void initializeAsyncDB() {
        // Thread to populate state DB
        Thread stateThread = new Thread(() -> {
            try {
                service.populateDbWithStateData();
            } catch (InterruptedException | IOException e) {
                System.out.println("State thread error");
            }
        });
        // Thread to populate country DB
        Thread countryThread = new Thread(() -> {
            try {
                service.populateDBWithCountryData();
            } catch (InterruptedException | IOException e) {
                System.out.println("Country thread error");
            }
        });

        // set error handlers
        stateThread.setUncaughtExceptionHandler(exceptionHandler);
        countryThread.setUncaughtExceptionHandler(exceptionHandler);
        // set names
        stateThread.setName("State Thread");
        countryThread.setName("Country Thread");
        // start the threads
        stateThread.start();
        countryThread.start();
    }
}
