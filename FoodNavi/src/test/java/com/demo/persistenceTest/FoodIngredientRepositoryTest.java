package com.demo.persistenceTest;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.demo.domain.FoodIngredient;
import com.demo.persistence.FoodIngredientRepository;

@SpringBootTest
public class FoodIngredientRepositoryTest {
	@Autowired
	private FoodIngredientRepository foodIngredientRepo;
	
	@Disabled
	@Test
	public void getFoodIngredienitListByFood() {
		List<FoodIngredient> foodIngredientList = foodIngredientRepo.getFoodIngredientListByFood(1);
		for (FoodIngredient fi : foodIngredientList) {
			System.out.println(fi.getIngredient().getName());
		}
	}
}
