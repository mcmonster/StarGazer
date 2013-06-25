package com.sneaky.stargazer.game;

import android.opengl.Matrix;
import android.util.FloatMath;
import static com.google.common.base.Preconditions.*;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sneaky.stargazer.device.Device;
import com.sneaky.stargazer.graphics.MVP;
import com.sneaky.stargazer.graphics.Renderable;
import com.sneaky.stargazer.graphics.shader.SimpleTexturedShader;
import com.sneaky.stargazer.graphics.textures.Texture;
import com.sneaky.stargazer.device.RotationEvent;
import com.sneaky.stargazer.graphics.textures.Animation;
import com.sneaky.stargazer.misc.Constants;
import com.sneaky.stargazer.misc.points.Point2D;

/**
 * Model of Mr. Fox.
 * 
 * @author R. Matt McCann
 */
public class Fox implements Renderable {
    /** Actual height of the fox. */
    private final float height;
    
    /** Last rendering position of the fox. */
    private Point2D lastRenderingPosition;
    
    /** How sensitive the fox's movement is to rotation of the phone. */
    private final float movementSensitivity = 2.5f;
    
    /** Rendering height of the fox. */
    private final float renderingHeight;
    
    /** Position of the fox on the screen. */
    private Point2D renderingPosition;
    
    /** Rendering width of the fox. */
    private final float renderingWidth;
    
    /** Used to draw the fox. */
    private final SimpleTexturedShader shader;
    
    /** Current action state of the fox. */
    private State state = State.STANDING_RIGHT;
    
    /** Texture of the fox. */
    private final Texture texture;
    
    /** Animation sequence of the fox walking. */
    private final Animation walkAnimation;
    
    /** Actual width of the fox. */
    private final float width;
    
    private enum State {
        STANDING_LEFT,
        STANDING_RIGHT,
        RUNNING,
        WALKING_LEFT,
        WALKING_RIGHT,
    }
    
    /** Guice injectable constructor. */
    @Inject
    public Fox(Device device,
               EventBus notifier,
               SimpleTexturedShader shader,
               @Named("Fox") Texture texture,
               @Named("FoxWalk") Animation walkAnimation) {
        this.shader = checkNotNull(shader);
        this.texture = checkNotNull(texture);
        this.walkAnimation = checkNotNull(walkAnimation);
        
        renderingHeight = 0.5f;
        renderingWidth = 0.5f / device.getAspectRatio();
        renderingPosition = new Point2D(0.0f, -0.5f + renderingHeight / 2.0f);
        
        height = 0.2f;
        width = 0.2f;
        
        notifier.register(this);
    }
    
    @Subscribe
    public void handleRotationEvent(RotationEvent event) {
        lastRenderingPosition = renderingPosition.clone();
        
        // Update the position of the fox
        renderingPosition.setX(event.getVector().getY() * movementSensitivity);
        
        // Clamp the floor to the left edge of the screen
        if (renderingPosition.getX() < -0.5f + width / 2.0f) {
            renderingPosition.setX(-0.5f + width / 2.0f);
        }

        // Clamp the ceiling to the right edge of the screen
        if (renderingPosition.getX() > 0.5f - width / 2.0f) {
            renderingPosition.setX(0.5f - width / 2.0f);
        }
        
        float moveDistance = renderingPosition.getX() - lastRenderingPosition.getX();
        float walkThreshold = 0.0025f;
        if (moveDistance >= walkThreshold) { // If the fox is walking to the right
            state = State.WALKING_RIGHT;
        } else if (moveDistance <= -walkThreshold) { // If the fox is walking to the left
            state = State.WALKING_LEFT;
        } else if ((state == State.WALKING_LEFT) || (state == State.STANDING_LEFT)) { // If the fox was last walking left
            state = State.STANDING_LEFT;
            renderingPosition = lastRenderingPosition.clone();
        } else { // If the fox was last walking right
            state = State.STANDING_RIGHT;
            renderingPosition = lastRenderingPosition.clone();
        }
    }
    
    @Override
    public void render(MVP mvp) {
        float[] model = mvp.peekCopyM();
        Matrix.translateM(model, Constants.NO_OFFSET, renderingPosition.getX(), renderingPosition.getY(), 0.0f);
        Matrix.scaleM(model, Constants.NO_OFFSET, renderingWidth, renderingHeight, 1.0f);
        switch(state) {
            case STANDING_LEFT:
                Matrix.rotateM(model, Constants.NO_OFFSET, 180.0f, 0.0f, 1.0f, 0.0f);
                shader.setTexture(texture.getHandle());
                break;
            case STANDING_RIGHT:
                shader.setTexture(texture.getHandle());
                break;
            case WALKING_LEFT:
                Matrix.rotateM(model, Constants.NO_OFFSET, 180.0f, 0.0f, 1.0f, 0.0f);
                shader.setTexture(walkAnimation.getNextFrame().getHandle());
                break;
            case WALKING_RIGHT:
                shader.setTexture(walkAnimation.getNextFrame().getHandle());
                break;
        }
        shader.setMVPMatrix(mvp.collapseM(model));
        shader.draw();
    }
}
