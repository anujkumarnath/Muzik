package com.androiddreams.muzik.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.rvSearchResult);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        SearchResultAdapter adapter = new SearchResultAdapter();

        ServerInterface serverInterface = APIClient.getClient().create(ServerInterface.class);
        TextInputEditText etSearch = root.findViewById(R.id.etSearch);
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


        return root;
    }

}