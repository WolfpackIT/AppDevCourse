package com.example.mzfirstspam;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mzfirstspam.dummy.DummyContent;
import com.example.mzfirstspam.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<String> myShouts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Replace 'android.R.id.list' with the 'id' of your RecyclerView
        mRecyclerView = /** (RecyclerView) **/ view.findViewById(R.id.list3);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        Log.d("debugMode", "The application stopped after this");
        mRecyclerView.setLayoutManager(mLayoutManager);

        String[] shouts = new String[myShouts.size()];
        myShouts.toArray(shouts); // fill the array

        mAdapter = new MyItemRecyclerViewAdapter(shouts);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       myShouts.add("Lekker Eddy!");
        myShouts.add("Fooood");
        myShouts.add("www.ishetaltijdvoorbier.nl");

    }


}
