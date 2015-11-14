package com.noahbkim.cubic;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.noahbkim.cubic.player.PlayerCamera;
import com.noahbkim.cubic.player.Player;

public class Cubic extends ApplicationAdapter {

	/* Rendering objects. */
	ModelBatch batch;
	Environment environment;
	PlayerCamera camera;

	/* Player objects. */
	Player player;
	ArrayList<Player> players;

	@Override
	public void create() {
		batch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        
        camera = new PlayerCamera();
        
        player = new Player();
        Player reference = new Player();
        reference.transform.translate(15f, 0, 0);
        reference.update();
        players = new ArrayList<Player>();
        players.add(player);
        players.add(reference);
        
        Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        Gdx.input.setCursorCatched(true);

        camera.player = player;
	}

	@Override
	public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        player.update();
        camera.update();
        
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.input.setCursorCatched(false);;

		batch.begin(camera);
		batch.render(players, environment);
		batch.end();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		player.dispose();
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
