package com.androiddreams.muzik.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.androiddreams.muzik.R;

public class HomeFragment extends Fragment {

    private String endpoint;
    private String keyword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnLanguage = view.findViewById(R.id.btnLanguage);
        Button btnGenre = view.findViewById(R.id.btnGenre);
        Button btnArtist = view.findViewById(R.id.btnArtist);
        OnClickListener onClickListener = new OnClickListener();

        btnLanguage.setOnClickListener(onClickListener);
        btnGenre.setOnClickListener(onClickListener);
        btnArtist.setOnClickListener(onClickListener);
    }

    private class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            switch (view.getId()) {
                case R.id.btnLanguage:
                    endpoint = "language";
                    keyword = "English";
                    break;
                case R.id.btnGenre:
                    endpoint = "genre";
                    keyword = "Pop";
                    break;
                case R.id.btnArtist:
                    endpoint = "artist";
                    keyword = "Alan Walker";
                    break;
            }
            bundle.putString("KEY_ENDPOINT", endpoint);
            bundle.putString("KEY_KEYWORD", keyword);
            Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_filter, bundle);
        }
    }
}