package nl.wolfpackit.callemailapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class shoutFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private OnFragmentInteractionListener mListener;
    private ShoutAdapter shoutAdapter;
    private List<String> myDataset = new ArrayList<String>();

    public shoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myDataset = populateShoutDataset();
        View view =  inflater.inflate(R.layout.fragment_shout, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        shoutAdapter = new ShoutAdapter(myDataset);
        mRecyclerView.setAdapter(shoutAdapter);
        return view;
    }

    private List<String> populateShoutDataset() {
        ArrayList<String> dataset = new ArrayList<>();
        dataset.add("lalala");
        dataset.add("test");
        dataset.add("lipsadecreativitate");
        dataset.add(":)");
        dataset.add(":+1:");
        return dataset;
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
