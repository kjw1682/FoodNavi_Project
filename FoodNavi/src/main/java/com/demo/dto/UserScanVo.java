package com.demo.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.demo.domain.Users;

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
public class UserScanVo {
	private String[][] searchType = {{"name", "이름"}};
	private String searchField = "";
	private String searchWord = "";
	private Page<Users> pageInfo = null;
	private List<Users> userList = null;
	private String sortBy = "";
	private String sortDirection = "";
	private int pageMaxDisplay = 0;
	
}
