package com.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.domain.Food;
import com.demo.domain.History;
import com.demo.domain.Users;
import com.demo.dto.FoodRecommendVo;
import com.demo.dto.FoodVo;
import com.demo.dto.UserVo;
import com.demo.service.DataInOutService;
import com.demo.service.FoodIngredientService;
import com.demo.service.FoodRecommendService;
import com.demo.service.FoodScanService;
import com.demo.service.HistoryService;
import com.demo.service.RcdService;
import com.demo.service.UsersService;

import jakarta.servlet.http.HttpSession;

@Controller
public class FoodScanController {
	
	@Autowired
	FoodScanService foodScanService;
	
	@Autowired
	FoodRecommendService foodRecommendService;
	
	@Autowired
	HistoryService historyService;
	
	@Autowired
	UsersService usersService;
	
	@Autowired
	DataInOutService dataInOutService;
	
	@Autowired
	RcdService rcdService;
	
	@Autowired
	FoodIngredientService foodIngredientService;
	
	// food 검색조건에 따른 리스트를 생성한다.
	// 만들어진 리스트는 검색조건과 함께 FoodRecommendVo에 담겨저 세션 영역에 저장된다.
	@RequestMapping("/food_scan")
	public String foodList(
			@RequestParam(value="page", defaultValue="0") int page, 
			@RequestParam(value="size", defaultValue="8") int size,
			@RequestParam(value="sortBy", defaultValue="name") String sortBy,
			@RequestParam(value="sortDirection", defaultValue="ASC") String sortDirection,
			@RequestParam(value="pageMaxDisplay", defaultValue="5") int pageMaxDisplay,
			@RequestParam(value="searchField", defaultValue="name") String searchField,
			@RequestParam(value="searchWord", defaultValue="") String searchWord,
			@RequestParam(value="banField", defaultValue="name") String banField,
			@RequestParam(value="banWord", defaultValue="") String banWord,
			@RequestParam(value="searchName", defaultValue="") String searchName,
			@RequestParam(value="searchIngredient", defaultValue="") String searchIngredient,
			@RequestParam(value="banName", defaultValue="") String banName,
			@RequestParam(value="banIngredient", defaultValue="") String banIngredient,
			@RequestParam(value="mealTime", defaultValue="") String[] mealTime,
			@RequestParam(value="foodType", defaultValue="all") String foodType,
			@RequestParam(value="purpose", defaultValue="all") String purpose,
			@RequestParam(value="recommend", defaultValue="false") boolean recommend,
			@RequestParam(value="dietType", defaultValue="all") String dietType,
			@RequestParam(value="allergys", defaultValue="") String[] allergys,
			@RequestParam(value="allergyEtc", defaultValue="") String allergyEtc,
			@RequestParam(value="vegetarian", defaultValue="0") String vegetarian,
			@RequestParam(value="scan", defaultValue="false") boolean scan,
			FoodRecommendVo foodScanVo, Model model, HttpSession session) {
		
		// 세션에서 사용자 정보 가져오기
    	Users user = (Users) session.getAttribute("loginUser");
    	// 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "redirect:user_login_form"; // 로그인 페이지로 이동.
        }
        
