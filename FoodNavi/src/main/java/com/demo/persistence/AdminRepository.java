package com.demo.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.domain.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
	
	public Optional<Admin> findByAdminid(String adminid);
	
}
