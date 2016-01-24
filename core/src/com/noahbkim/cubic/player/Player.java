package com.noahbkim.cubic.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
	
	/** Model properties. */
	public static Vector3 dimensions = new Vector3(1f, 1f, 1f);
	public static float azimuth;
	public static final float mass = 2.0f;
	
	/** Jump state. */
	public enum State {
		GROUNDED, JUMPING
	}
	
	public State state = State.GROUNDED;
	
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
		this(Models.box(dimensions));
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
		
		collisionShape = new btBoxShape(dimensions.scl(0.5f));
		collisionShape.calculateLocalInertia(mass, localInertia);
		constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, collisionShape, localInertia);
		rigidBody = new btRigidBody(constructionInfo);
		rigidBody.activate(true);
		rigidBody.setFriction(0.9f);
	}

	/**
	 * Move the player with the current input.
	 * @author Noah Kim
	 * @author Arman Siddique
	 */
	public void input() {
		Vector3 objectiveJoystick = new Vector3();
		if (Gdx.input.isKeyPressed(Input.Keys.W)) objectiveJoystick.x += 1;
		if (Gdx.input.isKeyPressed(Input.Keys.S)) objectiveJoystick.x -= 1;
		if (Gdx.input.isKeyPressed(Input.Keys.A)) objectiveJoystick.z -= 1;
		if (Gdx.input.isKeyPressed(Input.Keys.D)) objectiveJoystick.z += 1;
		azimuth += -Gdx.input.getDeltaX() * Cubic.defaults.mouseSensitivity;
		objectiveJoystick.rotate(azimuth, 0, 1, 0);
			
		Vector3 subjectiveJoystick = new Vector3();
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && state == State.GROUNDED) {
			subjectiveJoystick.y += 5;
			//state = State.JUMPING;
		}
		
		
		
		if (rigidBody.getLinearVelocity().len2() < 500) rigidBody.applyCentralImpulse(objectiveJoystick.add(subjectiveJoystick));

	}
	
	/**
	 * Update the entire player. 
	 * @author Noah Kim
	 * @author Arman Siddique
	 */
	@Override
	public void update() {
		transform.set(rigidBody.getCenterOfMassTransform());
		if (enableInput) input();
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
