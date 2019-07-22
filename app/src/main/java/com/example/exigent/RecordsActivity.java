package com.example.exigent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.exigent.Model.Messages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecordsActivity extends AppCompatActivity {
    Toolbar toolbarRecords;
    ListView listViewMessages;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference currentEmergencyRecordRef = firebaseDatabase.getReference();
    private FirebaseAuth mAuth;
    private  String currentUserId,currentUserEmail;

    List<Messages> messagesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        toolbarRecords = findViewById(R.id.toolBarRecords);
        setSupportActionBar(toolbarRecords);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView tvRecords = findViewById(R.id.tvHistory);
        listViewMessages = findViewById(R.id.listViewMessages);
        messagesList = new ArrayList<>();

        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        currentUserId  = user.getUid();
        currentUserEmail = user.getEmail();
        currentEmergencyRecordRef = firebaseDatabase.getReference().child("Emergencies").child(currentUserId);
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentEmergencyRecordRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messagesList.clear();
                for ( DataSnapshot messagesSnapshot : dataSnapshot.getChildren()){

                     Messages messages = messagesSnapshot.getValue(Messages.class);
                     messagesList.add(messages);
                }
                MessageList messageAdapter = new MessageList(RecordsActivity.this,messagesList);
                listViewMessages.setAdapter( messageAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