        UserVo userVo = new UserVo(user);
        if (page == 0 && scan) {
			page = 1;
			foodScanVo.setSearchField(searchField);
			foodScanVo.setSearchWord(searchWord);
			foodScanVo.setBanField(banField);
			foodScanVo.setBanWord(banWord);
			foodScanVo.setSearchName(searchName);
			foodScanVo.setSearchIngredient(searchIngredient);
			foodScanVo.setBanName(banName);
			foodScanVo.setBanIngredient(banIngredient);
			
			List<String> mealTimeList = Arrays.asList(mealTime);
			mealTime = new String[foodScanVo.getMealTimeArray().length];
			for (int i = 0 ; i < foodScanVo.getMealTimeArray().length ; i++) {
				String meal = foodScanVo.getMealTimeArray()[i][0];
				if (mealTimeList.contains(meal)) {
					mealTime[i] = meal;
				} else {
					mealTime[i] = "";
				}					
			}
			String lastMealTime = ""; 
			if (mealTimeList.size() == 0) {
				lastMealTime = foodScanVo.getMealTimeByTime();
			} else {
				lastMealTime = mealTimeList.get(0);
			}			
			userVo.setLastMealType(lastMealTime);
			foodScanVo.setMealTime(mealTime);
			
			foodScanVo.setFoodType(foodType);
			foodScanVo.setPurpose(purpose);
			foodScanVo.setDietType(dietType);

			List<String> allergyList = Arrays.asList(allergys);
			allergys = new String[foodScanVo.getAllergyArray().length];
			for (int i = 0 ; i < foodScanVo.getAllergyArray().length ; i++) {
				String allergy = foodScanVo.getAllergyArray()[i][0];
				if (allergyList.contains(allergy)) {
					allergys[i] = "y";
				} else {
					allergys[i] = "a";
				}					
			}
			foodScanVo.setAllergys(allergys);
			
			foodScanVo.setAllergyEtc(allergyEtc);
			foodScanVo.setVegetarian(vegetarian);
			foodScanVo.setRecommend(recommend);
			foodScanVo.setSortBy(sortBy);
			foodScanVo.setSortDirection(sortDirection);
			foodScanVo.setPageMaxDisplay(pageMaxDisplay);
			List<Food> foodScanList = foodScanService.getFoodScanList(user, foodScanVo);
			foodScanVo.setFoodList(foodScanList);
			if (foodScanVo.isRecommend()) {
				List<FoodVo> foodRecommendList = foodRecommendService.getFoodRecommendList("Recommend.py", userVo, foodScanList);
				foodScanVo.setFoodRecommendList(foodRecommendList);
			}
		} else if (page == 0 && !scan) {
			page = 1;
			foodScanVo.setSearchField(searchField);
			foodScanVo.setSearchName(searchName);
			foodScanVo.setSearchIngredient(searchIngredient);
			foodScanVo.setSearchWord(searchWord);
			foodScanVo.setBanName(banName);
			if (user.getNo_ingredient() == null || user.getNo_ingredient().equals("")) {
				foodScanVo.setBanField(banField);
				foodScanVo.setBanIngredient(banIngredient);
				foodScanVo.setBanWord(banWord);
			} else {
				foodScanVo.setBanField(foodScanVo.getSearchType()[1][0]);
				foodScanVo.setBanIngredient(user.getNo_ingredient());
				foodScanVo.setBanWord(user.getNo_ingredient());
			}			
			String currentMealTime = foodScanVo.getMealTimeByTime();
			mealTime = new String[foodScanVo.getMealTimeArray().length];
			for (int i = 0 ; i < foodScanVo.getMealTimeArray().length ; i++) {
				String meal = foodScanVo.getMealTimeArray()[i][0];
				if (meal.equals(currentMealTime)) {
					mealTime[i] = meal;
				} else {
					mealTime[i] = "";
				}
			}
			userVo.setLastMealType(currentMealTime);
			foodScanVo.setMealTime(mealTime);
			
			foodScanVo.setFoodType(foodType);
			foodScanVo.setPurpose(user.getUserGoal());
			foodScanVo.setDietType(user.getDietType());

			foodScanVo.setAllergys(new String[] {user.getNo_egg(), user.getNo_milk(), user.getNo_bean(), user.getNo_shellfish()});
			foodScanVo.setVegetarian(user.getVegetarian());
			foodScanVo.setRecommend(recommend);
			foodScanVo.setSortBy(sortBy);
			foodScanVo.setSortDirection(sortDirection);
			foodScanVo.setPageMaxDisplay(pageMaxDisplay);
			String tmp_allergyEtc = foodScanVo.getAllergyEtc();
			foodScanVo.setAllergyEtc("");
			List<Food> foodScanList = foodScanService.getFoodScanList(user, foodScanVo);
			foodScanVo.setAllergyEtc(tmp_allergyEtc);
			foodScanVo.setFoodList(foodScanList);
		} else {
			foodScanVo = (FoodRecommendVo)session.getAttribute("foodScanVo");						
		}
		
        List<Food> foodList = null;
        List<FoodVo> foodRecommendList = null;
		Map<String, Integer> pageInfo = new HashMap<>();
		List<FoodVo> currentList = null;
		List<FoodVo> currentRecommendList = null;
		
