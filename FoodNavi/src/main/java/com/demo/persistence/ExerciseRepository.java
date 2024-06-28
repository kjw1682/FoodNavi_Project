package com.demo.persistence;

import com.demo.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.domain.Exercise;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
    @Query(value = "SELECT e FROM Exercise e "
    + "WHERE e.user = :user ")
    List<Exercise> getExerciseListByUserAndType(Users user);

    @Query("SELECT e FROM Exercise e WHERE e.exerciseDate >= :startDate")
    List<Exercise> findLastWeekExercises(@Param("startDate") Timestamp startDate);
}