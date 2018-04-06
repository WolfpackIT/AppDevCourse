package nl.wolfpack.wolfpackapp.declarations.add;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import nl.wolfpack.wolfpackapp.R;

public class AddDeclarationActivity extends AppCompatActivity {

    FragmentTransaction fragmentTransaction;
    GeneralDeclarationFragment generalDeclarationFragment;
    KilometerDeclarationFragment kilometerDeclarationFragment;

    private Button buttonGeneral;
    private Button buttonKilometers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.declarations));
        setContentView(R.layout.activity_add_declaration);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        generalDeclarationFragment = new GeneralDeclarationFragment();
        fragmentTransaction.replace(R.id.frameLayoutFragmentDeclarationType, generalDeclarationFragment).commit();

        kilometerDeclarationFragment = new KilometerDeclarationFragment();

        buttonGeneral = (Button) findViewById(R.id.buttonGeneral);
        buttonGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutFragmentDeclarationType, generalDeclarationFragment).commit();
            }
        });

        buttonKilometers = (Button) findViewById(R.id.buttonKilometers);
        buttonKilometers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutFragmentDeclarationType, kilometerDeclarationFragment).commit();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle back button press
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
