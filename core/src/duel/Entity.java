package duel;

public class Entity {
	
	// Entity animation
	Animator anim;
	
	// Entity coordinates
	int initX;
	int initY;
	int x;
	int y;
	int xDisp;
	int yDisp;
	
	public Entity(String ref, int initX, int initY, int xDisp, int yDisp, int rows, int cols, float animSpeed){
		// Initialize entity coordinates and displacements
		this.initX = initX;
		this.initY = initY;
		this.x = initX;
		this.y = initY;
		this.xDisp = xDisp;
		this.yDisp = yDisp;
		
		createAnim(ref, rows, cols, animSpeed);
	}
	
	// Creates a new Animator object for the entity
	public void createAnim(String ref, int rows, int cols, float animSpeed){
		anim = new Animator(this, ref, rows, cols, animSpeed);
		anim.create();
	}
}
