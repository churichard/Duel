package duel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animator {
	
	private Entity entity;
	private String name;
	float animSpeed;
	
	private static int FRAME_COLS; // #1
	private static int FRAME_ROWS; // #2
	
	Animation rightAnimation; // #3
	Animation leftAnimation;
	Texture frameSheet; // #4
	TextureRegion[] rightFrames; // #5
	TextureRegion[] leftFrames;
	SpriteBatch spriteBatch; // #6
	TextureRegion currentFrame; // #7
	
	float stateTime; // #8
	
	public Animator(Entity entity, String name, int rows, int cols, float animSpeed) {
		this.entity = entity;
		this.name = "assets/" + name + ".png";
		this.animSpeed = animSpeed;
		FRAME_ROWS = rows;
		FRAME_COLS = cols;
	}
	
	// Creates the sprites
	public void create() {
		frameSheet = new Texture(Gdx.files.internal(name)); // #9
		TextureRegion[][] tmp = TextureRegion.split(frameSheet, frameSheet.getWidth() / FRAME_COLS, frameSheet.getHeight()
				/ FRAME_ROWS); // #10
		
		// If there are both right and left sprites
		if (FRAME_ROWS != 1 || FRAME_COLS != 1) {
			rightFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS / 2];
			leftFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS / 2];
		}
		// If there is only one sprite
		else {
			rightFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		}
		
		int index = 0;
		
		// If there are both right and left sprites
		if (FRAME_ROWS != 1 || FRAME_COLS != 1) {
			// Create frames for going to the right
			for (int i = 0; i < FRAME_ROWS / 2; i++) {
				for (int j = 0; j < FRAME_COLS; j++) {
					rightFrames[index++] = tmp[i][j];
				}
			}
			
			// Create frames for going to the left
			index = 0;
			for (int i = FRAME_ROWS / 2; i < FRAME_ROWS; i++) {
				for (int j = 0; j < FRAME_COLS; j++) {
					leftFrames[index++] = tmp[i][j];
				}
			}
			// Animation for going to the left
			leftAnimation = new Animation(animSpeed, leftFrames);
		} else {
			for (int i = 0; i < 1; i++) {
				rightFrames[0] = tmp[0][0];
			}
		}
		
		// Animation for going to the right
		rightAnimation = new Animation(animSpeed, rightFrames); // #11
		
		spriteBatch = new SpriteBatch(); // #12
		stateTime = 0f; // #13
	}
	
	// Renders the sprites animating to the right
	public void renderAnimRight() {
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT); // #14
		stateTime += Gdx.graphics.getDeltaTime(); // #15
		currentFrame = rightAnimation.getKeyFrame(stateTime, true); // #16
		
		spriteBatch.begin();
		spriteBatch.draw(currentFrame, entity.x, entity.y); // #17
		spriteBatch.end();
	}
	
	// Renders the sprites animating to the left
	public void renderAnimLeft() {
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT); // #14
		stateTime += Gdx.graphics.getDeltaTime(); // #15
		currentFrame = leftAnimation.getKeyFrame(stateTime, true); // #16
		
		spriteBatch.begin();
		spriteBatch.draw(currentFrame, entity.x, entity.y); // #17
		spriteBatch.end();
	}
	
	// Renders the static sprite facing right
	public void renderStaticRight(){
		spriteBatch.begin();
		spriteBatch.draw(rightFrames[0], entity.x, entity.y);
		spriteBatch.end();
	}
	
	// Renders the static sprite facing left
	public void renderStaticLeft(){
		spriteBatch.begin();
		spriteBatch.draw(leftFrames[0], entity.x, entity.y);
		spriteBatch.end();
	}
}