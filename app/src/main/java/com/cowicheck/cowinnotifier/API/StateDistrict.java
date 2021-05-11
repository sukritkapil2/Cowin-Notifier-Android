package com.cowicheck.cowinnotifier.API;

import com.cowicheck.cowinnotifier.Models.StateList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface StateDistrict {
    @Headers({"User-agent: Mozilla/5.0"})
    @GET("admin/location/states")
    Call<StateList> getStates();
}
