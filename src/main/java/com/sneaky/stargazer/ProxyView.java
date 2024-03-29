package com.sneaky.stargazer;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import static com.google.common.base.Preconditions.*;
import com.google.common.eventbus.EventBus;
import com.sneaky.stargazer.device.Device;
import com.sneaky.stargazer.device.OnPauseEvent;
import com.sneaky.stargazer.device.OnResumeEvent;
import com.sneaky.stargazer.io.PressTimer;
import com.sneaky.stargazer.misc.points.Point2D;
import com.sneaky.stargazer.misc.points.PointHelper;

/**
 * Evaluates raw user input, passing the input gestures to the proxying renderer.
 * 
 * @author R. Matt McCann
 */
public class ProxyView extends GLSurfaceView {
    /** Used when translating input events into normalized space. */
    private final Device device;
    
    /** How far the movement action has to drag before being classified as a drag gesture. */
    private final float dragActivationDistance = 10.0f;
    
    /** Whether or not the action has been classified as a drag gesture. */
    private boolean isDragAction = false;
    
    /** Last location touched on the screen. Used for calculating gesture vectors. */
    private Point2D lastTouchLocation;
    
    /** Used for handling drag related input. */
    private final ProxyRenderer renderer;
    
    /** Timing thread used to detect long press events. */
    private PressTimer pressTimer;
    
    /** Detects simple gestures. */
    //private final GestureDetector simpleGestureDetector;
    
    /** Detects zoom gestures. */
    private final ScaleGestureDetector zoomGestureDetector;
    
    /** Constructor. */
    public ProxyView(Activity activity, Device device, ProxyRenderer renderer) {
        super(activity.getApplicationContext());
        
        this.device = checkNotNull(device);
        this.renderer = checkNotNull(renderer);
        
        // Set the OpenGL context to be preserved when the application is paused
        setPreserveEGLContextOnPause(true);
        
        // Enable OpenGL ES 2.0
        setEGLContextClientVersion(2);
        
        // Bind the renderer
        setRenderer(renderer);
        
        // Enable manual rendering control by the game loop controller
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        
        // Set up the input detectors
        this.zoomGestureDetector = new ScaleGestureDetector(
                activity.getApplicationContext(), new ZoomGestureDetector(renderer));
    }
    
    /** {@inheritDoc} */
    @Override
    public void onPause() {
        super.onPause();
        renderer.onPause();
    }
    
    /** {@inheritDoc} */
    @Override
    public void onResume() {
        super.onResume();
        renderer.onResume();
    }
    
    /** {@inheritDoc} */
    @Override
    public synchronized final boolean onTouchEvent(final MotionEvent event) {
        Point2D touchLocation = PointHelper.normalize(device, event);
        
        // Check if a scale gesture occurred
        zoomGestureDetector.onTouchEvent(event);
        
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                lastTouchLocation = touchLocation;
                isDragAction = false;
                
                pressTimer = new PressTimer(renderer, System.currentTimeMillis(), touchLocation);
                new Thread(pressTimer).start();
                
                break;
            case MotionEvent.ACTION_MOVE:
                if (((lastTouchLocation.distanceTo(touchLocation) > dragActivationDistance) || isDragAction)) {
                    pressTimer.setIsShuttingDown(true);
                    
                    if (!isDragAction) {
                        isDragAction = renderer.handlePickUp(touchLocation);
                    }
                    
                    Point2D moveVector = touchLocation.subtract(lastTouchLocation);
                    lastTouchLocation = touchLocation;
                    
                    if (isDragAction) {
                        return renderer.handleDrag(moveVector);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                pressTimer.setIsShuttingDown(true);
                
                // If the action was long press
                if (pressTimer.isLongPress()) {
                    return true;
                // If the action was a drag gesture
                } else if (isDragAction) {
                    isDragAction = false;
                    return renderer.handleDrop(touchLocation);
                }
                // Otherwise the action was a simple click gesture
                else {
                    return renderer.handleClick(touchLocation);
                }
        }
        
        return true;
    }
    
    /** Simple zoom gesture detector. */
    final class ZoomGestureDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private final ProxyRenderer handler;
        
        public ZoomGestureDetector(final ProxyRenderer handler) {
            this.handler = handler;
        }
        
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.d("Input", "Zoom of scale " + detector.getScaleFactor());
            return handler.handleZoom(detector.getScaleFactor());
        }
    }
}
