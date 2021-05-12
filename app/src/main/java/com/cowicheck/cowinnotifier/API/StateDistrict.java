package com.cowicheck.cowinnotifier.API;

import com.cowicheck.cowinnotifier.Models.CenterList;
import com.cowicheck.cowinnotifier.Models.DistrictList;
import com.cowicheck.cowinnotifier.Models.StateList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StateDistrict {
    @Headers({"User-agent: Mozilla/5.0"})
    @GET("admin/location/states")
    Call<StateList> getStates();

    @Headers({"User-agent: Mozilla/5.0"})
    @GET("admin/location/districts/{id}")
    Call<DistrictList> getDistricts(@Path("id") long id);

    @Headers({"User-agent: Mozilla/5.0"})
    @GET("appointment/sessions/public/calendarByDistrict")
    Call<CenterList> getCenters(@Query("district_id") long districtId, @Query("date") String date);
}
