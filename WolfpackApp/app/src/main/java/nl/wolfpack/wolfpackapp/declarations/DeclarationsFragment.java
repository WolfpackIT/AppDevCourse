package nl.wolfpack.wolfpackapp.declarations;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

import nl.wolfpack.wolfpackapp.R;
import nl.wolfpack.wolfpackapp.declarations.add.AddDeclarationActivity;

public class DeclarationsFragment extends Fragment {

    private FloatingActionButton floatingActioButtonAddDeclaration;

    public DeclarationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_declarations, container, false);
        floatingActioButtonAddDeclaration = (FloatingActionButton) view.findViewById(R.id.floatingActioButtonAddDeclaration);
        floatingActioButtonAddDeclaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNewDeclarationIntent = new Intent(getContext(), AddDeclarationActivity.class);
                startActivity(addNewDeclarationIntent);
            }
        });
        return view;
    }

}
