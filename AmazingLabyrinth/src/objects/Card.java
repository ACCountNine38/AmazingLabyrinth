package objects;

import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Card {
	
	private static ArrayList<Card> cards = new ArrayList<Card>();
	
	private ImageIcon img;
	private boolean collected;
	
	public Card(ImageIcon img, boolean collected) {
		
		this.img = img;
		this.collected = collected;
		
	}
	
	private void initializeCards() {
		
		
		
	}

}
