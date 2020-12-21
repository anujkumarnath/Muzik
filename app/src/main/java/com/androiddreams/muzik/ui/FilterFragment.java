package com.androiddreams.muzik.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androiddreams.muzik.Listeners.OnItemClickListener;
import com.androiddreams.muzik.R;
import com.androiddreams.muzik.adapters.SearchResultAdapter;
import com.androiddreams.muzik.models.Track;
import com.androiddreams.muzik.network.APIClient;
import com.androiddreams.muzik.network.ServerInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterFragment extends Fragment {

    private OnItemClickListener onItemClickListener;

    private String endpoint;
    private String keyword;

    public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            endpoint = getArguments().getString("KEY_ENDPOINT");
            keyword = getArguments().getString("KEY_KEYWORD");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_filter, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.rvFilterResult);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SearchResultAdapter adapter = new SearchResultAdapter(getContext());

        adapter.setmOnItemClickListener(track -> onItemClickListener.onItemClick(track));

        ServerInterface serverInterface = APIClient.getClient().create(ServerInterface.class);
        Call<List<Track>> callSearch = serverInterface.getFilterResult(endpoint, keyword);
        callSearch.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.body() != null) {
                    adapter.setData(response.body());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                call.cancel();
            }
        });

        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            this.onItemClickListener = (OnItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnItemClickListener");
        }
    }
}