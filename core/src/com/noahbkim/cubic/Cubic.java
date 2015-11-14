package com.noahbkim.cubic;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.noahbkim.cubic.player.PlayerCamera;
import com.noahbkim.cubic.utility.Updatable;
import com.noahbkim.cubic.player.Player;

/**
 * Main game class.
 * @author Noah Kim
 */
public class Cubic extends ApplicationAdapter {
	
	/** Defaults. */
	public static class Defaults {
		public static float mouseSensitivity = 0.1f;
	}

	/** Rendering equipment. */
	ModelBatch batch;
	Environment environment;
	PlayerCamera camera;
	
	/** Game objects. */
	Player player;
	ArrayList<Player> players;
	ArrayList<Updatable> updatables;

	/**
	 * Create the game.
	 * @author Noah Kim
	 */
	@Override
	public void create() {
		
		/** Set up the rendering equipment. */
		batch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        camera = new PlayerCamera();
        
        /** Set up the player. */
        player = new Player();
        player.enableInput = true;
        camera.target(player);
        
        /* Create another player for reference. */
        Player reference = new Player();
        reference.transform.translate(15f, 0, 0);
        reference.update();
        
        /* Create the reference lists. */
        players = new ArrayList<Player>();
        players.add(player);
        players.add(reference);
        updatables = new ArrayList<Updatable>();
        updatables.addAll(players);
        updatables.add(camera);
        
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
		batch.render(players, environment);
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
