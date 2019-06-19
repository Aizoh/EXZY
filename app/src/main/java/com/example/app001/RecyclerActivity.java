package com.example.app001;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
//
import com.example.app001.Model.PanicEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.firebase.ui.auth.AuthUI;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class RecyclerActivity extends AppCompatActivity  implements View.OnClickListener{

    private FirebaseAuth mAuth;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    //handler for recycler view
    private RecyclerView recyclerView;
    //list HOLDER FOR IMAGES
    List<PanicEvent> listEvents = new ArrayList<>();

    //recycler adapter
    private RecyclerAdapter recyclerAdapter;

    //layout manager
    private  RecyclerView.LayoutManager layoutManager;

    boolean mBound = false;
    GPSService mService;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            GPSService.GPSBinder binder = (GPSService.GPSBinder) service;
            mService = binder.getService();
            mBound = true;
            Log.e(className.toShortString(),connection.toString());
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

            mBound = false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        PopulateEvents();
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerAdapter(listEvents);
        recyclerView.setAdapter(recyclerAdapter);
        BottomNavigationView  bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_profile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        Toast.makeText(getApplicationContext(),"Profile page",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_exit:
                        Toast.makeText(getApplicationContext(),"Exit page...signing out",
                                Toast.LENGTH_SHORT).show();
                        signOut();
                        break;
                    case R.id.action_guide:
                        startActivity(new Intent(getApplicationContext(),GuideActivity.class));
                        Toast.makeText(getApplicationContext(),"Guide page",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
                return ;
            }
        });


        //Request Permissions
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Ask for permission ANdroid L >
            ActivityCompat.requestPermissions(RecyclerActivity.this, new String[]{ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,}, 11);
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
  //We have received a panic Handle it appropriately
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PanicEvent event) {
        if (mBound) {
        Location loc = mService.getLocation();
        Toast.makeText(this,event.getCaption()+" was Reported at "+getLocationAddress(loc.getLatitude(), loc.getLongitude()),Toast.LENGTH_LONG).show();
    }else {
            Toast.makeText(this,event.getCaption()+" Help is coming",Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onClick(View v) {

       switch (v.getId()){
            /*case R.id.btnloc:
                if (mBound) {
                    Location loc = mService.getLocation();
                    Toast.makeText(this, "The location is " + getLocationAddress(loc.getLatitude(), loc.getLongitude()), Toast.LENGTH_LONG).show();
                    Log.d("Location is", loc.toString());
                }
            break;*/
        }


    }

    public String getLocationAddress(double mLatitude, double mLongitude) {


        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        // Get the current location from the input parameter list
        // Create a list to contain the result address
        List<Address> addresses = null;
        try {
            /*
             * Return 1 address.
             */
            addresses = geocoder.getFromLocation(mLatitude, mLongitude, 1);
        } catch (IOException e1) {
            e1.printStackTrace();
            return ("IO Exception trying to get address:" + e1);
        } catch (IllegalArgumentException e2) {
            // Error message to post in the log
            String errorString = "Illegal arguments "
                    + mLatitude + " , "
                    + mLongitude
                    + " passed to address service";
            e2.printStackTrace();
            return errorString;
        }
        // If the reverse geocode returned an address
        if (addresses != null && addresses.size() > 0) {
            // Get the first address
            Address address = addresses.get(0);
            /*
             * Format the first line of address (if available), city, and
             * country name.
             */
            String addressText = String.format(
                    "%s, %s, %s",
                    // If there's a street address, add it
                    address.getMaxAddressLineIndex() > 0 ? address
                            .getAddressLine(0) : "",
                    // Locality is usually a city
                    address.getLocality(),
                    // The country of the address
                    address.getCountryName());
            // Return the text
            return addressText;
        } else {
            return "No address found by the service: Note to the developers, If no address is found by google itself, there is nothing you can do about it.";
        }


    }

    //OVERRIDES
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, GPSService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mBound = false;
        EventBus.getDefault().unregister(this);
    }

    /**
     * The data to display in Recylerview Items
     */
    void PopulateEvents(){
        int[] images = {R.drawable.pic1,R.drawable.pic2,R.drawable.pic3,R.drawable.pic4};
        listEvents.add(new PanicEvent(images[0],"Fire"));
        listEvents.add(new PanicEvent(images[1],"Floods"));
        listEvents.add(new PanicEvent(images[2],"Robbery"));
        listEvents.add(new PanicEvent(images[3],"Terrorism"));
    }
    public  void signOut(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Signed out",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext() ,MainActivity.class));
                        }else{
                            Toast.makeText(getApplicationContext(),"Encountered an Error !!",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
