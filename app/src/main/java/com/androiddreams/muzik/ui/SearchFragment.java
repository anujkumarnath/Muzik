package com.androiddreams.muzik.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddreams.muzik.Listeners.OnItemClickListener;
import com.androiddreams.muzik.MainActivity;
import com.androiddreams.muzik.R;
import com.androiddreams.muzik.adapters.SearchResultAdapter;
import com.androiddreams.muzik.models.Track;
import com.androiddreams.muzik.network.APIClient;
import com.androiddreams.muzik.network.ServerInterface;
import com.androiddreams.muzik.utilities.ColorPaletteGenerator;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private OnItemClickListener onItemClickListener;
    private Timer timer;
    private ConstraintLayout rootLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        setStatusBarColor(ContextCompat.getColor(container.getContext(), R.color.colorPrimaryDark));
        ViewSwitcher viewSwitcher = root.findViewById(R.id.viewSwitcher);
        rootLayout = root.findViewById(R.id.rootLayout);
        TextView textView = root.findViewById(R.id.tvSearch);
        TextView tvSearchPageInfo = root.findViewById(R.id.tvSearchPageInfo);
        TextInputEditText etSearch = root.findViewById(R.id.etSearch);
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        textView.setOnClickListener(view -> {
            viewSwitcher.showNext();
            etSearch.requestFocus();
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
            tvSearchPageInfo.setVisibility(View.GONE);
        });

        etSearch.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() <= (etSearch.getLeft() + etSearch.getPaddingLeft() + etSearch.getCompoundDrawables()[0].getBounds().width())) {
                    etSearch.setText("");
                    viewSwitcher.showPrevious();
                    tvSearchPageInfo.setVisibility(View.VISIBLE);
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                    return true;
                }
            }
            return false;
        });

//        root.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
//            Rect r = new Rect();
//            root.getWindowVisibleDisplayFrame(r);
//            int screenHeight = root.getRootView().getHeight();
//            int keypadHeight = screenHeight - r.bottom;
//            if (keypadHeight > screenHeight * 0.15) {
//                viewSwitcher.showPrevious();
//            } else {
//            }
//        });

        RecyclerView recyclerView = root.findViewById(R.id.rvSearchResult);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        SearchResultAdapter adapter = new SearchResultAdapter(getContext());
        adapter.setmOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                List<MediaItem> mediaItems = new ArrayList<>();
                for (Track t : adapter.getTrackList().subList(position, adapter.getItemCount())) {
                   mediaItems.add(new MediaItem.Builder().setUri(Uri.parse(t.getmStreamURL())).setTag(t).build());
                }
                onItemClickListener.onItemClick(mediaItems);
            }
        });

        ServerInterface serverInterface = APIClient.getClient().create(ServerInterface.class);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                // user typed: start the timer
                if (editable.toString().equals("")) {
                    adapter.setData(new ArrayList<>());
                } else {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            SharedPreferences sp = getActivity().getSharedPreferences("login_prefs", Activity.MODE_PRIVATE);
                            String username = sp.getString("username", "placeholder@email.com");
                            Call<List<Track>> callSearch = serverInterface.getSearchResult(editable.toString(), username);
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
                                    Toast.makeText(getContext(), "CANNOT REACH TO SERVER", Toast.LENGTH_SHORT).show();
                                    call.cancel();
                                }
                            });
                            // do your actual work here
                        }
                    }, 600); // 600ms delay before the timer executes the „run“ method from TimerTask
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // nothing to do here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // user is typing: reset already started timer (if existing)
                if (timer != null) {
                    timer.cancel();
                }
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