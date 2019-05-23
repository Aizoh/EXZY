package com.example.app001;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app001.Model.PanicEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //FIELDS//

    EditText Etmail,Etpass;

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

        SignIn.setOnClickListener(this);
        SignUp.setOnClickListener(this);
        Tvforgotpass.setOnClickListener(this);
    }
    // a method for validation
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

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

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
                //Toast.makeText(this, "WELCOME HOME",Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(this, SignUpActivity.class));

                break;
            case R.id.forgot_pass:
                startActivity(new Intent(MainActivity.this,ForgotPassActivity.class));
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
