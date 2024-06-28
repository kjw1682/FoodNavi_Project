package com.demo.domain;

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
public class FoodIngredient {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int fiseq;
	
	@ManyToOne
	@JoinColumn(name="fseq", nullable=false)
	private Food food;
	
	@ManyToOne
	@JoinColumn(name="iseq", nullable=false)
	private Ingredient ingredient;
	
	private int amount;
}
