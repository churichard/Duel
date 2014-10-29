package duel;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Stage {
    
    // The coordinates of the platforms on the current stage
    int[] plats;
    
    // Backgrounds
    SpriteBatch spriteBatch;
    Texture currTex;
    
    // Textures
    HashMap<String, Texture> textures = new HashMap<String, Texture>();
    
    // Platforms
    HashMap<String, int[]> platCoord = new HashMap<String, int[]>();
    
    public Stage(String stageName) {
        // Initialize the SpriteBatch and stages
        spriteBatch = new SpriteBatch();
        initStages();
        
        // Set the current stage
        plats = platCoord.get(stageName);
        currTex = textures.get(stageName);
    }
    
    // Initialize the platform coordinates and textures for each of the stages
    public void initStages() {
        // Initialize the Greenery stage
        int[] temp = { 50, 280, 270, 290, 600, 814, 270, 290, 210, 660, 400, 420 };
        platCoord.put("Greenery", temp);
        textures.put("Greenery", new Texture("assets/Greenery.png"));
    }
    
    // Renders the background
    public void renderBackground() {
        spriteBatch.begin();
        spriteBatch.draw(currTex, 0, 0);
        spriteBatch.end();
    }
    
    // Returns the coordinates of the stage platforms
    public int[] getStagePlats() {
        return plats;
    }
}
