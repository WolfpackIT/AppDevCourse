package nl.wolfpackit.appdev12.contact;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import nl.wolfpackit.appdev12.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClickToContinueFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClickToContinueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClickToContinueFragment extends Fragment {
    private static final String ARG_PARAM1 = "label";

    private String label;

    private OnFragmentInteractionListener mListener;
    public ClickToContinueFragment(){
        // Required empty public constructor
    }

    public static ClickToContinueFragment newInstance(String label){
        ClickToContinueFragment fragment = new ClickToContinueFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, label);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            label = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_click_to_continue, container, false);
    }
    public void onViewCreated(View view, Bundle bundle){
        Button button = (Button) view.findViewById(R.id.continueButton);
        if(button!=null)
            button.setText(label);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                mListener.onFragmentInteraction();
            }
        });
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
        void onFragmentInteraction();
    }
}
