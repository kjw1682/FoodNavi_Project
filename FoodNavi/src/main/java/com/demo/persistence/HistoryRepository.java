package com.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.Food;
import com.demo.domain.History;
import com.demo.domain.Users;

public interface HistoryRepository extends JpaRepository<History, Integer> {
	@Query("SELECT history FROM History history "
			+ "WHERE history.user = :user "
			+ "AND history.servedDate IS NULL ")
	public List<History> getHistoryListNotConfirmedYet(Users user);
	
	@Query("SELECT history FROM History history "
			+ "WHERE history.user = :user "
			+ "AND history.servedDate IS NOT NULL ")
	public List<History> getHistoryListConfirmed(Users user);
	
	@Query("SELECT history FROM History history "
			+ "WHERE history.user = :user "
			+ "AND history.servedDate IS NULL "
			+ "AND history.food = :food ")
	public History getHistoryNotConfirmedYetByFood(Users user, Food food);
	
	@Query("SELECT history FROM History history "
			+ "WHERE history.user = :user "
			+ "AND history.food = :food ")
	public History getHistoryConfirmedByUserAndFood(Users user, Food food);
	
	@Query("SELECT history FROM History history "
			+ "WHERE history.servedDate IS NOT NULL ")
	public List<History> getHistoryListConfirmed();
	
	@Query("SELECT history FROM History history "
			+ "WHERE history.user = :user ")
	public List<History> getHistoryListByUser(Users user);
}
