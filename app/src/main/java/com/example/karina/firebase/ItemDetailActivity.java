package com.example.karina.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ItemDetailActivity extends AppCompatActivity {

    private TextView itemName;
    private TextView itemPrice;
    private TextView itemDescription;
    private ImageView imageItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        itemName = findViewById(R.id.txtItemNameD);
        itemPrice = findViewById(R.id.txtItemPriceD);
        itemDescription = findViewById(R.id.txtItemDescriptionD);
        imageItem = findViewById(R.id.imgItem);


        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        String description = getIntent().getStringExtra("description");
        String path = getIntent().getStringExtra("path");


        itemName.setText(name);
        itemPrice.setText(price);
        itemDescription.setText(description);

        Glide.with(this)
                .load(path)
                .into(imageItem);
    }
}
