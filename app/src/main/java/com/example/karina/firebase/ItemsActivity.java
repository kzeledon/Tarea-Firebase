package com.example.karina.firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ItemsActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private ListView list;
    DatabaseReference productDatabase;
    List<Item> items;

    public static final String EXTRA_MESSAGE = "com.example.dylan.firebase_app";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        auth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        productDatabase = FirebaseDatabase.getInstance().getReference("productos");

        list = findViewById(R.id.listProductos);

        items = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();

        productDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Item> itemsL = new ArrayList<>();
                for(DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    String name = productSnapshot.getValue(Item.class).getName();
                    String price = productSnapshot.getValue(Item.class).getPrice();
                    String photo = productSnapshot.getValue(Item.class).getPhoto();
                    String description = productSnapshot.getValue(Item.class).getDescription();
                    String userId = productSnapshot.getValue(Item.class).getUserId();
                    String id = productSnapshot.getKey();

                    Item item = new Item(name, price, photo, description, userId, id);
                    FirebaseUser user = auth.getCurrentUser();
                    String uid = user.getUid();
                    if(uid.compareTo(productSnapshot.getValue(Item.class).getUserId()) == 0) {
                        itemsL.add(item);
                    }



                }


                AdapterListView adapter = new AdapterListView(ItemsActivity.this, itemsL);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // TODO Auto-generated method stub


                        Intent intent = new Intent(getApplicationContext(), ItemDetailActivity.class);
                        intent.putExtra(EXTRA_MESSAGE, "");
                        intent.putExtra("name", items.get(position).getName());
                        intent.putExtra("price", items.get(position).getPrice());
                        intent.putExtra("description", items.get(position).getDescription());
                        intent.putExtra("path", items.get(position).getPhoto());

                        startActivity(intent);


                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void createItem(View view) {
        startActivity(new Intent(ItemsActivity.this, AddItemActivity.class));
    }

    public void signout(View view) {

        auth.signOut();

        FirebaseUser user = auth.getCurrentUser();
        if(user == null) {
            openMainActivity();
        }
        else {
            Toast.makeText(ItemsActivity.this, "No se pudo salir de la sesión correctamente, por favor, inténtelo de nuevo", Toast.LENGTH_SHORT).show();
            return;
        }



    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "");
        startActivity(intent);
    }

}
