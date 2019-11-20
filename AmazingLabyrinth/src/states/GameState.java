package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
public class GameState extends State implements KeyListener, Mover {

	// final variables
	private static final int BOARD_SIZE = 7;
	
	private Deck cardObeject = new Deck();
	private ArrayList<ImageIcon> cards;
	private ArrayList<Integer> CardNumber;

	ImageIcon iconLogo = new ImageIcon("cards/CardBack.jpg");

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
	private JButton rotatePieceButton;
	private ArrayList<JButton> tileShiftButtons;
	private ArrayList<PathTrackingButton> potentialPathways;
	
	private JLabel Player1Label;
	private JLabel Player2Label;
	private JLabel Player3Label;
	private JLabel Player4Label;
	
	private Tile extraPiece;
	private int currentPlayer;
	private int playerMoveAmount;
	private int tileMoveAmount;
	private int shiftID;
	private boolean canShift, canClick;
	private String playerMoveDirection;

	private ArrayList<LinkedList<String>> possiblePath;
	private ArrayList<Player> shiftedPlayers;
	private Queue<String> AIMoveSet;
	
	private Timer autoMoveTimer;
	private Timer playerShiftTimer;
	private Timer tileShiftTimer;

	@Override
	public void init() {
		
		// update background music
		MusicPlayer.stopMusic();
		playGameBackground();
		
		// Initializing constants
		currentPlayer = 0;
		
		// Initializing JComponents
		board = new Tile[BOARD_SIZE][BOARD_SIZE];
		boardIcons = new JLabel[BOARD_SIZE][BOARD_SIZE];
		playerIcons = new JLabel[4];
		extraPieceLabel = new JLabel(new ImageIcon(""));
		potentialPathways = new ArrayList<PathTrackingButton>();

		tileShiftButtons = new ArrayList<JButton>();

		CardsImage = new JLabel[24];

		cardObeject.initializeCards();
		cards = cardObeject.getCards();
		CardNumber = cardObeject.getIDNumber();
		
		
		// Initializing others types
		players = new Player[4];
		mapBits = new ArrayList<Integer>();
		canShift = true;
		possiblePath = new ArrayList<LinkedList<String>>();
		shiftedPlayers = new ArrayList<Player>();
		AIMoveSet = new LinkedList<String>();
				
		autoMoveTimer = new Timer(300, this);
		playerShiftTimer = new Timer(1, this);
		tileShiftTimer = new Timer(1, this);
		
		canClick = true;

		addKeyListener(this);

		for(int i = 0; i < 4; i++)
			players[i] = new Player(i, false);

		// Method Calls
		fillMapBits();
		
	}

