package com.noahbkim.cubic.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Vector3;

public class Player extends ModelInstance implements RenderableProvider {

	public Vector3 trajectory;
	public Model model;

	public Player() {
		this(PlayerModel.cube());
	}
	
	public Player(Model model) {
		super(model);
		this.model = model;
	}

	public void move() {
		Vector3 joystick = new Vector3();
		if (Gdx.input.isKeyPressed(Input.Keys.W)) joystick.z += 1;
		if (Gdx.input.isKeyPressed(Input.Keys.S)) joystick.z -= 1;
		if (Gdx.input.isKeyPressed(Input.Keys.A)) joystick.x -= 1;
		if (Gdx.input.isKeyPressed(Input.Keys.D)) joystick.x += 1;
		transform.translate(joystick);
	}
	
	public void update() {
		move();
	}
	
	public void dispose() {
		model.dispose();
	}
	
	public Vector3 getTranslation() {
		return new Vector3();
	}
	
}
