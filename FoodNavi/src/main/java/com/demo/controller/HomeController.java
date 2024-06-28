package com.demo.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.domain.Board;
import com.demo.domain.Exercise;
import com.demo.domain.Users;
import com.demo.dto.UserVo;
import com.demo.persistence.ExerciseOptionRepository;
import com.demo.service.ExerciseService;
import com.demo.service.HistoryService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ExerciseOptionRepository exerciseOptionRepo;

    @Autowired
    private ExerciseService exerciseService;

    @GetMapping("/")
    public String intropage() {
        return "index";
    }

    @GetMapping("/mainpage")
    public String mainpage(HttpSession session, Model model) {
        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");
        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "redirect:user_login_form"; // 로그인 페이지로 이동.
        }
        int minusKcal = 0;

        List<Exercise> exList = exerciseService.getExerciseListByUseqAndType(user);
        for (Exercise ex : exList) {
            LocalDate today = LocalDate.now();
            Instant instant = ex.getExerciseDate().toInstant();
            LocalDate exLocal = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            if (exLocal.equals(today)) {
                minusKcal += ((Integer.parseInt(ex.getExerciseOption().getFomula()) / 60) * ex.getTime());
            }
        }


        List<Board> boardList = new ArrayList<Board>();

        model.addAttribute("boardList", boardList);
        UserVo userVo = new UserVo((Users) (session.getAttribute("loginUser")));
        userVo.setKcalToday(((int) historyService.totalKcalToday(userVo.getUser()) - minusKcal) <= 0 ? 0 : ((int) historyService.totalKcalToday(userVo.getUser()) - minusKcal));
        userVo.setKcalOnTable((int) historyService.totalKcalOnTable(userVo.getUser()));
        userVo.setCarbToday(Math.round(historyService.totalCarbToday(userVo.getUser()) * 100) / 100f);
        userVo.setCarbOnTable(Math.round(historyService.totalCarbOnTable(userVo.getUser()) * 100) / 100f);
        userVo.setPrtToday(Math.round(historyService.totalPrtToday(userVo.getUser()) * 100) / 100f);
        userVo.setPrtOnTable(Math.round(historyService.totalPrtOnTable(userVo.getUser()) * 100) / 100f);
        userVo.setFatToday(Math.round(historyService.totalFatToday(userVo.getUser()) * 100) / 100f);
        userVo.setFatOnTable(Math.round(historyService.totalFatOnTable(userVo.getUser()) * 100) / 100f);

        model.addAttribute("userVo", userVo);
        return "mainpage";
    }

    @GetMapping("load_userVo")
    @ResponseBody
    public Map<String, Object> loadUserVo(HttpSession session) {
    	Map<String, Object> result = new HashMap<>();
        UserVo userVo = new UserVo((Users) (session.getAttribute("loginUser")));
        userVo.setKcalToday((int) historyService.totalKcalToday(userVo.getUser()));
        userVo.setKcalOnTable((int) historyService.totalKcalOnTable(userVo.getUser()));
        userVo.setCarbToday(Math.round(historyService.totalCarbToday(userVo.getUser()) * 100) / 100f);
        userVo.setCarbOnTable(Math.round(historyService.totalCarbOnTable(userVo.getUser()) * 100) / 100f);
        userVo.setPrtToday(Math.round(historyService.totalPrtToday(userVo.getUser()) * 100) / 100f);
        userVo.setPrtOnTable(Math.round(historyService.totalPrtOnTable(userVo.getUser()) * 100) / 100f);
        userVo.setFatToday(Math.round(historyService.totalFatToday(userVo.getUser()) * 100) / 100f);
        userVo.setFatOnTable(Math.round(historyService.totalFatOnTable(userVo.getUser()) * 100) / 100f);
        
        result.put("kcalToday", userVo.getKcalToday());
        result.put("kcalOnTable", userVo.getKcalOnTable());
        result.put("carbToday", userVo.getCarbToday());
        result.put("carbOnTable", userVo.getCarbOnTable());
        result.put("prtToday", userVo.getPrtToday());
        result.put("prtOnTable", userVo.getPrtOnTable());
        result.put("fatToday", userVo.getFatToday());
        result.put("fatOnTable", userVo.getFatOnTable());
        result.put("properCarb", userVo.getProperCarb());
        result.put("properPrt", userVo.getProperPrt());
        result.put("properFat", userVo.getProperFat());
        result.put("EER", userVo.getEER());
        
        return result;
    }
}
