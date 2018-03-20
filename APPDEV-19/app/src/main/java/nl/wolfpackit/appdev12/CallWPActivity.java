package nl.wolfpackit.appdev12;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
        Uri number = Uri.parse("tel:040-782 0814");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }
}
