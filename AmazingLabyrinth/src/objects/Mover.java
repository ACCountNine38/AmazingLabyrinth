package objects;

// interface that updates the position and checks if the player can move to a direction
public interface Mover {
	
	// method that updates the position of the player to the position in the parameter
	public void updatePosition(int x, int y);
	
	// method that checks if the player can move to a direction
	public boolean movable(int x, int y, int moveX, int moveY);

}
