package com.covid19trackerv2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@EnableScheduling
public class AsyncDBLoad extends Thread {

    @Autowired
    private CovidDataServices service;

    Thread.UncaughtExceptionHandler exceptionHandler =
            (th, ex) -> System.out.println("Uncaught exception: " + ex.getMessage()
                    + "\nIn Thread: " + th.getName());

    // scheduled to run at 0615 UTC everyday
    @Scheduled(cron = "0 15 6 * * *", zone = "UTC")
    public void fetchDailyStats() {
        Thread stateThread = new Thread(() -> {
            try {
                service.fetchDailyStateStats();
            } catch (InterruptedException | IOException e) {
                System.out.println("State daily scheduled fetch thread error");
            }
        });
        Thread countryThread = new Thread(() -> {
            try {
                service.fetchDailyCountryStats();
            } catch (InterruptedException | IOException e) {
                System.out.println("Country daily scheduled fetch thread error");
            }
        });

        // set error handlers
        stateThread.setUncaughtExceptionHandler(exceptionHandler);
        countryThread.setUncaughtExceptionHandler(exceptionHandler);
        // set names
        stateThread.setName("State DB daily fetch thread");
        countryThread.setName("Country DB daily fetch thread");
        // start the threads
        stateThread.start();
        countryThread.start();
    }

    @PostConstruct
    public void initializeAsyncDB() {
        // Thread to populate state DB
        Thread stateThread = new Thread(() -> {
            try {
                service.populateDbWithStateData();
            } catch (InterruptedException | IOException e) {
                System.out.println("State DB population thread error");
            }
        });
        // Thread to populate country DB
        Thread countryThread = new Thread(() -> {
            try {
                service.populateDBWithCountryData();
            } catch (InterruptedException | IOException e) {
                System.out.println("Country DB population thread error");
            }
        });

        // set error handlers
        stateThread.setUncaughtExceptionHandler(exceptionHandler);
        countryThread.setUncaughtExceptionHandler(exceptionHandler);
        // set names
        stateThread.setName("State DB population thread");
        countryThread.setName("Country DB population thread");
        // start the threads
        stateThread.start();
        countryThread.start();
    }
}
