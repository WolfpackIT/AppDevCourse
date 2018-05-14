package nl.wolfpackit.callemailapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 2;
    private SharedPreferences sharedPreferences;
    private String MY_PREFERENCES = "MY_PREFERENCES";
    private String PREFERENCE_NAME = "PREFERENCE_NAME";
    private String currentPhotoPath;
    private boolean succesfulCapture;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText nameTextField = findViewById(R.id.editText1);
        final Button emailButton = findViewById(R.id.button);
        final Button callButton = findViewById(R.id.button2);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        // I decided to just normally disable the buttons. No toast can be displayed
        // to the user if he presses one of these buttons, indeed, but I assumed that the user
        // is a rational human being and he doesn't click on greyed out buttons. I could have simply
        // deactivated the buttons and changed their colors to light gray, but this seems to make
        // the whole app a bit slow and buggy.
        emailButton.setEnabled(false);
        callButton.setEnabled(false);

        String sharedPreferenceName = sharedPreferences.getString(PREFERENCE_NAME, "");
        nameTextField.setText(sharedPreferenceName);

        nameTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (checkNameNotNull() && succesfulCapture) {
                    emailButton.setEnabled(true);
                    callButton.setEnabled(true);
                }
                else {
                    emailButton.setEnabled(false);
                    callButton.setEnabled(false);
                }
                editor.putString(PREFERENCE_NAME, nameTextField.getText().toString());
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // Method used to check whether the name textfield contains some characters (it's not null)
    public boolean checkNameNotNull() {
        final EditText nameTextField = findViewById(R.id.editText1);
        String textfieldContent = nameTextField.getText().toString();
        if (textfieldContent.matches("")) {
            return false;
        }
        return true;
    }

    public void openEmailActivity(View view) {
        Intent intent = new Intent(this, EmailWPActivity.class);
        intent.putExtra("imagePath", currentPhotoPath);
        startActivity(intent);
    }

    public void openCallActivity(View view) {
        Intent intent = new Intent(this, CallWPActivity.class);
        startActivity(intent);
    }

    private File createImageFile() throws IOException {
        String imageFileName = "Wolfpack";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void takePictureWithCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }
            catch(IOException ex){
                Log.e("Camera", ex.getMessage());
            }
            if(photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "/Android/data/app/callemailapp/files/Pictures", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        final Button emailButton = findViewById(R.id.button);
        final Button callButton = findViewById(R.id.button2);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // If the picture is captured succesfully, enable the buttons
            succesfulCapture = true;
            if (checkNameNotNull() && succesfulCapture) {
                emailButton.setEnabled(true);
                callButton.setEnabled(true);
            }
            Toast.makeText(this, "Picture taken succesfully", Toast.LENGTH_SHORT).show();
        }
    }

    public void openCameraActivity(View view) {
        // Check if permissions were not given by the user
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_DENIED) {
            // If the user did not give the permission yet, then request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
        // If permission was already given, start camera activity
        else {
            takePictureWithCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePictureWithCamera();
            }
            else {
                Toast.makeText(this, "You need to give us permission to use the camera", Toast.LENGTH_SHORT).show();
            }
        }
    }
}