package game.util;

public class Matrix3x3d {

	private double[] values;

	public Matrix3x3d(double[] values) {
		if (values.length != 9)
			throw new IllegalArgumentException(
					"The length of the parameter \"values\" must be 9!");
		this.values = values;
	}

	public double getValue(int row, int column) {
		return values[column + row * 3];
	}

	public void setValue(int row, int column,
			double value) {
		values[column + row * 3] = value;
	}

	public Vector3d transform(Vector3d v) {
		double x = v.getX();
		double y = v.getY();
		double z = v.getZ();
		double xn = x * getValue(0, 0) + y * getValue(0, 1)
				+ z * getValue(0, 2);
		double yn = x * getValue(1, 0) + y * getValue(1, 1)
				+ z * getValue(1, 2);
		double zn = x * getValue(2, 0) + y * getValue(2, 1)
				+ z * getValue(2, 2);
		return new Vector3d(xn, yn, zn);
	}

	public static Matrix3x3d create3dRotationMatrix(
			Axis axis, double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		if (axis == Axis.X) {
			return new Matrix3x3d(new double[] { 1, 0, 0, 0,
					cos, sin, 0, -sin, cos });
		}
		if (axis == Axis.Y) {
			return new Matrix3x3d(new double[] { cos, 0,
					-sin, 0, 1, 0, sin, 0, cos });
		}
		if (axis == Axis.Z) {
			return new Matrix3x3d(new double[] { cos, sin,
					0, -sin, cos, 0, 0, 0, 1 });
		}
		return null;
	}

	public static Matrix3x3d axisRemovalMatrix(Axis a) {
		if (a == Axis.X) return new Matrix3x3d(
				new double[] { 0, 0, 0, 0, 1, 0, 0, 0, 1 });
		if (a == Axis.Y) return new Matrix3x3d(
				new double[] { 1, 0, 0, 0, 0, 0, 0, 0, 1 });
		if (a == Axis.Z) return new Matrix3x3d(
				new double[] { 1, 0, 0, 0, 1, 0, 0, 0, 0 });
		return null;
	}

}
