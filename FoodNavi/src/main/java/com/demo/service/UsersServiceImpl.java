package com.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.demo.domain.UserChange;
import com.demo.persistence.UserChangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.Users;
import com.demo.persistence.UsersRepository;

@Service
public class UsersServiceImpl implements UsersService {
	
	@Autowired
	private UsersRepository usersRepo;

	@Autowired
	private UserChangeRepository userChangeRepo;
	
	@Override
	public void insertUser(Users vo) {
		// 회원 정보 저장
		usersRepo.save(vo);
	}

	@Override
	public int loginID(Users vo) {
		int result = -1;
		int useq = 0;
		if (usersRepo.findByUserid(vo.getUserid()).isPresent()) {
			useq = usersRepo.findByUserid(vo.getUserid()).get().getUseq();
		}
			// Users 테이블에서 사용자 조회
			Optional<Users> user = usersRepo.findById(useq);
		
		// 결과값 설정 :
		// 1: ID,PWD 일치, 0: 비밀번호 불일치, -1: ID가 존재하지 않음.
		if(!user.isPresent() || user.get().getUseyn().equals("n")) {
			result = -1;
		} else {
			if (user.get().getUserpw().equals(vo.getUserpw())) {
				result = 1;
			} else {
				result = 0; //비밀번호 불일치
			}
		}
		return result;
	}

	@Override
	public Users getUser(int useq) {
		
		return usersRepo.findById(useq).get();
	}

	@Override
	public int compareID(String id) {
		int result = -1;
		Optional<Users> user = usersRepo.findByUserid(id);
		if (user.isEmpty()) {
			result = -1;
		} else if (user.get().getUserid().equals(id)) {
			result = 0;
		} else {
			result = 1;
		}
		
		return result;
	}

	@Override
	public List<UserChange> getWeightList(Users user) {
		List<UserChange> allChanges = userChangeRepo.findRecentChanges(user);
		return allChanges.stream()
				.limit(10) // 최근 10개의 데이터만 가져오기
				.collect(Collectors.toList());
	}
}



