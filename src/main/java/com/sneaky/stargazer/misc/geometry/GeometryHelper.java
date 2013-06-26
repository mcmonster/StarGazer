package com.sneaky.stargazer.misc.geometry;

import android.util.Log;
import com.sneaky.stargazer.misc.points.Point2D;

/**
 * A variety of helper functions when dealing with geometry collisions.
 * 
 * @author R. Matt McCann
 */
public abstract class GeometryHelper {
    private static final String TAG = "GeometryHelper";
    
    public static boolean isIntersecting(Circle face1, Circle face2) {
        Point2D pos1 = face1.getPosition();
        Point2D pos2 = face2.getPosition();
        
        Log.d(TAG, "Pos1: " + face1.getPosition());
        Log.d(TAG, "Radius1: " + face1.getRadius());
        Log.d(TAG, "Pos2: " + face2.getPosition());
        Log.d(TAG, "Radius2: " + face2.getRadius());
        Log.d(TAG, "Distance: " + pos1.distanceTo(pos2));
        
        return (pos1.distanceTo(pos2) < face1.getRadius() + face2.getRadius());
    }
    
    public static boolean isIntersecting(Rectangle face1, Rectangle face2) {
        Log.d(TAG, "Face1 Center Pos: " + face1.getPosition().toString());
        Log.d(TAG, "Face1 Height: " + face1.getHeight());
        Log.d(TAG, "Face1 Width: " + face1.getWidth());
        Log.d(TAG, "Face2 Center Pos: " + face2.getPosition().toString());
        Log.d(TAG, "Face2 Height: " + face2.getHeight());
        Log.d(TAG, "Face2 Width: " + face2.getWidth());
        
        float bottomEdge1 = face1.getPosition().getY() - face1.getHeight() / 2.0f;
        float bottomEdge2 = face2.getPosition().getY() - face2.getHeight() / 2.0f;
        float leftEdge1 = face1.getPosition().getX() - face1.getWidth() / 2.0f;
        float leftEdge2 = face2.getPosition().getX() - face2.getWidth() / 2.0f;
        float rightEdge1 = face1.getPosition().getX() + face1.getWidth() / 2.0f;
        float rightEdge2 = face2.getPosition().getX() + face2.getWidth() / 2.0f;
        float topEdge1 = face1.getPosition().getY() + face1.getHeight() / 2.0f;
        float topEdge2 = face2.getPosition().getY() + face2.getHeight() / 2.0f;
        
        boolean bottomEdgeIntersect = bottomEdge1 < topEdge2 && bottomEdge1 > bottomEdge2;
        boolean leftEdgeIntersect = leftEdge1 > leftEdge2 && leftEdge1 < rightEdge2;
        boolean rightEdgeIntersect = rightEdge1 > leftEdge2 && rightEdge1 < rightEdge2;
        boolean topEdgeIntersect = topEdge1 > bottomEdge2 && topEdge1 < topEdge2;
        boolean contains1 = bottomEdge1 > bottomEdge2 && topEdge1 < topEdge2 &&
                leftEdge1 > leftEdge2 && rightEdge1 < rightEdge2;
        boolean contains2 = bottomEdge2 > bottomEdge1 && topEdge2 < topEdge1 &&
                leftEdge2 > leftEdge1 && rightEdge2 < rightEdge1;
        
        Log.d(TAG, "BottomEdgeIntersect? " + (bottomEdgeIntersect ? 'Y' : 'N'));
        Log.d(TAG, "LeftEdgeIntersect? " + (leftEdgeIntersect ? 'Y' : 'N'));
        Log.d(TAG, "RightEdgeIntersect? " + (rightEdgeIntersect ? 'Y' : 'N'));
        Log.d(TAG, "TopEdgeIntersect? " + (topEdgeIntersect ? 'Y' : 'N'));
        Log.d(TAG, "Contains1? " + (contains1 ? 'Y' : 'N'));
        Log.d(TAG, "Contains2? " + (contains2 ? 'Y' : 'N'));
        return bottomEdgeIntersect || leftEdgeIntersect || rightEdgeIntersect ||
                topEdgeIntersect || contains1 || contains2;
    }
}
