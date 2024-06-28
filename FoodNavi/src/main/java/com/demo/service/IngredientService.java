package com.demo.service;

import java.util.Optional;

import com.demo.domain.Ingredient;

public interface IngredientService {
	public Ingredient findById(int iseq);
	public Optional<Ingredient> findByName(String name);
	
}
