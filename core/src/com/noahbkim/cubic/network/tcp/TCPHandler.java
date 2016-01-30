package com.noahbkim.cubic.network.tcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.noahbkim.cubic.network.Handler;

/**
 * The server side worker thread that handles communication to and from a single client.
 * @author Noah Kim
 */
public class TCPHandler implements Handler {

	/* Handler state. */
	public boolean alive;
	public int index;
	
	/* Handler socket. */
	private Socket socket;
	private BufferedReader input;
	private OutputStream output;
	
	/* Handler server. */
	private TCPServer server;
	
	/**
	 * Create a new handler from a client connection.
	 * @param socket the client socket.
	 * @param server the server the handler works for.
	 */
	public TCPHandler(int index, Socket socket, TCPServer server) {
		/* Start the handler. */
		alive = true;
		this.index = index;
		/* Track the socket and server. */
		this.socket = socket;
		this.server = server;
		/* Bind the input and output streams. */
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = socket.getOutputStream();
	}
	
	/**
	 * Send a string to the connected client.
	 * @param contents the contents of the message.
	 */
	public void send(String contents) {
		/* Write the message. */
		try { output.write((contents + "\n").getBytes()); } catch (IOException e) { e.printStackTrace(); }
	}
	
	/**
	 * Receive a message from the client. Blocks the thread.
	 * @return the clients message bundled with a reference to the handler.
	 */
	public String receive() {
		/* Try to read a message. */
		String contents = null;
		try {
			contents = input.readLine();
			log("received \"" + contents + "\"");
		} catch (IOException e) {
			e.printStackTrace();
		}
		/* Try to return the message. */
		return contents;
	}
	
	/**
	 * Run the handler by receiving and passing messages to the server.
	 */
	public void run() {
		log("started");
		while (alive) {
			String contents = receive();
			if (contents == null) {
				alive = false;
				log("failed in receive");
				return;
			}
			TCPHandlerMessage message = new TCPHandlerMessage(this, contents);
			if (message != null) server.queue.add(message);
		}
		log("stopped");
	}
	
	/**
	 * Log a message as the handler.
	 * @param message the message to log.
	 */
	public void log(String message) {
		Gdx.app.log("Cubic Handler #" + index, message);
	}
	
}
