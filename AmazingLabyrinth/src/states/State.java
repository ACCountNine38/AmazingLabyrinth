package states;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import sounds.AudioPlayer;
import sounds.MusicPlayer;
import utility.CustomizationTool;

/*
 * Author: Alan Sun
 * 
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
		
		// calculating the modified screen dimensions
		ScreenWidth = (int) (1280 * scale);
		ScreenHeight = (int) (800 * scale);
		
		// the origin will start at half of the scaled screen difference
		scaledOrginX = 0; //(int)(State.unscaledScreenWidth - State.ScreenWidth)/2;
		scaledOrginY = 0;//(int)(State.unscaledScreenHeight - State.ScreenHeight)/2;
		
		// calculating the tile icon size
		tileIconSize = (int)(ScreenHeight/9 * State.scale);

		// method call of all the abstract and initialization methods
		init();
		addJComponents();
		CustomizationTool.frameSetup(this);
		CustomizationTool.customCursor(this);

	}

	// method that adds the items relating to the menu bar
	public void addMenuBar() {

		// create a new JMenuBar item that stores different menus
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// create a new menu called control and add it to the menu bar
		JMenu controlMenu = new JMenu("Control");
		menuBar.add(controlMenu);

		// creating the exit option under the control menu
		JMenuItem restartOption = new JMenuItem("Return to Menu");

		// add an action listener for button actions when clicked
		restartOption.addActionListener(new ActionListener() {

			// method handles the current button's actions
			@Override
			public void actionPerformed(ActionEvent e) {

				

			}
		});

		controlMenu.add(restartOption);

		// exit button closes the program
		JMenuItem exitOption = new JMenuItem("Exit Program");

		exitOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// exits the program
				System.exit(1);

			}
		});

		controlMenu.add(exitOption);

		// the help menu will include all the help related menu items
		JMenu helpMenu = new JMenu("Help");

		menuBar.add(helpMenu);

		// the description menu item will specify the screen descriptions and controls
		JMenuItem descriptionOption = new JMenuItem("Rules");
		descriptionOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				
				
			}

		});

		helpMenu.add(descriptionOption);

		// the description menu item will specify the screen descriptions and controls
		JMenuItem controlOption = new JMenuItem("Controls");
		controlOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// shows control description and controls upon clicking
				JOptionPane.showMessageDialog(null,
						"- Use the W-A-S-D keys to move the player\n"
								+ "- use the arrow keys to break the wall, if a hammer is avaliable\n"
								+ "- the score is based on how fast you escape the keep ***WITHOUT DYING***\n"
								+ "- you may purchase items by clicking the 3 item buttons on the side\n\n"
								+ "click 'ok' to continue...",
						"Controls", JOptionPane.INFORMATION_MESSAGE);

			}

		});

		helpMenu.add(controlOption);

		// add audio menu is used to control sound effects
		JMenu audioMenu = new JMenu("Audio");

		menuBar.add(audioMenu);

		// this menu item allows the user to disable music
		JMenuItem disableMusicOption = new JMenuItem("Disable Background Music");
		disableMusicOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// stops the music from playing
				MusicPlayer.stopMusic();
				

			}

		});

		audioMenu.add(disableMusicOption);

		// this menu item allows the user to play a random Music music
		JMenuItem enableMusicOption = new JMenuItem("Enable Background Music");
		enableMusicOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				playGameBackground();

			}

		});

		audioMenu.add(enableMusicOption);

		// this menu item allows the user to play a random Music music
		JMenuItem disableSFXOption = new JMenuItem("Disable Sound Effect");
		disableSFXOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// disabling all sounds by turning sound playing into false
				AudioPlayer.mute = true;

			}

		});

		audioMenu.add(disableSFXOption);

		// this menu item allows the user to play a random Music music
		JMenuItem enableSFXOption = new JMenuItem("Enable Sound Effect");
		enableSFXOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// enable sound effects to play for this screen
				AudioPlayer.mute = false;

			}

		});

		audioMenu.add(enableSFXOption);

	}
	
	// method that plays a random background for the game state
	public void playGameBackground() {
		
		// stop any previously existing music
		MusicPlayer.stopMusic();
		
		// play a brand new game music
		int musicID = (int)(Math.random()*2) + 1;
		MusicPlayer.playMusic("audio/gameTheme" + musicID + ".wav");
		
	}

	// method that initializes all the instances of a class, this replaces the constructor
	public abstract void init();
	
	// abstract method that adds all the JComponents to a state
	public abstract void addJComponents();

}