package com.androiddreams.muzik.ui;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

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
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private OnItemClickListener onItemClickListener;
    private Timer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        ViewSwitcher viewSwitcher = root.findViewById(R.id.viewSwitcher);
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
        adapter.setmOnItemClickListener(track -> {
//            Intent intent = new Intent(getContext(), MainActivity.class);
//            intent.putExtra("KEY_TRACK_URL", track.getmStreanURL());
//            startActivity(intent);
//            // TODO: stop any other playing activity
            onItemClickListener.onItemClick(track);
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

}