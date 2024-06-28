package com.demo.service;

import com.demo.domain.Exercise;
import com.demo.domain.Users;
import com.demo.persistence.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseServiceImpl implements ExerciseService{
    @Autowired
    private ExerciseRepository exerciseRepo;


    @Override
    public List<Exercise> getExerciseListByUseqAndType(Users user) {
        return exerciseRepo.getExerciseListByUserAndType(user);
    }
}
