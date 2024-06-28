package com.demo.service;

import java.util.List;

import com.demo.domain.Food;
import com.demo.domain.History;
import com.demo.domain.Users;

public interface HistoryService {
	public void historyIn(History history);
	public void historyUpdate(History history);
	public void historyOut(History history);
	public History getHistoryByHseq(int hseq);

	public History getConfirmedHistoryByUserAndFood(Users user, Food food);
	public List<History> getHistoryListNotConfirmedYet(Users user);
	public List<History> getHistoryListConfirmed(Users user);

	public float totalKcalToday(Users user);
	public float totalKcalOnTable(Users user);
	public float totalCarbToday(Users user);
	public float totalCarbOnTable(Users user);
	public float totalPrtToday(Users user);
	public float totalPrtOnTable(Users user);
	public float totalFatToday(Users user);
	public float totalFatOnTable(Users user);
}
