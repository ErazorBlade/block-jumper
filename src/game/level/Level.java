package game.level;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import game.entity.Entity;
import game.entity.Player;
import game.input.Mouse;
import game.util.Vector3d;

public abstract class Level {

	protected SuperLevel superLevel;
	protected Player player;
	protected List<Entity> entities = new ArrayList<Entity>();

	public Level(Vector3d playerPosition) {
		Mouse.yRad = 0;
		Mouse.xRad = 0;
		player = new Player(playerPosition, 0);
		player.init(this);
	}

	public void init(SuperLevel superLevel) {
		this.superLevel = superLevel;
	}

	public void restart() {
		superLevel.setLevel(newInstance());
	}

	public abstract Level newInstance();

	public void update() {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).isRemoved()) {
				entities.remove(i);
				i--;
				continue;
			}
			entities.get(i).update();
		}
		player.update();
	}

	public void render(Graphics g) {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render(player.camera, g);
		}
		player.render(player.camera, g);
	}

	public void add(Entity e) {
		e.init(this);
		entities.add(e);
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public Player getPlayer() {
		return player;
	}

}
