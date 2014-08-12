package duel;

import java.util.HashMap;

public class Melee extends Entity {	
	
	// Current weapon
	String currWeap;
	
	// Weapons
	HashMap<String, float[]> weapons = new HashMap<String, float[]>();
	
	public Melee(Character currChar, String meleeRef) {
		// Name, row, col, animSpeed
		super(meleeRef, currChar.x + 40, currChar.y + 37, currChar.xDisp, currChar.yDisp, 6, 4, 0.02f);
		
		// Initialize melee weapons
		float[] sword = { 6, 4, 0.02f }; // rows, columns, animSpeed
		weapons.put("Sword", sword);
		
		// Current weapon
		currWeap = meleeRef;
	}
}
