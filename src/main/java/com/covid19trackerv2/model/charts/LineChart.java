package com.covid19trackerv2.model.charts;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.sound.sampled.Line;
import java.time.LocalDate;

public class LineChart {
    @JsonProperty("date")
    private LocalDate _id;
    private Long confirmed;
    private Long deaths;
    private Long active;
    private Long recovered;

    public LineChart(LocalDate _id, Long confirmed, Long deaths, Long active, Long recovered) {
        this._id = _id;
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.active = active;
        this.recovered = recovered;
    }

    public LocalDate get_id() {
        return _id;
    }

    public void set_id(LocalDate _id) {
        this._id = _id;
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
