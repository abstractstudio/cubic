package com.noahbkim.cubic.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;
import com.noahbkim.cubic.Cubic;
import com.noahbkim.cubic.physics.PhysicsWorld;
import com.noahbkim.cubic.utility.Models;
import com.noahbkim.cubic.utility.Updatable;

/**
 * General player class based off of a model instance.
 * This class handles movement but is separated from the camera.
 */
public class Player extends ModelInstance implements RenderableProvider, Updatable, Disposable {
	
	/**
	 * Spawn a default player.
	 * @return a new player
	 */
	public static Player spawn(PhysicsWorld world) {
		Vector3 dimensions = new Vector3(1f, 1f, 1f);
		float mass = 2.0f;
		Model model = Models.box(dimensions);
		return new Player(world, model, dimensions, mass);
	}
	
	/** Model. */
	public PhysicsWorld world;
	public Model model;
	public Vector3 dimensions;
	public float mass;

	/** Movement. */
	public boolean movementEnabled;
	public boolean rotationEnabled;
	public float azimuth;
	public boolean grounded;
	public float lastRotation;
	
	/** Physics. */
	private Vector3 localInertia;
	private PlayerMotionState motionState;
	public btCollisionShape collisionShape;
	public btRigidBody.btRigidBodyConstructionInfo constructionInfo;
	public btRigidBody rigidBody;
	
	/**
	 * Instantiate a new player with a custom model. s
	 * @param model the model of the player.
	 */
	private Player(PhysicsWorld world, Model model, Vector3 dimensions, float mass) {
		/* Create the model. */
		super(model);
		this.world = world;
		this.model = model;
		this.dimensions = dimensions;
		this.mass = mass;
		
		/* Set up physics. */
		localInertia = new Vector3();
		motionState = new PlayerMotionState(this);
		collisionShape = new btBoxShape(dimensions.scl(0.5f));
		collisionShape.calculateLocalInertia(mass, localInertia);
		constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, motionState, collisionShape, localInertia);
		rigidBody = new btRigidBody(constructionInfo);
		rigidBody.setActivationState(Collision.DISABLE_DEACTIVATION);
		rigidBody.activate(true);
		rigidBody.setFriction(0.5f);
		
		/* Allow movement. */
		rotationEnabled = true;
		movementEnabled = true;
		lastRotation = 0;
	}

	/**
	 * Move the player with the current input.
	 */
	public void input() {
		if (rotationEnabled) {
			float rotation = Gdx.input.getDeltaX() * Cubic.defaults.mouseSensitivity;
			float mouseAcceleration = rotation - lastRotation;
			
			final float terminalAngularVel = 4.0f;
			
			if (rigidBody.getAngularVelocity().len2() < terminalAngularVel) {
				final float accelFactor = 0.02f;
				Vector3 r = new Vector3(1, 0, 0);
				Vector3 f = (new Vector3(0, 0, 1)).scl(mouseAcceleration * accelFactor);
				rigidBody.applyTorqueImpulse(r.crs(f));
			} else {
				System.out.println(rigidBody.getAngularVelocity().len2());
			}
			
			Quaternion bodyRot = new Quaternion();
			rigidBody.getCenterOfMassTransform().getRotation(bodyRot);
			azimuth = -bodyRot.getYaw();
			System.out.println(azimuth);
		} 
		
		if (movementEnabled) {
			Vector3 objectiveJoystick = new Vector3();
			if (Gdx.input.isKeyPressed(Input.Keys.W)) objectiveJoystick.x += 1;
			if (Gdx.input.isKeyPressed(Input.Keys.S)) objectiveJoystick.x -= 1;
			if (Gdx.input.isKeyPressed(Input.Keys.A)) objectiveJoystick.z -= 1;
			if (Gdx.input.isKeyPressed(Input.Keys.D)) objectiveJoystick.z += 1;
			
			Vector3 subjectiveJoystick = new Vector3();
			if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && grounded) {
				subjectiveJoystick.y += 5;
				//state = State.JUMPING;
			}
			
			Vector3 finalJoystick = objectiveJoystick.add(subjectiveJoystick);
			
			System.out.println(getTranslation());
			
			if (rigidBody.getLinearVelocity().len2() < 500) {
				rigidBody.applyCentralImpulse(finalJoystick.rotate(-azimuth, 0.0f, 1.0f, 0.0f));
			} else {
				System.out.println("Too fast!");
			}
		}
	}
	
	/**
	 * Update the entire player. 
	 */
	@Override
	public void update() {
		//transform.set(rigidBody.getCenterOfMassTransform());
		//System.out.println("Started update");
		input();
		//System.out.println("Finished update");
	}
	
	/**
	 * Dispose of the player. 
	 */
	@Override
	public void dispose() {
		collisionShape.dispose();
		constructionInfo.dispose();
		rigidBody.dispose();
		model.dispose();
	}
	
	/** 
	 * Get the translation of the player. 
	 * @return a vector containing the player's coordinates.
	 */
	public Vector3 getTranslation() {
		return new Vector3(transform.val[Matrix4.M03], transform.val[Matrix4.M13], transform.val[Matrix4.M23]);
	}
}
