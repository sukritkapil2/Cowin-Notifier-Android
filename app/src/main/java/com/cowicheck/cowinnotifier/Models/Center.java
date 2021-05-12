package com.cowicheck.cowinnotifier.Models;

import com.google.gson.annotations.SerializedName;

public class Center {

    @SerializedName("center_id")
    public int center_id;

    @SerializedName("name")
    public String name;

    @SerializedName("address")
    public String address;

    @SerializedName("state_name")
    public String state_name;

    @SerializedName("district_name")
    public String district_name;

    @SerializedName("block_name")
    public String block_name;

    @SerializedName("pincode")
    public int pincode;

    @SerializedName("latitude")
    public int latitude;

    @SerializedName("longitude")
    public int longitude;

    @SerializedName("from")
    public String from;

    @SerializedName("to")
    public String to;

    @SerializedName("fee_type")
    public String fee_type;

    @SerializedName("sessions")
    public Session[] sessions;

    public Center(int center_id, String name, String address, String state_name, String district_name, String block_name, int pincode, int latitude, int longitude, String from, String to, String fee_type, Session[] sessions) {
        this.center_id = center_id;
        this.name = name;
        this.address = address;
        this.state_name = state_name;
        this.district_name = district_name;
        this.block_name = block_name;
        this.pincode = pincode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.from = from;
        this.to = to;
        this.fee_type = fee_type;
        this.sessions = sessions;
    }

    public int getCenter_id() {
        return center_id;
    }

    public void setCenter_id(int center_id) {
        this.center_id = center_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getBlock_name() {
        return block_name;
    }

    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public Session[] getSessions() {
        return sessions;
    }

    public void setSessions(Session[] sessions) {
        this.sessions = sessions;
    }
}
