package com.demo.dto;

import java.util.List;

import com.demo.domain.Food;
import com.demo.domain.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
	private Users user = new Users();
	private float BMI = 0;
	private int EER = 0;
	private List<Food> todayFoodList = null; // 오늘 선택 목록을 추가 (테이블 생성 작업 후 처리할 것)
	private float[] properCarbRatio = null;
	private float[] properPrtRatio = null;
	private float[] properFatRatio = null;
	private float properCarb = 0;
	private float properPrt = 0;
	private float properFat = 0;
	private int kcalToday = 0;
	private int kcalMorning = 0;
	private int kcalLunch = 0;
	private int kcalDinner = 0;
	private int kcalSnack = 0;
	private int kcalOnTable = 0;
	private float carbToday = 0;
	private float carbOnTable = 0;
	private float prtToday = 0;
	private float prtOnTable = 0;
	private float fatToday = 0;
	private float fatOnTable = 0;
	private String lastMealType = "";
	
	
	public UserVo(Users user) {
		this.user = user;
		BMI = getBMI(user);				
		EER = getEER(user, 1);
		properCarbRatio = getProperCarbRatio(user);
		properPrtRatio = getProperPrtRatio(user);
		properFatRatio = getProperFatRatio(user);
		properCarb = Math.round((EER * (properCarbRatio[1]+properCarbRatio[1])/2 /4)*100)/100f;
		properPrt = Math.round((EER * (properPrtRatio[1]+properPrtRatio[1])/2 /4)*100)/100f;
		properFat = Math.round((EER * (properFatRatio[1]+properFatRatio[1])/2 /9)*100)/100f;
	}
	
	private float getBMI(Users user) {
		float BMI = 10000 * user.getWeight() / (float)Math.pow(user.getHeight(), 2);
		
		return (Math.round(BMI*100))/100f;
	}
	
	private int getEER(Users user, int activeLevel) {
		float value = 0;		 
		if (user.getAge() <= 2) {
			value = 89 * user.getWeight() - 100;
		} else if (user.getAge() <= 19) {
			if (user.getSex().equals("m")) {
				float[] activeValue = {1.0f, 1.13f, 1.26f, 1.42f};
				value = (int)(88.5 - 61.9*user.getAge() + activeValue[activeLevel]*(26.7*user.getWeight() + 9.03*user.getHeight())); 
			} else {
				float[] activeValue = {1.0f, 1.16f, 1.31f, 1.56f};
				value = (int)(135.3 - 30.8*user.getAge() + activeValue[activeLevel]*(10.0*user.getWeight() + 9.34*user.getHeight()));
			}
		} else {
			if (user.getSex().equals("m")) {
				float[] activeValue = {1.0f, 1.11f, 1.25f, 1.48f};
				value = (int)(662 - 9.53*user.getAge() + activeValue[activeLevel]*(15.91*user.getWeight() + 5.396*user.getHeight())); 
			} else {
				float[] activeValue = {1.0f, 1.12f, 1.27f, 1.45f};
				value = (int)(354 - 6.91*user.getAge() + activeValue[activeLevel]*(9.36*user.getWeight() + 7.26*user.getHeight()));
			}
		}
		return Math.round(value);
	}
	
	private float[] getProperCarbRatio(Users user) {
		float[] range = {0.55f, 0.65f};
		return range;
	}
	
	private float[] getProperPrtRatio(Users user) {
		float[] range = {0.07f, 0.20f};
		return range;
	}
	
	private float[] getProperFatRatio(Users user) {
		float[] range = {0.15f, 0.30f};
		if (user.getAge() <= 2) {
			range[0] = 0.20f;
			range[1] = 0.35f;
		}			
		return range;
	}
	
}
