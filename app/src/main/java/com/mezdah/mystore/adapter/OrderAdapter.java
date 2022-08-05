package com.mezdah.mystore.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mezdah.mystore.R;
import com.mezdah.mystore.data.Constants;
import com.mezdah.mystore.data.NotificationApi;
import com.mezdah.mystore.model.NotificationData;
import com.mezdah.mystore.model.Order;
import com.mezdah.mystore.model.PushNotification;
import com.mezdah.mystore.viewholder.OrderViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {
    Context context;
    ArrayList<Order> orders;
    Boolean isAdmin = false;

    public OrderAdapter(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    public OrderAdapter(Context context, ArrayList<Order> orders, Boolean isAdmin) {
        this.context = context;
        this.orders = orders;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_order,parent,false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
            if(isAdmin){
                holder.accept_refuse.setVisibility(View.VISIBLE);
            }else{
                holder.accept_refuse.setVisibility(View.GONE);

            }
            holder.order_number.setText("Order n°"+String.valueOf(position+1));
            holder.order_price.setText(String.valueOf(orders.get(position).getTotal_price())+" DZD");
            holder.order_city.setText(orders.get(position).getWilaya()+", "+orders.get(position).getCommune());
            holder.order_address.setText(orders.get(position).getAddress());
            Picasso.get().load(orders.get(position).getProducts().get(0).getImage())
                    .into(holder.order_picture);
            if(orders.get(position).getState() == 0){
                // waiting order
                holder.order_state.setText("Waiting");
                holder.order_state.setTextColor(context.getColor(R.color.orange));
            }else if (orders.get(position).getState() == 1){
                // accepted
                holder.order_state.setText("Accepted");
                holder.order_state.setTextColor(context.getColor(R.color.green));

            }else if (orders.get(position).getState() == 2){
                // Refused
                holder.order_state.setText("Refused");
                holder.order_state.setTextColor(context.getColor(R.color.red));

            }else {
                // completed
                holder.order_state.setText("Completed");
                holder.order_state.setTextColor(context.getColor(R.color.blue));


            }
            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    acceptOrder(orders.get(position).getOrderId(),orders.get(position).getUser_id());

                }
            });

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    private void acceptOrder(String idOrder , String userId){
        DatabaseReference OrdersRef = FirebaseDatabase.getInstance(context.getString(R.string.db_url)).getReference().child("Orders").child(idOrder);
        OrdersRef.child("state").setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    // send notification
                    sendNotification(userId);
                }
            }
        });
    }
    private void sendNotification(String userId){
        DatabaseReference UsersRef = FirebaseDatabase.getInstance(context.getString(R.string.db_url))
                .getReference()
                .child("Users")
                .child(userId);
        UsersRef.child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                NotificationApi notificationApi = retrofit.create(NotificationApi.class);
                String token  = snapshot.getValue().toString();
                NotificationData content = new NotificationData("Order accepted","You order has been accepted thank you!");
                PushNotification data = new PushNotification(content, token);

                Call<ResponseBody> response = notificationApi.postNotification(data);
                response.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(context, "Notification sent successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
