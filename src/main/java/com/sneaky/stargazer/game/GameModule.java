package com.sneaky.stargazer.game;

import com.google.inject.Key;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import com.sneaky.stargazer.R;
import com.sneaky.stargazer.BaseModule;
import com.sneaky.stargazer.ProxyRenderer;
import com.sneaky.stargazer.graphics.Renderer;
import com.sneaky.stargazer.graphics.textures.Texture;
import com.sneaky.stargazer.graphics.textures.TextureFactory;
import com.sneaky.stargazer.device.SensorMonitor;
import com.sneaky.stargazer.game.Star.StarFactory;
import com.sneaky.stargazer.graphics.text.BitmapGlyphString.GlyphStringFactory;
import com.sneaky.stargazer.graphics.textures.Animation;
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
        
        bind(Renderer.class).to(GameRenderer.class);
        bind(SensorMonitor.class).asEagerSingleton();
        bind(StarFallPattern.class).to(SimpleStarFallPattern.class);
        
        installFactories();
        loadAnimations();
        loadTextures();
    }
    
    private void installFactories() {
        install(new FactoryModuleBuilder().build(GlyphStringFactory.class));
        install(new FactoryModuleBuilder().build(StarFactory.class));
    }
    
    private void loadAnimations() {
        Map<String, Animation> animations = getAnimations();
        
        animations.put("FoxWalk", new Animation(new int[] {R.drawable.foxwalk1, 
            R.drawable.foxwalk2, R.drawable.foxwalk3, R.drawable.foxwalk4, 
            R.drawable.foxwalk5, R.drawable.foxwalk6, R.drawable.foxwalk7}, 5));
        
        for (Map.Entry<String, Animation> entry : animations.entrySet()) { // Load the animation textures
            Animation animation = entry.getValue();
            
            for (Texture texture : animation.getFrames()) {
                texture.setHandle(textureFactory.loadTexture(texture.getRawImage()));
            }
            
            bind(Key.get(Animation.class, Names.named(entry.getKey()))).toInstance(animation);
        }
    }
    
    private void loadTextures() {
        Map<String, Texture> textures = getTextures();
        textures.put("Fox", new Texture(R.drawable.foxstanding));
        textures.put("Grass", new Texture(8.0f, R.drawable.grass));
        textures.put("ProgressBar", new Texture(8.0f, R.drawable.progressbar));
        textures.put("ProgressBarMoon", new Texture(R.drawable.progressbarmoon));
        textures.put("SimpleStar", new Texture(R.drawable.simplestar));
        textures.put("Sky", new Texture(4.0f, R.drawable.sky));
        textures.put("StarBag", new Texture(R.drawable.starbag));
        
        for (Map.Entry<String, Texture> entry : textures.entrySet()) {
            Texture texture = entry.getValue();
            texture.setHandle(textureFactory.loadTexture(texture.getRawImage()));
            
            bind(Key.get(Texture.class, Names.named(entry.getKey()))).toInstance(texture);
        }
    }
}
