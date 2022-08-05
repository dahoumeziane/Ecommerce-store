package com.mezdah.mystore.admin;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mezdah.mystore.R;
import com.mezdah.mystore.data.Constants;
import com.mezdah.mystore.data.NotificationApi;
import com.mezdah.mystore.model.NotificationData;
import com.mezdah.mystore.model.PushNotification;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminHomeFrag extends Fragment {

    private View mview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview =inflater.inflate(R.layout.fragment_admin_home, container, false);

        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(Constants.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
        NotificationApi notificationApi = retrofit.create(NotificationApi.class);

        Button send_notif = mview.findViewById(R.id.sendBtn);
        send_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Send notification to users") ;
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                EditText notification_title = new EditText(getContext());
                notification_title.setHint("Notification title");
                EditText notification_message = new EditText(getContext());
                notification_message.setHint("Notification message");
                linearLayout.addView(notification_title);
                linearLayout.addView(notification_message);
                builder.setView(linearLayout) ;
                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NotificationData content = new NotificationData(notification_title.getText().toString(),notification_message.getText().toString());
                        PushNotification data = new PushNotification(content,Constants.TOPIC);

                        Call<ResponseBody> response = notificationApi.postNotification(data);
                        response.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.isSuccessful()){
                                    Toast.makeText(getContext(), "Notification sent successfully", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();

            }
        });

        return mview ;
    }
}