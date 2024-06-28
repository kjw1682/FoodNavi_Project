package com.demo.domain;

import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Users {
	@Column(length=50)
	private String userid;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int useq;
	
	@Column(length=100)
	private String userpw;
	private String name;
	
	@Column(length=1)
	private String sex;
	
	private int age;
	private float height;
	private float weight;
	
	private String useyn;	
	
	// 검색조건 기본값 설정
	private String no_egg;
	private String no_milk;
	private String no_bean;
	private String no_shellfish;
	private String no_ingredient;
	private String userGoal;
	private String dietType;
	@ColumnDefault(value="0")
	private String vegetarian;

	@ToString.Exclude
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER)
	private List<Rcd> likeList;
	
}
