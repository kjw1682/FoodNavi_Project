package com.demo.service;

import java.util.List;

import com.demo.domain.Admin;
import com.demo.domain.Food;
import com.demo.domain.History;
import com.demo.domain.Ingredient;
import com.demo.domain.Users;

public interface DataInOutService {

	public void adminListToCsv(List<Admin> adminList);
	
	public List<Food> foodInFromCsv(String csvFile, String n);
	public List<Food> foodInDummy(String n);
	public void foodListToCsv(List<Food> foodList);
	public void filteredListToCsv(List<Food> filteredList);

	public List<History> historyInDummy(String mealType, String n);
	public void historyListToCsv(List<History> historyList);
	
	public List<Ingredient> ingredientInFromCsv(String csvFile, String n);

	public List<Users> usersInDummy(String n);
	public void usersListToCsv(List<Users> users);	

}
