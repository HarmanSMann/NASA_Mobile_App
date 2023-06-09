package com.example.workday_nasamobileapp.DataAdaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workday_nasamobileapp.R;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private final ArrayList<ItemModel> modelArrayList;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public ItemAdapter(ArrayList<ItemModel> modelArrayList) {
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ItemViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemModel model = modelArrayList.get(position);
        holder.title.setText(model.getTitle());
        Picasso.get().load(model.getImageUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;

        public ItemViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            image = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ItemModel item = modelArrayList.get(position);
                        listener.onItemClick(item);
                    }
                }
            });
        }
    }
}
