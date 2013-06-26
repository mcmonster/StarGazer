package com.sneaky.stargazer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.sneaky.stargazer.device.Device;
import com.sneaky.stargazer.music.MusicService;

/**
 * Entry point activity used by all of the game. Delegates view and
 * rendering behavior out to the currently activated components.
 * 
 * @author R. Matt McCann
 */
public class ProxyActivity extends Activity {
    /** Provides a static method for retrieving the application context. */
    private static Context context;
    
    /** Service that plays the background music. */
    private Intent musicService;
    
    /** Evaluates raw user input and passes onto the active renderer. */
    private ProxyView view;
    
    public static Context getContext() { return context; }
    
    /** {@inheritDoc} */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        context = this;
        
        // Set the program to full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        ProxyRenderer renderer = new ProxyRenderer(this);
        view = new ProxyView(this, new Device(this), renderer);
        
        musicService = new Intent(this, MusicService.class);
        
        renderer.setView(view);
        setContentView(view);
    }
    
    /** {@inheritDoc} */
    @Override
    public final void onPause() {
        super.onPause();
        
        this.stopService(musicService);
        view.onPause();
    }
    
    /** {@inheritDoc} */
    @Override
    public final void onResume() {
        super.onResume();
        
        this.startService(musicService);
        view.onResume();
    }
}
