package com.tys.ms.dao;

import com.tys.ms.model.User;

import java.util.List;

public interface UserDao {
	User findById(int id);
	
	User findByJobID(String jobId);
	
	void save(User user);
	
	void deleteByJobId(String jobId);
	
	List<User> findAllUsers();

	List<User> findByType(String type);

	List<User> findDownUsers(String leaderId);

	List<User> findAllDownUsers(String leaderId);

}

