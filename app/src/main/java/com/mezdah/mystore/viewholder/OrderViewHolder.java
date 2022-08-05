package com.mezdah.mystore.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mezdah.mystore.R;

public class OrderViewHolder extends RecyclerView.ViewHolder {
    public TextView order_number,order_city,order_address,order_price,order_state;
    public ImageView order_picture;
    public Button  accept,refuse;
    public LinearLayout accept_refuse;
    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        order_number = itemView.findViewById(R.id.order_number);
        order_city = itemView.findViewById(R.id.order_city);
        order_address = itemView.findViewById(R.id.order_address);
        order_price = itemView.findViewById(R.id.order_price);
        order_state = itemView.findViewById(R.id.order_state);
        order_picture = itemView.findViewById(R.id.order_img);
        accept = itemView.findViewById(R.id.accept);
        refuse = itemView.findViewById(R.id.refuse);
        accept_refuse = itemView.findViewById(R.id.accept_refuse);
    }
}
