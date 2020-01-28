package utility;

import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JLabel;

import objects.Tile;

public class PathTrackingButton {

	private JLabel label;
	private LinkedList<String> track;
	private Tile tile;
	
	public PathTrackingButton(JLabel label, LinkedList<String> track, Tile tile) {
		
		this.label = label;
		this.track = track;
		this.tile = tile;
		
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

	public Tile getTile() {
		return tile;
	}

	public void setTile(Tile tile) {
		this.tile = tile;
	}
	
}


