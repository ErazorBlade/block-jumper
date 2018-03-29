package game.graphics;

import java.awt.Graphics;

import game.util.Axis;
import game.util.Line2d;
import game.util.Line3d;
import game.util.Vector3d;

public class Camera {

	public Vector3d position;

	public double yRotation, xRotation, f;

	private Projector projector;

	public Camera(Vector3d position, double yRotation,
			double xRotation, double f) {
		this.position = position;
		this.yRotation = yRotation;
		this.xRotation = xRotation;
		this.f = f;

		projector = new Projector();
	}

	public void renderLine(Line3d line, Graphics g) {
		Line2d l2 = projectLine(line);
		if (l2 == null) return;
		g.drawLine((int) l2.v1.getX(), (int) l2.v1.getY(),
				(int) l2.v2.getX(), (int) l2.v2.getY());
	}

	private Line2d projectLine(Line3d worldCoords) {
		return projector.fullProjection(worldCoords,
				position, yRotation, xRotation, f);
	}

	public Vector3d getUnitVectorAfterYRotation(Axis axis) {
		double sin = Math.sin(yRotation);
		double cos = Math.cos(yRotation);
		if (axis == Axis.X) {
			return new Vector3d(cos, 0, -sin);
		}
		if (axis == Axis.Y) {
			return new Vector3d(0, 1, 0);
		}
		if (axis == Axis.Z) {
			return new Vector3d(sin, 0, cos);
		}
		return null;
	}

}
