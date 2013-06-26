package com.sneaky.stargazer.misc.geometry;

import static com.google.common.base.Preconditions.*;
import com.sneaky.stargazer.misc.points.Point2D;

/**
 * A rectangular face.
 * 
 * @author R. Matt McCann
 */
public class Rectangle {
    private float height;
    
    private Point2D position;
    
    private float width;
    
    public Rectangle(float height, float width, Point2D position) {
        checkArgument(height > 0.0f, "Height must be > 0.0, got %s", height);
        checkArgument(width > 0.0f, "Width must be > 0.0f, got %s", width);
        
        this.height = height;
        this.position = checkNotNull(position);
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public Point2D getPosition() {
        return position;
    }

    public float getWidth() {
        return width;
    }
}
