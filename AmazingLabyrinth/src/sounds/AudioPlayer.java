package sounds;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/*
 * Author: Alan Sun
 * 
 * the audio player class plays a sound effect from a file
 */
public class AudioPlayer {

	// Variable for the music, stored as a clip
	private static Clip clip;
	private static AudioInputStream audioInput;
	
	public static boolean mute;

	// Methods that create/play the music, takes in the location of the music
	public static void playAudio(String audioLocation) {

		// if audio is not muted, then play sound
		if(!mute) {
			
			// if music location is found, play it. else catch it and print it is not found
			try {
	
				// Make a file that stores the location of the music
				File Sound = new File(audioLocation);
	
				// allow AudioInputStream to store the sound file
				audioInput = AudioSystem.getAudioInputStream(Sound);
	
				// the actual audio clip is received from the AudioInputStream
				clip = AudioSystem.getClip();
	
				// load the audio using clip
				clip.open(audioInput);
	
				// start the audio
				clip.start();
	
			} catch (Exception ex) {
	
				// if music file is not found, print it
				ex.printStackTrace();
	
			}
		
		}

	}

}
