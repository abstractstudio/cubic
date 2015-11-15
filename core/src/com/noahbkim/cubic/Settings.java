package com.noahbkim.cubic;

import java.util.HashMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.noahbkim.cubic.utility.Evaluator;

public class Settings {
	
	private HashMap<String, Object> map;
	
	public Settings(String path) {
		FileHandle file = Gdx.files.local(path);
		String contents = file.readString();
		String[] lines = contents.split("\n");
		map = new HashMap<String, Object>();
		for (String line : lines) {
			String[] keyTypeValue = line.split("=");
			String[] keyType = keyTypeValue[0].split(":");
			String key = keyType[0];
			String type = keyType[1];
			String value = keyTypeValue[1];
			map.put(key, Evaluator.evaluate(value, type));
		}
	}

	
}
