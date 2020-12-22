package com.androiddreams.muzik.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddreams.muzik.R;
import com.androiddreams.muzik.models.CardItem;
import com.androiddreams.muzik.models.Track;
import com.androiddreams.muzik.network.APIClient;
import com.androiddreams.muzik.network.ServerInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder> {

    private Context context;
    public String[] categories;
    private String[] endpoints = new String[] {"language", "genre", "artist", "Edm"};
    private CategoryItemRecyclerAdapter.OnCardClickListener cardClickListener;

    public MainRecyclerAdapter(Context context, String[] categories, CategoryItemRecyclerAdapter.OnCardClickListener cardClickListener) {
        this.context = context;
        this.categories = categories;
        this.cardClickListener = cardClickListener;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context).inflate(R.layout.main_recycler_row_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.categoryTitle.setText(categories[position]);

        // we get the list from network call with keyword = categories[position] --> artist
        // String
        ServerInterface serverInterface = APIClient.getClient().create(ServerInterface.class);
        Call<List<CardItem>> callSearch = serverInterface.getCardItems(endpoints[position]);
        callSearch.enqueue(new Callback<List<CardItem>>() {
            @Override
            public void onResponse(Call<List<CardItem>> call, Response<List<CardItem>> response) {
                if (response.body() != null) {
                    holder.bind(response.body(), endpoints[position]);
                }
            }

            @Override
            public void onFailure(Call<List<CardItem>> call, Throwable t) {
                Toast.makeText(context, "CANNOT REACH TO SERVER", Toast.LENGTH_LONG).show();
                call.cancel();
                List<CardItem> cardItems = new ArrayList<>();
                cardItems.add(new CardItem());
                cardItems.add(new CardItem());
                cardItems.add(new CardItem());
                cardItems.add(new CardItem());
                holder.bind(cardItems, endpoints[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.length;
    }

    public class MainViewHolder extends RecyclerView.ViewHolder{
        TextView categoryTitle;
        RecyclerView itemRecycler;
        CategoryItemRecyclerAdapter itemRecyclerAdapter;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.cat_title);
            itemRecycler = itemView.findViewById(R.id.item_recycler);
            itemRecycler.setLayoutManager(new LinearLayoutManager(itemView.getContext(), RecyclerView.HORIZONTAL, false));
            itemRecyclerAdapter = new CategoryItemRecyclerAdapter(itemView.getContext(), cardClickListener);
        }

        public void bind(List<CardItem> cardItems, String endpoint) {
            itemRecyclerAdapter.setData(cardItems, endpoint);
            itemRecycler.setAdapter(itemRecyclerAdapter);
        }
    }
}