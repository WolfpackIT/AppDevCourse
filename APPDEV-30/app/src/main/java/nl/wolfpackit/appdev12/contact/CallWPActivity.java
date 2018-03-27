package nl.wolfpackit.appdev12.contact;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nl.wolfpackit.appdev12.MainActivity;
import nl.wolfpackit.appdev12.R;

public class CallWPActivity extends AppCompatActivity implements ClickToContinueFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ClickToContinueFragment fragment = ClickToContinueFragment.newInstance("call now");
        fragmentTransaction.add(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }

    public void onFragmentInteraction(){
        Uri number = Uri.parse("tel:"+getString(R.string.phoneNumber));
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }
}
