package com.noahbkim.cubic.network;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.Socket;
import com.noahbkim.cubic.Settings;

/**
 * The server side of the Cubic game. Hosts the physics engine as well as the main game processing.
 * @author Noah Kim
 */
public class Server {
	
	/* Server state. */
	private boolean alive;
	
	/* Settings. */
	public Settings settings;
	public String host;
	public int port;
	
	/* Socket utilities. */
	private ServerSocketHints hints;
	private ServerSocket socket;
	public ArrayList<Handler> handlers;
	public ArrayList<Thread> threads;
	public LinkedBlockingQueue<HandlerMessage> queue;
	
	/**
	 * Initialize a socket server on a port.
	 * @param port the port to initialize the server on
	 */
	public Server() {
		/* Start the server. */
		alive = true;
		/* Settings. */
		settings = new Settings("cubic.settings");
		host = (String)settings.get("host");
		port = (int)(Integer)settings.get("port");
		/* Create the handler list and message queue. */
		handlers = new ArrayList<Handler>();
		threads = new ArrayList<Thread>();
		queue = new LinkedBlockingQueue<HandlerMessage>();
		/* Create the socket. */
		hints = new ServerSocketHints();
		hints.acceptTimeout = 0;
		socket = Gdx.net.newServerSocket(Protocol.TCP, host, port, hints);
		/* Logging. */
		log("initialized on " + host + ":" + port);

	}
	
	/**
	 * Listen for any incoming connections.
	 */
	public void listen() {
		/* Loop the listener. */
		log("started listening");
		while (alive) {
			/* Accept a new connection and create a new handler. */
			Socket client = socket.accept(null);
			Handler handler = new Handler(handlers.size(), client, this);
			handlers.add(handler);
			/* Keep track of the thread. */
			Thread thread = new Thread(handler);
			thread.start();
			threads.add(thread);
			log("received new connection from " + client.getRemoteAddress());
		}
		log("exited listener loop");
	}
	
	/**
	 * Execute the main server update.
	 */
	public void serve() {
		/* Loop the main server. */
		log("started serving");
		while (alive) {
			/* Create a blank message. */
			HandlerMessage message = null;
			/* Try to pull one from the queue. */
			try { message = queue.take(); } catch (InterruptedException e) { e.printStackTrace(); return; }
			/* Echo the message. */
			log("received \"" + message.contents + "\"");
			message.handler.send(message.contents);
		}
		log("exited server loop");
		socket.dispose();
	}
	
	/**
	 * Start the server.
	 */
	public void start() {
		/* Start the listener and server. */
		Thread listener = new Thread(new Runnable() { public void run() { listen(); } });
		Thread server = new Thread(new Runnable() { public void run() { serve(); } });
		listener.start();
		server.start();
		threads.add(listener);
		threads.add(server);
		/* Shut down the server. */
		log("server started");
	}
	
	/**
	 * Stop the server.
	 */
	public void stop() {
		alive = false;
		for (Thread thread : this.threads) { thread.interrupt(); }
	}
	
	public void log(String message) {
		Gdx.app.log("Cubic Server", message);
	}
	
}
