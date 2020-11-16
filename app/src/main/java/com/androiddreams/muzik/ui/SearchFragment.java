package com.androiddreams.muzik.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddreams.muzik.R;
import com.androiddreams.muzik.adapters.SearchResultAdapter;
import com.androiddreams.muzik.models.Track;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.rvSearchResult);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        SearchResultAdapter adapter = new SearchResultAdapter();
        recyclerView.setAdapter(adapter);
        List<Track> tracks = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Track track = new Track();
            track.setTitle("ALone");
            track.setArtist("Aajfajslkjfksajfksfj klasjfkla sjfklas kjasfkla sjfk asjf jjhjh jh kj hkh kh kjh");
            tracks.add(track);
        }
        adapter.setData(tracks);
        return root;
    }
}