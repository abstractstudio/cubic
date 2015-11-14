package com.noahbkim.cubic.utility;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

/**
 * A set of convenience functions for creating on-the-fly player models.
 * @author Noah Kim
 */
public class Models {

	/** 
	 * A couple of defaults for model properties. 
	 * @author Noah Kim
	 */
	public static class Defaults {
		public static Material material = new Material(ColorAttribute.createDiffuse(Color.WHITE));
		public static Material material2 = new Material(ColorAttribute.createDiffuse(Color.GRAY));
		public static Vector3 dimensions = new Vector3(5f, 5f, 5f);
		public static long attributes = Usage.Position | Usage.Normal;
	}

	/**
	 * Create a custom box model.
	 * @param dimensions a vector containing the width, height, and length of the box.
	 * @param material the material mapped to the box.
	 * @param attributes the model attributes of the box.
	 * @return a box model with the given specifications.
	 * @author Noah Kim
	 */
	public static Model box(Vector3 dimensions, Material material, long attributes) {
		ModelBuilder builder = new ModelBuilder();
		return builder.createBox(dimensions.x, dimensions.y, dimensions.z, material, attributes);
	}
	
	/**
	 * Create a default cube model. 
	 * @return a cube model with the default specifications.
	 * @author Noah Kim
	 */
	public static Model cube() {
		return box(Defaults.dimensions, Defaults.material, Defaults.attributes);
	}
	
}
