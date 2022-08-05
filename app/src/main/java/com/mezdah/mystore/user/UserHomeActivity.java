package com.mezdah.mystore.user;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mezdah.mystore.R;
import com.mezdah.mystore.data.Constants;


public class UserHomeActivity extends AppCompatActivity {

    ImageView home, cart, profile, orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        InitializationOfFields();


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new UserHome())
                .commit();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                home.setColorFilter(getColor(R.color.orange), PorterDuff.Mode.SRC_IN);
                cart.setColorFilter(getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
                profile.setColorFilter(getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
                orders.setColorFilter(getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,
                                new UserHome())
                        .commit();
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setColorFilter(getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
                cart.setColorFilter(getColor(R.color.orange), PorterDuff.Mode.SRC_IN);
                profile.setColorFilter(getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
                orders.setColorFilter(getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,
                                new CartFrag())
                        .commit();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setColorFilter(getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
                cart.setColorFilter(getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
                profile.setColorFilter(getColor(R.color.orange), PorterDuff.Mode.SRC_IN);
                orders.setColorFilter(getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,
                                new ProfileFrag())
                        .commit();
            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setColorFilter(getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
                cart.setColorFilter(getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
                profile.setColorFilter(getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
                orders.setColorFilter(getColor(R.color.orange), PorterDuff.Mode.SRC_IN);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,
                                new OrderFrag())
                        .commit();
            }
        });

    }

    private void InitializationOfFields() {
        home = findViewById(R.id.user_home);
        cart = findViewById(R.id.user_cart);
        profile = findViewById(R.id.user_profile);
        orders = findViewById(R.id.user_order);
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.TOPIC);
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        // save user device token into the database
                        DatabaseReference UsersRef = FirebaseDatabase.getInstance(getString(R.string.db_url))
                                                        .getReference()
                                                        .child("Users")
                                                        .child(mauth.getCurrentUser().getUid());
                        UsersRef.child("token").setValue(s);
                    }
                });
    }

}