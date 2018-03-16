package nl.wolfpack.emailwolfpack;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_CAMERA = 100;
    private static final int CAMERA_REQUEST = 101;

    private String name = "";
    private Bitmap photo;

    private Button emailButton;
    private Button callButton;

    private SharedPreferences sharedPreferences;
    private final String MY_PREFRENCES = "MY_PREFRENCES";
    private final String MY_PREFERENCES_NAME = "MY_PREFRENCES_NAME";
    private SharedPreferences.Editor myPrefrencesEditor;

    public static final String IMAGE_DIR = "Wolfpack";

    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callButton = (Button) findViewById(R.id.buttonCall);
        callButton.setEnabled(false);

        emailButton = (Button) findViewById(R.id.buttonEmail);
        emailButton.setEnabled(false);

        sharedPreferences = getSharedPreferences(MY_PREFRENCES, Context.MODE_PRIVATE);

        String sharedPrefrencesName = sharedPreferences.getString(MY_PREFERENCES_NAME, "");
        name = sharedPrefrencesName;

        TextView nameEditText = (TextView) findViewById(R.id.editTextName);
        nameEditText.setText(sharedPrefrencesName);

        myPrefrencesEditor = sharedPreferences.edit();

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name = charSequence.toString();

                if(charSequence.length() > 0 && photo != null) {
                    emailButton.setEnabled(true);
                } else {
                    emailButton.setEnabled(false);
                }

                myPrefrencesEditor.putString(MY_PREFERENCES_NAME, charSequence.toString());
                myPrefrencesEditor.apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public void onClickCall(View view) {
        Intent intent = new Intent(this, CallWPActivity.class);
        startActivity(intent);
    }

    public void onClickEmail(View view) {
        Intent intent = new Intent(this, EmailWPActivity.class);
        intent.putExtra("photoPath", mCurrentPhotoPath);
        startActivity(intent);
    }

    private void startCameraActivity() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("Camera", ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "Android/data/nl.wolfpack.emailwolfpack/files/Pictures",
                        photoFile);
                Log.d("photoURI", photoURI.toString());
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }
    }

    public void onClickCamera(View view) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
        } else {
            startCameraActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCameraActivity();
                } else {
                    Toast.makeText(getApplicationContext(), "We need the camera permission mijn maneer/vrouw..", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default:
                break;
        }
    }

    public File createImageFile() throws IOException {
        String imageFileName = "wolfpack";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            if(name.length() > 0) {
                emailButton.setEnabled(true);
            }
            Toast.makeText(this, "Picture taken!", Toast.LENGTH_SHORT).show();
        }
    }
}
