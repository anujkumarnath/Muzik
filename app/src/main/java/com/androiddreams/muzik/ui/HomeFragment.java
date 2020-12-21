package com.androiddreams.muzik.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddreams.muzik.R;
import com.androiddreams.muzik.adapters.CategoryItemRecyclerAdapter;
import com.androiddreams.muzik.adapters.MainRecyclerAdapter;
import com.androiddreams.muzik.models.CardItem;

public class HomeFragment extends Fragment implements CategoryItemRecyclerAdapter.OnCardClickListener {

    private NavController navController;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.main_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        String[] categories = new String[] {"Languages", "Genres", "Artists", "EDM"};
        MainRecyclerAdapter adapter = new MainRecyclerAdapter(getContext(), categories, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCardClick(CardItem cardItem, String endpoint) {
        Bundle bundle = new Bundle();
        bundle.putString("KEY_ENDPOINT", endpoint);
        bundle.putString("KEY_KEYWORD", cardItem.getTitle());
        navController.navigate(R.id.action_navigation_home_to_navigation_filter, bundle);
    }
}