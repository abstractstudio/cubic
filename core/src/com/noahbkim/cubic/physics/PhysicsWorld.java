package com.noahbkim.cubic.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.utils.Disposable;
import com.noahbkim.cubic.Cubic.defaults;
import com.noahbkim.cubic.utility.Updatable;

/**
 * Maintains the bullet physics world.
 * This class handles the initialization and updating of the physics world.
 * @author Arman Siddique
 * @author Noah Kim
 */
public class PhysicsWorld implements Updatable, Disposable {
	
	/** Physics world variables. */
	public btCollisionConfiguration collisionConfig;
	public btDispatcher dispatcher;
	public btDbvtBroadphase broadphase;
	public btConstraintSolver constraintSolver;
	public btDynamicsWorld dynamicsWorld;
	
	/** Physics simulation variable. */
	private int maxSubSteps;
	private float fixedTimeStep;	
	
	/**
	 * Creates a new Bullet Physics dynamic world.
	 * @param maxSubSteps the maximum number of substeps that can happen in each update.
	 * @param fixedTimeStep the length of the physics engine's internal clock.
	 */
	public PhysicsWorld(int maxSubSteps, float fixedTimeStep) {
		this.maxSubSteps = maxSubSteps;
		this.fixedTimeStep = fixedTimeStep;
		
		collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
        dynamicsWorld.setGravity(defaults.gravity);
	}
	
	/**
	 * Adds a rigid body to the physics world.
	 * @param body the rigid body to add.
	 */
	public void addRigidBody(btRigidBody body) {
		dynamicsWorld.addRigidBody(body);
	}
	
	/**
	 * Performs a step in the physics simulation.
	 */
	@Override
	public void update() {
		dynamicsWorld.stepSimulation(Gdx.graphics.getDeltaTime(), maxSubSteps, fixedTimeStep);
	}
	
	/**
	 * Disposes of all the physics world objects.
	 */
	@Override
	public void dispose() {
		dynamicsWorld.dispose();
		constraintSolver.dispose();
		broadphase.dispose();
		dispatcher.dispose();
		collisionConfig.dispose();
	}
}
