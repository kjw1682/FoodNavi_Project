package com.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.Food;
import com.demo.domain.History;
import com.demo.domain.Users;
import com.demo.persistence.HistoryRepository;

@Service
public class HistoryServiceImpl implements HistoryService {
	
	@Autowired
	private HistoryRepository historyRepo;
	
	@Override
	public void historyIn(History history) {
		History tmp_history = historyRepo.getHistoryNotConfirmedYetByFood(history.getUser(), history.getFood());
		System.out.println(1);
		if (tmp_history == null) {
			System.out.println(2);
			historyRepo.save(history);
		} else {
			System.out.println(3);
			tmp_history.setServeNumber(history.getServeNumber() + tmp_history.getServeNumber());
			historyRepo.save(tmp_history);
		}		
	}

	@Override
	public void historyUpdate(History history) {
		historyRepo.save(history);		
	}

	@Override
	public void historyOut(History history) {
		historyRepo.delete(history);		
	}

	@Override
	public History getHistoryByHseq(int hseq) {
		return historyRepo.findById(hseq).get();		
	}
	
	@Override
	public List<History> getHistoryListNotConfirmedYet(Users user) {
		return historyRepo.getHistoryListNotConfirmedYet(user);
	}

	@Override
	public List<History> getHistoryListConfirmed(Users user) {
		return historyRepo.getHistoryListConfirmed(user);
	}
	
	@Override
	public float totalKcalToday(Users user) {
		List<History> historyList = historyRepo.getHistoryListConfirmed(user);
		String today = String.valueOf(LocalDate.now());		
		float totalKcalToday = 0f;
		for (History history : historyList) {
			if (String.valueOf(history.getServedDate()).substring(0, 10).equals(today))
				totalKcalToday += history.getFood().getFoodDetail().getKcal()*history.getServeNumber()/history.getFood().getFoodDetail().getN();
		}
		return totalKcalToday;
	}

	@Override
	public float totalKcalOnTable(Users user) {
		List<History> historyList = historyRepo.getHistoryListNotConfirmedYet(user);
		float totalKcalToday = 0f;
		for (History history : historyList) {
			totalKcalToday += history.getFood().getFoodDetail().getKcal()*history.getServeNumber()/history.getFood().getFoodDetail().getN();
		}
		return totalKcalToday;
	}

	@Override
	public float totalCarbToday(Users user) {
		List<History> historyList = historyRepo.getHistoryListConfirmed(user);
		String today = String.valueOf(LocalDate.now());		
		float totalCarbToday = 0f;
		for (History history : historyList) {
			if (String.valueOf(history.getServedDate()).substring(0, 10).equals(today))
				totalCarbToday += history.getFood().getFoodDetail().getCarb()*history.getServeNumber()/history.getFood().getFoodDetail().getN();
		}
		return totalCarbToday;
	}

	@Override
	public float totalCarbOnTable(Users user) {
		List<History> historyList = historyRepo.getHistoryListNotConfirmedYet(user);
		float totalCarbToday = 0f;
		for (History history : historyList) {
			totalCarbToday += history.getFood().getFoodDetail().getCarb()*history.getServeNumber()/history.getFood().getFoodDetail().getN();
		}
		return totalCarbToday;
	}

	@Override
	public float totalPrtToday(Users user) {
		List<History> historyList = historyRepo.getHistoryListConfirmed(user);
		String today = String.valueOf(LocalDate.now());		
		float totalPrtToday = 0f;
		for (History history : historyList) {
			if (String.valueOf(history.getServedDate()).substring(0, 10).equals(today))
				totalPrtToday += history.getFood().getFoodDetail().getPrt()*history.getServeNumber()/history.getFood().getFoodDetail().getN();
		}
		return totalPrtToday;
	}

	@Override
	public float totalPrtOnTable(Users user) {
		List<History> historyList = historyRepo.getHistoryListConfirmed(user);
		float totalPrtToday = 0f;
		for (History history : historyList) {
			totalPrtToday += history.getFood().getFoodDetail().getPrt()*history.getServeNumber()/history.getFood().getFoodDetail().getN();
		}
		return totalPrtToday;
	}

	@Override
	public float totalFatToday(Users user) {
		List<History> historyList = historyRepo.getHistoryListConfirmed(user);
		String today = String.valueOf(LocalDate.now());		
		float totalFatToday = 0f;
		for (History history : historyList) {
			if (String.valueOf(history.getServedDate()).substring(0, 10).equals(today))
				totalFatToday += history.getFood().getFoodDetail().getFat()*history.getServeNumber()/history.getFood().getFoodDetail().getN();
		}
		return totalFatToday;
	}

	@Override
	public float totalFatOnTable(Users user) {
		List<History> historyList = historyRepo.getHistoryListConfirmed(user);
		float totalFatToday = 0f;
		for (History history : historyList) {
			totalFatToday += history.getFood().getFoodDetail().getFat()*history.getServeNumber()/history.getFood().getFoodDetail().getN();
		}
		return totalFatToday;
	}

	@Override
	public History getConfirmedHistoryByUserAndFood(Users user, Food food) {
		
		return historyRepo.getHistoryConfirmedByUserAndFood(user, food);
	}
}
