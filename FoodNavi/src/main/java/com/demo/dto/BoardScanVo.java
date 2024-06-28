package com.demo.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.demo.domain.Board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardScanVo {
	private String[][] searchType = {{"title", "제목"}, {"content", "내용"}};
	private String searchField = "";
	private String searchWord = "";
	private Page<Board> pageInfo = null;
	private List<Board> boardList = null;
	private String sortBy = "";
	private String sortDirection = "";
	private int pageMaxDisplay = 0;
}
