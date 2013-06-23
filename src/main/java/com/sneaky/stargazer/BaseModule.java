package com.sneaky.stargazer;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import static com.google.common.base.Preconditions.*;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.sneaky.stargazer.graphics.Renderer;
import com.sneaky.stargazer.misc.Constants;
import java.util.HashMap;
import java.util.Map;

/**
 * Base Guice dependency module.
 * 
 * @author R. Matt McCann
 */
public abstract class BaseModule extends AbstractModule {
    private final ProxyRenderer renderer;

    /** References to all textures used in the game module. */
    private final Map<String, Integer> textures = new HashMap<String, Integer>();
    
    /**
     * @param renderer Must not be null.
     */
    public BaseModule(ProxyRenderer renderer) {
        this.renderer = checkNotNull(renderer);
    }
    
    public void cleanUp() {
        getView().queueEvent(new Runnable() {
            @Override
            public void run() {
                int[] toBeDeleted = new int[textures.size()];
                
                int texturePos = 0;
                for (int texture : textures.values()) {
                    toBeDeleted[texturePos++] = texture;
                }
                
                GLES20.glDeleteTextures(textures.size(), toBeDeleted, Constants.NO_OFFSET);
            }
        });
    }
    
    /** {@inheritDocs} */
    @Override
    protected void configure() {
        bind(Activity.class).toInstance(renderer.getActivity());
        bind(Context.class).toInstance(renderer.getContext());
        bind(EventBus.class).asEagerSingleton();
        bind(ProxyActivity.class).toInstance(renderer.getActivity());
        bind(ProxyRenderer.class).toInstance(renderer);
        bind(ProxyView.class).toInstance(renderer.getView());
        bind(GLSurfaceView.class).toInstance(renderer.getView());
    }
    
    protected Map<String, Integer> getTextures() { return textures; }
    protected ProxyView getView() { return renderer.getView(); }
}
