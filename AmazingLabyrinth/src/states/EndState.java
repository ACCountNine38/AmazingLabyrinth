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

public class EndState extends JFrame implements ActionListener {
	
	private JPanel menuPanel;
	private JButton startButton;
	private JButton ExitButton;
	
	private ArrayList<Integer> Winning;
	
	private JLabel FirstPlace;
	private JLabel SecondPlace;
	private JLabel ThirdPlace;
	private JLabel FourthPlace;
	
	JLabel firstPlace = new JLabel();
	JLabel secondPlace = new JLabel();
	JLabel thirdPlace = new JLabel();
	JLabel fourthPlace = new JLabel();
	
	//create new screen with both AI and 4 player button options
	public EndState(ArrayList<Integer> winner) {
		
		this.Winning = winner;
		System.out.println("in constrotor");
		
		init();
		addJComponents();
		
		setVisible(true);
		
		setSize(1250, 850);
		setTitle("End");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void init() {
				
		menuPanel = new JPanel();
		ExitButton = new JButton("Exit"); 
		startButton = new JButton("Restart");
		
		FirstPlace = new JLabel("First Place: Player ");
		SecondPlace = new JLabel("Second Place: Player ");
		ThirdPlace = new JLabel("Third Place: Player ");
		FourthPlace = new JLabel("Fourth Place: Player ");
	
				
		MusicPlayer.playMusic("audio/menuTheme.wav");
		
	}

	public void addJComponents() {
		
		String First = Winning.get(0).toString();
		String Second = Winning.get(1).toString();
		String Third = Winning.get(2).toString();
		String Fourth = Winning.get(3).toString();
		
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
		
		// settings for the score panel and add it to the frame
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
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(startButton)) {
			new TypeState();
			this.dispose();
		}
		else if (event.getSource().equals(ExitButton)) {
			System.exit(0);
		}
		
	}

}
