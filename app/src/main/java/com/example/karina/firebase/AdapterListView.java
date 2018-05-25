package com.example.karina.firebase;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class AdapterListView extends ArrayAdapter<Item> {
    private final Activity context;
    private List<Item> items = null;
    DatabaseReference productDatabase;
    public static final String EXTRA_MESSAGE = "com.example.karina.firebase";



    public AdapterListView(Activity context, List<Item> items) {
        super(context, R.layout.list_view_item, items);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.items = items;
        productDatabase = FirebaseDatabase.getInstance().getReference("productos");


    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_view_item, null,true);

        TextView txtName = (TextView) rowView.findViewById(R.id.txtNombre);
        TextView txtPrice = (TextView) rowView.findViewById(R.id.txtPrecio);
        ImageView deleteButton = rowView.findViewById(R.id.imgDelete);
        final ImageView detailButton = rowView.findViewById(R.id.imgDetail);

        txtName.setText(items.get(position).getName());
        txtPrice.setText(items.get(position).getPrice());

        Log.i("ListView", items.get(position).getName());

        deleteButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteItem(items.get(position));
            }
        });

        detailButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                detailItem(items.get(position));
            }
        });


        return rowView;

    };

    public void deleteItem(Item item) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("productos").child(item.getId());
        reference.removeValue();

    }

    public void detailItem(Item item) {
        Intent intent = new Intent(context, ItemDetailActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "");
        intent.putExtra("name", item.getName());
        intent.putExtra("price", item.getPrice());
        intent.putExtra("description", item.getDescription());
        intent.putExtra("path", item.getPhoto());

        context.startActivity(intent);

    }


}
