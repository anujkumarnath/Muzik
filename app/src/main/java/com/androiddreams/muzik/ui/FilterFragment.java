package com.androiddreams.muzik.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androiddreams.muzik.Listeners.OnItemClickListener;
import com.androiddreams.muzik.R;
import com.androiddreams.muzik.adapters.SearchResultAdapter;
import com.androiddreams.muzik.models.Track;
import com.androiddreams.muzik.network.APIClient;
import com.androiddreams.muzik.network.ServerInterface;
import com.androiddreams.muzik.utilities.ColorPaletteGenerator;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.MediaItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterFragment extends Fragment {

    private OnItemClickListener onItemClickListener;

    private String endpoint;
    private String keyword;
    private ConstraintLayout rootLayout;

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
        setStatusBarColor(ContextCompat.getColor(container.getContext(), R.color.colorPrimaryDark));
        rootLayout = root.findViewById(R.id.rootLayout);
        TextView tvToolbar = root.findViewById(R.id.tvToolbar);
        ImageView ivBackButton = root.findViewById(R.id.ivBackButton);
        ivBackButton.setOnClickListener(view -> getActivity().onBackPressed());
        tvToolbar.setText(keyword);
        RecyclerView recyclerView = root.findViewById(R.id.rvFilterResult);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SearchResultAdapter adapter = new SearchResultAdapter(getContext());

        adapter.setmOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_LONG).show();
                List<MediaItem> mediaItems = new ArrayList<>();
                for (Track t : adapter.getTrackList().subList(position, adapter.getItemCount())) {
                    mediaItems.add(new MediaItem.Builder().setUri(Uri.parse(t.getmStreamURL())).setTag(t).build());
                }
                onItemClickListener.onItemClick(mediaItems);
            }
        });

        ServerInterface serverInterface = APIClient.getClient().create(ServerInterface.class);
        SharedPreferences sp = getActivity().getSharedPreferences("login_prefs", Activity.MODE_PRIVATE);
        String username = sp.getString("username", "placeholder@email.com");
        Call<List<Track>> callSearch = serverInterface.getFilterResult(endpoint, keyword, username);
        callSearch.enqueue(new Callback<List<Track>>() {
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