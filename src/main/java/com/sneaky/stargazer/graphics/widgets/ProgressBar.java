package com.sneaky.stargazer.graphics.widgets;

import android.opengl.Matrix;
import static com.google.common.base.Preconditions.*;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sneaky.stargazer.device.Device;
import com.sneaky.stargazer.graphics.MVP;
import com.sneaky.stargazer.graphics.Renderable;
import com.sneaky.stargazer.graphics.flow.GameTickEvent;
import com.sneaky.stargazer.graphics.shader.SimpleTexturedShader;
import com.sneaky.stargazer.graphics.textures.Texture;
import com.sneaky.stargazer.misc.Constants;
import com.sneaky.stargazer.misc.points.Point2D;

/**
 * Tracks how far along in the level the fox is.
 * 
 * @author R. Matt McCann
 */
public class ProgressBar implements Renderable {
    /** Body of the texture of the progress bar. */
    private Texture bar;
    
    /** Used to compensate for the device's aspect ratio. */
    private final Device device;
    
    /** Height of the progress bar. */
    private float height = 0.05f;
    
    /** Whether or not the progress bar is complete. */
    private boolean isComplete = false;
    
    /** Moon icon tracking the progress. */
    private Texture moon;
    
    /** Rendering size of the moon. */
    private float moonSize = 0.05f;
    
    /** Used to unsubscribe the progress bar from published events. */
    private final EventBus notifier;
    
    /** Center point of the progress bar on the screen. */
    private Point2D position;

    /** When the progress bar was started. */
    private long startTime = System.currentTimeMillis();
    
    /** Progress along the bar in percents. */
    private float percentageComplete = 0.0f; 
    
    /** Used to draw the progress bar. */
    private final SimpleTexturedShader shader;
    
    /** Time length over which the progress bar covers. */
    private long timeFrame;
    
    /** Width of the progress bar. */
    private float width;
    
    /** Guice injectable constructor. */
    @Inject
    public ProgressBar(@Named("ProgressBar") Texture bar,
                       Device device,
                       @Named("ProgressBarMoon") Texture moon,
                       EventBus notifier,
                       SimpleTexturedShader shader) {
        this.bar = checkNotNull(bar);
        this.device = checkNotNull(device);
        this.moon = checkNotNull(moon);
        this.notifier = checkNotNull(notifier);
        this.shader = checkNotNull(shader);
        
        width = height * bar.getAspectRatio() / device.getAspectRatio();
        
        float xPos = 0.5f - width / 2.0f;
        float yPos = 0.5f - height; 
        position = new Point2D(xPos, yPos);
        
        notifier.register(this); // Subscribe to event notifications
    }
    
    public boolean isComplete() { return isComplete; }
    
    @Subscribe
    public void onGameTickEvent(GameTickEvent event) {
        float elapsedTime = System.currentTimeMillis() - startTime;
        percentageComplete = elapsedTime / timeFrame; // Update the percentage
        
        if (percentageComplete > 1.0f) {
            isComplete = true;
            percentageComplete = 1.0f;
            notifier.unregister(this);
        }
    }
    
    /** {@inheritDocs} */
    @Override
    public void render(MVP mvp) {
        shader.activate();
        
        // Draw the bar
        float[] model = mvp.peekCopyM();
        Matrix.translateM(model, Constants.NO_OFFSET, position.getX(), position.getY(), 0.0f);
        mvp.pushM(model);
        Matrix.scaleM(model, Constants.NO_OFFSET, width, height, 1.0f);
        shader.setMVPMatrix(mvp.collapseM(model));
        shader.setTexture(bar.getHandle());
        shader.draw();
        
        // Draw the moon icon
        model = mvp.popM();
        float xPos = (0.5f - percentageComplete) * width;
        Matrix.translateM(model, Constants.NO_OFFSET, xPos, 0.0f, 0.0f);
        Matrix.scaleM(model, Constants.NO_OFFSET, moonSize / device.getAspectRatio(), moonSize, 0.0f);
        shader.setMVPMatrix(mvp.collapseM(model));
        shader.setTexture(moon.getHandle());
        shader.draw();
    }
    
    public void setTimeFrame(long timeFrame) {
        checkArgument(timeFrame > 0, "TimeFrame must be > 0, got %s", timeFrame);
        
        this.timeFrame = timeFrame;
    }
}
