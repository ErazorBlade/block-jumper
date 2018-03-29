package game.level;

import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;

import game.Game;
import game.entity.Entity;
import game.entity.environment.Block;
import game.entity.environment.Platform;
import game.util.Vector3d;

public class MainLevel extends Level {

	private Random random;
	private double xLastBlock = 0;
	private double yLastBlock = 0;
	private double zForNextBlock = 0;
	private Entity[] blocks;
	private int blockIndex = 0;

	private int score = -1;
	private static int highScore = 0;

	private boolean startPlatformExists = false;

	public MainLevel() {
		super(new Vector3d(0, 2, 0));

		random = new Random();

		add(new Platform(new Vector3d(0, 0, 0), 10, 10, 10,
				10));
		startPlatformExists = true;

		blocks = new Entity[4];
		Entity block = new Block(
				new Vector3d(random.nextDouble() * 10 - 5,
						0, 17),
				new Vector3d(2, 1, 6));
		add(block);
		blocks[blockIndex] = block;
		blockIndex = (blockIndex + 1) & 3;
	}

	public Level newInstance() {
		return new MainLevel();
	}

	public void update() {
		if (startPlatformExists
				&& player.position.getZ() >= 10) {
			entities.get(0).remove();
			startPlatformExists = false;
		}
		if (player.position.getY() < -10 + yLastBlock)
			restart();
		if (player.position.getZ() >= zForNextBlock) {
			score += 1;
			if (score > highScore) highScore = score;
			zForNextBlock += 17;
			Entity block = generateNewBlock(
					xLastBlock = xLastBlock
							+ random.nextDouble() * 8 - 4,
					yLastBlock = yLastBlock + 1,
					zForNextBlock + 17);
			add(block);
			if (blocks[blockIndex] != null)
				blocks[blockIndex].remove();
			blocks[blockIndex] = block;
			blockIndex = (blockIndex + 1) & 3;
		}
		super.update();
	}

	public void render(Graphics g) {
		g.setFont(new Font("Default", Font.PLAIN, 20));
		g.drawString("Score: " + score, 10,
				Game.height - 30);
		g.drawString("Highscore: " + highScore, 10,
				Game.height - 10);
		super.render(g);
	}

	private Entity generateNewBlock(double x, double y,
			double z) {
		Entity block = new Block(new Vector3d(x, y, z),
				new Vector3d(2, 1, 6));
		return block;
	}

}
