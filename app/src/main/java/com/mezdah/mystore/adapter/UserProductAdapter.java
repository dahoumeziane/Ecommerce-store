package com.mezdah.mystore.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mezdah.mystore.R;
import com.mezdah.mystore.model.Product;
import com.mezdah.mystore.user.ProductDetails;
import com.mezdah.mystore.viewholder.GridProductHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserProductAdapter extends RecyclerView.Adapter<GridProductHolder> {
    Context context;
    ArrayList<Product> products;

    public UserProductAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public GridProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_product_user, parent, false);
        return new GridProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridProductHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.product_title.setText(products.get(position).getName());
        holder.product_category.setText(products.get(position).getCategory());
        holder.product_price.setText(String.valueOf(products.get(position).getPrice() + " DZD"));
        Picasso.get().load(products.get(position).getImage()).into(holder.product_image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,ProductDetails.class);
                i.putExtra("productId",products.get(position).getProductID());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
