package game.entity;

import java.awt.Graphics;

import game.collision.BoxCollider;
import game.graphics.Camera;
import game.input.Keyboard;
import game.input.Mouse;
import game.level.Level;
import game.util.Axis;
import game.util.Vector3d;

public class Player implements Entity {

	public Vector3d position;
	private Vector3d velocityVertical, velocityHorizontal;

	private double gravityForce = 0.008;
	private double terminalVertical = 0.4;
	private double accelerationHorizontalOnGround = 0.03;
	private double accelerationHorizontalMidAir = 0.007;
	private double dragHorizontalOnGround = 0.2;
	private double dragHorizontalMidAir = 0.015;
	private double sprintModifier = 1.6;
	private double groundJumpStrength = 0.2;
	private double airJumpStrength = 0.15;

	private boolean onGround;
	private int airJumpsLeft;
	private int airJumpsMax = 0;

	private Vector3d cameraOffset = new Vector3d(0, 1.7, 0);
	private Vector3d colliderCentreOffset = new Vector3d(0,
			0.9, 0);

	private BoxCollider collider;

	public Camera camera;

	public Player(Vector3d pos, int airJumpsMax) {
		dragHorizontalOnGround = 1
				/ (dragHorizontalOnGround + 1);
		dragHorizontalMidAir = 1
				/ (dragHorizontalMidAir + 1);

		position = pos;
		velocityVertical = new Vector3d(0, 0, 0);
		velocityHorizontal = new Vector3d(0, 0, 0);

		onGround = false;
		this.airJumpsMax = airJumpsMax;
		airJumpsLeft = airJumpsMax;

		Vector3d colliderPosition = Vector3d.add(position,
				colliderCentreOffset);
		this.collider = new BoxCollider(colliderPosition,
				new Vector3d(0.9, 1.8, 0.9));

		Vector3d cameraPosition = Vector3d.add(position,
				cameraOffset);
		this.camera = new Camera(cameraPosition, 0, 0, 400);
	}

	public void init(Level level) {
		collider.init(level);
	}

	public void update() {
		camera.yRotation = Mouse.xRad;
		camera.xRotation = Mouse.yRad;

		if (onGround) airJumpsLeft = airJumpsMax;
		if (Keyboard.jump && onGround) {
			velocityVertical.setY(groundJumpStrength);
			Keyboard.jump = false;
		} else if (Keyboard.jump
				&& (onGround || airJumpsLeft > 0)) {
			velocityVertical.setY(airJumpStrength);
			airJumpsLeft--;
		} else {
			velocityVertical.translate(
					new Vector3d(0, -gravityForce, 0));
		}

		velocityHorizontal = Vector3d.multiply(
				velocityHorizontal,
				onGround ? dragHorizontalOnGround
						: dragHorizontalMidAir);
		if (velocityHorizontal.getMagnitude() < 0.001)
			velocityHorizontal = new Vector3d(0, 0, 0);

		Vector3d xUnit = camera
				.getUnitVectorAfterYRotation(Axis.X);
		Vector3d zUnit = camera
				.getUnitVectorAfterYRotation(Axis.Z);

		boolean sprinting = Keyboard.shift && Keyboard.w
				&& !Keyboard.s && onGround;

		Vector3d movementAcceleration = new Vector3d(0, 0,
				0);
		if (Keyboard.w)
			movementAcceleration.translate(zUnit);
		if (Keyboard.s) movementAcceleration
				.translate(Vector3d.multiply(zUnit, -1));
		if (Keyboard.d)
			movementAcceleration.translate(xUnit);
		if (Keyboard.a) movementAcceleration
				.translate(Vector3d.multiply(xUnit, -1));

		movementAcceleration.normalise();

		movementAcceleration = Vector3d.multiply(
				movementAcceleration,
				onGround ? accelerationHorizontalOnGround
						: accelerationHorizontalMidAir);
		if (sprinting)
			movementAcceleration = Vector3d.multiply(
					movementAcceleration, sprintModifier);

		velocityHorizontal.translate(movementAcceleration);

		if (velocityVertical
				.getMagnitude() > terminalVertical)
			velocityVertical = Vector3d.multiply(
					velocityVertical,
					terminalVertical / velocityVertical
							.getMagnitude());

		if (Math.abs(velocityVertical
				.getY()) > terminalVertical) {
			if (velocityVertical.getY() > 0)
				velocityVertical.setY(terminalVertical);
			else velocityVertical.setY(-terminalVertical);
		}

		move();

		Vector3d cameraPosition = Vector3d.add(position,
				cameraOffset);
		camera.position = cameraPosition;
	}

	void move() {
		velocityHorizontal = collider
				.move(velocityHorizontal);
		Vector3d velocityVerticalNew = collider
				.move(velocityVertical);
		if (velocityVertical.getY() < velocityVerticalNew
				.getY())
			onGround = true; // That means that the move function has changed the vertical velocity
		else onGround = false;
		velocityVertical = velocityVerticalNew;

		position = Vector3d.add(
				collider.getCentrePosition(),
				Vector3d.multiply(colliderCentreOffset,
						-1));
	}

	public void render(Camera c, Graphics g) {
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
