package com.example.exigent;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exigent.Model.Messages;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.Manifest.permission.SEND_SMS;

public class SendAlertActivity extends AppCompatActivity  implements View.OnClickListener{

    EditText etMsg;
    TextView tvSendMessage;
    Toolbar toolbarSendMessage;
    private BroadcastReceiver sentStatusReceiver, deliveredStatusReceiver;
    private static final int REQUEST_SMS = 0;
    ProgressBar pgBarEmergency;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference currentEmergencyDetailsRef = firebaseDatabase.getReference();
    DatabaseReference currentEmergencyRecordRef = firebaseDatabase.getReference();
    private FirebaseAuth mAuth;
    String currentUserId,sephone1,sephone2,currenUserEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_alert);
        Intent intent = getIntent();
        String areaMsg = intent.getStringExtra(RecyclerActivity.MESSAGE);
        toolbarSendMessage = findViewById(R.id.toolBarSendMessage);
        setSupportActionBar(toolbarSendMessage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvSendMessage = findViewById(R.id.tvSendMessage);
        Button sendMsg = findViewById(R.id.btnSendMsg);
        pgBarEmergency = findViewById(R.id.progressBarEmergency);
        pgBarEmergency.setVisibility(View.INVISIBLE);
        etMsg = findViewById(R.id.txtMsg);
        etMsg.setText(areaMsg);
        Linkify.addLinks(etMsg , Linkify.WEB_URLS);
        sendMsg.setOnClickListener(this);
        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        currentUserId  = user.getUid();
        currenUserEmail= user.getEmail();
        getEmergencyContact();
    }

    public void getEmergencyContact(){
        currentEmergencyDetailsRef = FirebaseDatabase.getInstance().getReference().child("Users/UsersProfile").child(currentUserId);
        currentEmergencyDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    sephone1 = dataSnapshot.child("ePhone1").getValue().toString();
                    sephone2 = dataSnapshot.child("ePhone2").getValue().toString();

                }else {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSendMsg:
                //send message permission check for vesions higher or equal to marshmallow
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    int hasSMSPermission = checkSelfPermission(SEND_SMS);
                    if(hasSMSPermission != PackageManager.PERMISSION_GRANTED){
                        if (! shouldShowRequestPermissionRationale(SEND_SMS)){
                            showMessageOKCancel( "you need to allow access to send SMS", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                        requestPermissions(new String[]{SEND_SMS},REQUEST_SMS);
                                    }

                                }
                            });
                            return;
                        }
                        requestPermissions(new String[]{SEND_SMS},REQUEST_SMS);
                        return;
                    }
                }
                sendMessage();
                break;
        }
    }
    public void sendMessage(){
        pgBarEmergency.setVisibility(View.VISIBLE);
        String message = etMsg.getText().toString().trim();
        String phone = "0792640208";
        //String phone1 = "0775526118";
        String [] phoneNos= new  String []{phone,sephone1,sephone2};
        if(message.isEmpty()){
            etMsg.setError("Enter text to send");
            etMsg.requestFocus();
        }
        for ( String phoneNo : phoneNos ){

            // class for sending the text message
            SmsManager smsManager = SmsManager.getDefault();
            //for long text messages split
            List<String> messages = smsManager.divideMessage(message);
            for (String msg : messages){

                PendingIntent sendIntent = PendingIntent.getBroadcast(this,0,new Intent("SMS_SENT"),0);
                PendingIntent deliveredIntent = PendingIntent.getBroadcast(this,0,new Intent("SMS_DELIVERED"),0);
                //smsManager.sendTextMessage(phoneNo,,msg,sendIntent,deliveredIntent);
                smsManager.sendTextMessage(phoneNo,null,msg,sendIntent,deliveredIntent);
            }

        }
        Date currentTime = Calendar.getInstance().getTime();
        String dateTime = currentTime.toString();

        currentEmergencyRecordRef = firebaseDatabase.getReference().child("Emergencies").child(currentUserId);
        Messages message1 = new Messages();
        message1.setDate(dateTime);
        message1.setMessage(message);

        currentEmergencyRecordRef.push().setValue(message1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    Toast.makeText(getApplicationContext(),"Added to Records",
                            Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(getApplicationContext(),"Failed to add to Records",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        // Progress dialogue
       /* final ProgressDialog msgDialogue = new ProgressDialog(getApplicationContext());
        msgDialogue.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        msgDialogue.setMessage("Reporting Emergency......");
        msgDialogue.setTitle("Exigency");

        msgDialogue.show();*/


    }
    public void onRequestPermissionResult(int requestCode,String permissions[],int[] grantResults){
        switch (requestCode){
            case REQUEST_SMS:
                if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"Permission granted you can now send your message",Toast.LENGTH_SHORT).show();
                    sendMessage();
                }else {
                    Toast.makeText(getApplicationContext(),"Permission Denied, you cant access sms",Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(SEND_SMS)){
                            showMessageOKCancel("you need to  allow access to both the permission", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                        requestPermissions(new String[]{SEND_SMS},REQUEST_SMS);
                                    }
                                }
                            });
                            return;
                        }
                    }

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }
    private boolean checkPermission(){
        return (ContextCompat.checkSelfPermission(getApplicationContext(),SEND_SMS) == PackageManager.PERMISSION_GRANTED);
    }
    private  void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{SEND_SMS},REQUEST_SMS);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK",okListener)
                .setNegativeButton("Cancel",null)
                .create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sentStatusReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = "Unknown Error";
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        s = "Message sent Successfully !";
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        //s = "Generic failure Error";
                        s = "Failed ...Top up your Account try Again Later";
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        s = "Error : Service Unavailable";
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        s = "Error: Null PDU";
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        s = "Error : Radio Off/ Network Unavailable";
                        break;
                    default:
                        s = "Failed sending message .Try Again Later....";
                        break;
                }
                etMsg.setText("");
                pgBarEmergency.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        };
        deliveredStatusReceiver  = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = "Message not Delivered";
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        s = "Message Received Help is on the way...";
                        break;
                    case Activity.RESULT_CANCELED:
                        s = "Failed to Deliver the message try Again Later...";
                        break;

                }
               // etMsg.setText("");
               // pgBarEmergency.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                //startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        };
        registerReceiver(sentStatusReceiver,new IntentFilter("SMS_SENT"));
        registerReceiver(deliveredStatusReceiver,new IntentFilter("SMS_DELIVERED"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(sentStatusReceiver);
        unregisterReceiver(deliveredStatusReceiver);
    }
}
