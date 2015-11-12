package com.noahbkim.cubic.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class Player implements RenderableProvider {

	public Vector3 translation;
	public Vector3 scale;
	public Quaternion rotation;
	public Material material;
	
	public Model model;
	public ModelInstance instance;
	
	public Player() {
		translation = new Vector3();
		scale = new Vector3(5, 5, 5);
		rotation = new Quaternion();
		material = new Material(ColorAttribute.createDiffuse(Color.WHITE));
	}
	
	public Player(Vector3 transform, Vector3 scale, Quaternion rotation, Material material) {
		this.translation = transform;
		this.scale = scale;
		this.rotation = rotation;
		this.material = material;
	}
	
	/* Convenience method, should eventually be deleted. */
	public Player(Vector3 transform, Vector3 scale, Quaternion rotation) {
		this(transform, scale, rotation, new Material(ColorAttribute.createDiffuse(Color.WHITE)));
	}
	
	public void create() {
		ModelBuilder builder = new ModelBuilder();
		model = builder.createBox(scale.x, scale.y, scale.z, material, Usage.Position | Usage.Normal);
		instance = new ModelInstance(model);
	}
	
	public void dispose() {
		model.dispose();
	}
	
	public void update() {
		instance.transform.setTranslation(translation);
		//instance.transform.rotate(rotation);
		//instance.transform.scale(scale.x, scale.y, scale.z);
	}

	@Override
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		instance.getRenderables(renderables, pool);
	}
	
}
