package com.noahbkim.cubic.utility;

/**
 * A utility class to evaluate strings and convert them to other types.
 * @author Noah Kim
 * @author Arman Siddique
 */
public class Evaluator {
	/** The string names of all the types. */
	public static final String BYTE = "byte";
	public static final String SHORT = "short";
	public static final String INT = "int";
	public static final String LONG = "long";
	public static final String DOUBLE = "double";
	public static final String FLOAT = "float";
	public static final String BOOLEAN = "boolean";
	public static final String STRING = "string";
	
	/**
	 * Takes a string value and converts it into the specified type.
	 * The supported types are: 
	 * <ul>
	 * 	<li>byte</li>
	 * 	<li>short</li>
	 * 	<li>int</li>
	 * 	<li>long</li>
	 * 	<li>double</li>
	 * 	<li>float</li>
	 * 	<li>boolean</li>
	 * 	<li>string</li> 
	 * </ul>
	 * Note that if the specified type is a string and the value has double quotations 
	 * around it, then only the string inside the quotations is returned.
	 * @param value the value to be converted.
	 * @param type the type to convert the value to (ex. int, double, string).
	 * @return the converted value.
	 * @author Noah Kim
	 * @author Arman Siddique
	 */
	public static final Object evaluate(String value, String type) {
		if (type.equals(BYTE)) return Byte.parseByte(value);
		else if (type.equals(SHORT)) return Short.parseShort(value);
		else if (type.equals(INT)) return Integer.parseInt(value);
		else if (type.equals(LONG)) return Long.parseLong(value);
		else if (type.equals(DOUBLE)) return Double.parseDouble(value);
		else if (type.equals(FLOAT)) return Float.parseFloat(value);
		else if (type.equals(BOOLEAN)) return Boolean.parseBoolean(value);
		else if (type.equals(STRING)) {
			if (value.startsWith("\"") && value.endsWith("\"") || value.startsWith("'") && value.endsWith("'"))
				return value.substring(1, value.length() - 1);
			else
				return value;
		}
		else return null;
	}
}
