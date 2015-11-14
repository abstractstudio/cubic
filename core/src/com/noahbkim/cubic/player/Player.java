package com.noahbkim.cubic.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Matrix4;
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
		if (Gdx.input.isKeyPressed(Input.Keys.W)) joystick.x += 1;
		if (Gdx.input.isKeyPressed(Input.Keys.S)) joystick.x -= 1;
		if (Gdx.input.isKeyPressed(Input.Keys.A)) joystick.z -= 1;
		if (Gdx.input.isKeyPressed(Input.Keys.D)) joystick.z += 1;
		transform.rotate(Vector3.Y, -Gdx.input.getDeltaX());
		transform.translate(joystick.nor());
	}
	
	public void update() {
		move();
	}
	
	public void dispose() {
		model.dispose();
	}
	
	public Vector3 getTranslation() {
		return new Vector3(transform.val[Matrix4.M03], transform.val[Matrix4.M13], transform.val[Matrix4.M23]);
	}
	
}
