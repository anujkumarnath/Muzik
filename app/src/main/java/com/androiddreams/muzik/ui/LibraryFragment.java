package com.androiddreams.muzik.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class LibraryFragment extends Fragment {

    private OnItemClickListener onItemClickListener;
    private TextView tvNoFavourite;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_library, container, false);
        tvNoFavourite = root.findViewById(R.id.tvNoFavourite);
        RecyclerView recyclerView = root.findViewById(R.id.rvFavourite);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SearchResultAdapter adapter = new SearchResultAdapter(getContext());

        adapter.setmOnItemClickListener(track -> onItemClickListener.onItemClick(track));

        ServerInterface serverInterface = APIClient.getClient().create(ServerInterface.class);
        Call<List<Track>> callFavourite = serverInterface.getFavouriteTracks("anuj@gmail.com");
        callFavourite.enqueue(new Callback<List<Track>>() {
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
                tvNoFavourite.setVisibility(View.VISIBLE);
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