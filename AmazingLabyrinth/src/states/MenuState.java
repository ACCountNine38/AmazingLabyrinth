package states;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sounds.MusicPlayer;

public class MenuState extends State {
	
	private JPanel menuPanel;
	private JButton startButton;
	private JButton optionsButton;
	private JButton exitButton;
	//create new screen with both AI and 4 player button options
	
	public MenuState() {

	}

	
	public void init() {
		
		menuPanel = new JPanel();
		optionsButton = new JButton("OPTIONS"); 
		startButton = new JButton("START");
		exitButton = new JButton("EXIT");
		
		MusicPlayer.playMusic("audio/menuTheme.wav");
		
	}

	
	public void addJComponents() {

		// settings for the score panel and add it to the frame
		menuPanel.setLayout(null);
		menuPanel.setBounds(0, 0, State.ScreenWidth, State.ScreenHeight);
		menuPanel.setBackground(Color.black);
		menuPanel.setOpaque(true);
		add(menuPanel);
		
		// set the required informations for all the JComponents
		startButton.setBounds(550, 280, 197, 80);
		startButton.addActionListener(this);
		optionsButton.setBounds(550, 420, 197, 80);
		optionsButton.addActionListener(this);
		exitButton.setBounds(550, 560, 197, 80);
		exitButton.addActionListener(this);
		
		
		// places the JComponents to the panel
		menuPanel.add(startButton);
		menuPanel.add(optionsButton);
		menuPanel.add(exitButton);
		
	}

	
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(startButton)) {
			new TypeState();
			this.dispose();
		}
		
		else if (event.getSource().equals(exitButton)) {
			System.exit(DO_NOTHING_ON_CLOSE);
			
		}
		else if (event.getSource().equals(optionsButton)) {
			new OptionState();
			this.dispose();
		}
	}


}
