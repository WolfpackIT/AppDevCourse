package com.example.kath.appdev_4_kath;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
       mailIntent.putExtra(Intent.EXTRA_EMAIL, "rene.le.clercq@wolfpackit.nl");
       mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello World!");
       mailIntent.putExtra(Intent.EXTRA_TEXT, "I did it! Greetings, Kath");

		// If there is no specific
		// activity to handle the intent,
		// The below call will result in an exception.
		// Now, for calls this is most likely no issue,
		// but I'd check this anyway to be sure using
		// (mailIntent.resolveActivity(getPackageManager()) != null)
		// The same holds for the the call intent below. 
       startActivity(mailIntent);
    }

    private void goToCallFragment() {
        Log.d(TAG, "Inside goToCallFragment");

        Uri phoneNumber = Uri.parse("tel:040 782 0814");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, phoneNumber);
		// Formally you would need to have
		// the permission android.Manifest.permission.CALL_PHONE
		// granted by the time you start the intent

		// In reference to this, I also placed a comment
		// in the manifest, in which you would have to
		// add that permission as well.
        startActivity(callIntent);
    }


}
