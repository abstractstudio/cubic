package com.noahbkim.engine.structures;

public class Quaternion {

	private float w;
	private float x;
	private float y;
	private float z;
	
	public Quaternion(float w, float x, float y, float z) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static Quaternion Euler(float yaw, float pitch, float roll) {
		float shy = (float)Math.sin((double)yaw / 2);
		float chy = (float)Math.cos((double)yaw / 2);
		float shp = (float)Math.sin((double)pitch / 2);
		float chp = (float)Math.cos((double)pitch / 2);
		float shr = (float)Math.sin((double)roll / 2);
		float chr = (float)Math.cos((double)roll / 2);
		float w = chy*chp*chr + shy*shp*shr;
		float x = shy*chp*chr - chy*shp*shr;
		float y = chy*shp*chr + shy*chp*shr;
		float z = chy*chp*shr - shy*shp*chr;
		return new Quaternion(w, x, y, z);
	}
	
	public String toString() {
		return "[" + this.w + ", " + this.x + ", " + this.y + ", " + this.z + "]"; 
	}
	
}
