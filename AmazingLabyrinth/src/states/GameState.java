package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import objects.Deck;
import objects.Mover;
import objects.Player;
import objects.Tile;

public class GameState extends State implements KeyListener, Mover {

	Deck cardObeject = new Deck();
	private ArrayList<ImageIcon> cards;

	private JPanel menuPanel;
	private JLabel boardLabel;

	private JLabel[][] boardIcons;
	private JLabel[] playerIcons;
	private JLabel[] CardsImage;
	private JLabel currentTurn;

	private Tile[][] board;
	private Player players[];
	private ArrayList<Integer> mapBits;
	private JLabel extraPieceLabel;
	private JButton rotatePieceButton;

	private JLabel Player1Label;
	private JLabel Player2Label;
	private JLabel Player3Label;
	private JLabel Player4Label;


	private Tile extraPiece;
	private int currentPlayer;

	@Override
	public void init() {

		// Initializing constants
		currentPlayer = 0;

		// Initializing JComponents
		board = new Tile[7][7];
		boardIcons = new JLabel[7][7];
		playerIcons = new JLabel[4];
		extraPieceLabel = new JLabel(new ImageIcon(""));

		CardsImage = new JLabel[10];
		cardObeject.initializeCards();
		cards = cardObeject.getCards();


		// Initializing others types
		players = new Player[4];
		mapBits = new ArrayList<Integer>();

		addKeyListener(this);

		for(int i = 0; i < 4; i++)
			players[i] = new Player(i);

		// Method Calls
		fillMapBits();

	}


	@Override
	public void addJComponents() {

		menuPanel = new JPanel(null);

		boardLabel = new JLabel(new ImageIcon(new ImageIcon("images/blogamazeingboard.jpg")
				.getImage().getScaledInstance(700, 700, 0)));
		boardLabel.setBounds(50, 50, 700, 700);

		// settings for the score panel and add it to the frame
		menuPanel.setLayout(null);
		menuPanel.setBounds(0, 0, State.ScreenWidth, State.ScreenHeight);
		menuPanel.setBackground(Color.black);
		add(menuPanel);

		for(int a = 0; a < 5; a++) {

			CardsImage[a] = new JLabel(cards.get(a));
			CardsImage[a].setBounds(800+a*70, 350, 200, 100);
			menuPanel.add(CardsImage[a]);

			for(int i = 5; i < 10; i++) {

				CardsImage[i] = new JLabel(cards.get(i));
				CardsImage[i].setBounds(800+a*70, 450, 200, 100);
				menuPanel.add(CardsImage[i]);

			}

		}


		for(int i = 0; i < playerIcons.length; i++) {

			playerIcons[i] = new JLabel(new ImageIcon(players[i].getImage()
					.getImage().getScaledInstance((840) / 9, (840) / 9, 0)));

			playerIcons[i].setBounds(75 + playerIcons[i].getIcon().getIconWidth()*players[i].getX(), 75 + 
					playerIcons[i].getIcon().getIconHeight()*players[i].getY(), 
					playerIcons[i].getIcon().getIconWidth(),
					playerIcons[i].getIcon().getIconHeight());

			menuPanel.add(playerIcons[i]);

		}

		currentTurn = new JLabel("Current Turn: Player " + (currentPlayer + 1));
		currentTurn.setBounds(825, 50, 500, 100);
		currentTurn.setForeground(Color.red);
		currentTurn.setFont(new Font("TimesRoman", Font.BOLD, 36));
		menuPanel.add(currentTurn);

		for(int i = 0; i < boardIcons.length; i++) {
			for(int j = 0; j < boardIcons[i].length; j++) {

				if(board[i][j].isCanMove()) {
					String path = board[i][j].getFilePath();

					boardIcons[i][j] = new JLabel(new ImageIcon(new ImageIcon(path)
							.getImage().getScaledInstance(92, 92, 0)));

					boardIcons[i][j].setBounds(76 + boardIcons[i][j].getIcon().getIconWidth()*i, 80 + 
							boardIcons[i][j].getIcon().getIconHeight()*j, 
							boardIcons[i][j].getIcon().getIconWidth(),
							boardIcons[i][j].getIcon().getIconHeight());

					menuPanel.add(boardIcons[i][j]);

				}

			}
		}

		extraPieceLabel = new JLabel(new ImageIcon(new ImageIcon(extraPiece.getFilePath())
				.getImage().getScaledInstance(92, 92, 0)));

		extraPieceLabel.setBounds(850, 200, extraPieceLabel.getIcon().getIconWidth(), extraPieceLabel.getIcon().getIconHeight());

		menuPanel.add(extraPieceLabel);


		rotatePieceButton = new JButton("rotate tile");	
		rotatePieceButton.setBounds(1050, 200, 200, 50);
		rotatePieceButton.addActionListener(this);
		menuPanel.add(rotatePieceButton);

		rotatePieceButton.setFocusable(false);

		// places the JComponents to the panel
		menuPanel.add(boardLabel);

		Player1Label = new JLabel(new ImageIcon("images/player1.png"));
		Player2Label = new JLabel(new ImageIcon("images/player2.png"));
		Player3Label = new JLabel(new ImageIcon("images/player3.png"));
		Player4Label = new JLabel(new ImageIcon("images/player4.png"));

		Player1Label.setBounds(700, 350, 200, 100);
		Player2Label.setBounds(700, 450, 200, 100);
		Player3Label.setBounds(700, 550, 200, 100);
		Player4Label.setBounds(700, 650, 200, 100);

		menuPanel.add(Player1Label);
		menuPanel.add(Player2Label);
		menuPanel.add(Player3Label);
		menuPanel.add(Player4Label);

	}

