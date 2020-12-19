package com.androiddreams.muzik.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ImageView;
import android.widget.TextView;

import com.androiddreams.muzik.MainActivity;
import com.androiddreams.muzik.R;
import com.androiddreams.muzik.models.Track;
import com.androiddreams.muzik.services.AudioService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.PlayerControlView;

public class PlayerActivity extends AppCompatActivity {

    private long playbackPostion = 0;
    private String mArtworkURL;
    private AudioService audioService;
    private PlayerControlView playerControlView;
    private SimpleExoPlayer player;
    private TextView tvTitle;
    private TextView tvArtist;
    private ImageView ivArtwork;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AudioService.AudioBinder binder = (AudioService.AudioBinder) iBinder;
            audioService = binder.getAudioService();
            player = audioService.getPlayer();
            playerControlView.setPlayer(player);
            player.addListener(new PlaybackStateListener());

            MediaItem mediaItem = player.getCurrentMediaItem();
            Track track = (Track) mediaItem.playbackProperties.tag;
            tvTitle.setText(track.getTitle());
            tvArtist.setText(track.getArtist());

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round) // change placeholder
                    .error(R.mipmap.ic_launcher_round);

            Glide.with(getApplicationContext()).load(track.getmArtWorkURL()).apply(options).into(ivArtwork);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        ivArtwork = findViewById(R.id.ivArtwork);
        tvTitle = findViewById(R.id.tvTitle);
        tvArtist = findViewById(R.id.tvArtist);

        /*
        // Receiving song information from intent
        Intent intentFromMainActivity = getIntent();
        if (intentFromMainActivity != null) {
            mArtworkURL = intentFromMainActivity.getStringExtra("KEY_ARTWORK_URL");

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round) // change placeholder
                    .error(R.mipmap.ic_launcher_round);

            Glide.with(this).load(mArtworkURL).apply(options).into(ivArtwork);
        }
        */

        Intent intent = new Intent(this, AudioService.class);
        startService(intent);
        bindService(intent, conn, BIND_AUTO_CREATE);

        playerControlView = findViewById(R.id.mainPlayerControlView);
        DefaultTimeBar timeBar = findViewById(R.id.exo_progress);
        timeBar.setEnabled(false);

        /*
        player = new SimpleExoPlayer.Builder(this).build();
        playerControlView.setPlayer(player);
        // Build the media items.
        MediaItem firstItem = MediaItem.fromUri(Uri.parse(mTrackURL));
//        MediaItem secondItem = MediaItem.fromUri(Uri.parse("http://13.126.87.166/faded.mp3"));
        // Add the media items to be played.
        player.addMediaItem(firstItem);
//        player.addMediaItem(secondItem);
        player.seekTo(playbackPostion);
        // Prepare the player.
        player.prepare();
        // Start the playback.
        player.play();

         */
    }

    private class PlaybackStateListener implements Player.EventListener {
        /*
        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            MediaItem mediaItem = player.getCurrentMediaItem();
        }

         */

        @Override
        public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
            Track track = (Track) mediaItem.playbackProperties.tag;
            tvTitle.setText(track.getTitle());
            tvArtist.setText(track.getArtist());

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round) // change placeholder
                    .error(R.mipmap.ic_launcher_round);

            Glide.with(getApplicationContext()).load(track.getmArtWorkURL()).apply(options).into(ivArtwork);
        }
    }
}