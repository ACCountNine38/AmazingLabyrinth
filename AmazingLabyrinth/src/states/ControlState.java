package states;

import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.color.*;

public class ControlState extends State{

	private JPanel controlsPanel;
	private JLabel controlLabel;
	//Add labels with controls information(maybe controls switch?)
	//use JLabel for text info
	public void init() {
		
		controlsPanel = new JPanel();
		controlLabel = new JLabel();
	}

	
	public void addJComponents() {
		controlsPanel.setLayout(null);
		controlsPanel.setBounds(0, 0, State.ScreenWidth, State.ScreenHeight);
		controlsPanel.setBackground(Color.white);
		controlsPanel.setOpaque(true);
		add(controlsPanel);
		
		controlLabel.setBounds(550, 420, 197, 80);
		controlLabel.setText("THE CONTROLS GO HERE");
		
		controlsPanel.add(controlLabel);
		
	}

	public void actionPerformed(ActionEvent e) {
		
		
	}
	
}
