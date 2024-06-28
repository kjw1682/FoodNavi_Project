package com.demo.service;

import java.util.List;


import com.demo.domain.Comments;

public interface BoardCommentsService {
	
	public void saveComment(Comments vo);
	
	public List<Comments> getCommentList(int bseq);
	
	public List<Comments> getReplyCommentList(int parentCseq);

	public void deletComment(int cseq);
	
	public void deletAllComment(int bseq);

	public void updateCommentCount(int bseq);

	public List<Comments> getCommentUserList(int useq);
}
