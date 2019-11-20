package utility;

import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JLabel;

public class PathTrackingButton {

	private JLabel label;
	private LinkedList<String> track;
	
	public PathTrackingButton(JLabel label, LinkedList<String> track) {
		
		this.label = label;
		this.track = track;
		
	}

	public JLabel getLabel() {
		return label;
	}

	public void setLabel(JLabel label) {
		this.label = label;
	}

	public LinkedList<String> getTrack() {
		return track;
	}

	public void setTrack(LinkedList<String> track) {
		this.track = track;
	}
	
}


