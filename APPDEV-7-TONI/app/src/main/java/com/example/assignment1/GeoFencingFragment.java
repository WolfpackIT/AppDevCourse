package com.example.assignment1;


import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

/**
 * Created by Wolfpack on 5/25/2018.
 */

public class GeoFencingFragment extends Fragment {
    public static final String TAG = "GeoFencingFragment";
    private Toolbar toolbar;
    private NotificationManagerCompat notificationManager;

    //RecyclerView//
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private  ArrayList<GeofencingItem> geofencingItems;
    //RecycerView//

    public static final int REQUEST_CHECK_SETTINGS = 1;

    //Create location services client
    private FusedLocationProviderClient mFusedLocationClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_geofencing, container, false);
        Log.d(TAG, "CREATED: GeoFencingFragment");

        toolbar = (Toolbar) view.findViewById(R.id.my_toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        createLocationRequest();

        mRecyclerView = view.findViewById(R.id.place_list_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        geofencingItems = new ArrayList<>();
        mAdapter = new GeofencingAdapter(geofencingItems);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        notificationManager = NotificationManagerCompat.from(getActivity());
        getDeviceChangeLocation();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main2, menu);
    }

    //SETTING UP THE ACTION HANDLERS IN APPBAR
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.get_fence:
                    getDeviceLocation();
                return true;
            case R.id.set_fence:
                    setDeviceLocation();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public static final String SHOUT_ACTION = "com.example.assignment1.ShoutsFragment";

    //CREATE NOTIFICATION FOR GET FENCE
    public void sendOnChannelGetFence(Double longt, Double lau, Double alt, Float speed) {
        Intent shoutIntent = new Intent(getActivity(), MainActivity.class);
        shoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        shoutIntent.putExtra("shoutsFragment", "displayShoutsFragment");
        PendingIntent shoutPendingIntent = PendingIntent.getActivity(getContext(), 0, shoutIntent, 0);

        String title = "Get Fence";
        String message = "Longtiude: " + longt.toString() + " , " + "Lautide: " + lau.toString();
        String big_message = "Here is a detailed information about the Geo gencing of your last known location";
        Notification notification = new NotificationCompat.Builder(getActivity(), App.CHANNEL_GET_FENCE)
                .setSmallIcon(R.drawable.ic_location_on_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(big_message + "\n Longtitude: " + longt + "\n Lautitude: " + lau + "\n Altitude: " + alt.toString() + "\n Speed: " + speed.toString() + "\n That is all the information you need!"))
                .setContentIntent(shoutPendingIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_location_on_black_24dp, "Start app", PendingIntent.getActivity(getActivity(), 0, shoutIntent, PendingIntent.FLAG_ONE_SHOT))
                .addAction(R.drawable.ic_location_on_black_24dp, "Archive", shoutPendingIntent)
                .build();

        notificationManager.notify(1, notification);
    }

    //Set up a location request
    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

    }

    //GET THE DEVICE'S LOCATION -- GET FENCE
    private void getDeviceLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try {
            Task location = mFusedLocationClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: found location!");

                    } else {
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(getActivity(), "Unable to get current location!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            location.addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        sendOnChannelGetFence(location.getLongitude(), location.getLatitude(), location.getAltitude(), location.getSpeed());
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException" + e.getMessage());
        }
    }

    //GET THE DEVICE'S LOCATION --> SET FENCE
    private void setDeviceLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try {
            Task location = mFusedLocationClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: found location!");

                    } else {
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(getActivity(), "Unable to get current location!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            location.addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        Double longi = location.getLongitude();
                        Double lauti = location.getLatitude();
                        Toast.makeText(getActivity(), "Sucessfully added Geofence!", Toast.LENGTH_SHORT).show();
                        insertGeoFenceItems(longi.toString(), lauti.toString());
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException" + e.getMessage());
            Toast.makeText(getActivity(), "Failed to add Geofence!", Toast.LENGTH_SHORT).show();
        }
    }

    //INSERT GEOFENCING ITEMS --> SET FENCE
    public void insertGeoFenceItems(String longi, String lauti) {
        geofencingItems.add(new GeofencingItem(longi.toString(), lauti.toString()));
        mAdapter.notifyDataSetChanged();
    }

    //GET DEVICE CHANGE LOCATION SETTINGS
    private void getDeviceChangeLocation() {

        createLocationRequest();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();

        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                locationSettingsResponse.getLocationSettingsStates();
                Log.d(TAG, locationSettingsResponse.getLocationSettingsStates().toString());
            }
        });

        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        });
    }
}
