package com.example.exigent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.viewmodel.AuthViewModelBase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageViewProfile;
    TextView tvName,tvEmail,tvPhone,tvRegion;
    TextView tvEname1,tvEphone1,tvErelationship1,tvEname2,tvEphone2,tvErelationship2;

    String pname,pemail,pphone,pregion;
    String pename1,pephone1,perelate1,pename2,pephone2,perelate2;

    FloatingActionButton btnEditProfile;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference profileDisplayRef = firebaseDatabase.getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String currentUserId;
    private DatabaseReference profileDisplayref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageViewProfile = findViewById(R.id.etImageViewProfile);

        Toolbar toolbarProfile = findViewById(R.id.toolBarUserProfile);
        setSupportActionBar(toolbarProfile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvProfile = findViewById(R.id.tvProfile);
        //Button btnEmergencyTopic = findViewById(R.id.btnemergencyTopic);

        tvName = findViewById(R.id.etName);
        tvEmail = findViewById(R.id.etEmail);
        tvPhone = findViewById(R.id.etPhone);
        tvRegion = findViewById(R.id.etRegion);

        tvEname1 = findViewById(R.id.etEmergeCont1Name);
        tvEphone1 = findViewById(R.id.etEmergeCont1Phone);
        tvErelationship1 = findViewById(R.id.etEmergeCont1relationship);

        tvEname2 = findViewById(R.id.etEmergeCont2Name);
        tvEphone2 = findViewById(R.id.etEmergeCont2Phone);
        tvErelationship2 = findViewById(R.id.etEmergeCont2Relationship);

        btnEditProfile = findViewById(R.id.floating_buttonEditProfile);
        btnEditProfile.setOnClickListener(this);

        getDetailsFromFirebase();
        //setDisplay();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floating_buttonEditProfile:
                startActivity(new Intent(getApplicationContext(),EditProfileActivity.class));
                break;
        }
    }
    /* TODO 11th render details from firebase*/

    public void getDetailsFromFirebase(){
        // assign values to the strings from firebase.
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        String  uid1  = user.getUid();

        profileDisplayRef = FirebaseDatabase.getInstance().getReference().child("Users/UsersProfile").child(uid1);

            String semail = user.getEmail();
            tvEmail.setText(semail);



        profileDisplayRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){


                    String sname = dataSnapshot.child("name").getValue().toString();
                    String sphone =dataSnapshot.child("phone").getValue().toString();
                    String sregion= dataSnapshot.child("region").getValue().toString();
                    //String semail= dataSnapshot.child("email").getValue().toString();
                    String sename1 = dataSnapshot.child("eName1").getValue().toString();
                    String sephone1 = dataSnapshot.child("ePhone1").getValue().toString();
                    String serelate1= dataSnapshot.child("eRelationship1").getValue().toString();
                    String sename2 = dataSnapshot.child("eName2").getValue().toString();
                    String sephone2 = dataSnapshot.child("ePhone2").getValue().toString();
                    String serelate2 = dataSnapshot.child("erelationship2").getValue().toString();

                    tvName.setText(sname);
                    tvPhone.setText(sphone);
                    tvRegion.setText(sregion);
                    tvEname1.setText(sename1);
                    tvEphone1.setText(sephone1);
                    tvErelationship1.setText(serelate1);
                    tvEname2.setText(sename2);
                    tvEphone2.setText(sephone2);
                    tvErelationship2.setText(serelate2);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });

        /*//FirebaseUser user = mAuth.getCurrentUser();
        currentUserId = mAuth.getCurrentUser().getUid();
        profileUserRef = FirebaseDatabase.getInstance().getReference("Users/UsersProfile").child(currentUserId);

        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String sname = dataSnapshot.child("name").getValue().toString();
                    String sphone =dataSnapshot.child("phone").getValue().toString();
                    String sregion= dataSnapshot.child("region").getValue().toString();
                    String semail= dataSnapshot.child("email").getValue().toString();
                    String sename1 = dataSnapshot.child("eName1").getValue().toString();
                    String sephone1 = dataSnapshot.child("ePhone1").getValue().toString();
                    String serelate1= dataSnapshot.child("eRelationship1").getValue().toString();
                    String sename2 = dataSnapshot.child("eName2").getValue().toString();
                    String sephone2 = dataSnapshot.child("ePhone2").getValue().toString();
                    String serelate2 = dataSnapshot.child("erelationship2").getValue().toString();

                    tvName.setText(sname);
                    tvEmail.setText(semail);
                    tvPhone.setText(sphone);
                    tvRegion.setText(sregion);

                    tvEname1.setText(sename1);
                    tvEphone1.setText(sephone1);
                    tvErelationship1.setText(serelate1);
                    tvEname2.setText(sename2);
                    tvEphone2.setText(sephone2);
                    tvErelationship2.setText(serelate2);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }
    // set fields display with the fetched firebase data
    public void setDisplay(){
        //setting values
        imageViewProfile.setImageURI(Uri.EMPTY);
        tvName.setText(pname);
        tvEmail.setText(pemail);
        tvPhone.setText(pphone);
        tvRegion.setText(pregion);

        tvEname1.setText(pename1);
        tvEphone1.setText(pephone1);
        tvErelationship1.setText(perelate1);
        tvEname2.setText(pename1);
        tvEphone2.setText(pephone2);
        tvErelationship2.setText(perelate2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity( new Intent(ProfileActivity.this,MainActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDetailsFromFirebase();
    }
}
