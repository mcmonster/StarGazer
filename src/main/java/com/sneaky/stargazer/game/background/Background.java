package com.sneaky.stargazer.game.background;

import android.opengl.Matrix;
import static com.google.common.base.Preconditions.*;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sneaky.stargazer.graphics.MVP;
import com.sneaky.stargazer.graphics.Renderable;
import com.sneaky.stargazer.graphics.shader.SimpleTexturedShader;
import com.sneaky.stargazer.graphics.textures.Texture;
import com.sneaky.stargazer.misc.Constants;

/**
 * Renders the background scene of the game.
 * 
 * @author R. Matt McCann
 */
public class Background implements Renderable {
    /** Texture of the shadowy grass. */
    private final Texture grass;
    
    /** Used to draw images on the screen. */
    private final SimpleTexturedShader shader;
    
    /** Texture of the night sky. */
    private final Texture sky;
    
    /** Guice injectable constructor. */
    @Inject
    public Background(@Named("Grass") Texture grass,
                      SimpleTexturedShader shader,
                      @Named("Sky") Texture sky) {
        this.grass = checkNotNull(grass);
        this.shader = checkNotNull(shader);
        this.sky = checkNotNull(sky);
    }
    
    /** {@inheritDocs} */
    @Override
    public void render(MVP mvp) {
        shader.activate();
        
        // Render the sky
        float skyHeight = 0.85f;
        float skyWidth = 1.0f;
        float[] model = mvp.peekCopyM();
        Matrix.translateM(model, Constants.NO_OFFSET, 0.0f, 0.5f - skyHeight / 2.0f, 0.0f);
        Matrix.scaleM(model, Constants.NO_OFFSET, skyWidth, skyHeight, 1.0f);
        shader.setMVPMatrix(mvp.collapseM(model));
        shader.setTexture(sky.getHandle());
        shader.draw();
        
        // Render the grass
        float grassHeight = 1.0f / grass.getAspectRatio();
        float grassWidth = 1.0f;
        model = mvp.peekCopyM();
        Matrix.translateM(model, Constants.NO_OFFSET, 0.0f, -0.35f + grassHeight / 2.0f, 0.0f);
        Matrix.scaleM(model, Constants.NO_OFFSET, grassWidth, grassHeight, 1.0f);
        shader.setMVPMatrix(mvp.collapseM(model));
        shader.setTexture(grass.getHandle());
        shader.draw();
    }
}
