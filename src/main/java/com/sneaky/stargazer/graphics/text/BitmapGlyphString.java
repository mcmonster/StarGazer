package com.sneaky.stargazer.graphics.text;

import android.graphics.Paint;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import static com.google.common.base.Preconditions.*;
import com.google.inject.Inject;
import com.sneaky.stargazer.ProxyView;
import com.sneaky.stargazer.device.Device;
import com.sneaky.stargazer.graphics.Color;
import com.sneaky.stargazer.graphics.MVP;
import com.sneaky.stargazer.graphics.shader.SimpleTexturedShader;
import com.sneaky.stargazer.graphics.textures.TextureFactory;
import com.sneaky.stargazer.misc.Constants;

/**
 * Glyph string implemented using generated bitmaps.
 * 
 * @author R. Matt McCann
 */
public class BitmapGlyphString extends GlyphString {
    /** Dimensions of the texture. */
    private final float[] aspectRatio = new float[1];

    /** Used to compensate for the screen's aspect ratio. */
    private final Device device;
    
    /** Handle of the raw texture. */
    private int rawTextureHandle = -1;
    
    /** Used to draw the string. */
    private final SimpleTexturedShader shader;
    
    private static final String TAG = "BitmapGlyphString";
    
    /** Used to create new bitmaps. */
    private final TextureFactory textureFactory;
    
    /** Used to retrieve the context for deleting textures. */
    private final ProxyView view;
    
    /** Guice injectable constructor. */
    @Inject
    public BitmapGlyphString(Device device,
                             SimpleTexturedShader shader,
                             TextureFactory textureFactory,
                             ProxyView view) {
        this.device = checkNotNull(device);
        this.shader = checkNotNull(shader);
        this.textureFactory = checkNotNull(textureFactory);
        this.view = checkNotNull(view);
    }
    
    public interface GlyphStringFactory {
        BitmapGlyphString create();
    }
    
    @Override
    public synchronized void render(MVP mvp) {
        if (rawTextureHandle != -1) { // If the texture has been loaded
            Log.d(TAG, "Rendering [Aspect Ratio = " + aspectRatio[0] + "]...");
            shader.activate();
            
            float[] model = mvp.peekCopyM();
            Matrix.translateM(model, Constants.NO_OFFSET, getPosition().getX(), getPosition().getY(), 0.0f);
            Matrix.scaleM(model, Constants.NO_OFFSET, getHeight() * aspectRatio[0] / device.getAspectRatio(), getHeight(), 1.0f);
            shader.setMVPMatrix(mvp.collapseM(model));
            shader.setTexture(rawTextureHandle);
            shader.draw();
        }
    }

    @Override
    protected synchronized void updateRendering() {
        Log.d(TAG, "Updating rendering...");
        String text = getText();
        
        if (rawTextureHandle != -1) { // If a texture has already been loaded
            view.queueEvent(new Runnable() { // Delete the texture
                @Override
                public void run() {
                    int numTextures = 1;
                    GLES20.glDeleteTextures(numTextures, new int[] {rawTextureHandle}, Constants.NO_OFFSET);
                }
            });
        }
        
        rawTextureHandle = textureFactory.texturizeText(text, Color.WHITE,
                Paint.Align.CENTER, 60.0f, aspectRatio);
    }
}
