package com.demo.service;

import com.demo.domain.Exercise;
import com.demo.domain.Users;

import java.util.List;

public interface ExerciseService {
    public List<Exercise> getExerciseListByUseqAndType(Users user);
}
