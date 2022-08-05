package com.mezdah.mystore.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mezdah.mystore.R;
import com.mezdah.mystore.adapter.ProductAdapter;
import com.mezdah.mystore.model.Product;

import java.util.ArrayList;


public class ProductFrag extends Fragment {


    FloatingActionButton add ;
    View mview;
    RecyclerView product_list;
    DatabaseReference productRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview =inflater.inflate(R.layout.fragment_product, container, false);
        add = mview.findViewById(R.id.addBtn);
        product_list = mview.findViewById(R.id.product_list);
        productRef = FirebaseDatabase
                .getInstance(getContext().getString(R.string.db_url))
                .getReference()
                .child("Products");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),AddProductActivity.class);
                startActivity(i);
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        product_list.setLayoutManager(manager);

        fetchDataFromDB();




        return mview;
    }
    private void fetchDataFromDB(){
        ArrayList<Product> products = new ArrayList<>();
        // getting data from the database
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.clear();
                for (DataSnapshot oneSnapshot : snapshot.getChildren()){
                    Product product = oneSnapshot.getValue(Product.class);
                    products.add(product);
                }
                ProductAdapter adapter = new ProductAdapter(getContext(),products);
                product_list.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}