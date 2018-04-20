package com.example.wolfpackapp.DeclarationsFragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wolfpackapp.DeclarationDatabase.DecDB;
import com.example.wolfpackapp.R;

public class KilometerDeclarationFragment extends Fragment {
    String TAGB = "help";

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
        if (MyDeclaration.car != null) {
            Log.d(TAGB, MyDeclaration.car.getNaar());
            Log.d(TAGB, MyDeclaration.car.getVan());
            Log.d(TAGB, Double.toString(MyDeclaration.car.getKilometers()));
            ed = view.findViewById(R.id.CarDecEdStart);
            ed.setText((CharSequence) MyDeclaration.car.getVan());
            ed2 = view.findViewById(R.id.CarDecEdEnd);
            ed2.setText((CharSequence) MyDeclaration.car.getNaar());
            ed3 = view.findViewById(R.id.CarDecTextDate);
            ed3.setText((CharSequence) MyDeclaration.info.getTimestamp());
            ed4 = view.findViewById(R.id.CarDecEdDistance);
            ed4.setText((CharSequence) Double.toString(MyDeclaration.car.getKilometers()));
            ed5 = view.findViewById(R.id.CarDecTotalprice);
            ed5.setText((CharSequence) Double.toString(MyDeclaration.info.getCash()));
        }
    }

    @Override
    public void onDestroy() {
        accept();
        super.onDestroy();
    }

    public void discard(){

    }

    public void accept(){
        new updateDeclarations().execute("");
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
            //TODO MyDeclaration.info.setCash(double)
            db.DecDAO().updateDec(MyDeclaration.info);

            db.DecDAO().updateDec(MyDeclaration.car);

            return (long) 0;
        }

        @Override
        protected void onPostExecute(Long aLong) {

        }
    }
}
