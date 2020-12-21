package com.androiddreams.muzik.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddreams.muzik.R;
import com.androiddreams.muzik.models.CardItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class CategoryItemRecyclerAdapter extends RecyclerView.Adapter<CategoryItemRecyclerAdapter.CategoryItemViewHolder> {

    private Context context;
    private List<CardItem> cardItems;
    private OnCardClickListener onCardClickListener;
    private String endpoint;

    public CategoryItemRecyclerAdapter(Context context, OnCardClickListener onCardClickListener) {
        this.context = context;
        this.onCardClickListener = onCardClickListener;
    }

    @NonNull
    @Override
    public CategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryItemViewHolder(LayoutInflater.from(context).inflate(R.layout.category_row_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemViewHolder holder, int position) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.movie2) // change placeholder
                .error(R.drawable.movie1);

        Glide.with(context).load(cardItems.get(position).getImageURL()).apply(options).into(holder.itemImage);
        //holder.itemImage.setImageResource(categoryItemList.get(position).getImageUrl());
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }

    public class CategoryItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView itemImage;
        public CategoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCardClickListener.onCardClick(cardItems.get(getAdapterPosition()), endpoint);
        }
    }

    public void setData(List<CardItem> cardItems, String endpoint) {
        this.endpoint = endpoint;
        this.cardItems = cardItems;
        notifyDataSetChanged();
    }

    public interface OnCardClickListener {
       void onCardClick(CardItem cardItem, String endpoint);
    }

}