package com.sneaky.stargazer.io;

import static com.google.common.base.Preconditions.*;
import com.sneaky.stargazer.ProxyRenderer;
import com.sneaky.stargazer.misc.points.Point2D;

/**
 * Keeps time until enough has passed to 
 * 
 * @author R. Matt McCann
 */
public class PressTimer implements Runnable {
    private boolean isLongPress = false;
    
    private boolean isShuttingDown = false;
    
    private final long longPressTime = 500;
    
    private final Point2D pressLocation;
    
    private final ProxyRenderer proxyRenderer;
    
    private final long startTime;
    
    public PressTimer(ProxyRenderer proxyRenderer, long startTime, 
                      Point2D pressLocation) {
        checkArgument(startTime > 0, "StartTime must be > 0, got %s", startTime);
        
        this.proxyRenderer = checkNotNull(proxyRenderer);
        this.startTime = startTime;
        this.pressLocation = checkNotNull(pressLocation);
    }
    
    public boolean isLongPress() { return isLongPress; }
    
    @Override
    public void run() {
        while (!isShuttingDown) {
            if (System.currentTimeMillis() - startTime > longPressTime) {
                isLongPress = true;
                proxyRenderer.handleLongPress(pressLocation);
                break;
            }
            
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) { }
        }
    }
    
    public void setIsShuttingDown(final boolean isShuttingDown) {
        this.isShuttingDown = isShuttingDown;
    }
}
