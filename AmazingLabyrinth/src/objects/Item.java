package objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Item {
	
	private static ArrayList<Item> items = new ArrayList<Item>();
	
	private ImageIcon img;
	
	public Item(ImageIcon img) {
		
		this.img = img;
		initializeCards();
		
	}
	
	public void initializeCards() {
		
		//items.add();
		
	}

}
