package com.androiddreams.muzik;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.androiddreams.muzik.Listeners.OnItemClickListener;
import com.androiddreams.muzik.models.Track;
import com.androiddreams.muzik.services.AudioService;
import com.androiddreams.muzik.ui.PlayerActivity;
import com.androiddreams.muzik.ui.SearchFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    PlayerControlView playerControlView;
    SimpleExoPlayer player;
    private String mTrackURL;
    private String mArtworkURL;
    private TextView tvTitle;
    private TextView tvArtist;
    private ImageView ivThumbnail;
    private AudioService audioService;
    private String TAG = MainActivity.class.getSimpleName();

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AudioService.AudioBinder binder = (AudioService.AudioBinder) iBinder;
            audioService = binder.getAudioService();
            player = audioService.getPlayer();
            playerControlView.setPlayer(player);
            player.addListener(new PlaybackStateListener());
            Log.d(TAG, "onServiceConnected: Called");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setItemIconTintList(null); // disables default colors in bottom navigation view

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        playerControlView = findViewById(R.id.playerControlView);
        DefaultTimeBar timeBar = findViewById(R.id.exo_progress);
        timeBar.setEnabled(false);
//        player = new SimpleExoPlayer.Builder(this).build();
//        playerControlView.setPlayer(player);

        // Handling mini player click event
        playerControlView.setOnClickListener(view -> {
            Intent playerActivityIntent = new Intent(MainActivity.this, PlayerActivity.class);
            playerActivityIntent.putExtra("KEY_TRACK_URL", mTrackURL);
            playerActivityIntent.putExtra("KEY_ARTWORK_URL", mArtworkURL);
            if (player != null)
                playerActivityIntent.putExtra("KEY_PLAYBACK_POSITION", player.getCurrentPosition());
//            player.stop();
            startActivity(playerActivityIntent);
        });
        tvTitle = findViewById(R.id.tvTitle);
        tvArtist = findViewById(R.id.tvArtist);
        ivThumbnail = findViewById(R.id.ivAlbumArt);

        tvTitle.setSelected(true);
        tvArtist.setSelected(true);

        Intent intent = new Intent(MainActivity.this, AudioService.class);
        startService(intent);
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }

    @Override
    public void onItemClick(Track track) {
        /*
        if (audioService != null) {
            audioService.stopForeground(true);
            audioService.stopSelf();
        }
         */

//        Toast.makeText(this, track.getmStreamURL(), Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(MainActivity.this, AudioService.class);
//        intent.putExtra("KEY_TRACK_URL", track.getmStreamURL());
//        startService(intent);
//        bindService(intent, conn, BIND_AUTO_CREATE);
//


        play(track);
    }

    private void play(Track track) {
        /*
        mTrackURL = URL;
        if (player != null) {
            player.stop();
            player.release();
        }
        */
        if (playerControlView.getVisibility() == View.GONE)
            playerControlView.setVisibility(View.VISIBLE);
        MediaItem mediaItem = new MediaItem.Builder().setUri(Uri.parse(track.getmStreamURL())).setTag(track).build();
        player.addMediaItem(mediaItem);
        player.next();
        /*
        player = new SimpleExoPlayer.Builder(this).build();
        playerControlView.setPlayer(player);

        MediaItem mediaSource = MediaItem.fromUri(URL);
        Toast.makeText(this, URL, Toast.LENGTH_LONG).show();

        player.addMediaItem(mediaSource);
        */
        player.prepare();
        player.play();
    }


    // TODO: Try changing data with the help of application class
    public class PlaybackStateListener implements ExoPlayer.EventListener {
        @Override
        public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
            Track track = (Track) mediaItem.playbackProperties.tag;
            tvTitle.setText(track.getTitle());
            tvArtist.setText(track.getArtist());
            mArtworkURL = track.getmArtWorkURL();

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round) // change placeholder
                    .error(R.mipmap.ic_launcher_round);

            Glide.with(getApplicationContext()).load(track.getmArtWorkURL()).apply(options).into(ivThumbnail);
        }

//        @Override
//        public void onPlaybackStateChanged(int state) {
//            if (state == ExoPlayer.STATE_ENDED) {
//                playerControlView.setVisibility(View.GONE);
//            }
//        }
    }
}