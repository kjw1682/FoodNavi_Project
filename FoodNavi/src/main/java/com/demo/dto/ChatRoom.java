package com.demo.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoom {

	// 채팅방 구현 클래스
	// 채팅방이 입장한 클라이언트의 정보를 가지고 있어야 하므로 WebSocketSession정보 리스트를 멤버 필드로 가짐.
	private String roomId;
	private String name;
	private String roomName;


	public static ChatRoom create(String name) {
		ChatRoom chatRoom = new ChatRoom();
		chatRoom.roomId = UUID.randomUUID().toString();
		chatRoom.name = name;
		return chatRoom;

	}

	public static ChatRoom botCreate(String name){
		ChatRoom chatRoom = new ChatRoom();
		chatRoom.roomId = "chatbot"+UUID.randomUUID().toString();
		chatRoom.name = name;
		return chatRoom;
	}

}
