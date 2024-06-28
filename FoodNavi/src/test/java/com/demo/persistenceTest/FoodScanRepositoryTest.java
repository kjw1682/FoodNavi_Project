package com.demo.persistenceTest;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import com.demo.domain.Food;
import com.demo.dto.FoodRecommendVo;
import com.demo.persistence.FoodScanRepository;

@SpringBootTest
public class FoodScanRepositoryTest {
	
	@Autowired
	private FoodScanRepository foodScanRepo;
	
	@Disabled
	@Test
	public void getLastFood() {
		Food food = foodScanRepo.findFirstByOrderByFseqDesc();
		System.out.println(food.getName());
	}
	
	
	@Disabled
	@Test
	public void foodFiltering() {
		// 음식 검색/필터링 테스트 (각각의 항목을 복합적으로 설정 가능)
		Pageable pageable = PageRequest.of(0, 20, Direction.ASC, "fseq");
		
		// 키워드 검색 (동시에 설정시 AND)
		String searchName = ""; // 음식이름에 포함될 키워드 (기본값 : "")
		String searchIngredient = ""; // 음식에 사용된 재료이름에 포함될 키워드 (기본값 : "")
		String banName = "|"; // 음식이름에 제외될 키워드 (기본값 : "|")
		String banIngredient = "|"; // 음식에 사용된 재료이름에 제외될 키워드 (기타 알러지 입력시 이곳에 입력) (기본값 : "|") 
		
		// 한끼유형 검색 (하나만 설정 가능)
		String morning = ""; // (History 전용) 아침으로 선택한 것 검색시 morning 입력 (기본값 : "") 
		String lunch = ""; // (History 전용) 점심으로 선택한 것 검색시 lunch 입력 (기본값 : "")
		String dinner = ""; // (History 전용) 저녁으로 선택한 것 검색시 dinner 입력 (기본값 : "")
		String snack = ""; // (History 전용) 간식으로 선택한 것 검색시 snack 입력 (기본값 : "")
		
		// 음식유형 검색
		String foodType = ""; // (기본값 : "") (main, sub, snack 중에서 입력 가능)
		
		// 칼로리와 탄단지 및 비율 검색
		float kcalMin = 0f;
		float kcalMax = 1000000f;
		float carbMin = 0f;
		float carbMax = 1000000f;
		float prtMin = 0f;
		float prtMax = 1000000f;
		float fatMin = 0f;
		float fatMax = 1000000f;
		float ratioCarbMin = 0f;
		float ratioCarbMax = 1000000f;
		float ratioPrtMin = 0f;
		float ratioPrtMax = 1000000f;
		float ratioFatMin = 0f;
		float ratioFatMax = 1000000f;
		
		// 특정 재료 제외 설정 (banIngredient와 같은 역할) (동시에 설정시 교집합)
		String no_egg = "a"; // 계란 알러지일 경우 "y" (기본값 : "a")
		String no_milk = "a"; // 우유 알러지일 경우 "y" (기본값 : "a")
		String no_bean = "a"; // 콩 알러지일 경우 "y" (기본값 : "a")
		String no_shellfish = "a"; // 갑각류 알러지일 경우 "y"  (기본값 : "a")
		String allergyEtc = "|"; // 기타 알러지가 있으면 "재료이름" (기본값 : "|")
		
		// 채식단계에 따른 검색
		// 0:제한없음, 1:육고기(조류 제외) 금지, 2:육고기(조류 포함) 금지, 3:육고기+어패류 금지, 4:육고기+어패류+알 금지, 5:육고기+어패류+알+유제품 금지
		int vegetarian = 0;
		
		FoodRecommendVo foodRecommendVo = new FoodRecommendVo();
		foodRecommendVo.setSearchName(searchName);
		foodRecommendVo.setSearchIngredient(searchIngredient);
		foodRecommendVo.setBanName(banName);
		foodRecommendVo.setBanIngredient(banIngredient);
		foodRecommendVo.setMealTime(new String[]{morning, lunch, dinner, snack});
		foodRecommendVo.setFoodType("all");
		foodRecommendVo.setPurpose("all");
		foodRecommendVo.setDietType("all");
		foodRecommendVo.setAllergys(new String[]{no_egg, no_milk, no_bean, no_shellfish});
		foodRecommendVo.setAllergyEtc(allergyEtc);
		foodRecommendVo.setVegetarian(String.valueOf(vegetarian));
		foodRecommendVo.setRecommend(false);
		foodRecommendVo.setSortBy("name");
		foodRecommendVo.setSortDirection("ASC");
		foodRecommendVo.setPageMaxDisplay(5);
		
		
		if (foodRecommendVo.isRecommend()) {
			int check = foodScanRepo.getFoodCountByMealTypeInHistory(morning, lunch, dinner, snack);
			if (check < 10) {
				foodRecommendVo.setRecommend(false);
			}
		}

		List<Food> foodList = null;		
		if (foodRecommendVo.isRecommend()) {
			foodList = foodScanRepo.getFoodRecommendList(
				searchName, searchIngredient, banName, banIngredient,
				morning, lunch, dinner, snack,
				kcalMin, kcalMax, carbMin, carbMax, prtMin, prtMax, fatMin, fatMax,	
				ratioCarbMin, ratioCarbMax, ratioPrtMin, ratioPrtMax, ratioFatMin, ratioFatMax, 
				no_egg, no_milk, no_bean, no_shellfish, allergyEtc, 
				vegetarian, foodType);
		} else {
			foodList = foodScanRepo.getFoodScanList(
				searchName, searchIngredient, banName, banIngredient,
				kcalMin, kcalMax, carbMin, carbMax, prtMin, prtMax, fatMin, fatMax,	
				ratioCarbMin, ratioCarbMax, ratioPrtMin, ratioPrtMax, ratioFatMin, ratioFatMax, 
				no_egg, no_milk, no_bean, no_shellfish, allergyEtc, 
				vegetarian, foodType);
		}
		
		foodRecommendVo.setFoodList(foodList);
		for (Food food : foodList) {			
			System.out.println(food.getFseq()+" : "+food.getName());
		}
		System.out.println("총 "+foodList.size()+"개");
		System.out.println(foodRecommendVo.isRecommend());
		
	}
	
	@Disabled
	@Test
	public void getFoodCountByMealTypeInHistory() {
		String morning = "";
		String lunch = "";
		String dinner = "";
		String snack = "";
		System.out.println(foodScanRepo.getFoodCountByMealTypeInHistory(morning, lunch, dinner, snack));
	}

	
	@Disabled
	@Test
	public void getFood() {
		Food food = foodScanRepo.findById(1).get();
		System.out.println(food.getName());
	}
	
	@Disabled
	@Test
	public void getTotalFoodCount() {
		System.out.println(foodScanRepo.getTotalFoodCount());		
	}
	
	
}
