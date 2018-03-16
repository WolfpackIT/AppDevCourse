package nl.wolfpackit.appdev12;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
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
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Button mailButton;
    private Button callButton;
    private Button pictureButton;
    private EditText nameTextBox;

    private String imageName = "Wolfpack.jpg";
    private String imagePath = ""; //will be assigned automatically
    private boolean pictureTaken = false;

    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mailButton = this.findViewById(R.id.emailButton);
        callButton = this.findViewById(R.id.callButton);
        pictureButton = this.findViewById(R.id.pictureButton);

        nameTextBox = this.findViewById(R.id.nameTextBox);
        nameTextBox.addTextChangedListener(new TextWatcher(){
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            public void afterTextChanged(Editable s) {
                nameChange(s.toString());
            }
        });

        mPrefs = getSharedPreferences("prefs", 0);
        nameTextBox.setText(mPrefs.getString("name", ""));


        if(shouldAskPermissions()){
            askPermissions();
        }

        updateButtonsEnabled();
    }

    //helped methods to make a button appear to be disabled but still listen for all events
    private void setEnabled(Button button, boolean enabled){
        button.setAlpha(enabled?1f:0.5f);
        button.setTag(button.getId(), enabled);
    }
    private boolean isEnabled(Button button){
        return (boolean)button.getTag(button.getId());
    }

    public void emailWP(View view){
        if(isEnabled(mailButton)){
            Intent intent = new Intent(this, EmailWPActivity.class);
            intent.putExtra("imagePath", imagePath);
            startActivity(intent);
        }else{
            Toast.makeText(this, getString(R.string.buttonDisabled), Toast.LENGTH_SHORT).show();
        }
    }
    public void callWP(View view){
        if(isEnabled(mailButton)){
            Intent intent = new Intent(this, CallWPActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, getString(R.string.buttonDisabled), Toast.LENGTH_SHORT).show();
        }
    }
    public void nameChange(String text){
        updateButtonsEnabled();
        mPrefs.edit().putString("name", text).commit();
    }


    private void updateButtonsEnabled(){
        boolean enabled = pictureTaken & nameTextBox.getText().length()>0;
        setEnabled(mailButton, enabled);
        setEnabled(callButton, enabled);
    }


    public void takePicture(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File photoFile = new File(storageDir, imageName);

                // Save a file: path for use with ACTION_VIEW intents
                imagePath = photoFile.getAbsolutePath();

                Uri photoURI = FileProvider.getUriForFile(this,
                        "nl.wolfpackit.appdev12",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (Exception ex) {
                Toast.makeText(this, "Something went wrong while trying to take a picture", Toast.LENGTH_SHORT).show();
                Log.w("APPDEV12", ex.toString());
            }
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            pictureTaken = true;
            updateButtonsEnabled();
        }
    }

    // Storage Permissions
    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }
}
