package com.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.domain.Food;
import com.demo.domain.History;
import com.demo.domain.Users;
import com.demo.dto.FoodRecommendVo;
import com.demo.dto.UserVo;
import com.demo.service.DataInOutService;
import com.demo.service.FoodScanService;
import com.demo.service.HistoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;


@Controller
public class HistoryController {
    @Autowired
    private HistoryService historyService;
    @Autowired
    private FoodScanService foodScanService;
    @Autowired
    private DataInOutService dataInOutService;

    // 추천 목록에서 음식을 히스토리 테이블에 기록한다(확정전)
    @Transactional
    @PostMapping ("/food_recommend_record")
    @ResponseBody
    public Map<String, String> recordFood(@RequestBody FoodRecord foodRecord, HttpSession session, HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();
		// 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");

        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
			response.put("message", "로그인 후 이용해주세요.");
            return response;
        }
        
        List<History> historyList = new ArrayList<>();
        List<History> historyListNotConfirmedYet = historyService.getHistoryListNotConfirmedYet(user);
        List<Integer> fseqListNotConfirmedYet = new ArrayList<>();
        for (History hs : historyListNotConfirmedYet) {
        	fseqListNotConfirmedYet.add(hs.getFood().getFseq());
        }        
        History history = null;
        int totalCheck = 0;
        int count = 0;
        String mealType = (String)session.getAttribute("mealTime");
        int fseq = (foodRecord.getFseq1() != 0 && foodRecord.getChecked1().equals("true")) ? foodRecord.getFseq1() : 0;
        if (fseq != 0) {
        	totalCheck++;
        	if (!fseqListNotConfirmedYet.contains(fseq)) {        		
        		history = new History();
        		history.setFood(foodScanService.getFoodByFseq(fseq));
        		history.setServeNumber(foodRecord.getAmount1());
        		history.setUser(user);
        		history.setMealType(mealType);
        		history.setDietType(user.getDietType());
        		history.setNo_egg(user.getNo_egg());
        		history.setNo_milk(user.getNo_milk());
        		history.setNo_bean(user.getNo_bean());
        		history.setNo_shellfish(user.getNo_shellfish());
        		history.setNo_ingredient(user.getNo_ingredient());
        		history.setPurpose(user.getUserGoal());
        		history.setVegetarian(user.getVegetarian());
        		historyList.add(history);
        		count++;
        	}
        }
        
        fseq = (foodRecord.getFseq2() != 0 && foodRecord.getChecked2().equals("true")) ? foodRecord.getFseq2() : 0;
        if (fseq != 0) {
        	totalCheck++;
        	if (!fseqListNotConfirmedYet.contains(fseq)) {        		
        		history = new History();
        		history.setFood(foodScanService.getFoodByFseq(fseq));
        		history.setServeNumber(foodRecord.getAmount2());
        		history.setUser(user);
        		history.setMealType(mealType);
        		history.setDietType(user.getDietType());
        		history.setNo_egg(user.getNo_egg());
        		history.setNo_milk(user.getNo_milk());
        		history.setNo_bean(user.getNo_bean());
        		history.setNo_shellfish(user.getNo_shellfish());
        		history.setNo_ingredient(user.getNo_ingredient());
        		history.setPurpose(user.getUserGoal());
        		history.setVegetarian(user.getVegetarian());
        		historyList.add(history);
        		count++;
        	}
        }
        
        fseq = (foodRecord.getFseq3() != 0 && foodRecord.getChecked3().equals("true")) ? foodRecord.getFseq3() : 0;
        if (fseq != 0) {
        	totalCheck++;
        	if (!fseqListNotConfirmedYet.contains(fseq)) {        		
        		history = new History();
        		history.setFood(foodScanService.getFoodByFseq(fseq));
        		history.setServeNumber(foodRecord.getAmount3());
        		history.setUser(user);
        		history.setMealType(mealType);
        		history.setDietType(user.getDietType());
        		history.setNo_egg(user.getNo_egg());
        		history.setNo_milk(user.getNo_milk());
        		history.setNo_bean(user.getNo_bean());
        		history.setNo_shellfish(user.getNo_shellfish());
        		history.setNo_ingredient(user.getNo_ingredient());
        		history.setPurpose(user.getUserGoal());
        		history.setVegetarian(user.getVegetarian());
        		historyList.add(history);
        		count++;
        	}
        }
        
