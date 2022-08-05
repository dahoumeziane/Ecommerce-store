package com.mezdah.mystore.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mezdah.mystore.R;
import com.mezdah.mystore.data.Constants;

public class HomeActivity extends AppCompatActivity {
    ImageView productBtn, homeBtn,ordersBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.TOPIC);

        initializationOfFields();
        // default active fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,new AdminHomeFrag())
                .commit();
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,new AdminHomeFrag())
                        .commit();
                homeBtn.setColorFilter(getColor(R.color.orange), PorterDuff.Mode.SRC_IN);
                productBtn.setColorFilter(getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
                ordersBtn.setColorFilter(getColor(R.color.gray), PorterDuff.Mode.SRC_IN);

            }
        });
        productBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productBtn.setColorFilter(getColor(R.color.orange), PorterDuff.Mode.SRC_IN);
                homeBtn.setColorFilter(getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
                ordersBtn.setColorFilter(getColor(R.color.gray), PorterDuff.Mode.SRC_IN);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,
                                new ProductFrag())
                        .commit();
            }
        });
        ordersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ordersBtn.setColorFilter(getColor(R.color.orange), PorterDuff.Mode.SRC_IN);

                productBtn.setColorFilter(getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
                homeBtn.setColorFilter(getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,
                                new AdminOrders())
                        .commit();
            }
        });
    }

    private void initializationOfFields() {
        productBtn = findViewById(R.id.product_btn);
        homeBtn = findViewById(R.id.home_btn);
        ordersBtn = findViewById(R.id.orders);
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        // save user device token into the database
                        DatabaseReference adminRef = FirebaseDatabase.getInstance(getString(R.string.db_url))
                                .getReference()
                                .child("Admin")
                                .child("token");
                        adminRef.setValue(s);
                    }
                });
    }


}