package com.noahbkim.cubic;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.noahbkim.cubic.utility.Evaluator;

/**
 * A utility class to load settings from a file. 
 * See the {@link #load(String)} method for more details.
 * @author Noah Kim
 * @author Arman Siddique
 */
public class Settings {
	/** A map that stores all the setting names and values. */
	private HashMap<String, Object> map;
	
	/**
	 * Loads settings from the specified file.
	 * @param path the path to the file.
	 */
	public Settings(String path) {
		load(path);
	}
	
	/**
	 * Loads settings from the specified file with the following rules: 
	 * <ol>
	 * 	<li>All empty lines and lines that start with '#' are ignored.</li>
	 * 	<li>Each setting is specified on one line.</li>
	 * 	<li>The setting's name is specified first as a string.</li>
	 * 	<li>The name is followed by a colon (':') and then the type of the setting.</li>
	 * 	<li>The type is followed by an equals sign ('=') and then the value of the setting.</li>
	 * </ol>
	 * For more information on types and values, see {@link Evaluator#evaluate(String, String)}.
	 * @param path the path to the file.
	 */
	public void load(String path) {
		FileHandle file = Gdx.files.local(path);		
		String contents = file.readString();
		String[] lines = contents.split("\n");
		map = new HashMap<String, Object>();
		for (String line : lines) {
			line = line.trim();
			if (line.startsWith("#") || line.equals("")) continue;
			String[] keyTypeValue = line.split("=");
			String[] keyType = keyTypeValue[0].split(":");
			String key = keyType[0].trim();
			String type = keyType[1].trim();
			String value = keyTypeValue[1].trim();
			map.put(key, Evaluator.evaluate(value, type));
		}
	}
	
	/**
	 * Gets the value of a specific setting.
	 * @param name the name of the setting.
	 * @return the value of the specified setting.
	 */
	public Object get(String name) {
		return map.get(name);
	}
	
	/**
	 * Sets the value of a setting. The setting is added if it doesn't already exist.
	 * @param name the name of the setting.
	 * @param value the value of the setting.
	 * @return the previously value of the settings, or null if it's new.
	 */
	public Object set(String name, Object value) {
		return map.put(name, value);
	}
	
}
