package states;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

public class TypeState extends State {

	private JPanel typePanel;
	private JButton playButton;
	private JButton aiButton;
	private JButton backButton;
	
	public void init() {
		
		typePanel = new JPanel();	
		playButton = new JButton("4 PLAYER GAME");
		aiButton = new JButton("AI GAME");
		backButton = new JButton("BACK");
	}
	
	
	public void addJComponents() {
		
		playButton.setBounds(550, 280, 197, 80);
		playButton.addActionListener(this);
		aiButton.setBounds(550, 420, 197, 80);
		aiButton.addActionListener(this);
		backButton.setBounds(40, 40, 100, 50);
		backButton.addActionListener(this);
		
		typePanel.setLayout(null);
		typePanel.setBounds(0, 0, State.ScreenWidth, State.ScreenHeight);
		typePanel.setBackground(Color.green);
		typePanel.setOpaque(true);
		add(typePanel);
		
		typePanel.add(playButton);
		typePanel.add(aiButton);
		typePanel.add(backButton);
		
	}

	//
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(playButton)) {
			new GameState();
			this.dispose();
		}
		
		else if (event.getSource().equals(backButton)) {
			new MenuState();
			this.dispose();
			
		}
		//else if (event.getSource().equals(aiButton)) {
			//new OptionState();
			//this.dispose();
		//}
	}

}
