package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

import objects.Deck;
import objects.Mover;
import objects.Player;
import objects.Point;
import objects.Tile;
import sounds.AudioPlayer;
import sounds.MusicPlayer;
import utility.PathTrackingButton;

/*
 * The game state holds all functions and properties of the actual game of Amazing Labyrinth
 * Able to listen to actions with keys, mouse, and buttons
 * Can be played with up to 4 players of AIs
 */
public class GameState extends State implements KeyListener, MouseListener, Mover {

	// final variables
	private static final int BOARD_SIZE = 7;

	//
	private Deck cardObeject = new Deck();
	private ArrayList<ImageIcon> cards;
	private ArrayList<Integer> CardNumber;

	private ImageIcon iconLogo;

	private JPanel gamePanel;

	private JLabel[][] boardIcons;
	private JLabel[] playerIcons;
	private ArrayList<JLabel>[] cardImages;
	private JLabel currentTurn;

	private Tile[][] board;
	private Player players[];
	private ArrayList<Integer> mapBits;
	private ArrayList<Tile>[] directItems;
	private JLabel extraPieceLabel;
	private JLabel boardBoarder;
	private JLabel saveInstruction;
	private ArrayList<JButton> tileShiftButtons;
	private ArrayList<PathTrackingButton> potentialPathways;
	private ArrayList<JLabel> highlightedPath;
	private JTextArea saveGameName;
	private JButton saveButton;
	private JButton rotateClockWise, rotateCounterClockWise;

	private JLabel Player1Label, Player2Label, Player3Label, Player4Label;

	private Tile extraPiece;
	private int currentPlayer;
	private int playerMoveAmount;
	private int tileMoveAmount;
	private int shiftID;
	private boolean canShift, canClick;
	private String playerMoveDirection;
	private String filePath;
	
	private ArrayList<Player> shiftedPlayers;
	private Queue<String> AIMoveSet;

	private Timer autoMoveTimer;
	private Timer playerShiftTimer;
	private Timer tileShiftTimer;

	private ArrayList<Integer>[] hands;
	
	private ArrayList<Integer> Winner;
	
	private boolean gameEnded;
	
	public static int[] playerAILevel = new int[4];
	
	// the constructor is used for loading a game
	public GameState(boolean loaded, String filePath) {
		
		// if the game is loaded, then load the game from existing file, else init will be called
		if(loaded) {
			
			this.filePath = filePath;
			loadGame();
			
		}
		
	}

	// method that initializes every instance, override from the State class
	@Override
	public void init() {

		// update background music
		MusicPlayer.stopMusic();
		playGameBackground();

		// Initializing constants
		currentPlayer = 0;
		
		Winner = new ArrayList<Integer>();
		
		// Initializing JComponents
		board = new Tile[BOARD_SIZE][BOARD_SIZE];
		boardIcons = new JLabel[BOARD_SIZE][BOARD_SIZE];
		playerIcons = new JLabel[4];
		extraPieceLabel = new JLabel(new ImageIcon(""));
		potentialPathways = new ArrayList<PathTrackingButton>();
		highlightedPath = new ArrayList<JLabel>();
		iconLogo = new ImageIcon("cards/CardBack.jpg");

		tileShiftButtons = new ArrayList<JButton>();

		//run the initializeCards method in the Deck class
		Deck.initializeCards();
		
		//add the Card values in the Deck to the array list in this class
		cards = Deck.getCards();
		//add the ID values in the Deck to the array list in this class
		CardNumber = Deck.getIDNumber();

		// Initializing others types
		players = new Player[4];
		mapBits = new ArrayList<Integer>();
		shiftedPlayers = new ArrayList<Player>();
		AIMoveSet = new LinkedList<String>();
		autoMoveTimer = new Timer(300, this);
		playerShiftTimer = new Timer(1, this);
		tileShiftTimer = new Timer(1, this);
		canShift = true;
		canClick = true;
		
		directItems = new ArrayList[4];
		
		for(int i = 0; i < 4; i++) {
			
			directItems[i] = new ArrayList<Tile>();
			
		}
		
		// initializing player's deck
		hands = new ArrayList[4];
		cardImages = new ArrayList[4];

		// enable key listener for this state
		addKeyListener(this);

		// initializes all the players
		for(int i = 0; i < 4; i++) {
			
			if(playerAILevel[i] == 0)
				players[i] = new Player(i, false, 0);
			
			else if(playerAILevel[i] == 1)
				players[i] = new Player(i, true, 1);
			
			else if(playerAILevel[i] == 2)
				players[i] = new Player(i, true, 2);
			
			else if(playerAILevel[i] == 3)
				players[i] = new Player(i, true, 3);
			
		}

		// Method Calls
		fillMapBits();
		addMenuBar();

	}

	// method that creates all the JComponents for this state, Override from the State class 
	@Override
	public void addJComponents() {

		// create a new panel to put the JComponents on top
		gamePanel = new JPanel(null);

		// panel settings, disable auto layout, set bounds and background
		gamePanel.setLayout(null);
		gamePanel.setBounds(scaledOrginX, scaledOrginY, ScreenWidth, ScreenHeight);
		gamePanel.setBackground(Color.black);

		// add panel to the frame
		add(gamePanel);
				
		int currentCard = 0;
		
		for(int i = 0; i < cardImages.length; i++) {
			
			hands[i] = new ArrayList<Integer>();
			cardImages[i] = new ArrayList<JLabel>();
			
			for(int j = 0; j < 5; j++) {
				
				hands[i].add(CardNumber.get(currentCard));
				cardImages[i].add(new JLabel(new ImageIcon(cards.get(currentCard).getImage().getScaledInstance(tileIconSize, tileIconSize, 0))));
				cardImages[i].get(j).setBounds(880 + j*70, 325 + 100*i, 60, 90);
				gamePanel.add(cardImages[i].get(j));
				currentCard++;
				
			}
		}
		
		// generate all game tiles
		for(int i = 0; i < BOARD_SIZE; i++) {
			for(int j = 0; j < BOARD_SIZE; j++) {

				// location for the tile in the image directory
				String path = board[i][j].getFilePath();

				// re-scale an image icon to fit the screen and position it on the screen;
				boardIcons[i][j] = new JLabel(new ImageIcon(new ImageIcon(path)
						.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));

				boardIcons[i][j].setBounds(scaledOrginX + tileIconSize + tileIconSize*i, 
						scaledOrginY + tileIconSize + tileIconSize*j, tileIconSize, tileIconSize);

				gamePanel.add(boardIcons[i][j]);

			}
		}

		// adding all 12 shift tile buttons, assigning each tile at a location
		/*
		 * index 0 - 2: top buttons
		 * index 3 - 5: right buttons
		 * index 6 - 8: bottom buttons
		 * index 8 - 11: left buttons
		 */
		for(int i = 0; i <= 2; i++) {

			// adding the current shift button to the array, assigning its index as id for later use
			tileShiftButtons.add(new JButton());

			// positioning the buttons
			tileShiftButtons.get(i).setBounds(boardIcons[1][0].getX() + tileIconSize*i*2, 
					boardIcons[1][0].getY() - tileIconSize, tileIconSize, tileIconSize);

			// enable action listener and disable auto focus for the current button
			tileShiftButtons.get(i).addActionListener(this);
			tileShiftButtons.get(i).setFocusable(false);
			gamePanel.add(tileShiftButtons.get(i));

		}

		for(int i = 3; i <= 5; i++) {

			tileShiftButtons.add(new JButton());

			tileShiftButtons.get(i).setBounds(boardIcons[BOARD_SIZE-1][0].getX() + tileIconSize, 
					boardIcons[BOARD_SIZE-1][1].getY() + (i-3)*tileIconSize*2, tileIconSize, tileIconSize);

			tileShiftButtons.get(i).addActionListener(this);
			tileShiftButtons.get(i).setFocusable(false);
			gamePanel.add(tileShiftButtons.get(i));


		}

		for(int i = 6; i <= 8; i++) {

			tileShiftButtons.add(new JButton());

			tileShiftButtons.get(i).setBounds(boardIcons[1][BOARD_SIZE-1].getX() + tileIconSize*(i-6)*2, 
					boardIcons[0][BOARD_SIZE-1].getY() + tileIconSize, tileIconSize, tileIconSize);

			tileShiftButtons.get(i).addActionListener(this);
			tileShiftButtons.get(i).setFocusable(false);
			gamePanel.add(tileShiftButtons.get(i));


		}

		for(int i = 9; i <= 11; i++) {

			tileShiftButtons.add(new JButton());

			tileShiftButtons.get(i).setBounds(boardIcons[0][1].getX() - tileIconSize, 
					boardIcons[0][1].getY() + tileIconSize*(i-9)*2, tileIconSize, tileIconSize);

			tileShiftButtons.get(i).addActionListener(this);
			tileShiftButtons.get(i).setFocusable(false);
			gamePanel.add(tileShiftButtons.get(i));


		}

		// updates the button icons for the tile shift buttons
		updateTileShiftButtonIcon();

		// displaying the player icons on the screen
		for(int i = 0; i < playerIcons.length; i++) {

			playerIcons[i] = new JLabel(new ImageIcon(players[i].getImage()
					.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));

			playerIcons[i].setBounds(tileIconSize + playerIcons[i].getIcon().getIconWidth()*players[i].getX(), 
					tileIconSize + playerIcons[i].getIcon().getIconHeight()*players[i].getY(), 
					playerIcons[i].getIcon().getIconWidth(),
					playerIcons[i].getIcon().getIconHeight());

			// add the player at index 0 of the JComponent array to be rendered on top of the tiles
			gamePanel.add(playerIcons[i], 0);

		}
		
		// label created to display the current player's turn
		currentTurn = new JLabel("Current Turn: Player " + (currentPlayer + 1));
		currentTurn.setBounds(830, 100, 500, 100);
		currentTurn.setForeground(Color.red);
		currentTurn.setFont(new Font("TimesRoman", Font.BOLD, 36));
		gamePanel.add(currentTurn);
		
		// the two rotate buttons allows the player to rotate the extra tile clockwise or counterclockwise
		rotateClockWise = new JButton(new ImageIcon(new ImageIcon("images/rotateC.png")
				.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));
		
		rotateClockWise.setBounds(880, 200, tileIconSize, tileIconSize);
		rotateClockWise.setFocusable(false);
		rotateClockWise.addActionListener(this);
		gamePanel.add(rotateClockWise);
		
		rotateCounterClockWise = new JButton(new ImageIcon(new ImageIcon("images/rotateCC.png")
				.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));
		
		rotateCounterClockWise.setBounds(900 + tileIconSize*2, 200, tileIconSize, tileIconSize);
		rotateCounterClockWise.addActionListener(this);
		rotateCounterClockWise.setFocusable(false);
		gamePanel.add(rotateCounterClockWise);
		
		// creating the label to display the extra piece
		extraPieceLabel = new JLabel(new ImageIcon(new ImageIcon(extraPiece.getFilePath())
				.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));

		extraPieceLabel.setBounds(890 + tileIconSize, 200, tileIconSize, tileIconSize);

		gamePanel.add(extraPieceLabel);

		// add the board boarder to the panel
		boardBoarder = new JLabel(new ImageIcon(new ImageIcon("images/boardBoarder.png")
				.getImage().getScaledInstance(tileIconSize*9, tileIconSize*9, 0)));
		boardBoarder.setBounds(scaledOrginX, scaledOrginY, 9*tileIconSize, 9*tileIconSize);
		gamePanel.add(boardBoarder);
		
		// creating a save instruction label to help the player to save the game
		saveInstruction = new JLabel("Enter game name to save");
		saveInstruction.setFont(new Font("times new roman", Font.ITALIC, 19));
		saveInstruction.setBounds(scaledOrginX + 860, scaledOrginY + 85, 200, 35);
		saveInstruction.setForeground(Color.white);
		gamePanel.add(saveInstruction);
		
		// creating a save game text area for the player to enter a valid game name
		saveGameName = new JTextArea();
		saveGameName.setFont(new Font("times new roman", Font.BOLD | Font.ITALIC, 32));
		saveGameName.setBounds(scaledOrginX + 860, scaledOrginY + 50, 200, 35);
		saveGameName.addMouseListener(this);
		saveGameName.setFocusable(false);
		gamePanel.add(saveGameName);
		
		// creating a button to allow the player to save game
		saveButton = new JButton("Save Game");
		saveButton.setBounds(scaledOrginX + 1075, scaledOrginY + 50, 100, 35);
		saveButton.addActionListener(this);
		saveButton.setFocusable(false);
		gamePanel.add(saveButton);
		
		// displaying a series of player icons for their deck
		Player1Label = new JLabel(new ImageIcon(new ImageIcon("images/player1.png")
				.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));
		Player2Label = new JLabel(new ImageIcon(new ImageIcon("images/player2.png")
				.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));
		Player3Label = new JLabel(new ImageIcon(new ImageIcon("images/player3.png")
				.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));
		Player4Label = new JLabel(new ImageIcon(new ImageIcon("images/player4.png")
				.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));

		Player1Label.setBounds(800, 345, tileIconSize, tileIconSize);
		Player2Label.setBounds(800, 445, tileIconSize, tileIconSize);
		Player3Label.setBounds(800, 545, tileIconSize, tileIconSize);
		Player4Label.setBounds(800, 645, tileIconSize, tileIconSize);

		gamePanel.add(Player1Label);
		gamePanel.add(Player2Label);
		gamePanel.add(Player3Label);
		gamePanel.add(Player4Label);

		// generate the walkable paths
		viewPath(players[currentPlayer].getX(), players[currentPlayer].getY(), 0, new LinkedList<String>(), new ArrayList<Point>());
		blockPlayerTest();
		highlightTiles();
		
		if(playerAILevel[0] > 0) {
			
			AIFindCard();
			
		}
		
	}

