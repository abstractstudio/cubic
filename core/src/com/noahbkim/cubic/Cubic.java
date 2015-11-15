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
import com.noahbkim.cubic.player.PlayerCamera;
import com.noahbkim.cubic.utility.Models;
import com.noahbkim.cubic.utility.Updatable;
import com.noahbkim.cubic.player.Player;

/**
 * Main game class.
 * @author Noah Kim
 */
public class Cubic extends ApplicationAdapter {
	
	/** Defaults. */
	public static class defaults {
		public static float mouseSensitivity = 0.1f;
	}
	
	public static Settings settings;
	
	/** Rendering equipment. */
	ModelBatch batch;
	Environment environment;
	PlayerCamera camera;
	
	/** Game objects. */
	Player player;
	ArrayList<Player> players;
	ArrayList<Updatable> updatables;
	ArrayList<ModelInstance> instances;

	/**
	 * Create the game.
	 * @author Noah Kim
	 */
	@Override
	public void create() {
		
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

        camera.target(player);
        
        /* Create another player for reference. */
        Player reference = new Player();
        reference.transform.translate(15f, 0, 0);
        reference.update();
        reference.transform.val[Matrix4.M13] += Models.defaults.dimensions.y / 2;
        
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
	}

	/**
	 * Render the game.
	 * @author Noah Kim
	 */
	@Override
	public void render() {
		
		/* Set up openGL. */
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        /* Update everybody. */
        for (int i = 0; i < updatables.size(); i++) updatables.get(i).update();
        
        /* Check for game input. */
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.input.setCursorCatched(false);;

        /* Render. */
		batch.begin(camera);
		batch.render(instances, environment);
		batch.end();
	}
	
	/**
	 * Dispose of the game.
	 * @author Noah Kim
	 */
	@Override
	public void dispose() {
		/* Dispose of everything. */
		batch.dispose();
		for (Player player : players) player.dispose();
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
