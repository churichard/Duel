package duel;

import java.util.HashMap;

import com.badlogic.gdx.math.Rectangle;

public class Melee extends Entity {
    
    // Constants
    private final static int CHAR_X_DISP = 40;
    private final static int CHAR_Y_DISP = 37;
	
	// Current weapon
	String currWeap;
	
	// Weapons
	HashMap<String, float[]> weapons = new HashMap<String, float[]>();
	
	// Bounding rectangle
	Rectangle boundingRectangle;
	
	public Melee(Character currChar, String meleeRef) {
		// Name, row, col, animSpeed
		super(meleeRef, currChar.x + CHAR_X_DISP, currChar.y + CHAR_Y_DISP, currChar.xDisp, currChar.yDisp, 6, 4, 0.03f);
		
		// Changes the x coord of the weapon if the character is not facing right
		if (!currChar.facingRight)
            x -= Render.MELEE_X_DISP;
		
		// Initialize melee weapons
		float[] sword = { 6, 4, 0.03f }; // rows, columns, animSpeed
		weapons.put("Sword", sword);
		boundingRectangle = new Rectangle();
		
		// Current weapon
		currWeap = meleeRef;
	}
}
