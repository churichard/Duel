package duel;

import com.badlogic.gdx.math.Rectangle;

public class Bullet extends Entity {
	
	// Bounding rectangle
	Rectangle boundingRectangle;
	
	public Bullet(Gun gun, String bulletRef, int gunXDisp, int gunYDisp, int xDisp, int yDisp) {
		super(bulletRef, gun.x + gunXDisp, gun.y + gunYDisp, xDisp, yDisp, 1, 1, 0.0625f);
		boundingRectangle = new Rectangle(gun.x + gunXDisp - 10, gun.y + gunYDisp, 15, 15);
	}
}
