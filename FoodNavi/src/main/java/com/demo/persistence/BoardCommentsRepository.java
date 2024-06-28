package com.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.demo.domain.Comments;

public interface BoardCommentsRepository extends JpaRepository<Comments, Integer> {

	@Query("SELECT c FROM Comments c " +
			"WHERE c.board.bseq = ?1 AND c.parentComment IS NULL " +
			"ORDER BY c.cseq DESC")
	List<Comments> findParentCommentsByBseq(int bseq);

	@Query("SELECT c FROM Comments c " +
			"WHERE c.parentComment.cseq = ?1 " +
			"ORDER BY c.cseq ASC")
	List<Comments> findRepliesByParentCommentCseq(int parentCseq);

	// 게시글 번호(bseq)에 해당하는 댓글들을 모두 삭제
	@Transactional
	void deleteByBoardBseq(int bseq);

	@Query("SELECT COUNT(c) FROM Comments c WHERE c.board.bseq = :bseq")
	int countByBoardBseq(int bseq);

	@Query("SELECT c FROM Comments c " +
			"WHERE c.user.useq = ?1 " +  // 공백 추가
			"ORDER BY c.cseq DESC")
	List<Comments> findCommentsByUseq(int useq);

	@Transactional
	void deleteByParentComment_Cseq(int cseq);
}