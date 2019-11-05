package objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Item {
	
	private static ArrayList<Item> items = new ArrayList<Item>();
	
	private BufferedImage img;
	
	private Item bat = new Item(new BufferedImage(0, 0, 0));
	
	public Item(BufferedImage img) {
		
		this.img = img;
		initializeCards();
		
	}
	
	public void initializeCards() {
		
		
		
	}

}
