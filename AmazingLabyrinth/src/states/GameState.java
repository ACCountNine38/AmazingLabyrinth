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
	private ArrayList<JButton> tileButtons;
	
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
		tileButtons = new ArrayList<JButton>();
		
		CardsImage = new JLabel[24];
		
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
		// settings for the score panel and add it to the frame
		menuPanel.setLayout(null);
		menuPanel.setBounds(0, 0, State.ScreenWidth, State.ScreenHeight);
		menuPanel.setBackground(Color.black);
		add(menuPanel);
		
		boardLabel = new JLabel(new ImageIcon(new ImageIcon("images/blogamazeingboard.jpg")
				.getImage().getScaledInstance(700, 700, 0)));
		boardLabel.setBounds(50, 50, 700, 700);
		
		for(int i = 0; i < playerIcons.length; i++) {
			
			playerIcons[i] = new JLabel(new ImageIcon(players[i].getImage()
					.getImage().getScaledInstance((840) / 9, (840) / 9, 0)));
			
			playerIcons[i].setBounds(75 + playerIcons[i].getIcon().getIconWidth()*players[i].getX(), 75 + 
					playerIcons[i].getIcon().getIconHeight()*players[i].getY(), 
					playerIcons[i].getIcon().getIconWidth(),
					playerIcons[i].getIcon().getIconHeight());
			
			menuPanel.add(playerIcons[i]);
			
		}
		
		Collections.shuffle(cards);

		for(int a = 0; a <=19; a++) {
			
			if (a<5){
				CardsImage[a] = new JLabel(new ImageIcon(cards.get(a).getImage().getScaledInstance(92, 92, 0)));
				CardsImage[a].setBounds(850+a*70, 300, 60, 100);
				menuPanel.add(CardsImage[a]);
			}
			else{
					
				CardsImage[a] = new JLabel(new ImageIcon(cards.get(a).getImage().getScaledInstance(92, 92, 0)));
				menuPanel.add(CardsImage[a]);			
				
			}

		}
		
		CardsImage[5].setBounds(850, 400, 60, 100);
		CardsImage[6].setBounds(920, 400, 60, 100);
		CardsImage[7].setBounds(990, 400, 60, 100);
		CardsImage[8].setBounds(1060, 400, 60, 100);
		CardsImage[9].setBounds(1130, 400, 60, 100);
		
		CardsImage[10].setBounds(850, 500, 60, 100);
		CardsImage[11].setBounds(920, 500, 60, 100);
		CardsImage[12].setBounds(990, 500, 60, 100);
		CardsImage[13].setBounds(1060, 500, 60, 100);
		CardsImage[14].setBounds(1130, 500, 60, 100);
		
		CardsImage[15].setBounds(850, 600, 60, 100);
		CardsImage[16].setBounds(920, 600, 60, 100);
		CardsImage[17].setBounds(990, 600, 60, 100);
		CardsImage[18].setBounds(1060, 600, 60, 100);
		CardsImage[19].setBounds(1130, 600, 60, 100);

		
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
		
		extraPieceLabel.setBounds(850, 170, extraPieceLabel.getIcon().getIconWidth(), extraPieceLabel.getIcon().getIconHeight());
		
		menuPanel.add(extraPieceLabel);
		
		rotatePieceButton = new JButton("rotate tile");
		rotatePieceButton.setBounds(1000, 170, 200, 50);
		rotatePieceButton.addActionListener(this);
		rotatePieceButton.setFocusable(false);
		menuPanel.add(rotatePieceButton);
		
		for(int i = 0; i < 12; i++) {
			
			tileButtons.add(new JButton(""));
			
			if(i == 0) {
				
				tileButtons.get(i).setBounds(200, 50, 50, 50);
				
			} else if(i == 1) {
				
				tileButtons.get(i).setBounds(400, 50, 50, 50);
				
			} else if(i == 2) {
				
				tileButtons.get(i).setBounds(600, 50, 50, 50);
				
			} else if(i == 3) {
				
				tileButtons.get(i).setBounds(700, 200, 50, 50);
				
			} else if(i == 4) {
				
				tileButtons.get(i).setBounds(700, 400, 50, 50);
				
			} else if(i == 5) {
				
				tileButtons.get(i).setBounds(700, 600, 50, 50);
				
			} else if(i == 6) {
				
				tileButtons.get(i).setBounds(200, 700, 50, 50);
				
			} else if(i == 7) {
				
				tileButtons.get(i).setBounds(400, 700, 50, 50);
				
			} else if(i == 8) {
				
				tileButtons.get(i).setBounds(600, 700, 50, 50);
				
			} else if(i == 9) {
				
				tileButtons.get(i).setBounds(50, 200, 50, 50);
				
			} else if(i == 10) {
				
				tileButtons.get(i).setBounds(50, 400, 50, 50);
				
			} else if(i == 11) {
				
				tileButtons.get(i).setBounds(50, 600, 50, 50);
				
			}
			
			tileButtons.get(i).addActionListener(this);
			tileButtons.get(i).setFocusable(false);
			menuPanel.add(tileButtons.get(i));
			
		}

		// places the JComponents to the panel
		menuPanel.add(boardLabel);

		Player1Label = new JLabel(new ImageIcon("images/player1.png"));
		Player2Label = new JLabel(new ImageIcon("images/player2.png"));
		Player3Label = new JLabel(new ImageIcon("images/player3.png"));
		Player4Label = new JLabel(new ImageIcon("images/player4.png"));

		Player1Label.setBounds(700, 300, 200, 100);
		Player2Label.setBounds(700, 400, 200, 100);
		Player3Label.setBounds(700, 500, 200, 100);
		Player4Label.setBounds(700, 600, 200, 100);

		menuPanel.add(Player1Label);
		menuPanel.add(Player2Label);
		menuPanel.add(Player3Label);
		menuPanel.add(Player4Label);

	}
	
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
	
	public void rotateExtraTile() {
		
		extraPiece.rotateTile();
		
		extraPieceLabel.setIcon(new ImageIcon(new ImageIcon(extraPiece.getFilePath())
				.getImage().getScaledInstance(92, 92, 0)));
		
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		
		if(event.getSource().equals(rotatePieceButton)) {
			
			rotateExtraTile();
			
		} 
		
		for(int i = 0; i < tileButtons.size(); i++) {
			
			// move 1st to 3rd movable column down
			if(event.getSource().equals(tileButtons.get(i)) && i >= 0 && i <= 2) {
				
				Tile tempExtraPiece = board[1 + i*2][board.length-1];
				
				for(int j = board.length - 1; j > 0; j--) {
					
					board[1 + i*2][j] = board[1 + i*2][j-1];
					
					boardIcons[1 + i*2][j].setIcon(new ImageIcon(new ImageIcon(board[1 + i*2][j].getFilePath())
							.getImage().getScaledInstance(92, 92, 0)));
					
				}
				
				board[1 + i*2][0] = extraPiece;
				boardIcons[1 + i*2][0].setIcon(new ImageIcon(new ImageIcon(board[1 + i*2][0].getFilePath())
						.getImage().getScaledInstance(92, 92, 0)));
				
				extraPiece = tempExtraPiece;
				
				extraPieceLabel.setIcon(new ImageIcon(new ImageIcon(extraPiece.getFilePath())
						.getImage().getScaledInstance(92, 92, 0)));
				
			}
			// move 4th to 5th movable column downddd
			else if(event.getSource().equals(tileButtons.get(i)) && i >= 3 && i <= 5) {
				
				Tile tempExtraPiece = board[1 + i*2][board.length-1];
				
				for(int j = board.length - 1; j > 0; j--) {
					
					board[1 + i*2][j] = board[1 + i*2][j-1];
					
					boardIcons[1 + i*2][j].setIcon(new ImageIcon(new ImageIcon(board[1 + i*2][j].getFilePath())
							.getImage().getScaledInstance(92, 92, 0)));
					
				}
				
				board[1 + i*2][0] = extraPiece;
				boardIcons[1 + i*2][0].setIcon(new ImageIcon(new ImageIcon(board[1 + i*2][0].getFilePath())
						.getImage().getScaledInstance(92, 92, 0)));
				
				extraPiece = tempExtraPiece;
				
				extraPieceLabel.setIcon(new ImageIcon(new ImageIcon(extraPiece.getFilePath())
						.getImage().getScaledInstance(92, 92, 0)));
				
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
			
		} else if(key.getKeyCode() == KeyEvent.VK_R) {
			
			rotateExtraTile();
			
		}
		
	}

	@Override
	public void keyReleased(KeyEvent key) {
		
		
	}

}
