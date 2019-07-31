package com.example.exigent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exigent.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView etImageViewProfile;
    EditText etName,etPhone,etRegion,etEmail;
    EditText etEname1,etEphone1,etErelationship1,etEname2,etEphone2,etErelationship2;

    Button btnUpdateprofile,btnCancelEdit;


    String name,phone,email,region;
    String ename1,ephone1,erelate1,ename2,ephone2,erelate2;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference profileUpdateRef = firebaseDatabase.getReference();
    DatabaseReference currentDetailsRef = firebaseDatabase.getReference();
    private FirebaseAuth mAuth;
    private  String currentUserId;


    //FirebaseStorage imageStorage = FirebaseStorage.getInstance();
    StorageReference profileImageStorageRef;

    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri imagefilePath, resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar editProfileToolbar = findViewById(R.id.toolBarEditUserProfile);
        setSupportActionBar(editProfileToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView tvEditProfile = findViewById(R.id.tvEditProfile);

        btnUpdateprofile = findViewById(R.id.btnUpdateProfile);
        //btnCancelEdit = findViewById(R.id.btnCancelEdit);
        etImageViewProfile = findViewById(R.id.etImageViewProfile);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etRegion = findViewById(R.id.etRegion);
        //etEmail = findViewById(R.id.etEmail);

        etEname1 = findViewById(R.id.etEmergeCont1Name);
        etEphone1 = findViewById(R.id.etEmergeCont1Phone);
        etErelationship1 = findViewById(R.id.etEmergeCont1relationship);
        etEname2 = findViewById(R.id.etEmergeCont2Name);
        etEphone2 = findViewById(R.id.etEmergeCont2Phone);
        etErelationship2 = findViewById(R.id.etEmergeCont2Relationship);

        // choosing an image to upload
        etImageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(EditProfileActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditProfileActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
                    } else {

                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);*/
            }
        });
        etName.setOnClickListener(this);
        etPhone.setOnClickListener(this);
        etRegion.setOnClickListener(this);
       // etEmail.setOnClickListener(this);

        etEname1.setOnClickListener(this);
        etEphone1.setOnClickListener(this);
        etErelationship1.setOnClickListener(this);
        etEname2.setOnClickListener(this);
        etEphone2.setOnClickListener(this);
        etErelationship2.setOnClickListener(this);
        btnUpdateprofile.setOnClickListener(this);

        FirebaseStorage imageStorage = FirebaseStorage.getInstance();
        profileImageStorageRef = imageStorage.getReference("Profile images");
        displayCurrentDetails();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case PICK_IMAGE_REQUEST:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }
    //to pick the changes and populate in firebase db
    public void displayCurrentDetails(){

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        currentUserId  = user.getUid();

        currentDetailsRef = FirebaseDatabase.getInstance().getReference().child("Users/UsersProfile").child(currentUserId);

        currentDetailsRef.addValueEventListener(new ValueEventListener() {
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

                    // set the input fields to the current data in firebase db.

                    etName.setText(sname);
                    etPhone.setText(sphone);
                    etRegion.setText(sregion);
                    //etEmail.setText(semail);

                    etEname1.setText(sename1);
                    etEphone1.setText(sephone1);
                    etErelationship1.setText(serelate1);
                    etEname2.setText(sename2);
                    etEphone2.setText(sephone2);
                    etErelationship2.setText(serelate2);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void updateProfile(){

        name = etName.getText().toString().trim();
        phone = etPhone.getText().toString().trim();
        region = etRegion.getText().toString().trim();
        //email = etEmail.getText().toString().trim();

        ename1 = etEname1.getText().toString().trim();
        ephone1 = etEphone1.getText().toString().trim();
        erelate1 = etErelationship1.getText().toString().trim();
        ename2 = etEname2.getText().toString().trim();
        ephone2 = etEphone2.getText().toString().trim();
        erelate2 = etErelationship2.getText().toString().trim();

        if(name.isEmpty()){
            etName.setError("Enter email ");
            etName.requestFocus();
            return;
        }
        if(phone.isEmpty()){
            etPhone.setError("Enter Phone ");
            etPhone.requestFocus();
            return;
        }
        if(region.isEmpty()){
            etRegion.setError("Enter email ");
            etRegion.requestFocus();
            return;
        }
        /*if(email.isEmpty()){
            etEmail.setError("Enter email ");
            etEmail.requestFocus();
            return;
        }*/
        ///emergency contact
        if(ename1.isEmpty()){
            etEname1.setError("Enter name  ");
            etEname1.requestFocus();
            return;
        }
        if(ephone1.isEmpty()){
            etEphone1.setError("Enter phone number ");
            etEphone1.requestFocus();
            return;
        }
        if(erelate1.isEmpty()){
            etErelationship1.setError("Enter email ");
            etErelationship1.requestFocus();
            return;
        }
        if(ename2.isEmpty()){
            etEname2.setError("Enter name  ");
            etEname2.requestFocus();
            return;
        }
        if(ephone2.isEmpty()){
            etEphone2.setError("Enter phone number ");
            etEphone2.requestFocus();
            return;
        }
        if(erelate2.isEmpty()){
            etErelationship2.setError("Enter email ");
            etErelationship2.requestFocus();
            return;
        }




        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        String  uid1  = user.getUid();

        profileUpdateRef = FirebaseDatabase.getInstance().getReference().child("Users/UsersProfile").child(uid1);


        HashMap usersave2 = new HashMap();
            usersave2.put("name",name);
            usersave2.put("phone",phone);
            //usersave2.put("email",email);
            usersave2.put("region",region);
            usersave2.put("eName1",ename1);
            usersave2.put("eName2",ename2);
            usersave2.put("ePhone1",ephone1);
            usersave2.put("ePhone2",ephone2);
            usersave2.put("eRelationship1",erelate1);
            usersave2.put("erelationship2",erelate2);

            profileUpdateRef.updateChildren( usersave2).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        /*Toast.makeText(getApplicationContext(),"succesfully updated Profile",
                                Toast.LENGTH_LONG).show();*/
                    }

                }
            });




        uploadImageFile();
        Toast.makeText(getApplicationContext(),"profile updated",Toast.LENGTH_LONG).show();

    }
    //cancel and go back to profile
    public void cancelUpdate(){

        startActivity( new Intent(getApplicationContext(),ProfileActivity.class));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnUpdateProfile:
                updateProfile();

                break;
            /*case R.id.btnCancelEdit:
                cancelUpdate();
                break;*/
        }

    }
    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imagefilePath = data.getData();


        }

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagefilePath);
                etImageViewProfile.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }


    }

    //upload image independently
    private void uploadImageFile() {
            //if there is a file to upload
            if (imagefilePath != null) {
                //displaying a progress dialog while upload is going on
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading");
                progressDialog.show();

                //Uri imageFile = Uri.fromFile(new File(String.val ueOf(imagefilePath)));


                FirebaseUser user = mAuth.getInstance().getCurrentUser();
                String  uid1  = user.getUid();
                currentUserId  = user.getUid();

                final StorageReference profileImgRef = profileImageStorageRef.child("profile image"+currentUserId +".jpg");
                profileImgRef.putFile(imagefilePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //if the upload is successfull
                                //hiding the progress dialog
                                progressDialog.dismiss();

                                //and displaying a success toast
                                Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                                profileImgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        Picasso.with(getApplicationContext())
                                                .load(uri)
                                                .into(etImageViewProfile);


                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                //if the upload is not successfull
                                //hiding the progress dialog
                                progressDialog.dismiss();

                                //and displaying error message
                                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                //calculating progress percentage
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                //displaying percentage in progress dialog
                                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                            }
                        });
                }else {
                Toast.makeText(getApplicationContext(),"failed uploading...try again later",
                        Toast.LENGTH_LONG).show();
                //you can display an error toast
            }
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EditProfileActivity.this,ProfileActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayCurrentDetails();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();
    }
}
