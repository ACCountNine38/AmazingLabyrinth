package objects;

import javax.swing.ImageIcon;

public class Player {
	
	private int x;
	private int y;
	
	private int id;
	private ImageIcon image;
	
	public Player(int id) {
		
		this.id = id;
		
		if(id == 0) {
			
			x = 0;
			y = 0;
			image = new ImageIcon("images/player1.png");
			
		} else if(id == 1) {
			
			x = 6;
			y = 0;
			image = new ImageIcon("images/player2.png");
			
		} else if(id == 2) {
			
			x = 0;
			y = 6;
			image = new ImageIcon("images/player3.png");
			
		} else if(id == 3) {
			
			x = 6;
			y = 6;
			image = new ImageIcon("images/player4.png");
			
		}
		
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ImageIcon getImage() {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}

}
