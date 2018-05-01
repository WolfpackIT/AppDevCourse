package com.example.wolfpackapp.DeclarationsFragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.wolfpackapp.DeclarationActivity;
import com.example.wolfpackapp.DeclarationDatabase.DecDB;
import com.example.wolfpackapp.DeclarationDatabase.Declaration;
import com.example.wolfpackapp.DeclarationDatabase.DeclarationCar;
import com.example.wolfpackapp.DeclarationDatabase.DeclarationOther;
import com.example.wolfpackapp.R;
import com.example.wolfpackapp.decleratonRecyclerViewAdapter.DeclarationAdapter;

import java.util.List;

import static android.view.View.VISIBLE;


public class AddDeclarationFragment extends AppCompatActivity {

    String NAME = "voornaam";
    String EMAIL = "email";
    String ADMIN = "admin";
    DecDB db;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout mySwipeRefreshLayout;
    static Declaration vis;
    static DeclarationCar car;
    static DeclarationOther other;
    List<DeclarationCar> c;
    List<DeclarationOther> o;
    static Boolean swipeRefresh = false;
    EditText infTitle;
    long value;
    long cid;
    boolean carb;
    boolean otherb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_declaration);
        Intent intent = getIntent();
        value = intent.getLongExtra("info", -1);
        cid = intent.getLongExtra("cid",-1);
        carb = intent.getBooleanExtra("car",true);
        otherb = intent.getBooleanExtra("general", true);
        db = Room.databaseBuilder(this,
                DecDB.class, "Declaration").fallbackToDestructiveMigration().build();
        new getDeclarations().execute("");

//        new SubmittedFragment.getDeclarations().execute("");


        mRecyclerView = /** (RecyclerView) **/ findViewById(R.id.FullDecList); //TODO add declist to fragment xml
        mLayoutManager = new LinearLayoutManager(this);
        Log.d("debugMode", "The application stopped after this");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mySwipeRefreshLayout = findViewById(R.id.SwipeRefresh2);
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
        infTitle = (EditText) findViewById(R.id.InfoTitleED);
        String test = infTitle.getText().toString();



        Button decBut = (Button) findViewById(R.id.SendInDec);
        decBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update database or add database;
                if (carb) {
                    Log.d("store", "store car shit");
                    new storeDBcar().execute("");
                } else if (otherb){
                    Log.d("store", "store other shit");
                    new storeDBother().execute("");
                }
                Log.d("store", "missed both");
            }
        });
        ImageButton ib = (ImageButton) findViewById(R.id.imageButton9);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newDec = new Intent(getApplicationContext(), MyDeclaration.class);
                newDec.putExtra("id", -5);
                newDec.putExtra("new", (long) value);
                startActivity(newDec);
            }
        });
        vis = new Declaration();
//        vis.setTitle(test);
//        Log.d("update title", test);
//        vis.setCash(car.getKilometers()*0.19);
        vis.setChecked(false);
    }

    private class storeDBcar extends AsyncTask<String, Integer, Long> {
        String TAGB = "getDeclarations";

        @Override
        protected Long doInBackground(String... strings) {
            SharedPreferences sharedpref = getSharedPreferences(EMAIL,Context.MODE_PRIVATE);
            String email = sharedpref.getString(EMAIL, "email");
            Log.d("mailtest addDEC act", email);
//            String name = sharedpref.getString(NAME, "username");
//            Boolean admin = sharedpref.getBoolean(ADMIN, false);

            String test = infTitle.getText().toString();
            vis.setTitle(test);
            if (db.DecDAO().getMaxID() >= vis.getUid()){
                db.DecDAO().updateDec(vis);
            } else {
                db.DecDAO().insert(vis);
            }
            return (long) 0;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            Intent intent = new Intent(getApplicationContext(), DeclarationActivity.class); //TODO where does this lead? what is the activity after updating the database
            intent.putExtra("id", -1); //Need extra value?
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private class storeDBother extends AsyncTask<String, Integer, Long> {
        String TAGB = "getDeclarations";

        @Override
        protected Long doInBackground(String... strings) {
            SharedPreferences sharedpref = getSharedPreferences(EMAIL,Context.MODE_PRIVATE);
            String email = sharedpref.getString(EMAIL, "email");
            Log.d("mailtest addDEC act", email);
//            String name = sharedpref.getString(NAME, "username");
//            Boolean admin = sharedpref.getBoolean(ADMIN, false);

            String test = infTitle.getText().toString();
            vis.setTitle(test);
            if (db.DecDAO().getMaxID() >= vis.getUid()){
                db.DecDAO().updateDec(vis);
            } else {
                db.DecDAO().insert(vis);
            }
            return (long) 0;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            Intent intent = new Intent(getApplicationContext(), DeclarationActivity.class); //TODO where does this lead? what is the activity after updating the database
            intent.putExtra("id", -1); //Need extra value?
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private class getDeclarations extends AsyncTask<String, Integer, Long> {
        String TAGB = "getDeclarations";

        @Override
        protected Long doInBackground(String... strings) {
            Log.d(TAGB, "show me pls;");
            if( db.DecDAO().loadSingleByIds(value) == null ){
                vis = new Declaration();
                vis.setUid(value);
            } else {
                vis = db.DecDAO().loadSingleByIds(value);
            }
            if (carb) {
                car = db.DecDAO().loadSingleCar(cid);
                car.setUid(value);
            } else if( otherb) {
                other = db.DecDAO().loadSingleOther(cid);
                other.setUid(value);
            }
            o = db.DecDAO().loadAllByOtherIds(value);
            c = db.DecDAO().loadAllByCarIds(value);
            infTitle.setText(vis.getTitle());
            return (long) 0;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            Log.d(TAGB, "show me pls;");
            mRecyclerView.setVisibility(VISIBLE);
            mAdapter = new FullDeclarationAdapter(vis, o, c, getApplicationContext());
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
