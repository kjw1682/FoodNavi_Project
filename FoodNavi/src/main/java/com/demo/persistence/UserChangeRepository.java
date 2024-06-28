package com.demo.persistence;

import com.demo.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.domain.UserChange;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface UserChangeRepository extends JpaRepository<UserChange, Integer> {

        @Query(value = "SELECT uc FROM UserChange uc " +
                "WHERE uc.user = :user " +
                "ORDER BY CAST(uc.createdAt AS TIMESTAMP) desc ")
        List<UserChange> findRecentChanges(Users user);

        UserChange findByUserAndCreatedAtBetween(Users user, Date startDate, Date endDate);

}
