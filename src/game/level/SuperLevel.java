package game.level;

import java.awt.Graphics;

import game.input.Keyboard;

public class SuperLevel {

	private Level level;

	public SuperLevel(Level level) {
		setLevel(level);
	}

	public void setLevel(Level level) {
		this.level = level;
		level.init(this);
	}

	public void update() {
		if (Keyboard.reset) {
			level.restart();
			Keyboard.reset = false;
		}
		level.update();
	}

	public void render(Graphics g) {
		level.render(g);
	}

}
