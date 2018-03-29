package game.graphics;

import game.Game;
import game.util.Axis;
import game.util.Line2d;
import game.util.Line3d;
import game.util.Matrix3x3d;
import game.util.Vector2d;
import game.util.Vector3d;

public class Projector {

	private double lastYRotation, lastXRotation;
	private Matrix3x3d lastYRotationMatrix,
			lastXRotationMatrix;

	private static double zMin = 0.0001;

	public Projector() {
		lastYRotation = 0;
		lastXRotation = 0;
		lastYRotationMatrix = null;
		lastXRotationMatrix = null;
	}

	public Line2d fullProjection(Line3d worldCoords,
			Vector3d cameraPosition, double yRotation,
			double xRotation, double f) {
		Vector3d p = worldToCameraProjection(worldCoords.v1,
				cameraPosition, yRotation, xRotation);
		Vector3d q = worldToCameraProjection(worldCoords.v2,
				cameraPosition, yRotation, xRotation);
		if (p.getZ() <= zMin && q.getZ() <= zMin)
			return null; // both vectors behind camera
		if (p.getZ() <= zMin || q.getZ() <= zMin) { // only one vector behind camera
			if (q.getZ() <= zMin) {
				Vector3d temp = p;
				p = q;
				q = temp;
			}
			double n = (zMin - p.getZ())
					/ (q.getZ() - p.getZ());
			Vector3d pq = Vector3d.add(q,
					Vector3d.multiply(p, -1));
			p = Vector3d.add(p, Vector3d.multiply(pq, n));
		}
		Vector2d pf = cameraToFilmProjection(p, f);
		Vector2d qf = cameraToFilmProjection(q, f);
		Vector2d pp = filmToPixelProjection(pf);
		Vector2d qp = filmToPixelProjection(qf);
		return new Line2d(pp, qp);
	}

	private Vector3d worldToCameraProjection(
			Vector3d worldCoords, Vector3d cameraPosition,
			double yRotation, double xRotation) {
		worldCoords = Vector3d.add(worldCoords,
				Vector3d.multiply(cameraPosition, -1));

		if (yRotation != 0) {
			Matrix3x3d yRotationMatrix;
			if (yRotation == lastYRotation)
				yRotationMatrix = lastYRotationMatrix;
			else yRotationMatrix = Matrix3x3d
					.create3dRotationMatrix(Axis.Y,
							yRotation);

			worldCoords = yRotationMatrix
					.transform(worldCoords);
		}

		if (xRotation != 0) {
			Matrix3x3d xRotationMatrix;
			if (xRotation == lastXRotation)
				xRotationMatrix = lastXRotationMatrix;
			else xRotationMatrix = Matrix3x3d
					.create3dRotationMatrix(Axis.X,
							xRotation);

			worldCoords = xRotationMatrix
					.transform(worldCoords);
		}

		return worldCoords;
	}

	private Vector2d cameraToFilmProjection(
			Vector3d cameraCoords, double f) {
		double x = cameraCoords.getX();
		double y = cameraCoords.getY();
		double z = cameraCoords.getZ();
		double nx = f * x / z;
		double ny = f * y / z;
		return new Vector2d(nx, ny);
	}

	private Vector2d filmToPixelProjection(
			Vector2d filmCoords) {
		double x = filmCoords.getX();
		double y = filmCoords.getY();
		double nx = x + Game.width / 2;
		double ny = -y + Game.height / 2;
		return new Vector2d(nx, ny);
	}

}
