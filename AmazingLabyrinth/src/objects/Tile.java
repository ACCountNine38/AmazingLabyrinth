package objects;

public class Tile {

	private int id;
	private String filePath;
	private boolean up, down, left, right;
	
	public Tile(int id) {
		
		this.id = id;
		
		/*
		 * method that fills the board tiles with integer IDs
		 * 
		 * up down - 1
		 * left right - 2
		 * up right - 3
		 * up left - 4
		 * left down - 5
		 * right down - 6
		 * up down right - 7
		 * up down left - 8
		 * left right up - 9
		 * left right down - 10
		 * up down left right - 11
		 * 
		*/
		
		if(id == 1) {
			
			up = true;
			down = true;
			
		} else if(id == 2) {
			
			left = true;
			right = true;
			
		} else if(id == 3) {
			
			up = true;
			right = true;
			
		} else if(id == 4) {
			
			up = true;
			left = true;
			
		} else if(id == 5) {
			
			left = true;
			down = true;
			
		} else if(id == 6) {
			
			right = true;
			down = true;
			
		} else if(id == 7) {
			
			up = true;
			down = true;
			right = true;
			
		} else if(id == 8) {
			
			up = true;
			down = true;
			left = true;
			
		} else if(id == 9) {
			
			up = true;
			left = true;
			right = true;
			
		} else if(id == 10) {
			
			down = true;
			left = true;
			right = true;
			
		} else if(id == 11) {
			
			up = true;
			down = true;
			left = true;
			right = true;
			
		}
		
		filePath = "images/path" + id + ".png";
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

}
