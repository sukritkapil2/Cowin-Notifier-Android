package com.cowicheck.cowinnotifier.API;

import androidx.annotation.NonNull;

import com.cowicheck.cowinnotifier.Models.CenterList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Centers {
    @Headers({"User-agent: Mozilla/5.0, Content-Type: application/json"})
    @GET("appointment/sessions/public/calendarByDistrict")
    Call<CenterList> getCentersByDistrict(@Query("district_id") long districtId, @Query("date") String date);

    @Headers({"User-agent: Mozilla/5.0, Content-Type: application/json"})
    @GET("appointment/sessions/public/calendarByPin")
    Call<CenterList> getCentersByPin(@Query("pincode") int pin, @Query("date") String date);
}
