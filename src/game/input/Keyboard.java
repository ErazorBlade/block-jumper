package game.input;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Keyboard implements KeyListener {

	private JFrame frame;

	public static boolean paused;
	public static boolean reset;
	private static boolean resetReady;
	public static boolean jump;
	private static boolean jumpReady;

	public static boolean shift;
	public static boolean w, s, a, d;
	public static boolean pgUp, pgDown;

	private Robot robot;

	public Keyboard(JFrame frame) {
		this.frame = frame;

		paused = false;

		reset = false;
		resetReady = true;

		jump = false;
		jumpReady = true;

		shift = false;

		w = false;
		a = false;
		s = false;
		d = false;

		pgUp = false;
		pgDown = false;

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			paused = !paused;
			if (paused) {
				frame.setCursor(null);
			} else {
				frame.setCursor(frame.getToolkit()
						.createCustomCursor(
								new BufferedImage(1, 1,
										BufferedImage.TYPE_INT_ARGB),
								new Point(), null));
				robot.mouseMove(
						frame.getX() + frame.getWidth() / 2,
						frame.getY()
								+ frame.getHeight() / 2);
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_R && resetReady) {
			reset = true;
			resetReady = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_SPACE
				&& jumpReady) {
			jump = true;
			jumpReady = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_SHIFT)
			shift = true;

		if (e.getKeyCode() == KeyEvent.VK_W) w = true;
		if (e.getKeyCode() == KeyEvent.VK_S) s = true;
		if (e.getKeyCode() == KeyEvent.VK_A) a = true;
		if (e.getKeyCode() == KeyEvent.VK_D) d = true;

		if (e.getKeyCode() == KeyEvent.VK_PAGE_UP)
			pgUp = true;
		if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
			pgDown = true;
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			jump = false;
			jumpReady = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_R)
			resetReady = true;

		if (e.getKeyCode() == KeyEvent.VK_SHIFT)
			shift = false;

		if (e.getKeyCode() == KeyEvent.VK_W) w = false;
		if (e.getKeyCode() == KeyEvent.VK_S) s = false;
		if (e.getKeyCode() == KeyEvent.VK_A) a = false;
		if (e.getKeyCode() == KeyEvent.VK_D) d = false;

		if (e.getKeyCode() == KeyEvent.VK_PAGE_UP)
			pgUp = false;
		if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
			pgDown = false;
	}

	public void keyTyped(KeyEvent e) {
	}

}
