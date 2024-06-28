package com.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.demo.domain.Food;
import com.demo.dto.FoodRecommendVo;
import com.demo.persistence.AdminFoodRepository;

@Service
public class AdminFoodServiceImpl implements AdminFoodService {

	@Autowired
	private AdminFoodRepository foodRepo;
	
	@Override
	public void insertFood(Food fvo) {
		foodRepo.save(fvo);
	}
	
	@Override
	public Food getFoodByFseq(int fseq) {
		return foodRepo.findById(fseq).get();
	}
	
	@Override
	public Food getFoodByMaxFseq() {
		return foodRepo.findFirstByOrderByFseqDesc();
	}

	@Override
	public Page<Food> getFoodList(FoodRecommendVo foodScanVo, int page, int size) {
		Pageable pageable = null;
		if (foodScanVo.getSortDirection().equals("ASC")) {
			pageable = PageRequest.of(page-1, size, Direction.ASC, foodScanVo.getSortBy());
		} else {
			pageable = PageRequest.of(page-1, size, Direction.DESC, foodScanVo.getSortBy());
		}
		
		String[][] searchType = foodScanVo.getSearchType();
		
		String searchField = foodScanVo.getSearchField();
		String searchWord = foodScanVo.getSearchWord();
		List<String> searchParams = new ArrayList<>();
				
		for (String[] field : searchType) {
			if (field[0].equals(searchField)) {
				searchParams.add(searchWord);
			} else {
				searchParams.add("");
			}
		}
		
		return foodRepo.getFoodList(searchParams.get(0), pageable);
	}

	@Override
	public void updateFood(Food fvo) {
		
		foodRepo.save(fvo);
		
	}
}
