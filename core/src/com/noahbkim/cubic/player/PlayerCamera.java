package com.noahbkim.cubic.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.noahbkim.cubic.Cubic;
import com.noahbkim.cubic.player.OrbitCamera.Defaults;
import com.noahbkim.cubic.utility.Updatable;

/**
 * Specialized camera that maintains an orbiting 3rd person view of the target model.
 * @author Noah Kim
 * @author Arman Siddique
 */
public class PlayerCamera extends PerspectiveCamera implements Updatable {

	/** A set of defaults for the player camera. */
	public static class Defaults {
		static float fieldOfView = 67.0f;
		static float startingAltitude = -50.0f;
		static float startingRadius = 5;
	}
	
	private Player player;
	private Quaternion rotation;
	private float radius;
	
	public PlayerCamera(Player target) {
		this(target, Defaults.fieldOfView, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	public PlayerCamera(Player target, float fieldOfView, int width, int height) {
		super(fieldOfView, width, height);
		player = target;
		rotation = new Quaternion(Vector3.X, Defaults.startingAltitude);
	}
	
	@Override
	public void update() {
		/* Exit if there is no targeted player. */
		if (player != null) {
			/* Get input. */
			input();
			
			/* Move and rotate the camera. */
			Vector3 origin = player.getTranslation();
			Vector3 translation = new Vector3();
			translation.x = (float)(radius * MathUtils.sinDeg(rotation.getPitch()) * MathUtils.cosDeg(player.azimuth)) + origin.x;
			translation.y = (float)(radius * MathUtils.cosDeg(rotation.getPitch())) + origin.y;
			translation.z = (float)(radius * MathUtils.sinDeg(rotation.getPitch()) * MathUtils.sinDeg(player.azimuth)) + origin.z;
			position.set(translation);
			up.set(Vector3.Y);
			lookAt(origin);
		}
	}
	
	public void input() {
		float rotY = Gdx.input.getDeltaY() * Cubic.defaults.mouseSensitivity;
		if (rotation.getPitch() + rotY < -1.0f && rotation.getPitch() + rotY > -89.0f)
			rotation.mul(new Quaternion(Vector3.X, rotY));
	}
}
