package com.example.wolfpackapp.DeclarationsFragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wolfpackapp.DeclarationDatabase.DecDB;
import com.example.wolfpackapp.R;
import com.example.wolfpackapp.StartUpFragments.LoginFragment;

public class KilometerDeclarationFragment extends Fragment {
    String TAGB = "help";
    String NAME = "voornaam";
    String EMAIL = "email";
    String ADMIN = "admin";

    TextView ed5;
    EditText ed4;
    EditText ed3;
    EditText ed2;
    EditText ed;

    DecDB db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kilometer_declaration, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = Room.databaseBuilder(getContext(),
                DecDB.class, "Declaration").build();
        ed = view.findViewById(R.id.CarDecEdStart);
        ed2 = view.findViewById(R.id.CarDecEdEnd);
        ed3 = view.findViewById(R.id.CarDecTextDate);
        ed4 = view.findViewById(R.id.CarDecEdDistance);
        ed5 = view.findViewById(R.id.CarDecTotalprice);
        if (MyDeclaration.newDecCar == false) {
//            Log.d(TAGB, MyDeclaration.car.getNaar());
//            Log.d(TAGB, MyDeclaration.car.getVan());
//            Log.d(TAGB, Double.toString(MyDeclaration.car.getKilometers()));
            ed.setText((CharSequence) MyDeclaration.car.getVan());
            ed2.setText((CharSequence) MyDeclaration.car.getNaar());
            ed3.setText((CharSequence) MyDeclaration.info.getTimestamp());
            ed4.setText((CharSequence) Double.toString(MyDeclaration.car.getKilometers()));
            ed5.setText((CharSequence) Double.toString(MyDeclaration.info.getCash()));
        } else {

        }
        ImageButton dis = view.findViewById(R.id.imageButton5);
        dis.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                discard();
            }
        });
        ImageButton ais = view.findViewById(R.id.imageButton6);
        ais.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                accept();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void discard(){
        getActivity().onBackPressed();
    }

    public void accept(){
        if( ed.getText().toString().matches("") || ed2.getText().toString().matches("") || ed3.getText().toString().matches("") || ed4.getText().toString().matches("")){
            Snackbar sn = Snackbar.make(getView(), "One or more fields have not been properly filled in yet", 1000);
            sn.show();
        } else if( MyDeclaration.newDecCar == false ) {
            Log.d("kilo", "update old");
            new updateDeclarations().execute("");
        } else {
            Log.d("kilo", "create new");
            new createDeclaration().execute("");
        }
    }

    private class createDeclaration extends AsyncTask<String, Integer, Long> {
        String TAGB = "updateDeclarations database";

        @Override
        protected Long doInBackground(String... value) {
            MyDeclaration.car.setVan( (ed.getText()).toString());
            MyDeclaration.car.setNaar( (ed2.getText()).toString());
            MyDeclaration.car.setKilometers(  Double.parseDouble(ed4.getText().toString()));
            long sUID = (long) MyDeclaration.car.getUid(); //TODO get latest and add one to setuid;
//            MyDeclaration.info.setUid(sUID); //TODO get latest and add one to setuid;
            SharedPreferences sharedpref = getActivity().getSharedPreferences(EMAIL,Context.MODE_PRIVATE);
            String email = sharedpref.getString(EMAIL, "email");
            Log.d("mailtest kilo", email);
            String name = sharedpref.getString(NAME, "username");
            Boolean admin = sharedpref.getBoolean(ADMIN, false);

//            MyDeclaration.info.setCash(Double.parseDouble(ed4.getText().toString()));
            db.DecDAO().insertCar(MyDeclaration.car);
//            db.DecDAO().insert(MyDeclaration.info);
//            getActivity().setContentView(R.layout.activity_main);
//            AddDeclarationFragment firstFragment = new AddDeclarationFragment();
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .add(R.id.mainA, firstFragment).commit();
            return (long) 0;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            Intent intent = new Intent(getContext(), AddDeclarationFragment.class);
            intent.putExtra("info", MyDeclaration.car.getUid());
            intent.putExtra("cid", MyDeclaration.car.getCID());
            intent.putExtra("car", true);
            intent.putExtra("general", false);
            startActivity(intent);
        }
    }

    private class updateDeclarations extends AsyncTask<String, Integer, Long> {
        String TAGB = "updateDeclarations database";

        @Override
        protected Long doInBackground(String... value) {
            if(!ed.getText().equals(MyDeclaration.car.getVan())){
                MyDeclaration.car.setVan( (ed.getText()).toString());
            }
            if(!ed2.getText().equals(MyDeclaration.car.getNaar())){
                MyDeclaration.car.setNaar( (ed2.getText()).toString());
            }
            if (!ed3.getText().equals(MyDeclaration.info.getTimestamp())){
                MyDeclaration.info.setTimestamp( (ed3.getText()).toString());
            }
            if(!ed4.getText().equals(MyDeclaration.car.getKilometers())){
                MyDeclaration.car.setKilometers(  Double.parseDouble(ed4.getText().toString()));
            }
            //TODO make sure that a new oid is made and set
            //TODO MyDeclaration.info.setCash(double)
            db.DecDAO().updateDec(MyDeclaration.info);

            db.DecDAO().updateDec(MyDeclaration.car);

            return (long) 0;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            Intent intent = new Intent(getContext(), AddDeclarationFragment.class);
            intent.putExtra("info", MyDeclaration.info.getUid());
            intent.putExtra("cid", MyDeclaration.car.getCID());
            intent.putExtra("car", true);
            intent.putExtra("general", false);
            startActivity(intent);
        }
    }
}
