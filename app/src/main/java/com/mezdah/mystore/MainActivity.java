package com.mezdah.mystore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.mezdah.mystore.admin.HomeActivity;
import com.mezdah.mystore.user.UserHomeActivity;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mauth = FirebaseAuth.getInstance();
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(3000);
                }catch(Exception e){

                }finally {
                    if(mauth.getCurrentUser() != null){
                        Intent i = new Intent(MainActivity.this, UserHomeActivity.class);
                        startActivity(i);
                    }else{
                        Intent i = new Intent(MainActivity.this,RegisterAct.class);
                        startActivity(i);
                    }


                }
            }
        };
        thread.start();

    }
}