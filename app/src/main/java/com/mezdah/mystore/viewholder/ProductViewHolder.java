package com.mezdah.mystore.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mezdah.mystore.R;

public class ProductViewHolder extends RecyclerView.ViewHolder{
    public TextView product_title , product_category,product_price,product_desc;
    public ImageView product_image,delete_product;
    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        product_title = itemView.findViewById(R.id.product_title);
        product_category = itemView.findViewById(R.id.product_category);
        product_price = itemView.findViewById(R.id.product_price);
        product_desc = itemView.findViewById(R.id.product_description);
        product_image = itemView.findViewById(R.id.product_img);
        delete_product = itemView.findViewById(R.id.delete_product);

    }
}
