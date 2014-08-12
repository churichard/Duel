package duel;

import java.util.HashMap;

public class Gun extends Entity {
	
	// Current weapon
	String currWeap;
	
	// Weapons
	HashMap<String, float[]> weapons = new HashMap<String, float[]>();
	
	public Gun(Character currChar, String gunRef) {
		// Name, row, col, animSpeed
		super(gunRef, currChar.x + 46, currChar.y + 40, currChar.xDisp, currChar.yDisp, 2, 1, 0.0625f);
		
		// Initialize guns
		float[] basicGun = { 2, 1, 0.0625f }; // rows, columns, animSpeed
		weapons.put("Gun", basicGun);
		
		// Current weapon
		currWeap = gunRef;
	}
}
