package com.cowicheck.cowinnotifier.Models;

import com.google.gson.annotations.SerializedName;

public class CenterList {

    @SerializedName("centers")
    public Center[] centers;

    public CenterList(Center[] centers) {
        this.centers = centers;
    }

    public Center[] getCenters() {
        return centers;
    }

    public void setCenters(Center[] centers) {
        this.centers = centers;
    }
}
