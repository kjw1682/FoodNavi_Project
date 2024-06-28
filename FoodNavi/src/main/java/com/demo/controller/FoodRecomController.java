package com.demo.controller;

import com.demo.domain.Food;
import com.demo.domain.History;
import com.demo.domain.Users;
import com.demo.dto.FoodRecommendVo;
import com.demo.dto.FoodVo;
import com.demo.dto.UserVo;
import com.demo.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class FoodRecomController {

    @Autowired
    private FoodScanService foodScanService;

    @Autowired
    private FoodRecommendService foodRecommendService;

    @Autowired
    private FoodIngredientService foodIngredientService;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private UsersService usersService;

    @GetMapping("/foodRecommendation")
    public String dietRecommendation(HttpSession session, Model model) {
        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");
        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "redirect:user_login_form"; // 로그인 페이지로 이동.
        }
        FoodRecommendVo foodRecommendVo = new FoodRecommendVo();
        String no_egg = user.getNo_egg();
        String no_milk = user.getNo_milk();
        String no_bean = user.getNo_bean();
        String no_shellfish = user.getNo_shellfish();
        foodRecommendVo.setAllergys(new String[]{no_egg, no_milk, no_bean, no_shellfish});
        foodRecommendVo.setAllergyEtc(user.getNo_ingredient());
        foodRecommendVo.setPurpose(user.getUserGoal());
        foodRecommendVo.setDietType(user.getDietType());
        foodRecommendVo.setVegetarian(user.getVegetarian());
        model.addAttribute("foodRecommendVo", foodRecommendVo);

        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
        List<History> historyList = historyService.getHistoryListConfirmed(user);
        float kcalMorning = 0f;
        float kcalLunch = 0f;
        float kcalDinner = 0f;
        float kcalSnack = 0f;
        for (History history : historyList) {
            if (today.equals(history.getServedDate().toString().substring(0, 10))) {
                float kcal = history.getFood().getFoodDetail().getKcal()/history.getFood().getFoodDetail().getN()*history.getServeNumber();
                String mealType = history.getMealType();
                if (mealType.equals("morning")) {
                    kcalMorning += kcal;
                } else if (mealType.equals("lunch")) {
                    kcalLunch += kcal;
                } else if (mealType.equals("dinner")) {
                    kcalDinner += kcal;
                } else {
                    kcalSnack += kcal;
                }
            }
        }
        UserVo userVo = new UserVo(user);
        userVo.setKcalMorning((int) kcalMorning);
        userVo.setKcalLunch((int) kcalLunch);
        userVo.setKcalDinner((int) kcalDinner);
        userVo.setKcalSnack((int) kcalSnack);
        model.addAttribute("userVo", userVo);

        return "food/foodDay";
    }

    // food 검색창에서의 추천 리스트를 생성한다.
    // 만들어진 추천 리스트는 검색 및 추천조건과 함께 foodScanVo에 담겨저 세션 영역에 저장된다.
    // 세부적인 추천 조건이 확정되면 거기에 맞게 검색 쿼리를 작성해야 한다.
    @RequestMapping("/food_recommend")
    public String foodRecommendList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            Model model, HttpSession session) {
        String mealTime = (String) session.getAttribute("mealTime");
        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");
        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "redirect:user_login_form"; // 로그인 페이지로 이동.
        }
        boolean check = false;
        boolean totalCheck = false;
        UserVo userVo = new UserVo(user);
        userVo.setLastMealType(mealTime);
        FoodRecommendVo[] foodRecommendVoArray = (FoodRecommendVo[])session.getAttribute("foodRecommendVoArray");
        if (page == 0) {
            check = true;
            page = 1;
            FoodRecommendVo foodRecommendVo = foodRecommendVoArray[0];
            List<Food> filteredList = foodScanService.getFoodScanList(user, foodRecommendVo);
            foodRecommendVo.setFoodList(filteredList);
            System.out.println(mealTime);
            List<FoodVo> foodRecommendList = foodRecommendService.getFoodRecommendList("Recommend.py", userVo, filteredList);

            for (int h = 0; h < 3; h++) {
                List<FoodVo> list = new ArrayList<>();
                for (FoodVo fv : foodRecommendList) {
                    if (fv.getFood().getFoodDetail().getFoodType().equals(foodRecommendVo.getFoodTypeCategory()[h + 1][0])) {
                        list.add(fv);
                    }
                    foodRecommendVoArray[h].setFoodRecommendList(list);
                }
            }
        } else {
            totalCheck = true;
        }

        Map<String, Integer> pageInfo = null;
        for (int i = 0; i < 3; i++) {
            List<FoodVo> foodRecommendList = foodRecommendVoArray[i].getFoodRecommendList();
            pageInfo = new HashMap<>();
            pageInfo.put("number", page);
            pageInfo.put("size", size);

            if (foodRecommendList.size() == 0) {
                pageInfo.put("totalPages", 1);
            } else {
                pageInfo.put("totalPages", (foodRecommendList.size() + size - 1) / size);
            }

            foodRecommendVoArray[i].setPageInfo(pageInfo);
            FoodVo foodVo = null;
            if (check) {
                if (foodRecommendVoArray[i].getFoodRecommendList().size() != 0) {
                    totalCheck = true;
                    foodRecommendVoArray[i].setListPossible(true);
                    foodRecommendVoArray[i].setIndex(1);
                    foodVo = foodRecommendList.get(0);
                    foodVo.setFoodIngredientList(foodIngredientService.getFoodIngredientListByFood(foodVo.getFood().getFseq()));
                    foodRecommendList.set(0, foodVo);
                } else {

                }
            } else {
                foodVo = foodRecommendList.get(foodRecommendVoArray[i].getIndex() - 1);
                foodVo.setFoodIngredientList(foodIngredientService.getFoodIngredientListByFood(foodVo.getFood().getFseq()));
                foodRecommendList.set(foodRecommendVoArray[i].getIndex() - 1, foodVo);
            }


            foodRecommendVoArray[i].setFoodRecommendList(foodRecommendList);
            foodRecommendVoArray[i].setTotal(foodRecommendList.size());
        }

        session.setAttribute("foodRecommendVoArray", foodRecommendVoArray);
        model.addAttribute("foodRecommendVo1", foodRecommendVoArray[0]);
        model.addAttribute("foodRecommendVo2", foodRecommendVoArray[1]);
        model.addAttribute("foodRecommendVo3", foodRecommendVoArray[2]);
        model.addAttribute("foodRecommendList1", foodRecommendVoArray[0].getFoodRecommendList());
        model.addAttribute("foodRecommendList2", foodRecommendVoArray[1].getFoodRecommendList());
        model.addAttribute("foodRecommendList3", foodRecommendVoArray[2].getFoodRecommendList());
        model.addAttribute("userVo", userVo);
        if (totalCheck) {
            return "food/foodRecommend";
        } else {
            return "food/foodRecommendNot";
        }


    }

    @PostMapping("/food_other_recommend")
    public String foodOtherRecommend(HttpSession session, Model model) {

        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");
        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "redirect:user_login_form"; // 로그인 페이지로 이동.
        }

        FoodRecommendVo foodRecommendVo = (FoodRecommendVo) session.getAttribute("foodRecommendVo");
        List<FoodVo> foodRecommendList = (List<FoodVo>) session.getAttribute("foodRecommendList");
        UserVo userVo = new UserVo(user);
        model.addAttribute("userVo", userVo);
        model.addAttribute("foodRecommendList", foodRecommendList);

        return "food/foodOtherRecommend";
    }

    // 추천음식을 표시한다.
    @GetMapping("/food_recommend_show")
    public String showFoodRecommend(Model model, HttpSession session, FoodRecommendVo foodRecommendVo) {
        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");
        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "redirect:user_login_form"; // 로그인 페이지로 이동.
        }

        FoodVo foodVo = foodRecommendVo.getFoodRecommendList().get(foodRecommendVo.getIndex());
        model.addAttribute("foodVo", foodVo);
        foodRecommendVo = (FoodRecommendVo) session.getAttribute("foodRecommend");
        model.addAttribute("foodRecommendVo", foodRecommendVo);

        return "food/foodRecommend";
    }

    // 추천화면 상차림으로 데이터를 보낸다.
    // 이미 존재하면 새로 추가하지 않고 개수만 늘린다.
    @PostMapping("/history_in_from_Recommend")
    public String historyInFromRecommend(HttpSession session) {

        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");
        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "redirect:user_login_form"; // 로그인 페이지로 이동.
        }

        FoodRecommendVo foodRecommendVo = (FoodRecommendVo) session.getAttribute("foodRecommend");
        Food food = foodRecommendVo.getFoodRecommendList().get(foodRecommendVo.getIndex()).getFood();

        History history = new History();
        history.setUser(user);
        history.setFood(food);
        history.setServeNumber(1);
        historyService.historyIn(history);

        return "";
    }

    @ResponseBody
    @PostMapping("recommend_section_reload")
    public Map<String, Object> recommend_section_reload(HttpSession session, HttpServletRequest request,
                                                        @RequestParam(value = "arrow_action") int arrow_action,
                                                        @RequestParam(value = "amount_action") int amount_action,
                                                        @RequestParam(value = "amount1") int amount1,
                                                        @RequestParam(value = "amount2") int amount2,
                                                        @RequestParam(value = "amount3") int amount3) {

        int section_num = (arrow_action + amount_action - 1) / 2;
        int amount = 0;
        if (section_num == 0) {
            amount = amount1;
        } else if (section_num == 1) {
            amount = amount2;
        } else {
            amount = amount3;
        }
        Map<String, Object> section_reload_info = new HashMap<>();
        // 세션에서 사용자 정보 가져오기
        FoodRecommendVo[] foodRecommendVoArray = (FoodRecommendVo[]) session.getAttribute("foodRecommendVoArray");
        FoodRecommendVo foodRecommendVo = null;
        Users user = (Users) session.getAttribute("loginUser");
        if (user != null) {
            if (arrow_action != 0) {
                foodRecommendVo = foodRecommendVoArray[section_num];
                if (arrow_action % 2 == 0) {
                    if (foodRecommendVo.getIndex() < foodRecommendVo.getTotal()) {
                        amount = 1;
                        foodRecommendVo.setIndex(foodRecommendVo.getIndex() + 1);
                        foodRecommendVoArray[section_num] = foodRecommendVo;
                        session.setAttribute("foodRecommendVoArray", foodRecommendVoArray);
                    } else {
                        foodRecommendVo = null;
                    }
                } else {
                    if (foodRecommendVo.getIndex() > 1) {
                        amount = 1;
                        foodRecommendVo.setIndex(foodRecommendVo.getIndex() - 1);
                        foodRecommendVoArray[section_num] = foodRecommendVo;
                        session.setAttribute("foodRecommendVoArray", foodRecommendVoArray);
                    } else {
                        foodRecommendVo = null;
                    }
                }
            } else {
                foodRecommendVo = foodRecommendVoArray[section_num];
                if (amount_action % 2 == 0) {
                    if (amount < 99) {
                        amount++;
                        ;
                    } else {
                        foodRecommendVo = null;
                    }
                } else {
                    if (amount > 1) {
                        amount--;
                    } else {
                        foodRecommendVo = null;
                    }
                }
            }
        }

        FoodVo foodVo = null;
        if (foodRecommendVo != null) {
            foodVo = foodRecommendVo.getFoodRecommendList().get(foodRecommendVo.getIndex() - 1);
        }


        if (foodVo != null) {
            section_reload_info.put("result", "success");
            section_reload_info.put("section_num", section_num + 1);
            section_reload_info.put("food_name", foodVo.getFood().getName());
            section_reload_info.put("food_img", foodVo.getFood().getImg());
            section_reload_info.put("fseq", foodVo.getFood().getFseq());
            section_reload_info.put("kcal", Integer.parseInt(foodVo.getKcal()) * amount);
            section_reload_info.put("carb", String.format("%.2f", (Float.parseFloat(foodVo.getCarb()) * amount)));
            section_reload_info.put("prt", String.format("%.2f", (Float.parseFloat(foodVo.getPrt()) * amount)));
            section_reload_info.put("fat", String.format("%.2f", (Float.parseFloat(foodVo.getFat()) * amount)));
            section_reload_info.put("amount", amount);
            section_reload_info.put("starScore", foodVo.getStarScore());
            section_reload_info.put("scoreView", foodVo.getScoreView());
        } else {
            section_reload_info.put("result", "fail");
        }


        return section_reload_info;
    }

    @PostMapping("/loading")
    public String loading(@RequestParam String mealTime, HttpSession session, 
    		@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection,
            @RequestParam(value = "pageMaxDisplay", defaultValue = "5") int pageMaxDisplay,
            @RequestParam(value = "searchField", defaultValue = "name") String searchField,
            @RequestParam(value = "searchWord", defaultValue = "") String searchWord,
            @RequestParam(value = "banField", defaultValue = "name") String banField,
            @RequestParam(value = "banWord", defaultValue = "") String banWord,
            @RequestParam(value = "searchName", defaultValue = "") String searchName,
            @RequestParam(value = "searchIngredient", defaultValue = "") String searchIngredient,
            @RequestParam(value = "banName", defaultValue = "") String banName,
            @RequestParam(value = "banIngredient", defaultValue = "") String banIngredient,
            @RequestParam(value = "foodType", defaultValue = "all") String foodType,
            @RequestParam(value = "purpose", defaultValue = "all") String purpose,
            @RequestParam(value = "recommend", defaultValue = "false") boolean recommend,
            @RequestParam(value = "dietType", defaultValue = "all") String dietType,
            @RequestParam(value = "allergys", defaultValue = "") String[] allergys,
            @RequestParam(value = "allergyEtc", defaultValue = "") String allergyEtc,
            @RequestParam(value = "vegetarian", defaultValue = "0") String vegetarian) {
        session.setAttribute("mealTime", mealTime);

        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");
        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "redirect:user_login_form"; // 로그인 페이지로 이동.
        }
        UserVo userVo = new UserVo(user);
        FoodRecommendVo foodRecommendVo1 = new FoodRecommendVo();
        FoodRecommendVo foodRecommendVo2 = new FoodRecommendVo();
        FoodRecommendVo foodRecommendVo3 = new FoodRecommendVo();
        FoodRecommendVo[] foodRecommendVoArray = {foodRecommendVo1, foodRecommendVo2, foodRecommendVo3};

        for (int h = 0; h < 3; h++) {
            FoodRecommendVo foodRecommendVo = foodRecommendVoArray[h];
            foodRecommendVo.setSearchField(searchField);
            foodRecommendVo.setSearchWord(searchWord);
            foodRecommendVo.setBanField(banField);
            foodRecommendVo.setBanWord(banWord);
            foodRecommendVo.setSearchName(searchName);
            foodRecommendVo.setSearchIngredient(searchIngredient);
            foodRecommendVo.setBanName(banName);
            foodRecommendVo.setBanIngredient(banIngredient);

            String[] mealType = new String[foodRecommendVo.getMealTimeArray().length];
            for (int i = 0; i < foodRecommendVo.getMealTimeArray().length; i++) {
                String meal = foodRecommendVo.getMealTimeArray()[i][0];
                if (mealTime.equals(meal)) {
                    mealType[i] = meal;
                } else {
                    mealType[i] = "";
                }
            }
            foodRecommendVo.setMealTime(mealType);
            String lastMealType = mealTime;

            userVo.setLastMealType(lastMealType);

            foodRecommendVo.setFoodType("all");

            foodRecommendVo.setPurpose(purpose);
            user.setUserGoal(purpose);

            foodRecommendVo.setDietType(dietType);
            user.setDietType(dietType);

            List<String> allergyList = Arrays.asList(allergys);
            String[] tmp_allergys = new String[foodRecommendVo.getAllergyArray().length];
            for (int i = 0; i < foodRecommendVo.getAllergyArray().length; i++) {
                String allergy = foodRecommendVo.getAllergyArray()[i][0];
                if (allergyList.contains(allergy)) {
                    tmp_allergys[i] = "y";
                } else {
                    tmp_allergys[i] = "a";
                }
            }
            foodRecommendVo.setAllergys(tmp_allergys);
            user.setNo_egg(tmp_allergys[0]);
            user.setNo_milk(tmp_allergys[1]);
            user.setNo_bean(tmp_allergys[2]);
            user.setNo_shellfish(tmp_allergys[3]);

            foodRecommendVo.setAllergyEtc(allergyEtc);
            user.setNo_ingredient(allergyEtc);

            foodRecommendVo.setVegetarian(vegetarian);
            user.setVegetarian(vegetarian);

            foodRecommendVo.setRecommend(recommend);
            foodRecommendVo.setSortBy(sortBy);
            foodRecommendVo.setSortDirection(sortDirection);
            foodRecommendVo.setPageMaxDisplay(pageMaxDisplay);
        }
        usersService.insertUser(user);
        session.setAttribute("foodRecommendVoArray", foodRecommendVoArray);
        
        return "food/loading";
    }

}