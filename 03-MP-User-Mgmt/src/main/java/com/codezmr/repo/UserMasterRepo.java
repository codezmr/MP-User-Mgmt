package com.codezmr.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codezmr.entity.UserMaster;

public interface UserMasterRepo extends JpaRepository<UserMaster, Integer>{

	
	public UserMaster findByEmail(String email);
}
