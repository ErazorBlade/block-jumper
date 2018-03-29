package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import game.input.Keyboard;
import game.input.Mouse;
import game.level.SuperLevel;
import game.level.TitleScreenLevel;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static int height = 720;
	public static int width = height * 16 / 9;

	public static final int updatesPerSecond = 60;

	private Keyboard key;
	private Mouse mouse;

	private Thread thread;
	public JFrame frame;
	private SuperLevel superLevel;
	private boolean running = false;

	public Game(SuperLevel superLevel) {
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);

		frame = new JFrame();

		this.superLevel = superLevel;

		frame.setResizable(false);
		frame.setTitle("Block Jumper");
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(
				JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setCursor(frame.getToolkit()
				.createCustomCursor(new BufferedImage(1, 1,
						BufferedImage.TYPE_INT_ARGB),
						new Point(), null));
		frame.setVisible(true);

		key = new Keyboard(frame);
		addKeyListener(key);

		mouse = new Mouse(frame);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);

		requestFocus();
	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this, "game_loop");
		thread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / updatesPerSecond;
		double delta = 0;
		int updates = 0;
		int frames = 0;
		requestFocus();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;

			if (System.currentTimeMillis()
					- timer >= 1000) {
				timer += 1000;
				System.out.println("ups: " + updates
						+ ", fps: " + frames);
				frame.setTitle("Block Jumper - ups: "
						+ updates + ", fps: " + frames);
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}

	public void update() {
		if (!Keyboard.paused) superLevel.update();
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		{
			//Graphics here
			g.setColor(new Color(0xffffff));
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(new Color(0x000000));
			superLevel.render(g);
		}
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		SuperLevel superLevel = new SuperLevel(
				new TitleScreenLevel());
		Game game = new Game(superLevel);
		game.start();
	}

}
