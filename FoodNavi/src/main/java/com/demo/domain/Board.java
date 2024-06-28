package com.demo.domain;

import java.util.Date;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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
public class Board {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int bseq;
	
	@Column(length=500)
	private String title;
	
	@Column(length=2000)
	private String content;
	
	@ManyToOne
	@JoinColumn(name="useq", nullable=false)
	private Users user;
	
	@Temporal(value=TemporalType.TIMESTAMP)
	@ColumnDefault("sysdate")
	@Column(updatable=false)
	private Date createdAt;
	
	private int cnt;

	@Getter
    @Setter
    private int commentCount;

	@Column
	private int likes;
}