        for (History hs : historyList) {
        	historyService.historyIn(hs);
        }
        
        String foodName = "";
		for (History hs : historyList) {
			foodName += hs.getFood().getName() + " ";
		}
        
		if (totalCheck == 0) {
			response.put("message", "선택된 음식이 없습니다.");
		} else if (count == 0) {
			response.put("message", "모든 음식이 이미 바구니에 있습니다.");
		} else if (totalCheck == count) {
    		response.put("message", foodName + "가(이) 성공적으로 저장되었습니다.");
        } else {
        	response.put("message", "이미 선택된 음식은 제외하고 " + foodName + "가(이) 성공적으로 저장되었습니다.");
        }
        
        return response;
    }

    // 오늘 기록하려고 하는 음식 보여주기
    @GetMapping ("/foodTodayHistory_view")
    public String foodTodayHistoryView(HttpSession session, Model model) {
        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");

        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "/user_login_form"; // 로그인 페이지로 이동.
        }

        UserVo userVo = new UserVo(user);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String today = now.format(formatter);

        List<History> notConfirmedHistoryList = historyService.getHistoryListNotConfirmedYet(user);

        model.addAttribute("today", today);
        model.addAttribute("userVo", userVo);
        model.addAttribute("notConfirmedHistoryList", notConfirmedHistoryList);

        return "food/foodTodayHistory";
    }


    // 확정된 히스토리 저장
    @Transactional
    @PostMapping ("/history_confirmed_record")
    @ResponseBody
    public String recordHistory(@RequestBody List<HistoryData> historyDataList, HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");

        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "/user_login_form"; // 로그인 페이지로 이동.
        }
        
        Date now = new Date();
        List<History> historyList = new ArrayList<>();

        List<History> tmp_historyList = historyService.getHistoryListNotConfirmedYet(user);
        List<Integer> serveNumList = new ArrayList<>();
        List<String> foodNameList = new ArrayList<>();
        for(HistoryData hd : historyDataList) {
            foodNameList.add(hd.getFood_name());
            serveNumList.add(hd.getServeNumber());
        }
        // 받은 데이터를 처리하는 로직을 작성
        for (History hs : tmp_historyList) {
            hs.setServedDate(now);
            for(int i=0; i<serveNumList.size(); i++) {
                if(hs.getFood().getName().equals(foodNameList.get(i))) {
                    hs.setServeNumber(serveNumList.get(i));
                }
            }
            historyService.historyUpdate(hs);
            historyList.add(hs);

        }
        
        dataInOutService.historyListToCsv(historyList);

        return "success";
    }

    @PostMapping ("/history_record_update")
    @ResponseBody
    public String updateHistory(@RequestBody List<HistoryData> historyDataList, HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");

        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "/user_login_form"; // 로그인 페이지로 이동.
        }

        // 받은 데이터를 처리하는 로직을 작성
        for (HistoryData historyData : historyDataList) {
            Food food = foodScanService.getFoodByName(historyData.getFood_name());

            History hs = historyService.getConfirmedHistoryByUserAndFood(user, food);
            hs.setServeNumber(historyData.getServeNumber());

            historyService.historyUpdate(hs);

        }

        return "success";
    }

    @GetMapping ("/foodHistory_view")
    public String foodHistoryView(HttpSession session, Model model) {
        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");

        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "/user_login_form"; // 로그인 페이지로 이동.
        }

        List<History> userHistoryList = historyService.getHistoryListConfirmed(user);
        UserVo userVo = new UserVo(user);
        model.addAttribute("userHistoryList", userHistoryList);
        model.addAttribute("userVo", userVo);

        return "food/foodHistory";
    }


    @PostMapping ("/delete_history_record")
    @ResponseBody
    public ResponseEntity<String> deleteHistoryRecord(@RequestBody List<HistoryData> dataList, HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");

        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("요청이 실패했습니다.");
        }
        try {
            // 히스토리 데이터 삭제 처리
            int hseq = 0;
            for (HistoryData data : dataList) {
                hseq = Integer.parseInt(data.getHseq());
            }
            History history = historyService.getHistoryByHseq(hseq);
            historyService.historyOut(history);
            return ResponseEntity.ok("성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제에 실패했습니다.");
        }

    }

    @PostMapping ("/history_in_from_detail")
    public String historyInFromDetail(@RequestParam ("fseq") String fseq, 
    		@RequestParam("resultType") String resultType,
                                      HttpSession session, Model model) {
        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");

        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "redirect:user_login_form";
        }
        
        String return_text = "";
        String mealType = (String)session.getAttribute("mealTime");
        
        List<History> historyListNotConfirmedYet = historyService.getHistoryListNotConfirmedYet(user);
        List<Integer> fseqListNotConfirmedYet = new ArrayList<>();
        for (History hs : historyListNotConfirmedYet) {
        	fseqListNotConfirmedYet.add(hs.getFood().getFseq());
        }
        
        int food_no = Integer.parseInt(fseq);
        if (fseqListNotConfirmedYet.contains(food_no)) {
        	return_text = "이미 목록에 있는 음식입니다.";
        } else {
        	if (resultType.equals("s")) {
        		FoodRecommendVo foodScanVo = (FoodRecommendVo)session.getAttribute("foodScanVo");
        		History history = new History();
        		history.setFood(foodScanService.getFoodByFseq(food_no));
        		history.setServeNumber(1);
        		history.setUser(user);
        		history.setMealType(mealType);
        		history.setDietType(foodScanVo.getDietType());
        		history.setPurpose(foodScanVo.getPurpose());
        		history.setVegetarian(foodScanVo.getVegetarian());
        		history.setNo_ingredient(foodScanVo.getBanIngredient());
        		String[] allergys = foodScanVo.getAllergys();
        		history.setNo_egg(allergys[0]);
        		history.setNo_milk(allergys[1]);
        		history.setNo_bean(allergys[2]);
        		history.setNo_shellfish(allergys[3]);
        		historyService.historyIn(history);
        		return_text = history.getFood().getName() + "가(이) 성공적으로 저장되었습니다.";
    		} else {
        		History history = new History();
        		history.setFood(foodScanService.getFoodByFseq(food_no));
        		history.setServeNumber(1);
        		history.setUser(user);
        		history.setMealType(mealType);
        		history.setDietType(user.getDietType());
        		history.setNo_egg(user.getNo_egg());
        		history.setNo_milk(user.getNo_milk());
        		history.setNo_bean(user.getNo_bean());
        		history.setNo_shellfish(user.getNo_shellfish());
        		history.setNo_ingredient(user.getNo_ingredient());
        		history.setPurpose(user.getUserGoal());
        		history.setVegetarian(user.getVegetarian());
        		historyService.historyIn(history);
        		return_text = history.getFood().getName() + "가(이) 성공적으로 저장되었습니다.";
        	} 
        	
        }
        model.addAttribute("success", return_text);
        return "food_scan/foodAlertPage";
    }

    /*
     * ----------------------------------------------------------------------------------
     */
    @Getter
    @Setter
    class HistoryData {
        private String food_name;
        private int serveNumber;
        private String hseq;
    }

    /*
     * ajax로 받는 RequestBody용 내부 클래스
     */
    @Getter
    @Setter
    class FoodRecord {
        private int fseq1;
        private int fseq2;
        private int fseq3;

        private int score1;
        private int score2;
        private int score3;

        private int amount1;
        private int amount2;
        private int amount3;

		private String checked1;
		private String checked2;
		private String checked3;
    }
}
