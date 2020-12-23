package com.androiddreams.muzik.ui;

import android.content.Context;
import android.graphics.Bitmap;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddreams.muzik.Listeners.OnItemClickListener;
import com.androiddreams.muzik.R;
import com.androiddreams.muzik.adapters.SearchResultAdapter;
import com.androiddreams.muzik.models.Track;
import com.androiddreams.muzik.network.APIClient;
import com.androiddreams.muzik.network.ServerInterface;
import com.androiddreams.muzik.utilities.ColorPaletteGenerator;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LibraryFragment extends Fragment {

    private OnItemClickListener onItemClickListener;
    private TextView tvNoFavourite;
    private FrameLayout rootLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_library, container, false);
        setStatusBarColor(ContextCompat.getColor(container.getContext(), R.color.colorPrimaryDark));
        tvNoFavourite = root.findViewById(R.id.tvNoFavourite);
        rootLayout = root.findViewById(R.id.rootLayout);
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
                    if (response.body().size() != 0)
                        setBackground(response.body().get(0).getmArtWorkURL());
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

    public void setBackground(String URL) {
        GradientDrawable gd = new GradientDrawable();
        Thread thread = new Thread(() -> {
            try {
                Bitmap bitmap = Glide.with(getContext()).asBitmap().load(Uri.parse(URL)).submit().get();
                Palette palette = Palette.from(bitmap).generate();
                int paletteColour = new ColorPaletteGenerator().getBackgroundColorFromPalette(palette);
                gd.setColors(new int[] {paletteColour, 0xE00000});
                getActivity().runOnUiThread(() -> {
                    rootLayout.setBackground(gd);
                    setStatusBarColor(paletteColour);
                });
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().setStatusBarColor(color);
        }
    }
}