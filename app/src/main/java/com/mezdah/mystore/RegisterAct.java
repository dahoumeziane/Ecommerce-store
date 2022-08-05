package com.mezdah.mystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mezdah.mystore.admin.HomeActivity;
import com.mezdah.mystore.model.User;


public class RegisterAct extends AppCompatActivity {
    EditText email, password, username;
    Button signup, signup_google;
    TextView go_to_login;
    FirebaseAuth mauth;
    DatabaseReference usersRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializationOfFields();

        go_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterAct.this,LoginActivity.class);
                startActivity(i);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkError()){
                    createAnAccount();
                }
            }
        });



    }
    private void createAnAccount(){
        String my_email = email.getText().toString();
        String my_pwd = password.getText().toString();
        mauth.createUserWithEmailAndPassword(my_email,my_pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // account created successfully !!
                            saveUserDataInDB(new User(username.getText().toString(),my_email));
                        }else{
                            //error occured (email already exist ,internet , wrong format of email , weak password)...
                            Toast.makeText(RegisterAct.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void saveUserDataInDB(User user){
        // to write data in database you need two things
        // first thing is the reference
        // second thing is the user data (the value of the data)
        usersRef.child(mauth.getCurrentUser().getUid())
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //data has been written into the database successfully
                            Intent i = new Intent(RegisterAct.this, HomeActivity.class);
                            startActivity(i);
                        }else{
                            // error occured (wrong format of data ,permission , internet connetion...etc)
                            Toast.makeText(RegisterAct.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void initializationOfFields() {
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        username = findViewById(R.id.register_name);
        signup = findViewById(R.id.register);
        signup_google = findViewById(R.id.register_google);
        go_to_login = findViewById(R.id.go_to_login);
        //Firebase auth init
        mauth=FirebaseAuth.getInstance();

        //Database ref init
        usersRef = FirebaseDatabase.getInstance(getString(R.string.db_url)).getReference().child("Users");



    }

    private boolean checkError() {
        if (email.getText().toString().isEmpty()) {
            email.setError("Please don't leave the email empty");
            return false;
        } else if (password.getText().toString().isEmpty()) {
            password.setError("Please dont leave password empty");
            return false;
        } else if (username.getText().toString().isEmpty()) {
            username.setError("Please dont leave the username empty");
            return false;
        } else {
            return true;
        }
    }


}