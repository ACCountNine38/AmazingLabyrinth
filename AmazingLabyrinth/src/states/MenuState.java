package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sounds.MusicPlayer;

public class MenuState extends State {
	
	private JPanel menuPanel;
	private JButton startButton;
	private JButton optionsButton;
	private JButton loadGameButton;
	private JLabel titleLabel;
	private JLabel background;
	//create new screen with both AI and 4 player button options
	
	public void init() {
		
		menuPanel = new JPanel();
		optionsButton = new JButton("OPTIONS"); 
		loadGameButton = new JButton("LOAD GAME");
		startButton = new JButton("START");
		
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
		startButton.setBounds(550, 230, 197, 80);
		startButton.addActionListener(this);
		startButton.setFont(new Font("times new roman", Font.ITALIC, 28));
		optionsButton.setBounds(550, 510, 197, 80);
		optionsButton.addActionListener(this);
		optionsButton.setFont(new Font("times new roman", Font.ITALIC, 28));
		loadGameButton.setBounds(550, 370, 197, 80);
		loadGameButton.addActionListener(this);
		loadGameButton.setFont(new Font("times new roman", Font.ITALIC, 28));
		
		titleLabel = new JLabel("AMAZING LABYRINTH");
		titleLabel.setBounds(320, 75, 800, 180);
		titleLabel.setFont((new Font( "Serif", Font.PLAIN, 60)));
		titleLabel.setForeground(Color.white);
		
		background = new JLabel(new ImageIcon(new ImageIcon("images/background.jpg")
				.getImage().getScaledInstance(ScreenWidth, ScreenHeight, 0)));
		background.setBounds(0, 0, ScreenWidth, ScreenHeight);
		
		// places the JComponents to the panel
		
		menuPanel.add(titleLabel);
		menuPanel.add(loadGameButton);
		menuPanel.add(startButton);
		menuPanel.add(optionsButton);
		menuPanel.add(background);
		
	}

	
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(startButton)) {
			new GameState(false, "");
			this.dispose();
		}
		
		else if (event.getSource().equals(loadGameButton)) {
			new LoadState();
			this.dispose();
		}
		else if (event.getSource().equals(optionsButton)) {
			new OptionState();
			this.dispose();
		}
	}
	
	private class CloseListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			
			System.exit(0);
		}
	}

}
