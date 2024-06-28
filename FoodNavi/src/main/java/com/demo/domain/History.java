package com.demo.domain;

import java.util.Date;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
public class History {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int hseq;
	
	@ManyToOne
	@JoinColumn(name="useq", nullable=false)
	private Users user;
	
	@ManyToOne
	@JoinColumn(name="fseq", nullable=false)
	private Food food;
	
	private int serveNumber;	
	private Date servedDate;
	
	// 추천조건설정 기록
	private String mealType;
	private String no_egg;
	private String no_milk;
	private String no_bean;
	private String no_shellfish;
	private String no_ingredient;
	private String purpose;
	private String dietType;
	private String vegetarian;
	
}
