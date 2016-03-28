package com.noahbkim.cubic.physics;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.noahbkim.cubic.Cubic;
import com.noahbkim.cubic.player.Player;

class CustomContactListener extends ContactListener {
	private Cubic cubicGame;
	
	public void setCubicGame(Cubic c) {
		this.cubicGame = c;
	}
	
	public boolean onContactAdded(int userValue0, int partId0, int index0, int userValue1, int pardId1, int index1) {
		ModelInstance m0 = cubicGame.instances.get(userValue0);
		ModelInstance m1 = cubicGame.instances.get(userValue1);
		
		Player p = null;
		if (m0 instanceof Player) p = (Player) m0;
		else if (m1 instanceof Player) p = (Player) m1;
		
		if (p != null)
			p.control = true;
		
		return true;
	}
}