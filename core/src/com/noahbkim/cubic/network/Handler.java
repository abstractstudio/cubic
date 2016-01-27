package com.noahbkim.cubic.network;

import com.badlogic.gdx.net.Socket;

public class Handler implements Runnable {

	private Socket socket;
	private Server server;
	
	public Handler(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
	}

	public void run() {
		
	}
	
}
