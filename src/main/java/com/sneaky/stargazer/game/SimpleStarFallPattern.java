package com.sneaky.stargazer.game;

import static com.google.common.base.Preconditions.*;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sneaky.stargazer.game.Star.StarFactory;
import com.sneaky.stargazer.graphics.textures.Texture;
import com.sneaky.stargazer.misc.points.Point2D;
import java.util.Random;

/**
 * A simple random star fall that goes on endlessly.
 * 
 * @author R. Matt McCann
 */
public class SimpleStarFallPattern implements StarFallPattern {
    /** When the last star was provided. */
    private long lastStarFall = System.currentTimeMillis();
    
    /** Used to randomly generated the placement and fall frequency of the stars. */
    private final Random random = new Random();
    
    /** Simple star texture used for the stars. */
    private final Texture simpleStar;
    
    /** Used to construct stars. */
    private final StarFactory starFactory;
    
    /** How long until the next star fall. */
    private long timeUntilNextStarFall;
    
    /** Guice injectable constructor. */
    @Inject
    public SimpleStarFallPattern(@Named("SimpleStar") Texture simpleStar,
                                 StarFactory starFactory) {
        this.simpleStar = checkNotNull(simpleStar);
        this.starFactory = checkNotNull(starFactory);
        
        timeUntilNextStarFall = 1000 * random.nextInt(3);
    }
    
    /** {@inheritDocs} */
    @Override
    public boolean hasMoreStars() { return true; }

    /** {@inheritDocs} */
    @Override
    public Optional<Star> nextStar() {
        // If not enough time has passed
        if (System.currentTimeMillis() < lastStarFall + timeUntilNextStarFall) {
            return Optional.<Star>absent();
        } else { // If enough time has passed
            lastStarFall = System.currentTimeMillis();
            timeUntilNextStarFall = 1000 * random.nextInt(3);
            
            Star star = starFactory.create();
            star.setFallVelocity(1.0f);
            star.setHeight(0.01f);
            star.setRenderingHeight(0.05f);
            star.setRenderingWidth(0.05f);
            star.setTexture(simpleStar);
            star.setWidth(0.01f);

            float xPos = -0.475f + 0.95f * random.nextFloat();
            float yPos = 0.6f;
            star.setPosition(new Point2D(xPos, yPos));
            star.setRenderingPosition(new Point2D(xPos, yPos));
            
            return Optional.of(star);
        }
    }
}
