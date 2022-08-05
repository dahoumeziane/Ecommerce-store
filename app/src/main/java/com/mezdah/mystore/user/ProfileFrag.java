package com.mezdah.mystore.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mezdah.mystore.R;
import com.mezdah.mystore.RegisterAct;
import com.mezdah.mystore.model.User;

public class ProfileFrag extends Fragment {

    private View mview;
    Button logout;
    TextView email_display,username_display;
    FirebaseAuth mauth;
    DatabaseReference usersRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview=inflater.inflate(R.layout.fragment_profile, container, false);
        logout = mview.findViewById(R.id.logout);
        email_display = mview.findViewById(R.id.email_display);
        username_display = mview.findViewById(R.id.username_display);
        mauth= FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance(getString(R.string.db_url)).getReference()
                        .child("Users").child(mauth.getCurrentUser().getUid());
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mauth.signOut();
                Intent i = new Intent(getContext(),RegisterAct.class);
                startActivity(i);
                getActivity().finish();

            }
        });
        getUserInfo();
        return mview;
    }
    private  void getUserInfo(){
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    username_display.setText(user.getUsername());
                    email_display.setText(user.getEmail());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

}