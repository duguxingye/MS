package com.tys.ms.service;

import com.tys.ms.model.User;

import java.util.List;

public interface UserService {
	
	User findById(int id);
	
	User findByJobId(String jobId);
	
	void saveUser(User user);
	
	void updateUser(User user);

	void changePwd(User user);
	
	void deleteUserByJobId(String jobId);

	List<User> findAllUsers();

	List<User> findByType(String type);

    List<User> findDownUsers(String leaderId);

	List<User> findAllDownUsers(String leaderId);
	
	boolean isUserJobIdUnique(Integer id, String jobId);

}