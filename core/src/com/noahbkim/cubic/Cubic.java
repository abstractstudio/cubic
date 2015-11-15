package com.noahbkim.cubic;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.noahbkim.cubic.player.Player;
import com.noahbkim.cubic.player.PlayerCamera;
import com.noahbkim.cubic.utility.Models;
import com.noahbkim.cubic.utility.Updatable;

/**
 * Main game class.
 * @author Noah Kim
 * @author Arman Siddique
 */
public class Cubic extends ApplicationAdapter {
	
	/** Defaults. */
	public static class defaults {
		public static float mouseSensitivity = 0.1f;
		public static Vector3 gravity = new Vector3(0.0f, -10.0f, 0.0f);
	}
	
	// this doesn't do anything yet (probably will be removed)
	private class CubicContactListener extends ContactListener {
		@Override
		public boolean onContactAdded(int userValue0, int partId0, int index0, int userValue1, int partId1, int index1) {
			return true;
		}
	}
	
	public static Settings settings;
	
	/** Rendering equipment. */
	private ModelBatch batch;
	private Environment environment;
	private PlayerCamera camera;
	
	/** Game objects. */
	private Player player;
	private ArrayList<Player> players;
	private ArrayList<Updatable> updatables;
	private ArrayList<ModelInstance> instances;
	
	/** Physics objects. */
	private btCollisionConfiguration collisionConfig;
	private btDispatcher dispatcher;
	private btDbvtBroadphase broadphase;
	private btConstraintSolver constraintSolver;
	private btDynamicsWorld dynamicsWorld;
	private CubicContactListener contactListener;
	
	private btCollisionShape groundShape;
	private btRigidBody.btRigidBodyConstructionInfo groundRigidBodyInfo;
	private btRigidBody groundRigidBody;

	/**
	 * Create the game.
	 * @author Noah Kim
	 * @author Arman Siddique
	 */
	@Override
	public void create() {
		/* Initialize Bullet. */
        Bullet.init();
		
		/* Load the settings. */
		settings = new Settings("settings.txt");
		
		/* Set up the rendering equipment. */
		batch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 2f, -1f, 2f));
        camera = new PlayerCamera();
        
        /* Set up the player. */
        player = new Player();
        player.enableInput = true;
        player.transform.val[Matrix4.M13] += Models.defaults.dimensions.y / 2;
        
        /* Set up the camera. */
        camera.target(player);
        
        /* Create another player for reference. */
        Player reference = new Player();
        reference.transform.translate(15f, 0, 0);
        reference.update();
        reference.transform.val[Matrix4.M13] += Models.defaults.dimensions.y / 2;
        reference.rigidBody.setWorldTransform(reference.transform);
        
        /* Create a floor. */
        ModelInstance floor = new ModelInstance(Models.box(new Vector3(100, 1, 100), Models.defaults.material2, Models.defaults.attributes));
        floor.transform.val[Matrix4.M13] -= 0.5;
        
        /* Create the reference lists. */
        players = new ArrayList<Player>();
        players.add(player);
        players.add(reference);
        updatables = new ArrayList<Updatable>();
        updatables.addAll(players);
        updatables.add(camera);
        instances = new ArrayList<ModelInstance>();
        instances.addAll(players);
        instances.add(floor);
        
        /* Put the cursor away. */
        Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        Gdx.input.setCursorCatched(true);
        
        /* Initialize physics. */
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
        dynamicsWorld.setGravity(defaults.gravity);
        //contactListener = new CubicContactListener();
        
        /* Add the ground to the world. */
        groundShape = new btBoxShape(new Vector3(100, 1, 100));
        groundRigidBodyInfo = new btRigidBody.btRigidBodyConstructionInfo(0.0f, null, groundShape, Vector3.Zero);
        groundRigidBody = new btRigidBody(groundRigidBodyInfo);
        groundRigidBody.setCollisionShape(groundShape);
        groundRigidBody.setWorldTransform(floor.transform);
        
        /* Add the players to the world. */
        dynamicsWorld.addRigidBody(groundRigidBody);
        for (Player p : players) {
        	p.rigidBody.setCollisionFlags(p.rigidBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        	dynamicsWorld.addRigidBody(p.rigidBody);
        }
	}

	/**
	 * Render the game.
	 * @author Noah Kim
	 * @author Arman Siddique
	 */
	@Override
	public void render() {
		/* Set up openGL. */
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        /* Update physics. */
        dynamicsWorld.stepSimulation(Gdx.graphics.getDeltaTime(), 5, 1.0f/60.0f);
        
        /* Update everybody. */
        for (Updatable u : updatables) {
        	u.update();
        }
        
        /* Check for game input. */
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
        	Gdx.input.setCursorCatched(false);
        }

        /* Render. */
		batch.begin(camera);
		batch.render(instances, environment);
		batch.end();
	}
	
	/**
	 * Dispose of the game.
	 * @author Noah Kim
	 * @author Arman Siddique
	 */
	@Override
	public void dispose() {
		/* Dispose of instances. */
		for (Player player : players) player.dispose();
		instances.clear();
		
		/* Dispose of physics bodies. */
		groundShape.dispose();
		groundRigidBodyInfo.dispose();
		groundRigidBody.dispose();
		
		/* Dispose of physics world stuff. */
		dynamicsWorld.dispose();
		constraintSolver.dispose();
		broadphase.dispose();
		dispatcher.dispose();
		collisionConfig.dispose();
		//contactListener.dispose();
		
		/* Dispose of game data. */
		batch.dispose();
	}
	
	@Override
	public void resume() {
		
	}
	
	@Override
	public void pause() {
		
	}
	
	@Override
	public void resize(int width, int height) {

	}

}
