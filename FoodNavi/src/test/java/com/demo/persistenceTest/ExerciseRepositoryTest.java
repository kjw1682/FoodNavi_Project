package com.demo.persistenceTest;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.demo.domain.ExerciseOption;
import com.demo.persistence.ExerciseOptionRepository;

@SpringBootTest
public class ExerciseRepositoryTest {
	@Autowired
	private ExerciseOptionRepository exerciseOptionRepo;
	
	@Disabled
	@Test
	public void insertExerciseTest() {
		ExerciseOption exo = ExerciseOption.builder()
				.type("농구")
				.fomula("100")
				.build();
		
		exerciseOptionRepo.save(exo);
	}
}
