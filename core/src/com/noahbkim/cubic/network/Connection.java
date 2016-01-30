package com.noahbkim.cubic.network;

public interface Connection extends Runnable {
	public void send(String contents);
	public String receive();
}
