package com.demo.persistence;

import com.demo.domain.ExerciseOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExerciseOptionRepository extends JpaRepository<ExerciseOption, Integer> {
	public Optional<ExerciseOption> findByType(String type);
	public ExerciseOption findFirstByOrderByEoseqDesc();
	List<ExerciseOption> findByTypeContainingIgnoreCase(String term);
	boolean existsByType(String type);
}