package com.demo.service;

import com.demo.domain.UserChange;
import com.demo.domain.Users;

import java.util.List;

public interface UsersService {
	public void insertUser(Users vo); // 회원가입
	
	public int loginID(Users vo); // 로그인
	
	public Users getUser(int useq); // 아이디로 회원 조회

	public int compareID(String id);

	public List<UserChange> getWeightList(Users user);

}