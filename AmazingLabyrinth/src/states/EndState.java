package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sounds.MusicPlayer;

//The state class that represents the last frame of the game. It shows the winner's order and allow the player to play again or exit
public class EndState extends JFrame implements ActionListener {
	
	//All the variables for the buttons, labels
	private JPanel menuPanel;
	private JButton startButton;
	private JButton ExitButton;
	
	//The Araylist for the winners
	private ArrayList<Integer> Winning;
	
	private JLabel FirstPlace;
	private JLabel SecondPlace;
	private JLabel ThirdPlace;
	private JLabel FourthPlace;
	
	JLabel firstPlace = new JLabel();
	JLabel secondPlace = new JLabel();
	JLabel thirdPlace = new JLabel();
	JLabel fourthPlace = new JLabel();
	
	//The constructor method that initialize the winner variable. 
	public EndState(ArrayList<Integer> winner) {
		
		//initialize the winner variable
		this.Winning = winner;
		
		//run the methods
		init();
		addJComponents();
		
		setVisible(true);
		
		//Set the size of the frame
		setSize(1280, 800);
		setTitle("End");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void init() {
		
		//setting the labels and panel name 
		menuPanel = new JPanel();
		ExitButton = new JButton("Exit"); 
		startButton = new JButton("Restart");
		
		FirstPlace = new JLabel("First Place:         Player");
		SecondPlace = new JLabel("Second Place:     Player ");
		ThirdPlace = new JLabel("Third Place:       Player ");
		FourthPlace = new JLabel("Fourth Place:     Player ");
	
		//plays the music		
		MusicPlayer.playMusic("audio/menuTheme.wav");
		
	}

	public void addJComponents() {
		
		//Assign the string with the values in the Winner Arraylist. It also turns them to String since they are int 
		String First = Winning.get(0).toString();
		String Second = Winning.get(1).toString();
		String Third = Winning.get(2).toString();
		String Fourth = Winning.get(3).toString();
		
		//Set the 1st/2nd/3rd/4th labels with their responsing String		
		firstPlace.setText(First);
		firstPlace.setBounds(600, 120, 150, 80);
		firstPlace.setFont(new Font("Serif", Font.BOLD, 50));
		firstPlace.setForeground(Color.white);
		menuPanel.add(firstPlace);
		
		secondPlace.setText(Second);
		secondPlace.setBounds(600, 270, 150, 80);
		secondPlace.setFont(new Font("Serif", Font.BOLD, 50));
		secondPlace.setForeground(Color.white);
		menuPanel.add(secondPlace);
		
		thirdPlace.setText(Third);
		thirdPlace.setBounds(600, 420, 150, 80);
		thirdPlace.setFont(new Font("Serif", Font.BOLD, 50));
		thirdPlace.setForeground(Color.white);
		menuPanel.add(thirdPlace);
		
		fourthPlace.setText(Fourth);
		fourthPlace.setBounds(600, 570, 150, 80);
		fourthPlace.setFont(new Font("Serif", Font.BOLD, 50));
		fourthPlace.setForeground(Color.white);
		menuPanel.add(fourthPlace);
		
		// settings for the menu panel and add it to the frame
		menuPanel.setLayout(null);
		menuPanel.setBounds(0, 0, State.ScreenWidth, State.ScreenHeight);
		menuPanel.setBackground(Color.black);
		menuPanel.setOpaque(true);
		add(menuPanel);
		
		// set the required informations for all the JComponents
		FirstPlace.setBounds(50, 100, 600, 100);
		FirstPlace.setFont(new Font("Serif", Font.BOLD, 50));
		FirstPlace.setForeground(Color.red);
		
		SecondPlace.setBounds(50, 250, 600, 100);
		SecondPlace.setForeground(Color.green);
		SecondPlace.setFont(new Font("Serif", Font.BOLD, 50));
		
		ThirdPlace.setBounds(50, 400, 600, 100);
		ThirdPlace.setForeground(Color.blue);
		ThirdPlace.setFont(new Font("Serif", Font.BOLD, 50));
		
		FourthPlace.setBounds(50, 550, 600, 100);
		FourthPlace.setForeground(Color.yellow);
		FourthPlace.setFont(new Font("Serif", Font.BOLD, 50));
		
		startButton.setBounds(800, 200, 197, 80);
		startButton.addActionListener(this);
		ExitButton.setBounds(800, 470, 197, 80);
		ExitButton.addActionListener(this);
		
		// places the JComponents to the panel
		menuPanel.add(FirstPlace);
		menuPanel.add(SecondPlace);
		menuPanel.add(ThirdPlace);
		menuPanel.add(FourthPlace);
		
		menuPanel.add(startButton);
		menuPanel.add(ExitButton);
	}
	//the actionPerformed method to checks the button when you press it
	@Override
	public void actionPerformed(ActionEvent event) {
		//if it clicks the startbutton, it will opens the type state
		if (event.getSource().equals(startButton)) {
			new TypeState();
			this.dispose();
		}
		//else if it is the exit button, it will close the game
		else if (event.getSource().equals(ExitButton)) {
			System.exit(0);
		}
		
	}

}
