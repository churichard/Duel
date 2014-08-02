package duel;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

public class Game extends ApplicationAdapter {
	
	// Game
	int[] stage; // The stage that the player is playing on
	
	// Backgrounds
	Texture greenery;
	SpriteBatch spriteBatch;
	
	// Main character
	Animator mainChar;
	final int mainCharInitX = 50;
	final int mainCharInitY = 150;
	
	boolean facingRight = true;
	boolean isJumping = false;
	boolean isFalling = false;
	boolean onPlatform = false;
	
	final int accel = 2;
	final int initVel = 24;
	int vel = initVel;
	
	// Platforms
	HashMap<String, int[]> platCoord = new HashMap<String, int[]>();
	int platformNum;
	
	// Weapon switching
	boolean meleeOn = true;
	
	// Melee
	Animator sword;
	boolean meleeSwing;
	final int swordInitX = mainCharInitX + 40;
	final int swordInitY = mainCharInitY + 37;
	
	// Guns
	Animator gun;
	final int gunInitX = mainCharInitX + 46;
	final int gunInitY = mainCharInitY + 40;
	
	// Bullets
	ArrayList<Animator> bullet = new ArrayList<Animator>();
	
	long bulletPrevTime = 999999999; // The time that the previous bullet was shot at
	long swordPrevTime = 999999999; // The time that the sword was last swung
	long weaponPrevTime = 999999999; // The time that the weapon was last switched
	
	@Override
	public void create() {
		// Create main character animations
		mainChar = new Animator("assets/MainChar.png", mainCharInitX, mainCharInitY, 7, 0, 8, 4, 0.0625f);
		mainChar.create();
		
		// Create gun animations
		gun = new Animator("assets/Gun.png", gunInitX, gunInitY, 7, 0, 2, 1, 0.0625f);
		gun.create();
		
		// Create sword
		sword = new Animator("assets/Sword.png", swordInitX, swordInitY, 7, 0, 6, 4, 0.02f);
		sword.create();
		
		// Create backgrounds
		spriteBatch = new SpriteBatch();
		greenery = new Texture("assets/Greenery.png");
		
		// Initialize platform coordinates
		int[] temp = { 50, 280, 270, 290, 600, 814, 270, 290, 210, 660, 400, 420 };
		platCoord.put("Greenery", temp);
		
		// Initialize the stage that the player is on
		stage = platCoord.get("Greenery");
	}
	
	@Override
	public void render() {
		// Renders background
		renderBackground();
		// Check bounds
		checkBounds();
		// Handles platforms
		handlePlatforms();
		
		// Renders main character movement
		renderMovement();
		// Handles main character jumps and falls
		handleAirtime();
		
		// Handles weapon switching
		handleWeapons();
		
		// Renders bullet
		renderBullet();
		
		if (meleeOn) {
			// Renders melee animation
			renderMeleeAnimation();
		} else {
			// Renders gun
			renderGun();
			// Renders gun firing
			handleGunFiring();
		}
	}
	
	// Renders the background
	public void renderBackground() {
		spriteBatch.begin();
		spriteBatch.draw(greenery, 0, 0);
		spriteBatch.end();
	}
	
	// Checks that the characters are within the bounds
	public void checkBounds() {
		// For main character
		if (mainChar.x < -29)
			mainChar.x = -29;
		if (mainChar.x > 899)
			mainChar.x = 899;
		if (mainChar.y < mainCharInitY)
			mainChar.y = mainCharInitY;
		if (mainChar.y > 550)
			mainChar.y = 550;
		
		if (sword.x < -50)
			sword.x = -50;
		if (sword.x > 939)
			sword.x = 939;
		if (sword.y <= swordInitX)
			sword.y = swordInitY;
		if (sword.y >= 570)
			sword.y = 570;
		
		if (gun.x < -29)
			gun.x = -29;
		if (gun.x > 945)
			gun.x = 945;
		if (gun.y <= gunInitY)
			gun.y = gunInitY;
		if (gun.y >= 570)
			gun.y = 570;
	}
	
