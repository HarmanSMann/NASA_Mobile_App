package com.example.workday_nasamobileapp;

import android.content.Intent;
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
import com.example.workday_nasamobileapp.DataAdaptor.ItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private int currentPage = 1, totalPages = 0;


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

        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://images-api.nasa.gov/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create API service
        NASAImageApi nasaImageApi = retrofit.create(NASAImageApi.class);

        search_btn.setOnClickListener(v -> {
            // Make API call
            if (!search_et.getText().toString().isEmpty()) {
                currentPage = 1;
                page_num.setText(String.valueOf(currentPage));
                searchImages(nasaImageApi, search_et.getText().toString());

            } else {
                Toast.makeText(this, "Please Add Search Term", Toast.LENGTH_SHORT).show();
            }
        });

        prev_btn.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                searchImages(nasaImageApi, search_et.getText().toString());
                page_num.setText(String.valueOf(currentPage));
                Toast.makeText(this, "Next Page:" + currentPage, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Cant go back", Toast.LENGTH_SHORT).show();
            }
        });

        next_btn.setOnClickListener(v -> {
            if (currentPage < totalPages) {
                currentPage++;
                searchImages(nasaImageApi, search_et.getText().toString());
                page_num.setText(String.valueOf(currentPage));
                Toast.makeText(this, "Next Page:", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No More Pages", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchImages(NASAImageApi nasaImageApi, String searchTerm) {
        Call<ResponseBody> call = nasaImageApi.searchImages(searchTerm, "image", currentPage);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        assert response.body() != null;
                        String jsonData = response.body().string();
                        Log.d("@Harman - Api response", "API response: " + jsonData);
                        // Process the JSON data and return data to list
                        processJsonData(jsonData);
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

    private void processJsonData(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject collectionObject = jsonObject.getJSONObject("collection");
            JSONArray itemsArray = collectionObject.getJSONArray("items");

            JSONObject metadataObject = collectionObject.getJSONObject("metadata");
            int totalHits = metadataObject.optInt("total_hits", 0);
            int pageSize = 100;
            totalPages = calculateTotalPages(totalHits, pageSize);

            ArrayList<ItemModel> modelArrayList = new ArrayList<>(); // Create a new modelArrayList

            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject itemObject = itemsArray.getJSONObject(i);
                JSONArray dataArray = itemObject.getJSONArray("data");
                JSONObject dataObject = dataArray.getJSONObject(0);

                String title = dataObject.optString("title", "");
                String imageUrl = "";
                String description = dataObject.optString("description", "");
                String date_created = (dataObject.optString("date_created", ""));

                if (itemObject.has("links")) {
                    JSONArray linksArray = itemObject.getJSONArray("links");
                    for (int j = 0; j < linksArray.length(); j++) {
                        JSONObject linkObject = linksArray.getJSONObject(j);
                        String render = linkObject.optString("render", "");
                        if (render.equals("image")) {
                            imageUrl = linkObject.optString("href", "");
                            break;
                        }
                    }
                }

                ItemModel itemModel = new ItemModel(title, imageUrl, description, date_created);
                modelArrayList.add(itemModel); // Add itemModel to modelArrayList
            }

            // Create and set the adapter with the updated modelArrayList
            ItemAdapter itemAdapter = new ItemAdapter(modelArrayList);
            recyclerView.setAdapter(itemAdapter);

            itemAdapter.setOnItemClickListener(item -> {
                // Handle item click here, launch the DetailsActivity and pass the item data to it
                Intent intent = new Intent(MainActivity.this, DetailsViewActivity.class);
                intent.putExtra("item", item);
                startActivity(intent);
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int calculateTotalPages(int totalHits, int pageSize) {
        return (int) Math.ceil((double) totalHits / pageSize);
    }
}
