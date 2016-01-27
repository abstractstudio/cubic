package com.noahbkim.cubic.network;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.Socket;

public class Server {	
	
	/* Server state */
	private boolean alive;
	
	/* Sockets */
	private ServerSocketHints hints;
	private ServerSocket socket;
	public ArrayList<Handler> handlers;
	
	/* Queue */
	public Queue<Message> queue;
	
	/**
	 * Initialize a socket server on a port.
	 * @param port
	 */
	public Server(int port) {
		
		/* State */
		alive = true;
		
		/* Sockets */
		handlers = new ArrayList<Handler>();
		hints = new ServerSocketHints();
		hints.acceptTimeout = 0;
		
		socket = Gdx.net.newServerSocket(Protocol.TCP, port, hints);
		
		queue = new LinkedList<Message>();
	}
	
	public void listen() {
		while (alive) {
			System.out.println("Listening");
			Socket client = socket.accept(null);
			System.out.println("Accepted");
			Handler handler = new Handler(client, this);
			handlers.add(handler);
			new Thread(handler).start();
			System.out.println("Received new connection");
		}
	}
	
	public void serve() {
		System.out.println("Running server");
		while (alive) {
			while (queue.isEmpty());
			Message message = queue.poll();
			System.out.println("Got a message");
			System.out.println(message.contents);
			message.handler.send(message.contents);
		}
	}
	
	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() { listen(); }
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() { serve(); }
		}).start();
	}
	
	public void stop() {
		
	}
	
}
