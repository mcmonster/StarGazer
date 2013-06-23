package com.sneaky.stargazer.game;

import static com.google.common.base.Preconditions.*;
import com.google.inject.Inject;
import com.sneaky.stargazer.game.background.Background;
import com.sneaky.stargazer.graphics.MVP;
import com.sneaky.stargazer.graphics.Renderer;
import com.sneaky.stargazer.graphics.flow.GameFlowController;
import com.sneaky.stargazer.misc.points.Point2D;

/**
 * Handles rendering the game scene.
 * 
 * @author R. Matt McCann
 */
public class GameRenderer extends Renderer {
    /** Background of the scene. */
    private final Background background;
    
    /**
     * @param background Must not be null.
     * @param gameFlowController Must not be null. 
     */
    @Inject
    public GameRenderer(Background background,
                        GameFlowController gameFlowController) {
        super(gameFlowController);
        
        this.background = checkNotNull(background);
    }
    
    /** {@inheritDocs} */
    @Override
    public void drawFrame(MVP mvp) {
        background.render(mvp);
    }

    public boolean handleClick(Point2D clickLocation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean handleLongPress(Point2D pressLocation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean handlePickUp(Point2D touchLocation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean handleDrag(Point2D moveVector) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean handleDrop(Point2D dropLocation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean handleZoom(float zoomFactor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
