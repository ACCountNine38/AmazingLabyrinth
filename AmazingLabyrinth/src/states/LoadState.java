package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
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
	private ArrayList<JButton> deleteButtons;
	private JLabel titleLabel;
	private JButton backButton;
	
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
		deleteButtons = new ArrayList<JButton>();
		
		// loads all the files and puts their name in a list
		File files = new File("saved");
		for(int i = 0; i < files.listFiles().length; i++) {
			savedPaths.add(files.listFiles()[i].getAbsolutePath());
			savedNames.add(files.listFiles()[i].getName());
		}
		
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
			savedButton.setBounds(0, 100*currentPath, 400, 100);
			savedButton.addActionListener(this);
			savedButton.setMaximumSize(savedButton.getSize());
			savedButton.setMinimumSize(savedButton.getSize());
			savedButton.setPreferredSize(savedButton.getSize());
			loadButtons.add(savedButton);
			loadPanel.add(savedButton);
			
			JButton deleteButton = new JButton("delete game");
			//deleteButton.setBounds(400, 100*currentPath, 400, 100);
			deleteButton.addActionListener(this);
			deleteButtons.add(deleteButton);
			//loadPanel.add(deleteButton);
			
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
		
		gameContainer = new JScrollPane(loadPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		gameContainer.setBounds(450, 150, 400, 600);
		backgroundPanel.add(gameContainer);
		
		backgroundPanel.add(backButton);
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		
		for(int button = 0; button < loadButtons.size(); button++) {
			
			if(event.getSource().equals(loadButtons.get(button))) {
				
				new GameState(true, savedPaths.get(button));
				this.dispose();
				
			} else if(event.getSource().equals(deleteButtons.get(button))) {
				
				File file = new File(savedPaths.get(button));
				file.delete();
				
				gameContainer.remove(loadButtons.get(button));
				gameContainer.remove(deleteButtons.get(button));
				
				savedPaths.remove(button);
				savedNames.remove(button);
				
				backgroundPanel.remove(loadButtons.get(button));
				backgroundPanel.remove(deleteButtons.get(button));
				
				loadButtons.remove(loadButtons.get(button));
				deleteButtons.remove(deleteButtons.get(button));
				
				repaint();
				
				return;
				
			}
			
		}
		
		if (event.getSource().equals(backButton)) {
			
			MusicPlayer.stopMusic();
			new MenuState();
			this.dispose();
			
		}
		
	}

}
