package com.androiddreams.muzik.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddreams.muzik.MainActivity;
import com.androiddreams.muzik.R;
import com.androiddreams.muzik.adapters.SearchResultAdapter;
import com.androiddreams.muzik.models.Track;
import com.androiddreams.muzik.network.APIClient;
import com.androiddreams.muzik.network.ServerInterface;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private OnItemClickListener onItemClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.rvSearchResult);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        SearchResultAdapter adapter = new SearchResultAdapter(getContext());
        adapter.setmOnItemClickListener(track -> {
//            Intent intent = new Intent(getContext(), MainActivity.class);
//            intent.putExtra("KEY_TRACK_URL", track.getmStreanURL());
//            startActivity(intent);
//            // TODO: stop any other playing activity
            onItemClickListener.onItemClick(track);
        });

        ServerInterface serverInterface = APIClient.getClient().create(ServerInterface.class);
        TextInputEditText etSearch = root.findViewById(R.id.etSearch);
        Button button = root.findViewById(R.id.btnSearch);
        button.setOnClickListener(view -> {
            Call<List<Track>> callSearch = serverInterface.getSearchResult(etSearch.getText().toString());
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
        });

        /*
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Call<List<Track>> callSearch = serverInterface.getSearchResult(editable.toString());
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
            }
        });
         */


        return root;
    }

    public interface OnItemClickListener {
        void onItemClick(Track track);
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