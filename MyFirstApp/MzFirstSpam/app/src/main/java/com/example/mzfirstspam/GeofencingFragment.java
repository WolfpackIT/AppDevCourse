package com.example.mzfirstspam;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
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

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GeofencingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GeofencingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeofencingFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Double currentLattitude;
    Double currentLongitude;
    List<Location> fencingList = new ArrayList<>();
    LocationManager mLocationManager;
    String provider;
    boolean locationpermitted;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationManager mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        provider = mLocationManager.getBestProvider(new Criteria(), false);
        checkLocationPermission();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_geofencing, container, false);

        // Replace 'android.R.id.list' with the 'id' of your RecyclerView
        mRecyclerView = /** (RecyclerView) **/view.findViewById(R.id.FencingList);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        Log.d("debugMode", "The application stopped after this");
        mRecyclerView.setLayoutManager(mLayoutManager);


        String[] fenceArray = new String[fencingList.size()];
        fencingList.toArray(fenceArray); // fill the array

        mAdapter = new MyItemRecyclerViewAdapter2(fenceArray, getContext());
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
        Log.d("locMngr", "reached");


//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//
//
//
//            return;
//        }
        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if(checkLocationPermission() == false){
            Log.d("LocPer", "no location acces");
            Snackbar bar = Snackbar.make(getView(), "please give us location access or this function will not be available", 1000);
            return;
        }

        mLocationManager.requestLocationUpdates(provider, 0, 0, mLocationListener);
        Location loc = mLocationManager.getLastKnownLocation(provider);

        String CHANNEL_ID = "400A";

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("WolfPack@ lat: "+currentLattitude+" and long: "+ currentLongitude)
                .setContentText("LocationFound")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat =  NotificationManagerCompat.from(getActivity());
        notificationManagerCompat.notify(4,mBuilder.build());



    }

    public void setFence(View view){
        Log.d("fencemgr", "reached");
        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if(checkLocationPermission() == false){
            Log.d("LocPer", "no location acces");
            Snackbar bar = Snackbar.make(getView(), "please give us location access or this function will not be available", 1000);
            return;
        }

        mLocationManager.requestLocationUpdates(provider, 0, 0, mLocationListener);
        Location loc = mLocationManager.getLastKnownLocation(provider);
//        loc.setLatitude(currentLattitude);
//        loc.setLongitude(currentLongitude);
//        fencingList.add(loc);
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            currentLattitude = location.getLatitude();
            currentLongitude = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.d("Latitude","disable");
        }

        @Override
        public void onProviderEnabled(String s) {
            Log.d("Latitude","enable");
        }

        @Override
        public void onProviderDisabled(String s) {
            Log.d("Latitude","status");
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

                        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                        locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
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
