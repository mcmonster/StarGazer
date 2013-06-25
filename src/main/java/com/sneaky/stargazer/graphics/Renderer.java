package com.sneaky.stargazer.graphics;

import android.opengl.GLES20;
import android.opengl.Matrix;
import static com.google.common.base.Preconditions.*;
import com.google.common.eventbus.EventBus;
import com.sneaky.stargazer.graphics.flow.GameFlowController;
import com.sneaky.stargazer.io.ClickHandler;
import com.sneaky.stargazer.io.DragHandler;
import com.sneaky.stargazer.io.LongPressHandler;
import com.sneaky.stargazer.io.ZoomHandler;
import com.sneaky.stargazer.misc.Constants;

/**
 * Provides common rendering work behind the scenes as convenience.
 * 
 * @author R. Matt McCann
 */
public abstract class Renderer implements ClickHandler, LongPressHandler, 
        DragHandler, ZoomHandler {
    /** Handles the flow of the rendering. */
    private final GameFlowController gameFlowController;
    
    /** Allows the application to trigger application level events. */
    private final EventBus notifier;
    
    /**
     * @param gameFlow Must not be null.
     */
    public Renderer(GameFlowController gameFlowController,
                    EventBus notifier) {
        this.gameFlowController = checkNotNull(gameFlowController);
        this.notifier = checkNotNull(notifier);
        
        // Enable transparency blending
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        
        // Activate the game flow controller
        gameFlowController.start();
    }
    
    /** Cleans up the resources allocated by the renderer. */
    public void close() {
        gameFlowController.setIsRunning(false);
    }
    
    /** Draws the current frame. */
    public void drawFrame(final MVP mvp) {
        // Set up the parameters of the projection matrix
        float near = -1.0f;
        float far = 1.0f;
        float left = -0.5f;
        float right = 0.5f;
        float top = 0.5f;
        float bottom = -0.5f;
        
        // Calculate the projection matrix
        float[] projectionMatrix = new float[Constants.MATRIX_SIZE];
        Matrix.orthoM(projectionMatrix, Constants.NO_OFFSET, left, right, bottom, top, near, far);
        mvp.pushP(projectionMatrix);
        
        drawFrameExt(mvp);
    }
    
    /** Sub-class specific functionality for drawing the current frame. */
    protected abstract void drawFrameExt(final MVP mvp);
    
    public EventBus getNotifier() { return notifier; }
}
