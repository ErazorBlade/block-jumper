package game.collision;

import game.util.Axis;
import game.util.Vector3d;

public class Collision {

	private BoxCollider collider;
	private double translationScale;
	private Axis axis;
	private Vector3d impactVector;

	public Collision(BoxCollider collider,
			double translationScale, Axis axis,
			Vector3d impactVector) {
		this.collider = collider;
		this.translationScale = translationScale;
		this.axis = axis;
		this.impactVector = impactVector;
	}

	public BoxCollider getCollider() {
		return collider;
	}

	public double getTranslationScale() {
		return translationScale;
	}

	public Axis getAxis() {
		return axis;
	}

	public Vector3d getImpactVector() {
		return impactVector;
	}

}
