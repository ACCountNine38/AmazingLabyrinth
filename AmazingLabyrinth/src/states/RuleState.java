package states;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.color.*;

public class RuleState extends State{

	private JPanel rulesPanel;
	private JLabel rulesLabel;
	private JButton backButton;
	private JLabel ruleLabel;
	//Add labels with rule information 
	
	public void init() {
		rulesPanel = new JPanel();	
		rulesLabel = new JLabel();
		backButton = new JButton();
		ruleLabel = new JLabel("GAME RULES");
	}

	
	public void addJComponents() {
		
		rulesPanel.setLayout(null);
		rulesPanel.setBounds(0, 0, State.ScreenWidth, State.ScreenHeight);
		rulesPanel.setBackground(Color.black);
		rulesPanel.setOpaque(true);
		add(rulesPanel);
		
		backButton.setBounds(40, 40, 100, 50);
		backButton.setText("BACK");
		backButton.addActionListener(this);
		rulesLabel.setBounds(250, 100, 800, 600);
		ruleLabel.setBounds(400, 50, 800, 180);
		ruleLabel.setFont((new Font( "Serif", Font.PLAIN, 60)));
		ruleLabel.setForeground(Color.white);
		
		
		rulesPanel.add(rulesLabel);
		rulesPanel.add(backButton);
		rulesPanel.add(ruleLabel);
		
		StringBuilder sb = new StringBuilder(64);
        sb.append("<html>If you are the first player, look at your first treasure card and return it, face down, to your pile of cards. This\r\n" + 
        		"is your first GOAL in the labyrinth. You may not look at your other cards until you have reached this first\r\n" + 
        		"GOAL.\r\n" + 
        		"To reach your goal you must shift the walls of the maze. This is done by pushing the extra maze card into\r\n" + 
        		"the labyrinth at some point so that you can move as far as you wish along its open passageways.\r\n" + 
        		"From the edge of the board push the extra maze card into the labyrinth. The places where the maze card can\r\n" + 
        		"be added are indicated on the edge of the board by an arrow. The maze card that is pushed out will become\r\n" + 
        		"the next player's means of shifthing the maze. Until the next player's turn, the new extra maze card is left\r\n" + 
        		"where it is so that all players will know how the maze has been shifted on this turn.\r\n" + 
        		"You must move the maze before each turn, even if you don't need to in order to reach your goal. This way\r\n" + 
        		"you can wall in another player!\r\n" + 
        		"You may not immediately reverse the last player's move by returning the extra card to the position it was\r\n" + 
        		"just pushed out of.\r\n" + 
        		"If the shifting maze pushes out a player's piece, the piece is transferred to the newly inserted maze card at\r\n" + 
        		"the other side. This applies whether the piece belongs to the person making the move or to another player.\r\n" + 
        		"Transferring this piece does not count as a move.\r\n" + 
        		"After shifting the maze, move your playing piece as far as you choose along the open corridor. You can also\r\n" + 
        		"choose not to move at all. Often you will be able to reach your goal in one move. If not, try to get in the best\r\n" + 
        		"possible position for your next turn.\r\n" + 
        		"More than one playing piece can occupy a single square. Once you reach your first goal, turn over the top\r\n" + 
        		"treasure card and leave it face up beside your pile of cards. The next card in the pile will be your next goal.\r\n" + 
        		"The turn then passes to the next player.\r\n" + 
        		"END OF GAME: Once you have reached all your goals you must return to the corner position from which\r\n" + 
        		"you entered the labyrinth. The winner is the first player to turn up all the treasure cards and return to his or\r\n" + 
        		"her starting position.").
                        append("  This is a very long String to see if you can wrap with in").
                        append("the available space</html>");

        JLabel label = new JLabel(sb.toString());
        label.setBounds(135, 225, 1000, 500);
        label.setFont((new Font( "Serif", Font.PLAIN, 17)));
        label.setForeground(Color.white);
        rulesPanel.add(label);
        
	}

		

	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(backButton)) {
			new OptionState();
			this.dispose();
		}
		
	}
	
}


