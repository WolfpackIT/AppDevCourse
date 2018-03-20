package nl.wolfpackit.appdev12;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;

public class EmailWPActivity extends AppCompatActivity implements ClickToContinueFragment.OnFragmentInteractionListener{
    private static int emailRequestCode = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ClickToContinueFragment fragment = ClickToContinueFragment.newInstance("mail now");
        fragmentTransaction.add(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }

    public void onFragmentInteraction(){
        Intent thisIntent = getIntent();
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"rene.le.clercq@wolfpackit.nl"});
        i.putExtra(Intent.EXTRA_SUBJECT, "HELLO WORLD");
        i.putExtra(Intent.EXTRA_TEXT   , "I did it, greetings Tar");

        //attach the picture
        File file = new File(thisIntent.getStringExtra("imagePath"));
        Uri uri = FileProvider.getUriForFile(this,
                "nl.wolfpackit.appdev12",
                file);
        i.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivityForResult(Intent.createChooser(i, "Send mail..."), emailRequestCode);
        } catch (android.content.ActivityNotFoundException ex) {}
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==emailRequestCode){
            if(resultCode==0){ //success
                MainActivity.createNotification(MainActivity.getStringByID(R.string.mailSuccessNotifcationTitle),
                                                MainActivity.getStringByID(R.string.mailSuccessNotifcationBody));
            }else{
                MainActivity.createNotification(MainActivity.getStringByID(R.string.mailFailNotifcationTitle),
                                                MainActivity.getStringByID(R.string.mailFailNotifcationBody));
            }
        }
    }
}
