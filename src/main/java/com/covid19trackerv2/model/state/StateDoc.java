package com.covid19trackerv2.model.state;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Document(collection = "us_collection")
public class StateDoc {

    @Id
    private String id;
    @JsonFormat(pattern = "MM-dd-yyyy")
    private LocalDate date;
    private List<UsState> states;

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

    public List<UsState> getStates() {
        return states;
    }

    public void setStates(List<UsState> states) {
        this.states = states;
    }
}
