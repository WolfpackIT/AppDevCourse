package nl.wolfpackit.wolfpack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import nl.wolfpackit.wolfpack.R;
import nl.wolfpackit.wolfpack.database.Account;
import nl.wolfpackit.wolfpack.database.Database;
import nl.wolfpackit.wolfpack.modules.BaseActivity;

public class SigninActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.w("DETECT THIS", "Activity created");
        setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Database.createDatabase(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        boolean signout = getIntent().getBooleanExtra("signout", false);

        if(signout){
            mGoogleSignInClient.revokeAccess()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        public void onComplete(Task<Void> task){
                            //refresh activity to now signin again
                            startActivity(new Intent(SigninActivity.this, SigninActivity.class));
                        }
                    });
        }else{
            signIn();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
//        Log.w("DETECT THIS", "started signin");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        Log.w("DETECT THIS", "logging in");
        if(requestCode == RC_SIGN_IN){
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
//            Log.w("DETECT THIS", "logged in");
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Account.createAccount(account, new Runnable(){
                public void run(){
//                    Log.w("DETECT THIS", "account created");
                    startActivity(new Intent(SigninActivity.this, BaseActivity.class));
                }
            });
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TEST", "signInResult:failed code=" + e.getStatusCode());
            if(e.getStatusCode()==12500){
                Toast.makeText(this, "Please update your google play services", Toast.LENGTH_SHORT).show();
            }else{
                signIn();
            }
        }
    }
}
