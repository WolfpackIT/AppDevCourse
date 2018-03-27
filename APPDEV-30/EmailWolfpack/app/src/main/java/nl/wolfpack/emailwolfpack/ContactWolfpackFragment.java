package nl.wolfpack.emailwolfpack;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class ContactWolfpackFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private static final int PERMISSIONS_REQUEST_CAMERA = 100;
    private static final int CAMERA_REQUEST = 101;

    private String name = "";
    private Bitmap photo;

    private Button emailButton;
    private Button callButton;
    private Button takePictureButton;

    private SharedPreferences sharedPreferences;
    private final String MY_PREFRENCES = "MY_PREFRENCES";
    private final String MY_PREFERENCES_NAME = "MY_PREFRENCES_NAME";
    private SharedPreferences.Editor myPrefrencesEditor;

    public static final String IMAGE_DIR = "Wolfpack";

    private String mCurrentPhotoPath;

    public ContactWolfpackFragment() {
        // Required empty public constructor
    }
    public static ContactWolfpackFragment newInstance(String param1, String param2) {
        ContactWolfpackFragment fragment = new ContactWolfpackFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_wolfpack, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        callButton = (Button) view.findViewById(R.id.buttonCall);
        callButton.setEnabled(false);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CallWPActivity.class);
                startActivity(intent);
            }
        });

        emailButton = (Button) view.findViewById(R.id.buttonEmail);
        emailButton.setEnabled(false);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EmailWPActivity.class);
                intent.putExtra("photoPath", mCurrentPhotoPath);
                startActivity(intent);
            }
        });


        takePictureButton = (Button) view.findViewById(R.id.buttonTakePicture);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
                } else {
                    startCameraActivity();
                }
            }
        });

        sharedPreferences = this.getActivity().getSharedPreferences(MY_PREFRENCES, Context.MODE_PRIVATE);

        String sharedPrefrencesName = sharedPreferences.getString(MY_PREFERENCES_NAME, "");
        name = sharedPrefrencesName;

        TextView nameEditText = (TextView) view.findViewById(R.id.editTextName);
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
                    callButton.setEnabled(true);
                } else {
                    emailButton.setEnabled(false);
                    callButton.setEnabled(false);

                }

                myPrefrencesEditor.putString(MY_PREFERENCES_NAME, charSequence.toString());
                myPrefrencesEditor.apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void startCameraActivity() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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
                Uri photoURI = FileProvider.getUriForFile(getContext(), "Android/data/nl.wolfpack.emailwolfpack/files/Pictures",
                        photoFile);
                Log.d("photoURI", photoURI.toString());
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCameraActivity();
                } else {
                    Toast.makeText(getContext().getApplicationContext(), "We need the camera permission mijn maneer/vrouw..", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default:
                break;
        }
    }

    public File createImageFile() throws IOException {
        String imageFileName = "wolfpack";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            if(name.length() > 0) {
                emailButton.setEnabled(true);
                callButton.setEnabled(true);
            }
            Toast.makeText(getContext(), "Picture taken!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
