package com.example.mzfirstspam;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import java.util.HashSet;
import java.util.List;

//import android.location.Location;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


import static android.content.Context.LOCATION_SERVICE;




public class GeofencingFragment extends Fragment {
    String DEFAULTLAT = "lattitude";
    String DEFAULTLON = "longitude";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Geofence> fenceList = new ArrayList<Geofence>();
    ArrayList<Location> locList = new ArrayList<Location>();
    Location[] arrLoc;
    PendingIntent mGeofencePendingIntent = null;
    private GeofencingClient mGeofencingClient;

    Double currentLattitude;
    Double currentLongitude;
    String provider;
    boolean locUpdate;
    boolean setFence;
    LocationManager mLocationManager;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGeofencingClient = LocationServices.getGeofencingClient(getContext());


        LocationManager mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        provider = mLocationManager.getBestProvider(new Criteria(), false);
        checkLocationPermission();
        if(!mLocationManager.isProviderEnabled(provider)) {
            Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsIntent);
        }
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(getContext(), GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        mGeofencePendingIntent = PendingIntent.getService(getActivity(), 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_geofencing, container, false);

        mRecyclerView = /** (RecyclerView) **/view.findViewById(R.id.FencingList);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        //Log.d("debugMode", "The application stopped after this");
        mRecyclerView.setLayoutManager(mLayoutManager);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String lat = sharedPref.getString(DEFAULTLAT, "0");

        SharedPreferences sharedPref2 = getActivity().getPreferences(Context.MODE_PRIVATE);
        String lon = sharedPref2.getString(DEFAULTLON, "0");

        Log.d("sharedPref","lat: "+lat+" lon"+lon);

        String[] latlist = lat.split("_");
        String[] lonlist = lon.split("_");
        if(latlist.length > 0 && lonlist.length > 0) {
            for (int i = 0; i < latlist.length; i++) {
                if (!latlist[i].equals("") && !lonlist[i].equals("")) {
                    Location x = new Location("dummyprovider");
                    double dlon = Double.parseDouble(lonlist[i]);
                    double dlat = Double.parseDouble(latlist[i]);
                    x.setLongitude(dlon);
                    x.setLatitude(dlat);
                    locList.add(x);
                }
            }
        }

        if (!locList.isEmpty()) {
           arrLoc = locList.toArray(new Location[locList.size()]);
        } else {
            arrLoc = new Location[1];
            Location x = new Location("dummyprovider");
            double dlon = 0;
            double dlat = 0;
            x.setLongitude(dlon);
            x.setLatitude(dlat);
            arrLoc[0] = (x);
        }
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
        checkLocationPermission();
        if(!mLocationManager.isProviderEnabled(provider)){
            Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsIntent);
        }
        mLocationManager.requestSingleUpdate(provider,mLocationListener, null );
    }

    public void setFence(View view){
        //Log.d("fencemgr", "reached");
        setFence = true;
        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        checkLocationPermission();
        if(!mLocationManager.isProviderEnabled(provider)){
            Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsIntent);
        }
        mLocationManager.requestSingleUpdate(provider,mLocationListener, null );
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint("MissingPermission")
        @Override
        public void onLocationChanged(final Location location) {
            currentLattitude = location.getLatitude();
            currentLongitude = location.getLongitude();

//            Snackbar onLocSnack = Snackbar.make(getView(), "currentLattitude: "+currentLongitude, 1000);
//            onLocSnack.show();
            Location loc = new Location("dummyprovider");
            loc.setLatitude(currentLattitude);
            loc.setLongitude(currentLongitude);
//            locList.add(loc);
            if(locUpdate) { // TODO add buttons in notification
                String CHANNEL_ID = "400A";
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Welcome @ wolfpack: ")
                        .setContentText("LocationFound")
                        .setStyle(new NotificationCompat.InboxStyle()
                            .addLine("Lattitude @ "+currentLattitude)
                            .addLine("Longitude @ "+currentLongitude)
                            .addLine("Accuracy @ "+location.getAccuracy())
                            .addLine("Altitude @ "+location.getAltitude())
                            .addLine("Provided @ "+location.getProvider())
                            .addLine("Speed @ "+location.getSpeed())
                            .addLine("Real bearing @"+location.getBearing())
                            .addLine("time @ "+location.getTime()))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());
                notificationManagerCompat.notify(4, mBuilder.build());
                locUpdate = false;

            } else if (setFence){
                String ID = "fence"+fenceList.size()+"400A";
                Location x = new Location("dummy provider");
                x.setLatitude(currentLattitude);
                x.setLongitude(currentLongitude);
                locList.add(x);
                fenceList.add(new Geofence.Builder()
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

                mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Geofences added
                                // ...
                            }
                        })
                        .addOnFailureListener(getActivity(), new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Failed to add geofences
                                // ...
                            }
                        });

                SharedPreferences sharedpref = getActivity().getPreferences(Context.MODE_PRIVATE);
                String lat = sharedpref.getString(DEFAULTLAT, "0");
                String lon = sharedpref.getString(DEFAULTLON, "0");
                Log.d("pref list", "lat"+lat+" lon"+lon);

                SharedPreferences edit = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = edit.edit();
                editor.putString(DEFAULTLON, lat+"_"+String.valueOf(currentLattitude));
                editor.commit();


                SharedPreferences edit2 = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = edit2.edit();
                editor2.putString(DEFAULTLAT, lat+"_"+String.valueOf(currentLongitude));
                editor2.commit();


                arrLoc = locList.toArray(new Location[locList.size()]);
//                mAdapter.notifyDataSetChanged();
                mAdapter = new MyItemRecyclerViewAdapter2(arrLoc, getContext());
                mRecyclerView.setAdapter(mAdapter);
                Log.d("newFence","new geofence added");
                setFence = false;
            }
        }

        private GeofencingRequest getGeofencingRequest() {
            GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
            builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
            builder.addGeofences(fenceList);
            return builder.build();
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
