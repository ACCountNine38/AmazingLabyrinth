package utility;

import java.util.LinkedList;

import javax.swing.JButton;

public class PathTrackingButton {

	private JButton button;
	private LinkedList<String> track;
	
	public PathTrackingButton(JButton button, LinkedList<String> track) {
		
		this.button = button;
		this.track = track;
		
	}

	public JButton getButton() {
		return button;
	}

	public void setButton(JButton button) {
		this.button = button;
	}

	public LinkedList<String> getTrack() {
		return track;
	}

	public void setTrack(LinkedList<String> track) {
		this.track = track;
	}
	
}
