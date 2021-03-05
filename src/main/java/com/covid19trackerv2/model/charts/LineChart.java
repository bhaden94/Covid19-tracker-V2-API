package com.covid19trackerv2.model.charts;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;

public class LineChart {

    @JsonIgnore
    private String _id;
    private LocalDate date;
    private Long confirmed;
    private Long deaths;
    private Long active;
    private Long recovered;

    public LineChart(String _id, LocalDate date, Long confirmed, Long deaths, Long active, Long recovered) {
        this._id = _id;
        this.date = date;
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.active = active;
        this.recovered = recovered;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Long confirmed) {
        this.confirmed = confirmed;
    }

    public Long getDeaths() {
        return deaths;
    }

    public void setDeaths(Long deaths) {
        this.deaths = deaths;
    }

    public Long getActive() {
        return active;
    }

    public void setActive(Long active) {
        this.active = active;
    }

    public Long getRecovered() {
        return recovered;
    }

    public void setRecovered(Long recovered) {
        this.recovered = recovered;
    }
}
