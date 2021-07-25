package com.example.covidapps;

public class CovidClass {
    String userName, code, lat, lon, additional;

    public CovidClass()
    {

    }
    public CovidClass(String userName, String code, String lat, String lon, String additional)
    {
        this.userName = userName;
        this.code = code;
        this.lat = lat;
        this.lon = lon;
        this.additional = additional;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }
}
