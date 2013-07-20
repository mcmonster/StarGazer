package com.sneaky.stargazer.graphics.text;

import static com.google.common.base.Preconditions.*;
import com.sneaky.stargazer.graphics.Renderable;
import com.sneaky.stargazer.misc.points.Point2D;

/**
 * Model of a renderable string.
 * 
 * @author R. Matt McCann
 */
public abstract class GlyphString implements Renderable {
    /** Rendering height of the text. */
    private float height;
    
    /** Position of the string. */
    private Point2D position = new Point2D(0.0f, 0.0f);
    
    /** The current text comprising the string. */
    private String text;

    public float getHeight() { 
        return height;
    }
    
    public Point2D getPosition() {
        return position;
    }
    
    public String getText() {
        return text;
    }

    public void setHeight(float height) {
        checkArgument(height > 0.0f, "Height must be > 0.0f, got %s", height);
        
    }
    
    public void setPosition(Point2D position) {
        this.position = checkNotNull(position);
    }
    
    public void setText(String text) {
        if (this.text == null || !this.text.equals(text)) { // If the text is different
            this.text = text;
            updateRendering();
        }
    }
    
    protected abstract void updateRendering();
}
