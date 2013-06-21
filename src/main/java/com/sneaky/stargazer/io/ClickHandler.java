package com.sneaky.stargazer.io;

import com.sneaky.stargazer.misc.points.Point2D;

/**
 * Handles the user input click events.
 * 
 * @author R. Matt McCann
 */
public interface ClickHandler {
    /**
     * Handles the click event.
     * 
     * @param clickLocation Location in screen coordinates where the click occurred.
     * @return Whether or not the click event was handled.
     */
    boolean handleClick(final Point2D clickLocation);
}
