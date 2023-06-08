package com.example.workday_nasamobileapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workday_nasamobileapp.NASAImageApi;
import com.example.workday_nasamobileapp.R;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://images-api.nasa.gov/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create API service
        NASAImageApi nasaImageApi = retrofit.create(NASAImageApi.class);

        // Make API call
        Call<ResponseBody> call = nasaImageApi.searchImages("moon");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String jsonData = response.body().string();
                        Log.d(TAG, "API response: " + jsonData);
                        // Process the JSON data
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Handle JSON parsing error
                    }
                } else {
                    Log.e(TAG, "API call failed: " + response.message());
                    // Handle unsuccessful API response
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "API call failed: " + t.getMessage());
                // Handle API request failure
            }
        });
    }
}
