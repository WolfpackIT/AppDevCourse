package nl.wolfpackit.wolfpack.modules.declarations.add;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import nl.wolfpackit.wolfpack.database.Receipt;
import nl.wolfpackit.wolfpack.database.TravelDeclaration;
import nl.wolfpackit.wolfpack.modules.declarations.DeclarationSubmittedListFragment;

public class DeclarationCreationActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText nameInput;
    TextView totalOutput;

    float total;
    int decsLeft;
    List<Declaration> declarations = new ArrayList<>();
    protected static int addDeclarationResult = 344;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declaration_creation);

        //handle the maximum character input length
        nameInput = findViewById(R.id.declarationName);
        final TextView characters = findViewById(R.id.declarationCharacters);
        nameInput.addTextChangedListener(new TextWatcher(){
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            public void afterTextChanged(Editable s){
                String text = nameInput.getText().toString();
                if(text.length()>20){
                    nameInput.setText(text.substring(0, 20));
                }else{
                    characters.setText(text.length()+"/20");
                }
            }
        });
        nameInput.setText("D"+(int)(Math.random()*Math.pow(10,8)));

        nameInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){ //hide keyboard when focus is lost
            public void onFocusChange(View v, boolean hasFocus){
                if(!hasFocus){
                    hideKeyboard();
                }
            }
        });
        getWindow().getDecorView().findViewById(android.R.id.content).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                hideKeyboard();
                nameInput.clearFocus();
            }
        });

        //declarations recycler
        recyclerView = findViewById(R.id.declarationDeclarations);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(new RecyclerView.Adapter<DeclarationSubmittedListFragment.ViewHolder>(){
            public DeclarationSubmittedListFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.declaration_creation_item, parent, false);
                DeclarationSubmittedListFragment.ViewHolder vh = new DeclarationSubmittedListFragment.ViewHolder(v);
                return vh;
            }
            public void onBindViewHolder(DeclarationSubmittedListFragment.ViewHolder holder, int position){
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
                    String[] months = getString(R.string.declarations_months).split(",");
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

        //add new declaration
        ((ImageButton)findViewById(R.id.declarationAddButton)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(DeclarationCreationActivity.this, DeclarationAddActivity.class);
                intent.putExtra("name", nameInput.getText().toString());
                hideKeyboard();
                startActivityForResult(intent, addDeclarationResult);
            }
        });

        //total output
        totalOutput = findViewById(R.id.declarationSumText);
        totalOutput.setText("0");

        //cancel
        findViewById(R.id.declarationCancel).setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });

        //submit
        findViewById(R.id.declarationSubmitButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                submitDeclaration();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==addDeclarationResult && resultCode== Activity.RESULT_OK){
            Declaration d = new Declaration();
            d.importString(data.getStringExtra("declaration"));

            declarations.add(d);

            //create new random name
            nameInput.setText("D"+(int)(Math.random()*Math.pow(10,8)));

            updateDeclarations();
        }
    }
    protected void updateDeclarations(){
        recyclerView.getAdapter().notifyDataSetChanged();

        //update the total
        total = 0;
        decsLeft = declarations.size();
        for(Declaration dec: declarations){
            dec.getTotal(new Database.QueryResult<Float>(){
                public void onResult(Float t){
                    total += t;

                    if(--decsLeft==0){
                        totalOutput.setText(String.format("%.2f", total));
                    }
                }
            });
        }
    }

    protected void submitDeclaration(){

        final int[] finished = new int[]{0};
        final int[] left = new int[declarations.size()];
        for(int i=0; i<declarations.size(); i++){
            final Declaration dec = declarations.get(i);
            final int index = i;
            left[index] = 2;

            final Database.QueryResult<Declaration> importData = new Database.QueryResult<Declaration>(){
                public void onResult(Declaration d) {
                    d.copyDeclaration(dec);
                    d.update();
                    d.getTravelDeclaration(new Database.QueryResult<TravelDeclaration>(){
                        public void onResult(TravelDeclaration td){
                            if(td!=null) td.update();
                        }
                    });
                    d.getReceipt(new Database.QueryResult<Receipt>(){
                        public void onResult(Receipt r){
                            if(r!=null) r.update();
                        }
                    });

                    if(++finished[0]==left.length)
                        finish();
                }
            };

            Database.createDeclaration(new Database.QueryResult<Declaration>() {
                public void onResult(final Declaration d){
                    //attache receipt
                    dec.getReceipt(new Database.QueryResult<Receipt>(){
                        public void onResult(Receipt r){
                            if(r!=null){
                                Database.createReceipt(new Database.QueryResult<Receipt>(){
                                    public void onResult(Receipt r){
                                        d.setReceipt(r);
                                        if(--left[index]==0)
                                            importData.onResult(d);
                                    }
                                });
                            }else{
                                if(--left[index]==0)
                                    importData.onResult(d);
                            }
                        }
                    });

                    //attache travel declaration
                    dec.getTravelDeclaration(new Database.QueryResult<TravelDeclaration>() {
                        public void onResult(TravelDeclaration td){
                            if(td!=null){
                                Database.createTravelDeclaration(new Database.QueryResult<TravelDeclaration>() {
                                    public void onResult(TravelDeclaration td){
                                        d.setTravelDeclaration(td);
                                        if(--left[index]==0)
                                            importData.onResult(d);
                                    }
                                });
                            }else{
                                if(--left[index]==0)
                                    importData.onResult(d);
                            }
                        }
                    });
                }
            });
        }
    }

    protected void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }
}
