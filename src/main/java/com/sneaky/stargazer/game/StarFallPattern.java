package com.sneaky.stargazer.game;

import com.google.common.base.Optional;

/**
 * Provides the stars for a level.
 * 
 * @author R. Matt McCann
 */
public interface StarFallPattern {
    /** @return Whether or not there are more stars in the fall pattern. */
    boolean hasMoreStars();
    
    /** 
     * @return The next star to fall. If it's not time yet for the next star, 
     * absent star is returned 
     */
    Optional<Star> nextStar();
}