package com.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;


import com.demo.domain.FoodDetail;

public interface AdminFoodDetailRepository extends JpaRepository<FoodDetail, Integer> {
	public FoodDetail findFirstByOrderByFdseqDesc();

}
