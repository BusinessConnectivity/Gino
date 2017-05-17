package com.bizconnectivity.gino.services;

import com.bizconnectivity.gino.models.Pulses;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EventbriteAPI {

    @GET("events/search/")
    Call<Pulses> getEvents(@Query("token") String token,
                           @Query("sort_by") String sort_by,
                           @Query("location.address") String location_address,
                           @Query("start_date.keyword") String start_date_keyword,
                           @Query("expand") String expand);
}
