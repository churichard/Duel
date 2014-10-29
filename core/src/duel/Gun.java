package duel;

import java.util.HashMap;

public class Gun extends Entity {
    
    // Constants
    private final static int CHAR_X_DISP = 46;
    private final static int CHAR_Y_DISP = 40;
	
	// Current weapon
	String currWeap;
	
	// Weapons
	HashMap<String, float[]> weapons = new HashMap<String, float[]>();
	
	public Gun(Character currChar, String gunRef) {
		// Name, row, col, animSpeed
	    super(gunRef, currChar.x + CHAR_X_DISP, currChar.y + CHAR_Y_DISP, currChar.xDisp, currChar.yDisp, 2, 1, 0.0625f);
	    
	    // Changes the x coord of the weapon if the character is not facing right
	    if (!currChar.facingRight)
	        x -= CHAR_X_DISP;
		
		// Initialize guns
		float[] basicGun = { 2, 1, 0.0625f }; // rows, columns, animSpeed
		weapons.put("Gun", basicGun);
		
		// Current weapon
		currWeap = gunRef;
	}
}