	// Renders main character movement
	public void renderMovement() {
		// Animate main character
		// Go right
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			// If you just turned right
			if (!facingRight) {
				facingRight = true;
				sword.x += 61;
				gun.x += 46;
			}
			// Moves the player and the gun
			mainChar.x += mainChar.xDisp;
			sword.x += sword.xDisp;
			gun.x += gun.xDisp;
			// Checks the boundaries
			checkBounds();
			// If the player is on the ground/platform, then show running animation
			if (mainChar.y == mainCharInitY || onPlatform) {
				mainChar.renderRight();
			} else {
				renderMainChar();
			}
		}
		// Go left
		else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			// If you just turned left
			if (facingRight) {
				facingRight = false;
				sword.x -= 61;
				gun.x -= 46;
			}
			// Moves the player and the gun
			mainChar.x += -1 * mainChar.xDisp;
			sword.x += -1 * sword.xDisp;
			gun.x += -1 * gun.xDisp;
			// Checks the boundaries
			checkBounds();
			// If the player is on the ground/platform, then show running animation
			if (mainChar.y == mainCharInitY || onPlatform) {
				mainChar.renderLeft();
			} else {
				renderMainChar();
			}
		} else {
			renderMainChar();
		}
	}
	
	// Handles jumps and falls
	public void handleAirtime() {
		//Jumping
		if (!isJumping && !isFalling && Gdx.input.isKeyPressed(Input.Keys.SPACE) || !isJumping && !isFalling
				&& Gdx.input.isKeyPressed(Input.Keys.W)) {
			isJumping = true;
			mainChar.y += vel;
			sword.y += vel;
			gun.y += vel;
			vel -= accel;
		}
		if (isJumping && !isFalling) {
			if (mainChar.y > mainCharInitY) {
				mainChar.y += vel;
				sword.y += vel;
				gun.y += vel;
				vel -= accel;
			} else {
				isJumping = false;
				vel = initVel;
			}
		}
		if (isFalling && !isJumping) {
			if (mainChar.y > mainCharInitY) {
				vel -= accel;
				mainChar.y += vel;
				sword.y += vel;
				gun.y += vel;
			} else {
				isFalling = false;
				vel = initVel;
			}
		}
	}
	
	// Handles platforms
	public void handlePlatforms() {
		// Iterate over each platform
		for (int i = 0; i < stage.length; i += 4) {
			if (vel < 0 && mainChar.x >= stage[i] && mainChar.x <= stage[i + 1] && mainChar.y > stage[i + 2]
					&& mainChar.y < stage[i + 3]) {
				isJumping = false;
				isFalling = false;
				onPlatform = true;
				platformNum = i / 4;
				vel = initVel;
				mainChar.y = stage[i + 2] + 10;
				sword.y = stage[i + 2] + 10 + 40;
				gun.y = stage[i + 2] + 10 + 40;
			}
		}
		// Checking to see if the character is falling
		if (onPlatform && !isJumping && mainChar.x < stage[4 * platformNum] || onPlatform && !isJumping
				&& mainChar.x > stage[4 * platformNum + 1]) {
			onPlatform = false;
			isFalling = true;
			vel = 0;
			platformNum = -1;
		}
	}
	
	// Handles weapon switching
	public void handleWeapons() {
		if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && (TimeUtils.millis() - weaponPrevTime) > 300) {
			weaponPrevTime = TimeUtils.millis();
			if (meleeOn)
				meleeOn = false;
			else
				meleeOn = true;
		}
	}
	
	// Render static main character
	public void renderMainChar() {
		if (facingRight) {
			mainChar.spriteBatch.begin();
			mainChar.spriteBatch.draw(mainChar.walkRightFrames[0], mainChar.x, mainChar.y);
			mainChar.spriteBatch.end();
		} else {
			mainChar.spriteBatch.begin();
			mainChar.spriteBatch.draw(mainChar.walkLeftFrames[0], mainChar.x, mainChar.y);
			mainChar.spriteBatch.end();
		}
	}
	
	// Renders melee weapon
	public void renderMelee() {
		if (facingRight) {
			sword.spriteBatch.begin();
			sword.spriteBatch.draw(sword.walkRightFrames[0], sword.x, sword.y);
			sword.spriteBatch.end();
		} else {
			sword.spriteBatch.begin();
			sword.spriteBatch.draw(sword.walkLeftFrames[0], sword.x, sword.y);
			sword.spriteBatch.end();
		}
	}
	
	// Renders melee weapon animation
	public void renderMeleeAnimation() {
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && (TimeUtils.millis() - swordPrevTime) > 150) {
			meleeSwing = true;
			swordPrevTime = TimeUtils.millis();
		}
		if (meleeSwing) {
			if (facingRight) {
				sword.renderRight();
			} else {
				sword.renderLeft();
			}
			if (sword.currentFrame == sword.walkRightFrames[0]
					|| sword.currentFrame == sword.walkLeftFrames[0]) {
				meleeSwing = false;
			}
		} else {
			// Renders melee weapon
			renderMelee();
		}
	}
	
	// Render static gun
	public void renderGun() {
		if (facingRight) {
			gun.spriteBatch.begin();
			gun.spriteBatch.draw(gun.walkRightFrames[0], gun.x, gun.y);
			gun.spriteBatch.end();
		} else {
			gun.spriteBatch.begin();
			gun.spriteBatch.draw(gun.walkLeftFrames[0], gun.x, gun.y);
			gun.spriteBatch.end();
		}
	}
	
	// Handles gun firing
	public void handleGunFiring() {
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && (TimeUtils.millis() - bulletPrevTime) > 300) {
			// Create bullets
			if (facingRight) {
				bullet.add(new Animator("assets/Bullet.png", gun.x + 46, gun.y + 15, 10, 0, 1, 1, 0.0625f));
			} else {
				bullet.add(new Animator("assets/Bullet.png", gun.x - 46, gun.y + 15, -10, 0, 1, 1, 0.0625f));
			}
			bullet.get(bullet.size() - 1).create();
			// Update the time that the bullet shot at
			bulletPrevTime = TimeUtils.millis();
		}
	}
	
	// Render bullet
	public void renderBullet() {
		// ArrayList of bullets to be removed
		ArrayList<Animator> removeBullet = new ArrayList<Animator>();
		// Render each bullet
		for (int i = 0; i < bullet.size(); i++) {
			bullet.get(i).spriteBatch.begin();
			bullet.get(i).spriteBatch.draw(bullet.get(i).walkSheet, bullet.get(i).x, bullet.get(i).y);
			bullet.get(i).spriteBatch.end();
			bullet.get(i).x += bullet.get(i).xDisp;
			bullet.get(i).y += bullet.get(i).yDisp;
			if (bullet.get(i).x > 1000 || bullet.get(i).x < -50) {
				removeBullet.add(bullet.get(i));
			}
		}
		// Remove the bullets that are off the screen
		for (int i = 0; i < removeBullet.size(); i++) {
			bullet.remove(removeBullet.get(i));
		}
	}
}
