package states;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.color.*;

public class OptionState extends State{

	private JPanel optionsPanel;
	private JButton controlsButton;
	private JButton rulesButton;
	
	public void init() {
		optionsPanel = new JPanel();	
		controlsButton = new JButton("CONTROLS");
		rulesButton = new JButton("RULES");
	}

	
	public void addJComponents() {
		optionsPanel.setLayout(null);
		optionsPanel.setBounds(0, 0, State.ScreenWidth, State.ScreenHeight);
		optionsPanel.setBackground(Color.gray);
		optionsPanel.setOpaque(true);
		add(optionsPanel);
		
		controlsButton.setBounds(550, 280, 197, 80);
		controlsButton.addActionListener(this);
		rulesButton.setBounds(550, 420, 197, 80);
		rulesButton.addActionListener(this);
		
		optionsPanel.add(controlsButton);
		optionsPanel.add(rulesButton);
		
	}

	

	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(rulesButton)) {
			new RuleState();
			this.dispose();
		}
		
		else if (event.getSource().equals(controlsButton)) {
			new ControlState();
			this.dispose();
		}
		
		else if (event.getSource().equals(controlsButton)) {
			System.exit(DO_NOTHING_ON_CLOSE);
			
		}
		
	}
	
	@Override
	public void saveGame() {
		
		
		
	}
	
}
