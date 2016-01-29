package com.noahbkim.cubic.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import sun.util.logging.resources.logging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;

/**
 * The server side worker thread that handles communication to and from a single client.
 * @author Noah Kim
 */
public class Handler implements Runnable {

	/* Handler state. */
	public boolean alive;
	public int index;
	
	/* Handler socket. */
	private Socket socket;
	private BufferedReader input;
	private BufferedWriter output;
	
	/* Handler server. */
	private Server server;
	
	/**
	 * Create a new handler from a client connection.
	 * @param socket the client socket.
	 * @param server the server the handler works for.
	 */
	public Handler(int index, Socket socket, Server server) {
		/* Start the handler. */
		alive = true;
		this.index = index;
		/* Track the socket and server. */
		this.socket = socket;
		this.server = server;
		/* Bind the input and output streams. */
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}
	
	/**
	 * Send a string to the connected client.
	 * @param contents the contents of the message.
	 */
	public void send(String contents) {
		/* Write the message. */
		try { output.write(contents); } catch (IOException e) { e.printStackTrace(); }
	}
	
	/**
	 * Receive a message from the client. Blocks the thread.
	 * @return the clients message bundled with a reference to the handler.
	 */
	public HandlerMessage receive() {
		/* Create a null message. */
		HandlerMessage message = new HandlerMessage(this, "ERROR");
		/* Try to read a message. */
		try {
			String contents = input.readLine();
			message = new HandlerMessage(this, contents);
		} catch (IOException e) {
			e.printStackTrace();
		}
		/* Try to return the message. */
		return message;
	}
	
	/**
	 * Run the handler by receiving and passing messages to the server.
	 */
	public void run() {
		while (alive) {
			HandlerMessage message = receive();
			if (message != null) server.queue.add(message);
		}
	}
	
	/**
	 * Log a message as the handler.
	 * @param message the message to log.
	 */
	public void log(String message) {
		Gdx.app.log("Cubic Handler #" + index, message);
	}
	
}
