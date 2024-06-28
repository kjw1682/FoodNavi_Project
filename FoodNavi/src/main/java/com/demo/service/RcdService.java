package com.demo.service;

import com.demo.domain.Food;
import com.demo.domain.Users;

import java.util.List;

public interface RcdService {
	public int rcdStatus(Users user, Food food);
	public int rcdUpdate(Users user, Food food);
	public int getRcdCountByFood(Food food);
	public List<Food> getRcdFoodListByUser(Users user);
}
