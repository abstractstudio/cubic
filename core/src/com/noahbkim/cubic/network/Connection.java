package com.noahbkim.cubic.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

public class Connection implements Runnable {
	
	/* Client state. */
	private boolean alive;
	
	/* Socket utility. */
	private SocketHints hints;
	private Socket socket;
	private BufferedReader input;
	private OutputStream output;
	
	/**
	 * Create a new connection to an address.
	 * @param ip the IP address of the server.
	 * @param port the port of the server.
	 */
	public Connection(String ip, int port) {
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
	 * Run the connection.
	 */
	public void run() {
		while (alive) {
			String message = receive();
			log("received \"" + message + "\"");
		}
	}
	
	public void stop() {
		alive = false;
	}
	
	public void log(String message) {
		Gdx.app.log("Cubic Connection", message);
	}
	
}
