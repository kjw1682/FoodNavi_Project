package com.demo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.config.PathConfig;
import com.demo.domain.Food;
import com.demo.dto.FoodVo;
import com.demo.dto.UserVo;
import com.demo.persistence.FoodDetailScanRepository;

@Service
public class FoodRecommendServiceImpl implements FoodRecommendService {
	
	@Autowired
	private DataInOutService dataInOutService;
	@Autowired
	private FoodScanService foodScanService;
	@Autowired
	private FoodDetailScanRepository foodDetailScanRepo;
	
	@Override
	public List<FoodVo> getFoodRecommendList(String pyFile, UserVo userVo, List<Food> filteredList) {
		String csvFile = "tmp_filtered.csv";
		pyFile = PathConfig.realPath(pyFile);
		csvFile = PathConfig.realPath(csvFile);

		List<FoodVo> foodRecommendList = new ArrayList<>();
		dataInOutService.filteredListToCsv(filteredList);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
			.append(String.valueOf(userVo.getUser().getUseq())).append(",")
			.append(userVo.getUser().getSex()).append(",")
			.append(String.valueOf(userVo.getUser().getAge())).append(",")
			.append(String.valueOf(userVo.getUser().getHeight())).append(",")
			.append(String.valueOf(userVo.getUser().getWeight())).append(",")
			.append(userVo.getUser().getNo_egg()).append(",")
			.append(userVo.getUser().getNo_milk()).append(",")
			.append(userVo.getUser().getNo_bean()).append(",")
			.append(userVo.getUser().getNo_shellfish()).append(",")
			.append(userVo.getUser().getUserGoal()).append(",")
			.append(userVo.getUser().getDietType()).append(",")
			.append(userVo.getUser().getVegetarian()).append(",")
			.append(userVo.getLastMealType()).append("\n");
			
		ProcessBuilder processBuilder = new ProcessBuilder("python", pyFile, stringBuilder.toString());
		try {
			Process process = processBuilder.start();
			System.out.println("파이썬 프로그램 실행 성공!");
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("파이썬 프로그램 실행 실패!");
		}
		
		File file = new File(csvFile);
		if (file.exists()) {
			if (file.delete()) {
				System.out.println(csvFile+"삭제 완료");
			} else {
				System.out.println(csvFile+"삭제 실패");
			}
		}
		
		String recommendFile = PathConfig.realPath("tmp_recommendList.csv");
		file = new File(recommendFile);
		if (file.exists()) {
			System.out.println("tmp_recommendList.csv 파일 생성 성공");
			
			// 받아오기 프로세스 입력
			int check = -1;
			int count = 0;
			String text = "";
			
			try {
				FileReader fr = new FileReader(recommendFile);
				BufferedReader br = new BufferedReader(fr);

				while(true) {
					text = br.readLine();
					if (text == null)
						break;			

					if (check != -1) {
						String[] input = text.split(",");
						Food food = foodScanService.getFoodByFseq(Integer.parseInt(input[0]));
						FoodVo foodVo = new FoodVo(food, Float.parseFloat(input[1]));
						boolean isEqual = foodRecommendList.contains(foodVo);
						if (!isEqual) {
							foodRecommendList.add(foodVo);
						}							
						text = "";
					} else {
						check = 0;
						text = "";
					}	
				}
				
				br.close();
				fr.close();
				
				file = new File(recommendFile);
				if (file.exists()) {
					if (file.delete()) {
						System.out.println("tmp_recommendList.csv 삭제 완료");
					} else {
						System.out.println("tmp_recommendList.csv 삭제 실패");
					}
				}
				
				
				
			} catch (IOException e) {
				System.out.println((count+1)+"번 데이터 입력 중 오류 발생!");
				e.printStackTrace();
			}
		} else {
			System.out.println("tmp_recommendList.csv 파일 생성 실패");
		}
		
		System.out.println(stringBuilder.toString());
		return foodRecommendList;
		
	}

}
