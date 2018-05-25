package com.example.karina.firebase;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.util.UUID;

public class AddItemActivity extends AppCompatActivity {

    private Button btnAddPhoto;
    private ImageView imgCell;
    private Uri uri = null;
    private EditText name;
    private EditText price;
    private EditText description;
    DatabaseReference productDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        btnAddPhoto = findViewById(R.id.btnAddPhoto);
        imgCell = findViewById(R.id.imageView);
        name = findViewById(R.id.itemNameCreate);
        price = findViewById(R.id.priceCreate);
        description = findViewById(R.id.descriptionCreate);
        productDatabase = FirebaseDatabase.getInstance().getReference("productos");

        btnAddPhoto.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            uri = data.getData();
            //textTargetUri.setText(targetUri.toString());
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                imgCell.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void addNewItem(View view) {

        String nameStr = name.getText().toString();
        String priceStr = price.getText().toString();
        String descriptionStr = description.getText().toString();
        if(TextUtils.isEmpty(nameStr)) {

            Toast.makeText(getApplicationContext(), "Ingresa un nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(priceStr)) {

            Toast.makeText(getApplicationContext(), "Ingresa un precio", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(descriptionStr)) {

            Toast.makeText(getApplicationContext(), "Ingresa una descripción", Toast.LENGTH_SHORT).show();
            return;
        }

        if(uri == null) {

            Toast.makeText(getApplicationContext(), "Debe adjuntar un archivo", Toast.LENGTH_SHORT).show();
            return;

        }

        uploadImageToFirebase(nameStr, priceStr, descriptionStr);




    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void uploadImageToFirebase(final String name, final String price, final String description) {
        FirebaseStorage storage;
        StorageReference storageReference;


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
        ref.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String path = taskSnapshot.getDownloadUrl().toString();
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                        String id = productDatabase.push().getKey();
                        Item item = new Item(name, price, path, description, userId, id);
                        productDatabase.child(id).setValue(item);
                        startActivity(new Intent(AddItemActivity.this, ItemsActivity.class));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddItemActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                    }
                });





    }
}
