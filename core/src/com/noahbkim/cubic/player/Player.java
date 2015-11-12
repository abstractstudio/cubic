package com.noahbkim.cubic.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.BaseAnimationController.Transform;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class Player implements RenderableProvider {


	public Vector3 trajectory;
	public Matrix4 transform;
	public Material material;
	public Vector3 dimensions;
	public Model model;
	public ModelInstance instance;
	public PerspectiveCamera camera;
	
	public Player() {
		material = new Material(ColorAttribute.createDiffuse(Color.WHITE));
		dimensions = new Vector3(5f, 5f, 5f);
		create();
	}
	
	public void create() {
		ModelBuilder builder = new ModelBuilder();
		model = builder.createBox(dimensions.x, dimensions.y, dimensions.z, material, Usage.Position | Usage.Normal);
		instance = new ModelInstance(model);
		transform = instance.transform;
	}
	
	public void look() {
		if (camera == null) return;
	}

	public void move() {
		if (camera == null) return;
		Vector3 joystick = new Vector3();
		if (Gdx.input.isKeyPressed(Input.Keys.W)) joystick.z += 1;
		if (Gdx.input.isKeyPressed(Input.Keys.S)) joystick.z -= 1;
		if (Gdx.input.isKeyPressed(Input.Keys.A)) joystick.x -= 1;
		if (Gdx.input.isKeyPressed(Input.Keys.D)) joystick.x += 1;
		transform.translate(joystick);
	}
	
	public void update() {
		look();
		move();
	}
	
	public void dispose() {
		model.dispose();
	}

	@Override
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		instance.getRenderables(renderables, pool);
	}
	
}
