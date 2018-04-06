package nl.wolfpack.wolfpack;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class LoginActivity extends AppCompatActivity implements GoogleSignInFragment.OnFragmentInteractionListener {
    private GoogleSignInAccount account;
    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account != null) {
            Intent toMainActivityIntent = new Intent(this, MainActivity.class);
            toMainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(toMainActivityIntent);
        }

        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
    }

    public void setEditTextEmail(String email) {
        editTextEmail.setText(email);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
