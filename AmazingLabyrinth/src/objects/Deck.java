package objects;

import java.util.ArrayList;

import java.util.*;
import javax.swing.ImageIcon;
import java.io.File;

//The class where all the cards and their ids are created
public class Deck {

	//two arrayList for the cards and id each of them have
	private static ArrayList<ImageIcon> cards = new ArrayList<ImageIcon>();
	private static ArrayList<Integer> IDNumber = new ArrayList<Integer>();

	//The main method that create the id elements in the list 
	public static void initializID() {

		IDNumber.clear();
		//for loop to add each id to the ID arrayList
		for (int i=0; i< 24; i++) {
			IDNumber.add(i);
		}

	}

	//The method that assign the card to the ID base on their position
	public static void initializeCards() {

		//run the initializID to create the list
		initializID();	
		//shuffle the list so the player gets a random hand every time. 
		Collections.shuffle(IDNumber);

		//using a for loop to assign the card base on the ID. 
		for (int i=0; i<24; i++) {

			if (IDNumber.get(i) == 0) {
				cards.add(i, new ImageIcon("cards/4.png"));
			}
			else if(IDNumber.get(i) == 1) {
				cards.add(i, new ImageIcon("cards/13.png"));
			}
			else if(IDNumber.get(i) == 2) {
				cards.add(i, new ImageIcon("cards/18.png"));
			}
			else if(IDNumber.get(i) == 3) {
				cards.add(i, new ImageIcon("cards/1.png"));
			}
			else if(IDNumber.get(i) == 4) {
				cards.add(i, new ImageIcon("cards/10.png"));
			}
			else if(IDNumber.get(i) == 5) {
				cards.add(i, new ImageIcon("cards/24.png"));
			}
			else if(IDNumber.get(i) == 6) {
				cards.add(i, new ImageIcon("cards/3.png"));
			}
			else if(IDNumber.get(i) == 7) {
				cards.add(i, new ImageIcon("cards/22.png"));
			}
			else if(IDNumber.get(i) == 8) {
				cards.add(i, new ImageIcon("cards/9.png"));
			}
			else if(IDNumber.get(i) == 9) {
				cards.add(i, new ImageIcon("cards/12.png"));
			}
			else if(IDNumber.get(i) == 10) {
				cards.add(i, new ImageIcon("cards/21.png"));
			}
			else if(IDNumber.get(i) == 11) {
				cards.add(i, new ImageIcon("cards/11.png"));
			}
			else if(IDNumber.get(i) == 12) {
				cards.add(i, new ImageIcon("cards/5.png"));
			}
			else if(IDNumber.get(i) == 13) {
				cards.add(i, new ImageIcon("cards/14.png"));
			}
			else if(IDNumber.get(i) == 14) {
				cards.add(i, new ImageIcon("cards/23.png"));
			}
			else if(IDNumber.get(i) == 15) {
				cards.add(i, new ImageIcon("cards/6.png"));
			}
			else if(IDNumber.get(i) == 16) {
				cards.add(i, new ImageIcon("cards/16.png"));
			}
			else if(IDNumber.get(i) == 17) {
				cards.add(i, new ImageIcon("cards/8.png"));
			}
			else if(IDNumber.get(i) == 18) {
				cards.add(i, new ImageIcon("cards/19.png"));
			}
			else if(IDNumber.get(i) == 19) {
				cards.add(i, new ImageIcon("cards/7.png"));
			}
			else if(IDNumber.get(i) == 20) {
				cards.add(i, new ImageIcon("cards/20.png"));
			}
			else if(IDNumber.get(i) == 21) {
				cards.add(i, new ImageIcon("cards/17.png"));
			}
			else if(IDNumber.get(i) == 22) {
				cards.add(i, new ImageIcon("cards/15.png"));
			}
			else if(IDNumber.get(i) == 23) {
				cards.add(i, new ImageIcon("cards/2.png"));
			}

		}

	}

	//getters and setters for the variables
	public static ArrayList<ImageIcon> getCards() {

		return cards;

	}

	public static void setCards(ArrayList<ImageIcon> cards) {
		Deck.cards = cards;
	}

	public static ArrayList<Integer> getIDNumber() {
		return IDNumber;
	}


	public static void setIDNumber(ArrayList<Integer> iDNumber) {
		IDNumber = iDNumber;
	}

}