package com.example.kath.appdev5kath;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity" ;
    //Buttons
    private static Button btnEmail;
    private static Button btnCall;

    //Edit Text variables
    private static EditText editTextName;
    private String name;
    private static boolean isNameWritten = false;

    //Camera Variables
    private static final int REQUEST_TAKE_IMAGE = 1;
    private static boolean isPhotoTaken = false;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEmail = (Button)findViewById(R.id.button_emailWP);
        btnCall = (Button)findViewById(R.id.button_callWP);

        editTextName = (EditText)findViewById(R.id.editText_name);
        name = editTextName.getText().toString();

        if(name != ""){
            isNameWritten = true;
        }

        //SAVE KEY VALUE
        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String nameInput = editTextName.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                saveEditTextData();
            }
        });

        loadEditTextData();
        updateViews();

    }


    public void onClickEmail(View view) {
        Log.d(TAG, "onClickEmail");
        if(!btnEmail.isEnabled())
        {
            Toast.makeText(getApplicationContext(),
                    "Button disabled, please fill in name and take a picture.", Toast.LENGTH_LONG);
        }
        else
        {
            Intent i = new Intent(MainActivity.this, EmailWPActivity.class);
            startActivity(i);
        }

    }

    public void onClickCall(View view) {
        Log.d(TAG, "onClickCall");
        if(!btnCall.isEnabled())
        {
            Toast.makeText(getApplicationContext(),
                    "Button disabled, please fill in name and take a picture.", Toast.LENGTH_LONG);
        }
        else
        {
            Intent i = new Intent(MainActivity.this, CallWPActivity.class);
            startActivity(i);
        }
    }

    public void onClickCamera(View view) {
        Log.d(TAG, "onClickCamera");
        Intent imageI = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (imageI.resolveActivity(getPackageManager()) != null) {
            File imageFile = null;
            try {
                imageFile = CreateTheImage();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (imageFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "Android/data/com.example.kath.appdev5kath/files/Pictures",
                            imageFile);
                    imageI.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(imageI, REQUEST_TAKE_IMAGE);
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        }
    }

    //SAVE IMAGE
    private File CreateTheImage() throws IOException {
        //Create an image file name
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        //Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //FEEDBACK
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == REQUEST_TAKE_IMAGE && resultCode == RESULT_OK) {
                Toast.makeText(this, "Picture was successfully taken", Toast.LENGTH_SHORT).show();
                isPhotoTaken = true;
                if(isPhotoTaken && isNameWritten){
                    btnCall.setEnabled(true);
                    btnEmail.setEnabled(true);
                }
            } else {
                Toast.makeText(this, "Picture was not successfully taken", Toast.LENGTH_SHORT).show();
            }
    }

    //SAVE KEY DATA VALUE

    String text;
    private void saveEditTextData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TEXT_NAME", editTextName.getText().toString());
        editor.apply();
    }

    private void loadEditTextData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_NAME", Context.MODE_PRIVATE);
        text = sharedPreferences.getString("TEXT_NAME", "");
    }

    private void updateViews() {
        editTextName.setText(text);
    }
}
