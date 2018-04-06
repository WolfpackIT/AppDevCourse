package nl.wolfpack.wolfpack.declarations.add;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import nl.wolfpack.wolfpack.R;


public class GeneralDeclarationFragment extends Fragment {

    private ToggleButton toggleButtonBTW;

    public GeneralDeclarationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general_declaration, container, false);
        toggleButtonBTW = view.findViewById(R.id.toggleButtonBTW);
        toggleButtonBTW.setChecked(true);
        return view;
    }

}
