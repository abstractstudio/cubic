package com.noahbkim.cubic;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.noahbkim.cubic.utility.Evaluator;

public class Settings {
	
	private HashMap<String, Object> map;
	
	public Settings(String path) {
		load(path);
	}
	
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
			String key = keyType[0];
			String type = keyType[1];
			String value = keyTypeValue[1];
			map.put(key, Evaluator.evaluate(value, type));
		}
	}

	public Object get(String key) {
		return map.get(key);
	}
	
	public Object set(String key, Object value) {
		return map.put(key, value);
	}
	
}
