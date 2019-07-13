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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import static android.Manifest.permission.SEND_SMS;

public class SendAlertActivity extends AppCompatActivity  implements View.OnClickListener{

    EditText etMsg;
    private BroadcastReceiver sentStatusReceiver, deliveredStatusReceiver;
    private static final int REQUEST_SMS = 0;
    ProgressBar pgBarEmergency;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_alert);
        Intent intent = getIntent();
        String areaMsg = intent.getStringExtra(RecyclerActivity.MESSAGE);
        Button sendMsg = findViewById(R.id.btnSendMsg);
        pgBarEmergency = findViewById(R.id.progressBarEmergency);
        pgBarEmergency.setVisibility(View.INVISIBLE);
        etMsg = findViewById(R.id.txtMsg);
        etMsg.setText(areaMsg);
        sendMsg.setOnClickListener(this);
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
        String message = etMsg.getText().toString().trim();
        String phone = "0792640208";
        if(message.isEmpty()){
            etMsg.setError("Enter text to send");
            etMsg.requestFocus();
        }
        pgBarEmergency.setVisibility(View.VISIBLE);
        // class for sending the text message
        SmsManager smsManager = SmsManager.getDefault();
        //for long text messages split
        List<String> messages = smsManager.divideMessage(message);
        for (String msg : messages){

            PendingIntent sendIntent = PendingIntent.getBroadcast(this,0,new Intent("SMS_SENT"),0);
            PendingIntent deliveredIntent = PendingIntent.getBroadcast(this,0,new Intent("SMS_DELIVERED"),0);
            smsManager.sendTextMessage(phone,null,msg,sendIntent,deliveredIntent);
        }
        pgBarEmergency.setVisibility(View.GONE);
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
                        s = "Generic failure Error";
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        s = "Error : Service Unavailable";
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        s = "Error: Null PDU";
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        s = "Error : Radio Off/ Network off";
                        break;
                    default:
                        break;
                }
            }
        };
        deliveredStatusReceiver  = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = "Message not Delivered";
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        s = "Message Received Help is on the way";
                        break;
                    case Activity.RESULT_CANCELED:
                        break;

                }
                etMsg.setText("");
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
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
