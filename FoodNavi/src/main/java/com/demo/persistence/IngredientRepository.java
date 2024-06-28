package com.demo.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
	public Ingredient findById(int iseq);
	public Optional<Ingredient> findByName(String name);
	public Ingredient findFirstByOrderByIseqDesc();
	List<Ingredient> findByNameContainingIgnoreCase(String term);

	@Query("SELECT ingredient FROM Ingredient ingredient, FoodIngredient foodIngredient "
			+ "WHERE ingredient = foodIngredient.ingredient "
			+ "AND foodIngredient.food.fseq = :fseq ")
	public List<Ingredient> getIngredientListInFood(int fseq);
}