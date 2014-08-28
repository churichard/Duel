package duel;

import java.util.HashMap;

import com.badlogic.gdx.math.Rectangle;

public class Melee extends Entity {
	
	// Current weapon
	String currWeap;
	
	// Weapons
	HashMap<String, float[]> weapons = new HashMap<String, float[]>();
	
	// Bounding rectangle
	Rectangle boundingRectangle;
	
	public Melee(Character currChar, String meleeRef) {
		// Name, row, col, animSpeed
		super(meleeRef, currChar.x + 40, currChar.y + 37, currChar.xDisp, currChar.yDisp, 6, 4, 0.03f);
		
		// Initialize melee weapons
		float[] sword = { 6, 4, 0.03f }; // rows, columns, animSpeed
		weapons.put("Sword", sword);
		boundingRectangle = new Rectangle();
		
		// Current weapon
		currWeap = meleeRef;
	}
}
