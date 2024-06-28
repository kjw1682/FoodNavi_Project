package com.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.domain.Board;
import com.demo.domain.Comments;
import com.demo.domain.Users;
import com.demo.service.BoardCommentsService;
import com.demo.service.UsersService;

import jakarta.servlet.http.HttpSession;
@RestController
@RequestMapping("/board_detail/")
public class BoardCommentsController {

	@Autowired
	BoardCommentsService boardCommentsService;
	@Autowired
	UsersService usersService;

	@GetMapping(value = "/list")
	@ResponseBody
	public Map<String, Object> getComments(@RequestParam(value = "bseq") int bseq, HttpSession session) {
		Map<String, Object> result = new HashMap<>();
		System.out.println(1);
		// 세션에서 사용자 정보 가져오기
		Users user = (Users) session.getAttribute("loginUser");
		int currentUser = user.getUseq();

		// 댓글 목록 가져오기
		List<Comments> parentComments = boardCommentsService.getCommentList(bseq);
		int[] parentCommentCseqArray = new int[parentComments.size()];
		int[] parentCommentUseqArray = new int[parentComments.size()];
		String[] parentCommentUserArray = new String[parentComments.size()];
		String[] parentCommentContentArray = new String[parentComments.size()];
		String[] parentCommentDateArray = new String[parentComments.size()];
		
		for (int i = 0 ; i < parentComments.size() ; i++) {
			parentCommentCseqArray[i] = parentComments.get(i).getCseq();
			parentCommentUseqArray[i] = parentComments.get(i).getUser().getUseq();
			Users tmp_user = usersService.getUser(parentComments.get(i).getUser().getUseq());
			parentCommentUserArray[i] = tmp_user.getName()+"("+tmp_user.getUserid()+")";
			parentCommentContentArray[i] = parentComments.get(i).getContent();
			String date = parentComments.get(i).getCreatedAt().toString();
			// date = date.substring(0, date.length()-4);
			parentCommentDateArray[i] = date;
		}
		
		
		int[][] childComentCseqArray = new int[parentComments.size()][];
		int[][] childCommentUseqArray = new int[parentComments.size()][];
		String[][] childCommentUserArray = new String[parentComments.size()][];
		String[][] childCommentContentArray = new String[parentComments.size()][];
		String[][] childCommentDateArray = new String[parentComments.size()][];
		
				
		Map<Integer, List<Comments>> commentsMap = new HashMap<>();
		
		int[] tmp_CseqArray = null;
		int[] tmp_UseqArray = null;
		String[] tmp_UserArray = null;
		String[] tmp_ContentArray = null;
		String[] tmp_DateArray = null;		
		// 부모 댓글마다 대댓글 목록 가져오기
		for (int i = 0 ; i < parentComments.size() ; i++) {
			List<Comments> replies = boardCommentsService.getReplyCommentList(parentComments.get(i).getCseq());
			tmp_CseqArray = new int[replies.size()];
			tmp_UseqArray = new int[replies.size()];
			tmp_UserArray = new String[replies.size()];
			tmp_ContentArray = new String[replies.size()];
			tmp_DateArray = new String[replies.size()];
			for (int j = 0 ; j < replies.size(); j++) {
				tmp_CseqArray[j] = replies.get(j).getCseq();
				tmp_UseqArray[j] = replies.get(j).getUser().getUseq();				
				Users tmp_user = usersService.getUser(replies.get(j).getUser().getUseq());
				tmp_UserArray[j] = tmp_user.getName()+"("+tmp_user.getUserid()+")";				
				tmp_ContentArray[j] = replies.get(j).getContent();
				String date = replies.get(j).getCreatedAt().toString();
				// date = date.substring(0, date.length()-4);
				tmp_DateArray[j] = date;
			}
			childComentCseqArray[i] = tmp_CseqArray;
			childCommentUseqArray[i] = tmp_UseqArray;
			childCommentUserArray[i] = tmp_UserArray;
			childCommentContentArray[i] = tmp_ContentArray;
			childCommentDateArray[i] = tmp_DateArray;			
		}
		
		// 대댓글을 포함한 댓글 수 계산
		int totalCommentCount = parentComments.size(); // 부모 댓글 수를 초기화
		for (List<Comments> replies : commentsMap.values()) {
			totalCommentCount += replies.size(); // 각 부모 댓글에 대한 대댓글 수를 더함
		}

		result.put("currentUser", currentUser);
		result.put("commentCount", totalCommentCount); // 대댓글을 포함한 총 댓글 수를 전달
		result.put("parentCommentCseqArray", parentCommentCseqArray);
		result.put("parentCommentUseqArray", parentCommentUseqArray);
		result.put("parentCommentUserArray", parentCommentUserArray);
		result.put("parentCommentContentArray", parentCommentContentArray);
		result.put("parentCommentDateArray", parentCommentDateArray);
		result.put("childCommentCseqArray", childComentCseqArray);
		result.put("childCommentUseqArray", childCommentUseqArray);
		result.put("childCommentUserArray", childCommentUserArray);
		result.put("childCommentContentArray", childCommentContentArray);
		result.put("childCommentDateArray", childCommentDateArray);

		return result;
	}



