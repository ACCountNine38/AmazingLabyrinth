package utility;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import states.State;

/*
 * Author: Alan Sun
 * 
 * This class serves as a tool that will be used in most of the classes
 * Contains methods that are used in all frames
 * Able to setup a frame and change the cursor icon
 */
public class CustomizationTool {

	// method that sets up a frame
	public static void frameSetup(JFrame frame) {

		// set the name and size of the frame, and now allowing user to resize
		frame.setTitle("Amazing Labyrinth");
		frame.setSize(State.ScreenWidth, State.ScreenHeight);
		frame.setResizable(false);

		// disables auto layout, center program, exit frame when programcloses
		frame.setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// set frame to appear on screen
		frame.setVisible(true);

	}

	// method that changes the cursor icon
	public static void customCursor(JFrame frame) {

		// using the java TookKit to change cursors
		Toolkit toolkit = Toolkit.getDefaultToolkit();

		// load an image using ToolKit
		Image mouse = toolkit.getImage("images/cursor.png").getScaledInstance(25, 40, 0);

		// set the cursor icon giving a new image, point, and name
		frame.setCursor(toolkit.createCustomCursor(mouse, new Point(0, 0), "Custom Cursor"));

	}
	
	// http://javazquez.com/juan/2008/07/11/rotating-java-images/
    static private ImageIcon rotateImageIcon(ImageIcon picture) {
        // FOR YOU ...
        int w = picture.getIconWidth();
        int h = picture.getIconHeight();
        int type = BufferedImage.TYPE_INT_RGB;  // other options, see api
        BufferedImage image = new BufferedImage(h, w, type);
        Graphics2D g2 = image.createGraphics();
        double x = (h - w)/2.0;
        double y = (w - h)/2.0;
        AffineTransform at = AffineTransform.getTranslateInstance(x, y);
        at.rotate(Math.toRadians(Math.PI/2), w/2.0, h/2.0);
        g2.drawImage(picture.getImage(), at, null);
        g2.dispose();
        picture = new ImageIcon(image);
 
        return picture;
    }

}
