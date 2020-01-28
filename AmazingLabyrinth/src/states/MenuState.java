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
	private JButton exitButton;
	private JLabel titleLabel;
	private JLabel background;
	//create new screen with both AI and 4 player button options
	
	public void init() {
		
		menuPanel = new JPanel();
		optionsButton = new JButton("OPTIONS"); 
		loadGameButton = new JButton("LOAD GAME");
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
		startButton.setBounds(50, ScreenHeight - 325, 225, 50);
		startButton.addActionListener(this);
		startButton.setFont(new Font("times new roman", Font.ITALIC, 28));
		startButton.setForeground(Color.white);
		startButton.setBackground(Color.DARK_GRAY);
		startButton.setOpaque(true);
		startButton.setBorderPainted(false);
		optionsButton.setBounds(50, ScreenHeight - 250, 225, 50);
		optionsButton.addActionListener(this);
		optionsButton.setFont(new Font("times new roman", Font.ITALIC, 28));
		optionsButton.setForeground(Color.white);
		optionsButton.setBackground(Color.DARK_GRAY);
		optionsButton.setOpaque(true);
		optionsButton.setBorderPainted(false);
		loadGameButton.setBounds(50, ScreenHeight - 175, 225, 50);
		loadGameButton.addActionListener(this);
		loadGameButton.setFont(new Font("times new roman", Font.ITALIC, 28));
		loadGameButton.setForeground(Color.white);
		loadGameButton.setBackground(Color.DARK_GRAY);
		loadGameButton.setOpaque(true);
		loadGameButton.setBorderPainted(false);
		exitButton.setBounds(50, ScreenHeight - 100, 225, 50);
		exitButton.addActionListener(this);
		exitButton.setFont(new Font("times new roman", Font.ITALIC, 28));
		exitButton.setForeground(Color.white);
		exitButton.setBackground(Color.DARK_GRAY);
		exitButton.setOpaque(true);
		exitButton.setBorderPainted(false);
		
		titleLabel = new JLabel("AMAZING LABYRINTH");
		titleLabel.setBounds(50, -25, 800, 180);
		titleLabel.setFont((new Font( "times new roman", Font.PLAIN | Font.BOLD, 60)));
		titleLabel.setForeground(Color.white);
		
		background = new JLabel(new ImageIcon(new ImageIcon("images/menuBackground.png")
				.getImage().getScaledInstance(ScreenWidth+500, ScreenHeight, 0)));
		background.setBounds(0, 0, ScreenWidth, ScreenHeight);
		
		// places the JComponents to the panel
		menuPanel.add(titleLabel);
		menuPanel.add(loadGameButton);
		menuPanel.add(startButton);
		menuPanel.add(optionsButton);
		menuPanel.add(exitButton);
		menuPanel.add(background);
		
	}

	
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(startButton)) {
			new TypeState();
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
		else if (event.getSource().equals(exitButton)) {
			System.exit(1);
		}
	}

}
