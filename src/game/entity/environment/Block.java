package game.entity.environment;

import java.awt.Graphics;

import game.collision.BoxCollider;
import game.entity.Entity;
import game.graphics.Camera;
import game.level.Level;
import game.util.Line3d;
import game.util.Vector3d;

public class Block implements Entity {

	private Line3d[] lines;

	private BoxCollider collider;

	public Block(Vector3d topCentrePosition,
			Vector3d size) {
		Vector3d centrePosition = Vector3d.add(
				topCentrePosition,
				new Vector3d(0, -size.getY() / 2, 0));
		collider = new BoxCollider(centrePosition, size);

		Vector3d c1 = collider.getCornerPosition();
		Vector3d c2 = Vector3d.add(c1, collider.getSize());
		Vector3d[] corners = new Vector3d[8];
		for (int i = 0; i < 8; i++) {
			double x, y, z;
			x = c1.getX() * (((i + 4) & 7) / 4)
					+ c2.getX() * ((i & 7) / 4);
			y = c1.getY() * (((i + 2) & 3) / 2)
					+ c2.getY() * ((i & 3) / 2);
			z = c1.getZ() * (((i + 1) & 1) / 1)
					+ c2.getZ() * ((i & 1) / 1);
			corners[i] = new Vector3d(x, y, z);
		}

		lines = new Line3d[12];
		lines[0] = new Line3d(corners[0], corners[1]); // Edges of ths surface with smaller x-coords
		lines[1] = new Line3d(corners[1], corners[3]);
		lines[2] = new Line3d(corners[3], corners[2]);
		lines[3] = new Line3d(corners[2], corners[0]);
		lines[4] = new Line3d(corners[4], corners[5]); // Edges of ths surface with higher x-coords
		lines[5] = new Line3d(corners[5], corners[7]);
		lines[6] = new Line3d(corners[7], corners[6]);
		lines[7] = new Line3d(corners[6], corners[4]);
		lines[8] = new Line3d(corners[0], corners[4]); // x-axis-parallel lines
		lines[9] = new Line3d(corners[1], corners[5]);
		lines[10] = new Line3d(corners[2], corners[6]);
		lines[11] = new Line3d(corners[3], corners[7]);
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
