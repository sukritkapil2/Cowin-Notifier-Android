package com.cowicheck.cowinnotifier.Models;

import java.util.ArrayList;

public class UserData {
    String placeType;
    String districtName;
    String stateName;
    long districtId;
    Integer pin;
    String date;
    ArrayList<CenterSession> foundSessions;

    public UserData(String placeType, String districtName, String stateName, long districtId, Integer pin, String date, ArrayList<CenterSession> foundSessions) {
        this.placeType = placeType;
        this.districtName = districtName;
        this.stateName = stateName;
        this.districtId = districtId;
        this.pin = pin;
        this.date = date;
        this.foundSessions = foundSessions;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(long districtId) {
        this.districtId = districtId;
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<CenterSession> getFoundSessions() {
        return foundSessions;
    }

    public void setFoundSessions(ArrayList<CenterSession> foundSessions) {
        this.foundSessions = foundSessions;
    }
}
