package com.demo.service;


import com.demo.domain.FoodDetail;

public interface AdminFoodDetailService {
	
	public void insertFoodDetail(FoodDetail fdvo);

	public FoodDetail getFoodDetailByMaxFdseq();
	
}
