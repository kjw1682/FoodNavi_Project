package com.demo.service;

import com.demo.domain.FoodIngredient;

import java.util.List;

public interface FoodIngredientService {
	public void insertFoodIngredient(FoodIngredient foodIngredient);

	public List<FoodIngredient> getFoodIngredientListByFood(int fseq);

	public void deleteFoodIngredient(FoodIngredient foodIngredient);
}
