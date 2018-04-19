package com.example.wolfpackapp.StartUpFragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wolfpackapp.Database.EmpDB;
import com.example.wolfpackapp.Database.Employee;
import com.example.wolfpackapp.LoginActivity;
import com.example.wolfpackapp.MainActivity;
import com.example.wolfpackapp.MainActivityfragments.RSSRecycler;
import com.example.wolfpackapp.R;
import com.example.wolfpackapp.adminDB.Admin;
import com.example.wolfpackapp.adminDB.AdpDB;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;


public class LoginFragment extends Fragment {

    String NAME = "voornaam";
    String EMAIL = "email";

    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 5000;
    Boolean signedIn = false;
    String id;
    AdpDB ad;
    EmpDB db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        db = Room.databaseBuilder(getContext(),
                EmpDB.class, "Employee").build();
        ad = Room.databaseBuilder(getContext(),
                AdpDB.class, "Admin").build();
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
                EditText email = (EditText) getActivity().findViewById(R.id.edLoginEmail);
                EditText pwd = (EditText) getActivity().findViewById(R.id.edLoginPassword);
                if (email.getText().toString().matches("")){
                    signIn();
                } else if (!email.getText().toString().matches("") && pwd.getText().toString().matches("")){
                    Snackbar pass = Snackbar.make(getView(), "please fill in a password, or delete the email addreses", 1500);
                } else {
                    if(email.getText().toString().contains("@") &&(email.getText().toString().contains(".")) ){
                        Snackbar input = Snackbar.make(getView(), "please fill in a valid email address", 1500);
                    } else {
                        Log.d("login", "please nicely input this. ");
                        //TODO allow alternative input and store in shared memory, also session id.
                        //TODO change login button
                    }
                }


                if (signedIn) {
                    if (db.EmpDAO().uidList(id).isEmpty()) {
                        // start service to update database, but let person go on
//                        Intent serv = new Intent(getApplicationContext(),);
//                        serv.putExtra("googleID", id);
//                        startService(serv);
                    }

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

            x.setUid(gID);
            x.setEmail(account.getEmail());
            x.setFirstName(account.getDisplayName());
            x.setLastName(account.getFamilyName());

            new updateDatabase().execute(x);

//            if (!db.EmpDAO().uidList(gID).isEmpty()) {
//                Log.d("Loginclass", "Possible error inserting in database");
//            }

            signedIn = true;
            Log.d("sso","signed in");

//            if(ad.AdDAO().findByName("martijn.ras96@gmail.com") != null){
//                Log.d("sso admin", "found: ");
//            }
            Log.d("sso admin", "nothing ");
            SharedPreferences sharedpref = getActivity().getPreferences(Context.MODE_PRIVATE);
            String email = sharedpref.getString(EMAIL, "email");
            String name = sharedpref.getString(NAME, "username");
            Log.d("sso Miail",""+email);
            Log.d("sso name",""+name);


            SharedPreferences edit = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = edit.edit();
            editor.putString(EMAIL, String.valueOf(account.getEmail()));
            editor.commit();


            SharedPreferences edit2 = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor2 = edit2.edit();
            editor2.putString(NAME, String.valueOf(account.getDisplayName()));
            editor2.commit();
            // Signed in successfully, show authenticated UI.
//            updateUI(account);
            getActivity().setContentView(R.layout.activity_main);
            MainFragment firstFragment = new MainFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.mainA, firstFragment).commit();
            Log.d("sso", "done logging in");

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
                        Log.d("data", "updated database");

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

    private class verifyAdmin extends AsyncTask<String, Integer, Long> {
        String TAGB = "loginbackground";

        @Override
        protected Long doInBackground(String... strings) {
            if (ad.AdDAO().findByName(strings[0]) != null)  {
                Snackbar sn = Snackbar.make(getView(),"welcome admin", 1000 );
                sn.show();
                Log.d("sso admin", "admin logged in");
                return (long) 1;
            }
            return (long) 0;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            Log.d("sso admin", "admin logged in");
            Log.d("sso admin", "admin logged in");
            if (aLong > 0){

                Log.d("sso ad", "onPostExecute: succes ");
            }
        }
    }
}

