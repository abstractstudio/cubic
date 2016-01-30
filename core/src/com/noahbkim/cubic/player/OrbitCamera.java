package com.noahbkim.cubic.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.noahbkim.cubic.Cubic;
import com.noahbkim.cubic.utility.Updatable;

/**
 * Specialized camera that maintains an orbiting 3rd person view of the target model.
 * @author Noah Kim
 * @author Arman Siddique
 */
public class OrbitCamera extends PerspectiveCamera implements Updatable {

	/** A set of defaults for the player camera. */
	public static class Defaults {
		static float fieldOfView = 67.0f;
		static float startingAltitude = -50.0f;
		static float startingAzimuth = 0;
		static float startingRadius = 5;
	}
	
	/** Player and relative location. */
	protected Player player;
	protected Quaternion rotation; 
	protected float radius;
	
	/** 
	 * Initialize a new player camera with default settings.
	 */
	public OrbitCamera() {
        this(Defaults.fieldOfView, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
	
	/** 
	 * Initialize a player camera with control over the perspective camera instantiation.
	 */
	public OrbitCamera(float fieldOfView, int width, int height) {
		super(fieldOfView, width, height);
		rotation = (new Quaternion(Vector3.X, Defaults.startingAltitude)).mulLeft(new Quaternion(Vector3.Y, Defaults.startingAzimuth));
		radius = Defaults.startingRadius;
	}
	
	/**
	 * Update the player camera's transform. 
	 */
	@Override
	public void update() {	
		/* Exit if there is no targeted player. */
		if (player != null) {
			/* Get input. */
			input();
			/* Move and rotate the camera. */
			Vector3 origin = player.getTranslation();
			Vector3 translation = new Vector3();
			translation.x = (float)(radius * MathUtils.sinDeg(rotation.getPitch()) * MathUtils.cosDeg(rotation.getYaw())) + origin.x;
			translation.y = (float)(radius * MathUtils.cosDeg(rotation.getPitch())) + origin.y;
			translation.z = (float)(radius * MathUtils.sinDeg(rotation.getPitch()) * MathUtils.sinDeg(rotation.getYaw())) + origin.z;
			position.set(translation);
			up.set(Vector3.Y);
			lookAt(origin);
		}
		/* Update the perspective camera. */
		super.update();
	}
	
	/**
	 * Used by subclasses to call {@link PerspectiveCamera#update()}.
	 */
	protected void updatePerspectiveCamera() {
		super.update();
	}
	
	/**
	 * Process the mouse input.
	 */
	public void input() {
		float rotX = Gdx.input.getDeltaX() * Cubic.defaults.mouseSensitivity;
		float rotY = Gdx.input.getDeltaY() * Cubic.defaults.mouseSensitivity;
		if (rotation.getPitch() + rotY < -1.0f && rotation.getPitch() + rotY > -89.0f) rotation.mul(new Quaternion(Vector3.X, rotY));
		rotation.mulLeft(new Quaternion(Vector3.Y, rotX));
	}
	
	/**
	 * Gets the player that this camera is targeting.
	 * @return the current target.
	 */
	public Player getTarget() {
		return player;
	}
	
	/**
	 * Makes this camera target a specific player.
	 * @param player the player to bind the camera to.
	 */
	public void setTarget(Player player) {
		this.player = player;
	}
	
	/**
	 * Gets the rotation of this camera as a quaternion.
	 * @return the rotation
	 */
	public Quaternion getRotation() {
		return rotation;
	}
}
