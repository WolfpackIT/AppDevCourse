package com.example.assignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static Button btnEmail;
    private static Button btnCall;
    private static Button btnPicture;
    private static EditText editTextName;
    private String text;

    static final int REQUEST_TAKE_PHOTO = 1;
    static boolean isPhotoTaken = false;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";

    String mCurrentPhotoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEmail = (Button)findViewById(R.id.btnEmailWP);
        btnCall = (Button)findViewById(R.id.btnCallWP);
        btnPicture = (Button)findViewById(R.id.btnTakePicture);
        editTextName = (EditText)findViewById(R.id.editTextName);

        //Disable the call and mail buttons
        disableButtons();

        //BUTTONS//
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!btnEmail.isClickable()) {
                    Toast.makeText(getApplicationContext(), "Email button is disabled. Please check if picture taken and filled in name", Toast.LENGTH_LONG).show();
                } else {
                    clickEmailPage();
                }

            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!btnCall.isClickable()) {
                    Toast.makeText(getApplicationContext(), "Call button is disabled. Please check if picture taken and filled in name", Toast.LENGTH_LONG).show();
                } else {
                    clickCallPage();
                }
            }
        });

        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOpenCamera();
            }
        });

		// Nice approach
		// Maybe move the setClickable()'s inside
		// the disableButtons() method
        disableButtons();
        btnEmail.setClickable(false);
        btnCall.setClickable(false);

        //EDITTEXT//
        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String nameInput = editTextName.getText().toString().trim();

                if(!nameInput.isEmpty() && isPhotoTaken) {
                    activateButtons();
					// Same thing here
					// You could just move the
					// setClickable inside the
					// activateButtons method I guess
                    btnCall.setClickable(true);
                    btnEmail.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                saveEditTextData();
            }
        });

        loadEditTextData();
        updateViews();
    }

    private void clickEmailPage() {
        Log.d(TAG, "clickEmailPage");

        Intent intent = new Intent("android.intent.action.EmailWPActivity");

        startActivity(intent);
    }

    private void clickCallPage() {
        Log.d(TAG, "clickCallPage");

        Intent intent = new Intent("android.intent.action.CallWPActivity");

        startActivity(intent);
    }

    //BUTTON DISABLE
    private void disableButtons() {
        btnEmail.setTextColor(Color.parseColor("#757c87"));
        btnCall.setTextColor(Color.parseColor("#757c87"));
    }

    private void activateButtons() {
        btnEmail.setTextColor(Color.parseColor("#000000"));
        btnCall.setTextColor(Color.parseColor("#000000"));
    }

    //CAMERA
    private void clickOpenCamera() {
        Log.d(TAG, "clickOpenCamera");

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(TAG, ex.getMessage());
            }
            // Continue only if the File was successfully created
            try {
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "Android/data/com.example.assignment1/files/Pictures",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

					// As far as I know you need camera permissions
					// for starting the camere app using e.g.
					// ActivityCompat.requestPermissions(this,
					// new String[]{ Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE },
					// PERMISSIONS_MULTIPLE_REQUEST);
					// External storage is included in these permissions
					// You might as well ask for that as well since
					// on succesful activity result you're gonna need it
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            } catch (Exception ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private File createImageFile() throws IOException {
        //Create an image file name
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        //Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Toast.makeText(this, "Picture was successfully taken", Toast.LENGTH_SHORT).show();
            isPhotoTaken = true;
        } else {
            Toast.makeText(this, "Picture was not successfully taken", Toast.LENGTH_SHORT).show();
        }
    }

    //SHARED PERFS KEY-VALUE PAIR
    private void saveEditTextData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT, editTextName.getText().toString());

        editor.apply();
    }

    private void loadEditTextData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "");
    }

    private void updateViews() {
        editTextName.setText(text);
    }
}
