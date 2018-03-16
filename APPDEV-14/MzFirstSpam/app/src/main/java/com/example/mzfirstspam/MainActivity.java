package com.example.mzfirstspam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class MainActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    private boolean picTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        picTaken = false;
        Button mailb = (Button) findViewById(R.id.button);
        mailb.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.grey, null)); //without theme);
        Button callb = (Button) findViewById(R.id.button2);
        callb.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.grey, null)); //without theme);
    }

    public void mailWP(View view)  {
        EditText editText = (EditText) findViewById(R.id.editText);
        String content = editText.getText().toString();
    if (!picTaken || content.matches("")){
        //TODO error message
    } else {

        Intent intent = new Intent(this, MailActivity.class);
        intent.putExtra("picLoc",mCurrentPhotoPath);
        startActivity(intent);
    }
    }

    public void callWP(View view) {

        if (!picTaken){
            //TODO error message
        } else {

            Intent intent = new Intent(this, CallActivity.class);
            startActivity(intent);
        }
    }

    public void takePic(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File pic = null;
            try{
                pic = createImageFile();
            } catch (IOException ex){
                //Error occured sensible error message TODO
            }
            if (pic != null){
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        pic);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                picTaken = true;

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Button mailb = (Button) findViewById(R.id.button2);
            mailb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            Button callb = (Button) findViewById(R.id.button);
            callb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
