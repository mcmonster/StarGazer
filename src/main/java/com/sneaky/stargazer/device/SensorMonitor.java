package com.sneaky.stargazer.device;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import static com.google.common.base.Preconditions.*;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.sneaky.stargazer.misc.points.Point3D;

/**
 * Monitors the phone's internal hardware sensors and triggers events
 * as the values change.
 * 
 * @author R. Matt McCann
 */
public class SensorMonitor implements SensorEventListener {
    /** Used to retrieve the hardware sensor interfaces. */
    private final Activity activity;
    
    /** Current heading of the phone in degrees clock-wise past north. */
    private float heading = 0.0f;
    
    /** Current acceleration value. */
    private float[] gravity;
    
    /** Whether or not the sensor monitor is shutting down. */
    private boolean isShuttingDown = false;
    
    /** Magnetic orientation of the phone. */
    private float[] magneticOrientation;
    
    /** Used to notify interested parties of sensor driven events. */
    private final EventBus notifier;
    
    private static final String TAG = "SensorMonitor";
    
    /** Guice injectable constructor. */
    @Inject
    public SensorMonitor(Activity activity,
                         EventBus notifier) {
        this.activity = checkNotNull(activity);
        this.notifier = checkNotNull(notifier);
        
        onResume(null); // Initially register this as a sensor monitor
        
        notifier.register(this);
    }

    @Subscribe
    public void onPause(OnPauseEvent event) {
        // Unregister the hardware listener to save battery
        SensorManager sensorManager = 
                (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(this);
    }
    
    @Subscribe
    public void onResume(OnResumeEvent event) {
        // Register this as a hardware sensor event listener
        SensorManager sensorManager = 
                (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, 
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, 
                sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), 
                SensorManager.SENSOR_DELAY_GAME);
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                gravity = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magneticOrientation = event.values.clone();
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                Point3D vector = new Point3D(event.values[0], event.values[1], event.values[2]);
                notifier.post(new RotationEvent(vector));
                break;
        }
        
        if (gravity != null && magneticOrientation != null) {
            int threeBy3MatrixSize = 9;
            float[] rotationMatrix = new float[threeBy3MatrixSize];
            float[] remappedRotationMatrix = new float[threeBy3MatrixSize];
            
            // Calculate the orientation matrix
            SensorManager.getRotationMatrix(rotationMatrix, null, gravity, magneticOrientation);
            
            // Remap to the camera's point of view
            //TODO ??
            
            // Retrieve the axis orientations
            float[] values = new float[3];
            SensorManager.getOrientation(rotationMatrix, values);
            //Log.d(TAG, Arrays.toString(values));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { /* Do nothing */ }
}
