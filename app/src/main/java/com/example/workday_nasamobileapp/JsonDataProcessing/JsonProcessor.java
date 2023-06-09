package com.example.workday_nasamobileapp.JsonDataProcessing;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import com.example.workday_nasamobileapp.DataAdaptor.ItemAdapter;
import com.example.workday_nasamobileapp.DataAdaptor.ItemModel;
import com.example.workday_nasamobileapp.DetailsViewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonProcessor {

    public static void processJsonData(String jsonData, RecyclerView recyclerView, Context context) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject collectionObject = jsonObject.getJSONObject("collection");
            JSONArray itemsArray = collectionObject.getJSONArray("items");

            JSONObject metadataObject = collectionObject.getJSONObject("metadata");
            int totalHits = metadataObject.optInt("total_hits", 0);
            int pageSize = 100;
            PaginationHelper paginationHelper = new PaginationHelper();
            paginationHelper.setTotalPages(calculateTotalPages(totalHits, pageSize));

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
                Intent intent = new Intent(context, DetailsViewActivity.class);
                intent.putExtra("item", item);
                context.startActivity(intent);
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static int calculateTotalPages(int totalHits, int pageSize) {
        return (int) Math.ceil((double) totalHits / pageSize);
    }
}
