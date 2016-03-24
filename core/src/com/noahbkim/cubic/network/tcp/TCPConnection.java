package com.noahbkim.cubic.network.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.noahbkim.cubic.network.Connection;

public class TCPConnection implements Connection {
	
	/* Client state. */
	private boolean alive;
	
	/* Socket utility. */
	private SocketHints hints;
	private Socket socket;
	private BufferedReader input;
	private OutputStream output;
	
	/* Send receive timing. */
	private long sendTime;
	private long receiveTime;
	
	/**
	 * Create a new connection to an address.
	 * @param ip the IP address of the server.
	 * @param port the port of the server.
	 */
	public TCPConnection(String ip, int port) {
		alive = true;
		/* Try to connect to the server. */
		try {
			hints = new SocketHints();
			socket = Gdx.net.newClientSocket(Protocol.TCP, ip, port, hints);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		/* Bind the input and output streams. */
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = socket.getOutputStream();
	}
	
	/**
	 * Send a message to the server.
	 * @param contents the message to send.
	 */
	public void send(String contents) {
		/* Write the message. */
		try { output.write((contents + "\n").getBytes()); } catch (IOException e) { e.printStackTrace(); }
		log("sent \"" + contents + "\"");
	}

	/**
	 * Receive a message from the client. Blocks the thread.
	 * @return the clients message bundled with a reference to the handler.
	 */
	public String receive() {
		/* Create a null message. */
		String contents = "ERROR";
		/* Try to read a message. */
		try { contents = input.readLine(); } catch (IOException e) { e.printStackTrace(); }
		/* Try to return the message. */
		return contents;
	}
	
	/**
	 * Handle a received message.
	 * @param message the message received by the connection.
	 */
	public void handle(String message) {
		if (message.equals("PING")) {
			receiveTime = System.currentTimeMillis();
			log("ping time: " + (receiveTime - sendTime) + " ms");
		}
	}
	
	/**
	 * Run the connection.
	 */
	public void run() {
		while (alive) {
			String message = receive();
			handle(message);
			log("received \"" + message + "\"");
		}
	}
	
	/**
	 * Stop the connection.
	 */
	public void stop() {
		alive = false;
	}
	
	/**
	 * Ping the connection
	 */
	public void ping() {
		sendTime = System.currentTimeMillis();
		send("PING");
	}
	
	public void log(String message) {
		Gdx.app.log("Cubic Connection", message);
	}
	
}
