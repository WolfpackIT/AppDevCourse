package nl.wolfpack.emailwolfpack.slack;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import nl.wolfpack.emailwolfpack.R;

public class ShoutFragment extends Fragment {
    List<String> shouts;
    RecyclerView recyclerView;
    ShoutsAdapter shoutsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shouts = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shout_layout, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewShouts);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String shout = shouts.get(position);
                        RequestQueue queue = Volley.newRequestQueue(getContext());

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                })
        );
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), 1);
        recyclerView.addItemDecoration(dividerItemDecoration);

        shouts.add("lekker eddy");
        shouts.add("hue");
        shouts.add("lekker eddy");
        shouts.add("hue");
        shouts.add("lekker eddy");
        shouts.add("hue");
        shouts.add("lekker eddy");
        shouts.add("hue");
        shouts.add("lekker eddy");
        shouts.add("hue");
        shouts.add("lekker eddy");
        shouts.add("hue");
        shouts.add("lekker eddy");
        shouts.add("hue");
        shouts.add("lekker eddy");
        shouts.add("hue");
        shouts.add("lekker eddy");
        shouts.add("hue");

        shoutsAdapter = new ShoutsAdapter(shouts);
        recyclerView.setAdapter(shoutsAdapter);

        return view;
    }



}
