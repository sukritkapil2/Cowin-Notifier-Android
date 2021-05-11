package com.cowicheck.cowinnotifier.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StateList {
    @SerializedName("states")
    State[] states;

    @SerializedName("ttl")
    Integer length;

    public StateList(State[] states, Integer length) {
        this.states = states;
        this.length = length;
    }

    public State[] getStates() {
        return states;
    }

    public void setStates(State[] states) {
        this.states = states;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
