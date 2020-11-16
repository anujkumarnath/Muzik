package com.androiddreams.muzik.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.androiddreams.muzik.MainActivity;
import com.androiddreams.muzik.R;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.PlayerControlView;

public class PlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        PlayerControlView playerControlView = findViewById(R.id.mainPlayerControlView);
        DefaultTimeBar timeBar = findViewById(R.id.exo_progress);
        timeBar.setEnabled(false);
        SimpleExoPlayer player = new SimpleExoPlayer.Builder(this).build();
        playerControlView.setPlayer(player);

        // Build the media items.
        MediaItem firstItem = MediaItem.fromUri(Uri.parse("http://13.126.87.166/faded.mp3"));
        MediaItem secondItem = MediaItem.fromUri(Uri.parse("http://13.126.87.166/faded.mp3"));
        // Add the media items to be played.
        player.addMediaItem(firstItem);
        player.addMediaItem(secondItem);
        // Prepare the player.
        player.prepare();
        // Start the playback.
        player.play();
    }
}