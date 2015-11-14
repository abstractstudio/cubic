package com.noahbkim.cubic.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class PlayerCamera extends PerspectiveCamera {

	public Player player;
	public float azimuth;
	public float altitude;
	public float radius;
	
	public PlayerCamera() {
        super(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        altitude = 30;
        azimuth = 180;
        radius = 20;
    }
	
	public PlayerCamera(float fieldOfView, int width, int height) {
		super(fieldOfView, width, height);
        altitude = 30;
        azimuth = 180;
        radius = 20;
	}
	
	public void update() {
		super.update();
		if (player == null) return;
		move();
	}
	
	public void move() {
		azimuth += Gdx.input.getDeltaX();
		altitude += -Gdx.input.getDeltaY();
		MathUtils.clamp(altitude, -90, 90);

		Vector3 origin = player.getTranslation();
		Vector3 translation = new Vector3();
		translation.x = (float)(radius * MathUtils.sinDeg(altitude) * MathUtils.cosDeg(azimuth)) + origin.x;
		translation.y = (float)(radius * MathUtils.cosDeg(altitude)) + origin.y;
		translation.z = (float)(radius * MathUtils.sinDeg(altitude) * MathUtils.sinDeg(azimuth)) + origin.z;
		position.set(translation);
		up.set(Vector3.Y);
		lookAt(origin);
	}
	
	public void target(Player player) {
		this.player = player;
	}
	
}
