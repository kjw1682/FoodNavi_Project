package com.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.demo.domain.Food;
import com.demo.domain.Users;
import com.demo.dto.FoodRecommendVo;
import com.demo.dto.UserVo;
import com.demo.persistence.FoodScanRepository;

@Service
public class FoodScanServiceImpl implements FoodScanService {
	
	@Autowired
	private FoodScanRepository foodScanRepo;
	
	@Override
	public Food getFoodByFseq(int fseq) {
		return foodScanRepo.findById(fseq).get();
	}

	@Override
	public Food getFoodByName(String name) {
		return foodScanRepo.findByName(name);
	}
	
	@Override
	public List<Food> getFoodScanList(Users user, FoodRecommendVo foodScanVo) {

		UserVo userVo = new UserVo(user);
		
		String[][] searchType = foodScanVo.getSearchType();
		
		// 검색어 반영
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
		
		// 금지어 반영
		String banField = foodScanVo.getBanField();
		String banWord = foodScanVo.getBanWord();
		List<String> banParams = new ArrayList<>();
				
		for (String[] field : searchType) {
			if (field[0].equals(banField) && !banWord.equals("")) {
				banParams.add(banWord);
			} else {
				banParams.add("|");
			}
		}
		
		// 한끼유형 반영
		List<String> mealParams = new ArrayList<>();
		List<String> mealCheck = Arrays.asList(foodScanVo.getMealTime());
		for (String[] meal : foodScanVo.getMealTimeArray()) {
			if (mealCheck.contains(meal[0])) {
				mealParams.add(meal[0]);
			} else {
				mealParams.add("");
			}
		}
		
		if (mealParams.size() == 0) {
			mealParams.add(foodScanVo.getMealTimeByTime());
		}

		
		// 음식유형 반영
		String foodType = foodScanVo.getFoodType();
		if (foodType.equals("all")) {
			foodType = "";
		}
		
		// 알레르기 반영
		List<String> allergyParams = Arrays.asList(foodScanVo.getAllergys());
		String allergyEtc = foodScanVo.getAllergyEtc();
		if (allergyEtc.equals("")) {
			allergyEtc = "|";
		}
		
		// 채식여부 반영	
		int vegetarian = 0;
		String vegetarianField = foodScanVo.getVegetarian();
		if (!vegetarianField.equals("0")) {
			vegetarian = Integer.parseInt(vegetarianField);
		}
		
		float kcalMin = 0f;
		float kcalMax = 1000000f;
		float carbMin = 0f;
		float carbMax = 1000000f;
		float prtMin = 0f;
		float prtMax = 1000000f;
		float fatMin = 0f;
		float fatMax = 1000000f;
		String purpose = foodScanVo.getPurpose();
		// 목적에 따라 수치 변경
		if (purpose.equals("diet")) {
			kcalMin = 0f;
			kcalMax = userVo.getEER()/3*0.7f;
			carbMin = 0f;
			carbMax = 1000000f;
			prtMin = 0f;
			prtMax = 1000000f;
			fatMin = 0f;
			fatMax = 1000000f;
		} else if (purpose.equals("bulkup")) {
			kcalMin = userVo.getEER()/3*1.0f;
			kcalMax = userVo.getEER()/3*1.3f;
			carbMin = 0f;
			carbMax = 1000000f;
			prtMin = 0f;
			prtMax = 1000000f;
			fatMin = 0f;
			fatMax = 1000000f;
		} 
		
		float ratioCarbMin = 0f;
		float ratioCarbMax = 1000000f;
		float ratioPrtMin = 0f;
		float ratioPrtMax = 1000000f;
		float ratioFatMin = 0f;
		float ratioFatMax = 1000000f;
		String dietType = foodScanVo.getDietType();
		// 균형식 : 탄단지 균형 비율 기준
		// 저탄고지 : 탄단지 비율 1:2:7
		if (dietType.equals("balance")) {
			ratioCarbMin = userVo.getProperCarbRatio()[0];
			ratioCarbMax = userVo.getProperCarbRatio()[1];
			ratioPrtMin = userVo.getProperPrtRatio()[0];
			ratioPrtMax = userVo.getProperPrtRatio()[1];
			ratioFatMin = userVo.getProperFatRatio()[0];
			ratioFatMax = userVo.getProperFatRatio()[1];
		} else if (dietType.equals("lowCarb")) {
			ratioCarbMin = 0.00f;
			ratioCarbMax = 0.20f;
			ratioPrtMin = 0.10f;
			ratioPrtMax = 0.30f;
			ratioFatMin = 0.60f;
			ratioFatMax = 0.80f;
		}
		
		List<Food> foodList = null;
		if (foodScanVo.isRecommend()) {
			int check = foodScanRepo.getFoodCountByMealTypeInHistory(
					mealParams.get(0), mealParams.get(1), mealParams.get(2), mealParams.get(3));
			if (check < 10) {
				foodScanVo.setRecommend(false);				
			} 
		} 
		
		if (foodScanVo.isRecommend()) {
			foodList = foodScanRepo.getFoodRecommendList(
					searchParams.get(0), searchParams.get(1), banParams.get(0), banParams.get(1),
					mealParams.get(0), mealParams.get(1), mealParams.get(2), mealParams.get(3),
					kcalMin, kcalMax, carbMin, carbMax, prtMin, prtMax, fatMin, fatMax,	
					ratioCarbMin, ratioCarbMax, ratioPrtMin, ratioPrtMax, ratioFatMin, ratioFatMax, 
					allergyParams.get(0), allergyParams.get(1), allergyParams.get(2), allergyParams.get(3), allergyEtc, 
					vegetarian, foodType);
		} else {
			foodList = foodScanRepo.getFoodScanList(
					searchParams.get(0), searchParams.get(1), banParams.get(0), banParams.get(1),
					kcalMin, kcalMax, carbMin, carbMax, prtMin, prtMax, fatMin, fatMax,	
					ratioCarbMin, ratioCarbMax, ratioPrtMin, ratioPrtMax, ratioFatMin, ratioFatMax, 
					allergyParams.get(0), allergyParams.get(1), allergyParams.get(2), allergyParams.get(3), allergyEtc, 
					vegetarian, foodType);
		}
		
		
		return foodList;
	}

	@Override
	public List<Food> getFoodSearchList() {
		return foodScanRepo.getAllByFood();
	}

}
