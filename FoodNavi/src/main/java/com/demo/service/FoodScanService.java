package com.demo.service;

import java.util.List;

import com.demo.domain.Food;
import com.demo.domain.Users;
import com.demo.dto.FoodRecommendVo;

public interface FoodScanService {
	
	public Food getFoodByFseq(int fseq);
	
	public Food getFoodByName(String name);
	
	public List<Food> getFoodScanList(Users user, FoodRecommendVo foodRecommendVo);

	public List<Food> getFoodSearchList();
}
