package com.sneaky.stargazer.graphics;

import android.opengl.GLES20;
import static com.google.common.base.Preconditions.*;
import com.sneaky.stargazer.graphics.flow.GameFlowController;
import com.sneaky.stargazer.io.ClickHandler;
import com.sneaky.stargazer.io.DragHandler;
import com.sneaky.stargazer.io.LongPressHandler;
import com.sneaky.stargazer.io.ZoomHandler;

/**
 * Provides common rendering work behind the scenes as convenience.
 * 
 * @author R. Matt McCann
 */
public abstract class BaseRenderer implements ClickHandler, LongPressHandler, 
        DragHandler, ZoomHandler {
    private final GameFlowController gameFlowController;
    
    /**
     * @param gameFlow Must not be null.
     */
    public BaseRenderer(GameFlowController gameFlowController) {
        this.gameFlowController = checkNotNull(gameFlowController);
        
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
    
    /** Draw the current frame. */
    public abstract void drawFrame(final MVP mvp);
}
