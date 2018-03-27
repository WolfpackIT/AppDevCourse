package nl.wolfpackit.appdev12.wiggle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import nl.wolfpackit.appdev12.MainActivity;
import nl.wolfpackit.appdev12.R;
import nl.wolfpackit.appdev12.shout.ShoutListAdapter;

public class WiggleFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ShoutListAdapter adapter;
    private TextView runningText;
    private SharedPreferences.OnSharedPreferenceChangeListener spChanged;

    private float shakeThreshold = 3;
    private int timeRange = 0;
    private int requiredShakes = 4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wiggle, container, false);
    }


    public void onAttach(Context context) {
        super.onAttach(context);
    }
    public void onViewCreated(View view, Bundle bundle){
        runningText = (TextView) view.findViewById(R.id.wiggleRunningText);
        setRunning(MainActivity.mPrefs.getBoolean("wiggleRunning", false));

        spChanged = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if(key=="wiggleRunning")
                    setRunning(MainActivity.mPrefs.getBoolean("wiggleRunning", false));
            }
        };
        MainActivity.mPrefs.registerOnSharedPreferenceChangeListener(spChanged);



        timeRange =  MainActivity.mPrefs.getInt("timeRange", 2000);
        requiredShakes = MainActivity.mPrefs.getInt("shakesRequired", 4);
        shakeThreshold = MainActivity.mPrefs.getFloat("shakeThreshold", 3);

        SeekBar intensity = (SeekBar) getActivity().findViewById(R.id.wiggleThresholdSlider);
        intensity.setMax(60);
        intensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                shakeThreshold = progress/10f;
                updateWiggleParameters();
            }
            public void onStartTrackingTouch(SeekBar seekBar){}
            public void onStopTrackingTouch(SeekBar seekBar){}
        });
        intensity.setProgress((int)(shakeThreshold*10));


        //setup the time input
        final Spinner timeSpinner = (Spinner) getActivity().findViewById(R.id.wiggleTimeSpinner);
        String[] times = new String[]{"500ms", "1000ms", "1500ms", "2000ms", "2500ms", "3000ms"};
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, times);
        timeSpinner.setAdapter(timeAdapter);
        timeSpinner.setSelection(java.util.Arrays.asList(times).indexOf(timeRange+"ms"));
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                timeRange = Integer.parseInt(timeSpinner.getSelectedItem().toString().replaceAll("[\\D]", ""));
                updateWiggleParameters();
            }
            public void onNothingSelected(AdapterView<?> parentView){}
        });

        //setup the shakes input
        final Spinner countSpinner = (Spinner) getActivity().findViewById(R.id.wiggleCountSpinner);
        String[] counts = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> countAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, counts);
        countSpinner.setAdapter(countAdapter);
        countSpinner.setSelection(java.util.Arrays.asList(counts).indexOf(requiredShakes+""));
        countSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                requiredShakes = Integer.parseInt(countSpinner.getSelectedItem().toString());
                updateWiggleParameters();
            }
            public void onNothingSelected(AdapterView<?> parentView){}
        });
    }

    protected void updateWiggleParameters(){
        SharedPreferences.Editor editor = MainActivity.mPrefs.edit();
        editor.clear();
        editor.putInt("timeRange", timeRange);
        editor.putInt("requiredShakes", requiredShakes);
        editor.putFloat("shakeThreshold", shakeThreshold);
        editor.commit();
        System.out.println("changed"+timeRange);



        Intent mIntent = new Intent();
        mIntent.setAction("nl.wolfpack.UPDATE_WIGGLE_DATA");
        Bundle extras = mIntent.getExtras();
        mIntent.putExtra("shakeThreshold", shakeThreshold);
        mIntent.putExtra("requiredShakes", requiredShakes);
        mIntent.putExtra("timeRange", timeRange);
        getActivity().sendBroadcast(mIntent);

    }

    public void setRunning(boolean running){
        Intent wiggleTracker = new Intent(getActivity(), WiggleService.class);
        if(running){
            runningText.setText(getActivity().getText(R.string.wiggleIsRunningText));
            if (Build.VERSION.SDK_INT >= 26) {
                getActivity().startForegroundService(wiggleTracker);
            }else{
                Toast.makeText(this.getActivity(), getString(R.string.wiggleNotSupportedText), Toast.LENGTH_SHORT).show();
            }
        }else{
            runningText.setText(getActivity().getText(R.string.wiggleIsNotRunningText));

            if (Build.VERSION.SDK_INT >= 26) {
                getActivity().stopService(wiggleTracker);
            }else{
                Toast.makeText(this.getActivity(), getString(R.string.wiggleNotSupportedText), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

