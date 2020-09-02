package com.covid19trackerv2.model.state;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UsState {

    private String state;
    private long confirmed;
    private long deaths;
    private long recovered;
    private long active;
    @JsonProperty("incident_rate")
    private double incidentRate;
    @JsonProperty("mortality_rate")
    private double mortalityRate;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(long confirmed) {
        this.confirmed = confirmed;
    }

    public long getDeaths() {
        return deaths;
    }

    public void setDeaths(long deaths) {
        this.deaths = deaths;
    }

    public long getRecovered() {
        return recovered;
    }

    public void setRecovered(long recovered) {
        this.recovered = recovered;
    }

    public long getActive() {
        return active;
    }

    public void setActive(long active) {
        this.active = active;
    }

    public double getIncidentRate() {
        return incidentRate;
    }

    public void setIncidentRate(double incidentRate) {
        this.incidentRate = incidentRate;
    }

    public double getMortalityRate() {
        return mortalityRate;
    }

    public void setMortalityRate(double mortalityRate) {
        this.mortalityRate = mortalityRate;
    }
}
