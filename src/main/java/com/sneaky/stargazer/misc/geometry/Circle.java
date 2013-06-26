package com.sneaky.stargazer.misc.geometry;

import static com.google.common.base.Preconditions.*;
import com.sneaky.stargazer.misc.points.Point2D;

/**
 *
 * @author R. Matt McCann
 */
public class Circle {
    private Point2D position;
    
    private float radius;
    
    public Circle(Point2D position, float radius) {
        checkArgument(radius > 0.0f, "Radius must be > 0.0f, got %s", radius);
        
        this.position = checkNotNull(position);
        this.radius = radius;
    }

    public Point2D getPosition() {
        return position;
    }

    public float getRadius() {
        return radius;
    }
}
