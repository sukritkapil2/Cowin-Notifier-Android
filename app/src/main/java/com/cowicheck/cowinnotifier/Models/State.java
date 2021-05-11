package com.cowicheck.cowinnotifier.Models;

import com.google.gson.annotations.SerializedName;

public class State {
    @SerializedName("state_id")
    Integer state_id;

    @SerializedName("state_name")
    String state_name;

    public State(Integer state_id, String state_name) {
        this.state_id = state_id;
        this.state_name = state_name;
    }

    public Integer getState_id() {
        return state_id;
    }

    public void setState_id(Integer state_id) {
        this.state_id = state_id;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }
}
