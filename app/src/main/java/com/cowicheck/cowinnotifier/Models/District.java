package com.cowicheck.cowinnotifier.Models;

import com.google.gson.annotations.SerializedName;

public class District {
    @SerializedName("district_id")
    Integer district_id;

    @SerializedName("district_name")
    String district_name;

    public District(Integer district_id, String district_name) {
        this.district_id = district_id;
        this.district_name = district_name;
    }

    public Integer getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(Integer district_id) {
        this.district_id = district_id;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }
}
