package com.androiddreams.muzik;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.androiddreams.muzik.ui.PlayerActivity;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    PlayerControlView playerControlView;
    SimpleExoPlayer player;
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
        player = new SimpleExoPlayer.Builder(this).build();
        playerControlView.setPlayer(player);

        // Handling mini player click event
        playerControlView.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
            startActivity(intent);
        });

        /*
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
         */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}