package com.example.mzfirstspam;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


public class WiggleFragment extends Fragment {
    static View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!WiggleService.getRunning()) {
            Log.d("oncreate wiggle", "top one");
            Intent intent = new Intent(getContext(), WiggleService.class);
            getActivity().startService(intent);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_wiggle, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("onview wiggle", "reached");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                TextView tv = (TextView) getView().findViewById(R.id.textView);
                if(!WiggleService.getRunning()) {
                    tv.setText("not running");
                } else {
                    tv.setText("running");
                }
            }
        }, 1000);
    }
}
