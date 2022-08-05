package com.mezdah.mystore.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mezdah.mystore.R;
import com.mezdah.mystore.adapter.ProductAdapter;
import com.mezdah.mystore.adapter.UserProductAdapter;
import com.mezdah.mystore.model.Product;

import java.util.ArrayList;

public class UserHome extends Fragment {
    DatabaseReference productRef;
    RecyclerView productsList;
    View mview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview =inflater.inflate(R.layout.fragment_user_home, container, false);
        InitializationOfFields();
       // StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL);
        GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        productsList.setLayoutManager(manager);
        fetchDataFromDB();
        return mview;
    }
    private void InitializationOfFields(){
        productsList = mview.findViewById(R.id.products);
        productRef = FirebaseDatabase
                .getInstance(getContext().getString(R.string.db_url))
                .getReference()
                .child("Products");
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
                UserProductAdapter adapter = new UserProductAdapter(getContext(),products);
                productsList.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}