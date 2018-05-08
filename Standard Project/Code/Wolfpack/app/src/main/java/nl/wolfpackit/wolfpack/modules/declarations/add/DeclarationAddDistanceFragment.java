package nl.wolfpackit.wolfpack.modules.declarations.add;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import nl.wolfpackit.wolfpack.R;
import nl.wolfpackit.wolfpack.database.Declaration;
import nl.wolfpackit.wolfpack.database.TravelDeclaration;
import nl.wolfpackit.wolfpack.modules.BaseActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.wolfpackit.wolfpack.R;
import nl.wolfpackit.wolfpack.database.Database;
import nl.wolfpackit.wolfpack.database.Declaration;
import nl.wolfpackit.wolfpack.modules.BaseActivity;
import nl.wolfpackit.wolfpack.modules.declarations.DeclarationSubmittedListFragment;

public class DeclarationAddDistanceFragment extends Fragment {
    DeclarationAddActivity activity;
    RecyclerView recyclerView; //receipt recycler
    float compensationAmount = 0.19f;
    public DeclarationAddDistanceFragment(){}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_declaration_add_distance, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (DeclarationAddActivity) getActivity();
        getView().setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                hideKeyboard(v);
            }
        });

        //compensation
        TextView compensationText = activity.findViewById(R.id.declarationDistanceCompensationText);
        compensationText.setText(compensationAmount+"");

        //distance
        final EditText distanceInput = activity.findViewById(R.id.declarationDistanceKMText);
        distanceInput.addTextChangedListener(new TextWatcher(){
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            public void afterTextChanged(Editable s){
                try {
                    final String text = distanceInput.getText().toString();
                    activity.setDistanceAmount(Float.parseFloat(text), compensationAmount);
                }catch(Exception e){};
            }
        });
        distanceInput.setText("0");

        //date input
        final EditText dateInput = activity.findViewById(R.id.declarationDistanceDateText);
        dateInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String text = dateInput.getText().toString();
                    SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
                    try{
                        final Date d = f.parse(text);
                        activity.getTravelDeclaration(new Database.QueryResult<TravelDeclaration>(){
                            public void onResult(TravelDeclaration td){
                                td.setDateTimestamp(d.getTime());
                            }
                        });
                    }catch (ParseException e){
                        Toast.makeText(activity, getString(R.string.declaration_date_format_message), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        Date date = new Date();
        dateInput.setText(date.getDate()+"-"+date.getMonth()+"-"+(date.getYear()+1900));
        dateInput.getOnFocusChangeListener().onFocusChange(dateInput, false); //save value

        //setup receipt adapter
        recyclerView = DeclarationAddGeneralFragment.setupReceiptRecycler(R.id.declarationDistanceReceiptContainer, activity);

        //cities
        final EditText fromInput = activity.findViewById(R.id.declarationDistanceFromText);
        distanceInput.addTextChangedListener(new TextWatcher(){
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            public void afterTextChanged(Editable s){
                final String text = fromInput.getText().toString();
                activity.getTravelDeclaration(new Database.QueryResult<TravelDeclaration>(){
                    public void onResult(TravelDeclaration td){
                        td.setStartCity(text);
                    }
                });
            }
        });

        final EditText toInput = activity.findViewById(R.id.declarationDistanceToText);
        distanceInput.addTextChangedListener(new TextWatcher(){
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            public void afterTextChanged(Editable s){
                final String text = fromInput.getText().toString();
                activity.getTravelDeclaration(new Database.QueryResult<TravelDeclaration>(){
                    public void onResult(TravelDeclaration td){
                        td.setEndCity(text);
                    }
                });
            }
        });
    }
    protected void hideKeyboard(View v){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void updateReceipt(){
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }
}
