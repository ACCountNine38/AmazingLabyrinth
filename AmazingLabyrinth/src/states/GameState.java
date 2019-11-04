package states;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameState extends State {

	private JPanel menuPanel;
	private JLabel boardLabel;

	public GameState() {

		
		
	}

	@Override
	public void addJComponents() {
		
		menuPanel = new JPanel(null);
		boardLabel = new JLabel(new ImageIcon(new ImageIcon("images/blogamazeingboard.jpg")
				.getImage().getScaledInstance(State.ScreenWidth, State.ScreenHeight, 0)));
		
		// settings for the score panel and add it to the frame
		menuPanel.setLayout(null);
		menuPanel.setBounds(0, 0, 25 * 30, 25 * 28);
		menuPanel.setBackground(Color.black);
		menuPanel.setOpaque(true);
		add(menuPanel);

		// set the required informations for all the JComponents
		boardLabel.setBounds(0, 0, State.ScreenWidth, State.ScreenHeight);

		// places the JComponents to the panel
		menuPanel.add(boardLabel);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
