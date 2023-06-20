package com.example.workday_nasamobileapp;
//Harman Mann
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workday_nasamobileapp.DataAdaptor.ItemAdapter;
import com.example.workday_nasamobileapp.JsonDataProcessing.JsonProcessor;
import com.example.workday_nasamobileapp.JsonDataProcessing.NASAImageApi;
import com.example.workday_nasamobileapp.JsonDataProcessing.NASAImageApiService;
import com.example.workday_nasamobileapp.JsonDataProcessing.PaginationHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PaginationHelper paginationHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button search_btn = findViewById(R.id.btn_search);
        Button prev_btn = findViewById(R.id.btn_previous_page);
        Button next_btn = findViewById(R.id.btn_next_page);
        EditText search_et = findViewById(R.id.search_bar);
        TextView page_num = findViewById(R.id.page_number);

        recyclerView = findViewById(R.id.results_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemAdapter itemAdapter = new ItemAdapter(new ArrayList<>());
        recyclerView.setAdapter(itemAdapter);

        NASAImageApi nasaImageApi = NASAImageApiService.getApi();
        paginationHelper = new PaginationHelper();


        search_btn.setOnClickListener(v -> {
            // Make API call
            if (!search_et.getText().toString().isEmpty()) {
                paginationHelper.setCurrentPage(1);
                page_num.setText(String.valueOf(paginationHelper.getCurrentPage()));
                searchImages(nasaImageApi, search_et.getText().toString());

            } else {
                Toast.makeText(this, "Please Add Search Term", Toast.LENGTH_SHORT).show();
            }
        });

        prev_btn.setOnClickListener(v -> {
            if (paginationHelper.hasPreviousPage()) {
                paginationHelper.goToPreviousPage();
                searchImages(nasaImageApi, search_et.getText().toString());
                page_num.setText(String.valueOf(paginationHelper.getCurrentPage()));
                Toast.makeText(this, "Next Page:" + paginationHelper.getCurrentPage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Can't go back", Toast.LENGTH_SHORT).show();
            }
        });

        next_btn.setOnClickListener(v -> {
            if (paginationHelper.hasNextPage()) {
                paginationHelper.goToNextPage();
                searchImages(nasaImageApi, search_et.getText().toString());
                page_num.setText(String.valueOf(paginationHelper.getCurrentPage()));
                Toast.makeText(this, "Next Page:" + paginationHelper.getCurrentPage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No More Pages", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchImages(NASAImageApi nasaImageApi, String searchTerm) {
        Call<ResponseBody> call = nasaImageApi.searchImages(searchTerm, "image", paginationHelper.getCurrentPage());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        assert response.body() != null;
                        String jsonData = response.body().string();
                        Log.d("@Harman - Api response", "API response: " + jsonData);
                        // Process the JSON data and return data to list
                        int totalHits = getTotalHitsFromJson(jsonData);
                        int pageSize = 100;
                        int totalPages = calculateTotalPages(totalHits, pageSize);
                        paginationHelper.setTotalPages(totalPages);
                        JsonProcessor.processJsonData(jsonData, recyclerView, MainActivity.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Handle JSON parsing error
                    }
                } else {
                    Log.e("@Harman - API failed", "API call failed: " + response.message());
                    // Handle unsuccessful API response
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("api failed", "API call failed: " + t.getMessage());
                // Handle API request failure
            }
        });
    }

    private int getTotalHitsFromJson(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject collectionObject = jsonObject.getJSONObject("collection");
            JSONObject metadataObject = collectionObject.getJSONObject("metadata");
            return metadataObject.optInt("total_hits", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int calculateTotalPages(int totalHits, int pageSize) {
        return (int) Math.ceil((double) totalHits / pageSize);
    }

}
