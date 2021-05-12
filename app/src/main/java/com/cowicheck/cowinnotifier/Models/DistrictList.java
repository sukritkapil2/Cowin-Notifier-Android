package com.cowicheck.cowinnotifier.Models;

import com.google.gson.annotations.SerializedName;

public class DistrictList {
    @SerializedName("districts")
    District[] districts;

    @SerializedName("ttl")
    Integer length;

    public DistrictList(District[] districts, Integer length) {
        this.districts = districts;
        this.length = length;
    }

    public District[] getDistricts() {
        return districts;
    }

    public void setDistricts(District[] districts) {
        this.districts = districts;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
