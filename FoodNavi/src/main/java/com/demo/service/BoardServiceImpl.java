package com.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.demo.domain.Board;
import com.demo.dto.BoardScanVo;
import com.demo.persistence.BoardRepository;

import jakarta.transaction.Transactional;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardRepository boardRepo;

	@Override
	public void insertBoard(Board vo) {
		boardRepo.save(vo);
	}

	@Override
	public Board getBoard(int bseq) {
		return boardRepo.findById(bseq).get();
	}

	@Override
	public void editBoard(Board vo) {
		boardRepo.save(vo);
	}

	@Override
	public void deleteBoard(int bseq) {
	    boardRepo.deleteById(bseq); 
	}
	
	@Override
	@Transactional
	public int updateCnt(int bseq) {
		return boardRepo.updateCnt(bseq);
	}

	@Override
	public List<Board> getBestBoardList() {
		return boardRepo.findBestList();
	}

	@Override
	public List<Board> getAuthorBoardList(int useq) {
		return boardRepo.findAuthorList(useq);
	}

	@Override
	public void likePost(int bseq) {
		Board board = boardRepo.findById(bseq).orElseThrow(() -> new RuntimeException("Board not found"));
		board.setLikes(board.getLikes() + 1);
		boardRepo.save(board);
	}

	@Override
	public void unlikePost(int bseq) {
		Board board = boardRepo.findById(bseq).orElseThrow(() -> new RuntimeException("Board not found"));
		board.setLikes(board.getLikes() - 1);
		boardRepo.save(board);
	}


	@Override
	public Page<Board> findBoardList(BoardScanVo boardScanVo, int page, int size) {
	    Pageable pageable = null;
	    if (boardScanVo.getSortDirection().equals("ASC")) {
	        pageable = PageRequest.of(page - 1, size, Direction.ASC, boardScanVo.getSortBy());
	    } else {
	        pageable = PageRequest.of(page - 1, size, Direction.DESC, boardScanVo.getSortBy());
	    }

	    String searchField = boardScanVo.getSearchField();
	    String searchWord = boardScanVo.getSearchWord();

	    Page<Board> resultPage = null;
	    switch (searchField) {
	        case "title":
	            resultPage = boardRepo.findByTitleContaining(searchWord, pageable);
	            break;
	        case "content":
	            resultPage = boardRepo.findByContentContaining(searchWord, pageable);
	            break;
	        case "writer":
	            resultPage = boardRepo.findByUserUserid(searchWord, pageable);
	            break;
	        case "titleContent":
	            resultPage = boardRepo.findBoardList(searchWord, searchWord, pageable);
	            break;
	        default:
	            resultPage = boardRepo.findAllByOrderByCreatedAtDesc(pageable);
	            break;
	    }


	    return resultPage;
	}

}
