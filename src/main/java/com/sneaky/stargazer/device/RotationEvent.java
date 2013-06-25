package com.sneaky.stargazer.device;

import android.util.Log;
import static com.google.common.base.Preconditions.*;
import com.sneaky.stargazer.misc.points.Point3D;

/**
 * Triggered when the hardware's rotational sensor detects rotation.
 * 
 * @author R. Matt McCann
 */
public class RotationEvent {
    private static final String TAG = "RotationEvent";
    
    private final Point3D vector;
    
    public RotationEvent(Point3D vector) {
        this.vector = checkNotNull(vector);
        
        Log.d(TAG, "Orientation {" + vector.toString() + "}");
    }
    
    public Point3D getVector() { return vector; }
}