	@Override
	public void addJComponents() {
		
		addMenuBar();
		
		// create a new panel to put the JComponents on top
		gamePanel = new JPanel(null);

		// panel settings, disable auto layout, set bounds and background
		gamePanel.setLayout(null);
		gamePanel.setBounds(scaledOrginX, scaledOrginY, ScreenWidth, ScreenHeight);
		gamePanel.setBackground(Color.black);
		
		// add panel to the frame
		add(gamePanel);
		
		System.out.println(cards);
		
		for(int a = 0; a <=19; a++) {

			if (a<5){
				CardsImage[a] = new JLabel(new ImageIcon(cards.get(a).getImage().getScaledInstance(92, 92, 0)));
				CardsImage[a].setBounds(850+a*70, 300, 60, 90);
				gamePanel.add(CardsImage[a]);
			}
			else{

				CardsImage[a] = new JLabel(new ImageIcon(cards.get(a).getImage().getScaledInstance(92, 92, 0)));
				gamePanel.add(CardsImage[a]);			

			}

		}

		CardsImage[5].setBounds(850, 400, 60, 90);
		CardsImage[6].setBounds(920, 400, 60, 90);
		CardsImage[7].setBounds(990, 400, 60, 90);
		CardsImage[8].setBounds(1060, 400, 60, 90);
		CardsImage[9].setBounds(1130, 400, 60, 90);

		CardsImage[10].setBounds(850, 500, 60, 90);
		CardsImage[11].setBounds(920, 500, 60, 90);
		CardsImage[12].setBounds(990, 500, 60, 90);
		CardsImage[13].setBounds(1060, 500, 60, 90);
		CardsImage[14].setBounds(1130, 500, 60, 90);

		CardsImage[15].setBounds(850, 600, 60, 90);
		CardsImage[16].setBounds(920, 600, 60, 90);
		CardsImage[17].setBounds(990, 600, 60, 90);
		CardsImage[18].setBounds(1060, 600, 60, 90);
		CardsImage[19].setBounds(1130, 600, 60, 90);
		
		// label created to display the current player's turn
		currentTurn = new JLabel("Current Turn: Player " + (currentPlayer + 1));
		currentTurn.setBounds(825, 50, 500, 100);
		currentTurn.setForeground(Color.red);
		currentTurn.setFont(new Font("TimesRoman", Font.BOLD, 36));
		gamePanel.add(currentTurn);

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
		
		// creating the label to display the extra piece
		extraPieceLabel = new JLabel(new ImageIcon(new ImageIcon(extraPiece.getFilePath())
				.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));

		extraPieceLabel.setBounds(850, 200, tileIconSize, tileIconSize);
		
		gamePanel.add(extraPieceLabel);
		
		// creating a button that rotates the extra tile,
		rotatePieceButton = new JButton("rotate tile");
		
		rotatePieceButton.setBounds(1050, 200, 200, 50);
		
		// enable button action and disable auto focus
		rotatePieceButton.addActionListener(this);
		rotatePieceButton.setFocusable(false);
		
		gamePanel.add(rotatePieceButton);
		
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
		
		// add the board boarder to the panel
		boardBoarder = new JLabel(new ImageIcon(new ImageIcon("images/boardBoarder.png")
				.getImage().getScaledInstance(tileIconSize*9, tileIconSize*9, 0)));
		boardBoarder.setBounds(scaledOrginX, scaledOrginY, 9*tileIconSize, 9*tileIconSize);
		gamePanel.add(boardBoarder);
		
		// displaing a series of player icons for their deck
		Player1Label = new JLabel(new ImageIcon("images/player1.png"));
		Player2Label = new JLabel(new ImageIcon("images/player2.png"));
		Player3Label = new JLabel(new ImageIcon("images/player3.png"));
		Player4Label = new JLabel(new ImageIcon("images/player4.png"));

		Player1Label.setBounds(700, 350, 200, 100);
		Player2Label.setBounds(700, 450, 200, 100);
		Player3Label.setBounds(700, 550, 200, 100);
		Player4Label.setBounds(700, 650, 200, 100);

		gamePanel.add(Player1Label);
		gamePanel.add(Player2Label);
		gamePanel.add(Player3Label);
		gamePanel.add(Player4Label);
		
		// generate the walkable paths
		viewPath(players[currentPlayer].getX(), players[currentPlayer].getY(), 0, new LinkedList<String>(), new ArrayList<Point>());
		
	}
	
