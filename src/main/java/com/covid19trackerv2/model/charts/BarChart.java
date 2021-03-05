package com.covid19trackerv2.model.charts;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BarChart {
    private String rate;
    @JsonProperty("state_country")
    private Double stateCountry;
    @JsonProperty("us_world")
    private Double usWorld;

    public BarChart(String rate) {
        this.rate = rate;
        this.stateCountry = 0.0;
        this.usWorld = 0.0;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Double getStateCountry() {
        return stateCountry;
    }

    public void setStateCountry(Double stateCountry) {
        this.stateCountry = stateCountry;
    }

    public Double getUsWorld() {
        return usWorld;
    }

    public void setUsWorld(Double usWorld) {
        this.usWorld = usWorld;
    }
}
