package com.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.FoodDetail;
import com.demo.persistence.AdminFoodDetailRepository;

@Service
public class AdminFoodDetailServiceImpl implements AdminFoodDetailService {

	@Autowired
	private AdminFoodDetailRepository foodDetailRepo;
	

	@Override
	public void insertFoodDetail(FoodDetail fdvo) {
		foodDetailRepo.save(fdvo);
		
	}


	@Override
	public FoodDetail getFoodDetailByMaxFdseq() {
		return foodDetailRepo.findFirstByOrderByFdseqDesc();
	}

}
