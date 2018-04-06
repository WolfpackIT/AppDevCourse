package nl.wolfpack.wolfpackapp.declarations.add;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.wolfpack.wolfpackapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class KilometerDeclarationFragment extends Fragment {


    public KilometerDeclarationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kilometer_declaration, container, false);
    }

}