	@PostMapping(value = "/save")
	public Map<String, Object> saveCommentAction(@RequestParam(value = "bseq",required = false) int bseq,
												 @RequestParam(value = "cseq", required = false) Integer cseq,
												 @RequestParam(value = "CommentContent", required = false) String content,
												 HttpSession session) {
		Map<String, Object> map = new HashMap<>();
		Users user = (Users) session.getAttribute("loginUser");

		if (user == null) { // 로그인 되어 있지 않음.
			map.put("result", "not_logedin");
		} else {
			if (cseq == null) {
				// 원댓글 저장
				if (content == null || content.isEmpty()) {
					map.put("result", "fail");
				} else {
					Comments vo = new Comments(); // Comments 객체 생성
					vo.setUser(user);

					Board b = new Board();
					b.setBseq(bseq);
					vo.setBoard(b);
					vo.setContent(content);

					try {
						boardCommentsService.saveComment(vo);
						map.put("result", "success");
					} catch (Exception e) {
						map.put("result", "fail");
						e.printStackTrace(); // 에러 로그 출력
					}
				}
			}
			}

		return map;
	}

	@PostMapping(value = "/rplSave")
	public Map<String, Object> saveReplyAction(@RequestParam(value = "bseq", required = false) Integer bseq,
											   @RequestParam(value = "ReplyContent", required = false) String replyContent,
											   @RequestParam(value = "cseq", required = false) Integer cseq,
											   HttpSession session) {

		Map<String, Object> map = new HashMap<>();
		Users user = (Users) session.getAttribute("loginUser");

		if (user == null) { // 로그인 되어 있지 않음.
			map.put("result", "not_logedin");
		} else {
			if (replyContent == null || replyContent.isEmpty()) {
				map.put("result", "fail");
			} else {
				Comments vo = new Comments(); // Comments 객체 생성
				vo.setUser(user);

				Comments parentComment = new Comments();
				parentComment.setCseq(cseq);
				vo.setParentComment(parentComment);

				Board b = new Board();
				b.setBseq(bseq);
				vo.setBoard(b);
				vo.setContent(replyContent);

				try {
					boardCommentsService.saveComment(vo);
					map.put("result", "success");
				} catch (Exception e) {
					map.put("result", "fail");
					e.printStackTrace(); // 에러 로그 출력
				}
			}
		}
		return map;
	}


	// 댓글 삭제
	@PostMapping(value = "/delete")
	public Map<String, Object> deleteCommentAction(@RequestParam(value = "cseq") int cseq) {

		Map<String, Object> map = new HashMap<>();

		boardCommentsService.deletComment(cseq);
		map.put("result", "success");

		return map;
	}
}
