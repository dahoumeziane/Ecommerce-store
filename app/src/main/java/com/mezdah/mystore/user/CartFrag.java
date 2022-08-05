package com.mezdah.mystore.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mezdah.mystore.R;
import com.mezdah.mystore.adapter.CartAdapter;
import com.mezdah.mystore.adapter.ProductAdapter;
import com.mezdah.mystore.model.Product;

import java.util.ArrayList;

public class CartFrag extends Fragment {


    DatabaseReference cartRef;
    View mview;
    FirebaseAuth mauth;
    RecyclerView cart_items;
    int TotalPrice = 0;
    ArrayList<Product> products;
    TextView cart_price;
    Button checkout;
    LinearLayout empty_view ;
    CardView bottom_card;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_cart, container, false);
        cart_items = mview.findViewById(R.id.cart_items);
        mauth = FirebaseAuth.getInstance();
        checkout=mview.findViewById(R.id.checkout);
        cartRef = FirebaseDatabase.getInstance(getContext().getString(R.string.db_url))
                .getReference()
                .child("Cart")
                .child(mauth.getCurrentUser().getUid());
        cart_price= mview.findViewById(R.id.cart_price);
        empty_view = mview.findViewById(R.id.empty_linear);
        bottom_card = mview.findViewById(R.id.bottom);
        fetchDataFromDB();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        cart_items.setLayoutManager(manager);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),ConfirmOrderAct.class);
                i.putExtra("products",products);
                i.putExtra("totalPrice",TotalPrice);
                startActivity(i);
            }
        });
        return mview;
    }

    private void fetchDataFromDB(){
        products = new ArrayList<>();
        // getting data from the database
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.clear();
                for (DataSnapshot oneSnapshot : snapshot.getChildren()){
                    Product product = oneSnapshot.getValue(Product.class);
                    products.add(product);
                }
                if(products.isEmpty()){
                    empty_view.setVisibility(View.VISIBLE);
                    bottom_card.setVisibility(View.INVISIBLE);
                }else{
                    empty_view.setVisibility(View.INVISIBLE);
                    bottom_card.setVisibility(View.VISIBLE);
                }
                calcPrice();
                CartAdapter adapter = new CartAdapter(getContext(),products);
                cart_items.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void calcPrice(){
        TotalPrice = 0;
        for (Product product : products){
            TotalPrice += product.getPrice();
        }
        cart_price.setText(String.valueOf(TotalPrice)+" DZD");
    }


}