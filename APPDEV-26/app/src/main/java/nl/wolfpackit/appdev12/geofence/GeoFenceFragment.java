package nl.wolfpackit.appdev12.geofence;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import nl.wolfpackit.appdev12.MainActivity;
import nl.wolfpackit.appdev12.R;

public class GeoFenceFragment extends android.support.v4.app.Fragment{
    public static int REQUEST_LOCATION = 0;
    public static GeoFenceFragment instance;

    private FusedLocationProviderClient mFusedLocationClient;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    FenceListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_geo_fence, container, false);
    }


//    public void onAttach(Context context) {
//        super.onAttach(context);
//    }
    public void onViewCreated(View view, Bundle bundle){
        instance = this;
        //fences setup
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fencesContainer);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        String[] fenceData = MainActivity.mPrefs.getString("fences", "").split(",");
        List<Fence> fences = new ArrayList<Fence>();
        if(fenceData[0].length()>0)
            for(int i=0; i<fenceData.length; i++){
                fences.add(new Fence(fenceData[i]));
            }

        adapter = new FenceListAdapter(fences, this.getActivity().getApplication());
        mRecyclerView.setAdapter(adapter);

        //buttons setup
        mFusedLocationClient = new FusedLocationProviderClient(MainActivity.instance);
        ((Button)view.findViewById(R.id.setFenceButton)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                createFlag();
            }else{
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
            }
            }
        });
        ((Button)view.findViewById(R.id.getLocationButton)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(MainActivity.instance, new OnSuccessListener<Location>() {
                        final Activity a = getActivity();
                        public void onSuccess(final Location location) {
                            if(location!=null){
                                DecimalFormat decimalFormat = new DecimalFormat(".#####");
                                final AlertDialog dialog = new AlertDialog.Builder(a)
                                        .setTitle(MainActivity.getStringByID(R.string.getLocationTitle))
                                        .setMessage(MainActivity.getStringByID(R.string.getLocationText)
                                                .replace("[long]", decimalFormat.format(location.getLongitude()))
                                                .replace("[lat]", decimalFormat.format(location.getLatitude())))
                                        .setPositiveButton(R.string.addFenceConfirm, null)
                                        .show();
                            }
                        }
                    });
                }else{
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_LOCATION);
                }
            }
        });


        Intent backgroundTracker = new Intent(getActivity(), LocationTrackerService.class);
        if (Build.VERSION.SDK_INT >= 26) {
            getActivity().startForegroundService(backgroundTracker);
        }
    }

    public void createFlag(){
        if (ActivityCompat.checkSelfPermission(MainActivity.instance, Manifest.permission.ACCESS_FINE_LOCATION)
                ==PackageManager.PERMISSION_GRANTED){
            final Activity a = getActivity();
            mFusedLocationClient.getLastLocation().addOnSuccessListener(MainActivity.instance, new OnSuccessListener<Location>() {
                public void onSuccess(final Location location) {
                    final EditText name = new EditText(a);
                    name.setHint(MainActivity.getStringByID(R.string.fenceNamePlaceholder));

                    if (location != null){ // Got last known location. In some rare situations this can be null.
                        //create a listener for when clicking ok
                        final DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String nameText = name.getText().toString();
                                if (nameText.length() > 0)
                                    adapter.insertFence(new Fence(nameText, location.getLatitude(), location.getLongitude()), MainActivity.mPrefs);
                            }
                        };
                        //create a dialog to ask for the name
                        final AlertDialog dialog = new AlertDialog.Builder(a)
                                .setTitle(R.string.addFenceTitle)
                                .setMessage(R.string.addFenceText)
                                .setView(name)
                                .setPositiveButton(R.string.addFenceConfirm,clickListener)
                                .setNegativeButton(R.string.addFenceCancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                    }
                                })
                                .show();
                        //automatically click ok, when an enter was sent
                        name.setOnKeyListener(new View.OnKeyListener(){
                            public boolean onKey(View v, int keyCode, KeyEvent event) {
                                if(keyCode == 66 && event.getAction()==0) {
                                    clickListener.onClick(dialog, 0); //whichButton doesn't really matter
                                    dialog.cancel();
                                }
                                return false;
                            }
                        });
                    }else{
                        //no location found error
                    }
                }
            });
        }else{
            //show some permission error message
        }
    }
}
