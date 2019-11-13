package objects;

import java.util.ArrayList;

import java.util.*;
import javax.swing.ImageIcon;
import java.io.File;

public class Deck {
	
	private static ArrayList<ImageIcon> cards = new ArrayList<ImageIcon>();

	public Deck() {
		
		initializeCards();
		System.out.println("Hello world");
		
	}

	public static void initializeCards() {
		
//		for (int i=1; i<=24; i++) {
//			cards.add(new ImageIcon("images/cards/" + i +"png"));
//		}
		
		cards.add(new ImageIcon("images/Bat1.png"));
		cards.add(new ImageIcon("images/Bat1.png"));
		cards.add(new ImageIcon("images/Bat1.png"));
		cards.add(new ImageIcon("images/Bat1.png"));
		cards.add(new ImageIcon("images/Bat1.png"));
		cards.add(new ImageIcon("images/Bat2.png"));
		cards.add(new ImageIcon("images/Bat2.png"));
		cards.add(new ImageIcon("images/Bat2.png"));
		cards.add(new ImageIcon("images/Bat2.png"));
		cards.add(new ImageIcon("images/Bat2.png"));
		

		
	}
	static ArrayList<ImageIcon> getshuffle() {
		
		Collections.shuffle(cards,new Random());
		return cards;
	}
	
	public static ArrayList<ImageIcon> getCards() {
		
		System.out.println(cards.size());
		return cards;
	}

	public static void setCards(ArrayList<ImageIcon> cards) {
		Deck.cards = cards;
	}
	

}
