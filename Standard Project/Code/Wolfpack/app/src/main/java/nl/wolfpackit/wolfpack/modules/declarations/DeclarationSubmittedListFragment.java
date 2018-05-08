package nl.wolfpackit.wolfpack.modules.declarations;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.wolfpackit.wolfpack.R;
import nl.wolfpackit.wolfpack.database.Database;
import nl.wolfpackit.wolfpack.database.Declaration;
import nl.wolfpackit.wolfpack.modules.BaseActivity;
import nl.wolfpackit.wolfpack.modules.declarations.add.DeclarationCreationActivity;

public class DeclarationSubmittedListFragment extends Fragment{
    BaseActivity activity;
    RecyclerView recyclerView;
    List<Declaration> declarations;
    protected static int addDeclarationResult = 434;
    int requestID = 0;
    EditText searchField;

    public DeclarationSubmittedListFragment(){}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Database.createDeclaration(new Database.QueryResult<Declaration>() {
//            public void onResult(final Declaration dec) {
//                dec.setAmount(90);
//                dec.update();
//                dec.setName("testy");
//                dec.setDateTimestamp(System.currentTimeMillis());
//                Database.createDeclarationFeedback(new Database.QueryResult<DeclarationFeedback>() {
//                    public void onResult(DeclarationFeedback feedback){
//                        feedback.setDeclarationID(dec.getID());
//                        feedback.setApproved(true);
//                        feedback.setFeedback("testers");
//                        feedback.update();
//                    }
//                });
//                dec.update();
//            }
//        });
        getAllDeclarations();
    }
    protected void updateDeclarationViews(){
        recyclerView.getAdapter().notifyDataSetChanged();
    }
    protected void getAllDeclarations(){
        declarations = new ArrayList<>();
        final int thisRequestID = ++requestID;
        Database.getDeclarationsPending(new Database.QueryResult<List<Declaration>>() {
            public void onResult(List<Declaration> pendingDeclarations){
                if(thisRequestID==requestID){
                    declarations.addAll(declarations.size(), pendingDeclarations);
                    updateDeclarationViews();
                }
            }
        });
        Database.getDeclarationsDenied(new Database.QueryResult<List<Declaration>>() {
            public void onResult(List<Declaration> deniedDeclarations){
                if(thisRequestID==requestID){
                    declarations.addAll(0, deniedDeclarations);
                    updateDeclarationViews();
                }
            }
        });
    }
    protected void getSearchDeclarations(String search){
        declarations = new ArrayList<>();
        Database.getDeclarationsBySearch(search, new Database.QueryResult<List<Declaration>>() {
            public void onResult(List<Declaration> searchDeclarations){
                declarations = searchDeclarations;
                updateDeclarationViews();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_declaration_submitted_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (BaseActivity) getActivity();

        recyclerView = view.findViewById(R.id.declarationsSubmitted);
        LinearLayoutManager llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(new RecyclerView.Adapter<ViewHolder>(){
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.declaration_submitted_item, parent, false);
                ViewHolder vh = new ViewHolder(v);
                return vh;
            }
            public void onBindViewHolder(ViewHolder holder, int position){
                final View v = holder.view;
                Declaration d = declarations.get(position);

                //set data
                d.getTotal(new Database.QueryResult<Float>() {
                    public void onResult(Float total){
                        ((TextView)v.findViewById(R.id.declarationAmount)).setText("€ "+String.format("%.2f", total));
                    }
                });
                ((TextView)v.findViewById(R.id.declarationNameLabel)).setText(d.getName());

                //set date
                if(d.getDateTimestamp()>0){
                    Date date = new Date(d.getDateTimestamp());
                    String[] months = activity.getString(R.string.declarations_months).split(",");
                    String dateText = date.getDate()+" "+months[date.getMonth()-1]+" "+(1900+date.getYear());
                    ((TextView)v.findViewById(R.id.declarationDate)).setText(dateText);
                }else{
                    ((TextView)v.findViewById(R.id.declarationDate)).setText("Missing");
                }
            }
            public int getItemCount() {
                return declarations.size();
            }
        });

        //search bar
        searchField = view.findViewById(R.id.declarationSearchField);
        searchField.addTextChangedListener(new TextWatcher(){
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            public void afterTextChanged(Editable s){
                String text = searchField.getText().toString();
                Log.w("detect", "test:"+text);
                if(text.length()>0){
                    getSearchDeclarations(text);
                }else{
                    getAllDeclarations();
                }
            }
        });
        searchField.setOnFocusChangeListener(new View.OnFocusChangeListener(){ //hide keyboard when focus is lost
            public void onFocusChange(View v, boolean hasFocus){
                if(!hasFocus){
                    hideKeyboard(v);
                }
            }
        });
        getView().setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                hideKeyboard(v);
            }
        });

        //create declaration button
        ((ImageButton)activity.findViewById(R.id.declarationsAddButton)).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(activity, DeclarationCreationActivity.class);
                startActivityForResult(intent, addDeclarationResult);
            }
        });
    }
    protected void hideKeyboard(View v){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.w("DETECT", "SOMETHING CHANGED");
        super.onActivityResult(requestCode, resultCode, data);

        searchField.setText(searchField.getText());
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
