package nl.wolfpackit.wolfpack.modules.declarations.add;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.wolfpackit.wolfpack.ImageTools;
import nl.wolfpackit.wolfpack.R;
import nl.wolfpackit.wolfpack.database.Database;
import nl.wolfpackit.wolfpack.database.Declaration;
import nl.wolfpackit.wolfpack.database.Receipt;
import nl.wolfpackit.wolfpack.modules.BaseActivity;
import nl.wolfpackit.wolfpack.modules.declarations.DeclarationSubmittedListFragment;

public class DeclarationAddGeneralFragment extends Fragment {
    DeclarationAddActivity activity;
    RecyclerView recyclerView; //receipt recycler
    public static int fileSelectResult = 333;
    public static int imageSelectResult = 433;


    public DeclarationAddGeneralFragment(){}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_declaration_add_general, container, false);
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

        //count description length
        final EditText nameInput = activity.findViewById(R.id.declarationGeneralDescriptionText);
        final TextView characters = activity.findViewById(R.id.declarationGeneralDescriptionCount);
        nameInput.addTextChangedListener(new TextWatcher(){
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            public void afterTextChanged(Editable s){
                String text = nameInput.getText().toString();
                if(text.length()>50){
                    nameInput.setText(text.substring(0, 50));
                }else{
                    characters.setText(text.length()+"/50");
                }
                activity.getDeclaration().setDescription(nameInput.getText().toString());
            }
        });
        nameInput.setText("");
        nameInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){ //hide keyboard when focus is lost
            public void onFocusChange(View v, boolean hasFocus){
                if(!hasFocus){
                    hideKeyboard(v);
                }
            }
        });

        //date input
        final EditText dateInput = activity.findViewById(R.id.declarationGeneralDateText);
        dateInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String text = dateInput.getText().toString();
                    SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
                    try{
                        Date d = f.parse(text);
                        activity.getDeclaration().setDateTimestamp(d.getTime());
                    }catch (ParseException e){
                        Toast.makeText(activity, getString(R.string.declaration_date_format_message), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        Date date = new Date();
        dateInput.setText(date.getDate()+"-"+date.getMonth()+"-"+(date.getYear()+1900));
        dateInput.getOnFocusChangeListener().onFocusChange(dateInput, false); //save value

        //amount input
        final EditText amountInput = activity.findViewById(R.id.declarationGeneralAmountText);
        amountInput.addTextChangedListener(new TextWatcher(){
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            public void afterTextChanged(Editable s){
                try{
                    String text = amountInput.getText().toString();
                    activity.setGeneralAmount(Float.parseFloat(text));
                }catch(Exception e){}
            }
        });
        amountInput.setText("0");

        //btw amount input
        final Switch btwToggle = activity.findViewById(R.id.declarationGeneralBTWSwitch);

        final ImageView alertSymbol = activity.findViewById(R.id.declarationGeneralBTWAlert);
        final TextView alertLabel = activity.findViewById(R.id.declarationGeneralBTWAlertLabel);
        final EditText btwInput = activity.findViewById(R.id.declarationGeneralBTWAmountText);
        btwInput.addTextChangedListener(new TextWatcher(){
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            public void afterTextChanged(Editable s){
                try{
                    String text = btwInput.getText().toString();
                    float amount = Float.parseFloat(text);
                    float generalAmount = activity.getDeclaration().getAmount();
                    float frac = amount/generalAmount;

                    float[] possibleFracs = new float[]{0.21f};
                    boolean isValidFrac = false;
                    for(float vFrac: possibleFracs)
                        if(Math.abs(vFrac-frac)<0.01)
                            isValidFrac = true;

                    if(isValidFrac)
                        activity.setBTWAmount(amount);
                    if(isValidFrac || !btwToggle.isChecked()){
                        alertSymbol.setVisibility(View.GONE);
                        alertLabel.setVisibility(View.GONE);
                    }else{
                        alertSymbol.setVisibility(View.VISIBLE);
                        alertLabel.setVisibility(View.VISIBLE);
                    }
                }catch(Exception e){}
            }
        });
        btwInput.setText("0");

        //btw toggle
        btwToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b){
                View container = activity.findViewById(R.id.declarationGeneralBTWContainerOuter);
                if(b){
                    container.setVisibility(View.VISIBLE);
                    String text = btwInput.getText().toString();
                    activity.setBTWAmount(Float.parseFloat(text));
                }else{
                    container.setVisibility(View.INVISIBLE);
                    activity.setBTWAmount(0);
                }
            }
        });
        btwToggle.setChecked(true); //force an event fire
        btwToggle.setChecked(false);

        //setup receipt adapter
        recyclerView = setupReceiptRecycler(R.id.declarationGeneralReceiptContainer, activity);
    }
    protected void hideKeyboard(View v){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static RecyclerView setupReceiptRecycler(int id, final DeclarationAddActivity activity){
        RecyclerView recyclerView = activity.findViewById(id);
        LinearLayoutManager llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(new RecyclerView.Adapter(){
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
                Log.w("VAL", viewType+"-stuff");
                View v;
                if(viewType==0){
                    v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.declaration_receipt_add_file, parent, false);
                }else if(viewType==1){
                    v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.declaration_receipt_take_picture, parent, false);
                }else{
                    v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.declaration_receipt_thumbnail, parent, false);
                }
                ViewHolder vh = new ViewHolder(v);
                return vh;
            }
            public int getItemViewType(int position) {
                return position<2?position:2;
            }
            public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
                if(position==0){
                    holder.itemView.findViewById(R.id.declarationReceiptAddFile).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("*/*");

                            activity.startActivityForResult(intent, fileSelectResult);
                        }
                    });
                }else if(position==1){
                    holder.itemView.findViewById(R.id.declarationReceiptTakePicture).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view){
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if(takePictureIntent.resolveActivity(activity.getPackageManager())!=null){
                                activity.startActivityForResult(takePictureIntent, imageSelectResult);
                            }
                        }
                    });
                }else{
                    Receipt receipt = activity.getReceipt();
                    Log.w("TYPE", receipt.getType());
                    if(receipt.getType().equals("IMAGE")){
                        Bitmap image = ImageTools.getImageFromString(receipt.getData());
                        ImageTools.makeImageCircular(image, new ImageTools.ImageResultListener(){
                            public void onImageRecieve(Bitmap image){
                                ((ImageView)holder.itemView.findViewById(R.id.declarationReceiptPicture)).setImageBitmap(image);
                            }
                        });
                    }else{
                        ((ImageView)holder.itemView.findViewById(R.id.declarationReceiptPicture)).setImageDrawable(activity.getResources().getDrawable(R.drawable.background_gradient));
                    }

                    ((ImageButton)holder.itemView.findViewById(R.id.declarationReceiptRemoveButton)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view){
                            activity.removeReceipt();
                        }
                    });
                }
            }
            public int getItemCount(){
                return 2+(activity.getReceipt()!=null?1:0);
            }
        });
        return recyclerView;
    }
    public void selectedFile(String data){
        Receipt receipt = activity.getReceipt();
        if(receipt==null)
            receipt = activity.createReceipt();
        receipt.setType("FILE");
        receipt.setData(data);
        recyclerView.getAdapter().notifyDataSetChanged();
    }
    public void selectedImage(String data){
        Receipt receipt = activity.getReceipt();
        if(receipt==null)
            receipt = activity.createReceipt();
        receipt.setType("IMAGE");
        receipt.setData(data);
        recyclerView.getAdapter().notifyDataSetChanged();
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


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }


}
