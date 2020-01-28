package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import sounds.MusicPlayer;

/*
 * class the enables the player to load a previously saved game
 */
public class LoadState extends State {

	// JComponents of this screen
	private JPanel backgroundPanel;
	private JPanel loadPanel;
	private JScrollPane gameContainer;
	private ArrayList<String> savedPaths;
	private ArrayList<String> savedNames;
	private ArrayList<JButton> loadButtons;
	private JLabel titleLabel;
	private JLabel background;
	private JButton backButton;
	private JButton deleteButton;
	private JButton playButton;
	
	private String selectedPath;
	private int previousButton;
	
	// method that initializes every instance, override from the State class
	@Override
	public void init() {
		
		// update background music
		MusicPlayer.stopMusic();
		MusicPlayer.playMusic("audio/playerSelection.wav");
				
		// initializes all the JComponents
		savedPaths = new ArrayList<String>();
		savedNames = new ArrayList<String>();
		loadButtons = new ArrayList<JButton>();
		
		// loads all the files and puts their name in a list
		File files = new File("saved");
		for(int i = 0; i < files.listFiles().length; i++) {
			savedPaths.add(files.listFiles()[i].getAbsolutePath());
			savedNames.add(files.listFiles()[i].getName());
		}
		
		previousButton = -1;
		
	}
	
	// method that creates all the JComponents for this state, Override from the State class 
	@Override
	public void addJComponents() {
	
		// background panel is created to put all the JComponents on top
		backgroundPanel = new JPanel(null);
		backgroundPanel.setLayout(null);
		backgroundPanel.setBounds(0, 0, ScreenWidth, ScreenHeight);
		backgroundPanel.setBackground(Color.black);
		add(backgroundPanel);
		
		loadPanel = new JPanel();
		loadPanel.setLayout(new BoxLayout(loadPanel, BoxLayout.Y_AXIS));
		loadPanel.setBounds(0, 0, ScreenWidth, ScreenHeight);
		
		int currentPath = 0;
		for(String path: savedNames) {
			
			JButton savedButton = new JButton(path);
			savedButton.setBounds(0, 100*currentPath, 380, 100);
			savedButton.addActionListener(this);
			savedButton.setMaximumSize(savedButton.getSize());
			savedButton.setMinimumSize(savedButton.getSize());
			savedButton.setPreferredSize(savedButton.getSize());
			savedButton.setForeground(Color.white);
			savedButton.setBackground(Color.darkGray);
			savedButton.setOpaque(true);
			savedButton.setFont(new Font("times new roman", Font.ITALIC, 24));
			savedButton.setBorderPainted(false);
			loadButtons.add(savedButton);
			loadPanel.add(savedButton);
			
			JButton deleteButton = new JButton("delete game");
			//deleteButton.setBounds(400, 100*currentPath, 400, 100);
			deleteButton.addActionListener(this);
			
			currentPath++;
			
		}
		
		titleLabel = new JLabel("LOAD GAME");
		titleLabel.setBounds(475, 0, 800, 180);
		titleLabel.setFont((new Font("Serif", Font.PLAIN, 60)));
		titleLabel.setForeground(Color.white);
		backgroundPanel.add(titleLabel);
		
		backButton = new JButton("BACK");
		backButton.setBounds(40, 40, 100, 50);
		backButton.addActionListener(this);
		backgroundPanel.add(backButton);
		
		gameContainer = new JScrollPane(loadPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		gameContainer.setBounds(250, 150, 400, 600);
		backgroundPanel.add(gameContainer);
		
		playButton = new JButton("Load Game");
		playButton.setBounds(700, 300, 300, 100);
		playButton.addActionListener(this);
		playButton.setForeground(Color.white);
		playButton.setBackground(Color.darkGray);
		playButton.setOpaque(true);
		playButton.setFont(new Font("times new roman", Font.ITALIC, 24));
		playButton.setBorderPainted(false);
		backgroundPanel.add(playButton);
		
		deleteButton = new JButton("Delete Game");
		deleteButton.setBounds(700, 500, 300, 100);
		deleteButton.addActionListener(this);
		deleteButton.setForeground(Color.white);
		deleteButton.setBackground(Color.darkGray);
		deleteButton.setOpaque(true);
		deleteButton.setFont(new Font("times new roman", Font.ITALIC, 24));
		deleteButton.setBorderPainted(false);
		backgroundPanel.add(deleteButton);
		
		background = new JLabel(new ImageIcon(new ImageIcon("images/selectionBackground.jpg")
				.getImage().getScaledInstance(ScreenWidth, ScreenHeight, 0)));
		background.setBounds(0, 0, ScreenWidth, ScreenHeight);
		backgroundPanel.add(background);
		
		backgroundPanel.add(backButton);
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		
		for(int button = 0; button < loadButtons.size(); button++) {
			
			if(event.getSource().equals(loadButtons.get(button))) {
				
				selectedPath = savedPaths.get(button);
				loadButtons.get(button).setBackground(Color.yellow);
				loadButtons.get(button).setForeground(Color.DARK_GRAY);
				loadButtons.get(button).repaint();
				if(previousButton != -1 && previousButton != button) {
					
					loadButtons.get(previousButton).setBackground(Color.DARK_GRAY);
					loadButtons.get(previousButton).setForeground(Color.white);
					loadButtons.get(previousButton).repaint();
					
				}
				previousButton = button;
				
			} 
			
		}
		
		if (event.getSource().equals(backButton)) {
			
			MusicPlayer.stopMusic();
			new MenuState();
			this.dispose();
			
		} else if (event.getSource().equals(playButton)) {
			
			if(selectedPath != null) {
				
				new GameState(true, selectedPath);
				this.dispose();
				
			} else {
				
				// display message dialogue for invalid input
				JOptionPane.showMessageDialog(null, "Please select a saved game to be loaded \n\n"
								+ "click 'ok' to continue...",
						"INVALID GAME PATH", JOptionPane.WARNING_MESSAGE);
				
			}
			
		} else if (event.getSource().equals(deleteButton)) {
			
			if(selectedPath != null) {
				
				File file = new File(selectedPath);
				file.delete();
				
				gameContainer.remove(loadButtons.get(previousButton));
				
				savedPaths.remove(previousButton);
				savedNames.remove(previousButton);
				
				loadButtons.remove(previousButton);
				
				loadPanel.revalidate();
				loadPanel.repaint();
				gameContainer.revalidate();
				gameContainer.repaint();
				
				selectedPath = null;
				previousButton = -1;
				
			} else {
				
				// display message dialogue for invalid input
				JOptionPane.showMessageDialog(null, "Please select a saved game to be deleted \n\n"
								+ "click 'ok' to continue...",
						"INVALID GAME PATH", JOptionPane.WARNING_MESSAGE);
				
			}
			
		}
		
	}

}
