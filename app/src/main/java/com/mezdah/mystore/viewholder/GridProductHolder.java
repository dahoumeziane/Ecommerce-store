package com.mezdah.mystore.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mezdah.mystore.R;

public class GridProductHolder extends RecyclerView.ViewHolder {
    public ImageView product_image;
    public TextView product_title,product_category,product_price;
    public GridProductHolder(@NonNull View itemView) {
        super(itemView);
        product_image = itemView.findViewById(R.id.img);
        product_title = itemView.findViewById(R.id.title);
        product_category = itemView.findViewById(R.id.desc);
        product_price = itemView.findViewById(R.id.price);
    }
}
