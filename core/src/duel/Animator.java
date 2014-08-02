package duel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animator {
	
	private String name;
	int x;
	int y;
	int xDisp;
	int yDisp;
	float animSpeed;
	
	private static int FRAME_COLS; // #1
	private static int FRAME_ROWS; // #2
	
	Animation walkRightAnimation; // #3
	Animation walkLeftAnimation;
	Texture walkSheet; // #4
	TextureRegion[] walkRightFrames; // #5
	TextureRegion[] walkLeftFrames;
	SpriteBatch spriteBatch; // #6
	TextureRegion currentFrame; // #7
	
	float stateTime; // #8
	
	public Animator(String name, int x, int y, int xDisp, int yDisp, int rows, int cols, float animSpeed) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.xDisp = xDisp;
		this.yDisp = yDisp;
		this.animSpeed = animSpeed;
		FRAME_ROWS = rows;
		FRAME_COLS = cols;
	}
	
	// Creates the sprites
	public void create() {
		walkSheet = new Texture(Gdx.files.internal(name)); // #9
		TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight()
				/ FRAME_ROWS); // #10
		
		// If there are both right and left sprites
		if (FRAME_ROWS != 1 || FRAME_COLS != 1) {
			walkRightFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS / 2];
			walkLeftFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS / 2];
		}
		// If there is only one sprite
		else {
			walkRightFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		}
		
		int index = 0;
		
		// If there are both right and left sprites
		if (FRAME_ROWS != 1 || FRAME_COLS != 1) {
			// Create frames for going to the right
			for (int i = 0; i < FRAME_ROWS / 2; i++) {
				for (int j = 0; j < FRAME_COLS; j++) {
					walkRightFrames[index++] = tmp[i][j];
				}
			}
			
			// Create frames for going to the left
			index = 0;
			for (int i = FRAME_ROWS / 2; i < FRAME_ROWS; i++) {
				for (int j = 0; j < FRAME_COLS; j++) {
					walkLeftFrames[index++] = tmp[i][j];
				}
			}
			// Animation for going to the left
			walkLeftAnimation = new Animation(animSpeed, walkLeftFrames);
		}
		else{
			for (int i = 0; i < 1; i++){
				walkRightFrames[0] = tmp[0][0];
			}
		}
		
		// Animation for going to the right
		walkRightAnimation = new Animation(animSpeed, walkRightFrames); // #11
		
		spriteBatch = new SpriteBatch(); // #12
		stateTime = 0f; // #13
	}
	
	// Renders the sprites walking right
	public void renderRight() {
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT); // #14
		stateTime += Gdx.graphics.getDeltaTime(); // #15
		currentFrame = walkRightAnimation.getKeyFrame(stateTime, true); // #16
		
		spriteBatch.begin();
		spriteBatch.draw(currentFrame, x, y); // #17
		spriteBatch.end();
	}
	
	// Renders the sprites walking left
	public void renderLeft() {
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT); // #14
		stateTime += Gdx.graphics.getDeltaTime(); // #15
		currentFrame = walkLeftAnimation.getKeyFrame(stateTime, true); // #16
		
		spriteBatch.begin();
		spriteBatch.draw(currentFrame, x, y); // #17
		spriteBatch.end();
	}
}