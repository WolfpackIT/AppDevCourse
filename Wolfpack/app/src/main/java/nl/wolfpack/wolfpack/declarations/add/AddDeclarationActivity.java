package nl.wolfpack.wolfpack.declarations.add;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ToggleButton;

import nl.wolfpack.wolfpack.R;

public class AddDeclarationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.declarations));
        setContentView(R.layout.activity_add_declaration);


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
