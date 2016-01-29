package com.noahbkim.cubic.network;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

public class Client {
	
	public Client(String ip, int port) {
		SocketHints hints = new SocketHints();
		Socket socket = Gdx.net.newClientSocket(Protocol.TCP, ip, port, hints);
		System.out.println("Client: connected");
		try {
			socket.getOutputStream().write("Hello, world\n".getBytes());
			System.out.println("Client: wrote to stream");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
