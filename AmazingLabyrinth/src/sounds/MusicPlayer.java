package sounds;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/*
 * Author: Alan Sun
 * 
 * the music player class plays and stops a music loaded from file
 */
public class MusicPlayer {

	// Variable for the music, stored as a clip
	private static Clip clip;
	private static AudioInputStream audioInput;

	// Methods that create/play the music, takes in the location of the music
	public static void playMusic(String musicLocation) {

		// if music location is found, play it. else catch it and print it is not found
		try {

			// Make a file that stores the location of the music
			File Sound = new File(musicLocation);

			// allow AudioInputStream to store the sound file
			audioInput = AudioSystem.getAudioInputStream(Sound);

			// the actual audio clip is received from the AudioInputStream
			clip = AudioSystem.getClip();

			// load the audio using clip
			clip.open(audioInput);

			// allows the music to loop continuously once it finishes
			clip.loop(Clip.LOOP_CONTINUOUSLY);

			// start the music
			clip.start();

		} catch (Exception ex) {

			// if music file is not found, print it
			System.out.println("music file is not found");

		}

	}

	// Method to stop the music
	public static void stopMusic() {
		// Stop the music
		clip.stop();

	}

}
