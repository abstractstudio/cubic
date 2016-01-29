package com.noahbkim.cubic.player;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

public class PlayerMotionState extends btMotionState {
	private Player player;
	
	public PlayerMotionState(Player player) {
		super();
		this.player = player;
	}
	
	@Override
	public void getWorldTransform(Matrix4 worldTrans) {
		worldTrans.set(player.transform);
	}
	
	@Override
	public void setWorldTransform(Matrix4 worldTrans) {
		player.transform.set(worldTrans);
		//System.out.println(player.transform);
		//System.out.println(worldTrans);
		//System.out.println("--------------------------------------------");
		//System.out.println(player.getTranslation());
		//System.out.println("Setting " + System.nanoTime());
	}
}
