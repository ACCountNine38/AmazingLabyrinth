package main;

import java.util.ArrayList;

import states.EndState;
import states.GameState;
import states.MenuState;

public class Launcher {
	
	static ArrayList<Integer> Winner = new ArrayList<Integer>();
	
	public static void main(String[] args) {
//		
//		Winner.add(1);
//		Winner.add(2);
//		Winner.add(3);
//		Winner.add(4);
//		
//		//new MenuState();
//		new EndState(Winner);
		
		new MenuState();
	}

}
