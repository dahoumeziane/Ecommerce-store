package com.mezdah.mystore.admin;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mezdah.mystore.R;
import com.mezdah.mystore.model.Product;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddProductActivity extends AppCompatActivity {
    ImageView select_image;
    EditText product_name,product_desc,product_price;
    TextView select_category;
    Uri imageFile ;
    StorageReference productImagesRef;
    DatabaseReference productRef;
    Button add_product ;
    String imageURL;
    String [] categories;
    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        initializationOfFields();
        // variable to get image from gallery

        ActivityResultLauncher<Intent> openGalleryResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // Here, no request code
                            Intent data = result.getData();
                            imageFile = data.getData();
                            select_image.setImageURI(imageFile);
                        }
                    }
                });
        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openGalleryResult.launch(openGallery());
            }
        });
        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFields()){
                   uploadImageToGoogleCloud();
                }

            }
        });
        select_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(AddProductActivity.this);
                mbuilder.setTitle("Please select the category");
                mbuilder.setSingleChoiceItems(categories, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        select_category.setText(categories[position]);
                        dialogInterface.dismiss();
                    }
                });
                mbuilder.show();
            }
        });
    }
    private void initializationOfFields(){
        select_image = findViewById(R.id.select_img);
        product_name = findViewById(R.id.product_name);
        product_desc = findViewById(R.id.product_desc);
        product_price = findViewById(R.id.product_price);
        select_category = findViewById(R.id.select_category);
        productImagesRef = FirebaseStorage.getInstance().getReference()
                            .child("Product images");
        add_product = findViewById(R.id.add_product);
        categories = new String[]{"Gamer","Student","Developer","Workstation","Notebook","Convertible","Professionnal"};
        loading = new ProgressDialog(this);
        loading.setTitle("Uploading your product");
        loading.setMessage("Please wait while we are adding your product");
    }
    private Intent openGallery(){
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        return i;

    }
    private String generateID(){

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyyMMdd");
        String saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String saveCurrentTime = currentTime.format(calForDate.getTime());
        return saveCurrentDate+saveCurrentTime;
    }
    private void saveProductDataIntoDb(Product product){


        productRef.setValue(product)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            loading.dismiss();
                            Toast.makeText(AddProductActivity.this, "Product added", Toast.LENGTH_SHORT).show();
                        }else{
                            loading.dismiss();
                            Toast.makeText(AddProductActivity.this, "Erreur", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private boolean checkFields(){
        if(product_name.getText().toString().isEmpty()){
            product_name.setError("Please set product name");
            return false;
        }else if (product_desc.getText().toString().isEmpty()){
            product_desc.setError("Please set description");
            return false;
        }else if (product_price.getText().toString().isEmpty()){
            product_price.setError("Please select price");
            return false;
        }else if(imageFile==null){
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    private void uploadImageToGoogleCloud(){
        loading.show();
        String id = generateID();
        productImagesRef.child(id+".jpeg")
                .putFile(imageFile)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){

                            //get download url of the uploaded image
                            productImagesRef.child(id+".jpeg")
                                    .getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            imageURL = uri.toString();
                                            productRef= FirebaseDatabase.getInstance(getString(R.string.db_url)).getReference().child("Products");
                                            productRef = productRef.push(); // Generate a unique key
                                            String id = productRef.getKey(); // Get the generated key from firebase
                                            Product product = new Product(product_name.getText().toString(),
                                                    product_desc.getText().toString()
                                                    ,imageURL,select_category.getText().toString(),
                                                    id
                                                    ,Integer.valueOf(product_price.getText().toString())

                                            );
                                            saveProductDataIntoDb(product);
                                        }
                                    });


                        }else{
                            loading.dismiss();
                            Toast.makeText(AddProductActivity.this, "Error", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


}