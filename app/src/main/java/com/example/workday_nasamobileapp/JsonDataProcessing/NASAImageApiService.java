package com.example.workday_nasamobileapp.JsonDataProcessing;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NASAImageApiService {
    private static final String BASE_URL = "https://images-api.nasa.gov/";

    private static Retrofit retrofit;

    public static NASAImageApi getApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(NASAImageApi.class);
    }
}
