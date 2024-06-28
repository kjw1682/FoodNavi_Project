package com.demo.service;

import java.util.List;

import com.demo.domain.Board;
import com.demo.persistence.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.Comments;
import com.demo.persistence.BoardCommentsRepository;

@Service
public class BoardCommentsServiceImpl implements BoardCommentsService {

	@Autowired
	BoardRepository boardRepo;
	@Autowired
	BoardCommentsRepository BoardCommentsRepo;

	@Override
	public void saveComment(Comments vo) {
		BoardCommentsRepo.save(vo);
		updateCommentCount(vo.getBoard().getBseq());
	}

	@Override
	public List<Comments> getCommentList(int bseq) {
		return BoardCommentsRepo.findParentCommentsByBseq(bseq);
	}



	@Override
	public List<Comments> getReplyCommentList(int parentCseq) {
		return BoardCommentsRepo.findRepliesByParentCommentCseq(parentCseq);
	}


	@Override
	public void deletComment(int cseq) {
		Comments comment = BoardCommentsRepo.findById(cseq).orElse(null);
		if (comment != null) {
			BoardCommentsRepo.deleteByParentComment_Cseq(cseq);
			BoardCommentsRepo.deleteById(cseq);
			updateCommentCount(comment.getBoard().getBseq());
		}
	}

	@Override
	public void deletAllComment(int bseq) {
		BoardCommentsRepo.deleteByBoardBseq(bseq);
		updateCommentCount(bseq);
	}

	@Override
	public void updateCommentCount(int bseq) {
		Board board = boardRepo.findById(bseq).orElse(null);
		if (board != null) {
			board.setCommentCount(BoardCommentsRepo.countByBoardBseq(bseq));
			boardRepo.save(board);
		}

	}

	@Override
	public List<Comments> getCommentUserList(int useq) {
		return BoardCommentsRepo.findCommentsByUseq(useq);
	}
}