	/*
	 * method that fills the board tiles with integer IDs
	 * 
	 * up down - 1
	 * left right - 2
	 * up right - 4
	 * up left - 5
	 * left down - 6
	 * right down - 7
	 * up down right - 8
	 * up down left - 9
	 * left right up - 10
	 * left right down - 11
	 * up down left right - 12
	 * 
	 */
	private void fillMapBits() {

		// generating fixed tiles

		board[0][0] = new Tile(3, 0, false);
		board[0][6] = new Tile(2, 0, false);
		board[6][6] = new Tile(5, 0, false);
		board[6][0] = new Tile(4, 0, false);

		board[0][2] = new Tile(9, 1, false);
		board[0][4] = new Tile(9, 2, false);

		board[2][0] = new Tile(6, 3, false);
		board[2][2] = new Tile(9, 4, false);
		board[2][4] = new Tile(8, 5, false);
		board[2][6] = new Tile(8, 6, false);

		board[4][0] = new Tile(6, 7, false);
		board[4][2] = new Tile(6, 8, false);
		board[4][4] = new Tile(7, 9, false);
		board[4][6] = new Tile(8, 10, false);

		board[6][2] = new Tile(7, 11, false);
		board[6][4] = new Tile(7, 12, false);

		ArrayList<Tile> avaliableTiles = new ArrayList<Tile>();

		for(int count = 0; count < 12; count++) {

			avaliableTiles.add(new Tile((int)(Math.random()*2), 0, true));

		}

		for(int count = 0; count < 10; count++) {

			avaliableTiles.add(new Tile((int)(Math.random()*4) + 2 , 0, true));

		}

		for(int count = 13; count <= 18; count++) {

			avaliableTiles.add(new Tile((int)(Math.random()*4) + 2 , count, true));

		}

		for(int count = 19; count <= 24; count++) {

			avaliableTiles.add(new Tile((int)(Math.random()*4) + 6 , count, true));

		}

		Collections.shuffle(avaliableTiles);

		int index = 0;

		// generating random tiles

		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {

				if(board[i][j] == null) {

					board[i][j] = avaliableTiles.get(index);
					index++;

				}

			}
		}

		extraPiece = avaliableTiles.get(avaliableTiles.size()-1);

	}

	@Override
	public void updatePosition(int x, int y) {

		int moveX = players[currentPlayer].getX() + x;
		int moveY = players[currentPlayer].getY() + y;

		if(x != 0 && moveX < board.length && moveX >= 0 && movable(x, y, moveX, moveY)) {

			players[currentPlayer].setX(moveX);

		}

		if(y != 0 && moveY < board.length && moveY >= 0 && movable(x, y, moveX, moveY)) {

			players[currentPlayer].setY(moveY);

		}

		playerIcons[currentPlayer].setBounds(75 + playerIcons[currentPlayer].getIcon().getIconWidth()*
				players[currentPlayer].getX(), 75 + playerIcons[currentPlayer].getIcon().getIconHeight()*players[currentPlayer].getY(), 
				playerIcons[currentPlayer].getIcon().getIconWidth(), playerIcons[currentPlayer].getIcon().getIconHeight());

	}

	@Override
	public boolean movable(int x, int y, int moveX, int moveY) {

		Tile currentTile = board[players[currentPlayer].getX()][players[currentPlayer].getY()];

		// moving down, top block must have down connection
		if(y > 0 && currentTile.isDown()) {

			return board[moveX][moveY].isUp();

		} 

		// moving up, top block must have up connection
		else if(y < 0 && currentTile.isUp()) {

			return board[moveX][moveY].isDown();

		}

		// moving right, right block must have left connection
		if(x > 0 && currentTile.isRight()) {

			return board[moveX][moveY].isLeft();

		}

		// moving left, left block must have right connection
		else if(x < 0 && currentTile.isLeft()) {

			return board[moveX][moveY].isRight();

		}

		return false;

	}

	public void rotatePiece() {

		extraPiece.rotateTile();

	}

	@Override
	public void actionPerformed(ActionEvent event) {

		if(event.getSource().equals(rotatePieceButton)) {

			rotatePiece();

			extraPieceLabel.setIcon(new ImageIcon(new ImageIcon(extraPiece.getFilePath())
					.getImage().getScaledInstance(92, 92, 0)));

		}

	}

	@Override
	public void keyTyped(KeyEvent key) {


	}

	@Override
	public void keyPressed(KeyEvent key) {



		if(key.getKeyCode() == KeyEvent.VK_W) {

			updatePosition(0, -1);


		}

		else if(key.getKeyCode() == KeyEvent.VK_S) {

			updatePosition(0, 1);

		}

		else if(key.getKeyCode() == KeyEvent.VK_A) {

			updatePosition(-1, 0);

		}

		else if(key.getKeyCode() == KeyEvent.VK_D) {

			updatePosition(1, 0);

		}

		else if(key.getKeyCode() == KeyEvent.VK_ENTER) {

			System.out.println("Hello");

			// default player color
			Color playerColor = Color.red; 

			if(currentPlayer == 3) {

				currentPlayer = 0;

			} else {

				currentPlayer++;

				if(currentPlayer == 1) {

					playerColor = Color.blue;

				} else if(currentPlayer == 2) {

					playerColor = Color.YELLOW;

				} else if(currentPlayer == 3) {

					playerColor = Color.green;

				}

			}

			currentTurn.setText("Current Turn: Player " + (currentPlayer + 1));
			currentTurn.setForeground(playerColor);

		}
		else if(key.getKeyCode() == KeyEvent.VK_I) {

			fillMapBits();
		}

	}

	@Override
	public void keyReleased(KeyEvent key) {


	}

}
