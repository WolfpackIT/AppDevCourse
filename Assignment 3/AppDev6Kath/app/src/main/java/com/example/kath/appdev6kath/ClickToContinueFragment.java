package com.example.kath.appdev6kath;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ClickToContinueFragment extends Fragment {
   private static final String TAG = "ClickToContinueFragment";

   public String btnName;
   private Button btnPlaceholder;

   @Override
   public View onCreateView( LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

       View v = inflater.inflate(R.layout.fragment_click_to_continue, container, false);

       btnPlaceholder = v.findViewById(R.id.button_placeholder);
       btnPlaceholder.setText(btnName);

       if(btnName.contains("Mail")){
           btnPlaceholder.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   goToMailFragment();
                   Log.d(TAG, "goToMailFragment");
               }
           });
       }else{
           btnPlaceholder.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   goToCallFragment();
                   Log.d(TAG, "goToCallFragment");
               }
           });
       }

       return v;

   }
    private void goToMailFragment() {
       Log.d(TAG, "Inside goToMailFragment");
       Intent mailIntent = new Intent(Intent.ACTION_SEND);
       mailIntent.setType("text/plain");
       mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rene.le.clercq@wolfpackit.nl"});
       mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello World!");
       mailIntent.putExtra(Intent.EXTRA_TEXT, "I did it! Greetings, Kath");

       startActivity(Intent.createChooser(mailIntent, "Email:"));
    }

    private void goToCallFragment() {
        Log.d(TAG, "Inside goToCallFragment");

        Uri phoneNumber = Uri.parse("tel:040 782 0814");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, phoneNumber);

        startActivity(callIntent);
    }


}
