package objects;

public interface Mover {
	
	public void updatePosition(int x, int y);
	
	public boolean movable(int x, int y, int moveX, int moveY);

}
