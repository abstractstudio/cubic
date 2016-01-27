package com.noahbkim.cubic.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.badlogic.gdx.net.Socket;

/**
 * The server side worker thread that handles communication to and from a single client.
 * @author Noah Kim
 */
public class Handler implements Runnable {

	private boolean alive;
	private Socket socket;
	private Server server;
	private BufferedReader input;
	private BufferedWriter output;
	
	public Handler(Socket socket, Server server) {
		alive = true;
		this.socket = socket;
		this.server = server;
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}
	
	public void send(String contents) {
		try {
			output.write(contents);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Message receive() {
		Message message = null;
		
		try {
			System.out.println("Starting receive");
			String contents = input.readLine();
			System.out.println("Read contents");
			System.out.println(contents);
			message = new Message(this, contents);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}
	
	public void run() {
		System.out.println("Receiving");
		while (alive) {
			Message message = receive();
			System.out.println(message);
			if (message != null) server.queue.add(message);
		}
	}
	
}
