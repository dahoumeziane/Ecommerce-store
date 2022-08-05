package com.mezdah.mystore.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mezdah.mystore.R;
import com.mezdah.mystore.model.Product;
import com.mezdah.mystore.viewholder.ProductViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    Context context;
    ArrayList<Product> products;
    DatabaseReference cartRef;
    FirebaseAuth mauth = FirebaseAuth.getInstance();

    public CartAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
        if(context!=null){
            cartRef = FirebaseDatabase.getInstance(context.getString(R.string.db_url))
                    .getReference()
                    .child("Cart")
                    .child(mauth.getCurrentUser().getUid());
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_product,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Picasso.get().load(products.get(position).getImage())
                .into(holder.product_image);
        holder.product_title.setText(products.get(position).getName());
        holder.product_category.setText(products.get(position).getCategory());
        holder.product_price.setText(String.valueOf(products.get(position).getPrice())+" DZD");
        holder.product_desc.setText(products.get(position).getDescription());
        holder.delete_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete product
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(context);
                mbuilder.setTitle("Delete "+products.get(position).getName());
                mbuilder.setMessage("Do you really want to delete "+products.get(position).getName()+" ?");
                mbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cartRef
                                .child(products.get(position)
                                        .getProductID())
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(context, "Product deleted", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                mbuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                mbuilder.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
