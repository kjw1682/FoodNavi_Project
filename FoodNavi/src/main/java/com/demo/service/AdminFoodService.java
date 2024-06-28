package com.demo.service;

import org.springframework.data.domain.Page;

import com.demo.domain.Food;
import com.demo.dto.FoodRecommendVo;


public interface AdminFoodService {
	
	public Food getFoodByFseq(int fseq);
	
	public Food getFoodByMaxFseq();
	
	public Page<Food> getFoodList(FoodRecommendVo foodScanVo, int page, int size);
	
	public void insertFood(Food fvo);

	public void updateFood(Food fvo);
}