	// method that adds the items relating to the menu bar
	public void addMenuBar() {

		// create a new JMenuBar item that stores different menus
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// create a new menu called control and add it to the menu bar
		JMenu controlMenu = new JMenu("File");
		menuBar.add(controlMenu);

		// creating the exit option under the control menu
		JMenuItem menuOption = new JMenuItem("Return to Menu");
		
		JFrame currentFrame = this;
		// add an action listener for button actions when clicked
		menuOption.addActionListener(new ActionListener() {

			// method handles the current button's actions
			@Override
			public void actionPerformed(ActionEvent e) {

				autoMoveTimer.stop();
				playerShiftTimer.stop();
				tileShiftTimer.stop();
				MusicPlayer.stopMusic();
				new MenuState();
				currentFrame.dispose();

			}
			
		});

		controlMenu.add(menuOption);
		
		// the help menu will include all the help related menu items
		JMenu helpMenu = new JMenu("Help");

		menuBar.add(helpMenu);

		// the description menu item will specify the screen descriptions and controls
		JMenuItem descriptionOption = new JMenuItem("Rules");
		descriptionOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				new RuleState();
				
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
	
	// method that loads the game from the saved files
	private void loadGame() {
		
		// a scanner is declared to read inputs from a path
		Scanner input = null;
		
		// try an catch is used to detect if the file path is valid
		try {
			
			input = new Scanner(new File(filePath));
			
			// loop through the board to update every icon from the saved file
			for(int x = 0; x < BOARD_SIZE; x++) {
				
				for(int y = 0; y < BOARD_SIZE; y++) {
					
					// the values for the tile id and item will be side by side from the input file
					board[x][y] = new Tile(input.nextInt(), input.nextInt());
					
					// re-scale an image icon to fit the screen and position it on the screen;
					boardIcons[x][y].setIcon(new ImageIcon(new ImageIcon(board[x][y].getFilePath())
							.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));
					
				}
				
			}
			
			// the extra piece information is contained after the board info
			extraPiece = new Tile(input.nextInt(), input.nextInt());
			
			// creating the label to display the extra piece
			extraPieceLabel = new JLabel(new ImageIcon(new ImageIcon(extraPiece.getFilePath())
					.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));

			extraPieceLabel.setBounds(890 + tileIconSize, 200, tileIconSize, tileIconSize);
			
			// load in all the player informations
			for(int player = 0; player < 4; player++) {
				
				int xLocation = input.nextInt();
				int yLocation = input.nextInt();
				int isAI = input.nextInt();
				String isActive = input.next();
				
				// if the player is an AI, then change the AI variable to true
				if(isAI == 1) {
					
					players[player] = new Player(player, true, 0);
					
				} else {
					
					players[player] = new Player(player, false, 0);
					
				}
				
				// set the locations of the player to the location stored in file
				players[player].setX(xLocation);
				players[player].setY(yLocation);
				
				// updates if the player is active (if the player completed the game or not)
				if(isActive.equals("false")) {
					
					players[player].setActive(false);
					
				} else {
					
					players[player].setActive(true);
					
				}
				
				playerIcons[player].setBounds(tileIconSize + playerIcons[player].getIcon().getIconWidth()*players[player].getX(), 
						tileIconSize + playerIcons[player].getIcon().getIconHeight()*players[player].getY(), 
						playerIcons[player].getIcon().getIconWidth(),
						playerIcons[player].getIcon().getIconHeight());
				
			}
			
			// read the current player input from file
			currentPlayer = input.nextInt();
			
			// label created to display the current player's turn
			currentTurn.setText("Current Turn: Player " + (currentPlayer + 1));
			currentTurn.setForeground(players[currentPlayer].getColorID());
			
			// update if boolean for if the tiles can still be shifted
			if(input.next().equals("false")) {
				
				canShift = false;
				
			} else {
				
				canShift = true;
				
			}
			
			unhighlightTiles();
			highlightTiles();
			
			// updates the shift button icons based on if tiles can still be shifted
			updateTileShiftButtonIcon();
			
			// clearing previous potential pathways and generates a new set
			clearWalkLines();
			viewPath(players[currentPlayer].getX(), players[currentPlayer].getY(), 0, new LinkedList<String>(), new ArrayList<Point>());
			
			// repaint the board to update the JComponents
			repaint();
			
		} 
		
		// if file is not found, then print the error message
		catch (FileNotFoundException error) {
			
			error.printStackTrace();
			
		}
		
	}
	
	// method that updates the icons for the tile shift buttons
	private void updateTileShiftButtonIcon() {

		// if tiles cannot be shifted, then change the icons into the invalid icons
		if(!canShift) {

			for(int i = 0; i < tileShiftButtons.size(); i++) {

				tileShiftButtons.get(i).setIcon(new ImageIcon(new ImageIcon("images/invalid.png")
						.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));

			}

		} 
		
		// if tiles can be shifted, then update the icons into arrows pointing at the direction of the sift
		else {

			// every direction has 3 buttons
			/*
			 * 0-2: down arrow
			 * 3-5: left arrow
			 * 6-8: up arrows
			 * 9-11: right arrows
			 */
			for(int i = 0; i <= 2; i++) {

				tileShiftButtons.get(i).setIcon(new ImageIcon(new ImageIcon("images/arrowDown.png")
						.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));

			}

			for(int i = 3; i <= 5; i++) {

				tileShiftButtons.get(i).setIcon(new ImageIcon(new ImageIcon("images/arrowLeft.png")
						.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));


			}

			for(int i = 6; i <= 8; i++) {

				tileShiftButtons.get(i).setIcon(new ImageIcon(new ImageIcon("images/arrowUp.png")
						.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));

			}

			for(int i = 9; i <= 11; i++) {

				tileShiftButtons.get(i).setIcon(new ImageIcon(new ImageIcon("images/arrowRight.png")
						.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));

			}

		}

	}

	// method that sets the information for the tile layout of the board before game starts
	private void fillMapBits() {

		// generating fixed tiles
		board[0][0] = new Tile(3, 25);
		board[0][6] = new Tile(2, 27);
		board[6][6] = new Tile(5, 28);
		board[6][0] = new Tile(4, 26);

		board[0][2] = new Tile(9, 3);
		board[0][4] = new Tile(9, 7);

		board[2][0] = new Tile(6, 1);
		board[2][2] = new Tile(9, 4);
		board[2][4] = new Tile(8, 8);
		board[2][6] = new Tile(8, 11);

		board[4][0] = new Tile(6, 2);
		board[4][2] = new Tile(6, 5);
		board[4][4] = new Tile(7, 9);
		board[4][6] = new Tile(8, 12);

		board[6][2] = new Tile(7, 6);
		board[6][4] = new Tile(7, 10);

		// creating a temporary array to hold all the tiles in the game
		ArrayList<Tile> avaliableTiles = new ArrayList<Tile>();

		// adding 12 plain straight up down tiles
		for(int count = 0; count < 12; count++) {

			avaliableTiles.add(new Tile((int)(Math.random()*2), 0));

		}

		// adding 10 plain right angle tiles
		for(int count = 0; count < 10; count++) {

			avaliableTiles.add(new Tile((int)(Math.random()*4) + 2 , 0));

		}

		// adding all the right angle tiles with an item on top, assigning the index as its id
		for(int index = 13; index <= 18; index++) {

			avaliableTiles.add(new Tile((int)(Math.random()*4) + 2 , index));

		}

		// adding all the 3 sided tiles with an item on top, assigning the index as its id
		for(int index = 19; index <= 24; index++) {

			avaliableTiles.add(new Tile((int)(Math.random()*4) + 6 , index));

		}

		// shuffle the list to be randomly displayed on screen
		Collections.shuffle(avaliableTiles);

		// index variable to keep track of the current tile being put on the board
		int index = 0;

		// uploading random tile setup on the board
		for(int i = 0; i < BOARD_SIZE; i++) {
			for(int j = 0; j < BOARD_SIZE; j++) {

				// only add the tile if it is not previously generated
				if(board[i][j] == null) {

					board[i][j] = avaliableTiles.get(index);
					index++;

				}

			}
		}

		// there will be exactly one extra piece leftover at the end
		extraPiece = avaliableTiles.get(avaliableTiles.size()-1);

	}

	// method that recursively generates dotted blue lines indicating where the player can move to
	private void viewPath(int x, int y, int previousDirection, LinkedList<String> newPath, ArrayList<Point> visited) {

		// ensures that AI doesn't go back and forth infinitely
		if(visited.contains(new Point(x, y))) {

			// if the tile is already visited, exit method to avoid stack overflow
			return;

		}

		// if the tile current player is on can go up and the top tile can go down, then there is a path
		if(y > 0 && board[x][y].isUp() && board[x][y-1].isDown()) {

			// draw the path that leads the player to the walkable direction
			JLabel upPath = new JLabel(new ImageIcon(new ImageIcon("images/pathUp.png")
					.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));

			upPath.setFocusable(false);
			upPath.addMouseListener(this);
			upPath.setBounds(tileIconSize + tileIconSize*x, tileIconSize + tileIconSize*y, tileIconSize, tileIconSize);

			// because the path will be erased after turn ends, it will be added to an array
			potentialPathways.add(new PathTrackingButton(upPath, newPath, board[x][y]));

			// draw the path at index 4 on the panel so that the 4 players are drawn on top of it
			gamePanel.add(upPath, 4);

			// if the previous direction is 2(down), then exit the statement to avoid going back and forth
			if(previousDirection != 2) {

				// creating a temporary linked list to be passed to the next recursive call
				// this is used in a Queue for the AI to seek to a location without confusion 
				LinkedList<String> newWalkablePath = new LinkedList<String>(newPath);
				newWalkablePath.add("up");

				// creating a temporary array list to be passed to the next recursive call for backtracking
				ArrayList<Point> tempPoint = visited;
				tempPoint.add(new Point(x, y));

				// recursively checks the path for the next movable direction
				viewPath(x, y-1, 1, newWalkablePath, tempPoint);

			}

		} 

		// if the tile current player is on can go down and the top tile can go up, then there is a path
		if(y < BOARD_SIZE-1 && board[x][y].isDown() && board[x][y+1].isUp()) {
			
			JLabel downPath = new JLabel(new ImageIcon(new ImageIcon("images/pathDown.png")
					.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));

			downPath.setFocusable(false);
			downPath.addMouseListener(this);
			downPath.setBounds(tileIconSize + tileIconSize*x, tileIconSize + tileIconSize*y, tileIconSize, tileIconSize);

			potentialPathways.add(new PathTrackingButton(downPath, newPath, board[x][y]));
			gamePanel.add(downPath, 4);

			if(previousDirection != 1) {

				LinkedList<String> newWalkablePath = new LinkedList<String>(newPath);
				newWalkablePath.add("down");
				
				ArrayList<Point> tempPoint = visited;
				tempPoint.add(new Point(x, y));

				viewPath(x, y+1, 2, newWalkablePath, tempPoint);

			}

		}

		// if the tile current player is on can go left and the top tile can go right, then there is a path
		if(x > 0 && board[x][y].isLeft() && board[x-1][y].isRight()) {
			
			JLabel leftPath = new JLabel(new ImageIcon(new ImageIcon("images/pathLeft.png")
					.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));

			leftPath.setFocusable(false);
			leftPath.addMouseListener(this);
			leftPath.setBounds(tileIconSize + tileIconSize*x, tileIconSize + tileIconSize*y, tileIconSize, tileIconSize);

			potentialPathways.add(new PathTrackingButton(leftPath, newPath, board[x][y]));
			gamePanel.add(leftPath, 4);

			if(previousDirection != 4) {

				LinkedList<String> newWalkablePath = new LinkedList<String>(newPath);
				newWalkablePath.add("left");

				ArrayList<Point> tempPoint = visited;
				tempPoint.add(new Point(x, y));

				viewPath(x-1, y, 3, newWalkablePath, tempPoint);

			}

		} 

		// if the tile current player is on can go right and the top tile can go left, then there is a path
		if(x < BOARD_SIZE-1 && board[x][y].isRight() && board[x+1][y].isLeft()) {
			
			JLabel rightPath = new JLabel(new ImageIcon(new ImageIcon("images/pathRight.png")
					.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));

			rightPath.setFocusable(false);
			rightPath.addMouseListener(this);
			rightPath.setBounds(tileIconSize + tileIconSize*x, tileIconSize + tileIconSize*y, tileIconSize, tileIconSize);

			potentialPathways.add(new PathTrackingButton(rightPath, newPath, board[x][y]));
			gamePanel.add(rightPath, 4);

			if(previousDirection != 3) {

				LinkedList<String> newWalkablePath = new LinkedList<String>(newPath);
				newWalkablePath.add("right");

				ArrayList<Point> tempPoint = visited;
				tempPoint.add(new Point(x, y));

				viewPath(x+1, y, 4, newWalkablePath, tempPoint);

			}

		}

	}

	// method that clears the walk lines after a turn ends or if paths are shifted
	private void clearWalkLines() {

		// remove every path lines in the panel 
		for(PathTrackingButton path: potentialPathways) {
			
			gamePanel.remove(path.getLabel());
			
		}

		// repaint the panel to erase the lines
		gamePanel.repaint();

		// empty the list after all elements are removed
		potentialPathways.clear();

	}
		
	// method that handles the action of when a turn ends
	// it can be called by the AI and player
	private void endTurn() {
		
		if(gameEnded) {
			
			System.out.println("~~~~~~~~~~~~~~!!!GAME OVER!!!~~~~~~~~~~~~~~");
			
		} else {
		
			System.out.println("~~~~~~~~~~~~~~+Player " + (currentPlayer+1) +"'s +Turn Ended~~~~~~~~~~~~~~");
			AudioPlayer.playAudio("audio/startTurn.wav");
	
			// if the current turn is AI
			if(players[currentPlayer].isAI()) {
	
				// stop the AI movement by shutting down the timer
				autoMoveTimer.stop();
	
			}
	
			// enable button shifting
			canShift = true;
	
			// this line checks if the player is still active. CurrentPlayer will keep adding 
			//until it reaches a player that is active. If you are not active, it will skip your turn
			//Also when it reaches beyond 3, it returns back to zero
			do {
				
				currentPlayer++;
				if (currentPlayer > 3) {
					currentPlayer = 0;
				}
				
			} while(!players[currentPlayer].isActive());
	
			// set the text and color of the player turn label to suit the current player
			currentTurn.setText("Current Turn: Player " + (currentPlayer + 1));
			currentTurn.setForeground(players[currentPlayer].getColorID());
	
			// clear the walk lines because a turn has ended
			clearWalkLines();
	
			// if the current player is AI, then start the timer for it to make actions
			if(players[currentPlayer].isAI()) {
	
				AIFindCard();
	
			} else {
	
				// generate new walk lines for the next player
				viewPath(players[currentPlayer].getX(), players[currentPlayer].getY(), 0, new LinkedList<String>(), new ArrayList<Point>());
	
			}
			
			unhighlightTiles();
			highlightTiles();
	
			// updates the icons of the tile shift buttons
			updateTileShiftButtonIcon();
			
		}

	}
	
	// method that updates the position of the players
	@Override
	public void updatePosition(int x, int y) {
		
		// if the frame is focused on the save game text area, switch the focus by disabling the text area
		saveGameName.setFocusable(false);
		
		// the player can only move if the can click variable is true
		if(!canClick) {

			return;

		}

		// stores the position of the tile the current player is moving towards
		int moveX = players[currentPlayer].getX() + x;
		int moveY = players[currentPlayer].getY() + y;

		// if the moving is within the board in the x direction, then update player's position
		if(x != 0 && moveX < BOARD_SIZE && moveX >= 0 && 
				movable(players[currentPlayer].getX(), players[currentPlayer].getY(), x, y)) {

			// set the new positions of the player
			players[currentPlayer].setX(moveX);

			if(x > 0)
				playerMoveDirection = "right";
			else
				playerMoveDirection = "left";

			playerMoveAmount = tileIconSize;
			
			// start the shifting animation by starting the player shifting timer
			playerShiftTimer.start();

			AudioPlayer.playAudio("audio/move.wav");

		}

		// if the moving is within the board in the y direction, then update player's position
		if(y != 0 && moveY < BOARD_SIZE && moveY >= 0 &&
				movable(players[currentPlayer].getX(), players[currentPlayer].getY(), x, y)) {

			players[currentPlayer].setY(moveY);

			if(y > 0)
				playerMoveDirection = "down";
			else
				playerMoveDirection = "up";

			playerMoveAmount = tileIconSize;
			
			playerShiftTimer.start();

			AudioPlayer.playAudio("audio/move.wav");

		}

	}

	// method from the interface mover, checks if two tiles can be connected with a path
	@Override
	public boolean movable(int x, int y, int moveX, int moveY) {

		Tile currentTile = board[x][y];

		// moving down, top block must have down connection
		if(moveY > 0 && currentTile.isDown()) {

			return board[x][y+1].isUp();

		} 

		// moving up, top block must have up connection
		else if(moveY < 0 && currentTile.isUp()) {

			return board[x][y-1].isDown();

		}

		// moving right, right block must have left connection
		if(moveX > 0 && currentTile.isRight()) {

			return board[x+1][y].isLeft();

		}

		// moving left, left block must have right connection
		else if(moveX < 0 && currentTile.isLeft()) {

			return board[x-1][y].isRight();

		}

		return false;

	}

	// method that rotates the extra tile clockwise
	public void rotateExtraTileClockWise(boolean testing) {

		if(!testing)
			AudioPlayer.playAudio("audio/rotate.wav");

		// call the rotate clock wise method from within the tile class itself
		extraPiece.rotateTileClockWise();

		if(!testing)
			// reset the new icon for the extra tile
			extraPieceLabel.setIcon(new ImageIcon(new ImageIcon(extraPiece.getFilePath())
					.getImage().getScaledInstance(92, 92, 0)));

	}
	
	// method that rotates the extra tile counterclockwise
	public void rotateExtraTileCounterClockWise(boolean testing) {
		
		if(!testing)
			AudioPlayer.playAudio("audio/rotate.wav");

		extraPiece.rotateTileCounterClockWise();

		if(!testing)
			extraPieceLabel.setIcon(new ImageIcon(new ImageIcon(extraPiece.getFilePath())
					.getImage().getScaledInstance(92, 92, 0)));

	}
	
	//Check the cards on the board and the card you have on your hand
	public void checkCards() {
		
		for(int i = 0; i < hands[currentPlayer].size(); i++) {
			
			if(hands[currentPlayer].get(i)+1 == board[players[currentPlayer].getX()][players[currentPlayer].getY()].getItem()) {
				
				cardImages[currentPlayer].get(i).setIcon(iconLogo);
				cardImages[currentPlayer].remove(i);
				hands[currentPlayer].remove(i);
				unhighlightTiles();
				highlightTiles();
				AudioPlayer.playAudio("audio/cardCollected.wav");
				break;
				
			}
			
		}
		
		if(hands[currentPlayer].isEmpty()) {
			
			AudioPlayer.playAudio("audio/gameOver.wav");
			JOptionPane.showMessageDialog(null, "Player " + (currentPlayer+1) + " have finished all their cards!!!");
			players[currentPlayer].setActive(false);
			Winner.add(currentPlayer);
			endTurn();
			
		}
		
		//When their is 3 winner, that means that game is completely finished and it will stops and create a new state
		if (Winner.size() ==3) {
			
			gameEnded = true;
			//since the winner class has only the size of 3, add whatever that is missing and they will be in last place. 
			if (!Winner.contains(1)) {
				Winner.add(1);
			}else if (!Winner.contains(2)) {
				Winner.add(2);
			}else if (!Winner.contains(3)) {
				Winner.add(3);
			}else if (!Winner.contains(4)) {
				Winner.add(4);
			}
			
			JOptionPane.showMessageDialog(null, "Game finished!!!");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//opens the last frame
			autoMoveTimer.stop();
			playerShiftTimer.stop();
			tileShiftTimer.stop();
			MusicPlayer.stopMusic();
			new EndState(Winner);
			this.dispose();
			
		}
		
	}
	
	private void highlightTiles() {
		
		for(int i = 0; i < BOARD_SIZE; i++) {
			for(int j = 0; j < BOARD_SIZE; j++) {
				
				if(hands[currentPlayer].contains(board[i][j].getItem()-1)) {
					
					JLabel highlight = new JLabel(new ImageIcon(new ImageIcon("images/walkableButton.png")
							.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));
					highlight.setBounds(boardIcons[i][j].getX(), boardIcons[i][j].getY(), tileIconSize, tileIconSize);
					highlightedPath.add(highlight);
					
					gamePanel.add(highlight, 0);
					
				}
				
			}
		}
		
	}
	
	private void unhighlightTiles() {
		
		for(int i = 0; i < highlightedPath.size(); i++) {
			
			gamePanel.remove(highlightedPath.get(i));
			
		}
		
		repaint();
		highlightedPath.clear();
		
	}
	
	// method that shifts the player along with shifting the tiles
	private void shiftPlayer(Player player, int playerID, int direction) {

		// 
		shiftedPlayers.add(player);
		
		// checks which direction player is moving
		switch (direction) {
		
			// checks if the player is moving down
			case 1: {
				
				playerMoveDirection = "down";

				// set the y position of the player
				player.setY(player.getY() + 1);

				// if player Y is out of the bound of the board, then bring it to the other sidee of the board
				if(player.getY() >= BOARD_SIZE) {

					player.setY(0);

				} 
				
				break;
				
			} case 2: {
				
				// checks if the player is moving left
				playerMoveDirection = "left";

				player.setX(player.getX() - 1);

				if(player.getX() < 0) {

					player.setX(BOARD_SIZE-1);

				} 
				
				break;
				
			} case 3: {
				
				// checks if the player is moving up
				playerMoveDirection = "up";

				player.setY(player.getY() - 1);

				if(player.getY() < 0) {

					player.setY(BOARD_SIZE-1);

				} 
				
				break;
				
			} case 4: {
				
				// checks if the player is moving right
				playerMoveDirection = "right";

				player.setX(player.getX() + 1);

				if(player.getX() >= BOARD_SIZE) {

					player.setX(0);

				} 
				
			}
		
		} 

	}

	// Inherent method from the action listener that checks JComponent actions
	@Override
	public void actionPerformed(ActionEvent event) {
		
		// if the action source is the auto move timer, then move the player automatically
		if (event.getSource() == autoMoveTimer) {

			// if the player cannot click, then exit the method
			if(!canClick) {

				return;

			}
			
			// if the auto move set is not empty, then move the player to its next direction in the move set
			if(!AIMoveSet.isEmpty()) {

				// take the first element from the move set to use it to move the player
				String nextMove = AIMoveSet.poll();

				// update the player's position from the move set operation - up, down, left, right
				if(nextMove.equals("up")) {

					updatePosition(0, -1);
					checkCards();

				} else if(nextMove.equals("down")) {

					updatePosition(0, 1);
					checkCards();

				} else if(nextMove.equals("left")) {

					updatePosition(-1, 0);
					checkCards();

				} else if(nextMove.equals("right")) {

					updatePosition(1, 0);
					checkCards();

				}

			} else {

				// if player is an AI and the move set is empty, then end the current turn
				if(players[currentPlayer].isAI() && findNumAvaliableItem() == 0) {
					
					if(canShift) {
						
						preventWin();
						
					} else {
						
						endTurn();
						
					}

				} else if (players[currentPlayer].isAI()){
					
					AIFindCard();
					
				}

			}

		} 
		
		// if player shift timer is active, the move the player to its destination from tone tile to another
		else if (event.getSource() == playerShiftTimer) {

			// during the shift, player cannot click
			canClick = false;

			// checks the player's shift direction then shift the icon to the direction by a small amount of pixels to create an animation
			if(playerMoveDirection.equals("up")) {

				playerIcons[currentPlayer].setBounds(
						playerIcons[currentPlayer].getX(), playerIcons[currentPlayer].getY() - 2, tileIconSize, tileIconSize);


			} else if(playerMoveDirection.equals("down")) {

				playerIcons[currentPlayer].setBounds(
						playerIcons[currentPlayer].getX(), playerIcons[currentPlayer].getY() + 2, tileIconSize, tileIconSize);

			} else if(playerMoveDirection.equals("left")) {

				playerIcons[currentPlayer].setBounds(
						playerIcons[currentPlayer].getX() - 2, playerIcons[currentPlayer].getY(), tileIconSize, tileIconSize);

			} else if(playerMoveDirection.equals("right")) {

				playerIcons[currentPlayer].setBounds(
						playerIcons[currentPlayer].getX() + 2, playerIcons[currentPlayer].getY(), tileIconSize, tileIconSize);

			}

			// subtract the amount of pixels the players have to move 
			playerMoveAmount -= 2;

			// repaint the current player icon
			playerIcons[currentPlayer].repaint();

			// if the player shifting is complete, then enable clicking and stop the shift timer
			if(playerMoveAmount == 0) {

				playerShiftTimer.stop();

				canClick = true;

			}

		} 
		
		// 
		else if(event.getSource() == tileShiftTimer) {

			canClick = false;

			if(shiftID <= 2) {

				extraPieceLabel.setBounds(extraPieceLabel.getX(), extraPieceLabel.getY() + 2, tileIconSize, tileIconSize);

				for(int i = 0; i < BOARD_SIZE; i++) {

					boardIcons[shiftID*2 + 1][i].setBounds(boardIcons[shiftID*2 + 1][i].getX(), boardIcons[shiftID*2 + 1][i].getY() + 2, tileIconSize, tileIconSize);
					boardIcons[shiftID*2 + 1][i].repaint();

				}

				for(Player player: shiftedPlayers) {

					playerIcons[player.getId()].setBounds(playerIcons[player.getId()].getX(), playerIcons[player.getId()].getY() + 2, tileIconSize, tileIconSize);

					playerIcons[player.getId()].repaint();

				}

			} else if(shiftID <= 5) {

				extraPieceLabel.setBounds(extraPieceLabel.getX() - 2, extraPieceLabel.getY(), tileIconSize, tileIconSize);

				for(int i = 0; i < BOARD_SIZE; i++) {

					boardIcons[i][(shiftID-3)*2 + 1].setBounds(boardIcons[i][(shiftID-3)*2 + 1].getX() - 2, boardIcons[i][(shiftID-3)*2 + 1].getY(), tileIconSize, tileIconSize);
					boardIcons[i][(shiftID-3)*2 + 1].repaint();
					extraPieceLabel.repaint();

				}

				for(Player player: shiftedPlayers) {

					playerIcons[player.getId()].setBounds(playerIcons[player.getId()].getX() - 2, playerIcons[player.getId()].getY(), tileIconSize, tileIconSize);
					playerIcons[player.getId()].repaint();

				}

			} else if(shiftID <= 8) {

				extraPieceLabel.setBounds(extraPieceLabel.getX(), extraPieceLabel.getY() - 2, tileIconSize, tileIconSize);

				for(int i = 0; i < BOARD_SIZE; i++) {

					boardIcons[(shiftID-6)*2 + 1][i].setBounds(boardIcons[(shiftID-6)*2 + 1][i].getX(), boardIcons[(shiftID-6)*2 + 1][i].getY() - 2, tileIconSize, tileIconSize);
					boardIcons[(shiftID-6)*2 + 1][i].repaint();
					extraPieceLabel.repaint();

				}

				for(Player player: shiftedPlayers) {

					playerIcons[player.getId()].setBounds(playerIcons[player.getId()].getX(), playerIcons[player.getId()].getY() - 2, tileIconSize, tileIconSize);
					playerIcons[player.getId()].repaint();

				}

			} else if(shiftID <= 11) {

				extraPieceLabel.setBounds(extraPieceLabel.getX() + 2, extraPieceLabel.getY(), tileIconSize, tileIconSize);

				for(int i = 0; i < BOARD_SIZE; i++) {

					boardIcons[i][(shiftID-9)*2 + 1].setBounds(boardIcons[i][(shiftID-9)*2 + 1].getX() + 2, boardIcons[i][(shiftID-9)*2 + 1].getY(), tileIconSize, tileIconSize);
					boardIcons[i][(shiftID-9)*2 + 1].repaint();
					extraPieceLabel.repaint();

				}

				for(Player player: shiftedPlayers) {

					playerIcons[player.getId()].setBounds(playerIcons[player.getId()].getX() + 2, playerIcons[player.getId()].getY(), tileIconSize, tileIconSize);
					playerIcons[player.getId()].repaint();

				}

			}

			tileMoveAmount -= 2;

			if(tileMoveAmount == 0) {

				tileShiftTimer.stop();

				// regenerate all game tiles to its original position
				for(int i = 0; i < BOARD_SIZE; i++) {
					for(int j = 0; j < BOARD_SIZE; j++) {

						boardIcons[i][j].setIcon(new ImageIcon(new ImageIcon(board[i][j].getFilePath())
								.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));

						boardIcons[i][j].setBounds(scaledOrginX + tileIconSize + tileIconSize*i, 
								scaledOrginY + tileIconSize + tileIconSize*j, tileIconSize, tileIconSize);

						boardIcons[i][j].repaint();

					}
				}

				for(Player player: shiftedPlayers) {

					playerShiftValidation(player.getId());

				}

				shiftedPlayers.clear();

				extraPieceLabel.setIcon(new ImageIcon(new ImageIcon(extraPiece.getFilePath())
						.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));

				extraPieceLabel.setBounds(890 + tileIconSize, 200, tileIconSize, tileIconSize);
				extraPieceLabel.repaint();

				canClick = true;

			}

		} else if(event.getSource().equals(rotateClockWise)) {

			rotateExtraTileClockWise(false);
			
		} else if(event.getSource().equals(rotateCounterClockWise)) {

			rotateExtraTileCounterClockWise(false);
			
		} else if(event.getSource().equals(saveButton)) {
			
			saveGameName.setFocusable(false);
			
			if(saveGameName.getText().length() == 0) {
				
				// display message dialogue for invalid input
				JOptionPane.showMessageDialog(null, "File name cannot be empty to be saved \n\n"
								+ "click 'ok' to continue...",
						"INVALID FILE NAME", JOptionPane.WARNING_MESSAGE);
				
			} else {
				
				String fileName = "saved/"+saveGameName.getText();
				
				try {
					
					PrintWriter outputStream = new PrintWriter(fileName);
					
					File files = new File("saved");
					for(File currentFile: files.listFiles()) {
						
						if(currentFile.getName().equals(fileName)) {
							
							currentFile.delete();
							break;
							
						}
						
					}
					
					for(int i = 0; i < BOARD_SIZE; i++) {
						
						for(int j = 0; j < BOARD_SIZE; j++) {
							
							outputStream.print(board[i][j].getId() + " " + board[i][j].getItem() + " ");
							
						}
						
						outputStream.println();
						
					}
					
					outputStream.println(extraPiece.getId() + " " + extraPiece.getItem());
					
					for(int player = 0; player < 4; player++) {
						
						if(players[player].isAI()) {
							
							outputStream.println(players[player].getX() + " " + players[player].getY() + " " + 1);
							
						} else {
							
							outputStream.println(players[player].getX() + " " + players[player].getY() + " " + 0);
							
						}
						
						outputStream.println(players[player].isActive());
						
					}
					
					outputStream.println(currentPlayer);
					outputStream.println(canShift);
					
					outputStream.close();
					
					// display message dialogue for invalid input
					JOptionPane.showMessageDialog(null, "Game Successfully saved \n\n"
									+ "click 'ok' to continue...",
							"SAVE COMPLETE", JOptionPane.INFORMATION_MESSAGE);
					
				} catch(FileNotFoundException error) {
					
					System.out.println("save file not found");
					
				}
				
				saveGameName.setText("");
				
			}
			
		}
		
		for(int button = 0; button < tileShiftButtons.size(); button++) {

			if(canShift && event.getSource().equals(tileShiftButtons.get(button))) {

				shiftID = button;

				if(canShift) {
					shiftButtonClick();
					canShift = false;
				}

			} else if(!canShift && event.getSource().equals(tileShiftButtons.get(button))) {
				
				AudioPlayer.playAudio("audio/deny.wav");
				
			}

		}

	}

	// method that makes sure the player is within the board
	// if not, then move the player to the other side of the board
	private void playerShiftValidation(int id) {

		// if player is being shifted above the tiles, reset location to the bottom
		if(playerIcons[id].getY() < boardIcons[0][0].getY()) {

			playerIcons[id].setBounds(playerIcons[id].getX(), boardIcons[0][BOARD_SIZE-1].getY(),
					tileIconSize, tileIconSize);

			playerIcons[id].repaint();

		}

		// if player is being shifted below the tiles, reset location to the top
		if(playerIcons[id].getY() > boardIcons[0][BOARD_SIZE-1].getY()) {

			playerIcons[id].setBounds(playerIcons[id].getX(), boardIcons[0][0].getY(),
					tileIconSize, tileIconSize);

			playerIcons[id].repaint();

		}

		// if player is being shifted left of the tiles, reset location to the right
		if(playerIcons[id].getX() < boardIcons[0][0].getX()) {

			playerIcons[id].setBounds(boardIcons[BOARD_SIZE-1][0].getX(), playerIcons[id].getY(),
					tileIconSize, tileIconSize);

			playerIcons[id].repaint();

		}

		// if player is being shifted right of the tiles, reset location to the left
		if(playerIcons[id].getX() > boardIcons[BOARD_SIZE-1][0].getX()) {

			playerIcons[id].setBounds(boardIcons[0][0].getX(), playerIcons[id].getY(),
					tileIconSize, tileIconSize);

			playerIcons[id].repaint();

		}

	}

	private void AIFindCard() {
		
		clearWalkLines();
		// generate new walk lines for the next player
		viewPath(players[currentPlayer].getX(), players[currentPlayer].getY(), 0, new LinkedList<String>(), new ArrayList<Point>());

		// checks if there is a path available for the AI to move
		if(!potentialPathways.isEmpty()) {

			for(int i = 0; i < potentialPathways.size(); i++) {
				
				if(hands[currentPlayer].contains(potentialPathways.get(i).getTile().getItem()-1)) {
					
					AIMoveSet = potentialPathways.get(i).getTrack();
						
				}
				
			}
			
			// start the move timer for the AI to auto move
			autoMoveTimer.start();

		} else {
			
			if(canShift) {
				
				selectAIRotateButton();
				// start the move timer for the AI to auto move
				autoMoveTimer.start();
				
			}
			
		}
		
	}
	
	private void preventWin() {
		
		blockPlayerTest();
		
		int tempCurrentPlayer = currentPlayer;
		
		currentPlayer = 0;
		
		boolean playerHasDirectItem = false;
		
		for(int i = 0; i < directItems.length; i++) {
			
			if(hands[currentPlayer].size() - directItems[i].size() == 0 && players[currentPlayer].isActive()) {
				
				currentPlayer = i;
				
				if(directItems[i].size() > 0) {
					
					playerHasDirectItem = true;
					
				} else {
					
					playerHasDirectItem = false;
					
				}
				
				break;
				
			}
			
		}
		
		if(players[tempCurrentPlayer].getAILevel() >= 2 && playerHasDirectItem) {
			
			System.out.println("prevented win: Player " + (currentPlayer+1));
			
			Tile tempExtraTile = extraPiece;
			
			int minIndex = 0;
			int minAmount = Integer.MAX_VALUE;
			int rotate = 0;
			
			ArrayList<Integer> directItems = new ArrayList<Integer>();
			
			for(int i = 0; i < 4; i++) {
				
				directItems.add(numItemsCanGet(0, 6, i));
				directItems.add(numItemsCanGet(1, 7, i));
				directItems.add(numItemsCanGet(2, 8, i));
				directItems.add(numItemsCanGet(3, 9, i));
				directItems.add(numItemsCanGet(4, 10, i));
				directItems.add(numItemsCanGet(5, 11, i));
				directItems.add(numItemsCanGet(6, 0, i));
				directItems.add(numItemsCanGet(7, 1, i));
				directItems.add(numItemsCanGet(8, 2, i));
				directItems.add(numItemsCanGet(9, 3, i));
				directItems.add(numItemsCanGet(10, 4, i));
				directItems.add(numItemsCanGet(11, 5, i));
				
				for(int j = 0; j < directItems.size(); j++) {
					
					if(directItems.get(j) < minAmount) {
						minIndex = j;
						minAmount = directItems.get(minIndex);
						rotate = i;
					}
				}
				
				directItems.clear();
				
			}
			
			extraPiece = tempExtraTile;
			
			if(rotate == 1) {
				rotateExtraTileClockWise(true);
			} else if(rotate == 2) {
				rotateExtraTileClockWise(true);
				rotateExtraTileClockWise(true);
			} else if(rotate == 3) {
				rotateExtraTileCounterClockWise(true);
			}
			
			currentPlayer = tempCurrentPlayer;
			
			shiftID = minIndex;
			
			if(canShift) {
				
				shiftButtonClick();
				canShift = false;
				
			}
			
		} else {
			
			currentPlayer = tempCurrentPlayer;
			selectAIRotateButton();
			
		}
		
	}
	
	private void blockPlayer() {
		
		blockPlayerTest();
		
		int tempCurrentPlayer = currentPlayer;
		
		currentPlayer = 0;
		
		boolean playerHasDirectItem = false;
		
		for(int i = 0; i < directItems.length; i++) {
			
			if(directItems[i].size() > directItems[currentPlayer].size()) {
				
				currentPlayer = i;
				
				if(directItems[i].size() > 0) {
					
					playerHasDirectItem = true;
					
				} else {
					
					playerHasDirectItem = false;
					
				}
				
			}
			
		}
		
		//System.out.println(playerHasDirectItem + "   " + players[currentPlayer].getAILevel());
		if(!playerHasDirectItem && players[tempCurrentPlayer].getAILevel() == 3) {
			currentPlayer = tempCurrentPlayer;
			findFuturePath();
			return;
			
		} else if(!playerHasDirectItem) {
			System.out.println("no players to block and collectable treasures are not near, shift randomized");
			System.out.println("AI Level too low for improved steps");
			
			currentPlayer = tempCurrentPlayer;
			
			shiftID = (int)(Math.random()*3);
			
			if(shiftID == 1) {
				
				endTurn();
				return;
				
			}
			
			shiftID = (int)(Math.random()*12);
			
			if(canShift) {
				
				shiftButtonClick();
				canShift = false;
				
			}
			
			return;
			
		}

		System.out.println("blocked player: " + (currentPlayer+1));
		
		Tile tempExtraTile = extraPiece;
		
		int minIndex = 0;
		int minAmount = Integer.MAX_VALUE;
		int rotate = 0;
		
		ArrayList<Integer> directItems = new ArrayList<Integer>();
		
		for(int i = 0; i < 4; i++) {
			
			directItems.add(numItemsCanGet(0, 6, i));
			directItems.add(numItemsCanGet(1, 7, i));
			directItems.add(numItemsCanGet(2, 8, i));
			directItems.add(numItemsCanGet(3, 9, i));
			directItems.add(numItemsCanGet(4, 10, i));
			directItems.add(numItemsCanGet(5, 11, i));
			directItems.add(numItemsCanGet(6, 0, i));
			directItems.add(numItemsCanGet(7, 1, i));
			directItems.add(numItemsCanGet(8, 2, i));
			directItems.add(numItemsCanGet(9, 3, i));
			directItems.add(numItemsCanGet(10, 4, i));
			directItems.add(numItemsCanGet(11, 5, i));
			
			for(int j = 0; j < directItems.size(); j++) {
				
				if(directItems.get(j) < minAmount) {
					minIndex = j;
					minAmount = directItems.get(minIndex);
					rotate = i;
				}
			}
			
			directItems.clear();
			
		}
		
		extraPiece = tempExtraTile;
		
		if(rotate == 1) {
			rotateExtraTileClockWise(true);
		} else if(rotate == 2) {
			rotateExtraTileClockWise(true);
			rotateExtraTileClockWise(true);
		} else if(rotate == 3) {
			rotateExtraTileCounterClockWise(true);
		}
		
		currentPlayer = tempCurrentPlayer;
		
		shiftID = minIndex;
		
		if(canShift) {
			
			shiftButtonClick();
			canShift = false;
			
		}
		
	}
	
	private void blockPlayerTest() {
		
		for(int i = 0; i < 4; i++) {
			
			directItems[i].clear();
			
		}
		
		int tempCurrentPlayer = currentPlayer;
		
		for(currentPlayer = 0; currentPlayer < 4; currentPlayer++) {
			
			clearWalkLines();
			// generate new potential pathways
			viewPath(players[currentPlayer].getX(), players[currentPlayer].getY(), 0, new LinkedList<String>(), new ArrayList<Point>());

			for(int j = 0; j < potentialPathways.size(); j++) {
				
				if(hands[currentPlayer].contains(potentialPathways.get(j).getTile().getItem()-1) && 
						!directItems[currentPlayer].contains(potentialPathways.get(j).getTile())) {
					
					directItems[currentPlayer].add(potentialPathways.get(j).getTile());
					
				}
				
			}
			
		}
		
		currentPlayer = tempCurrentPlayer;
		clearWalkLines();
		viewPath(players[currentPlayer].getX(), players[currentPlayer].getY(), 0, new LinkedList<String>(), new ArrayList<Point>());
		
	}
	
	private void findFuturePath() {
		
		Tile tempExtraTile = extraPiece;
		
		int maxIndex = 0;
		int maxAmount = 0;
		int rotate = 0;
		
		ArrayList<Integer> directItems = new ArrayList<Integer>();
		
		for(int i = 0; i < 4; i++) {
			
			directItems.add(findPotentialPathwaySize(0, 6, i));
			directItems.add(findPotentialPathwaySize(1, 7, i));
			directItems.add(findPotentialPathwaySize(2, 8, i));
			directItems.add(findPotentialPathwaySize(3, 9, i));
			directItems.add(findPotentialPathwaySize(4, 10, i));
			directItems.add(findPotentialPathwaySize(5, 11, i));
			directItems.add(findPotentialPathwaySize(6, 0, i));
			directItems.add(findPotentialPathwaySize(7, 1, i));
			directItems.add(findPotentialPathwaySize(8, 2, i));
			directItems.add(findPotentialPathwaySize(9, 3, i));
			directItems.add(findPotentialPathwaySize(10, 4, i));
			directItems.add(findPotentialPathwaySize(11, 5, i));
			
			for(int j = 0; j < directItems.size(); j++) {
				
				if(directItems.get(j) > maxAmount) {
					maxIndex = j;
					maxAmount = directItems.get(maxIndex);
					rotate = i;
				}
				
			}
			
			directItems.clear();
			
		}
		
		extraPiece = tempExtraTile;
		
		if(rotate == 1) {
			rotateExtraTileClockWise(true);
		} else if(rotate == 2) {
			rotateExtraTileClockWise(true);
			rotateExtraTileClockWise(true);
		} else if(rotate == 3) {
			rotateExtraTileCounterClockWise(true);
		}
		
		System.out.println("moveable path size expanded for future move");
			
		shiftID = maxIndex;
			
		if(canShift) {
			shiftButtonClick();
			canShift = false;
		}
		
	}
	
	private void expandSpace() {
		
		Tile tempExtraTile = extraPiece;
		
		int maxIndex = 0;
		int maxAmount = 0;
		int rotate = 0;
		
		ArrayList<Integer> directItems = new ArrayList<Integer>();
		
		for(int i = 0; i < 4; i++) {
			
			directItems.add(findPotentialPathwaySize(0, 6, i));
			directItems.add(findPotentialPathwaySize(1, 7, i));
			directItems.add(findPotentialPathwaySize(2, 8, i));
			directItems.add(findPotentialPathwaySize(3, 9, i));
			directItems.add(findPotentialPathwaySize(4, 10, i));
			directItems.add(findPotentialPathwaySize(5, 11, i));
			directItems.add(findPotentialPathwaySize(6, 0, i));
			directItems.add(findPotentialPathwaySize(7, 1, i));
			directItems.add(findPotentialPathwaySize(8, 2, i));
			directItems.add(findPotentialPathwaySize(9, 3, i));
			directItems.add(findPotentialPathwaySize(10, 4, i));
			directItems.add(findPotentialPathwaySize(11, 5, i));
			
			for(int j = 0; j < directItems.size(); j++) {
				
				if(directItems.get(j) > maxAmount) {
					maxIndex = j;
					maxAmount = directItems.get(maxIndex);
					rotate = i;
				}
				
			}
			
			directItems.clear();
			
		}
		
		extraPiece = tempExtraTile;
		
		if(rotate == 1) {
			rotateExtraTileClockWise(true);
		} else if(rotate == 2) {
			rotateExtraTileClockWise(true);
			rotateExtraTileClockWise(true);
		} else if(rotate == 3) {
			rotateExtraTileCounterClockWise(true);
		}
		
		System.out.println("moveable path size expanded");
			
		shiftID = maxIndex;
			
		if(canShift) {
			shiftButtonClick();
			canShift = false;
		}
		
	}
	
	private int findNumAvaliableItem() {
		
		int numItem = 0;
		for(int i = 0; i < potentialPathways.size(); i++) {
			
			if(hands[currentPlayer].contains(potentialPathways.get(i).getTile().getItem()-1)) {
				
				numItem++;
					
			}
			
		}
		
		return numItem;
		
	}
	
	private void selectAIRotateButton() {
		
		Tile tempExtraTile = extraPiece;
		
		int maxIndex = 0;
		int maxAmount = 0;
		int rotate = 0;
		
		int[] directItems = new int[12];
		
		for(int i = 0; i < 4; i++) {
			
			directItems[0] = (numItemsCanGet(0, 6, i));
			directItems[1] = (numItemsCanGet(1, 7, i));
			directItems[2] = (numItemsCanGet(2, 8, i));
			directItems[3] = (numItemsCanGet(3, 9, i));
			directItems[4] = (numItemsCanGet(4, 10, i));
			directItems[5] = (numItemsCanGet(5, 11, i));
			directItems[6] = (numItemsCanGet(6, 0, i));
			directItems[7] = (numItemsCanGet(7, 1, i));
			directItems[8] = (numItemsCanGet(8, 2, i));
			directItems[9] = (numItemsCanGet(9, 3, i));
			directItems[10] = (numItemsCanGet(10, 4, i));
			directItems[11] = (numItemsCanGet(11, 5, i));
			
			for(int j = 0; j < directItems.length; j++) {
				
				if(directItems[j] > maxAmount) {
					
					maxIndex = j;
					maxAmount = directItems[j];
					rotate = i;
					
				}
				
			}
			
			Arrays.fill(directItems, 0);
			
		}
		
		extraPiece = tempExtraTile;
		
		if(rotate == 1) {
			rotateExtraTileClockWise(true);
		} else if(rotate == 2) {
			rotateExtraTileClockWise(true);
			rotateExtraTileClockWise(true);
		} else if(rotate == 3) {
			rotateExtraTileCounterClockWise(true);
		}
		
		if(maxAmount == 0 && players[currentPlayer].getAILevel() >= 2) {
			
			blockPlayer();
			
		} else if(maxAmount == 0) {
			
			System.out.println("no collectable treasures, shift randomized");
			System.out.println("AI Level too low for improved steps");
			
			shiftID = (int)(Math.random()*3);
			
			if(shiftID == 1) {
				
				endTurn();
				return;
				
			}

			shiftID = (int)(Math.random()*12);
			
			if(shiftID == 12) {
				
				endTurn();
				return;
				
			}
			
			if(canShift) {
				shiftButtonClick();
				canShift = false;
			}
			
		} else {
			
			System.out.println("Shifted Tile: " + maxIndex + " Amount: " + maxAmount + " Rotate: " + rotate);
			
			shiftID = maxIndex;
			
			if(canShift) {
				shiftButtonClick();
				canShift = false;
			}
		
		}
		
	}
	
	// method that executes the actions of the shift buttons
	private void shiftButtonClick() {

		// move the movable columns downwards
		if(shiftID >= 0 && shiftID <= 2) {

			// create a temporary extra piece tile at the last tile of the shiftID
			Tile tempExtraPiece = board[1 + shiftID*2][BOARD_SIZE-1];

			// shiftID each tile to the adjacent tiles
			for(int j = BOARD_SIZE - 1; j > 0; j--) {

				board[1 + shiftID*2][j] = board[1 + shiftID*2][j-1];

				boardIcons[1 + shiftID*2][j].setIcon(new ImageIcon(new ImageIcon(board[1 + shiftID*2][j].getFilePath())
						.getImage().getScaledInstance(92, 92, 0)));

			}

			// set the extra piece label to the button location for it to animate sliding in
			extraPieceLabel.setBounds(boardIcons[1 + shiftID*2][0].getX(), 
					boardIcons[1 + shiftID*2][0].getY() - tileIconSize, tileIconSize, tileIconSize);

			// checks if any players are on the tile, if there are, then shiftID them also by calling shiftPlayer method
			for(int index = 0; index < players.length; index++) {

				if(players[index].getX() == 1 + shiftID*2) {

					shiftPlayer(players[index], index, 1);

				}

			}

			// updates the extra piece tile to the appropriate location
			board[1 + shiftID*2][0] = extraPiece;

			// update the current extra piece tile
			extraPiece = tempExtraPiece;

		}

		// move movable rows leftwards
		else if(shiftID >= 3 && shiftID <= 5) {

			Tile tempExtraPiece = board[0][1 + (shiftID-3)*2];

			for(int j = 0; j < BOARD_SIZE-1; j++) {

				board[j][1 + (shiftID-3)*2] = board[j+1][1 + (shiftID-3)*2];

				boardIcons[j][1 + (shiftID-3)*2].setIcon(new ImageIcon(new ImageIcon(board[j][1 + (shiftID-3)*2].getFilePath())
						.getImage().getScaledInstance(92, 92, 0)));

			}

			extraPieceLabel.setBounds(boardIcons[boardIcons.length - 1][1 + (shiftID-3)*2].getX() + tileIconSize, 
					boardIcons[boardIcons.length - 1][1 + (shiftID-3)*2].getY(), tileIconSize, tileIconSize);

			for(int index = 0; index < players.length; index++) {

				if(players[index].getY() == 1 + (shiftID-3)*2) {

					shiftPlayer(players[index], index, 2);

				}

			}

			board[BOARD_SIZE-1][1 + (shiftID-3)*2] = extraPiece;

			extraPiece = tempExtraPiece;

		}	

		// move the movable columns upwards
		else if(shiftID >= 6 && shiftID <= 8) {

			Tile tempExtraPiece = board[1 + (shiftID-6)*2][0];

			for(int j = 0; j < BOARD_SIZE - 1; j++) {

				board[1 + (shiftID-6)*2][j] = board[1 + (shiftID-6)*2][j+1];

				boardIcons[1 + (shiftID-6)*2][j].setIcon(new ImageIcon(new ImageIcon(board[1 + (shiftID-6)*2][j].getFilePath())
						.getImage().getScaledInstance(92, 92, 0)));

			}

			extraPieceLabel.setBounds(boardIcons[1 + (shiftID-6)*2][boardIcons.length - 1].getX(), 
					boardIcons[1 + (shiftID-6)*2][boardIcons.length - 1].getY() + tileIconSize, tileIconSize, tileIconSize);

			for(int index = 0; index < players.length; index++) {

				if(players[index].getX() == 1 + (shiftID-6)*2) {

					shiftPlayer(players[index], index, 3);

				}

			}

			board[1 + (shiftID-6)*2][BOARD_SIZE-1] = extraPiece;

			extraPiece = tempExtraPiece;

		}

		// move movable rows rightwards
		else if(shiftID >= 9 && shiftID <= 11) {

			Tile tempExtraPiece = board[BOARD_SIZE - 1][1 + (shiftID-9)*2];

			for(int j = BOARD_SIZE - 1; j > 0; j--) {

				board[j][1 + (shiftID-9)*2] = board[j-1][1 + (shiftID-9)*2];

				boardIcons[j][1 + (shiftID-9)*2].setIcon(new ImageIcon(new ImageIcon(board[j][1 + (shiftID-9)*2].getFilePath())
						.getImage().getScaledInstance(92, 92, 0)));

			}

			extraPieceLabel.setBounds(boardIcons[0][1 + (shiftID-9)*2].getX() - tileIconSize, 
					boardIcons[0][1 + (shiftID-9)*2].getY(), tileIconSize, tileIconSize);

			for(int index = 0; index < players.length; index++) {

				if(players[index].getY() == 1 + (shiftID-9)*2) {

					shiftPlayer(players[index], index, 4);

				}

			}

			board[0][1 + (shiftID-9)*2] = extraPiece;

			extraPiece = tempExtraPiece;

		}	

		unhighlightTiles();
		highlightTiles();
		
		// to start the animation, the amount of pixels the tile have to move is exactly the tile size
		tileMoveAmount = tileIconSize;

		// there can be only one shift every turn, disable tile shifting
		canShift = false;

		// clear potential pathways so new ones can be generated
		clearWalkLines();

		// generate new potential pathways
		viewPath(players[currentPlayer].getX(), players[currentPlayer].getY(), 0, new LinkedList<String>(), new ArrayList<Point>());

		if(players[currentPlayer].isAI())
			AIFindCard();
		
		// update the shift button icons to the invalid icon
		updateTileShiftButtonIcon();

		// the player on the tile's move amount will also be the size of a tile
		playerMoveAmount = tileIconSize;
		
		// start the timer for the tiles to shift in animation
		tileShiftTimer.start();

		AudioPlayer.playAudio("audio/buttonSlide.wav");

	}	
	private void shiftTiles(int shift) {
		
		// move the movable columns downwards
		if(shift >= 0 && shift <= 2) {

			// create a temporary extra piece tile at the last tile of the shiftID
			Tile tempExtraPiece = board[1 + shift*2][BOARD_SIZE-1];
						
			// shift each tile to the adjacent tiles
			for(int j = BOARD_SIZE - 1; j > 0; j--) {

				board[1 + shift*2][j] = board[1 + shift*2][j-1];

			}

			// checks if any players are on the tile, if there are, then shift them also by calling shiftPlayer method
			for(int index = 0; index < players.length; index++) {

				if(players[index].getX() == 1 + shift*2) {

					players[index].setY(players[index].getY()+1);

				}
				
				if(players[index].getY() > BOARD_SIZE-1)
					players[index].setY(0);

			}

			// updates the extra piece tile to the appropriate location
			board[1 + shift*2][0] = extraPiece;
			
			extraPiece = tempExtraPiece;

		}

		// move movable rows leftwards
		else if(shift >= 3 && shift <= 5) {
			
			Tile tempExtraPiece = board[0][1 + (shift-3)*2];
			
			for(int j = 0; j < BOARD_SIZE-1; j++) {

				board[j][1 + (shift-3)*2] = board[j+1][1 + (shift-3)*2];
				
			}
			
			for(int index = 0; index < players.length; index++) {

				if(players[index].getY() == 1 + (shift-3)*2) {

					players[index].setX(players[index].getX()-1);

				}
				
				if(players[index].getX() < 0)
					players[index].setX(BOARD_SIZE-1);

			}

			board[BOARD_SIZE-1][1 + (shift-3)*2] = extraPiece;

			extraPiece = tempExtraPiece;
			
		}	

		// move the movable columns upwards
		else if(shift >= 6 && shift <= 8) {
			
			Tile tempExtraPiece = board[1 + (shift-6)*2][0];
			
			for(int j = 0; j < BOARD_SIZE - 1; j++) {

				board[1 + (shift-6)*2][j] = board[1 + (shift-6)*2][j+1];

			}

			for(int index = 0; index < players.length; index++) {

				if(players[index].getX() == 1 + (shift-6)*2) {

					players[index].setY(players[index].getY()-1);

				}
				
				if(players[index].getY() < 0)
					players[index].setY(BOARD_SIZE-1);

			}

			board[1 + (shift-6)*2][BOARD_SIZE-1] = extraPiece;
			
			extraPiece = tempExtraPiece;

		}

		// move movable rows rightwards
		else if(shift >= 9 && shift <= 11) {

			Tile tempExtraPiece = board[BOARD_SIZE - 1][1 + (shift-9)*2];
			
			for(int j = BOARD_SIZE - 1; j > 0; j--) {

				board[j][1 + (shift-9)*2] = board[j-1][1 + (shift-9)*2];

			}
			
			for(int index = 0; index < players.length; index++) {

				if(players[index].getY() == 1 + (shift-9)*2) {

					players[index].setX(players[index].getX()+1);

				}
				
				if(players[index].getX() > BOARD_SIZE-1)
					players[index].setX(0);

			}

			board[0][1 + (shift-9)*2] = extraPiece;

			extraPiece = tempExtraPiece;
		}	
		
	}
	

	private int numItemsCanGet(int shift, int back, int rotate) {
		
		if(rotate == 1) {
			rotateExtraTileClockWise(true);
		} else if(rotate == 2) {
			rotateExtraTileClockWise(true);
			rotateExtraTileClockWise(true);
		} else if(rotate == 3) {
			rotateExtraTileCounterClockWise(true);
		}
		
		shiftTiles(shift);
		clearWalkLines();
		viewPath(players[currentPlayer].getX(), players[currentPlayer].getY(), 0, new LinkedList<String>(), new ArrayList<Point>());
		
		ArrayList<Integer> itemList = new ArrayList<Integer>();
		
		for(int i = 0; i < potentialPathways.size(); i++) {
			
			if(hands[currentPlayer].contains(potentialPathways.get(i).getTile().getItem()-1)
					&& !itemList.contains(potentialPathways.get(i).getTile().getItem()-1)) {
				
				itemList.add(potentialPathways.get(i).getTile().getItem()-1);
				
			}
			
		}
		
		shiftTiles(back);
		//clearWalkLines();
		
		if(rotate == 1) {
			rotateExtraTileCounterClockWise(true);
		} else if(rotate == 2) {
			rotateExtraTileCounterClockWise(true);
			rotateExtraTileCounterClockWise(true);
		} else if(rotate == 3) {
			rotateExtraTileClockWise(true);
		}
		
		return itemList.size();
		
	}
	
	private int findPotentialPathwaySize(int shift, int back, int rotate) {
		
		int maxSize = 0;
		
		if(rotate == 1) {
			rotateExtraTileClockWise(true);
		} else if(rotate == 2) {
			rotateExtraTileClockWise(true);
			rotateExtraTileClockWise(true);
		} else if(rotate == 3) {
			rotateExtraTileCounterClockWise(true);
		}
		
		shiftTiles(shift);
		clearWalkLines();
		viewPath(players[currentPlayer].getX(), players[currentPlayer].getY(), 0, new LinkedList<String>(), new ArrayList<Point>());
		
		ArrayList<Tile> tileList = new ArrayList<Tile>();
		
		for(int i = 0; i < potentialPathways.size(); i++) {
			
			if(!tileList.contains(potentialPathways.get(i).getTile())) {
				
				tileList.add(potentialPathways.get(i).getTile());
				
				if(tileList.size() > maxSize)
					maxSize = tileList.size();
				
			}
			
		}
		
		clearWalkLines();
		shiftTiles(back);
		
		if(rotate == 1) {
			rotateExtraTileCounterClockWise(true);
		} else if(rotate == 2) {
			rotateExtraTileCounterClockWise(true);
			rotateExtraTileCounterClockWise(true);
		} else if(rotate == 3) {
			rotateExtraTileClockWise(true);
		}
		
		return maxSize;
		
	}
	
	private int findPotentialPathwaySize2(int shift, int back, int rotate) {
		
		int maxSize = 0;
		
		if(rotate == 1) {
			rotateExtraTileClockWise(true);
		} else if(rotate == 2) {
			rotateExtraTileClockWise(true);
			rotateExtraTileClockWise(true);
		} else if(rotate == 3) {
			rotateExtraTileCounterClockWise(true);
		}
		
		shiftTiles(shift);
		clearWalkLines();
		viewPath(players[currentPlayer].getX(), players[currentPlayer].getY(), 0, new LinkedList<String>(), new ArrayList<Point>());
		
		ArrayList<Tile> tileList = new ArrayList<Tile>();
		
		for(int i = 0; i < potentialPathways.size(); i++) {
			
			if(!tileList.contains(potentialPathways.get(i).getTile())) {
				
				tileList.add(potentialPathways.get(i).getTile());
				
				if(tileList.size() > maxSize)
					maxSize = tileList.size();
				
			}
			
		}
		
		clearWalkLines();
		
		Tile tempExtraTile = extraPiece;
		
		int maxIndex = 0;
		int maxAmount = 0;
		int rotating = 0;
		
		int[] directItems = new int[12];
		
		for(int i = 0; i < 4; i++) {
			
			directItems[0] = (numItemsCanGet(0, 6, i));
			directItems[1] = (numItemsCanGet(1, 7, i));
			directItems[2] = (numItemsCanGet(2, 8, i));
			directItems[3] = (numItemsCanGet(3, 9, i));
			directItems[4] = (numItemsCanGet(4, 10, i));
			directItems[5] = (numItemsCanGet(5, 11, i));
			directItems[6] = (numItemsCanGet(6, 0, i));
			directItems[7] = (numItemsCanGet(7, 1, i));
			directItems[8] = (numItemsCanGet(8, 2, i));
			directItems[9] = (numItemsCanGet(9, 3, i));
			directItems[10] = (numItemsCanGet(10, 4, i));
			directItems[11] = (numItemsCanGet(11, 5, i));
			
			for(int j = 0; j < directItems.length; j++) {
				
				if(directItems[j] > maxAmount) {
					
					maxIndex = j;
					maxAmount = directItems[j];
					rotating = i;
					
				}
				
			}
			
			Arrays.fill(directItems, 0);
			
		}
		
		extraPiece = tempExtraTile;
		
		if(rotating == 1) {
			rotateExtraTileClockWise(true);
		} else if(rotating == 2) {
			rotateExtraTileClockWise(true);
			rotateExtraTileClockWise(true);
		} else if(rotating == 3) {
			rotateExtraTileCounterClockWise(true);
		}
		
		shiftTiles(back);
		
		if(rotate == 1) {
			rotateExtraTileCounterClockWise(true);
		} else if(rotate == 2) {
			rotateExtraTileCounterClockWise(true);
			rotateExtraTileCounterClockWise(true);
		} else if(rotate == 3) {
			rotateExtraTileClockWise(true);
		}
		
		return maxSize + maxAmount;
		
	}

	// Inherit method from the key listener class
	@Override
	public void keyPressed(KeyEvent key) {

		// key control WASD is used to check the player movement in all 4 directions
		if(key.getKeyCode() == KeyEvent.VK_W) {

			// update the current player position
			updatePosition(0, -1);
			checkCards();
			
		}

		else if(key.getKeyCode() == KeyEvent.VK_S) {

			updatePosition(0, 1);
			checkCards();
			
		}

		else if(key.getKeyCode() == KeyEvent.VK_A) {

			updatePosition(-1, 0);
			checkCards();

		}

		else if(key.getKeyCode() == KeyEvent.VK_D) {

			updatePosition(1, 0);
			checkCards();

		}

		else if(key.getKeyCode() == KeyEvent.VK_ENTER) {

			// when player presses enter, the current turn ends
			endTurn();

		} else if(key.getKeyCode() == KeyEvent.VK_R) {

			// key control <R> is a shortcut for rotating the leftover piece
			rotateExtraTileClockWise(false);

		} 

	}

	// other inherent methods from key listener
	@Override
	public void keyTyped(KeyEvent key) {

	}
	
	// method that detects if a JComponent is being pressed by a mouse
	@Override
	public void mousePressed(MouseEvent event) {
		
		// checks if a available button is being clicked
		for(int index = 0; index < potentialPathways.size(); index++) {
			
			// if one of the potential pathways are clicked, then auto move to that location
			if(event.getSource().equals(potentialPathways.get(index).getLabel())) {
				
				// set the auto move set into the track stored by the current potential pathway
				AIMoveSet = potentialPathways.get(index).getTrack();
				
				// start the move timer for auto move
				autoMoveTimer.start();
				
				// clean any leftover walk lines and generate a new set of potential pathways
				clearWalkLines();
				viewPath(players[currentPlayer].getX(), players[currentPlayer].getY(), 0, new LinkedList<String>(), new ArrayList<Point>());
				
				// exit the method in-case the same method is executed again
				return;
				
			}
			
		}
		
		// if the save text area is clicked, then focus the game to it to allow typing
		if(event.getSource().equals(saveGameName)) {
			
			saveGameName.setFocusable(true);
			
		} 
		
	}
	
	// methods from the mouse listener
	@Override
	public void keyReleased(KeyEvent key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}
	
}


