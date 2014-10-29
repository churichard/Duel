package duel;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;

public class Character extends Entity {
	
	// Character attributes
	int health;
	
	// Character booleans
	boolean facingRight = true; // Sprite is facing to the right
	boolean isJumping = false; // Sprite is jumping
	boolean isFalling = false; // Sprite is falling
	boolean onPlatform = false; // Sprite is on a platform
	boolean meleeSwing; // Sprite is swinging a melee weapon
	boolean meleeHit; // Melee weapon hit the other character
	
	// Platforms
	int platformNum;
	
	// Weapons
	Melee melee;
	Gun gun;
	
	// Bullets
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	// Weapon switching
	boolean meleeOn = true;
	
	// Bounding rectangle
	Rectangle boundingRectangle;
	
	// Acceleration and velocity
	final int accel = 2;
	final int initVel = 26;
	int vel = initVel;
	
	// Timers
	long bulletPrevTime = 0; // The time that the previous bullet was shot at
	//long swordPrevTime = 0; // The time that the sword was last swung
	long switchPrevTime = 0; // The time that the weapon was last switched
	
	// Initializes a new character
	public Character(String charRef, int health, int initX, int initY, int xDisp, int yDisp, int rows, int cols,
			float animSpeed, boolean facingRight, String meleeRef, String gunRef) {
		super(charRef, initX, initY, xDisp, yDisp, rows, cols, animSpeed);
		
		// Initialize character attributes
		this.health = health;
		this.facingRight = facingRight;
		boundingRectangle = new Rectangle();
		
		// Create melee weapon
		melee = new Melee(this, meleeRef);
		
		// Create gun weapon
		gun = new Gun(this, gunRef);
	}
	
	// Creates a new bullet
	public void addBullet(int gunXDisp, int gunYDisp, int xDisp, int yDisp) {
		bullets.add(new Bullet(gun, "Bullet", gunXDisp, gunYDisp, xDisp, yDisp));
	}
	
	// Removes a bullet
	public void removeBullet(Bullet bullet){
		bullets.remove(bullet);
	}
}
