package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import sounds.MusicPlayer;

public class TypeState extends State {

	private JPanel typePanel;
	private JButton playButton;
	private JButton backButton;
	private JLabel background;
	private JLabel titleLabel;
	
	private ButtonGroup[] radioButtons;
	private JRadioButton[][] playerSelection;
	
	private JLabel[] players;
	private JLabel[] names;
	
	public void init() {
		
		// update background music
		MusicPlayer.stopMusic();
		MusicPlayer.playMusic("audio/playerSelection.wav");
		
		typePanel = new JPanel();	
		playButton = new JButton("START");
		backButton = new JButton("BACK");
		
		playerSelection = new JRadioButton[4][4];
		radioButtons = new ButtonGroup[4];
		
		players = new JLabel[4];
		names = new JLabel[4];
		
	}
	
	
	public void addJComponents() {
		
		for(int i = 0; i < 4; i++) {
			
			radioButtons[i] = new ButtonGroup();
			
			for(int j = 0; j < 4; j++) {
				
				if(j == 0) {
					playerSelection[i][j] = new JRadioButton("human");
					playerSelection[i][j].setSelected(true);
				}
				else if(j == 1)
					playerSelection[i][j] = new JRadioButton("simple AI");
				else if(j == 2)
					playerSelection[i][j] = new JRadioButton("strong AI");
				else if(j == 3)
					playerSelection[i][j] = new JRadioButton("insane AI");
				
				playerSelection[i][j].setFont(new Font("times new roman", Font.ITALIC, 30));
				playerSelection[i][j].setForeground(Color.white);
				playerSelection[i][j].setBounds(150 + i * 250, 500 + j * 35, 250, 35);

				radioButtons[i].add(playerSelection[i][j]);
				typePanel.add(playerSelection[i][j]);
				
			}
			
		}

		backButton.setBounds(50, 50, 150, 50);
		backButton.addActionListener(this);
		backButton.setFont(new Font("times new roman", Font.ITALIC, 28));
		backButton.setForeground(Color.white);
		backButton.setBackground(Color.DARK_GRAY);
		backButton.setOpaque(true);
		backButton.setBorderPainted(false);
		
		playButton.setBounds(ScreenWidth - 200, ScreenHeight - 100, 150, 50);
		playButton.addActionListener(this);
		playButton.setFont(new Font("times new roman", Font.ITALIC, 28));
		playButton.setForeground(Color.white);
		playButton.setBackground(Color.DARK_GRAY);
		playButton.setOpaque(true);
		playButton.setBorderPainted(false);
		
		typePanel.setLayout(null);
		typePanel.setBounds(0, 0, State.ScreenWidth, State.ScreenHeight);
		typePanel.setBackground(Color.green);
		typePanel.setOpaque(true);
		add(typePanel);
		
		titleLabel = new JLabel("PLAYER CUSTOMIZATION");
		titleLabel.setBounds(ScreenWidth/2 - 400, -25, 800, 200);
		titleLabel.setFont((new Font( "times new roman", Font.PLAIN | Font.BOLD, 60)));
		titleLabel.setForeground(Color.white);
		
		background = new JLabel(new ImageIcon(new ImageIcon("images/selectionBackground.jpg")
				.getImage().getScaledInstance(ScreenWidth, ScreenHeight, 0)));
		background.setBounds(0, 0, ScreenWidth, ScreenHeight);
		
		for(int i = 0; i < 4; i++) {
			
			names[i] = new JLabel("Player " + (i+1));
			names[i].setBounds(190 + i * 250, 110, 300, 300);
			names[i].setFont(new Font("times new roman", Font.ITALIC | Font.BOLD, 28));
			names[i].setForeground(Color.white);
			
			typePanel.add(names[i]);
			
			players[i] = new JLabel(new ImageIcon(new ImageIcon("images/player" + (i+1) + ".png")
					.getImage().getScaledInstance(300, 300, 0)));
			players[i].setBounds(90 + i * 250, 275, 300, 300);

			typePanel.add(players[i]);
					
		}
		
		typePanel.add(backButton);
		typePanel.add(playButton);
		typePanel.add(titleLabel);
		typePanel.add(background);
		
	}

	public void actionPerformed(ActionEvent event) {
		
		if (event.getSource().equals(playButton)) {
			
			for(int i = 0; i < 4; i++) {
				
				if(playerSelection[i][0].isSelected()) {
					GameState.playerAILevel[i] = 0;
					
				} else if(playerSelection[i][1].isSelected()) {
					GameState.playerAILevel[i] = 1;
					
				} else if(playerSelection[i][2].isSelected()) {
					GameState.playerAILevel[i] = 2;
					
				} else if(playerSelection[i][3].isSelected()) {
					GameState.playerAILevel[i] = 3;
				}
				
			}
			
			GameState gameState = new GameState(false, "");
			this.dispose();
		}
		
		else if (event.getSource().equals(backButton)) {
			MusicPlayer.stopMusic();
			new MenuState();
			this.dispose();
			
		}
		
	}

}
