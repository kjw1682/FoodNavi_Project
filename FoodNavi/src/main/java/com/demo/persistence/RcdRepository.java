package com.demo.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.Food;
import com.demo.domain.Rcd;
import com.demo.domain.Users;

public interface RcdRepository extends JpaRepository<Rcd, Integer> {
	
	public Optional<Rcd> findByUserAndFood(Users user, Food food);
	public List<Rcd> findByFood(Food food);
	public List<Rcd> findByUser(Users user);
	
	@Query("SELECT food From Food food, Rcd rcd, Users user WHERE food = rcd.food AND user = rcd.user ")
	public List<Food> getRcdFoodListByUser(Users user);
}
