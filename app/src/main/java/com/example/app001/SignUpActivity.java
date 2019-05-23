package com.example.app001;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmail, editTextPassword;
    Button register;
    private FirebaseAuth mAuth;
    //private FirebaseUser user = mAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_sing_up);
        editTextEmail = findViewById(R.id.eTmail);
        editTextPassword = findViewById(R.id.eTpass);
        findViewById(R.id.btnregister).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);
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

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //adding verification
                            FirebaseUser user = mAuth.getCurrentUser();
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
            case R.id.btnregister:
                registerUser();
                break;
            case R.id.textViewLogin:
                startActivity(new Intent(this,MainActivity.class));

                break;
        }
    }
}
