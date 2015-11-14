package com.noahbkim.cubic.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.noahbkim.cubic.Cubic;
import com.noahbkim.cubic.utility.Updatable;

/**
 * Specialized camera that maintains an orbiting 3rd person view of the target model.
 * @author Noah Kim
 */
public class PlayerCamera extends PerspectiveCamera implements Updatable {

	/** A set of defaults for the player camera. */
	public static class Defaults {
		static float fieldOfView = 67f;
		static float startingAltitude = -50;
		static float startingAzimuth = 0;
		static float startingRadius = 20;
	}
	
	/** Player and relative location. */
	public Player player;
	public float azimuth = Defaults.startingAzimuth;
	public float altitude = Defaults.startingAltitude;
	public float radius = Defaults.startingRadius;
	
	/** 
	 * Initialize a new player camera with default settings.
	 * @author Noah Kim
	 */
	public PlayerCamera() {
        super(Defaults.fieldOfView, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
	
	/** 
	 * Initialize a player camera with control over the perspective camera instantiation.
	 * @author Noah Kim
	 */
	public PlayerCamera(float fieldOfView, int width, int height) {
		super(fieldOfView, width, height);
	}
	
	/**
	 * Update the player camera. 
	 * @author Noah Kim
	 */
	public void update() {
				
		/* Exit if there is no targeted player. */
		if (player != null) {
			
			/* Get input. */
			input();
			
			/* Move the camera. */
			Vector3 origin = player.getTranslation();
			Vector3 translation = new Vector3();
			translation.x = (float)(radius * MathUtils.sinDeg(altitude) * MathUtils.cosDeg(azimuth)) + origin.x;
			translation.y = (float)(radius * MathUtils.cosDeg(altitude)) + origin.y;
			translation.z = (float)(radius * MathUtils.sinDeg(altitude) * MathUtils.sinDeg(azimuth)) + origin.z;
			position.set(translation);
			up.set(Vector3.Y);
			lookAt(origin);
		}

		/* Update the perspective camera. */
		super.update();
	}
	
	/**
	 * Get user input.
	 * @author Noah Kim
	 */
	public void input() {
		azimuth += Gdx.input.getDeltaX() * Cubic.Defaults.mouseSensitivity;
		float dy = Gdx.input.getDeltaY() * Cubic.Defaults.mouseSensitivity;
		if (altitude + dy > -1) altitude = -1;
		else if (altitude + dy < -90) altitude = -90;
		else altitude += dy;
	}
	
	/**
	 * Target a specific player.
	 * @param player the player to bind the camera to.
	 * @author Noah Kim
	 */
	public void target(Player player) {
		this.player = player;
	}
	
}
