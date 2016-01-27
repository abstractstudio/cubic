package com.noahbkim.cubic.network;

public class Message {

	public Handler handler;
	public String contents;
	
	public Message(Handler handler, String contents) {
		this.handler = handler;
		this.contents = contents;
	}
	
}
