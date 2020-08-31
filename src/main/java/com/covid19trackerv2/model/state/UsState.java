package com.covid19trackerv2.model.state;

public class UsState {

    private String state;
    private long confirmed;
    private long deaths;
    private long recovered;
    private long active;
    private double incident_rate;
    private double mortality_rate;

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

    public double getIncident_rate() {
        return incident_rate;
    }

    public void setIncident_rate(double incident_rate) {
        this.incident_rate = incident_rate;
    }

    public double getMortality_rate() {
        return mortality_rate;
    }

    public void setMortality_rate(double mortality_rate) {
        this.mortality_rate = mortality_rate;
    }
}
