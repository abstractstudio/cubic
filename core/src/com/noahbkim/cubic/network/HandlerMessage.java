package com.noahbkim.cubic.network;

public class HandlerMessage {

	public Handler handler;
	public String contents;
	
	public HandlerMessage(Handler handler, String contents) {
		this.handler = handler;
		this.contents = contents;
	}
	
}
