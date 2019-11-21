package objects;

import java.awt.Color;

import javax.swing.ImageIcon;

public class Player {
	
	private int x;
	private int y;
	
	private int id;
	private ImageIcon image;
	private Color colorID;
	private boolean AI;
	
	//The boolean variable to check if the player is still in the game 
	private boolean isActive;
	
	public Player(int id, boolean AI) {

		this.isActive = true;
		this.id = id;
		this.AI = AI;
		
		if(id == 0) {
			
			x = 0;
			y = 0;
			image = new ImageIcon("images/player1.png");
			colorID = Color.red;
			
		} else if(id == 1) {
			
			x = 6;
			y = 0;
			image = new ImageIcon("images/player2.png");
			colorID = Color.yellow;
			
		} else if(id == 2) {
			
			x = 0;
			y = 6;
			image = new ImageIcon("images/player3.png");
			colorID = Color.green;
			
		} else if(id == 3) {
			
			x = 6;
			y = 6;
			image = new ImageIcon("images/player4.png");
			colorID = Color.blue;
			
		}
		
	}

	//Getters and setters for the isActive variable
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
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

	public Color getColorID() {
		return colorID;
	}

	public void setColorID(Color colorID) {
		this.colorID = colorID;
	}

	public boolean isAI() {
		return AI;
	}

	public void setAI(boolean aI) {
		AI = aI;
	}
}
