package com.mezdah.mystore.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mezdah.mystore.R;
import com.mezdah.mystore.adapter.OrderAdapter;
import com.mezdah.mystore.model.Order;

import java.util.ArrayList;

public class AdminOrders extends Fragment {

    private View mview;
    RecyclerView ordersList;
    DatabaseReference ordersRef;
    FirebaseAuth mauth;
    ArrayList<Order> orders = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview =inflater.inflate(R.layout.fragment_admin_orders, container, false);
        ordersList = mview.findViewById(R.id.order_list);
        initializationOfFields();
        getAdminOrders();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        ordersList.setLayoutManager(manager);
        return mview;
    }

    private void initializationOfFields() {
        mauth = FirebaseAuth.getInstance();
        ordersList = mview.findViewById(R.id.order_list);
        ordersRef = FirebaseDatabase.getInstance(getString(R.string.db_url)).getReference()
                .child("Orders");

    }

    private void getAdminOrders() {

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orders.clear();
                for (DataSnapshot oneSnapShot : snapshot.getChildren()) {
                    if (oneSnapShot.child("state").getValue().toString()
                            .equals("0")) {
                        Order order = oneSnapShot.getValue(Order.class);
                        orders.add(order);
                    }
                }
                OrderAdapter adapter = new OrderAdapter(getContext(), orders,true);
                ordersList.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}