package ru.example.makaroff.wheathervideo.io.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather{

    @JsonProperty("cod")
    protected String cod;

    public String getCod(){
        return cod;
    }
}
