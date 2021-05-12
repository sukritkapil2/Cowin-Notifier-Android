package com.cowicheck.cowinnotifier.Models;

import com.google.gson.annotations.SerializedName;

public class Session {

    @SerializedName("session_id")
    public String session_id;

    @SerializedName("date")
    public String date;

    @SerializedName("available_capacity")
    public int available_capacity;

    @SerializedName("min_age_limit")
    public int min_age_limit;

    @SerializedName("vaccine")
    public String vaccine;

    @SerializedName("slots")
    public String[] slots;

    public Session(String session_id, String date, int available_capacity, int min_age_limit, String vaccine, String[] slots) {
        this.session_id = session_id;
        this.date = date;
        this.available_capacity = available_capacity;
        this.min_age_limit = min_age_limit;
        this.vaccine = vaccine;
        this.slots = slots;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAvailable_capacity() {
        return available_capacity;
    }

    public void setAvailable_capacity(int available_capacity) {
        this.available_capacity = available_capacity;
    }

    public int getMin_age_limit() {
        return min_age_limit;
    }

    public void setMin_age_limit(int min_age_limit) {
        this.min_age_limit = min_age_limit;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public String[] getSlots() {
        return slots;
    }

    public void setSlots(String[] slots) {
        this.slots = slots;
    }
}