	private void updateTileShiftButtonIcon() {
		
		if(!canShift) {
			
			for(int i = 0; i < tileShiftButtons.size(); i++) {
				
				tileShiftButtons.get(i).setIcon(new ImageIcon(new ImageIcon("images/invalid.png")
						.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));
				
			}
			
		} else {
		
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
			JButton upPath = new JButton(new ImageIcon(new ImageIcon("images/pathUp.png")
					.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));

			upPath.setBorderPainted(false);
			upPath.setFocusable(false);
			upPath.addActionListener(this);
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
			
			JButton downPath = new JButton(new ImageIcon(new ImageIcon("images/pathDown.png")
					.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));

			downPath.setBorderPainted(false);
			downPath.setFocusable(false);
			downPath.addActionListener(this);
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
			
			JButton leftPath = new JButton(new ImageIcon(new ImageIcon("images/pathLeft.png")
					.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));

			leftPath.setBorderPainted(false);
			leftPath.setFocusable(false);
			leftPath.addActionListener(this);
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
			
			JButton rightPath = new JButton(new ImageIcon(new ImageIcon("images/pathRight.png")
					.getImage().getScaledInstance(tileIconSize, tileIconSize, 0)));

			rightPath.setBorderPainted(false);
			rightPath.setFocusable(false);
			rightPath.addActionListener(this);
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
			
			gamePanel.remove(path.getButton());
			
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
		
		// if the current player id is 3, go back to player 0, else it increments
		if(currentPlayer == 3) {

			currentPlayer = 0;

		} else {

			currentPlayer++;

		}

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
				
		updateTileShiftButtonIcon();
		
	}
	
	@Override
	public void updatePosition(int x, int y) {

		if(!canClick) {
			
			return;
			
		}
		
		int moveX = players[currentPlayer].getX() + x;
		int moveY = players[currentPlayer].getY() + y;
		
		if(x != 0 && moveX < BOARD_SIZE && moveX >= 0 && 
				movable(players[currentPlayer].getX(), players[currentPlayer].getY(), x, y)) {

			players[currentPlayer].setX(moveX);
			
			if(x > 0)
				playerMoveDirection = "right";
			else
				playerMoveDirection = "left";
			
			playerMoveAmount = tileIconSize;
			playerShiftTimer.start();

			AudioPlayer.playAudio("audio/move.wav");
			
		}

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
	
	public void rotateExtraTile() {

		AudioPlayer.playAudio("audio/rotate.wav");
		
		extraPiece.rotateTile();
		
		extraPieceLabel.setIcon(new ImageIcon(new ImageIcon(extraPiece.getFilePath())
				.getImage().getScaledInstance(92, 92, 0)));
		
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		
		for(int index = 0; index < potentialPathways.size(); index++) {
			
			if(event.getSource() == potentialPathways.get(index).getButton()) {
				
				AIMoveSet = potentialPathways.get(index).getTrack();
				autoMoveTimer.start();
				
				clearWalkLines();
				viewPath(players[currentPlayer].getX(), players[currentPlayer].getY(), 0, new LinkedList<String>(), new ArrayList<Point>());
				possiblePath.clear();
				
			}
			
		}
		
		if (event.getSource() == autoMoveTimer) {
			
			if(!canClick) {
				
				return;
				
			}
			
			if(canShift && players[currentPlayer].isAI()) {
					
				shiftID = (int)(Math.random()*12);
				
				shiftButtonClick();
				
				return;
				
			}
			
			if(!AIMoveSet.isEmpty()) {
				
				String nextMove = AIMoveSet.poll();
				
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
				
				if(players[currentPlayer].isAI()) {
					
					endTurn();
					
				}
				
			}
			
		} else if (event.getSource() == playerShiftTimer) {
			
			canClick = false;
			
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
			
			playerMoveAmount -= 2;
			
			playerIcons[currentPlayer].repaint();
			
			if(playerMoveAmount == 0) {
				
				playerShiftTimer.stop();
				
				canClick = true;
				
			}
			
		} else if(event.getSource() == tileShiftTimer) {
			
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
				
				extraPieceLabel.setBounds(850, 200, tileIconSize, tileIconSize);
				extraPieceLabel.repaint();
				
				canClick = true;
				
			}
			
		} else if(event.getSource().equals(rotatePieceButton)) {
			
			rotateExtraTile();
			
		} 
		
		for(int button = 0; button < tileShiftButtons.size(); button++) {
			
			if(canShift && event.getSource().equals(tileShiftButtons.get(button))) {
				
				shiftID = button;
				
				shiftButtonClick();
				
			}
			
		}

	}
	
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
	
	private void shiftButtonClick() {
		
		// move the movable columns downwards
		if(shiftID >= 0 && shiftID <= 2) {
			
			Tile tempExtraPiece = board[1 + shiftID*2][BOARD_SIZE-1];
			
			for(int j = BOARD_SIZE - 1; j > 0; j--) {
				
				board[1 + shiftID*2][j] = board[1 + shiftID*2][j-1];
				
				boardIcons[1 + shiftID*2][j].setIcon(new ImageIcon(new ImageIcon(board[1 + shiftID*2][j].getFilePath())
						.getImage().getScaledInstance(92, 92, 0)));
				
			}
			
			extraPieceLabel.setBounds(boardIcons[1 + shiftID*2][0].getX(), 
					boardIcons[1 + shiftID*2][0].getY() - tileIconSize, tileIconSize, tileIconSize);
			
			for(int index = 0; index < players.length; index++) {
				
				if(players[index].getX() == 1 + shiftID*2) {
					
					shiftPlayer(players[index], index, 1);
					
				}
				
			}
			
			board[1 + shiftID*2][0] = extraPiece;
			
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

		tileMoveAmount = tileIconSize;
		
		canShift = false;
		
		clearWalkLines();

		viewPath(players[currentPlayer].getX(), players[currentPlayer].getY(), 0, new LinkedList<String>(), new ArrayList<Point>());
		
		if(players[currentPlayer].isAI()) {
			
			if(!possiblePath.isEmpty()) {
				
				int AIMoveSetID = (int)(Math.random()*possiblePath.size());
	
				for(String direction: possiblePath.get(AIMoveSetID)) {
				
					AIMoveSet.add(direction);
					
				}
				
				autoMoveTimer.start();
				
			}
			
		}
		
		possiblePath.clear();
		
		updateTileShiftButtonIcon();
		
		playerMoveAmount = tileIconSize;
		tileShiftTimer.start();
		
		AudioPlayer.playAudio("audio/buttonSlide.wav");
		
		
	}
	
	private void shiftPlayer(Player player, int playerID, int direction) {
		
		shiftedPlayers.add(player);
		
		if(direction == 1) {
			
			playerMoveDirection = "down";
			
			player.setY(player.getY() + 1);
			
			if(player.getY() >= BOARD_SIZE) {
				
				player.setY(0);
				
			} 
			
		} else if(direction == 2) {
			
			playerMoveDirection = "left";
			
			player.setX(player.getX() - 1);
			
			if(player.getX() < 0) {
				
				player.setX(BOARD_SIZE-1);
				
			} 
			
		} else if(direction == 3) {
			
			playerMoveDirection = "up";
			
			player.setY(player.getY() - 1);
			
			if(player.getY() < 0) {
				
				player.setY(BOARD_SIZE-1);
				
			} 
			
		} else if(direction == 4) {
			
			playerMoveDirection = "right";
			
			player.setX(player.getX() + 1);
			
			if(player.getX() >= BOARD_SIZE) {
				
				player.setX(0);
				
			} 
			
		}

	}

	@Override
	public void keyTyped(KeyEvent key) {

	}

	@Override
	public void keyPressed(KeyEvent key) {

		if(key.getKeyCode() == KeyEvent.VK_W) {

			updatePosition(0, -1);
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
			
			endTurn();
			
		} else if(key.getKeyCode() == KeyEvent.VK_R) {
			
			rotateExtraTile();
		
		}
		
	}

	public void CheckCards(int x, int y) {
			
		int player0X = players[0].getX();
		int player0Y = players[0].getY();

		int player1X = players[1].getX();
		int player1Y = players[1].getY();

		int player2X = players[2].getX();
		int player2Y = players[2].getY();

		int player3X = players[3].getX();
		int player3Y = players[3].getY();
		
		ArrayList<Integer> Hand1 = new ArrayList<Integer>();
		ArrayList<Integer> Hand2 = new ArrayList<Integer>();
		ArrayList<Integer> Hand3 = new ArrayList<Integer>();
		ArrayList<Integer> Hand4 = new ArrayList<Integer>();
			
		for (int i=0; i<5; i++) 
			Hand1.add(CardNumber.get(i));

		for (int i=5; i<10; i++) 
			Hand2.add(CardNumber.get(i));
			
		for (int i=10; i<15; i++) 
			Hand3.add(CardNumber.get(i));
			
		for (int i=15; i<20; i++) 
			Hand4.add(CardNumber.get(i));
		
		for (int i=0; i<24; i++) {
			
			if(board[player0X][player0Y].getItem() == CardNumber.get(i)+1) {	
				if(Hand1.contains(CardNumber.get(i))) {
					CardsImage[Hand1.indexOf(CardNumber.get(i))].setIcon(iconLogo);
					Hand1.remove(CardNumber.get(i));
				}
			}
			else if(board[player1X][player1Y].getItem() == CardNumber.get(i)+1) {
				if(Hand2.contains(CardNumber.get(i))) {
					CardsImage[Hand2.indexOf(CardNumber.get(i)) + 5].setIcon(iconLogo);
					Hand2.remove(CardNumber.get(i));
				}
			}
			else if(board[player2X][player2Y].getItem() == CardNumber.get(i)+1) {
				if(Hand3.contains(CardNumber.get(i))) {
					CardsImage[Hand3.indexOf(CardNumber.get(i)) + 10].setIcon(iconLogo);
					Hand3.remove(CardNumber.get(i));
				}
			}
			else if(board[player3X][player3Y].getItem() == CardNumber.get(i)+1) {
				if(Hand4.contains(CardNumber.get(i))) {
					CardsImage[Hand4.indexOf(CardNumber.get(i)) + 15].setIcon(iconLogo);
					Hand4.remove(CardNumber.get(i));
				}
			}
		}
		
		if (Hand1.isEmpty() == true) {
			
		} else if (Hand2.isEmpty() == true) {
			
		} else if (Hand3.isEmpty() == true) {
			
		} else if (Hand4.isEmpty() == true) {
			
		}
		
		
	}

	@Override
	public void keyReleased(KeyEvent key) {


	}

}

