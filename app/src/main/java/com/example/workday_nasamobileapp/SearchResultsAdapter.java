package com.example.workday_nasamobileapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workday_nasamobileapp.DataIndex.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultViewHolder> {

    private List<SearchResult> searchResults;

    public SearchResultsAdapter() {
        this.searchResults = new ArrayList<>();
    }

    public void setSearchResults(List<SearchResult> searchResults) {
        this.searchResults = searchResults;
        Log.d("@Harman - set in re", "setSearchResults: " +  searchResults.get(0));
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new SearchResultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        SearchResult searchResult = searchResults.get(position);
        holder.title.setText(searchResult.getTitle());
        Log.d("@Harman - Bind", searchResult.getTitle());

        // Set other data to corresponding views
    }

    @Override
    public int getItemCount() {
        Log.d("@Harman - re size", "getItemCount: " + searchResults.size());
        return searchResults.size();
    }

    public static class SearchResultViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView imageView;

        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
//            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
