package nl.wolfpackit.appdev12;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
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

public class ContactFragment extends android.support.v4.app.Fragment{
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }


    public void onAttach(Context context) {
        super.onAttach(context);
    }
    public void onViewCreated(View view, Bundle bundle){
        mailButton = view.findViewById(R.id.emailButton);
        callButton = view.findViewById(R.id.callButton);
        pictureButton = view.findViewById(R.id.pictureButton);

        mailButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){ emailWP(v); }
        });
        callButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){ callWP(v); }
        });
        pictureButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){ takePicture(v); }
        });

        nameTextBox = view.findViewById(R.id.nameTextBox);
        nameTextBox.addTextChangedListener(new TextWatcher(){
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            public void afterTextChanged(Editable s) {
                nameChange(s.toString());
            }
        });

        mPrefs = getActivity().getSharedPreferences("prefs", 0);
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
            Intent intent = new Intent(this.getActivity(), EmailWPActivity.class);
            intent.putExtra("imagePath", imagePath);
            startActivity(intent);
        }else{
            Toast.makeText(this.getActivity(), getString(R.string.buttonDisabled), Toast.LENGTH_SHORT).show();
        }
    }
    public void callWP(View view){
        if(isEnabled(mailButton)){
            Intent intent = new Intent(this.getActivity(), CallWPActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this.getActivity(), getString(R.string.buttonDisabled), Toast.LENGTH_SHORT).show();
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

        if (takePictureIntent.resolveActivity(this.getActivity().getPackageManager()) != null) {
            try {
                File storageDir = this.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File photoFile = new File(storageDir, imageName);

                // Save a file: path for use with ACTION_VIEW intents
                imagePath = photoFile.getAbsolutePath();

                Uri photoURI = FileProvider.getUriForFile(this.getActivity(),
                        "nl.wolfpackit.appdev12",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (Exception ex) {
                Toast.makeText(this.getActivity(), "Something went wrong while trying to take a picture", Toast.LENGTH_SHORT).show();
                Log.w("APPDEV12", ex.toString());
            }
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == this.getActivity().RESULT_OK){
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
