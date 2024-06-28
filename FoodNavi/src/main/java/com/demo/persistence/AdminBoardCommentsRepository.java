package com.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.demo.domain.Comments;

public interface AdminBoardCommentsRepository extends JpaRepository<Comments, Integer> {
	
	@Query("SELECT c FROM Comments c WHERE c.board.bseq = ?1")
		List<Comments> findCommentsByBseq(int bseq);
	
	@Query("SELECT COUNT(c) FROM Comments c " +
	           "WHERE c.board.bseq =?1")
		int getTotalCommentCount(int bseq);

	// 게시글 번호(bseq)에 해당하는 댓글들을 모두 삭제
    @Transactional
    void deleteByBoardBseq(int bseq);
}
