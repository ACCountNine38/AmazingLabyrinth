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
	
	private int[][] board = new int[9][9];
	private ArrayList<Integer> mapBits = new ArrayList<Integer>();
	
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
		
		// filling the pre-generated tiles
		for(int i = 2; i < board.length; i++) {
			
			// filling the corners
			board[0][0] = 7;
			board[board.length - 1][0] = 2;
			board[0][board.length - 1] = 6;
			board[board.length - 1][board.length - 1] = 5;
			
			if(i % 2 == 0) {
				board[i][0] = '1';
				board[0][i] = '1';
				board[board.length-1][i] = '1';
				board[i][board.length-1] = '1';
			}
			
		}
		
	}
	
	private void addMapBits() {
		
		
		
	}
	
	@Override
	public void addJComponents() {
		
		fillMapBits();
		
		menuPanel = new JPanel(null);
		boardLabel = new JLabel(new ImageIcon(new ImageIcon("images/gameboard.png")
				.getImage().getScaledInstance(700, 700, 0)));
		
		// settings for the score panel and add it to the frame
		menuPanel.setLayout(null);
		menuPanel.setBounds(0, 0, State.ScreenWidth, State.ScreenHeight);
		menuPanel.setBackground(Color.black);
		menuPanel.setOpaque(true);
		add(menuPanel);

		// set the required informations for all the JComponents
		boardLabel.setBounds(50, 50, 700, 700);

		// places the JComponents to the panel
		menuPanel.add(boardLabel);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
