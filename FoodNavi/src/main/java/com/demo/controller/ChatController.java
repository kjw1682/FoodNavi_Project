package com.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.demo.domain.Food;
import com.demo.dto.ChatMessage;
import com.demo.service.FoodScanService;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.Map;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatController {
//
//	@RequestMapping("/chat")
//	public String chat() {
//		
//		return "chat";
//		
//	}


	private final FoodScanService foodScanService;
	private final SimpMessageSendingOperations messagingTemplate;
	
	@MessageMapping("/chat/message")
	public void handleMessage(ChatMessage message){
		if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
			message.setMessage(message.getSender() + "님이 입장하셨습니다.");
		} else if (ChatMessage.MessageType.BOT.equals(message.getType())){
			message.setSender("[챗봇 상담사]");
			Map<String, Object> result = new HashMap<>();
			result.put("name", searchFood(message.getMessage()).get("name"));
			result.put("fseq", searchFood(message.getMessage()).get("fseq"));
			if (!result.get("name").equals(message.getMessage())){
				if (result.get("name").equals("커뮤니티1")){
					message.setMessage("커뮤니티 페이지로 이동하시겠습니까?");
					message.setCate("/board_list");
				} else if (result.get("name").equals("나의변화1")) {
					message.setMessage("나의변화 페이지로 이동하시겠습니까?");
					message.setCate("/user_mychange_view");
				} else if (result.get("name").equals("나의활동1")) {
					message.setMessage("나의활동 페이지로 이동하시겠습니까?");
					message.setCate("/user_myactivity_view");
				} else if (result.get("name").equals("마이페이지1")) {
					message.setMessage("마이페이지 로 이동하시겠습니까?");
					message.setCate("/pw_check");
				} else if (result.get("name").equals("식단추천1")){
					message.setMessage("식단추천 페이지로 이동하시겠습니까?");
					message.setCate("/foodRecommendation");
				} else {
					String link = "https://m.coupang.com/nm/search?q=" + result.get("name");
					String foodLink = String.valueOf(result.get("fseq"));
					message.setMessage(result.get("name") + "의 주문페이지를 보여드릴게요.\n");
					message.setLink(link);
					message.setFoodLink(foodLink);
				}
			} else {
				message.setMessage("'" + result.get("name") + "'"
						+"의 요청을 처리하지 못했습니다.\n (데이터에 존재하지 않거나 처리할 수 없는 요청입니다.)");
			}
		}
		messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(),message);

	}

	public Map<String, Object> searchFood(String message){
		List<Food> foodList = foodScanService.getFoodSearchList();
		List<String> categoryList = new ArrayList<>();
		categoryList.add("커뮤니티");
		categoryList.add("나의변화");
		categoryList.add("나의활동");
		categoryList.add("마이페이지");
		categoryList.add("식단추천");
		Map<String, Object> result = new HashMap<>();
		for(Food foodName : foodList){
			if(message.replaceAll(" ", "").contains(foodName.getName().replaceAll(" ", ""))){
				result.put("name","'" + foodName.getName() + "'");
				result.put("fseq", Integer.toString(foodName.getFseq()));
				break;
			} else {
				for (String category : categoryList) {
					if(message.replaceAll(" ", "").contains(category) || message.equals(category)){
						result.put("name", category + "1");
						break;
					} else {
						result.put("name", message);
					}
				}
			}
		}
		return result;
	}


}
