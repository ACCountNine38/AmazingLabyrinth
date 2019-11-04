package states;

import java.awt.event.ActionListener;

import javax.swing.JFrame;

import utility.CustomizationTool;

public abstract class State extends JFrame implements ActionListener {
	
	public static final int ScreenWidth = 750;
	public static final int ScreenHeight = 700;
	
	public State() {
		
		addJComponents();
		CustomizationTool.frameSetup(this);
		CustomizationTool.customCursor(this);
		
	}
	
	public abstract void addJComponents();

}