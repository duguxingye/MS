package com.tys.ms.service;

import com.tys.ms.model.UserProfile;

import java.util.List;

public interface UserProfileService {

	UserProfile findById(int id);

	UserProfile findByType(String type);
	
	List<UserProfile> findAll();

	List<UserProfile> findDownAll(int upId);
	
}
