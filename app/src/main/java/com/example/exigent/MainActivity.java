package com.example.exigent;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //FIELDS//

    EditText Etmail,Etpass;
    ProgressBar progressBar;

    //sign up
    EditText etReg_name, etPassword, etReg_password,
            etReg_region, etReg_phone, etReg_email, etReg_confirmemail;
    Button btnReg_register;


    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isUserLoggedIn();
        Button SignUp = findViewById(R.id.btnup);
        Button SignIn = findViewById(R.id.btnIn);
        Etmail = findViewById(R.id.eTmail);
        Etpass = findViewById(R.id.eTpass);
        TextView Tvforgotpass = findViewById(R.id.forgot_pass);
        Tvforgotpass.setVisibility(View.VISIBLE);
        progressBar = findViewById(R.id.progress_circular);
        progressBar.setVisibility(View.INVISIBLE);
        SignIn.setOnClickListener(this);
        SignUp.setOnClickListener(this);
        Tvforgotpass.setOnClickListener(this);
    }
    // a method for user input validation
    public void validateUser(){
        String email,password;
        email = Etmail.getText().toString().trim();
        password = Etpass.getText().toString().trim();

        if(email.isEmpty()){
            Etmail.setError("Enter email ");
            Etmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Etmail.setError("Enter valid Email");
            Etmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            Etpass.setError("Enter password");
            Etpass.requestFocus();
            return;
        }
        if(password.length()<6){
            Etpass.setError("Minimum of six characters");
            Etpass.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    //check if user's email is verified

                    if (firebaseAuth.getCurrentUser().isEmailVerified()){

                        startActivity(new Intent(MainActivity.this, RecyclerActivity.class));
                        //We are never coming back here after signing in
                        finish();

                    }else {
                        Toast.makeText(MainActivity.this,"PLEASE VERIFY YOUR EMAIL",
                                Toast.LENGTH_LONG).show();

                    }



                }else {
                    Toast.makeText(MainActivity.this,task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnIn:
                //startActivity(new Intent(this, SignedInActivity.class));
                //startActivity(new Intent(this, RecyclerActivity.class));
               /* if (firebaseAuth.getCurrentUser()!=null && firebaseAuth.getCurrentUser().isEmailVerified()){
                    startActivity(new Intent(this, RecyclerActivity.class));
                }else {
                    validateUser();
                }*/
               validateUser();

                break;

            case R.id.btnup:
                //registerUser();
                startActivity(new Intent(this, SignUpActivity.class));

                break;
            case R.id.forgot_pass:

                startActivity(new Intent(getApplicationContext(),ForgotPassActivity.class));
                break;

        }

    }



    private void isUserLoggedIn(){
        //User is logged in skip logging in
        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this, RecyclerActivity.class));
            //No looking back
            finish();
        }
    }

}
