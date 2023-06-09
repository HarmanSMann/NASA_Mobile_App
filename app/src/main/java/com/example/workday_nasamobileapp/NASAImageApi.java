package com.example.workday_nasamobileapp;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NASAImageApi {
    @GET("search")
    Call<ResponseBody> searchImages(@Query("q") String query, @Query("media_type") String mediaType);;
}

