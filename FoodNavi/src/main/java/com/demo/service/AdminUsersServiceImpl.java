package com.demo.service;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.demo.domain.Users;
import com.demo.dto.UserScanVo;
import com.demo.persistence.AdminUsersRepository;
@Service
public class AdminUsersServiceImpl implements AdminUsersService {

	@Autowired
	private AdminUsersRepository usersRepo;
	
	@Override
	public Page<Users> getUsersList(UserScanVo userScanVo, int page, int size) {
		Pageable pageable = null;
		if (userScanVo.getSortDirection().equals("ASC")) {
			pageable = PageRequest.of(page-1, size, Direction.ASC, userScanVo.getSortBy());
		} else {
			pageable = PageRequest.of(page-1, size, Direction.DESC, userScanVo.getSortBy());
		}
		
		String[][] searchType = userScanVo.getSearchType();
		
		String searchField = userScanVo.getSearchField();
		String searchWord = userScanVo.getSearchWord();
		List<String> searchParams = new ArrayList<>();
				
		for (String[] field : searchType) {
			if (field[0].equals(searchField)) {
				searchParams.add(searchWord);
			} else {
				searchParams.add("");
			}
		}
		
		return usersRepo.getUsersList(searchParams.get(0), pageable);
	}

	@Override
	public Users getUserByUseq(int useq) {
		return usersRepo.findById(useq).get();
	}

	@Override
	public List<Users> findAll() {

		return usersRepo.findAll();
	}

	@Override
	public void updateUser(Users user) {
		usersRepo.save(user);		
	}

}
