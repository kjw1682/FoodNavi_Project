package com.demo.controller;

import com.demo.dto.FoodVo;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/foodDetailChart/data")
public class foodDetailChartController {
    @GetMapping("/drawChart")
    public float[] foodDetailChart(HttpSession session) {
        FoodVo vo = (FoodVo) session.getAttribute("foodVo");
        float carb = vo.getFood().getFoodDetail().getCarb();
        float prt = vo.getFood().getFoodDetail().getPrt();
        float fat = vo.getFood().getFoodDetail().getFat();

        BigDecimal bd_carb = new BigDecimal(Float.toString(carb));
        BigDecimal bd_prt = new BigDecimal(Float.toString(prt));
        BigDecimal bd_fat = new BigDecimal(Float.toString(fat));

        bd_carb = bd_carb.setScale(2, RoundingMode.HALF_UP);
        bd_prt = bd_prt.setScale(2, RoundingMode.HALF_UP);
        bd_fat = bd_fat.setScale(2, RoundingMode.HALF_UP);

        float roundCarb = bd_carb.floatValue();
        float roundPrt = bd_prt.floatValue();
        float roundFat = bd_fat.floatValue();

        float[] irgd = new float[3];
        irgd[0] = roundCarb;
        irgd[1] = roundPrt;
        irgd[2] = roundFat;
        return irgd;
    }
}
