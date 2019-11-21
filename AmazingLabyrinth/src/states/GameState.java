package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
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
	private JLabel[] CardsImage;
	private JLabel currentTurn;

	private Tile[][] board;
	private Player players[];
	private ArrayList<Integer> mapBits;
	private JLabel extraPieceLabel;
	private JLabel boardBoarder;
	private JLabel saveInstruction;
	private ArrayList<JButton> tileShiftButtons;
	private ArrayList<PathTrackingButton> potentialPathways;
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

	private ArrayList<LinkedList<String>> possiblePath;
	private ArrayList<Player> shiftedPlayers;
	private Queue<String> AIMoveSet;

	private Timer autoMoveTimer;
	private Timer playerShiftTimer;
	private Timer tileShiftTimer;

	private ArrayList<Integer> Hand1;
	private ArrayList<Integer> Hand2;
	private ArrayList<Integer> Hand3;
	private ArrayList<Integer> Hand4;
	
	private ArrayList<Integer> Winner;
	
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
		iconLogo = new ImageIcon("cards/CardBack.jpg");

		tileShiftButtons = new ArrayList<JButton>();

		CardsImage = new JLabel[24];

		//run the initializeCards method in the Deck class
		Deck.initializeCards();
		
		//add the Card values in the Deck to the array list in this class
		cards = Deck.getCards();
		//add the ID values in the Deck to the array list in this class
		CardNumber = Deck.getIDNumber();

		// Initializing others types
		players = new Player[4];
		mapBits = new ArrayList<Integer>();
		possiblePath = new ArrayList<LinkedList<String>>();
		shiftedPlayers = new ArrayList<Player>();
		AIMoveSet = new LinkedList<String>();
		autoMoveTimer = new Timer(300, this);
		playerShiftTimer = new Timer(1, this);
		tileShiftTimer = new Timer(1, this);
		canShift = true;
		canClick = true;
		
		// initializing player's deck
		Hand1 = new ArrayList<Integer>();
		Hand2 = new ArrayList<Integer>();
		Hand3 = new ArrayList<Integer>();
		Hand4 = new ArrayList<Integer>();
		
		//Assign the Hand arrayList base on the player, for example
		//if you are player1 then you will get the first 5 cards on the list
		//and if you are second you will get number 5 to 10 etc....
		for (int i=0; i<5; i++) 
			Hand1.add(CardNumber.get(i));

		for (int i=5; i<10; i++) 
			Hand2.add(CardNumber.get(i));

		for (int i=10; i<15; i++) 
			Hand3.add(CardNumber.get(i));

		for (int i=15; i<20; i++) 
			Hand4.add(CardNumber.get(i));

		// enable key listener for this state
		addKeyListener(this);

		// initializes all the players
		for(int i = 0; i < 4; i++)
			players[i] = new Player(i, false);

		// Method Calls
		fillMapBits();
		addMenuBar();

	}

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
		
		// a for loop that draws all the cards in each player;s deck
		for(int a = 0; a <=19; a++) {

			// draws player 1's deck
			if (a<5){
				
				CardsImage[a] = new JLabel(new ImageIcon(cards.get(a).getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));
				CardsImage[a].setBounds(880+a*70, 325, 60, 90);
				gamePanel.add(CardsImage[a]);
				
			} 
			
			// draws player 2's deck
			else if (a<10){
				
				CardsImage[a] = new JLabel(new ImageIcon(cards.get(a).getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));
				CardsImage[a].setBounds(880+(a-5)*70, 425, 60, 90);
				gamePanel.add(CardsImage[a]);
				
			} 
			
			// draws player 3's deck
			else if (a<15){
				
				CardsImage[a] = new JLabel(new ImageIcon(cards.get(a).getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));
				CardsImage[a].setBounds(880+(a-10)*70, 525, 60, 90);
				gamePanel.add(CardsImage[a]);
				
			} 
			
			// draws player 4's deck
			else if (a<20){
				
				CardsImage[a] = new JLabel(new ImageIcon(cards.get(a).getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));
				CardsImage[a].setBounds(880+(a-15)*70, 625, 60, 90);
				gamePanel.add(CardsImage[a]);
				
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
					
					players[player] = new Player(player, true);
					
				} else {
					
					players[player] = new Player(player, false);
					
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
			potentialPathways.add(new PathTrackingButton(upPath, newPath));

			// draw the path at index 4 on the panel so that the 4 players are drawn on top of it
			gamePanel.add(upPath, 4);

			// if the previous direction is 2(down), then exit the statement to avoid going back and forth
			if(previousDirection != 2) {

				// creating a temporary linked list to be passed to the next recursive call
				// this is used in a Queue for the AI to seek to a location without confusion 
				LinkedList<String> newWalkablePath = new LinkedList<String>(newPath);
				newWalkablePath.add("up");
				possiblePath.add(newWalkablePath);

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

			potentialPathways.add(new PathTrackingButton(downPath, newPath));
			gamePanel.add(downPath, 4);

			if(previousDirection != 1) {

				LinkedList<String> newWalkablePath = new LinkedList<String>(newPath);
				newWalkablePath.add("down");
				possiblePath.add(newWalkablePath);

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

			potentialPathways.add(new PathTrackingButton(leftPath, newPath));
			gamePanel.add(leftPath, 4);

			if(previousDirection != 4) {

				LinkedList<String> newWalkablePath = new LinkedList<String>(newPath);
				newWalkablePath.add("left");
				possiblePath.add(newWalkablePath);

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

			potentialPathways.add(new PathTrackingButton(rightPath, newPath));
			gamePanel.add(rightPath, 4);

			if(previousDirection != 3) {

				LinkedList<String> newWalkablePath = new LinkedList<String>(newPath);
				newWalkablePath.add("right");
				possiblePath.add(newWalkablePath);

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

			autoMoveTimer.start();
			// clear the previous AI path lines
			possiblePath.clear();

		} else {

			// generate new walk lines for the next player
			viewPath(players[currentPlayer].getX(), players[currentPlayer].getY(), 0, new LinkedList<String>(), new ArrayList<Point>());

		}

		// updates the icons of the tile shift buttons
		updateTileShiftButtonIcon();

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
	public void rotateExtraTileClockWise() {

		AudioPlayer.playAudio("audio/rotate.wav");

		// call the rotate clock wise method from within the tile class itself
		extraPiece.rotateTileClockWise();

		// reset the new icon for the extra tile
		extraPieceLabel.setIcon(new ImageIcon(new ImageIcon(extraPiece.getFilePath())
				.getImage().getScaledInstance(92, 92, 0)));

	}
	
	// method that rotates the extra tile counterclockwise
	public void rotateExtraTileCounterClockWise() {

		AudioPlayer.playAudio("audio/rotate.wav");

		extraPiece.rotateTileCounterClockWise();

		extraPieceLabel.setIcon(new ImageIcon(new ImageIcon(extraPiece.getFilePath())
				.getImage().getScaledInstance(92, 92, 0)));

	}
	
	//Check the cards on the board and the card you have on your hand
	public void CheckCards(int x, int y) {

		//the variables for the player's position
		int player0X = players[0].getX();
		int player0Y = players[0].getY();

		int player1X = players[1].getX();
		int player1Y = players[1].getY();

		int player2X = players[2].getX();
		int player2Y = players[2].getY();

		int player3X = players[3].getX();
		int player3Y = players[3].getY();
	
		//A for loop that runs 24 time because there is a total of 24 items 
		for (int i=0; i<24; i++) {

			//All of these methods are similar, the only difference is what player it is. 
			//It first checks if the tile contains an item that the player has in his hands. If it returns true it turns the image of the item into a black car
			//and remove it from the hand list. Depends on the player, the position of the cards will move. 
			if(board[player0X][player0Y].getItem() == CardNumber.get(i)+1) {	
				if(Hand1.contains(CardNumber.get(i))) {
					AudioPlayer.playAudio("audio/cardCollected.wav");
					CardsImage[Hand1.indexOf(CardNumber.get(i))].setIcon(iconLogo);
					Hand1.remove(CardNumber.get(i));
				}
			}
			else if(board[player1X][player1Y].getItem() == CardNumber.get(i)+1) {
				if(Hand2.contains(CardNumber.get(i))) {
					AudioPlayer.playAudio("audio/cardCollected.wav");
					CardsImage[Hand2.indexOf(CardNumber.get(i)) + 5].setIcon(iconLogo);
					Hand2.remove(CardNumber.get(i));
				}
			}
			else if(board[player2X][player2Y].getItem() == CardNumber.get(i)+1) {
				if(Hand3.contains(CardNumber.get(i))) {
					AudioPlayer.playAudio("audio/cardCollected.wav");
					CardsImage[Hand3.indexOf(CardNumber.get(i)) + 10].setIcon(iconLogo);
					Hand3.remove(CardNumber.get(i));
				}
			}
			else if(board[player3X][player3Y].getItem() == CardNumber.get(i)+1) {
				if(Hand4.contains(CardNumber.get(i))) {
					AudioPlayer.playAudio("audio/cardCollected.wav");
					CardsImage[Hand4.indexOf(CardNumber.get(i)) + 15].setIcon(iconLogo);
					Hand4.remove(CardNumber.get(i));
				}
			}

		}
		
		//This checks the winning condition. If any players hand is empty, it means they win and it will show a message and their turn will be force to end. 
		//It also set their isActive to false because they are out of the game and is will ensure they don't get a turn again.
		//The Winner list is an ArrayList that checks the order of winners.
		if (Hand1.isEmpty() == true) {	
			AudioPlayer.playAudio("audio/gameOver.wav");
			JOptionPane.showMessageDialog(null, "Player 1 have finished all their cards!!!");
			players[0].setActive(false);
			Hand1.add(10);
			Winner.add(1);
			endTurn();
			
		} else if (Hand2.isEmpty() == true) {
			AudioPlayer.playAudio("audio/gameOver.wav");
			JOptionPane.showMessageDialog(null, "Player 2 have finished all their cards!!!");
			players[1].setActive(false);
			Hand2.add(10);
			Winner.add(2);
			endTurn();

		} else if (Hand3.isEmpty() == true) {
			AudioPlayer.playAudio("audio/gameOver.wav");
			JOptionPane.showMessageDialog(null, "Player 3 have finished all their cards!!!");
			players[2].setActive(false);
			Hand3.add(10);
			Winner.add(3);
			endTurn();

		} else if (Hand4.isEmpty() == true) {
			AudioPlayer.playAudio("audio/gameOver.wav");
			JOptionPane.showMessageDialog(null, "Player 4 have finished all their cards!!!");
			players[3].setActive(false);
			Hand4.add(10);
			Winner.add(4);
			endTurn();

		}
		
		//When their is 3 winner, that means that game is completely finished and it will stops and create a new state
		if (Winner.size() ==3) {
			
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
			System.out.println(Winner);
			this.dispose();
			//opens the last frame
			new EndState(Winner);
		}
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

			// shift the tiles and if it is an AI and never shifted tiles before in the current turn
			if(canShift && players[currentPlayer].isAI()) {

				shiftID = (int)(Math.random()*12);

				shiftButtonClick();

				return;

			}

			// if the auto move set is not empty, then move the player to its next direction in the move set
			if(!AIMoveSet.isEmpty()) {

				// take the first element from the move set to use it to move the player
				String nextMove = AIMoveSet.poll();

				// update the player's position from the move set operation - up, down, left, right
				if(nextMove.equals("up")) {

					updatePosition(0, -1);

				} else if(nextMove.equals("down")) {

					updatePosition(0, 1);

				} else if(nextMove.equals("left")) {

					updatePosition(-1, 0);

				} else if(nextMove.equals("right")) {

					updatePosition(1, 0);

				}

			} else {

				// if player is an AI and the move set is empty, then end the current turn
				if(players[currentPlayer].isAI()) {
					
					autoMoveTimer.stop();
					endTurn();

				} else {
					
					// stop the moving after move set is complete and current player is not an AI
					autoMoveTimer.stop();
					
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

			rotateExtraTileClockWise();
			
		} else if(event.getSource().equals(rotateCounterClockWise)) {

			rotateExtraTileCounterClockWise();
			
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
					
				} catch(FileNotFoundException error) {
					
					System.out.println("save file not found");
					
				}
				
				saveGameName.setText("");
				
			}
			
		}

		for(int button = 0; button < tileShiftButtons.size(); button++) {

			if(canShift && event.getSource().equals(tileShiftButtons.get(button))) {

				shiftID = button;

				shiftButtonClick();

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

	// method that executes the actions of the shift buttons
	private void shiftButtonClick() {

		// move the movable columns downwards
		if(shiftID >= 0 && shiftID <= 2) {

			// create a temporary extra piece tile at the last tile of the shift
			Tile tempExtraPiece = board[1 + shiftID*2][BOARD_SIZE-1];

			// shift each tile to the adjacent tiles
			for(int j = BOARD_SIZE - 1; j > 0; j--) {

				board[1 + shiftID*2][j] = board[1 + shiftID*2][j-1];

				boardIcons[1 + shiftID*2][j].setIcon(new ImageIcon(new ImageIcon(board[1 + shiftID*2][j].getFilePath())
						.getImage().getScaledInstance(92, 92, 0)));

			}

			// set the extra piece label to the button location for it to animate sliding in
			extraPieceLabel.setBounds(boardIcons[1 + shiftID*2][0].getX(), 
					boardIcons[1 + shiftID*2][0].getY() - tileIconSize, tileIconSize, tileIconSize);

			// checks if any players are on the tile, if there are, then shift them also by calling shiftPlayer method
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

		// to start the animation, the amount of pixels the tile have to move is exactly the tile size
		tileMoveAmount = tileIconSize;

		// there can be only one shift every turn, disable tile shifting
		canShift = false;

		// clear potential pathways so new ones can be generated
		clearWalkLines();

		// generate new potential pathways
		viewPath(players[currentPlayer].getX(), players[currentPlayer].getY(), 0, new LinkedList<String>(), new ArrayList<Point>());

		// if the current player is an AI, then generate a move set for it to move
		if(players[currentPlayer].isAI()) {

			// checks if there is a path available for the AI to move
			if(!possiblePath.isEmpty()) {

				// update the move set to a random one within the possible path
				int AIMoveSetID = (int)(Math.random()*possiblePath.size());

				for(String direction: possiblePath.get(AIMoveSetID)) {

					AIMoveSet.add(direction);

				}

				// start the move timer for the AI to auto move
				autoMoveTimer.start();

			}

		}

		// clean the possible pathways so that next time it can be empty to begin with
		possiblePath.clear();

		// update the shift button icons to the invalid icon
		updateTileShiftButtonIcon();

		// the player on the tile's move amount will also be the size of a tile
		playerMoveAmount = tileIconSize;
		
		// start the timer for the tiles to shift in animation
		tileShiftTimer.start();

		AudioPlayer.playAudio("audio/buttonSlide.wav");


	}

	// Inherit method from the key listener class
	@Override
	public void keyPressed(KeyEvent key) {

		// key control WASD is used to check the player movement in all 4 directions
		if(key.getKeyCode() == KeyEvent.VK_W) {

			// update the current player position
			updatePosition(0, -1);
			
			// check if the tile player walks on is a card in their deck
			CheckCards(0, -1);

		}

		else if(key.getKeyCode() == KeyEvent.VK_S) {

			updatePosition(0, 1);
			CheckCards(0, 1);

		}

		else if(key.getKeyCode() == KeyEvent.VK_A) {

			updatePosition(-1, 0);
			CheckCards(-1, 0);

		}

		else if(key.getKeyCode() == KeyEvent.VK_D) {

			updatePosition(1, 0);
			CheckCards(1, 0);

		}

		else if(key.getKeyCode() == KeyEvent.VK_ENTER) {

			// when player presses enter, the current turn ends
			endTurn();

		} else if(key.getKeyCode() == KeyEvent.VK_R) {

			// key control <R> is a shortcut for rotating the leftover piece
			rotateExtraTileClockWise();

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
				possiblePath.clear();
				
				// exit the method in-case the same method is executed again
				return;
				
			}
			
		}
		
		// if the save text area is clicked, then focus the game to it to allow typing
		if(event.getSource().equals(saveGameName)) {
			
			saveGameName.setFocusable(true);
			
		} 
		
	}
	
	// over inheriented methods from the mouse listener
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

}


