package duel;

public class Bullet extends Entity {
	
	public Bullet(Gun gun, String bulletRef, int gunXDisp, int gunYDisp, int xDisp, int yDisp) {
		super(bulletRef, gun.x + gunXDisp, gun.y + gunYDisp, xDisp, yDisp, 1, 1, 0.0625f);
	}
}
