package com.example.ecovid19_update.Data;

public class CountriesTotal {
    private String country, flag;
    private int cases, deaths, recovered, active, todayCases;

    public CountriesTotal(String country, String flag, int cases, int deaths, int recovered, int active, int todayCases) {
        this.country = country;
        this.flag = flag;
        this.cases = cases;
        this.deaths = deaths;
        this.recovered = recovered;
        this.active = active;
        this.todayCases = todayCases;
    }

    public String getCountry() {
        return country;
    }

    public String getFlag() {
        return flag;
    }

    public int getCases() {
        return cases;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getRecovered() {
        return recovered;
    }

    public int getActive() {
        return active;
    }

    public int getTodayCases() {
        return todayCases;
    }
}
