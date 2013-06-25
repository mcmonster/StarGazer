package com.sneaky.stargazer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sneaky.stargazer.device.OnPauseEvent;
import com.sneaky.stargazer.device.OnResumeEvent;
import com.sneaky.stargazer.game.GameModule;
import com.sneaky.stargazer.graphics.MVP;
import com.sneaky.stargazer.graphics.Renderer;
import com.sneaky.stargazer.io.ClickHandler;
import com.sneaky.stargazer.io.DragHandler;
import com.sneaky.stargazer.io.LongPressHandler;
import com.sneaky.stargazer.io.ZoomHandler;
import com.sneaky.stargazer.misc.points.Point2D;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Delegates rendering and user input handling to the currently
 * activated rendering component.
 * 
 * @author R. Matt McCann
 */
public class ProxyRenderer implements ClickHandler, DragHandler, 
        GLSurfaceView.Renderer, LongPressHandler, ZoomHandler {
    /** Activity registered with the Android OS. */
    private final ProxyActivity activity;
    
    private Renderer currentRenderer;
    
    private ProxyView view;
    
    public ProxyRenderer(final ProxyActivity activity) {
        this.activity = activity;
    }
    
    public ProxyActivity getActivity() { return activity; }
    
    public Context getContext() { return activity.getApplicationContext(); }
    
    public EventBus getNotifier() { return currentRenderer.getNotifier(); }
    
    public ProxyView getView() { return view; } 
    
    @Override
    public boolean handleClick(Point2D clickLocation) {
        return currentRenderer.handleClick(clickLocation);
    }
    
    @Override
    public boolean handleDrag(Point2D moveVector) {
        return currentRenderer.handleDrag(moveVector);
    }

    @Override
    public boolean handleDrop(Point2D dropLocation) {
        return currentRenderer.handleDrop(dropLocation);
    }
    
    @Override
    public boolean handleLongPress(Point2D pressLocation) {
        return currentRenderer.handleLongPress(pressLocation);
    }
    
    @Override
    public boolean handlePickUp(Point2D touchLocation) {
        return currentRenderer.handlePickUp(touchLocation);
    }
    
    @Override
    public boolean handleZoom(float zoomFactor) {
        return currentRenderer.handleZoom(zoomFactor);
    }
    
    @Override
    public synchronized void onDrawFrame(GL10 arg0) {
        // Draw the background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        
        currentRenderer.drawFrame(new MVP());
    }

    public void onPause() {
        if (currentRenderer != null) {
            currentRenderer.getNotifier().post(new OnPauseEvent());
        }
    }
    
    public void onResume() {
        if (currentRenderer != null) {
            currentRenderer.getNotifier().post(new OnResumeEvent());
        }
    }
    
    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        // Set up the view-port
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        Injector injector = Guice.createInjector(new GameModule(this));
        currentRenderer = injector.getInstance(Renderer.class);
    }
    
    public synchronized void setRenderer(final Renderer renderer) {
        currentRenderer.close();
        currentRenderer = renderer;
    }

    public void setView(final ProxyView view) { this.view = view; }
}
