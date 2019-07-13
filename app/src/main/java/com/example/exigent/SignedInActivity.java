package com.example.exigent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SignedInActivity extends AppCompatActivity {
    // Write a message to the database
   // FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference myRef = database.getReference("message");

    //myRef.setValue("Hello, World!");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);
    }
}