		if (!foodScanVo.isRecommend()) {
			foodList = foodScanVo.getFoodList();
			pageInfo = new HashMap<>();
			pageInfo.put("number", page-1);
			pageInfo.put("size", size);
			pageInfo.put("totalPages", (foodList.size()+size-1)/size);
			currentList = new ArrayList<>();
			for (int i = size*(page-1) ; i < Math.min(size*page, foodList.size()) ; i++) {
				currentList.add(new FoodVo(foodList.get(i)));
			}
		} else {
			foodRecommendList = foodScanVo.getFoodRecommendList();
			pageInfo = new HashMap<>();
			pageInfo.put("number", page-1);
			pageInfo.put("size", size);
			pageInfo.put("totalPages", (foodRecommendList.size()+size-1)/size);
			currentRecommendList = new ArrayList<>();
			for (int i = size*(page-1) ; i < Math.min(size*page, foodRecommendList.size()) ; i++) {
				currentRecommendList.add(foodRecommendList.get(i));
			}
		}
		
		foodScanVo.setPageInfo(pageInfo);
		foodScanVo.setFoodList(foodList);
		session.setAttribute("foodScanVo", foodScanVo);
		model.addAttribute("foodScanVo", foodScanVo);
		model.addAttribute("foodList", currentList);		
		model.addAttribute("foodRecommendList", currentRecommendList);
		model.addAttribute("pageInfo", foodScanVo.getPageInfo());
		model.addAttribute("userVo", userVo);


