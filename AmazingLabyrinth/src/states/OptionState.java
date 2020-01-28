package states;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.color.*;

public class OptionState extends State{

	private JPanel optionsPanel;
	private JButton controlsButton;
	private JButton rulesButton;
	private JButton backButton;
	private JLabel titleLabel;
	
	
	public void init() {
		optionsPanel = new JPanel();	
		controlsButton = new JButton("CONTROLS");
		rulesButton = new JButton("RULES");
		backButton = new JButton("BACK");
		titleLabel = new JLabel("GAME OPTIONS");
		
	}

	
	public void addJComponents() {
		optionsPanel.setLayout(null);
		optionsPanel.setBounds(0, 0, State.ScreenWidth, State.ScreenHeight);
		optionsPanel.setBackground(Color.black);
		optionsPanel.setOpaque(true);
		add(optionsPanel);
		
		controlsButton.setBounds(550, 280, 197, 80);
		controlsButton.addActionListener(this);
		rulesButton.setBounds(550, 420, 197, 80);
		rulesButton.addActionListener(this);
		backButton.setBounds(40, 40, 100, 50);
		backButton.addActionListener(this);
		titleLabel.setBounds(400, 50, 800, 180);
		titleLabel.setFont((new Font( "Serif", Font.PLAIN, 60)));
		titleLabel.setForeground(Color.white);
		
		optionsPanel.add(controlsButton);
		optionsPanel.add(rulesButton);
		optionsPanel.add(backButton);
		optionsPanel.add(titleLabel);
		
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
		
		else if (event.getSource().equals(backButton)) {
			new MenuState();
			this.dispose();
		}
	}
	
}


