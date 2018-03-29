package game.entity.environment;

import java.awt.Graphics;

import game.collision.BoxCollider;
import game.entity.Entity;
import game.graphics.Camera;
import game.level.Level;
import game.util.Line3d;
import game.util.Vector3d;

public class Platform implements Entity {

	private Line3d[] lines;

	private BoxCollider collider;

	public Platform(Vector3d topCentrePosition,
			double xLength, double zLength, int xTiles,
			int zTiles) {
		Vector3d centrePosition = Vector3d.add(
				topCentrePosition,
				new Vector3d(0, -0.05, 0));
		collider = new BoxCollider(centrePosition,
				new Vector3d(xLength, 0.1, zLength));

		double x1 = collider.getCornerPosition().getX();
		double xStep = collider.getSize().getX() / xTiles;
		double x2 = x1 + collider.getSize().getX();
		double y = collider.getCornerPosition().getY();
		double z1 = collider.getCornerPosition().getZ();
		double zStep = collider.getSize().getZ() / zTiles;
		double z2 = z1 + collider.getSize().getZ();

		lines = new Line3d[xTiles + 1 + zTiles + 1];
		for (int xTile = 0; xTile <= xTiles; xTile++) {
			Vector3d v1 = new Vector3d(x1 + xStep * xTile,
					y, z1);
			Vector3d v2 = new Vector3d(x1 + xStep * xTile,
					y, z2);
			lines[xTile] = new Line3d(v1, v2);
		}
		for (int zTile = 0; zTile <= zTiles; zTile++) {
			Vector3d v1 = new Vector3d(x1, y,
					z1 + zStep * zTile);
			Vector3d v2 = new Vector3d(x2, y,
					z1 + zStep * zTile);
			lines[xTiles + 1 + zTile] = new Line3d(v1, v2);
		}
	}

	public void init(Level level) {
		collider.init(level);
	}

	public void update() {
	}

	public void render(Camera c, Graphics g) {
		for (int i = 0; i < lines.length; i++) {
			c.renderLine(lines[i], g);
		}
	}

	public BoxCollider getCollider() {
		return collider;
	}

	private boolean removed = false;

	public void remove() {
		removed = true;
	}

	public boolean isRemoved() {
		return removed;
	}

}
