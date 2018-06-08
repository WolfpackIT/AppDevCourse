package nl.wolfpack.wolfpackapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Locale;

public class PostLoginActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, UserProfileFragment.OnFragmentInteractionListener, HomePageFragment.OnFragmentInteractionListener {

    private DrawerLayout mDrawerLayout;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private int INTERNET_PERMISSION_REQUEST_CODE = 1661;
    private final Uri slackURI = Uri.parse("slack://channel?team={T03CWKJRV}&id={C1LF9Q8SX}"); // Could not add it to resources
    private final Uri wikiURI = Uri.parse("https://wiki.wolfpackit.nl");
    private String requestedCommand = "";
    private ImageView profilePictureView;
    private TextView fullNameView;
    private TextView emailView;
    private TextView roleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.inflateHeaderView(R.layout.nav_header);

        profilePictureView = hView.findViewById(R.id.imageViewProfilePicture);
        fullNameView = hView.findViewById(R.id.textViewFullName);
        emailView = hView.findViewById(R.id.textViewEmail);
        roleView = findViewById(R.id.textViewRole);

        if(user != null) {
            profilePictureView.setImageDrawable(null);
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {
                        final Bitmap bmp = getImageBitmap(user.getPhotoUrl().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                profilePictureView.setImageBitmap(bmp);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            fullNameView.setText(user.getDisplayName());
            emailView.setText(user.getEmail());
            updateRoleTextView();
        }
    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("bmp", "Error getting bitmap", e);
        }
        return bm;
    }


    private void updateRoleTextView() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roleText = dataSnapshot.child("users").child(mAuth.getCurrentUser().getUid()).child("role").getValue(String.class);
                roleView.setText(roleText);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("databaseError", "Error reading from database");
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (id == R.id.home) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new HomePageFragment()).commit();
        }
        else if (id == R.id.declarations) {
            //fragmentManager.beginTransaction().replace(R.id.content_frame, new DeclarationsFragment()).commit();
        }
        else if (id == R.id.order_food) {
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) != Calendar.TUESDAY) {
                Toast.makeText(getApplicationContext(), R.string.order_food_toast, Toast.LENGTH_SHORT).show();
            }
            else {
                slackTrigger();
            }
        }
        else if (id == R.id.Wiki) {
            openWiki();
        }
        else if (id == R.id.profile) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new UserProfileFragment()).commit();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openWiki() {
        requestedCommand = "WIKI";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, wikiURI);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, INTERNET_PERMISSION_REQUEST_CODE);
        }
        else {
            if (browserIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(browserIntent);
            }
        }
    }

    private void slackTrigger() {
        requestedCommand = "SLACK";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, slackURI);
        // Check if there is any app that can handle this
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, INTERNET_PERMISSION_REQUEST_CODE);
        }
        else {
            if (browserIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(browserIntent);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Intent browserIntent = null;
        if (requestedCommand == "SLACK") {
            browserIntent = new Intent(Intent.ACTION_VIEW, slackURI);
        }
        else if (requestedCommand == "WIKI") {
            browserIntent = new Intent(Intent.ACTION_VIEW, wikiURI);
        }
        if (requestCode == INTERNET_PERMISSION_REQUEST_CODE) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (browserIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(browserIntent);
                }
                else {
                    Toast.makeText(this, R.string.no_internet_app, Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, R.string.internet_permission_request, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.basic_action_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.switch_language_action) {
            switchLanguage();
            return true;
        } else if(id == R.id.signout_action) {
            userLogOff();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchLanguage() {
        // Check the default language on the phone. If it is dutch, give the user the option to switch to english.
        if (LocaleManager.getLanguage().contentEquals("Nederlands")) {
            LocaleManager.setLocale(getApplicationContext(), new Locale("en"));
        }
        else {
            LocaleManager.setLocale(getApplicationContext(), new Locale("nl", "NL"));
        }
        recreate();
    }

    private void userLogOff() {
        mAuth.signOut();
        Intent loginRedirect = new Intent(this, LoginActivity.class);
        startActivity(loginRedirect);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
