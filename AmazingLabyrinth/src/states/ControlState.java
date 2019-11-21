package states;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.color.*;

public class ControlState extends State{

	private JPanel controlsPanel;
	private JLabel controlLabel;
	private JButton backButton;
	private JLabel returnLabel;
	private JLabel rotateLabel;
	private JLabel titleLabel;
	
	public void init() {
		
		controlsPanel = new JPanel();
		controlLabel = new JLabel();
		returnLabel = new JLabel();
		backButton = new JButton("BACK");
		rotateLabel = new JLabel();
		titleLabel = new JLabel();
		
	}

	// add panel components and set their properties
	public void addJComponents() {
		controlsPanel.setLayout(null);
		controlsPanel.setBounds(0, 0, State.ScreenWidth, State.ScreenHeight);
		controlsPanel.setBackground(Color.black);
		controlsPanel.setOpaque(true);
		add(controlsPanel);
		
		controlLabel.setBounds(350, 250, 600, 80);
		controlLabel.setOpaque(true);
		controlLabel.setText("Press WASD to move your character");
		controlLabel.setBackground(Color.black);
		controlLabel.setForeground(Color.white);
		controlLabel.setFont((new Font( "Serif", Font.PLAIN, 20)));
		backButton.setBounds(40, 40, 100, 50);
		backButton.addActionListener(this);
		returnLabel.setBounds(350, 425, 600, 80);
		returnLabel.setText("Press the return button to end the current turn");
		returnLabel.setBackground(Color.black);
		returnLabel.setForeground(Color.white);
		returnLabel.setFont((new Font( "Serif", Font.PLAIN, 20)));
		rotateLabel.setBounds(350, 625, 600, 80);
		rotateLabel.setText("Press R to rotate the tile clockwise");
		rotateLabel.setBackground(Color.black);
		rotateLabel.setForeground(Color.white);
		rotateLabel.setFont((new Font( "Serif", Font.PLAIN, 20)));
		titleLabel.setBounds(400, 50, 800, 180);
		titleLabel.setText("KEY CONTROLS");
		titleLabel.setBackground(Color.black);
		titleLabel.setForeground(Color.white);
		titleLabel.setFont((new Font( "Serif", Font.PLAIN, 60)));
		
		//controlsPanel.add(controlLabel);
		controlsPanel.add(backButton);
		controlsPanel.add(controlLabel);
		controlsPanel.add(returnLabel);
		controlsPanel.add(rotateLabel);
		controlsPanel.add(titleLabel);
		
		try {
			
		//getting images from image file and setting them on the type Panel
		BufferedImage myPicture = ImageIO.read(new File("images/wasdkeys2.png"));
		JLabel picLabel = new JLabel(new ImageIcon(myPicture));
		picLabel.setBounds(160, 250, 144, 94);
		controlsPanel.add(picLabel);
		
		BufferedImage myPicture2 = ImageIO.read(new File("images/enterkey2.png"));
		JLabel picLabel2 = new JLabel(new ImageIcon(myPicture2));
		picLabel2.setBounds(140, 375, 200, 200);
		controlsPanel.add(picLabel2);
		
		BufferedImage myPicture3 = ImageIO.read(new File("images/Rkey.png"));
		JLabel picLabel3 = new JLabel(new ImageIcon(myPicture3));
		picLabel3.setBounds(160, 600, 126, 120);
		controlsPanel.add(picLabel3);
		
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}

	public void actionPerformed(ActionEvent event) {
		
		if (event.getSource().equals(backButton)) {
			new OptionState();
			this.dispose();
		}
		
	}
	
}



