package nl.wolfpack.emailwolfpack.wiggle;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import nl.wolfpack.emailwolfpack.R;

public class WiggleFragment extends Fragment {

    private WiggleListenor wiggleListenor;
    private boolean wiggleEnabled;

    private SeekBar minNrOfShakes;


    private final static String CHANNEL_ID = "WIGGLE";
    NotificationManager mNM;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mNM = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wiggle, container, false);

        minNrOfShakes = (SeekBar) view.findViewById(R.id.minShakeCountBar);
        minNrOfShakes.setEnabled(false);
        minNrOfShakes.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                wiggleListenor.setmShakeCount(seekBar.getProgress());
            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.wiggle_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void enableWiggled() {
        wiggleEnabled = true;
        minNrOfShakes.setEnabled(true);
    }

    private void sendNotification () {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Wolfpack")
                .setContentText("Your last known location is...")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Are you OK?"));
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.notify(544, builder.build());
    }

    private void setWiggle() {
        wiggleListenor = new WiggleListenor(getContext());
        wiggleListenor.setOnShakeListener(new WiggleListenor.OnShakeListener() {
            @Override
            public void onShake() {
                sendNotification();
                Log.d(getTag(), "Shaked!");
            }
        });
        enableWiggled();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_wiggle:
                setWiggle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
