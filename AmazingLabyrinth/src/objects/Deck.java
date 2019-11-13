package objects;

import java.util.ArrayList;

import java.util.*;
import javax.swing.ImageIcon;
import java.io.File;

public class Deck {
	
	private static ArrayList<ImageIcon> cards = new ArrayList<ImageIcon>();

	public Deck() {
		
		initializeCards();
		
	}

	public static void initializeCards() {
		
//		for (int i=1; i<=24; i++) {
//			
//			cards.add(new ImageIcon("cards/" + i +"png"));
//			
//		}
		
		cards.add(new ImageIcon("cards/1.png"));
		cards.add(new ImageIcon("cards/2.png"));
		cards.add(new ImageIcon("cards/3.png"));
		cards.add(new ImageIcon("cards/4.png"));
		cards.add(new ImageIcon("cards/5.png"));
		cards.add(new ImageIcon("cards/6.png"));
		cards.add(new ImageIcon("cards/7.png"));
		cards.add(new ImageIcon("cards/8.png"));
		cards.add(new ImageIcon("cards/9.png"));
		cards.add(new ImageIcon("cards/10.png"));
		cards.add(new ImageIcon("cards/11.png"));
		cards.add(new ImageIcon("cards/12.png"));
		cards.add(new ImageIcon("cards/13.png"));
		cards.add(new ImageIcon("cards/14.png"));
		cards.add(new ImageIcon("cards/15.png"));
		cards.add(new ImageIcon("cards/16.png"));
		cards.add(new ImageIcon("cards/17.png"));
		cards.add(new ImageIcon("cards/18.png"));
		cards.add(new ImageIcon("cards/19.png"));
		cards.add(new ImageIcon("cards/20.png"));
		cards.add(new ImageIcon("cards/21.png"));
		cards.add(new ImageIcon("cards/22.png"));
		cards.add(new ImageIcon("cards/23.png"));
		cards.add(new ImageIcon("cards/24.png"));
		

	}
	static ArrayList<ImageIcon> getshuffle() {
		
		Collections.shuffle(cards,new Random());
		return cards;
	}
	
	public static ArrayList<ImageIcon> getCards() {
		
		return cards;
		
	}

	public static void setCards(ArrayList<ImageIcon> cards) {
		Deck.cards = cards;
	}
	

}
