package nl.wolfpack.emailwolfpack.geofence;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nl.wolfpack.emailwolfpack.MainActivity;
import nl.wolfpack.emailwolfpack.R;
import nl.wolfpack.emailwolfpack.TinyDB;

public class GeoFenceFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private FusedLocationProviderClient mFusedLocationClient;

    private ArrayList<String> locations;
    private RecyclerView recyclerView;
    private LocationsAdapter adapter;

    private final static int PERMISSIONS_REQUEST_COARSE_LOCATION = 211;

    private final static String CHANNEL_ID = "geo_fence";
    private NotificationManager mNM;

    private TinyDB tinydb;




    public GeoFenceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mNM = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        locations = new ArrayList<String>();

        try {
            tinydb = new TinyDB(getContext());

            ArrayList<String> dbLocations = tinydb.getListString("FENCES_LIST");
            if(dbLocations.size() > 0) {
                locations.addAll(dbLocations);
            }
        } catch (Exception e){
            Toast.makeText(getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_geo_fence, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewLocation);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), 1);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new LocationsAdapter(locations);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.geo_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void sendNotification(String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Wolfpack")
                .setContentText(getString(R.string.your_last_location_is))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text));
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.notify(544, builder.build());
    }

    private void getLastKnownLocation() {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_COARSE_LOCATION);
        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        String text = "Longitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude();
                        sendNotification(text);
                        locations.add(text);
                        adapter.notifyDataSetChanged();

                        tinydb.putListString("FENCES_LIST", locations);
                    }
                }
            });

            mFusedLocationClient.getLastLocation().addOnFailureListener(getActivity(), new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_COARSE_LOCATION: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastKnownLocation();
                } else {
                    Toast.makeText(getContext().getApplicationContext(), "Mijn maneer/vrouw we need your location...", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default:
                break;
        }
    }

    private void showGeoLocationNotification() {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_COARSE_LOCATION);
        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            // Create the NotificationChannel
                            CharSequence name = getString(R.string.channel_name);
                            String description = getString(R.string.channel_description);
                            int importance = NotificationManager.IMPORTANCE_DEFAULT;
                            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                            mChannel.setDescription(description);
                            // Register the channel with the system; you can't change the importance
                            // or other notification behaviors after this
                            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(
                                    Context.NOTIFICATION_SERVICE);
                            notificationManager.createNotificationChannel(mChannel);
                        }

                        String text = "Latitude: " + location.getLongitude() + " Latitude: " + location.getLongitude() + "\n"
                                + "Accuracy: " + location.getAccuracy()  + " Altitude: " +  location.getAltitude();

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("Current Location")
                                .setContentText("Expand to see details")
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                                .setPriority(NotificationCompat.PRIORITY_HIGH);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
                        notificationManager.notify(222, mBuilder.build());
                    } else {
                        Toast.makeText(getContext(), "Something went wrong with getting location...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_fence:
                getLastKnownLocation();
                return true;
            case R.id.set_geo:
                showGeoLocationNotification();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
