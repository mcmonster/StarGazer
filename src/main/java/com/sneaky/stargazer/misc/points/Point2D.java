package com.sneaky.stargazer.misc.points;

import android.util.FloatMath;
import android.view.MotionEvent;

/**
 * Simple two-dimensional point.
 * 
 * @author R. Matt McCann
 */
public class Point2D {
    private float x = 0.0f;
    private float y = 0.0f;
    
    public Point2D() { }
    
    public Point2D(MotionEvent event) {
        x = event.getX();
        y = event.getY();
    }
    
    public Point2D(final Point2D source) {
        this.x = source.x;
        this.y = source.y;
    }
    
    public Point2D(final float x, final float y) {
        this.x = x;
        this.y = y;
    }
    
    public void addToX(float xAdd) { x += xAdd; }
    public void addToY(float yAdd) { y += yAdd; }

    public float distanceTo(Point2D target) {
        float xFactor = this.x - target.x;
        float yFactor = this.y - target.y;
        
        return FloatMath.sqrt(xFactor * xFactor + yFactor * yFactor);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point2D other = (Point2D) obj;
        if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x)) {
            return false;
        }
        if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y)) {
            return false;
        }
        return true;
    }
    
    public float getX() { return x; }
    public float getY() { return y; }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Float.floatToIntBits(this.x);
        hash = 79 * hash + Float.floatToIntBits(this.y);
        return hash;
    }
    
    public void normalize(float max) {
        x /= max;
        y /= max;
    }
    
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    
    public Point2D subtract(final Point2D subtractee) {
        Point2D result = new Point2D();
        
        result.x = this.x - subtractee.x;
        result.y = this.y - subtractee.y;
        
        return result;
    }
}