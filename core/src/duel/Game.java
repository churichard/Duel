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
	
	// Characters
	Character[] chars = new Character[1];
	Character currChar;
	
	// Platforms
	HashMap<String, int[]> platCoord = new HashMap<String, int[]>();
	
	// Acceleration and velocity
	final int accel = 2;
	final int initVel = 26;
	int vel = initVel;
	
	@Override
	public void create() {
		// Create characters
		// charRef, initX, initY, xDisp, yDisp, rows, cols, animSpeed, meleeRef, gunRef
		chars[0] = new Character("mainChar", 50, 150, 7, 0, 8, 4, 0.0625f, "Sword", "Gun");
		
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
		
		for (Character c : chars) {
			// Setting current character
			currChar = c;
			
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
			
			if (currChar.meleeOn) {
				// Renders melee animation
				renderMeleeAnimation();
			} else {
				// Renders gun
				renderGun();
				// Renders gun firing
				handleGunFiring();
			}
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
		if (currChar.x < -29)
			currChar.x = -29;
		if (currChar.x > 899)
			currChar.x = 899;
		if (currChar.y < currChar.initY)
			currChar.y = currChar.initY;
		if (currChar.y > 550)
			currChar.y = 550;
		
		// For melee weapon
		if (currChar.melee.x < -50)
			currChar.melee.x = -50;
		if (currChar.melee.x > 939)
			currChar.melee.x = 939;
		if (currChar.melee.y <= currChar.melee.initY)
			currChar.melee.y = currChar.melee.initY;
		if (currChar.melee.y >= 570)
			currChar.melee.y = 570;
		
		// For gun
		if (currChar.gun.x < -29)
			currChar.gun.x = -29;
		if (currChar.gun.x > 945)
			currChar.gun.x = 945;
		if (currChar.gun.y <= currChar.gun.initY)
			currChar.gun.y = currChar.gun.initY;
		if (currChar.gun.y >= 570)
			currChar.gun.y = 570;
	}
	
	// Renders main character movement
	public void renderMovement() {
		// Animate main character
		// Go right
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			// If you just turned right
			if (!currChar.facingRight) {
				currChar.facingRight = true;
				currChar.melee.x += 61;
				currChar.gun.x += 46;
			}
			// Moves the player and the gun
			currChar.x += currChar.xDisp;
			currChar.melee.x += currChar.xDisp;
			currChar.gun.x += currChar.xDisp;
			// Checks the boundaries
			checkBounds();
			// If the player is on the ground/platform, then show running animation
			if (currChar.y == currChar.initY || currChar.onPlatform) {
				currChar.anim.renderAnimRight();
			} else {
				renderChar();
			}
		}
		// Go left
		else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			// If you just turned left
			if (currChar.facingRight) {
				currChar.facingRight = false;
				currChar.melee.x -= 61;
				currChar.gun.x -= 46;
			}
			// Moves the player and the gun
			currChar.x += -1 * currChar.xDisp;
			currChar.melee.x += -1 * currChar.xDisp;
			currChar.gun.x += -1 * currChar.xDisp;
			// Checks the boundaries
			checkBounds();
			// If the player is on the ground/platform, then show running animation
			if (currChar.y == currChar.initY || currChar.onPlatform) {
				currChar.anim.renderAnimLeft();
			} else {
				renderChar();
			}
		} else {
			renderChar();
		}
	}
	
	// Handles jumps and falls
	public void handleAirtime() {
		//Jumping
		if (!currChar.isJumping && !currChar.isFalling && Gdx.input.isKeyPressed(Input.Keys.SPACE) || !currChar.isJumping && !currChar.isFalling
				&& Gdx.input.isKeyPressed(Input.Keys.W)) {
			currChar.isJumping = true;
			currChar.y += vel;
			currChar.melee.y += vel;
			currChar.gun.y += vel;
			vel -= accel;
		}
		// Checking to see if the character is jumping or falling
		if (currChar.isJumping || currChar.isFalling) {
			if (currChar.y > currChar.initY) {
				vel -= accel;
				currChar.y += vel;
				currChar.melee.y += vel;
				currChar.gun.y += vel;
			} else {
				currChar.isJumping = false;
				currChar.isFalling = false;
				vel = initVel;
			}
		}
	}
	
	// Handles platforms
	public void handlePlatforms() {
		// Iterate over each platform
		for (int i = 0; i < stage.length; i += 4) {
			if (vel < 0 && currChar.x >= stage[i] && currChar.x <= stage[i + 1] && currChar.y > stage[i + 2]
					&& currChar.y < stage[i + 3]) {
				if (currChar.platformNum == i / 4 && !currChar.isFalling || currChar.platformNum != i / 4) {
					currChar.isJumping = false;
					currChar.isFalling = false;
					currChar.onPlatform = true;
					currChar.platformNum = i / 4;
					vel = initVel;
					currChar.y = stage[i + 2] + 10;
					currChar.melee.y = stage[i + 2] + 10 + 40;
					currChar.gun.y = stage[i + 2] + 10 + 40;
				}
			}
		}
		// Checking to see if the character is falling
		if (currChar.onPlatform && !currChar.isJumping && currChar.x < stage[4 * currChar.platformNum] || currChar.onPlatform && !currChar.isJumping
				&& currChar.x > stage[4 * currChar.platformNum + 1]) {
			currChar.onPlatform = false;
			currChar.isFalling = true;
			vel = 0;
		}
		// Checking to see if the character is dropping
		if (currChar.onPlatform && !currChar.isJumping && Gdx.input.isKeyPressed(Input.Keys.S)) {
			currChar.onPlatform = false;
			currChar.isFalling = true;
			vel = 0;
		}
	}
	
	// Handles weapon switching
	public void handleWeapons() {
		if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && (TimeUtils.millis() - currChar.weaponPrevTime) > 300) {
			currChar.weaponPrevTime = TimeUtils.millis();
			if (currChar.meleeOn)
				currChar.meleeOn = false;
			else
				currChar.meleeOn = true;
		}
	}
	
	// Render static character
	public void renderChar() {
		if (currChar.facingRight) {
			currChar.anim.renderStaticRight();
		} else {
			currChar.anim.renderStaticLeft();
		}
	}
	
	// Renders melee weapon
	public void renderMelee() {
		if (currChar.facingRight) {
			currChar.melee.anim.renderStaticRight();
		} else {
			currChar.melee.anim.renderStaticLeft();
		}
	}
	
	// Renders melee weapon animation
	public void renderMeleeAnimation() {
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && (TimeUtils.millis() - currChar.swordPrevTime) > 150) {
			currChar.meleeSwing = true;
			currChar.swordPrevTime = TimeUtils.millis();
		}
		if (currChar.meleeSwing) {
			if (currChar.facingRight) {
				currChar.melee.anim.renderAnimRight();
			} else {
				currChar.melee.anim.renderAnimLeft();
			}
			if (currChar.melee.anim.currentFrame == currChar.melee.anim.rightFrames[0]
					|| currChar.melee.anim.currentFrame == currChar.melee.anim.leftFrames[0]) {
				currChar.meleeSwing = false;
			}
		} else {
			// Renders melee weapon
			renderMelee();
		}
	}
	
	// Render static gun
	public void renderGun() {
		if (currChar.facingRight) {
			currChar.gun.anim.renderStaticRight();
		} else {
			currChar.gun.anim.renderStaticLeft();
		}
	}
	
	// Handles gun firing
	public void handleGunFiring() {
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && (TimeUtils.millis() - currChar.bulletPrevTime) > 300) {
			// Create bullets
			if (currChar.facingRight) {
				currChar.addBullet(46, 15, 10, 0);
			} else {
				currChar.addBullet(-18, 15, -10, 0);
			}
			// Update the time that the bullet shot at
			currChar.bulletPrevTime = TimeUtils.millis();
		}
	}
	
	// Render bullet
	public void renderBullet() {
		// ArrayList of bullets to be removed
		ArrayList<Bullet> removeBullets = new ArrayList<Bullet>();
		// Render each bullet
		for (int i = 0; i < currChar.bullets.size(); i++) {
			currChar.bullets.get(i).anim.renderStaticRight();
			currChar.bullets.get(i).x += currChar.bullets.get(i).xDisp;
			currChar.bullets.get(i).y += currChar.bullets.get(i).yDisp;
			if (currChar.bullets.get(i).x > 1000 || currChar.bullets.get(i).x < -50) {
				removeBullets.add(currChar.bullets.get(i));
			}
		}
		// Remove the bullets that are off the screen
		for (int i = 0; i < removeBullets.size(); i++) {
			currChar.bullets.remove(removeBullets.get(i));
		}
	}
}