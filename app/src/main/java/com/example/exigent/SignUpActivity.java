package com.example.exigent;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exigent.Model.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextName, editTextEmail, editTextPassword,editTextPhone,editTextRegion;
    TextInputLayout textInputLayoutPassword;
    Button register;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    private static final String TAG = "DBLog";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_sing_up);
        textInputLayoutPassword = findViewById(R.id.tInLyRegPassword);
        editTextName = findViewById(R.id.etName);
        editTextEmail = findViewById(R.id.etEmail);
        editTextPassword = findViewById(R.id.etPass);
        editTextPhone = findViewById(R.id.etPhone);
        editTextRegion = findViewById(R.id.etRegion);
        /*TextView Tvforgotpass = findViewById(R.id.forgot_pass);
        Tvforgotpass.setVisibility(View.INVISIBLE);
        Button btnIn = findViewById(R.id.btnIn);
        btnIn.setVisibility(View.INVISIBLE);*/
        //findViewById(R.id.btnIn).setOnClickListener(this);

        findViewById(R.id.btnSignUp).setOnClickListener(this);
        progressBar = findViewById(R.id.progress_circularReg);
        progressBar.setVisibility(View.INVISIBLE);
        FirebaseApp.initializeApp(SignUpActivity.this);
        mAuth = FirebaseAuth.getInstance();
    }
    private  void registerUser(){
        final String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();
        final String region = editTextRegion.getText().toString().trim();
        if (name.isEmpty()){
            editTextName.setError("Enter name");
            editTextName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextEmail.setError("Enter email ");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Enter valid Email");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("Enter password");
            editTextPassword.requestFocus();
            return;
        }
        if(!password.isEmpty()){
            textInputLayoutPassword.setPasswordVisibilityToggleEnabled(false);
        }
        if(password.length()<6){
            editTextPassword.setError("Minimum of six characters");
            editTextPassword.requestFocus();
            return;
        }
        if(phone.isEmpty()){
            editTextPhone.setError("Enter phone number");
            editTextPhone.requestFocus();
            return;
        }
        if (phone.length()<10){
            editTextPhone.setError("Invalid Phone number");
            editTextPhone.requestFocus();
            return;
        }
        if (region.isEmpty()){
            editTextRegion.setError("Enter region");
            editTextRegion.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            //adding verification
                            FirebaseUser user = mAuth.getInstance().getCurrentUser();
                            //adding a database account for details created
                            String email = user.getEmail();
                            String  uid  = user.getUid();
                            //use the POJO User Object
                          User usersave =  new User();
                            usersave.setEmail(email);
                            usersave.setId(uid);
                            usersave.setName(name);
                            usersave.setPhone(phone);
                            usersave.setRegion(region);
                            usersave.seteName1("person1 name");
                            usersave.setePhone1("07XXXXXXX");
                            usersave.seteRelationship1("relationship");
                            usersave.seteName2("person2 name");
                            usersave.setePhone2("07XXXXXXX");
                            usersave.setERelationship2("relationship");

                            // TODO: 5/27/2019
                            // Instantiate firebase database
                           // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            //path to store the data:were storing the information under Users
                            DatabaseReference databaseReference = firebaseDatabase.getReference("Users");

                            databaseReference.child("UsersProfile").child(uid).setValue(usersave).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),"Successful user added",
                                                Toast.LENGTH_LONG).show();
                                    }else{

                                        Toast.makeText(getApplicationContext(),"Failure adding user",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(SignUpActivity.class.getCanonicalName(),e.getMessage());
                                }
                            });
                           // TODO  :25/07/2019 add a firestore database

                          user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(getApplicationContext(), "Registration was successful check your email for verification",
                                                Toast.LENGTH_SHORT).show();
                                        editTextName.setText("");
                                        editTextEmail.setText("");
                                        editTextPassword.setText("");
                                        editTextPhone.setText("");
                                        editTextRegion.setText("");

                                        // updateUI(user);

                                    }else {

                                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }



                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                             Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "We have encountered an Issue! Try Again Later",
                                    Toast.LENGTH_LONG).show();
                           // updateUI(null);
                        }

                        // ...

                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignUp:
                registerUser();

                break;
            case R.id.btnIn:
                startActivity(new Intent(this,MainActivity.class));

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignUpActivity.this,MainActivity.class));
    }

    /*public  void signOut(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        if(task.isSuccessful()){
                           // Toast.makeText(getApplicationContext(),"Signed out",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext() ,MainActivity.class));
                        }else{
                            Toast.makeText(getApplicationContext(),"Encountered an Error !!",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }*/
}
