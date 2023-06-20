package com.example.workday_nasamobileapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workday_nasamobileapp.DataAdaptor.ItemModel;
import com.squareup.picasso.Picasso;

public class DetailsViewActivity extends AppCompatActivity {

    ImageView imageView;
    TextView title, description, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);
        imageView = findViewById(R.id.img_details_image);
        title = findViewById(R.id.tv_details_title);
        description = findViewById(R.id.tv_details_description);
        date = findViewById(R.id.tv_details_date);

        ItemModel item = (ItemModel) getIntent().getSerializableExtra("item");

        if (item != null) {
            Picasso.get().load(item.getImageUrl()).into(imageView);
            title.setText(item.getTitle());
            description.setText(item.getDescription());
            date.setText(item.getDate());
        }
    }
}