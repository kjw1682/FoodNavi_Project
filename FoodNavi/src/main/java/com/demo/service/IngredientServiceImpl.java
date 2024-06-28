package com.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.Ingredient;
import com.demo.persistence.IngredientRepository;

@Service
public class IngredientServiceImpl implements IngredientService {

	@Autowired
	private IngredientRepository ingredientRepo;

	@Override
	public Ingredient findById(int iseq) {
		return ingredientRepo.findById(iseq);
	}

	@Override
	public Optional<Ingredient> findByName(String name) {
		
		return ingredientRepo.findByName(name);
	}

}
