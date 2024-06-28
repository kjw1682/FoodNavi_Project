package com.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.domain.FoodIngredient;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FoodIngredientRepository extends JpaRepository<FoodIngredient, Integer> {
    @Query("SELECT fi FROM FoodIngredient fi WHERE fi.food.fseq = :fseq")
    public List<FoodIngredient> getFoodIngredientListByFood(int fseq);
}