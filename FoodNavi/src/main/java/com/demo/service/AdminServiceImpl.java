package com.demo.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.Admin;
import com.demo.persistence.AdminRepository;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepository adminRepo;
	
	@Override
	public int adminCheck(Admin vo) {
		int result = -1;
		Optional<Admin> admin = adminRepo.findByAdminid(vo.getAdminid());
		if(admin.isEmpty()) {
			result = -1;
		} else if(vo.getAdminpw().equals(admin.get().getAdminpw())) {
			result = 1;
		} else {
			result = 0;
		}
		
		
		return result;	
	}

	@Override
	public Admin getAdmin(String adminid) {
		
		return adminRepo.findByAdminid(adminid).get();
		
	}

	

}
