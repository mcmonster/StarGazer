package com.sneaky.stargazer.game;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.sneaky.R;
import com.sneaky.stargazer.BaseModule;
import com.sneaky.stargazer.ProxyRenderer;
import com.sneaky.stargazer.graphics.TextureFactory;
import java.util.Map;

/**
 * Dependency bindings for the game module.
 * 
 * @author R. Matt McCann
 */
public class GameModule extends BaseModule {
    /** Used to load textures into the OpenGL context. */
    private final TextureFactory textureFactory;
    
    /**
     * @param renderer Must not be null.
     */
    public GameModule(ProxyRenderer renderer) {
        super(renderer);
        
        this.textureFactory = new TextureFactory(renderer.getContext());
    }
    
    /** {@inheritDocs} */
    @Override
    protected void configure() {
        super.configure();
        
        loadTextures();
    }
    
    private void loadTextures() {
        Map<String, Integer> textures = getTextures();
        textures.put("BackgroundTexture", R.drawable.background);
        
        for (Map.Entry<String, Integer> entry : textures.entrySet()) {
            bind(Key.get(Integer.class, Names.named(entry.getKey())))
                .toInstance(textureFactory.loadTexture(entry.getValue()));
        }
    }
}
