package com.example.ecovid19_update.Data;

import androidx.fragment.app.FragmentActivity;

import java.util.List;

public class TopCountriesData {
    private String country, flag;
    private int cases, deaths, recovered, active;

    public TopCountriesData(String country, String flag, int cases, int deaths, int recovered, int active) {
        this.country = country;
        this.flag = flag;
        this.cases = cases;
        this.deaths = deaths;
        this.recovered = recovered;
        this.active = active;
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
}
