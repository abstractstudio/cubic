package com.noahbkim.cubic;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.noahbkim.cubic.network.Connection;
import com.noahbkim.cubic.network.Server;
import com.noahbkim.cubic.network.tcp.TCPConnection;
import com.noahbkim.cubic.network.tcp.TCPServer;
import com.noahbkim.cubic.physics.PhysicsWorld;
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
	
	/** States. */
	public static enum State {
		PLAYING, PAUSED
	}
		
	public static Settings settings;
	
	/** Game. */
	public State state;
	
	/** Rendering equipment. */
	private ModelBatch batch;
	private Environment environment;
	private PlayerCamera camera;
	private AssetManager manager;
	
	/** Game objects. */
	private Player player;
	public ArrayList<Player> players;
	public ArrayList<Updatable> updatables;
	public ArrayList<ModelInstance> instances;
	
	/** Physics World. */
	private PhysicsWorld physicsWorld;
	
	/** Physics objects for the ground. */
	public static int GROUND_ID = 6969;
	private btCollisionShape groundShape;
	private btRigidBody.btRigidBodyConstructionInfo groundRigidBodyInfo;
	private btRigidBody groundRigidBody;

	/**
	 * Create the game.
	 */
	@Override
	public void create() {
		
		/* Initialize Bullet. */
        Bullet.init();
		
		/* Load the settings. */
		settings = new Settings("cubic.settings");

		//testNetwork();
		
		/* Set up the rendering equipment. */
		batch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 2f, -1f, 2f));
        manager = new AssetManager();
        
        /* Initialize physics. */
        physicsWorld = new PhysicsWorld(this, 5, 1.0f/60.0f);
        
        /* Add the ground to the world. */
        groundShape = new btBoxShape(new Vector3(50, 0.5f, 50));
        groundRigidBodyInfo = new btRigidBody.btRigidBodyConstructionInfo(0.0f, null, groundShape, Vector3.Zero);
        groundRigidBody = new btRigidBody(groundRigidBodyInfo);
        groundRigidBody.setCollisionShape(groundShape);
        groundRigidBody.setUserValue(GROUND_ID);
        physicsWorld.addRigidBody(groundRigidBody);
        
        /* Set up the player. */
        player = Player.spawn(physicsWorld);
        player.transform.val[Matrix4.M13] += player.dimensions.y / 2;
        
        /* Set up the camera. */
        camera = new PlayerCamera(player);
        
        /* Create another player for reference. */
        Player reference1 = Player.spawn(physicsWorld);
        reference1.movementEnabled = false;
        reference1.rotationEnabled = false;
        reference1.transform.translate(15f, 0, 0);
        reference1.transform.val[Matrix4.M13] += reference1.dimensions.y / 2;
        
        Player reference2 = Player.spawn(physicsWorld);
        reference2.movementEnabled = false;
        reference2.rotationEnabled = false;
        reference2.transform.translate(0f, 0, 15f);
        reference2.transform.val[Matrix4.M13] += reference2.dimensions.y / 2;
        
        /* Create a floor. */
        ModelInstance floor = new ModelInstance(Models.box(new Vector3(100, 1, 100), Models.defaults.material2, Models.defaults.attributes));
//        manager.load("floor.png", Texture.class);
//        manager.finishLoading();
//        Texture texture = manager.get("floor.png");
//        texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
//        TextureAttribute textureAttribute = new TextureAttribute(TextureAttribute.Diffuse, texture);
//        Material material = floor.materials.get(0);
//        material.set(textureAttribute);
        
        floor.transform.val[Matrix4.M13] -= 0.5;
        groundRigidBody.setWorldTransform(floor.transform);
        groundRigidBody.setFriction(1.0f);

        /* Create the reference lists. */
        players = new ArrayList<Player>();
        players.add(player);
        players.add(reference1);
        players.add(reference2);
        updatables = new ArrayList<Updatable>();
        updatables.addAll(players);
        updatables.add(camera);
        instances = new ArrayList<ModelInstance>();
        instances.addAll(players);
        instances.add(floor);
        
        /* Put the cursor away. */
        Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        Gdx.input.setCursorCatched(true);
        
        /* Add the players to the world. */
        for (int i = 0; i < players.size(); i++) {
        	Player p = players.get(i);
        	p.rigidBody.setUserValue(i);
        	physicsWorld.addRigidBody(p.rigidBody);
        }
        
        state = State.PLAYING;
	}

	/**
	 * Render the game.
	 */
	@Override
	public void render() {
		/* Set up openGL. */
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        /* Update physics. */
        physicsWorld.update();
        
        /* Update everybody. */
        for (Updatable u : updatables) {
        	u.update();
        }
        
        /* Check for game input. */
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
        	if (state == State.PLAYING) pause();
        	else if (state == State.PAUSED) resume();
        }
        if (!player.control && camera.isFollowingTarget()) {
        	System.out.println("Now using orbit camera");
        	camera.followTarget(false);
        	player.rotationEnabled = false;
        } else if (player.control && !camera.isFollowingTarget()) {
        	System.out.println("Now using player camera");
        	camera.followTarget(true);
        	player.rotationEnabled = true;
        }

        /* Render. */
		batch.begin(camera);
		batch.render(instances, environment);
		batch.end();
	}
	
	/**
	 * Dispose of the game.
	 */
	@Override
	public void dispose() {		
		/* Dispose of instances. */
		for (Player player : players) player.dispose();
		instances.clear();
		
		/* Dispose of ground physics body. */
		groundShape.dispose();
		groundRigidBodyInfo.dispose();
		groundRigidBody.dispose();
		
		/* Dispose of the physics world. */
		physicsWorld.dispose();
		
		/* Dispose of game data. */
		batch.dispose();
	}
	
	@Override
	public void resume() {
		System.out.println("resume");
        Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        Gdx.input.setCursorCatched(true);
        state = State.PLAYING;
	}
	
	@Override
	public void pause() {
		System.out.println("pause");
        Gdx.input.setCursorCatched(false);
		state = State.PAUSED;
	}
	
	@Override
	public void resize(int width, int height) {
		Gdx.gl.glViewport(0, 0, width, height);
	}
	
	public void testNetwork() {
		System.out.println((Integer)settings.get("port"));
		Server s = new TCPServer();
		s.start();
		Connection c = new TCPConnection("127.0.0.1", (Integer)settings.get("port"));
		Thread t = new Thread(c);
		t.start();
		c.send("Hello, world!");
	}

}
