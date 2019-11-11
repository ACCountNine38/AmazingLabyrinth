package objects;

public class Tile {

	private int id, item;
	private String filePath;
	private boolean up, down, left, right, canMove;
	
	public Tile(int id, int item, boolean canMove) {
		
		this.item = item;
		this.canMove = canMove;
		
		setId(id);
		
	}
	
	public void rotateTile(Tile tile, boolean random) {
		
		if(!random) {
			
			if(tile.getId() == 1)
				setId(2);
			
			else if(tile.getId() == 2)
				setId(1);
			
			else if(tile.getId() == 3) 
				setId(4);
			
			else if(tile.getId() == 4) 
				setId(5);
			
			else if(tile.getId() == 5) 
				setId(6);
			
			else if(tile.getId() == 6) 
				setId(3);
			
			
		}
		
		else  {
		
			int numRotation = (int)(Math.random()*4);
			
		}
		
		
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		
		/*
		 * method that fills the board tiles with integer IDs
		 * 
		 * up down - 0
		 * left right - 1
		 * up right - 2
		 * right down - 3
		 * down left - 4
		 * left up - 5
		 * left right down - 6
		 * left up down - 7
		 * left up right - 8
		 * up right down - 9
		 * 
		*/
		
		// normal tiles that does not have an item on top

		if(id == 0) { 
			
			up = true;
			down = true;
			
		} else if(id == 1) {
			
			left = true;
			right = true;
			
		} else if(id == 2) {
			
			up = true;
			right = true;
			
		} else if(id == 3) {
			
			right = true;
			down = true;
			
		} else if(id == 4) {
			
			down = true;
			left = true;
			
		} else if(id == 5) {
			
			left = true;
			up = true;
			
		} else if(id == 6) {
			
			left = true;
			right = true;
			down = true;
			
		} else if(id == 7) {
			
			up = true;
			down = true;
			left = true;
			
		} else if(id == 8) {
			
			up = true;
			left = true;
			right = true;
			
		} else if(id == 9) {
			
			down = true;
			up = true;
			right = true;
			
		} 
		
		filePath = "images/tile" + id + ".png";
		
		if(item != 0) {
			
			if(item == 13) {
				
				filePath = "images/Owl" + (id-2) + ".png";
				
			} else if(item == 14) {
				
				filePath = "images/Rat" + (id-2) + ".png";
				
			} else if(item == 15) {
				
				filePath = "images/Moth" + (id-2) + ".png";
				
			} else if(item == 16) {
				
				filePath = "images/Lizard" + (id-2) + ".png";
				
			} else if(item == 17) {
				
				filePath = "images/Spider" + (id-2) + ".png";
				
			} else if(item == 18) {
				
				filePath = "images/Scarab" + (id-2) + ".png";
				
			} else if(item == 19) {
				
				filePath = "images/GhostBottle" + (id-6) + ".png";
				
			} else if(item == 20) {
				
				filePath = "images/LadyPig" + (id-6) + ".png";
				
			} else if(item == 21) {
				
				filePath = "images/GhostWaving" + (id-6) + ".png";
				
			} else if(item == 22) {
				
				filePath = "images/Sorceress" + (id-6) + ".png";
				
			} else if(item == 23) {
				
				filePath = "images/Bat" + (id-6) + ".png";
				
			} else if(item == 24) {
				
				filePath = "images/Dragon" + (id-6) + ".png";
				
			} 
			
		}
		
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

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public boolean isCanMove() {
		return canMove;
	}

	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}

}
