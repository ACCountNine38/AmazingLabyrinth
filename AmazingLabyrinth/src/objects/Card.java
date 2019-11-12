package objects;

import java.util.ArrayList;
import java.util.*;
import javax.swing.ImageIcon;
import java.io.File;
public class Card {
	
	private static ArrayList<Card> cards = new ArrayList<Card>();
	private boolean collected;
	private ImageIcon img;
	
	public Card(ImageIcon img, boolean collected) {
		
		this.img = img;
		this.collected = collected;
		
	}
	
	private void initializeCards() {
				
		cards.add(new Card(new ImageIcon("images/cards/1.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/2.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/3.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/4.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/5.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/6.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/7.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/8.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/9.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/10.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/11.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/12.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/13.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/14.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/15.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/16.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/17.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/18.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/19.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/20.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/21.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/22.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/23.png"), false));
		cards.add(new Card(new ImageIcon("images/cards/24.png"), false));
		
	}
	static ArrayList<Card> getshuffle() {
		
		Collections.shuffle(cards,new Random());
		return cards;
	}
	
}

