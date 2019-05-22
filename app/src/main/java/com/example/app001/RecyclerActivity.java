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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
//
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class RecyclerActivity extends AppCompatActivity  implements View.OnClickListener{

    //handler for recycler view
    private RecyclerView recyclerView;
    //ARRAY HOLDER FOR IMAGES
    private  int[] images = {R.drawable.pic1,R.drawable.pic2,R.drawable.pic3,R.drawable.pic4};
    //recycler adapter
    private RecyclerAdapter recyclerAdapter;

    //layout manager
    private  RecyclerView.LayoutManager layoutManager;

    //LOCATION ADDS
    Button LocButton;
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
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    //LOCATION ENDS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerAdapter(images);
        recyclerView.setAdapter(recyclerAdapter);
        //LOCATION ADDS
        LocButton = findViewById(R.id.btnloc);
        LocButton.setOnClickListener(this);

        Intent intent = new Intent(this, GPSService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        //Request Permissions
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Ask for permission ANdroid L >
            ActivityCompat.requestPermissions(RecyclerActivity.this, new String[]{ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,}, 11);
        }
        //LOCATION ENDS

    }
    //LOCATION ADDS
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnloc:
                if (mBound) {
                    Location loc = mService.getLocation();
                    Toast.makeText(this, "The location is " + getLocationAddress(loc.getLatitude(), loc.getLongitude()), Toast.LENGTH_LONG).show();
                    Log.d("Location is", loc.toString());
                }
            break;
        }

        /*if (v.getId() == R.id.btnloc) {
            if (mBound) {
                Location loc = mService.getLocation();
                Toast.makeText(this, "The location is " + getLocationAddress(loc.getLatitude(), loc.getLongitude()), Toast.LENGTH_LONG).show();
                Log.d("Location is", loc.toString());
            }
        }*/

    }
    // METHOD FOR ACCESSING LOCATION
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

    //LOCATION ENDS

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
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mBound = false;
    }
}
