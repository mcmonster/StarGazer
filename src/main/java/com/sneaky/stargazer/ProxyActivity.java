package com.sneaky.stargazer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.sneaky.stargazer.device.Device;

/**
 * Entry point activity used by all of the game. Delegates view and
 * rendering behavior out to the currently activated components.
 * 
 * @author R. Matt McCann
 */
public class ProxyActivity extends Activity {
    /** Evaluates raw user input and passes onto the active renderer. */
    private ProxyView view;
    
    /** {@inheritDoc} */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Set the program to full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        ProxyRenderer renderer = new ProxyRenderer(this);
        view = new ProxyView(this, new Device(this), renderer);
        
        renderer.setView(view);
        setContentView(view);
    }
    
    /** {@inheritDoc} */
    @Override
    public final void onPause() {
        super.onPause();
        view.onPause();
    }
    
    /** {@inheritDoc} */
    @Override
    public final void onResume() {
        super.onResume();
        view.onResume();
    }
}
