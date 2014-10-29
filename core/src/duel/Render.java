package duel;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Render {
    
    // Constants
    static final int MELEE_X_DISP = 61;
    static final int GUN_X_DISP = 46;
    
    // SpriteBatch
    private static SpriteBatch spriteBatch = new SpriteBatch();
    
    // Font
    static BitmapFont font;
    
    // Render static character
    public static void renderChar(Character c) {
        if (c.facingRight) {
            c.anim.renderStaticRight();
        } else {
            c.anim.renderStaticLeft();
        }
    }
    
    // Renders melee weapon
    public static void renderMelee(Character c) {
        if (c.facingRight) {
            c.melee.anim.renderStaticRight();
        } else {
            c.melee.anim.renderStaticLeft();
        }
    }
    
    // Render gun
    public static void renderGun(Character c) {
        if (c.facingRight) {
            c.gun.anim.renderStaticRight();
        } else {
            c.gun.anim.renderStaticLeft();
        }
    }
    
    // Render bullets
    public static void renderBullets(Character c) {
        // ArrayList of bullets to be removed
        ArrayList<Bullet> removeBullets = new ArrayList<Bullet>();
        
        // Render each bullet
        for (int i = 0; i < c.bullets.size(); i++) {
            c.bullets.get(i).anim.renderStaticRight();
            c.bullets.get(i).x += c.bullets.get(i).xDisp;
            c.bullets.get(i).y += c.bullets.get(i).yDisp;
            if (c.bullets.get(i).x > 1000 || c.bullets.get(i).x < -50) {
                removeBullets.add(c.bullets.get(i));
            }
        }
        
        // Remove the bullets that are off the screen
        for (int i = 0; i < removeBullets.size(); i++) {
            c.bullets.remove(removeBullets.get(i));
        }
    }
    
    // Renders character movement
    public static void renderMovement(Character c) {
        // Animate main character
        // Go right
        if (Gdx.input.isKeyPressed(Input.Keys.D) && c == Game.chars[0]
                || Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT) && c == Game.chars[1]) {
            // If you just turned right
            if (!c.facingRight) {
                c.facingRight = true;
                c.melee.x += MELEE_X_DISP;
                c.gun.x += GUN_X_DISP;
            }
            // Moves the player and the gun
            c.x += c.xDisp;
            c.melee.x += c.xDisp;
            c.gun.x += c.xDisp;
            // Checks the boundaries
            Game.checkBounds(c);
            // If the player is on the ground/platform, then show running animation
            if (c.y == c.initY || c.onPlatform) {
                c.anim.renderAnimRight();
            } else {
                renderChar(c);
            }
        }
        // Go left
        else if (Gdx.input.isKeyPressed(Input.Keys.A) && c == Game.chars[0]
                || Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT) && c == Game.chars[1]) {
            // If you just turned left
            if (c.facingRight) {
                c.facingRight = false;
                c.melee.x -= MELEE_X_DISP;
                c.gun.x -= GUN_X_DISP;
            }
            // Moves the player and the gun
            c.x += -1 * c.xDisp;
            c.melee.x += -1 * c.xDisp;
            c.gun.x += -1 * c.xDisp;
            // Checks the boundaries
            Game.checkBounds(c);
            // If the player is on the ground/platform, then show running animation
            if (c.y == c.initY || c.onPlatform) {
                c.anim.renderAnimLeft();
            } else {
                renderChar(c);
            }
        } else {
            renderChar(c);
        }
    }
    
    // Renders melee weapon animation
    public static void renderMeleeAnimation(Character c) {
        if (Gdx.input.isKeyPressed(Input.Keys.V) && c == Game.chars[0] && c.meleeSwing == false
                || Gdx.input.isKeyPressed(Input.Keys.RIGHT_BRACKET) && c == Game.chars[1]
                && c.meleeSwing == false) {
            c.meleeSwing = true;
            c.meleeHit = false;
            //c.swordPrevTime = TimeUtils.millis();
        }
        if (c.meleeSwing) {
            if (c.facingRight) {
                c.melee.anim.renderAnimRight();
            } else {
                c.melee.anim.renderAnimLeft();
            }
            if (c.melee.anim.currentFrame == c.melee.anim.rightFrames[0]
                    || c.melee.anim.currentFrame == c.melee.anim.leftFrames[0]) {
                c.meleeSwing = false;
            }
        } else {
            // Renders melee weapon
            renderMelee(c);
        }
    }
    
    // Draws the GUI
    public static void drawGUI() {
        spriteBatch.begin();
        // Draw player 1 health
        font.draw(spriteBatch, "Player 1", 15, 630);
        font.draw(spriteBatch, "HP: " + Game.chars[0].health + "/1000", 15, 600);
        // Draw player 2 health
        font.draw(spriteBatch, "Player 2", 750, 630);
        font.draw(spriteBatch, "HP: " + Game.chars[1].health + "/1000", 750, 600);
        spriteBatch.end();
    }
}
