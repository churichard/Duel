package duel;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.TimeUtils;

public class Game extends ApplicationAdapter {
    
    // Stage
    Stage stage;
	
	// Characters
	static Character[] chars = new Character[2];
	Character currChar;
	
	@Override
	public void create() {
		// Create characters
		// charRef, health, initX, initY, xDisp, yDisp, rows, cols, animSpeed, facingRight, meleeRef, gunRef
		chars[0] = new Character("mainChar", 1000, 50, 150, 7, 0, 8, 4, 0.0625f, true, "Sword", "Gun");
		chars[1] = new Character("mainChar2", 1000, 800, 150, 7, 0, 8, 4, 0.0625f, false, "Sword", "Gun");
		
		// Initialize stage
		stage = new Stage("Greenery");
		
		// Initialize font
		Render.font = new BitmapFont();
		Render.font.setScale(2);
	}
	
	@Override
	public void render() {
		// Renders background
		stage.renderBackground();
		
		for (Character c : chars) {
			// Setting current character
			currChar = c;
			
			// Check bounds
			checkBounds(currChar);
			
			int[] stagePlats = stage.getStagePlats();
			// Handles platforms
			handlePlatforms(stagePlats);
			
			// Renders main character movement
			Render.renderMovement(currChar);
			// Handles main character jumps and falls
			handleAirtime();
			
			// Handles weapon switching
			handleWeapons();
			
			// Renders bullet
			Render.renderBullets(currChar);
			
			// Renders melee
			if (currChar.meleeOn) {
				// Renders melee animation
				Render.renderMeleeAnimation(currChar);
			} else {
				// Renders gun
				Render.renderGun(currChar);
				// Handles gun firing
				handleGunFiring();
			}
			
			// Update character status
			updateCharacters();
			
			// Updates bounding rectangles
			updateBoundingRectangles();
		}
		
		// Draws the GUI
		Render.drawGUI();
	}
	
	// Checks that the characters are within the bounds
	public static void checkBounds(Character c) {
		// For main character
		if (c.x < -29)
			c.x = -29;
		if (c.x > 899)
			c.x = 899;
		if (c.y < c.initY)
			c.y = c.initY;
		if (c.y > 550)
			c.y = 550;
		
		// For melee weapon
		if (c.melee.x < -50)
			c.melee.x = -50;
		if (c.melee.x > 939)
			c.melee.x = 939;
		if (c.melee.y <= c.melee.initY)
			c.melee.y = c.melee.initY;
		if (c.melee.y >= 570)
			c.melee.y = 570;
		
		// For gun
		if (c.gun.x < -29)
			c.gun.x = -29;
		if (c.gun.x > 945)
			c.gun.x = 945;
		if (c.gun.y <= c.gun.initY)
			c.gun.y = c.gun.initY;
		if (c.gun.y >= 570)
			c.gun.y = 570;
	}
	
	// Updates character status
    public void updateCharacters() {
        Character otherChar;
        
        // Sets the other character
        if (currChar == chars[0]) {
            otherChar = chars[1];
        } else {
            otherChar = chars[0];
        }
        
        // If the other char is hit by a melee swing
        if (currChar.meleeSwing && currChar.meleeHit == false
                && currChar.melee.boundingRectangle.overlaps(otherChar.boundingRectangle)) {
            otherChar.health -= 100;
            currChar.meleeHit = true;
        }
        // If the other char is hit by a bullet
        for (int i = 0; i < currChar.bullets.size(); i++) {
            if (currChar.bullets.get(i).boundingRectangle.overlaps(otherChar.boundingRectangle)) {
                otherChar.health -= 50;
                currChar.removeBullet(currChar.bullets.get(i));
            }
        }
    }
    
    // Updates bounding rectangles
    public void updateBoundingRectangles() {
        // Characters and melee weapons
        if (currChar.facingRight) {
            currChar.boundingRectangle.set(currChar.x + 30, currChar.y, 50, 90);
            if (currChar.meleeSwing && currChar.meleeOn)
                currChar.melee.boundingRectangle.set(currChar.x + 50, currChar.y + 40, 60, 60);
            else if (currChar.meleeOn)
                currChar.melee.boundingRectangle.set(currChar.x + 50, currChar.y + 50, 35, 50);
        } else {
            currChar.boundingRectangle.set(currChar.x + 10, currChar.y, 50, 90);
            if (currChar.meleeSwing && currChar.meleeOn)
                currChar.melee.boundingRectangle.set(currChar.x - 20, currChar.y + 40, 60, 60);
            else if (currChar.meleeOn)
                currChar.melee.boundingRectangle.set(currChar.x + 5, currChar.y + 50, 35, 50);
        }
        // Bullets
        for (int i = 0; i < currChar.bullets.size(); i++) {
            currChar.bullets.get(i).boundingRectangle.set(currChar.bullets.get(i).x - 10, currChar.bullets.get(i).y, 15, 15);
        }
    }
    
