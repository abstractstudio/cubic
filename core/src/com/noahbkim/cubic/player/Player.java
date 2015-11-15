package com.noahbkim.cubic.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.noahbkim.cubic.Cubic;
import com.noahbkim.cubic.utility.Models;
import com.noahbkim.cubic.utility.Updatable;

/**
 * General player class based off of a model instance.
 * This class handles movement but is separated from the camera.
 * @author Noah Kim
 */
public class Player extends ModelInstance implements RenderableProvider, Updatable {
	/** Model settings. */
	public static final float LENGTH = 1.0f;
	public static final float WIDTH = 1.0f;
	public static final float HEIGHT = 1.0f;
	
	public static final float MASS = 1.0f;
	
	/** Player settings. */
	public boolean enableInput;
	
	/** Base player model. */
	public Model model;
	
	/** Rigid body for physics. */
	public btCollisionShape collisionShape;
	public btRigidBody rigidBody;
	public btRigidBody.btRigidBodyConstructionInfo constructionInfo;
	private Vector3 localInertia = new Vector3();

	/** Instantiate a new player. Model is a default cube. */
	public Player() {
		this(Models.cube());
	}
	
	/**
	 * Instantiate a new player with a custom model. s
	 * @param model the model of the player.
	 * @author Noah Kim
	 * @author Arman Siddique
	 */
	public Player(Model model) {
		super(model);
		this.model = model;
		
		this.collisionShape = new btBoxShape(Models.defaults.dimensions);
		this.collisionShape.calculateLocalInertia(MASS, localInertia);
		this.constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(MASS, null, collisionShape, localInertia);
		this.rigidBody = new btRigidBody(constructionInfo);
		this.rigidBody.activate(true);
	}

	/**
	 * Move the player with the current input.
	 * @author Noah Kim
	 * @author Arman Siddique
	 */
	public void input() {
		Vector3 joystick = new Vector3();
		if (Gdx.input.isKeyPressed(Input.Keys.W)) joystick.x += 1;
		if (Gdx.input.isKeyPressed(Input.Keys.S)) joystick.x -= 1;
		if (Gdx.input.isKeyPressed(Input.Keys.A)) joystick.z -= 1;
		if (Gdx.input.isKeyPressed(Input.Keys.D)) joystick.z += 1;
<<<<<<< HEAD
		//transform.translate(joystick.nor());
		//transform.rotate(Vector3.Y, -Gdx.input.getDeltaX() * Cubic.defaults.mouseSensitivity);
		if (rigidBody.getLinearVelocity().len2() < 500)
			rigidBody.applyCentralImpulse(joystick);
=======
		transform.translate(joystick.nor());
		transform.rotate(Vector3.Y, -Gdx.input.getDeltaX() * Cubic.defaults.mouseSensitivity);
>>>>>>> origin/master
	}
	
	/**
	 * Update the entire player. 
	 * @author Noah Kim
	 * @author Arman Siddique
	 */
	public void update() {
		if (enableInput) input();
		transform.set(rigidBody.getCenterOfMassTransform());
	}
	
	/**
	 * Dispose of the player. 
	 * @author Noah Kim
	 */
	public void dispose() {
		collisionShape.dispose();
		constructionInfo.dispose();
		rigidBody.dispose();
		model.dispose();
	}
	
	/** 
	 * Get the translation of the player. 
	 * @return a vector containing the player's coordinates.
	 * @author Noah Kim
	 * @author Arman Siddique
	 */
	public Vector3 getTranslation() {
		return new Vector3(transform.val[Matrix4.M03], transform.val[Matrix4.M13], transform.val[Matrix4.M23]);
	}
	
}
