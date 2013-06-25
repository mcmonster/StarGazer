package com.sneaky.stargazer;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import static com.google.common.base.Preconditions.*;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.sneaky.stargazer.graphics.textures.Animation;
import com.sneaky.stargazer.graphics.textures.Texture;
import com.sneaky.stargazer.misc.Constants;
import java.util.HashMap;
import java.util.Map;

/**
 * Base Guice dependency module.
 * 
 * @author R. Matt McCann
 */
public abstract class BaseModule extends AbstractModule {
    /** References to all animations used in the module. */
    private final Map<String, Animation> animations = new HashMap<String, Animation>();
    
    /** Can be retrieved for delegated rendering purposes. */
    private final ProxyRenderer renderer;

    /** References to all textures used in the module. */
    private final Map<String, Texture> textures = new HashMap<String, Texture>();
    
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
                for (Texture texture : textures.values()) {
                    toBeDeleted[texturePos++] = texture.getHandle();
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
        bind(GLSurfaceView.class).toInstance(renderer.getView());
        bind(ProxyActivity.class).toInstance(renderer.getActivity());
        bind(ProxyRenderer.class).toInstance(renderer);
        bind(ProxyView.class).toInstance(renderer.getView());
    }
    
    protected Map<String, Animation> getAnimations() { return animations; }
    protected Map<String, Texture> getTextures() { return textures; }
    protected ProxyView getView() { return renderer.getView(); }
}
