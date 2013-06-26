package com.sneaky.stargazer.music;

import android.content.Context;
import com.sneaky.stargazer.R;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import com.sneaky.stargazer.ProxyActivity;

/**
 * Provides the background music for the game.
 * 
 * @author R. Matt McCann
 */
public class MusicService extends Service {
    public final static String ACTION_PLAY = "MusicService.ACTION_PLAY";
    
    /** Handles playing the music. */
    private MediaPlayer mediaPlayer;

    private final static String TAG = "MusicService";
    
    /** {@inheritDocs} */
    @Override
    public IBinder onBind(Intent intent) {
        return null; // Do nothing?
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    
    /** {@inheritDocs} */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer = MediaPlayer.create(ProxyActivity.getContext(), R.raw.test_soundtrack);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(100, 100);
        mediaPlayer.start();
        
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
