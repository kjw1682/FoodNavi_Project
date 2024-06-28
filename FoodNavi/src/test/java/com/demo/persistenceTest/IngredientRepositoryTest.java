package com.demo.persistenceTest;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.demo.domain.Ingredient;
import com.demo.persistence.IngredientRepository;
import com.demo.service.FoodScanService;

@SpringBootTest
public class IngredientRepositoryTest {
	
	@Autowired
	FoodScanService foodScanService;
	
	@Autowired
	IngredientRepository ingredientRepo;
	
	@Disabled
	@Test
	public void IngredientListInFood() {
		int fseq = 1;
		List<Ingredient> ingredientListInFood = ingredientRepo.getIngredientListInFood(fseq);
		for (Ingredient s : ingredientListInFood) {
			System.out.println(s.getName());
		}
	}

}
