package game.level;

import java.awt.Font;
import java.awt.Graphics;

import game.util.Vector3d;

public class TitleScreenLevel extends Level {

	public TitleScreenLevel() {
		super(new Vector3d(0, 0, 0));
	}

	public void restart() {
		superLevel.setLevel(new MainLevel());
	}

	public void update() {
	}

	public void render(Graphics g) {
		g.setFont(new Font("Default", Font.PLAIN, 50));
		g.drawString("Block Jumper", 500, 100);

		g.setFont(new Font("Default", Font.PLAIN, 30));
		g.drawString("Controls:", 520, 270);
		g.drawString("WASD:   move", 530, 310);
		g.drawString("Space:    jump", 530, 340);
		g.drawString("Shift:       sprint", 530, 370);
		g.drawString("R:            restart level", 530,
				400);
		g.drawString("Escape:  pause and free corsor", 530,
				430);

		g.setFont(new Font("Default", Font.PLAIN, 40));
		g.drawString("Press R to start!", 500, 600);
	}

	public Level newInstance() {
		return null;
	}

}
