package com.example.kath.appdev6kath;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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


public class ContactWolfpackFragment extends Fragment {
    private static final String TAG = "ContactWolfpackFragment" ;
    private static final String CHANNEL_ID = "PICTURE_TAKEN_NOTIFICATION_CHANNEL";
    Button mBtnCall;
    Button mBtnEmail;
    Button mBtnCamera;
    EditText mEditTextName;

    String name;

    boolean isNameWritten = false;
    boolean isPhotoTaken = false;

    //Camera Variables
    private static final int REQUEST_TAKE_IMAGE = 1;
    private String mCurrentPhotoPath;

    private NotificationCompat.Builder mBuilder;



    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView : Starting");
        View v = inflater.inflate(R.layout.fragment_contact_wolfpack, container, false);

        mEditTextName = (EditText)v.findViewById(R.id.editText_name);
        name = mEditTextName.getText().toString();

        if(name != ""){
            isNameWritten = true;
        }

        //SAVE KEY VALUE
        mEditTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String nameInput = mEditTextName.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                saveEditTextData();
            }
        });

        loadEditTextData();
        updateViews();

        mBtnCall = (Button)v.findViewById(R.id.button_callWP);
        mBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CallWPActivity.class);
                startActivity(intent);
            }
        });

        mBtnEmail = (Button)v.findViewById(R.id.button_emailWP);
        mBtnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EmailWPActivity.class);
                startActivity(intent);
            }
        });

        mBtnCamera = (Button)v.findViewById(R.id.button_camera);
        mBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClickCamera");
                Intent imageI = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (imageI.resolveActivity(getActivity().getPackageManager()) != null) {
                    File imageFile = null;
                    try {
                        imageFile = CreateTheImage();
                        if (imageFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(getActivity(),
                                    "Android/data/com.example.kath.appdev5kath/files/Pictures",
                                    imageFile);
							imageI.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
							
							// You need to ask for permission
							// to use the camera first.
                            startActivityForResult(imageI, REQUEST_TAKE_IMAGE);
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, ex.getMessage());
                    }
                }
            }
        });



        return v;
    }

    private File CreateTheImage() throws IOException {
        //Create an image file name
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		
		// You need to ask for permission to write to external storage first.
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        //Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //FEEDBACK
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_IMAGE && resultCode == getActivity().RESULT_OK) {
            Toast.makeText(getActivity(), "Picture was successfully taken", Toast.LENGTH_SHORT).show();
            isPhotoTaken = true;
            showNotification();

            if(isPhotoTaken && isNameWritten){
                mBtnCall.setEnabled(true);
                mBtnEmail.setEnabled(true);
            }
        } else {
            Toast.makeText(getActivity(), "Picture was not successfully taken", Toast.LENGTH_SHORT).show();
        }
    }

    String text;
    private void saveEditTextData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("SHARED_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TEXT_NAME", mEditTextName.getText().toString());
        editor.apply();
    }

    private void loadEditTextData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("SHARED_NAME", Context.MODE_PRIVATE);
        text = sharedPreferences.getString("TEXT_NAME", "");
    }

    private void updateViews(){
        mEditTextName.setText(text);
    }

    //NOTIFICATION
    public void setNotification(){
        mBuilder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_smile)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

    }

    public void showNotification() {
        setNotification();
        if(mBuilder != null) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());
            notificationManagerCompat.notify(1, mBuilder.build());
        } else {
            Log.e(TAG, "mBuilder is null");
        }

    }





}
