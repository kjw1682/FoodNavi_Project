package com.demo.persistenceTest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.demo.domain.Users;
import com.demo.persistence.UsersRepository;

@SpringBootTest
public class UsersRepoistoryTest {
	@Autowired
	private UsersRepository usersRepo;
	
	@Disabled
	@Test
	public void insertUsersTest() {
		Users vo = Users.builder()
				.userid("one")
				.userpw("1234")
				.name("홍길동")
				.sex("M")
				.age(30)
				.height(180)
				.weight(75)
				.build();
		
		usersRepo.save(vo);
	}
	
	@Disabled
	@Test
	public void deleteUsersTest() {
		Users vo = usersRepo.findByUserid("sn").get();
		usersRepo.delete(vo);
	}
}
