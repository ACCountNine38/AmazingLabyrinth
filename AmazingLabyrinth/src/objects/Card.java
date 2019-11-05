package objects;

import java.util.ArrayList;

public class Card {
	
	private static ArrayList<Card> cards = new ArrayList<Card>();
	
	private Item item;
	private boolean collected;
	
	public Card(Item item, boolean collected) {
		
		this.item = item;
		this.collected = collected;
		
	}
	
	private void initializeCards() {
		
		
		
	}

}
