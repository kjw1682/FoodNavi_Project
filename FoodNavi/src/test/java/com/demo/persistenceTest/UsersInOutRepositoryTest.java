package com.demo.persistenceTest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.demo.persistence.UsersInOutRepository;

@SpringBootTest
public class UsersInOutRepositoryTest {
	
	@Autowired
	private UsersInOutRepository usersInOutRepo;
	
	@Disabled
	@Test
	public void getTotalUsersCount() {
		System.out.println(usersInOutRepo.getTotalUsersCount());
	}
}
