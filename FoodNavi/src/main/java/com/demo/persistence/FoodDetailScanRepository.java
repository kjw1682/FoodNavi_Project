package com.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.domain.FoodDetail;
import org.springframework.data.jpa.repository.Query;

public interface FoodDetailScanRepository extends JpaRepository<FoodDetail, Integer> {
    @Query("SELECT fd FROM FoodDetail fd WHERE fd.food.fseq = :fseq")
    public FoodDetail findByFseq(int fseq);
}
