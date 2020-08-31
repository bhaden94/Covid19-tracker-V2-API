package com.covid19trackerv2.model.country;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class CountryDoc {

    private String id;
    private LocalDate date;
    private List<Country> countries;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }
}
