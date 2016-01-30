package com.noahbkim.cubic.network.tcp;

public class TCPHandlerMessage {

	public TCPHandler handler;
	public String contents;
	
	public TCPHandlerMessage(TCPHandler handler, String contents) {
		this.handler = handler;
		this.contents = contents;
	}
	
}
