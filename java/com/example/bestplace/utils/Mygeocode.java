package com.example.bestplace.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.bestplace.db_room.postR.Post;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//Mygeocode is a utility class that implements the method of fusedlocation provider
public class Mygeocode implements OnSuccessListener<Location> {
    //flpc
    FusedLocationProviderClient fusedLocationProviderClient;
    //where stram the location oputput
    TextView textView;
    //location request
    LocationRequest locationRequest;
    //context
    Activity context;
    //post
    Post post;
    //constructor and set the fusedpoviderclient
    public Mygeocode(Activity context, TextView textView, Post post) {
        this.context = context;
        this.textView = textView;
        this.post = post;
        fusedLocationProviderClient = new FusedLocationProviderClient(context);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);

    }
    //get the last know location.
    public void g_last_loc(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this);
    }
    //on success callback
    @Override
    public void onSuccess(Location location) {
        if (location == null) {
            Toast.makeText(context, "Trying to found a position\n turn on gps", Toast.LENGTH_SHORT).show();
        } else {

            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                //convert the location result set on the textview and the post.location
                List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                textView.setText(address.get(0).getLocality());
                post.setLocation(address.get(0).getLocality());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


