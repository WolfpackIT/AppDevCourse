package nl.wolfpackit.appdev12.shout;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.wolfpackit.appdev12.MainActivity;
import nl.wolfpackit.appdev12.R;

public class ShoutFragment extends android.support.v4.app.Fragment{
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ShoutListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shout, container, false);
    }


    public void onAttach(Context context) {
        super.onAttach(context);
    }
    public void onViewCreated(View view, Bundle bundle){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        adapter = new ShoutListAdapter(MainActivity.getStringByID(R.string.shouts).split(","));
        mRecyclerView.setAdapter(adapter);
    }
}
