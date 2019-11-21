package states;

import java.awt.Color;
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
		setSize(1250, 850);
		setTitle("End");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void init() {
		
		//setting the labels and panel name 
		menuPanel = new JPanel();
		ExitButton = new JButton("Exit"); 
		startButton = new JButton("Restart");
		
		FirstPlace = new JLabel("First Place: Player ");
		SecondPlace = new JLabel("Second Place: Player ");
		ThirdPlace = new JLabel("Third Place: Player ");
		FourthPlace = new JLabel("Fourth Place: Player ");
	
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
		firstPlace.setBounds(500, 100, 200, 80);
		menuPanel.add(firstPlace);
		
		secondPlace.setText(Second);
		secondPlace.setBounds(500, 250, 200, 80);
		menuPanel.add(secondPlace);
		
		thirdPlace.setText(Third);
		thirdPlace.setBounds(500, 400, 200, 80);
		menuPanel.add(thirdPlace);
		
		fourthPlace.setText(Fourth);
		fourthPlace.setBounds(500, 550, 200, 80);
		menuPanel.add(fourthPlace);
		
		// settings for the menu panel and add it to the frame
		menuPanel.setLayout(null);
		menuPanel.setBounds(0, 0, State.ScreenWidth, State.ScreenHeight);
		menuPanel.setBackground(Color.black);
		menuPanel.setOpaque(true);
		add(menuPanel);
		
		// set the required informations for all the JComponents
		FirstPlace.setBounds(200, 100, 200, 80);
		SecondPlace.setBounds(200, 250, 200, 80);
		ThirdPlace.setBounds(200, 400, 200, 80);
		FourthPlace.setBounds(200, 550, 200, 80);
		
		startButton.setBounds(800, 150, 197, 80);
		startButton.addActionListener(this);
		ExitButton.setBounds(800, 450, 197, 80);
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
