package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.FoodIngredient;
import com.demo.persistence.FoodIngredientRepository;

import java.util.List;

@Service
public class FoodIngredientServiceImpl implements FoodIngredientService {

	@Autowired
	private FoodIngredientRepository foodIngredientRepo;
	@Override
	public void insertFoodIngredient(FoodIngredient foodIngredient) {
		foodIngredientRepo.save(foodIngredient);

	}

	@Override
	public List<FoodIngredient> getFoodIngredientListByFood(int fseq) {

		return foodIngredientRepo.getFoodIngredientListByFood(fseq);
	}

	@Override
	public void deleteFoodIngredient(FoodIngredient foodIngredient) {
		foodIngredientRepo.delete(foodIngredient);
	}

}
