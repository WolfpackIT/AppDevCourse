package nl.wolfpack.wolfpack.declarations;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.wolfpack.wolfpack.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeclarationSearchFragment extends Fragment {


    public DeclarationSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_declaration_search, container, false);
    }

}
