package game.entity;

import java.awt.Graphics;

import game.collision.BoxCollider;
import game.graphics.Camera;
import game.level.Level;

public interface Entity {

	public void init(Level level);

	public void remove();

	public boolean isRemoved();

	public void update();

	public void render(Camera c, Graphics g);

	public BoxCollider getCollider();

}
