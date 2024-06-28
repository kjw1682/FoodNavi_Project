package com.demo.persistence;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.Users;

public interface AdminUsersRepository extends JpaRepository<Users, Integer> {
	@Query("SELECT users FROM Users users "
			+ "WHERE users.name LIKE %:searchName% ")
	public Page<Users> getUsersList(String searchName, Pageable pageable);
}
