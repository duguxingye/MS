package com.tys.ms.dao;

import com.tys.ms.model.UserProfile;

import java.util.List;

public interface UserProfileDao {

	List<UserProfile> findAll();

	List<UserProfile> findDown(int upId);
	
	UserProfile findByType(String type);
	
	UserProfile findById(int id);
}
