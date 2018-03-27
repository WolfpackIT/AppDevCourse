package com.example.mzfirstspam;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

//import android.location.Location;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationRequest;



import static android.content.Context.LOCATION_SERVICE;




public class GeofencingFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Geofence> fenceList;
    ArrayList<Location> locList;
    FenceDB db = Room.databaseBuilder(getActivity().getApplicationContext(),
            FenceDB.class, "fence").build();


    //private LocationListener mLocationListener;
    Double currentLattitude;
    Double currentLongitude;
    String provider;
    boolean locUpdate;
    boolean setFence;
    LocationManager mLocationManager;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("xxx", "onCreate: "+db.fenceDao().getAll());


        LocationManager mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        provider = mLocationManager.getBestProvider(new Criteria(), false);
        checkLocationPermission();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_geofencing, container, false);




        mRecyclerView = /** (RecyclerView) **/view.findViewById(R.id.FencingList);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        Log.d("debugMode", "The application stopped after this");
        mRecyclerView.setLayoutManager(mLayoutManager);


        Location[] arrLoc = locList.toArray(new Location[locList.size()]);

        mAdapter = new MyItemRecyclerViewAdapter2(arrLoc, getContext());
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button fence = (Button) getView().findViewById(R.id.buttonFence);
        Button loc = (Button) getView().findViewById(R.id.buttonLoc);
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationAction(view);
            }
        });
        fence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFence(view);
            }
        });
    }

    public void locationAction(View view) {
        //Log.d("locMngr", "reached");
        locUpdate = true;
        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if(!checkLocationPermission()){
            Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsIntent);
        }
        mLocationManager.requestSingleUpdate(provider,mLocationListener, null );


    }

    public void setFence(View view){
        //Log.d("fencemgr", "reached");
        setFence = true;
        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if(!checkLocationPermission()){
            Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsIntent);
        }
        mLocationManager.requestSingleUpdate(provider,mLocationListener, null );

    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            currentLattitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            Snackbar onLocSnack = Snackbar.make(getView(), "currentLattitude: "+currentLongitude, 1000);
            onLocSnack.show();
            Location loc = new Location("dummyprovider");
            loc.setLatitude(currentLattitude);
            loc.setLongitude(currentLongitude);
            locList.add(loc);
            if(locUpdate) { // TODO add buttons in notification
                String CHANNEL_ID = "400A";
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("WolfPack@ lat: " + currentLattitude + " and long: " + currentLongitude)
                        .setContentText("LocationFound")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());
                notificationManagerCompat.notify(4, mBuilder.build());
                locUpdate = false;

            } else if (setFence){
                String ID = "fence"+fenceList.size()+"400A";

                fenceList.add(new Geofence.Builder()
                        // Set the request ID of the geofence. This is a string to identify this
                        // geofence.
                        .setRequestId(ID)
                        .setCircularRegion(
                                currentLattitude,
                                currentLongitude,
                                100
                        )
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                Geofence.GEOFENCE_TRANSITION_EXIT)
                        .build());
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }

    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("test tittle")
                        .setMessage("test string")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:

                        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

                        mLocationManager.requestLocationUpdates(provider, 400, 1,  mLocationListener);
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }



}
