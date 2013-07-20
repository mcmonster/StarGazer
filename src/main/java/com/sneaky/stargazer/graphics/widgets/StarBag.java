package com.sneaky.stargazer.graphics.widgets;

import android.opengl.Matrix;
import static com.google.common.base.Preconditions.*;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sneaky.stargazer.device.Device;
import com.sneaky.stargazer.graphics.MVP;
import com.sneaky.stargazer.graphics.Renderable;
import com.sneaky.stargazer.graphics.shader.SimpleTexturedShader;
import com.sneaky.stargazer.graphics.text.BitmapGlyphString.GlyphStringFactory;
import com.sneaky.stargazer.graphics.text.GlyphString;
import com.sneaky.stargazer.graphics.textures.Texture;
import com.sneaky.stargazer.misc.Constants;
import com.sneaky.stargazer.misc.points.Point2D;

/**
 * Displays the number of captured stars.
 * 
 * @author R. Matt McCann
 */
public class StarBag implements Renderable {
    /** Image of the bag. */
    private final Texture bag;
    
    /** Rendering height of the bag. */
    private float bagHeight = 0.1f;
    
    /** Used to adjust the rendering sizes to compensate for the screen's aspect ratio. */
    private final Device device;
    
    /** Number of stars caught. */
    private int numStarsCaught = 0;
    
    /** Number of stars that must be caught. */
    private int numStarsRequired = 1;
    
    /** Used to draw the star bag. */
    private final SimpleTexturedShader shader;
    
    /** Used to construct the number of stars caught text. */
    private final GlyphString starsCaughtText;
    
    /** Guice injectable constructor. */
    @Inject
    public StarBag(@Named("StarBag") Texture bag,
                   Device device,
                   SimpleTexturedShader shader,
                   GlyphStringFactory textFactory) {
        this.bag = checkNotNull(bag);
        this.device = checkNotNull(device);
        this.shader = checkNotNull(shader);
        starsCaughtText = textFactory.create();
        starsCaughtText.setHeight(1.0f);
        starsCaughtText.setText("" + numStarsCaught + "/" + numStarsRequired);
        starsCaughtText.setPosition(new Point2D(0.0f, 0.0f));
    }
    
    /** {@inheritDocs} */
    @Override
    public void render(MVP mvp) {
        shader.activate();
        
        // Draw the bag itself
        float bagWidth = bagHeight / device.getAspectRatio();
        float bagMargin = 0.07f;
        float bagX = 0.5f - bagMargin / device.getAspectRatio() - bagWidth / 2.0f;
        float bagY = -0.5f + bagMargin + bagHeight / 2.0f;
        float[] model = mvp.peekCopyM();
        Matrix.translateM(model, Constants.NO_OFFSET, bagX, bagY, 0.0f);
        Matrix.scaleM(model, Constants.NO_OFFSET, bagWidth, bagHeight, 1.0f);
        shader.setMVPMatrix(mvp.collapseM(model));
        shader.setTexture(bag.getHandle());
        shader.draw();
        
        // Draw the number of stars caught
        //mvp.pushM(model);
        starsCaughtText.render(mvp);
        //mvp.popM();
    }
}
