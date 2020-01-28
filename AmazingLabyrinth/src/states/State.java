package states;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import sounds.AudioPlayer;
import sounds.MusicPlayer;
import utility.CustomizationTool;

/*
 * The parent class of all states, top of the class hierarchy
 * contains default information to be used for other states and abstract methods
 */
public abstract class State extends JFrame implements ActionListener {

	// final variables
	public static final double unscaledScreenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static final double unscaledScreenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private static final double widthScale = unscaledScreenWidth / 1280;
	private static final double heightScale = unscaledScreenHeight / 800;
	
	// scale variable to determine the size of components based on the screen size
	public static double scale;

	// the scaled dimensions of the game screen
	public static int ScreenWidth;
	public static int ScreenHeight;

	// the origin of components placed on screen based on the screen size
	public static int scaledOrginX;
	public static int scaledOrginY;
	
	// modified tile icon size based on the scale
	public static int tileIconSize;
			
	// a constructor that will be shared to all states
	public State() {

		// to avoid any stretching, the scale that is the minimum will be the final scale
		if (widthScale < heightScale) {

			scale = 1;

		} else {

			scale = 1;

		}
		scale = 1;
		// calculating the modified screen dimensions
		ScreenWidth = (int) (1280 * scale);
		ScreenHeight = (int) (800 * scale);
		
		// calculating the tile icon size
		tileIconSize = (int)(ScreenHeight/9.5);
		
		//scaledOrginX = (int)(State.unscaledScreenWidth - State.ScreenWidth)/2;
		//scaledOrginY = (int)(State.unscaledScreenHeight - State.ScreenHeight)/2;
		scaledOrginX = 0;
		scaledOrginY = 0;

		// method call of all the abstract and initialization methods
		init();
		addJComponents();
		CustomizationTool.frameSetup(this);
		CustomizationTool.customCursor(this);

	}

	// method that initializes all the instances of a class, this replaces the constructor
	public abstract void init();
	
	// abstract method that adds all the JComponents to a state
	public abstract void addJComponents();

}