package com.example.app001;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity extends AppCompatActivity implements View.OnClickListener {

    EditText Etforgot_email;
    Button BtnForgotpass;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pass);
        Etforgot_email = findViewById(R.id.f_email);
        BtnForgotpass = findViewById(R.id.btnforgot_pass);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        Etforgot_email.setOnClickListener(this);
        BtnForgotpass.setOnClickListener(this);


    }
    public void resetPassword(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress;
        emailAddress = Etforgot_email.getText().toString().trim();
        if(emailAddress.isEmpty()){
            Etforgot_email.setError("Enter email ");
            Etforgot_email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            Etforgot_email.setError("Enter valid Email");
            Etforgot_email.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(emailAddress).
                addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassActivity.this,
                             "Password sent to your email",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ForgotPassActivity.this,
                            task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnforgot_pass:
                resetPassword();
                break;

        }
    }
}
