package com.example.mzfirstspam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;



public class MainFragment extends Fragment {
    static final int REQUEST_TAKE_PHOTO = 1;
    private boolean picTaken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        picTaken = false;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;

    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button mailb = (Button) getView().findViewById(R.id.button);
        mailb.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.grey, null)); //without theme);
        mailb.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mailWP(view);
            }
        });
        Button callb = (Button) getView().findViewById(R.id.button2);
        callb.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.grey, null)); //without theme);
        callb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                callWP(view);
            }
        });
        Button picbtn = (Button) getView().findViewById(R.id.button3);
        picbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                takePic(view);
            }
        });
    }

    public void mailWP(View view) {
        EditText editText = (EditText) getView().findViewById(R.id.editText);
        String content = editText.getText().toString();
        if (!picTaken || content.matches("")) {
            //TODO error message
            Snackbar snackbar = Snackbar.make(view, "Not supported", 1000);
            snackbar.show();
        } else {

            Intent intent = new Intent(getActivity(), MailActivity.class);
            intent.putExtra("picLoc", mCurrentPhotoPath);
            startActivity(intent);
        }
    }

    public void callWP(View view) {

        if (!picTaken) {
            //TODO error message
            Snackbar snackbar = Snackbar.make(view, "Not supported", 1000);
            snackbar.show();
        } else {

            Intent intent = new Intent(getActivity(), CallActivity.class);
            startActivity(intent);
        }
    }

    public void takePic(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            File pic = null;
            try {
                pic = createImageFile();
            } catch (IOException ex) {
                //Error occured sensible error message TODO
            }
            if (pic != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.fileprovider",
                        pic);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                picTaken = true;

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Button mailb = (Button) getView().findViewById(R.id.button);
            mailb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            Button callb = (Button) getView().findViewById(R.id.button2);
            callb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
