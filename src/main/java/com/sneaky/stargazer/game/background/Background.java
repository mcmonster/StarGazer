package com.sneaky.stargazer.game.background;

import static com.google.common.base.Preconditions.*;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sneaky.stargazer.graphics.MVP;
import com.sneaky.stargazer.graphics.Renderable;
import com.sneaky.stargazer.graphics.shader.SimpleTexturedShader;

/**
 * Renders the background scene of the game.
 * 
 * @author R. Matt McCann
 */
public class Background implements Renderable {
    /** Texture for the static portion of the background. */
    private final int background;
    
    /** Used to draw images on the screen. */
    private final SimpleTexturedShader shader;
    
    @Inject
    public Background(@Named("BackgroundTexture") int background,
                      SimpleTexturedShader shader) {
        checkArgument(background > 0, "Expected background > 0, got %s", background);
        
        this.background = background;
        this.shader = checkNotNull(shader);
    }
    
    /** {@inheritDocs} */
    public void render(MVP mvp) {
        shader.activate();
        
        // Render the static background
        shader.setMVPMatrix(mvp.collapse());
        shader.setTexture(background);
        shader.draw();
    }
}
