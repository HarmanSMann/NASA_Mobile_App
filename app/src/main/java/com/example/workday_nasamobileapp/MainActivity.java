package com.example.workday_nasamobileapp;

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

import com.example.workday_nasamobileapp.DataIndex.SearchResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private SearchResultsAdapter searchResultsAdapter;
    private ItemAdapter itemAdapter;
    private ArrayList<ItemModel> modelArrayList;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button search_btn = findViewById(R.id.btn_search);
        Button prev_btn = findViewById(R.id.btn_previous_page);
        Button next_btn = findViewById(R.id.btn_next_page);
        EditText search_et = findViewById(R.id.search_bar);
        TextView page_num = findViewById(R.id.page_number);

        searchResultsAdapter = new SearchResultsAdapter();
        recyclerView = findViewById(R.id.results_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                Call<ResponseBody> call = nasaImageApi.searchImages(search_et.getText().toString(), "image");
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
            } else {
                Toast.makeText(this, "Please Add Search Term", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processJsonData(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONObject("collection").getJSONArray("items");

            modelArrayList = new ArrayList<>(); // Create a new modelArrayList

            // Iterate through items and extract search results
            Log.d("@Harman - num of Items", "processJsonData: " + jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject itemObject = jsonArray.getJSONObject(i);
                JSONArray dataArray = itemObject.getJSONArray("data");
                JSONObject dataObject = dataArray.getJSONObject(0);

                String title = dataObject.optString("title", "");
                Log.d("@Harman - Title", "processJsonData: " + title);
                String imageUrl = "";

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
                ItemModel itemModel = new ItemModel(title, imageUrl);
                modelArrayList.add(itemModel); // Add itemModel to modelArrayList
            }

            // Create and set the adapter with the updated modelArrayList
            itemAdapter = new ItemAdapter(modelArrayList, this);
            recyclerView.setAdapter(itemAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
