package com.mezdah.mystore.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import com.mezdah.mystore.model.Product;
import com.mezdah.mystore.model.PushNotification;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfirmOrderAct extends AppCompatActivity {
    DatabaseReference ordersRef, cartRef;
    ArrayList<Product> products;
    EditText full_name, phone_number, address, wilaya, commune;
    Button send;
    TextView total_price_display;
    int total_price;
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        initializationOfFields();
        products = (ArrayList<Product>) getIntent().getSerializableExtra("products");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send order to database
                sendOrder();
            }
        });
    }

    private void initializationOfFields() {
        full_name = findViewById(R.id.full_name);
        address = findViewById(R.id.address);
        phone_number = findViewById(R.id.phone_number);
        wilaya = findViewById(R.id.wilaya);
        commune = findViewById(R.id.commune);
        send = findViewById(R.id.confirm_order);
        total_price_display = findViewById(R.id.total_price);
        ordersRef = FirebaseDatabase
                .getInstance(getString(R.string.db_url))
                .getReference()
                .child("Orders");
        mauth = FirebaseAuth.getInstance();
        cartRef = FirebaseDatabase
                .getInstance(getString(R.string.db_url))
                .getReference()
                .child("Cart")
                .child(mauth.getCurrentUser().getUid());
        total_price = getIntent().getIntExtra("totalPrice", -1);
        total_price_display.setText(String.valueOf(total_price)+ " DZD");

    }

    private void sendOrder() {
        String id = ordersRef.push().getKey();
        Order order = new Order(products, full_name.getText().toString()
                , address.getText().toString()
                , phone_number.getText().toString()
                , wilaya.getText().toString()
                , commune.getText().toString()
                , mauth.getCurrentUser().getUid(), total_price
                , 0,id);

        ordersRef.child(id).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    cartRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                sendNotification();
                                send.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(ConfirmOrderAct.this, "You have already make the order", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Toast.makeText(ConfirmOrderAct.this, "Order recieved", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(ConfirmOrderAct.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });

    }
    private void sendNotification(){
        DatabaseReference adminRef = FirebaseDatabase.getInstance(getString(R.string.db_url))
                .getReference()
                .child("Admin")
                .child("token");
        adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               //admin token device
                String token  = snapshot.getValue().toString();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                NotificationApi notificationApi = retrofit.create(NotificationApi.class);
                NotificationData content = new NotificationData("New order","New order has been recieved !");
                PushNotification data = new PushNotification(content, token);

                Call<ResponseBody> response = notificationApi.postNotification(data);
                response.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(ConfirmOrderAct.this, "Notification sent successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ConfirmOrderAct.this, response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(ConfirmOrderAct.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}