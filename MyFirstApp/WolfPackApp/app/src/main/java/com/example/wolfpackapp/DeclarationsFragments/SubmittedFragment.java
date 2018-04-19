package com.example.wolfpackapp.DeclarationsFragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wolfpackapp.Database.EmpDB;
import com.example.wolfpackapp.DeclarationDatabase.DecDB;
import com.example.wolfpackapp.DeclarationDatabase.Declaration;
import com.example.wolfpackapp.R;
import com.example.wolfpackapp.decleratonRecyclerViewAdapter.DeclarationAdapter;

import java.util.ArrayList;
import java.util.List;


public class SubmittedFragment extends Fragment {

    String NAME = "voornaam";
    String EMAIL = "email";
    String ADMIN = "admin";
    DecDB db;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout mySwipeRefreshLayout;
    List<Declaration> vis;
    Boolean swipeRefresh = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submitted_declarations, container, false);
        db = Room.databaseBuilder(getContext(),
                DecDB.class, "Declaration").build();
        new getDeclarations().execute("");

        mRecyclerView = /** (RecyclerView) **/ view.findViewById(R.id.decList); //TODO add declist to fragment xml
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        Log.d("debugMode", "The application stopped after this");
        mRecyclerView.setLayoutManager(mLayoutManager);
        TextView empty = view.findViewById(R.id.EmptyDeclarations);
        mRecyclerView.setVisibility(view.GONE);
        empty.setText("fetching Declarations from server");
        empty.setVisibility(view.VISIBLE);
        mySwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        TextView empty = getView().findViewById(R.id.EmptyDeclarations);
                        empty.setText("fetching Declarations from server");
                        empty.setVisibility(getView().VISIBLE);
                        Log.i("declareRefresh", "onRefresh called from SwipeRefreshLayout");
                        mRecyclerView.setVisibility(getView().GONE);

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        swipeRefresh = true;
                        new getDeclarations().execute("");
                    }
                }
        );


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private class getDeclarations extends AsyncTask<String, Integer, Long> {
        String TAGB = "getDeclarations";

        @Override
        protected Long doInBackground(String... strings) {
            SharedPreferences sharedpref = getActivity().getPreferences(Context.MODE_PRIVATE);
            String email = sharedpref.getString(EMAIL, "email");
            String name = sharedpref.getString(NAME, "username");
            Boolean admin = sharedpref.getBoolean(ADMIN, false);
            //if is admin
            if ( admin ){
             vis = db.DecDAO().getFullList(false);
            } else {
                vis = db.DecDAO().getCheckedList(email, false);
            }

            return (long) 0;
        }

        @Override
        protected void onPostExecute(Long aLong) {

            if(swipeRefresh){

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        TextView empty = getView().findViewById(R.id.EmptyDeclarations);
                        if ( vis.isEmpty()){
                            mRecyclerView.setVisibility(getView().GONE);
                            empty.setText("No declarations were found");
                            empty.setVisibility(getView().VISIBLE);

                        } else {
                            empty.setVisibility(getView().GONE);
                            mRecyclerView.setVisibility(getView().VISIBLE);
                        }
                        mAdapter = new DeclarationAdapter(vis, getContext());
                        mRecyclerView.setAdapter(mAdapter);
                        mySwipeRefreshLayout.setRefreshing(false);
                        swipeRefresh = false;
                    }
                }, 1000);


            } else {
                TextView empty = getView().findViewById(R.id.EmptyDeclarations);
                if ( vis.isEmpty()){
                    mRecyclerView.setVisibility(getView().GONE);
                    empty.setText("No declarations were found");
                    empty.setVisibility(getView().VISIBLE);

                } else {
                    empty.setVisibility(getView().GONE);
                    mRecyclerView.setVisibility(getView().VISIBLE);
                }
                mAdapter = new DeclarationAdapter(vis, getContext());
                mRecyclerView.setAdapter(mAdapter);
            }
        }
    }

}
