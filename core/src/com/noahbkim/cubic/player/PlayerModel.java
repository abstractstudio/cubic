package com.noahbkim.cubic.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class PlayerModel {

	public static class Defaults {
		public static Material material = new Material(ColorAttribute.createDiffuse(Color.WHITE));
		public static Vector3 dimensions = new Vector3(5f, 5f, 5f);
	}

	public static Model cube(Vector3 dimensions, Material material) {
		ModelBuilder builder = new ModelBuilder();
		return builder.createBox(5f, dimensions.y, dimensions.z, material, Usage.Position | Usage.Normal);
	}
	
	public static Model cube() {
		ModelBuilder builder = new ModelBuilder();
		return builder.createBox(Defaults.dimensions.x, Defaults.dimensions.y, Defaults.dimensions.z, Defaults.material, Usage.Position | Usage.Normal);
	}
	
}
