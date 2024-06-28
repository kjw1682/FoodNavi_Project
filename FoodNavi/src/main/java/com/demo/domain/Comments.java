package com.demo.domain;

import java.util.Date;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
public class Comments {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int cseq;
	
	@ManyToOne
	@JoinColumn(name="bseq", nullable=false)
	private Board board;
	
	@Column(length=2000)
	private String content;
	
	@ManyToOne
	@JoinColumn(name="useq", nullable=false)
	private Users user;

	@ManyToOne // 대댓글이라면 해당 필드는 부모 댓글을 가리킴
	@JoinColumn(name="parent_cseq", nullable=true) // 부모 댓글이 없을 수도 있으므로 nullable 설정
	private Comments parentComment; // 대댓글의 경우에만 사용됨

	@Temporal(value=TemporalType.TIMESTAMP)
	@ColumnDefault("sysdate")
	@Column(updatable=false)
	private Date createdAt;
	
	
}
