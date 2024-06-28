package com.demo.controller;

import com.demo.domain.Exercise;
import com.demo.persistence.ExerciseRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/activities/data")
public class ExerciseController {

    private final ExerciseRepository exerciseRepository;

    public ExerciseController(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @GetMapping("/last-week")
    public int[] getLastWeekExercises() {
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
        LocalDateTime startOfDay = oneWeekAgo.atStartOfDay();
        Timestamp timestamp = Timestamp.valueOf(startOfDay);

        List<Exercise> exercises = exerciseRepository.findLastWeekExercises(timestamp);
        int[] exerciseTimes = new int[7]; // 일, 월, 화, 수, 목, 금, 토 순서로 운동 시간을 저장

        for (Exercise exercise : exercises) {
            Date date = exercise.getExerciseDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) -1; // 데이터가 없으면 0이 되도록 설정
            exerciseTimes[dayOfWeek] += exercise.getTime();
        }

        return exerciseTimes;
    }
}