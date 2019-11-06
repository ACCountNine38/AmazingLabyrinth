package states;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MenuState extends State {

	private JPanel menuPanel;
	private JButton startButton;
	
	public MenuState() {
		
		

	}

	@Override
	public void addJComponents() {

		menuPanel = new JPanel(null);
		startButton = new JButton(new ImageIcon(new ImageIcon
				("images/gameboard.png").getImage().getScaledInstance(393/2, 170/2, 0)));
		
		// settings for the score panel and add it to the frame
		menuPanel.setLayout(null);
		menuPanel.setBounds(0, 0, State.ScreenWidth, State.ScreenHeight);
		menuPanel.setBackground(Color.black);
		menuPanel.setOpaque(true);
		add(menuPanel);

		// set the required informations for all the JComponents
		startButton.setBounds(270, 280, startButton.getIcon().getIconWidth(), startButton.getIcon().getIconHeight());
		startButton.addActionListener(this);

		// places the JComponents to the panel
		menuPanel.add(startButton);

	}

	@Override
	public void actionPerformed(ActionEvent event) {
		
		if (event.getSource().equals(startButton)) {
			
			new GameState();
			
			this.dispose();
			
		}
		
	}

}
