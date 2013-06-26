package com.sneaky.stargazer.game;

import android.opengl.Matrix;
import static com.google.common.base.Preconditions.*;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.sneaky.stargazer.graphics.MVP;
import com.sneaky.stargazer.graphics.Renderable;
import com.sneaky.stargazer.graphics.flow.GameTickEvent;
import com.sneaky.stargazer.graphics.shader.SimpleTexturedShader;
import com.sneaky.stargazer.graphics.textures.Texture;
import com.sneaky.stargazer.misc.Constants;
import com.sneaky.stargazer.misc.points.Point2D;

/**
 * Model of a falling star.
 * 
 * @author R. Matt McCann
 */
public class Star implements Renderable {
    /** Velocity at which the star falls. */
    private float fallVelocity;
    
    /** Actual height of the star. */
    private float height;
    
    /** Whether or not the star has moved off screen. */
    private boolean isOffScreen = false;
    
    /** Used to unregister the star from published events. */
    private final EventBus notifier;
    
    /** Actual position of the star. */
    private Point2D position;
    
    /** Rendering height of the star. */
    private float renderingHeight;
    
    /** Position of the star on the screen. */
    private Point2D renderingPosition;
    
    /** Rendering width of the fox. */
    private float renderingWidth;
    
    /** Used to draw the star. */
    private final SimpleTexturedShader shader;
    
    /** Texture of the star. */
    private Texture texture;
    
    /** Actual width of the fox. */
    private float width;
    
    /** Guice injectable constructor. */
    @Inject
    public Star(EventBus notifier,
                SimpleTexturedShader shader) {
        this.notifier = checkNotNull(notifier);
        this.shader = checkNotNull(shader);
        
        notifier.register(this);
    }
    
    /** Injection compatible object factory. */
    public interface StarFactory {
        Star create();
    }
    
    public float getHeight() { return height; }
    
    public Point2D getPosition() { return position; }
    
    public float getWidth() { return width; }
    
    public boolean isOffScreen() { return isOffScreen; }
    
    @Subscribe
    public void onGameTickEvent(GameTickEvent event) {
        // Move the star farther along the fall
        renderingPosition.addToY(-fallVelocity / event.getTicksPerSecond());
        position = renderingPosition.clone();
        
        if (renderingPosition.getY() < -1.0f) { // If the star is far off the screen
            notifier.unregister(this);
            isOffScreen = true;
        }
    }
    
    @Override
    public void render(MVP mvp) {
        float[] model = mvp.peekCopyM();
        Matrix.translateM(model, Constants.NO_OFFSET, renderingPosition.getX(), renderingPosition.getY(), 0.0f);
        Matrix.scaleM(model, Constants.NO_OFFSET, renderingWidth, renderingHeight, 1.0f);
        shader.setMVPMatrix(mvp.collapseM(model));
        shader.setTexture(texture.getHandle());
        shader.activate();
        shader.draw();
    }

    public void setFallVelocity(float fallVelocity) {
        this.fallVelocity = fallVelocity;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setPosition(Point2D position) {
        this.position = checkNotNull(position);
    }
    
    public void setRenderingHeight(float renderingHeight) {
        this.renderingHeight = renderingHeight;
    }

    public void setRenderingPosition(Point2D renderingPosition) {
        this.renderingPosition = checkNotNull(renderingPosition);
    }

    public void setRenderingWidth(float renderingWidth) {
        this.renderingWidth = renderingWidth;
    }

    public void setTexture(Texture texture) {
        this.texture = checkNotNull(texture);
    }

    public void setWidth(float width) {
        checkArgument(width > 0.0f, "Width must be > 0.0f, got %s", width);
        
        this.width = width;
    }
}
