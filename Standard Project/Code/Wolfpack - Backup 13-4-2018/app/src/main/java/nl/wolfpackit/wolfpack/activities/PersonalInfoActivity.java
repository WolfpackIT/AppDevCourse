package nl.wolfpackit.wolfpack.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import nl.wolfpackit.wolfpack.ImageTools;
import nl.wolfpackit.wolfpack.R;
import nl.wolfpackit.wolfpack.database.Account;
import nl.wolfpackit.wolfpack.modules.ModuleFragment;
import nl.wolfpackit.wolfpack.modules.semiModules.SemiModuleFragment;

public class PersonalInfoActivity extends AppCompatActivity {
    protected boolean editing = false;
    protected Account account = Account.getAccount();
    protected View editToolbar;
//    public static PersonalInfoActivity createInstance() {
//        ModuleFragment f = ModuleFragment.createInstance(R.layout.activity_personal_info, PersonalInfoActivity.class);
//        return (PersonalInfoActivity) f;
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        Toolbar toolbar = findViewById(R.id.personalInfoToolbar);
        setSupportActionBar(toolbar);

        //add back navigation
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.back);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("");


        //set base info
        ((TextView) findViewById(R.id.profileName)).setText(account.getName());
        ((TextView) findViewById(R.id.profileFunction)).setText(account.getRole().name());
        account.getPicture(new ImageTools.ImageResultListener() {
            public void onImageRecieve(Bitmap image) {
                ((ImageView) findViewById(R.id.profilePicture)).setImageBitmap(image);
            }
        }, this);

        //load data
        loadData();
        ((ImageButton)findViewById(R.id.profileEditButton)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!editing)
                    setEditMode(true);
            }
        });

        //hide keyboard when clicking outside of textfield
        getWindow().getDecorView().findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        //setup edit stop events
        editToolbar = findViewById(R.id.profileEditToolbar);
        findViewById(R.id.profileXButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditMode(false);
            }
        });
        findViewById(R.id.profileCheckButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFields();
                setEditMode(false);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret = super.onOptionsItemSelected(item);
        int ID = item.getItemId();
        switch(ID){
            case android.R.id.home:
                finish();
                return true;
        }
        return ret;
    }


    protected void saveFields(){
        account.setDateOfBirth(((EditText)findViewById(R.id.profileDateField)).getText().toString());
        account.setCity(((EditText)findViewById(R.id.profileCityField)).getText().toString());
        account.setPhone(((EditText)findViewById(R.id.profileNumberField)).getText().toString());
        account.setIBAN(((EditText)findViewById(R.id.profileIBANField)).getText().toString());
        account.setAllergies(((EditText)findViewById(R.id.profileAllergiesField)).getText().toString());
        account.update();
    }
    protected void setEditMode(boolean enabled){
        editing = enabled;
        if(enabled){
//            Toolbar toolbar = activity.findViewById(R.id.personalInfoEditToolbar);
//            ActionBar actionbar = activity.setToolbar(toolbar, false);
//            actionbar.set
            getSupportActionBar().hide();
            editToolbar.setVisibility(View.VISIBLE);
        }else{
//            Toolbar toolbar = activity.findViewById(getToolbar());
//            ActionBar actionbar = activity.setToolbar(toolbar);

            hideKeyboard();
            loadData();
            getSupportActionBar().show();
            editToolbar.setVisibility(View.GONE);
        }

        int fieldVisibility = enabled?View.VISIBLE:View.INVISIBLE;
        ((EditText)findViewById(R.id.profileDateField)).setVisibility(fieldVisibility);
        ((EditText)findViewById(R.id.profileCityField)).setVisibility(fieldVisibility);
        ((EditText)findViewById(R.id.profileNumberField)).setVisibility(fieldVisibility);
        ((EditText)findViewById(R.id.profileIBANField)).setVisibility(fieldVisibility);
        ((EditText)findViewById(R.id.profileAllergiesField)).setVisibility(fieldVisibility);
        int valueVisibility = !enabled?View.VISIBLE:View.INVISIBLE;
        ((TextView)findViewById(R.id.profileDateValue)).setVisibility(valueVisibility);
        ((TextView)findViewById(R.id.profileCityValue)).setVisibility(valueVisibility);
        ((TextView)findViewById(R.id.profileNumberValue)).setVisibility(valueVisibility);
        ((TextView)findViewById(R.id.profileIBANValue)).setVisibility(valueVisibility);
        ((TextView)findViewById(R.id.profileAllergiesValue)).setVisibility(valueVisibility);
    }
    protected void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    protected void loadData(){
        //date
        ((TextView)findViewById(R.id.profileDateValue)).setText(account.getDateOfBirth());
        ((EditText)findViewById(R.id.profileDateField)).setText(account.getDateOfBirth());

        //city
        ((TextView)findViewById(R.id.profileCityValue)).setText(account.getCity());
        ((EditText)findViewById(R.id.profileCityField)).setText(account.getCity());

        //number
        ((TextView)findViewById(R.id.profileNumberValue)).setText(account.getPhone());
        ((EditText)findViewById(R.id.profileNumberField)).setText(account.getPhone());

        //IBAN
        ((TextView)findViewById(R.id.profileIBANValue)).setText(account.getIBAN());
        ((EditText)findViewById(R.id.profileIBANField)).setText(account.getIBAN());

        //allergies
        ((TextView)findViewById(R.id.profileAllergiesValue)).setText(account.getAllergies());
        ((EditText)findViewById(R.id.profileAllergiesField)).setText(account.getAllergies());
    }
}
