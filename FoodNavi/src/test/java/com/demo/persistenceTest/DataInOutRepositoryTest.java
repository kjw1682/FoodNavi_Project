package com.demo.persistenceTest;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.demo.config.PathConfig;
import com.demo.domain.Food;
import com.demo.domain.History;
import com.demo.domain.Ingredient;
import com.demo.domain.Users;
import com.demo.service.DataInOutService;

@SpringBootTest
public class DataInOutRepositoryTest {
	@Autowired
	DataInOutService dataInOutService;

	@Disabled
	@Test
	public void historyInputDummy() {
		historyInDummy("morning", "50");
		historyInDummy("lunch", "50");
		historyInDummy("dinner", "50");
		historyInDummy("snack", "50");
	}
	
	@Disabled
	@Test
	public void totalInput() {
		// 기본 데이터 입력
		usersInDummy();
		ingredientInFromCsv();
		foodInFromCsv();
		historyInputDummy();
	}
	
	@Disabled
	@Test
	public void usersInDummy() {
		// 랜덤 더미유저 입력하기
		String n = "1000"; // 입력할 더미유저 숫자
		List<Users> usersList = dataInOutService.usersInDummy(n);
		dataInOutService.usersListToCsv(usersList);
	}
	
	@Disabled
	@Test
	public void ingredientInFromCsv() {
		// 재료파일(igrd.csv)로부터 Ingredient 테이블에 데이터 입력하기
		String csvFile = "igrd.csv";
		String pyFile = "";
		csvFile = PathConfig.realPath(csvFile);
		pyFile = PathConfig.realPath(pyFile);
		String n = "all";// igrd.csv 파일에 있는 데이터 전부 읽기
		List<Ingredient> ingredientList = dataInOutService.ingredientInFromCsv(csvFile, n);
	}
		
	
	@Disabled
	@Test
	public void foodInDummy() {
		// 랜덤하게 더미 음식데이터 추가(현재는 사용 금지)
		String n = "1000"; // 추가할 음식 숫자
		List<Food> foodList = dataInOutService.foodInDummy(n);
		dataInOutService.foodListToCsv(foodList);
	}
	
	@Disabled
	@Test
	public void foodInFromCsv() {
		// FoodIngredient.csv 파일로부터 Food와 FoodIngredient, FoodDetail 테이블에 데이터 입력
		// Ingredient 테이블에 데이터가 먼저 입력되어 있어야 한다.
		String csvFile = "FoodIngredient.csv";
		String pyFile = "";
		csvFile = PathConfig.realPath(csvFile);
		pyFile = PathConfig.realPath(pyFile);
		String n = "all"; // FoodIngredient.csv 파일에 있는 데이터 전부 읽기
		List<Food> foodList = dataInOutService.foodInFromCsv(csvFile, n);
		for (Food food : foodList) {
			System.out.println(food.getName());
		}
		
		
	}
	
	
	@Disabled
	@Test
	public void historyInDummy(String mealTime, String n) {
		// 현재 생성되어 있는 유저와 음식을 기준으로 완전히 랜덤하게 선택 기록을 생성
		// 입력매개변수 mealTime은 mealType 생성변수. random : 시간기준, morning : 아침, lunch : 점심, dinner : 저녁, snack : 간식
		// 입력매개변수 n은 끼니를 생성하는 유저 숫자. all 입력시 모든 User의 끼니 기록 생성.
		String csvFile = "tmp_history.csv"; // 생성한 기록이 임시 저장되는 csv 파일
		String pyFile = "HistoryListToCsv.py"; // 임시 저장된 csv 파일과 기존의 History.csv 파일을 합치는 명령을 수행하는 파이썬 파일
		csvFile = PathConfig.realPath(csvFile);
		pyFile = PathConfig.realPath(pyFile);
		
		List<History> historyList = dataInOutService.historyInDummy(mealTime, n);
		dataInOutService.historyListToCsv(historyList);
	}
	
	
}
