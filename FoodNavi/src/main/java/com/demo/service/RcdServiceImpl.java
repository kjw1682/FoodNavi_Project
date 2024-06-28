package com.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.Food;
import com.demo.domain.Rcd;
import com.demo.domain.Users;
import com.demo.persistence.RcdRepository;

@Service
public class RcdServiceImpl implements RcdService {
	
	@Autowired
	private RcdRepository rcdRepo;
	
	@Override
	public int rcdStatus(Users user, Food food) {
		int result = 0;
		Optional<Rcd> rcdOp = rcdRepo.findByUserAndFood(user, food);
		if (!rcdOp.isEmpty())
			result++;		
		
		return result;
	}
	
	
	public int rcdUpdate(Users user, Food food) {
		int result = 0;
		Optional<Rcd> rcdOp = rcdRepo.findByUserAndFood(user, food);
		if (rcdOp.isEmpty()) {
			Rcd rcd = new Rcd();
			rcd.setUser(user);
			rcd.setFood(food);
			rcdRepo.save(rcd);
			result = 1;
		} else {
			rcdRepo.delete(rcdOp.get());
		}
		
		return result;
	}

	@Override
	public int getRcdCountByFood(Food food) {
		List<Rcd> rcdList = rcdRepo.findByFood(food);
		return rcdList.size();
	}


	@Override
	public List<Food> getRcdFoodListByUser(Users user) {
		return rcdRepo.getRcdFoodListByUser(user);
	}


}
