package com.noahbkim.cubic.player;

import com.badlogic.gdx.graphics.PerspectiveCamera;

public class OrbitCamera extends PerspectiveCamera {

	public Player player;
	public float azimuth;
	public float altitude;
	public float radius;
	
	public OrbitCamera() {
		super();
		azimuth = 0;
		altitude = 30;
		radius = 10;
	}
	
	public void update() {
		if (player == null) return;
		position.x = (float)(radius * Math.sin(altitude) * Math.cos(azimuth));
		position.y = (float)(radius * Math.cos(altitude));
		position.z = (float)(radius * Math.sin(altitude) * Math.sin(azimuth));
	}
	
	public void target(Player player) {
		this.player = player;
	}
	
}
