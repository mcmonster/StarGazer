package com.sneaky.stargazer.game;

import android.util.Log;
import com.google.common.base.Optional;
import static com.google.common.base.Preconditions.*;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.sneaky.stargazer.game.background.Background;
import com.sneaky.stargazer.graphics.MVP;
import com.sneaky.stargazer.graphics.Renderer;
import com.sneaky.stargazer.graphics.flow.GameFlowController;
import com.sneaky.stargazer.graphics.flow.GameTickEvent;
import com.sneaky.stargazer.graphics.widgets.ProgressBar;
import com.sneaky.stargazer.misc.geometry.Circle;
import com.sneaky.stargazer.misc.geometry.GeometryHelper;
import com.sneaky.stargazer.misc.points.Point2D;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles rendering the game scene.
 * 
 * @author R. Matt McCann
 */
public class GameRenderer extends Renderer {
    /** Background of the scene. */
    private final Background background;

    /** The fox character. */
    private final Fox fox;
    
    /** How many stars have been captured. */
    private int numCapturedStars = 0;
    
    /** Progress bar represents the fox's progress through the level. */
    private final ProgressBar progressBar;
    
    /** Used to create falling stars. */
    private final StarFallPattern starFallPattern;
    
    /** Current stars on the screen. */
    private final Set<Star> stars = new HashSet<Star>();
    
    private static final String TAG = "GameRenderer";
    
    /**
     * @param background Must not be null.
     * @param gameFlowController Must not be null. 
     */
    @Inject
    public GameRenderer(Background background,
                        Fox fox,
                        GameFlowController gameFlowController,
                        EventBus notifier,
                        ProgressBar progressBar,
                        StarFallPattern starFallPattern) {
        super(gameFlowController, notifier);
        
        this.background = checkNotNull(background);
        this.fox = checkNotNull(fox);
        this.progressBar = checkNotNull(progressBar);
        this.starFallPattern = checkNotNull(starFallPattern);
        
        notifier.register(this);
    }
    
    /** Looks for captured stars. */
    private void detectCapturedStars() {
        Circle foxFace = new Circle(fox.getPosition(), fox.getWidth());
        
        Set<Star> toBeRemoved = new HashSet<Star>();
        for (Star star : stars) { // For each star
            Circle starFace = new Circle(star.getPosition(), star.getWidth());
            
            if (GeometryHelper.isIntersecting(foxFace, starFace)) { // If the fox and the star intersect
                Log.d("Stars", "Captured star!");
                numCapturedStars++;
                toBeRemoved.add(star);
            }
        }
        
        stars.removeAll(toBeRemoved); // Remove the off screen stars
    }
    
    /** {@inheritDocs} */
    @Override
    protected synchronized void drawFrameExt(MVP mvp) {
        long start = System.currentTimeMillis();
        
        background.render(mvp);
        fox.render(mvp);

        for (Star star : stars) { // For each star
            star.render(mvp); // Draw the star
        }
        
        progressBar.render(mvp);
        progressBar.setTimeFrame(120 * 1000);
        
        Log.d(TAG, "Run Time: " + (System.currentTimeMillis() - start));
    }

    /** {@inheritDocs} */
    @Override
    public boolean handleClick(Point2D clickLocation) {
        return true; // Unimplemented
    }

    /** {@inheritDocs} */
    @Override
    public boolean handleLongPress(Point2D pressLocation) {
        return true; // Unimplemented
    }

    /** {@inheritDocs} */
    @Override
    public boolean handlePickUp(Point2D touchLocation) {
        return true; // Unimplemented
    }

    /** {@inheritDocs} */
    @Override
    public boolean handleDrag(Point2D moveVector) {
        return true; // Unimplemented
    }

    /** {@inheritDocs} */
    @Override
    public boolean handleDrop(Point2D dropLocation) {
        return true; // Unimplemented
    }

    /** {@inheritDocs} */
    @Override
    public boolean handleZoom(float zoomFactor) {
        return true; // Unimplemented
    }
    
    /** Removes and adds stars as they come into existence and move off screen. */
    private void manageStars() {
        Log.d(TAG, "# Stars: " + stars.size());
        
        Set<Star> toBeRemoved = new HashSet<Star>();
        for (Star star : stars) { // For each star
            if (star.isOffScreen()) { // If the star is off the screen
                toBeRemoved.add(star); // Mark it for later removal
            }
        }
        
        stars.removeAll(toBeRemoved); // Remove the off screen stars

        // Check if another star has fallen onto the screen
        if (starFallPattern.hasMoreStars()) {
            Optional<Star> star = starFallPattern.nextStar();
            
            if (star.isPresent()) { // If its time for a new star
                stars.add(star.get());
            }
        }
    }
    
    @Subscribe
    public synchronized void onGameTickEvent(GameTickEvent event) {
        detectCapturedStars();
        manageStars(); // Remove any old stars and add new ones
    }
}