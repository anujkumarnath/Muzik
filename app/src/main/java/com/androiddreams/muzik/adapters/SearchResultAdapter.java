package com.androiddreams.muzik.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddreams.muzik.MainActivity;
import com.androiddreams.muzik.R;
import com.androiddreams.muzik.models.Track;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {
    private List<Track> mTrackList;
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;

    public SearchResultAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_list_item, parent, false);
        return new SearchResultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mTrackList.size();
    }

    public void setData(List<Track> trackList) {
        mTrackList = trackList;
        notifyDataSetChanged();
    }

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public class SearchResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        TextView tvArtist;
        ImageView ivThumbnail;
        ImageView ivOptions;

        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            ivOptions = itemView.findViewById(R.id.ivOptionsBtn);

            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            Track track = mTrackList.get(position);
            tvTitle.setText(track.getTitle());
            tvArtist.setText(track.getArtist());

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round) // change placeholder
                    .error(R.mipmap.ic_launcher_round);

            Glide.with(mContext).load(track.getmArtWorkURL()).apply(options).into(ivThumbnail);
        }

        @Override
        public void onClick(View view) {
            mOnItemClickListener.onItemClick(mTrackList.get(getAdapterPosition()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Track track);
    }
}
