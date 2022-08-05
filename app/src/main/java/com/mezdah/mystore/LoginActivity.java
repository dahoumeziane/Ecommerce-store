package com.mezdah.mystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mezdah.mystore.admin.HomeActivity;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    Button login;
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializationOfFields();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFields()){
                    signIn();

                }
            }
        });



    }
    private void signIn(){
        String my_email = email.getText().toString();
        String my_password = password.getText().toString();
        mauth.signInWithEmailAndPassword(my_email,my_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // you successfully logged in your account !
                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();
                        }else{
                            // an error has been occurred for a example (internet connexion , wrong email , wrong password ...etc)
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void initializationOfFields(){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        mauth = FirebaseAuth.getInstance();

    }
    private boolean checkFields(){
        if(email.getText().toString().isEmpty()){
            email.setError("please fill the email");
            return false;
        }else if (password.getText().toString().isEmpty()){
            password.setError("Please fill the password");
            return false;
        }else {
            return true;
        }
    }

}