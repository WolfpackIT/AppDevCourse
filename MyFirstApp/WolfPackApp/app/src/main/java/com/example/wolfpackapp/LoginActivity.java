package com.example.wolfpackapp;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.wolfpackapp.Database.EmpDB;
import com.example.wolfpackapp.Database.Employee;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 5000;
    Boolean signedIn = false;
    String id;

    EmpDB db = Room.databaseBuilder(this,
            EmpDB.class, "Employee").build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

// Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Button btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                signIn();
                if ( signedIn) {
                    if (db.EmpDAO().uidList(id).isEmpty()){
                        // start service to update database, but let person go on
//                        Intent serv = new Intent(getApplicationContext(),);
//                        serv.putExtra("googleID", id);
//                        startService(serv);
                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    private void signIn() {
        //TODO get google api token or whatever
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String gID = account.getId();
            id = account.getId();
            Employee x = new Employee();
            if (db.EmpDAO().uidList(gID).isEmpty()){
                x.setUid(gID);
                x.setEmail(account.getEmail());
                x.setFirstName(account.getDisplayName());
                x.setLastName(account.getFamilyName());
                db.EmpDAO().insert(x);
            }
            if( !db.EmpDAO().uidList(gID).isEmpty()){
                Log.d("Loginclass","Possible error inserting in database");
            }

            signedIn = true;
            // Signed in successfully, show authenticated UI.
//            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("LoginClass", "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }
}