		return "food_scan/foodScanList";
	}
	
	
	// 음식의 상세보기를 연다.
	@GetMapping("/food_detail")
	public String showFoodDetail(Food food, Model model, HttpSession session, FoodRecommendVo foodScanVo,
			@RequestParam(value="type", defaultValue="s") String showType) {
		// 세션에서 사용자 정보 가져오기
    	Users user = (Users) session.getAttribute("loginUser");
    	// 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "redirect:user_login_form"; // 로그인 페이지로 이동.
        }
        
        if (showType.equals("s")) {
        	foodScanVo = (FoodRecommendVo)session.getAttribute("foodScanVo");
        	foodScanVo.setResultType("s");
            FoodVo foodVo = null;
            if (foodScanVo.isRecommend()) {
            	List<FoodVo> foodVoList = foodScanVo.getFoodRecommendList();
            	for (FoodVo vo : foodVoList) {
            		if (vo.getFood().getFseq() == food.getFseq()) {
            			foodVo = vo;
            			break;
            		}
            	}
            } else {
            	food = foodScanService.getFoodByFseq(food.getFseq()); 
            	foodVo = new FoodVo(food);        	
            }
            foodVo.setFoodIngredientList(foodIngredientService.getFoodIngredientListByFood(foodVo.getFood().getFseq()));
            model.addAttribute("foodVo", foodVo);
            
            UserVo userVo = new UserVo(user);
            model.addAttribute("userVo", userVo);
            
            Map<String, Integer> rcdStatus = new HashMap<>();
            rcdStatus.put("rcdStatus", rcdService.rcdStatus(user, foodVo.getFood()));        
            rcdStatus.put("rcdCount", rcdService.getRcdCountByFood(foodVo.getFood()));
            model.addAttribute("rcdStatus", rcdStatus);
    		
    		model.addAttribute("pageInfo", foodScanVo.getPageInfo());
    		model.addAttribute("foodList", foodScanVo.getFoodList());
    		model.addAttribute("foodSRVo", foodScanVo);
    		session.setAttribute("foodVo", foodVo);
        } else if (showType.equals("r1")){
        	FoodRecommendVo[] foodRecommendVoArray = (FoodRecommendVo[])session.getAttribute("foodRecommendVoArray");
        	FoodRecommendVo foodRecommendVo = foodRecommendVoArray[0];
        	foodRecommendVo.setResultType("r");
            FoodVo foodVo = null;
            List<FoodVo> foodVoList = foodRecommendVo.getFoodRecommendList();
        	for (FoodVo vo : foodVoList) {
        		if (vo.getFood().getFseq() == food.getFseq()) {
        			foodVo = vo;
        			break;
        		}
        	}
        	foodVo.setFoodIngredientList(foodIngredientService.getFoodIngredientListByFood(foodVo.getFood().getFseq()));
            model.addAttribute("foodVo", foodVo);
            UserVo userVo = new UserVo(user);
            model.addAttribute("userVo", userVo);
           
            Map<String, Integer> rcdStatus = new HashMap<>();
            rcdStatus.put("rcdStatus", rcdService.rcdStatus(user, foodVo.getFood()));        
            rcdStatus.put("rcdCount", rcdService.getRcdCountByFood(foodVo.getFood()));
            model.addAttribute("rcdStatus", rcdStatus);    		
    		model.addAttribute("foodSRVo", foodRecommendVo);
    		session.setAttribute("foodVo", foodVo);
        } else if (showType.equals("r2")){
        	FoodRecommendVo[] foodRecommendVoArray = (FoodRecommendVo[])session.getAttribute("foodRecommendVoArray");
        	FoodRecommendVo foodRecommendVo = foodRecommendVoArray[1];
        	foodRecommendVo.setResultType("r");
            FoodVo foodVo = null;
            List<FoodVo> foodVoList = foodRecommendVo.getFoodRecommendList();
        	for (FoodVo vo : foodVoList) {
        		if (vo.getFood().getFseq() == food.getFseq()) {
        			foodVo = vo;
        			break;
        		}
        	}
        	foodVo.setFoodIngredientList(foodIngredientService.getFoodIngredientListByFood(foodVo.getFood().getFseq()));
            model.addAttribute("foodVo", foodVo);
            UserVo userVo = new UserVo(user);
            model.addAttribute("userVo", userVo);
           
            Map<String, Integer> rcdStatus = new HashMap<>();
            rcdStatus.put("rcdStatus", rcdService.rcdStatus(user, foodVo.getFood()));        
            rcdStatus.put("rcdCount", rcdService.getRcdCountByFood(foodVo.getFood()));
            model.addAttribute("rcdStatus", rcdStatus);    		
    		model.addAttribute("foodSRVo", foodRecommendVo);
    		session.setAttribute("foodVo", foodVo);
        } else if (showType.equals("r3")){
        	FoodRecommendVo[] foodRecommendVoArray = (FoodRecommendVo[])session.getAttribute("foodRecommendVoArray");
        	FoodRecommendVo foodRecommendVo = foodRecommendVoArray[2];
        	foodRecommendVo.setResultType("r");
            FoodVo foodVo = null;
            List<FoodVo> foodVoList = foodRecommendVo.getFoodRecommendList();
        	for (FoodVo vo : foodVoList) {
        		if (vo.getFood().getFseq() == food.getFseq()) {
        			foodVo = vo;
        			break;
        		}
        	}
        	foodVo.setFoodIngredientList(foodIngredientService.getFoodIngredientListByFood(foodVo.getFood().getFseq()));
            model.addAttribute("foodVo", foodVo);
            UserVo userVo = new UserVo(user);
            model.addAttribute("userVo", userVo);
           
            Map<String, Integer> rcdStatus = new HashMap<>();
            rcdStatus.put("rcdStatus", rcdService.rcdStatus(user, foodVo.getFood()));        
            rcdStatus.put("rcdCount", rcdService.getRcdCountByFood(foodVo.getFood()));
            model.addAttribute("rcdStatus", rcdStatus);    		
    		model.addAttribute("foodSRVo", foodRecommendVo);
    		session.setAttribute("foodVo", foodVo);
        } else if(showType.equals("c")){
			food = foodScanService.getFoodByFseq(food.getFseq());
			FoodVo foodVo = new FoodVo(food);
			foodVo.setFoodIngredientList(foodIngredientService.getFoodIngredientListByFood(foodVo.getFood().getFseq()));
			model.addAttribute("foodVo", foodVo);

			UserVo userVo = new UserVo(user);
			model.addAttribute("userVo", userVo);
			foodScanVo.setResultType("c");
			Map<String, Integer> rcdStatus = new HashMap<>();
			rcdStatus.put("rcdStatus", rcdService.rcdStatus(user, foodVo.getFood()));
			rcdStatus.put("rcdCount", rcdService.getRcdCountByFood(foodVo.getFood()));
			model.addAttribute("rcdStatus", rcdStatus);

			model.addAttribute("pageInfo", foodScanVo.getPageInfo());
			model.addAttribute("foodList", foodScanVo.getFoodList());
			model.addAttribute("foodSRVo", foodScanVo);
			session.setAttribute("foodVo", foodVo);
		} else if(showType.equals("m")){
			FoodRecommendVo foodRecommendVo = (FoodRecommendVo)session.getAttribute("foodRecommendVo");
			foodRecommendVo.setResultType("m");
			FoodVo foodVo = null;
			for (FoodVo vo : foodRecommendVo.getFoodRecommendList()) {
				if (vo.getFood().getFseq() == food.getFseq()) {
					foodVo = vo;
					break;
				}
			}
			model.addAttribute("foodVo", foodVo);
			UserVo userVo = new UserVo(user);
            model.addAttribute("userVo", userVo);
			Map<String, Integer> rcdStatus = new HashMap<>();
			rcdStatus.put("rcdStatus", rcdService.rcdStatus(user, foodVo.getFood()));
			rcdStatus.put("rcdCount", rcdService.getRcdCountByFood(foodVo.getFood()));
			model.addAttribute(foodRecommendVo.getPageInfo());
			model.addAttribute("rcdStatus", rcdStatus);
			model.addAttribute("pageInfo", foodRecommendVo.getPageInfo());
			model.addAttribute("foodSRVo", foodRecommendVo);
			session.setAttribute("foodVo", foodVo);
		}
        
		
		return "food_scan/foodDetail";
	}

	// 상차림에서 데이터를 갱신한다.
	@PostMapping("/history_update")
	public String historyUpdate(HttpSession session, History history) {
		// 세션에서 사용자 정보 가져오기
    	Users user = (Users) session.getAttribute("loginUser");
    	// 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "redirect:user_login_form"; // 로그인 페이지로 이동.
        }
		
		if (history.getServeNumber() == 0) {
			historyService.historyOut(history);
		} else {
			historyService.historyUpdate(history);
		}		
		
		return "redirect:mypage_history";
	}
	
	// 상차림에서 데이터를 제거한다.
	@PostMapping("/history_out")
	public String historyOut(HttpSession session, History history) {
		// 세션에서 사용자 정보 가져오기
    	Users user = (Users) session.getAttribute("loginUser");
    	// 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "redirect:user_login_form"; // 로그인 페이지로 이동.
        }		
		
		historyService.historyOut(history);
		return "redirect:mypage_history";
	}
	
	// 상차림 후 현재시각을 기록한다.
	@Transactional
	@GetMapping("/history_confirm")
	public String historyConfirmed(HttpSession session) {
		
		// 세션에서 사용자 정보 가져오기
    	Users user = (Users) session.getAttribute("loginUser");
    	// 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "redirect:user_login_form"; // 로그인 페이지로 이동.
        }
		
		List<History> historyList = historyService.getHistoryListNotConfirmedYet(user);
		Date now = new Date();
		
		for (History history : historyList) {
			history.setServedDate(now);
			historyService.historyUpdate(history);
		}
		dataInOutService.historyListToCsv(historyList);
		
		return "redirect:mypage_history";
	}
	
	// 상세보기에서 음식 추천/해제
	@GetMapping("rcd_update")
	@ResponseBody
	public Map<String, Integer> rcdUpdate(HttpSession session, Food food) {

		Map<String, Integer> rcdStatus = new HashMap<>();
		// 세션에서 사용자 정보 가져오기
    	Users user = (Users) session.getAttribute("loginUser");
    	// 세션에 로그인 정보가 없는 경우
        if (user != null) {
        	food = foodScanService.getFoodByFseq(food.getFseq());
            int result = rcdService.rcdUpdate(user, food);
            rcdStatus.put("rcdStatus", result);
            int rcdCount = rcdService.getRcdCountByFood(food);
            rcdStatus.put("rcdCount", rcdCount);
        } else {
        	rcdStatus.put("rcdStatus", -1);
        	rcdStatus.put("rcdCount", 0);
        }
        
        return rcdStatus;
	}
	
	// 마이페이지의 내 추천리스트에서 음식추천 해제
	@GetMapping("rcd_delete")
	public String rcdDelete(HttpSession session, Food food, RedirectAttributes re) {
		// 세션에서 사용자 정보 가져오기
    	Users user = (Users) session.getAttribute("loginUser");
    	// 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "redirect:user_login_form"; // 로그인 페이지로 이동.
        }
        
        food = foodScanService.getFoodByFseq(food.getFseq());
        rcdService.rcdUpdate(user, food);       
        re.addAttribute("fseq", food.getFseq());
        return "redirect:mypage_like_list";
	}
}
