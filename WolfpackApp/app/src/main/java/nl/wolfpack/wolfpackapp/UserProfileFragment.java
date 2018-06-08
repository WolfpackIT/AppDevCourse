package nl.wolfpack.wolfpackapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class UserProfileFragment extends Fragment {

    //TODO: Implement birthday push to database

    private OnFragmentInteractionListener mListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private UserReference userData = new UserReference("", "", "", null, "", "", "", "", "Wolf");
    private TextView nameTextView;
    private TextView birthdateTextView;
    private TextView phoneNumberTextView;
    private TextView IBANTextView;
    private TextView allergiesTextView;
    private EditText birthdateEditText;
    private EditText phoneNumberEditText;
    private EditText IBANEditText;
    private EditText allergiesEditText;


    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        return view;
    }

    private void allowUserToEditProfile() {
        birthdateEditText.setVisibility(View.VISIBLE);
        phoneNumberEditText.setVisibility(View.VISIBLE);
        IBANEditText.setVisibility(View.VISIBLE);
        allergiesEditText.setVisibility(View.VISIBLE);

        birthdateTextView.setVisibility(View.INVISIBLE);
        phoneNumberTextView.setVisibility(View.INVISIBLE);
        IBANTextView.setVisibility(View.INVISIBLE);
        allergiesTextView.setVisibility(View.INVISIBLE);

        FloatingActionButton fab = getView().findViewById(R.id.fab_start_edit);
        FloatingActionButton fab2 = getView().findViewById(R.id.fab_finish_edit);
        fab.setVisibility(View.INVISIBLE);
        fab2.setVisibility(View.VISIBLE);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        retrieveUserInfoFromDatabase(mAuth.getCurrentUser().getUid());

        nameTextView = getView().findViewById(R.id.name_textview);
        birthdateTextView = getView().findViewById(R.id.birthdate_textview);
        phoneNumberTextView = getView().findViewById(R.id.phonenumber_textview);
        IBANTextView = getView().findViewById(R.id.iban_textview);
        allergiesTextView = getView().findViewById(R.id.allergies_textview);

        birthdateEditText = getView().findViewById(R.id.birthdate_edt);
        phoneNumberEditText = getView().findViewById(R.id.phone_edt);
        IBANEditText = getView().findViewById(R.id.iban_edt);
        allergiesEditText = getView().findViewById(R.id.allergies_edt);

        hideEditTexts();

        FloatingActionButton fab = getView().findViewById(R.id.fab_start_edit);
        FloatingActionButton fab2 = getView().findViewById(R.id.fab_finish_edit);
        fab2.setVisibility(View.INVISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allowUserToEditProfile();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finishProfileEditing();
            }
        });
    }

    private void finishProfileEditing() {
        hideEditTexts();
        FloatingActionButton fab = getView().findViewById(R.id.fab_start_edit);
        FloatingActionButton fab2 = getView().findViewById(R.id.fab_finish_edit);
        fab.setVisibility(View.VISIBLE);
        fab2.setVisibility(View.INVISIBLE);
        addChangesToDatabase();

        birthdateTextView.setVisibility(View.VISIBLE);
        phoneNumberTextView.setVisibility(View.VISIBLE);
        IBANTextView.setVisibility(View.VISIBLE);
        allergiesTextView.setVisibility(View.VISIBLE);
    }

    private void addChangesToDatabase() {
        DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
        rootReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("IBAN").setValue(IBANEditText.getText().toString());
                mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("allergies").setValue(allergiesEditText.getText().toString());
                mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("phoneNumber").setValue(phoneNumberEditText.getText().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        IBANTextView.setText(IBANEditText.getText().toString());
        phoneNumberTextView.setText(phoneNumberEditText.getText().toString());
        allergiesTextView.setText(allergiesEditText.getText().toString());
    }

    private void hideEditTexts() {
        birthdateEditText.setVisibility(View.INVISIBLE);
        phoneNumberEditText.setVisibility(View.INVISIBLE);
        IBANEditText.setVisibility(View.INVISIBLE);
        allergiesEditText.setVisibility(View.INVISIBLE);
    }


    private void retrieveUserInfoFromDatabase(final String userID) {
        DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userDbReference = rootReference.child("users");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                nameTextView.setText(dataSnapshot.child(userID).child("name").getValue(String.class));
                //birthdateTextView.setText(new SimpleDateFormat("MM/dd/yyyy").format(userData.birthDate));
                phoneNumberTextView.setText(dataSnapshot.child(userID).child("phoneNumber").getValue(String.class));
                IBANTextView.setText(dataSnapshot.child(userID).child("IBAN").getValue(String.class));
                allergiesTextView.setText(dataSnapshot.child(userID).child("allergies").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        userDbReference.addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
