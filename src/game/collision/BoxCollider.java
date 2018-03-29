package game.collision;

import java.util.ArrayList;
import java.util.List;

import game.entity.Entity;
import game.level.Level;
import game.util.Axis;
import game.util.Matrix3x3d;
import game.util.Vector3d;

public class BoxCollider {

	private Vector3d cornerPosition, size;

	private Level level;

	public BoxCollider(Vector3d centrePosition,
			Vector3d size) {
		if (size.getX() < 0) size.setX(-size.getX());
		if (size.getY() < 0) size.setY(-size.getY());
		if (size.getZ() < 0) size.setZ(-size.getZ());
		this.size = size;

		cornerPosition = Vector3d.add(centrePosition,
				Vector3d.multiply(size, -0.5));
	}

	public void init(Level level) {
		this.level = level;
	}

	public Vector3d getCornerPosition() {
		return cornerPosition;
	}

	public void setCornerPosition(Vector3d cornerPosition) {
		this.cornerPosition = cornerPosition;
	}

	public Vector3d getCentrePosition() {
		return Vector3d.add(cornerPosition,
				Vector3d.multiply(size, 0.5));
	}

	public void setCentrePosition(Vector3d centrePosition) {
		this.cornerPosition = Vector3d.add(centrePosition,
				Vector3d.multiply(size, -0.5));
	}

	public Vector3d getSize() {
		return size;
	}

	public void setSize(Vector3d size) {
		Vector3d centre = getCentrePosition();
		this.size = size;
		this.setCentrePosition(centre);
	}

	public Vector3d move(Vector3d translation) {
		List<Collision> collisionsUnsorted = new ArrayList<Collision>();
		List<Entity> entities = level.getEntities();
		for (int i = 0; i < entities.size(); i++) {
			Collision collision = collisionDetection(
					translation,
					entities.get(i).getCollider());
			if (collision == null) continue;
			collisionsUnsorted.add(collision);
		}
		List<Collision> collisions = new ArrayList<Collision>();
		while (!collisionsUnsorted.isEmpty()) {
			int smallestIndex = 0;
			double smallestScale = collisionsUnsorted.get(0)
					.getTranslationScale();
			for (int i = 1; i < collisionsUnsorted
					.size(); i++) {
				if (collisionsUnsorted.get(i)
						.getTranslationScale() < smallestScale) {
					smallestIndex = i;
					smallestScale = collisionsUnsorted
							.get(i).getTranslationScale();
				}
			}
			collisions.add(collisionsUnsorted
					.remove(smallestIndex));
		}
		if (collisions.isEmpty())
			cornerPosition.translate(translation);
		//double translationScale = 1; // unstable
		for (int i = 0; i < collisions.size(); i++) {
			collisions.set(i, collisionDetection(
					translation,
					collisions.get(i).getCollider()));
			Collision collision = collisions.get(i);
			translation = collisionReaction(translation,
					collision);
			//translationScale -= collision.getTranslationScale(); // unstable
		}
		//cornerPosition.translate(Vector3d.multiply(translation, translationScale)); // unstable
		return translation;
	}

	private Collision collisionDetection(
			Vector3d translation, BoxCollider collider) {
		{ // Checking if a collision will occur
			Vector3d v1min, v1max, v2min, v2max;
			v1min = Vector3d.add(cornerPosition,
					translation);
			v1max = Vector3d.add(
					Vector3d.add(cornerPosition, size),
					translation);
			v2min = new Vector3d(collider.cornerPosition);
			v2max = Vector3d.add(collider.cornerPosition,
					collider.size);
			double x1min = v1min.getX();
			double x1max = v1max.getX();
			double x2min = v2min.getX();
			double x2max = v2max.getX();
			double y1min = v1min.getY();
			double y1max = v1max.getY();
			double y2min = v2min.getY();
			double y2max = v2max.getY();
			double z1min = v1min.getZ();
			double z1max = v1max.getZ();
			double z2min = v2min.getZ();
			double z2max = v2max.getZ();
			boolean cx = collision1Dimensional(x1min, x1max,
					x2min, x2max);
			boolean cy = collision1Dimensional(y1min, y1max,
					y2min, y2max);
			boolean cz = collision1Dimensional(z1min, z1max,
					z2min, z2max);
			if (!(cx && cy && cz)) return null;
		}
		double dx, dy, dz;
		{ // Calculating the distance
			Vector3d v1min, v1max, v2min, v2max;
			v1min = new Vector3d(cornerPosition);
			v1max = Vector3d.add(cornerPosition, size);
			v2min = new Vector3d(collider.cornerPosition);
			v2max = Vector3d.add(collider.cornerPosition,
					collider.size);
			double x1min = v1min.getX();
			double x1max = v1max.getX();
			double x2min = v2min.getX();
			double x2max = v2max.getX();
			double y1min = v1min.getY();
			double y1max = v1max.getY();
			double y2min = v2min.getY();
			double y2max = v2max.getY();
			double z1min = v1min.getZ();
			double z1max = v1max.getZ();
			double z2min = v2min.getZ();
			double z2max = v2max.getZ();
			boolean cx = collision1Dimensional(x1min, x1max,
					x2min, x2max); // Already colliding?
			boolean cy = collision1Dimensional(y1min, y1max,
					y2min, y2max);
			boolean cz = collision1Dimensional(z1min, z1max,
					z2min, z2max);
			if (cx && cy && cz) return null; // Already inside other collider

			if (cx) dx = 0;
			else if (translation.getX() > 0)
				dx = x2min - x1max;
			else dx = x2max - x1min;

			if (cy) dy = 0;
			else if (translation.getY() > 0)
				dy = y2min - y1max;
			else dy = y2max - y1min;

			if (cz) dz = 0;
			else if (translation.getZ() > 0)
				dz = z2min - z1max;
			else dz = z2max - z1min;
		}
		double nx = dx / translation.getX();
		double ny = dy / translation.getY();
		double nz = dz / translation.getZ();

		if (nx != nx) nx = -1; // (x != x) is true if x = NaN
		if (ny != ny) ny = -1;
		if (nz != nz) nz = -1;

		Axis axis;
		double n;
		if (nx > Math.max(ny, nz)) {
			axis = Axis.X;
			n = nx;
		} else if (ny > nz) {
			axis = Axis.Y;
			n = ny;
		} else {
			axis = Axis.Z;
			n = nz;
		}

		Vector3d impactVector = Vector3d
				.multiply(translation, n);

		return new Collision(collider, n, axis,
				impactVector);
	}

	private boolean collision1Dimensional(double min1,
			double max1, double min2, double max2) {
		if (min1 < min2)
			return (max1 <= min2) ^ (min1 <= max2); // <= instead of < fixed the colliding-and-stopping-and-then-falling-through-the-ground issue
		else return (max2 <= min1) ^ (min2 <= max1);
	}

	private Vector3d collisionReaction(Vector3d translation,
			Collision collision) {
		this.cornerPosition
				.translate(collision.getImpactVector());
		return Matrix3x3d
				.axisRemovalMatrix(collision.getAxis())
				.transform(translation);
	}

}
