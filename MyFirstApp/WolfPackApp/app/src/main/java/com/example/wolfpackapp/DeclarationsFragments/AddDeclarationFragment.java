package com.example.wolfpackapp.DeclarationsFragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.wolfpackapp.DeclarationDatabase.DecDB;
import com.example.wolfpackapp.DeclarationDatabase.Declaration;
import com.example.wolfpackapp.R;
import com.example.wolfpackapp.decleratonRecyclerViewAdapter.DeclarationAdapter;

import java.util.List;


public class AddDeclarationFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_add_declaration, container, false);
        db = Room.databaseBuilder(getContext(),
                DecDB.class, "Declaration").fallbackToDestructiveMigration().build();

//        new SubmittedFragment.getDeclarations().execute("");

        mRecyclerView = /** (RecyclerView) **/ view.findViewById(R.id.decList); //TODO add declist to fragment xml
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        Log.d("debugMode", "The application stopped after this");
        mRecyclerView.setLayoutManager(mLayoutManager);
        TextView empty = view.findViewById(R.id.EmptyDeclarations);
        mRecyclerView.setVisibility(view.GONE);
        empty.setText("fetching Declarations from server");
        empty.setVisibility(view.VISIBLE);
        mySwipeRefreshLayout = view.findViewById(R.id.SwipeRefresh2);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //TODO implement this.
//                        TextView empty = getView().findViewById(R.id.EmptyDeclarations);
//                        empty.setText("fetching Declarations from server");
//                        empty.setVisibility(getView().VISIBLE);
//                        Log.i("declareRefresh", "onRefresh called from SwipeRefreshLayout");
//                        mRecyclerView.setVisibility(getView().GONE);
//
//                        // This method performs the actual data-refresh operation.
//                        // The method calls setRefreshing(false) when it's finished.
//                        swipeRefresh = true;
////                        new SubmittedFragment.getDeclarations().execute("");
                    }
                }
        );
        mAdapter = new DeclarationAdapter(, getContext());
        Button decBut = (Button) view.findViewById(R.id.SendInDec);
        decBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update database or add database;
                Intent intent = new Intent(getContext(), MyDeclaration.class); //TODO where does this lead? what is the activity after updating the database
                intent.putExtra("id", -1); //Need extra value?
                startActivity(intent);
            }
        });
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

            return (long) 0;
        }

        @Override
        protected void onPostExecute(Long aLong) {

        }
    }
}
