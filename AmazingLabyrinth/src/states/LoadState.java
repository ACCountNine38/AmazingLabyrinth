package states;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class LoadState extends State {

	private JPanel loadPanel;
	private JScrollPane gameContainer;
	private ArrayList<String> savedPaths;
	
	@Override
	public void init() {
		
		savedPaths = new ArrayList<String>();
		
		File files = new File("saved");
		for(int i = 0; i < files.listFiles().length; i++) 
			savedPaths.add(files.listFiles()[i].getName());
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		
	}

	@Override
	public void addJComponents() {
	
		loadPanel = new JPanel();
		loadPanel.setBounds(0, 0, ScreenWidth, ScreenHeight);
		loadPanel.setBackground(Color.black);
		add(loadPanel);

		gameContainer = new JScrollPane();
		for(String path: savedPaths) {
			
			JButton savedButton = new JButton(path);
			savedButton.addActionListener(this);
			gameContainer.add(savedButton);
			
		}
		
		gameContainer.setBounds(100, 100, 500, 600);
		loadPanel.add(gameContainer);
		
	}

	@Override
	public void saveGame() {
		// TODO Auto-generated method stub
		
	}

}
