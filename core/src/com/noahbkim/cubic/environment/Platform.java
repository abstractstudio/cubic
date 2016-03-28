package com.noahbkim.cubic.environment;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;
import com.noahbkim.cubic.utility.Updatable;

public class Platform extends ModelInstance implements Updatable, Disposable {
	public Model model;
	
	public Vector3 dimensions;
	public float mass;
	private Vector3 localInertia;
	
	private btCollisionShape collisionShape;
	private btRigidBody.btRigidBodyConstructionInfo rigidBodyInfo;
	public btRigidBody rigidBody;
	
	public Platform(Model model, Vector3 dimensions) {
		this(model, dimensions, 0.0f, new Vector3());
	}
	
	public Platform(Model model, Vector3 dimensions, float mass, Vector3 localInertia) {
		super(model);
		this.model = model;
		this.dimensions = dimensions;
		this.mass = mass;
		this.localInertia = localInertia;
		
		this.collisionShape = new btBoxShape(dimensions.scl(0.5f));
		this.rigidBodyInfo = new btRigidBody.btRigidBodyConstructionInfo(this.mass, null, this.collisionShape, this.localInertia);
		this.rigidBody = new btRigidBody(this.rigidBodyInfo);
		this.rigidBody.setWorldTransform(this.transform);
	}

	@Override
	public void update() {
		this.rigidBody.setWorldTransform(this.transform);
	}
	
	@Override
	public void dispose() {
		this.collisionShape.dispose();
		this.rigidBodyInfo.dispose();
		this.rigidBody.dispose();
	}
}
