package com.androiddreams.muzik.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.annotation.Nullable;

import com.androiddreams.muzik.R;
import com.androiddreams.muzik.models.Track;
import com.androiddreams.muzik.ui.PlayerActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.util.concurrent.ExecutionException;

public class AudioService extends Service {

    private static final String TAG = "AudioService";
    public final Binder binder = new AudioBinder();
    private SimpleExoPlayer mPlayer;
    private PlaybackStateListener playbackStateListener = new PlaybackStateListener();
    private PlayerNotificationManager playerNotificationManager;
    private boolean ended = false;
    private String mStreamUrl;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        mStreamUrl = intent.getStringExtra("KEY_TRACK_URL");
        initializePlayer();
        return START_NOT_STICKY;
    }

    //private void initializePlayer(String url) {
    private void initializePlayer() {
//        if (mPlayer != null) {
//            mPlayer.stop();
//            mPlayer.release();
//        }
        if (mPlayer == null) {
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(this);
            mPlayer = new SimpleExoPlayer.Builder(this)
                    .setTrackSelector(trackSelector)
                    .build();
            mPlayer.setPlayWhenReady(false);
            mPlayer.addListener(playbackStateListener);
            //mPlayer.prepare(buildMediaSource(Uri.parse(getString(R.string.media_url_mp3))), false, false);
//            mPlayer.prepare(buildMediaSource(Uri.parse(url)), false, false);

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
                    this,
                    "NOTIFICATION_CHANNEL_ID",
                    R.string.exoplayer_channel_name,
                    R.string.exoplayer_channel_description,
                    11111,
                    new PlayerNotificationManager.MediaDescriptionAdapter() {
                        @Override
                        public CharSequence getCurrentContentTitle(Player player) {
                            android.util.Log.d(TAG, "getCurrentContentTitle: Called");
                            MediaItem mediaItem = player.getCurrentMediaItem();
                            Track track = (Track) mediaItem.playbackProperties.tag;
                            return track.getTitle();
                        }

                        @Nullable
                        @Override
                        public PendingIntent createCurrentContentIntent(Player player) {
                            android.util.Log.d(TAG, "createCurrentContentIntent: Called");
                            Intent intent = new Intent(AudioService.this, PlayerActivity.class);
                            // TODO: TWO PLAYER ACTIVITY ON TOP OF EACH OTHER
                            return PendingIntent.getActivity(AudioService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        }

                        @Nullable
                        @Override
                        public CharSequence getCurrentContentText(Player player) {
                            android.util.Log.d(TAG, "getCurrentContentText: Called");
                            MediaItem mediaItem = player.getCurrentMediaItem();
                            Track track = (Track) mediaItem.playbackProperties.tag;
                            return track.getArtist();
                        }

                        @Nullable
                        @Override
                        public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
                            android.util.Log.d(TAG, "getCurrentLargeIcon: Called");

                            MediaItem mediaItem = player.getCurrentMediaItem();
                            Track track = (Track) mediaItem.playbackProperties.tag;

                            // load the media image asyncronously
                            Thread thread = new Thread(() -> {
                                RequestOptions options = new RequestOptions()
                                        .centerCrop()
                                        .placeholder(R.drawable.artwork_placeholder)
                                        .error(R.drawable.artwork_placeholder);
                                try {
                                    android.util.Log.d(TAG, "Call toh oise");
                                    Bitmap bitmap = Glide.with(getApplicationContext())
                                            .asBitmap()
                                            .load(Uri.parse(track.getmArtWorkURL()))
                                            .apply(options)
                                            .submit()
                                            .get();
                                    callback.onBitmap(bitmap);
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });

                            thread.start();
                            return null;
//                            return BitmapFactory.decodeResource(getResources(), R.drawable.test_album_art_small);
                        }
                    },
                    new PlayerNotificationManager.NotificationListener() {
                        @Override
                        public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
                            android.util.Log.d(TAG, "onNotificationCancelled: Called");
                        }

                        @Override
                        public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
                            android.util.Log.d(TAG, "onNotificationPosted: Called");
                            if (ongoing)
                                startForeground(notificationId, notification);
                        }
                    });

            //playerNotificationManager.setPriority(NotificationCompat.PRIORITY_MAX);
            playerNotificationManager.setColorized(true);
            playerNotificationManager.setPlayer(mPlayer);
//            MediaSessionCompat mediaSession = new MediaSessionCompat(this, "ExoPlayer");
//            mediaSession.setActive(true);
//            playerNotificationManager.setMediaSessionToken(mediaSession.getSessionToken());
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, "music");
        ProgressiveMediaSource.Factory mediaSourceFactory =
                new ProgressiveMediaSource.Factory(dataSourceFactory);

        return mediaSourceFactory.createMediaSource(uri);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return binder;
    }

    public SimpleExoPlayer getPlayer() {
        return mPlayer;
    }

    public class AudioBinder extends Binder {
        public AudioService getAudioService() {
            Log.d(TAG, "getAudioService: Called");
            return AudioService.this;
        }
    }

    private class PlaybackStateListener implements Player.EventListener {

        @Override
        public void onIsPlayingChanged(boolean isPlaying) {
            if (!isPlaying)
                stopForeground(false);
        }
    }
}