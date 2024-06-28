package com.demo.persistenceTest;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.demo.domain.Food;
import com.demo.domain.Users;
import com.demo.persistence.RcdRepository;
import com.demo.service.UsersService;

@SpringBootTest
public class RcdRepositoryTest {
	@Autowired
	RcdRepository rcdRepo;
	@Autowired
	UsersService usersService;
	
	@Test
	public void getRcdFoodListByUser() {
		Users user = usersService.getUser(2);
		System.out.println(user.getName());
		List<Food> rcdFoodList = rcdRepo.getRcdFoodListByUser(user);
		for (Food f : rcdFoodList) {
			System.out.println(f.getName());
		}
	}
}
