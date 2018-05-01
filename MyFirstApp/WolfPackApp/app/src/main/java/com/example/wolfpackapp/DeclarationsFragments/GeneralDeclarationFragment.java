package com.example.wolfpackapp.DeclarationsFragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.wolfpackapp.DeclarationDatabase.DecDB;
import com.example.wolfpackapp.R;


public class GeneralDeclarationFragment extends Fragment {
    String TAGB = "help";
    String NAME = "voornaam";
    String EMAIL = "email";
    String ADMIN = "admin";

    TextView ed5;
    EditText ed4;
    EditText ed3;
    EditText ed2;
    EditText ed;
    Switch sed;

    DecDB db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general_declaration, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = Room.databaseBuilder(getContext(),
                DecDB.class, "Declaration").build();
        ed = view.findViewById(R.id.OtherDecDesED);
        ed2 = view.findViewById(R.id.OtherDecDateED);
        ed3 = view.findViewById(R.id.OtherDecBTWpriceED);
        ed4 = view.findViewById(R.id.OtherDecBtwCostED);
        ed5 = view.findViewById(R.id.OtherDecTotalprice);
        sed = view.findViewById(R.id.switch1);


        if (MyDeclaration.newDec != true && MyDeclaration.newDecCar == true) {

            ed.setText((CharSequence) MyDeclaration.general.getDes());

            ed2.setText((CharSequence) MyDeclaration.info.getTimestamp());

            ed3.setText((CharSequence) Double.toString(MyDeclaration.general.getCost()));

            ed4.setText((CharSequence) Double.toString(MyDeclaration.general.getBtwCost()));

            ed5.setText((CharSequence) Double.toString(MyDeclaration.general.getTotalCost()));

        }else {

        }
        ImageButton dis = view.findViewById(R.id.imageButton4);
        dis.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                discard();
            }
        } );
        ImageButton ais = view.findViewById(R.id.imageButton3);
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
        if(ed.getText().toString().matches("") || ed2.getText().toString().matches("") || ed3.getText().toString().matches("") || (ed4.getText().toString().matches("") && sed.isActivated()) || ed5.getText().toString().matches("")){
            Snackbar sn = Snackbar.make(getView(), "One or more fields have not been properly filled in yet", 1000);
            sn.show();
        } else if( MyDeclaration.general != null) {
            new updateDeclarations().execute("");
        } else {
            new createDeclaration().execute("");
        }
    }
    private class createDeclaration extends AsyncTask<String, Integer, Long> {
        String TAGB = "updateDeclarations database";

        @Override
        protected Long doInBackground(String... value) {
            if(sed.isActivated()){
                MyDeclaration.general.setBtwCost(  Double.parseDouble(ed4.getText().toString()));
            } else {
                MyDeclaration.general.setBtwCost((long) 0);
            }
            MyDeclaration.general.setTotalCost(Double.parseDouble(ed5.getText().toString()));
            MyDeclaration.info.setCash(Double.parseDouble(ed5.getText().toString()));
            MyDeclaration.general.setCost( Double.parseDouble(ed3.getText().toString()));
            MyDeclaration.info.setTimestamp( (ed2.getText()).toString());
            MyDeclaration.general.setDes( (ed.getText()).toString());
            MyDeclaration.general.setUid(MyDeclaration.info.getUid());
            MyDeclaration.general.setBtw(sed.isActivated());

//            getActivity().setContentView(R.layout.activity_main);
            db.DecDAO().insertOther(MyDeclaration.general);
            db.DecDAO().insert(MyDeclaration.info);


//            AddDeclarationFragment firstFragment = new AddDeclarationFragment();
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .add(R.id.mainA, firstFragment).commit();
            return (long) 0;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            Intent intent = new Intent(getContext(), AddDeclarationFragment.class);
            intent.putExtra("info", MyDeclaration.info.getUid());
            intent.putExtra("cid", MyDeclaration.general.getOID());
            intent.putExtra("car", false);
            intent.putExtra("general", true);
            startActivity(intent);
        }
    }

    private class updateDeclarations extends AsyncTask<String, Integer, Long> {
        String TAGB = "updateDeclarations database";

        @Override
        protected Long doInBackground(String... value) {
            if(!ed.getText().toString().matches(MyDeclaration.general.getDes())){
                MyDeclaration.general.setDes( (ed.getText()).toString());
            }
            if(!ed2.getText().toString().matches(MyDeclaration.info.getTimestamp())){
                MyDeclaration.info.setTimestamp( (ed2.getText()).toString());
            }
            if (Double.parseDouble(ed3.getText().toString()) != (MyDeclaration.general.getCost())){
                MyDeclaration.general.setCost( Double.parseDouble(ed3.getText().toString()));
            }
            if( Double.parseDouble(ed4.getText().toString())  != (MyDeclaration.general.getBtwCost()) && sed.isActivated()){
                MyDeclaration.general.setBtwCost(  Double.parseDouble(ed4.getText().toString()));
            } else if( !sed.isActivated()){
                MyDeclaration.general.setBtwCost((long) 0);
            }
            if( Double.parseDouble(ed5.getText().toString()) != (MyDeclaration.general.getTotalCost())) {
                MyDeclaration.general.setTotalCost(Double.parseDouble(ed5.getText().toString()));
                MyDeclaration.info.setCash(Double.parseDouble(ed5.getText().toString()));
            }
            //TODO make sure that a new oid is made and set
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
