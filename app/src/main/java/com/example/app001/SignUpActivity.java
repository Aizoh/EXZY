package com.example.app001;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app001.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmail, editTextPassword;
    Button register;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    //private FirebaseUser user = mAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_sing_up);
        editTextEmail = findViewById(R.id.eTmail);
        editTextPassword = findViewById(R.id.eTpass);
        TextView Tvforgotpass = findViewById(R.id.forgot_pass);
        Tvforgotpass.setVisibility(View.INVISIBLE);
        findViewById(R.id.btnIn).setOnClickListener(this);
        findViewById(R.id.btnup).setOnClickListener(this);
        progressBar = findViewById(R.id.progress_circular);
        progressBar.setVisibility(View.INVISIBLE);
        FirebaseApp.initializeApp(SignUpActivity.this);
        mAuth = FirebaseAuth.getInstance();
    }
    private  void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

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
        if(password.length()<6){
            editTextPassword.setError("Minimum of six characters");
            editTextPassword.requestFocus();
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
                            FirebaseUser user = mAuth.getCurrentUser();
                            //adding a database account for details created
                            String email = user.getEmail();
                            String  uid  = user.getUid();
                            //use the POJO User Object
                          User usersave =  new User();
                            usersave.setEmail(email);
                            usersave.setId(uid);
                            usersave.setName("");
                            usersave.setPhone("");

                            // TODO: 5/27/2019
                            //Instantiate firebase database
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            //path to store the data:were storing the information under Users
                            DatabaseReference databaseReference = firebaseDatabase.getReference("Users");

                            databaseReference.child(" UsersProfile").push().setValue(usersave);

                            //END OF FIREBASE REALTIME DATABASE
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(getApplicationContext(), "Registration was successful check your email for verification",
                                                Toast.LENGTH_SHORT).show();
                                        editTextEmail.setText("");
                                        editTextPassword.setText("");

                                        // updateUI(user);

                                    }else {

                                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }



                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            // Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "We have encountered an Issue! Try Again Later",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // ...
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnup:
                registerUser();
                break;
            case R.id.btnIn:
                startActivity(new Intent(this,MainActivity.class));

                break;
        }
    }
}
