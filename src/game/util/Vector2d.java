package game.util;

public class Vector2d {

	private double x, y;

	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getMagnitude() {
		return Math.sqrt(x * x + y * y);
	}

	public void translate(Vector2d v) {
		this.x += v.x;
		this.y += v.y;
	}

	public static Vector2d add(Vector2d v1, Vector2d v2) {
		return new Vector2d(v1.x + v2.x, v1.y + v2.y);
	}

	public static Vector2d multiply(Vector2d v, double a) {
		return new Vector2d(a * v.x, a * v.y);
	}

}
