package states;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameState extends State {

	private JPanel menuPanel;
	private JLabel boardLabel;
	
	private int[][] board;
	private JLabel[][] boardIcons;
	private ArrayList<Integer> mapBits;
	
	public GameState() {

		
		
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
		
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board.length; j++) {
				
				board[i][j] = (int)(Math.random()*11)+1;
				
			}
		}
		
	}

	@Override
	public void init() {
		
		board = new int[9][9];
		boardIcons = new JLabel[9][9];
		mapBits = new ArrayList<Integer>();
		
	}

	@Override
	public void addJComponents() {
		
		fillMapBits();
		
		menuPanel = new JPanel(null);
		boardLabel = new JLabel(new ImageIcon(new ImageIcon("images/gameboard.png")
				.getImage().getScaledInstance(700, 700, 0)));
		boardLabel.setBounds(50, 50, 700, 700);
		
		// settings for the score panel and add it to the frame
		menuPanel.setLayout(null);
		menuPanel.setBounds(0, 0, State.ScreenWidth, State.ScreenHeight);
		menuPanel.setBackground(Color.black);
		menuPanel.setOpaque(true);
		add(menuPanel);
		/*
		 * method that fills the board tiles with integer IDs
		 * 
		 * up down - 1
		 * left right - 2
		 * up right - 3
		 * up left - 4
		 * left down - 5
		 * right down - 6
		 * up down right - 7
		 * up down left - 8
		 * left right up - 9
		 * left right down - 10
		 * up down left right - 11
		 * 
		*/
		for(int i = 0; i < boardIcons.length; i++) {
			for(int j = 0; j < boardIcons[i].length; j++) {
				
				String path = "images/path" + board[i][j] + ".png";
				
				boardIcons[i][j] = new JLabel(new ImageIcon(new ImageIcon(path)
						.getImage().getScaledInstance((650) / 9, (650) / 9, 0)));
				
				boardIcons[i][j].setBounds(75 + boardIcons[i][j].getIcon().getIconWidth()*i, 75 + 
						boardIcons[i][j].getIcon().getIconHeight()*j, 
						boardIcons[i][j].getIcon().getIconWidth(),
						boardIcons[i][j].getIcon().getIconHeight());
				
				menuPanel.add(boardIcons[i][j]);
				
			}
		}
		
		// places the JComponents to the panel
		menuPanel.add(boardLabel);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
