package game.util;

public class Vector3d {

	private double x, y, z;

	public Vector3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3d(Vector3d v) {
		this(v.x, v.y, v.z);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getMagnitude() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public void translate(Vector3d v) {
		x += v.x;
		y += v.y;
		z += v.z;
	}

	public void normalise() {
		if (getMagnitude() == 0) return;
		double factor = 1 / getMagnitude();
		x *= factor;
		y *= factor;
		z *= factor;
	}

	public static Vector3d add(Vector3d v1, Vector3d v2) {
		return new Vector3d(v1.x + v2.x, v1.y + v2.y,
				v1.z + v2.z);
	}

	public static Vector3d multiply(Vector3d v, double a) {
		return new Vector3d(a * v.x, a * v.y, a * v.z);
	}

}
