package com.demo.persistence;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.domain.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {

	Page<Board> findAllByOrderByCreatedAtDesc(Pageable pageable);

	@Modifying
	@Query("update Board p set p.cnt = p.cnt+1 where p.bseq = :bseq")
	int updateCnt(@Param("bseq") int bseq);
	
	@Query("SELECT b FROM Board b "
			+ "WHERE b.title LIKE %:title% "
			+ "or b.content LIKE %:content% OR b.content IS NULL ")
	Page<Board> findBoardList(String title, String content, Pageable pageable);

	Page<Board> findByTitleContaining(String title, Pageable pageable);
	
	Page<Board> findByUserUserid(String userid, Pageable pageable);
	
	Page<Board> findByContentContaining(String writer, Pageable pageable);

	@Query("SELECT b FROM Board b ORDER BY b.cnt DESC")
	List<Board> findBestList();

	@Query("SELECT b FROM Board b WHERE b.user.useq = ?1 ORDER BY b.createdAt DESC")
	List<Board> findAuthorList(int useq);


}
