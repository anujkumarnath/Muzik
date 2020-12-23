package com.androiddreams.muzik.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddreams.muzik.R;
import com.androiddreams.muzik.adapters.CategoryItemRecyclerAdapter;
import com.androiddreams.muzik.adapters.MainRecyclerAdapter;
import com.androiddreams.muzik.models.CardItem;
import com.androiddreams.muzik.utilities.ColorPaletteGenerator;
import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment implements CategoryItemRecyclerAdapter.OnCardClickListener {

    private NavController navController;
    private FrameLayout rootLayout;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rootLayout = view.findViewById(R.id.rootLayout);
        setBackground();
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

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().setStatusBarColor(color);
        }
    }

    public void setBackground() {
        GradientDrawable gd = new GradientDrawable();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.english);
        Palette palette = Palette.from(bitmap).generate();
        int paletteColour = new ColorPaletteGenerator().getBackgroundColorFromPalette(palette);
        gd.setColors(new int[] {paletteColour, 0x0F171E});
        setStatusBarColor(paletteColour);
        rootLayout.setBackground(gd);
    }

}