package com.demo.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.domain.Users;

public interface UsersRepository extends JpaRepository<Users, Integer>{
	public Optional<Users> findByUserid(String id);
	
	public Users findFirstByOrderByUseqDesc();
}
