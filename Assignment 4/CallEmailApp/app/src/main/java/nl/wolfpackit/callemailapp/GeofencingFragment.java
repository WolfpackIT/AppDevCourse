package nl.wolfpackit.callemailapp;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class GeofencingFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private FusedLocationProviderClient mFusedLocationClient;
    private RecyclerView mRecyclerView;
    private List<String> locationList = new ArrayList<>();
    private static GeoAdapter geoAdapter;
    private final int PERM_REQ_COARSE_LOCATION = 911;
    private final String CHANNEL_ID = "GEOFENCE_NOTIFICATION_CHANNEL";

    //TODO: Create something that stores the locations and the shared preferences.

    public GeofencingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setFence() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERM_REQ_COARSE_LOCATION);
        }
        else{
            mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        String locationText = "Longitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude();
                        createNotification(locationText);
                        locationList.add(locationText);
                        geoAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    private void sendLocationNotification(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERM_REQ_COARSE_LOCATION);
        }
        else{
            mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
                        CharSequence channelName = getString(R.string.channel_name);
                        String channelDescription = getString(R.string.channel_description);
                        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, channelName, channelImportance);
                        mChannel.setDescription(channelDescription);
                        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(
                            Context.NOTIFICATION_SERVICE);
                        notificationManager.createNotificationChannel(mChannel);

                        String notificationText = "Latitude: " + location.getLongitude() + " Latitude: " + location.getLongitude() + "\n"
                                + "Accuracy: " + location.getAccuracy();

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("Current Location")
                                .setContentText("Expand to see details")
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText))
                                .setPriority(NotificationCompat.PRIORITY_HIGH);

                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getContext());
                        notificationManagerCompat.notify(678, mBuilder.build());
                    }
                    //TODO: Solve bug here (location is always null)
                    else {
                        Toast.makeText(getContext(), R.string.retrieving_location_issue, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void createNotification(String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setContentText("Your last location was ")
                .setContentTitle("CallEmailApp")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text));
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.notify(666, builder.build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERM_REQ_COARSE_LOCATION) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setFence();
            }
            else {
                Toast.makeText(getContext(), R.string.location_permission, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.setfence:
                setFence();
                return true;
            case R.id.getlocation:
                sendLocationNotification();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_geofencing, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.geofencing_recycler_view);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), 1);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        GeoAdapter geoAdapter = new GeoAdapter(locationList);
        mRecyclerView.setAdapter(geoAdapter);
        return view;
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