    // Handles platforms
    public void handlePlatforms(int[] stagePlats) {
        // Iterate over each platform
        for (int i = 0; i < stagePlats.length; i += 4) {
            if (currChar.vel < 0 && currChar.x >= stagePlats[i] && currChar.x <= stagePlats[i + 1]
                    && currChar.y > stagePlats[i + 2] && currChar.y < stagePlats[i + 3]) {
                if (currChar.platformNum == i / 4 && !currChar.isFalling || currChar.platformNum != i / 4) {
                    currChar.isJumping = false;
                    currChar.isFalling = false;
                    currChar.onPlatform = true;
                    currChar.platformNum = i / 4;
                    currChar.vel = currChar.initVel;
                    currChar.y = stagePlats[i + 2] + 10;
                    currChar.boundingRectangle.set(currChar.x, currChar.y, 90, 90);
                    currChar.melee.y = stagePlats[i + 2] + 10 + 40;
                    currChar.gun.y = stagePlats[i + 2] + 10 + 40;
                }
            }
        }
        // Checking to see if the character is falling
        if (currChar.onPlatform && !currChar.isJumping && currChar.x < stagePlats[4 * currChar.platformNum]
                || currChar.onPlatform && !currChar.isJumping
                && currChar.x > stagePlats[4 * currChar.platformNum + 1]) {
            currChar.onPlatform = false;
            currChar.isFalling = true;
            currChar.vel = 0;
        }
        // Checking to see if the character is dropping
        if (currChar.onPlatform && !currChar.isJumping && Gdx.input.isKeyPressed(Input.Keys.S)
                && currChar == chars[0] || currChar.onPlatform && !currChar.isJumping
                && Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN) && currChar == chars[1]) {
            currChar.onPlatform = false;
            currChar.isFalling = true;
            currChar.vel = 0;
        }
    }
	
	// Handles jumps and falls
	public void handleAirtime() {
		//Jumping
		if (!currChar.isJumping && !currChar.isFalling && Gdx.input.isKeyPressed(Input.Keys.W)
				&& currChar == chars[0] || !currChar.isJumping && !currChar.isFalling
				&& Gdx.input.isKeyPressed(Input.Keys.DPAD_UP) && currChar == chars[1]) {
			currChar.isJumping = true;
			currChar.y += currChar.vel;
			currChar.melee.y += currChar.vel;
			currChar.gun.y += currChar.vel;
			currChar.vel -= currChar.accel;
		}
		// Checking to see if the character is jumping or falling
		if (currChar.isJumping || currChar.isFalling) {
			if (currChar.y > currChar.initY) {
				currChar.vel -= currChar.accel;
				currChar.y += currChar.vel;
				currChar.melee.y += currChar.vel;
				currChar.gun.y += currChar.vel;
			} else {
				currChar.isJumping = false;
				currChar.isFalling = false;
				currChar.vel = currChar.initVel;
			}
		}
	}
	
	// Handles weapon switching
	public void handleWeapons() {
		if ((Gdx.input.isKeyPressed(Input.Keys.B) && (TimeUtils.millis() - currChar.switchPrevTime) > 300 && currChar == chars[0])
				|| (Gdx.input.isKeyPressed(Input.Keys.LEFT_BRACKET)
						&& (TimeUtils.millis() - currChar.switchPrevTime) > 300 && currChar == chars[1])) {
			currChar.switchPrevTime = TimeUtils.millis();
			if (currChar.meleeOn)
				currChar.meleeOn = false;
			else
				currChar.meleeOn = true;
		}
	}
	
	// Handles gun firing
	public void handleGunFiring() {
		if (Gdx.input.isKeyPressed(Input.Keys.V) && (TimeUtils.millis() - currChar.bulletPrevTime) > 400
				&& currChar == chars[0] || Gdx.input.isKeyPressed(Input.Keys.RIGHT_BRACKET)
				&& (TimeUtils.millis() - currChar.bulletPrevTime) > 400 && currChar == chars[1]) {
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
}