package game.input;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

public class Mouse
		implements MouseListener, MouseMotionListener {

	public static int x, y;

	public static double sensitivity = 0.003;
	public static double xRad, yRad;

	public static boolean wasPaused;

	private JFrame frame;
	private Robot robot;

	public Mouse(JFrame frame) {
		wasPaused = true;
		this.frame = frame;
		x = 0;
		y = 0;
		xRad = 0;
		yRad = 0;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		robot.mouseMove(frame.getX() + frame.getWidth() / 2,
				frame.getY() + frame.getHeight() / 2);
	}

	private void movement(MouseEvent e) {
		Point p = e.getLocationOnScreen();

		x = e.getX();
		y = e.getY();

		xRad += sensitivity * (p.getX()
				- (frame.getX() + frame.getWidth() / 2));
		if (xRad >= 2 * Math.PI || xRad < 0)
			xRad %= 2 * Math.PI;

		yRad += sensitivity * (p.getY()
				- (frame.getY() + frame.getHeight() / 2));
		yRad = Math.min(yRad, Math.PI / 2);
		yRad = Math.max(yRad, -Math.PI / 2);

		robot.mouseMove(frame.getX() + frame.getWidth() / 2,
				frame.getY() + frame.getHeight() / 2);
	}

	public void mouseDragged(MouseEvent e) {
		if (!Keyboard.paused) movement(e);
	}

	public void mouseMoved(MouseEvent e) {
		if (!Keyboard.paused) movement(e);
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

}
