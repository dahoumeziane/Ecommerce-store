package com.mezdah.mystore.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mezdah.mystore.R;
import com.mezdah.mystore.model.Product;
import com.squareup.picasso.Picasso;

public class ProductDetails extends AppCompatActivity {
    DatabaseReference productRef , cartRef ;
    String productId;
    ImageView product_image,back_btn;
    TextView product_title_top,product_title,product_category,product_description,product_price;
    Button add_to_cart;
    FirebaseAuth mauth;
    Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        // get selected product id
        productId = getIntent().getStringExtra("productId");
        initializationOfFields();
        getProductDetails();
        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        // add product to cart
                addProductToCart();
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void initializationOfFields(){
        mauth = FirebaseAuth.getInstance();
        productRef= FirebaseDatabase.getInstance(getString(R.string.db_url)).getReference()
                    .child("Products").child(productId);
        cartRef = FirebaseDatabase.getInstance(getString(R.string.db_url)).getReference()
                    .child("Cart").child(mauth.getCurrentUser().getUid());
        product_title_top=findViewById(R.id.product_title_top);
        product_image=findViewById(R.id.product_image);
        product_category=findViewById(R.id.product_category);
        product_description=findViewById(R.id.product_description);
        product_price=findViewById(R.id.product_price);
        add_to_cart=findViewById(R.id.add_to_cart);
        product_title=findViewById(R.id.product_title);
        back_btn = findViewById(R.id.back);
    }
    private void getProductDetails(){
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                product = snapshot.getValue(Product.class);
                product_title.setText(product.getName());
                product_title_top.setText(product.getName());
                product_description.setText(product.getDescription());
                product_price.setText(String.valueOf(product.getPrice())+" DZD");
                product_category.setText(product.getCategory());
                Picasso.get().load(product.getImage()).into(product_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void addProductToCart(){
        cartRef.child(productId).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ProductDetails.this, "Product added to your cart !", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ProductDetails.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}