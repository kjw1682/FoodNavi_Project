package com.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {
	
	// 채팅 메세지를 구현하는 클래스
	
	// 메세지타입 : 입장, 채팅 두가지임
	public enum MessageType{
		ENTER, TALK, BOT
	}
	
	private MessageType type;
	private String roomId;
	private String sender;
	private String message;
	private String link;
	private String cate;
	private String foodLink;
	
}
