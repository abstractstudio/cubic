package com.noahbkim.cubic.network;

public interface Handler extends Runnable {
	public void send(String contents);
	public String receive();
}
