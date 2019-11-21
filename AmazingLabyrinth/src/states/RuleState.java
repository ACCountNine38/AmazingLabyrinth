package states;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.color.*;

public class RuleState extends State{

	private JPanel rulesPanel;
	private JLabel rulesLabel;
	private JButton backButton;
	//Add labels with rule information 
	
	public void init() {
		rulesPanel = new JPanel();	
		rulesLabel = new JLabel();
		backButton = new JButton();
	}

	
	public void addJComponents() {
		
		rulesPanel.setLayout(null);
		rulesPanel.setBounds(0, 0, State.ScreenWidth, State.ScreenHeight);
		rulesPanel.setBackground(Color.pink);
		rulesPanel.setOpaque(true);
		add(rulesPanel);
		
		backButton.setBounds(40, 40, 100, 50);
		backButton.setText("BACK");
		backButton.addActionListener(this);
		rulesLabel.setBounds(550, 420, 197, 80);
		rulesLabel.setText("THE RULES GO HERE");
		
		rulesPanel.add(rulesLabel);
		rulesPanel.add(backButton);
		
	}

	

	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(backButton)) {
			new OptionState();
			this.dispose();
		}
		
	}
	
}
