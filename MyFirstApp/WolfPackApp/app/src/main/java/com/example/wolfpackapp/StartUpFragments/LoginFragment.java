package com.example.wolfpackapp.StartUpFragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wolfpackapp.Database.EmpDB;
import com.example.wolfpackapp.Database.Employee;
import com.example.wolfpackapp.LoginActivity;
import com.example.wolfpackapp.MainActivity;
import com.example.wolfpackapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


public class LoginFragment extends Fragment {

    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 5000;
    Boolean signedIn = false;
    String id;

    EmpDB db = Room.databaseBuilder(getActivity(),
            EmpDB.class, "Employee").build();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

// Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        Button btn = (Button) getActivity().findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                signIn();
                if (signedIn) {
                    if (db.EmpDAO().uidList(id).isEmpty()) {
                        // start service to update database, but let person go on
//                        Intent serv = new Intent(getApplicationContext(),);
//                        serv.putExtra("googleID", id);
//                        startService(serv);
                    }
                    Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

            }
        });
        return  view;
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

            x.setUid(gID);
            x.setEmail(account.getEmail());
            x.setFirstName(account.getDisplayName());
            x.setLastName(account.getFamilyName());

            new updateDatabase().execute(x);

            if (!db.EmpDAO().uidList(gID).isEmpty()) {
                Log.d("Loginclass", "Possible error inserting in database");
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

    private class updateDatabase extends AsyncTask<Employee, Integer, Long> {
        String TAGB = "loginbackground";

        @Override
        protected Long doInBackground(Employee... employees) {
            for (int i = 0; i < employees.length; i++) {
                if (!employees[i].getUid().isEmpty()) {
                    if(db.EmpDAO().uidList(employees[i].getUid()).isEmpty()){
                        db.EmpDAO().insert(employees[i]);
                        return (long) 1;
                    }
                }
            }
            return (long) 0;
        }

        protected void onProgressUpdate(Integer... progress) {
            Log.d(TAGB, "updating database");
        }

        protected void onPostExecute(Long result) {
            Log.d(TAGB, "Downloaded " + result + " bytes");
        }


    }
}

