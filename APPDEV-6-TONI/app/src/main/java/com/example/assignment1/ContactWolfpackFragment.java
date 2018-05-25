package com.example.assignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Wolfpack on 5/24/2018.
 */

public class ContactWolfpackFragment extends Fragment {
    private static final String TAG = "ContactWolfpackFragment";

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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        Log.d(TAG, "CREATED: ContactWolfpackFragment");

        btnEmail = (Button) view.findViewById(R.id.btnEmailWP);
        btnCall = (Button) view.findViewById(R.id.btnCallWP);
        btnPicture = (Button) view.findViewById(R.id.btnTakePicture);
        editTextName = (EditText) view.findViewById(R.id.editTextName);

        //Disable the call and mail buttons
        disableButtons();

        //BUTTONS//
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!btnEmail.isClickable()) {
                    Toast.makeText(getActivity(), "Email button is disabled. Please check if picture taken and filled in name", Toast.LENGTH_LONG).show();
                } else {
                    clickEmailPage();
                }

            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!btnCall.isClickable()) {
                    Toast.makeText(getActivity(), "Call button is disabled. Please check if picture taken and filled in name", Toast.LENGTH_LONG).show();
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

        return view;
    }

    private void clickEmailPage() {
        Log.d(TAG, "clickEmailPage");

        Intent intent = new Intent(getActivity(), EmailWPActivity.class);

        startActivity(intent);
    }

    private void clickCallPage() {
        Log.d(TAG, "clickCallPage");

        Intent intent = new Intent(getActivity(), CallWPActivity.class);

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

        if (takePictureIntent.resolveActivity(this.getActivity().getPackageManager()) != null) {
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
                    Uri photoURI = FileProvider.getUriForFile(getActivity(),
                            "Android/data/com.example.assignment1/files/Pictures",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            } catch (Exception ex) {
                Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private File createImageFile() throws IOException {
        //Create an image file name
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = this.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        //Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Toast.makeText(getActivity(), "Picture was successfully taken", Toast.LENGTH_SHORT).show();
            isPhotoTaken = true;
        } else {
            Toast.makeText(getActivity(), "Picture was not successfully taken", Toast.LENGTH_SHORT).show();
        }
    }

    //SHARED PERFS KEY-VALUE PAIR
    private void saveEditTextData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT, editTextName.getText().toString());

        editor.apply();
    }

    private void loadEditTextData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "");
    }

    private void updateViews() {
        editTextName.setText(text);
    }
}
