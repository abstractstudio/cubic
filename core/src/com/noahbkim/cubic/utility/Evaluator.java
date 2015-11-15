package com.noahbkim.cubic.utility;

public class Evaluator {

	public static final String BYTE = "byte";
	public static final String SHORT = "byte";
	public static final String INT = "byte";
	public static final String LONG = "byte";
	public static final String DOUBLE = "byte";
	public static final String FLOAT = "byte";
	public static final String BOOLEAN = "byte";
	
	public static final Object evaluate(String value, String type) {
		if (type == BYTE) return Byte.parseByte(value);
		else if (type == SHORT) return Short.parseShort(value);
		else if (type == INT) return Integer.parseInt(value);
		else if (type == LONG) return Long.parseLong(value);
		else if (type == DOUBLE) return Double.parseDouble(value);
		else if (type == FLOAT) return Float.parseFloat(value);
		else if (type == BOOLEAN) return Boolean.parseBoolean(value);
		else return null;
	}
	
}
