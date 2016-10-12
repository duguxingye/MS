package com.tys.ms.service;

import com.tys.ms.dao.UserDao;
import com.tys.ms.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao dao;

	@Autowired
    private PasswordEncoder passwordEncoder;
	
	public User findById(int id) {
		return dao.findById(id);
	}

	public User findByJobId(String jobId) {
		User user = dao.findByJobID(jobId);
		return user;
	}

	public void saveUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		dao.save(user);
	}

	public void updateUser(User user) {
		User entity = dao.findById(user.getId());
		if(entity!=null){
			entity.setJobId(user.getJobId());
			if(!user.getPassword().equals(entity.getPassword())){
				entity.setPassword(passwordEncoder.encode(user.getPassword()));
			}
			entity.setName(user.getName());
			entity.setPhone(user.getPhone());
			entity.setUserProfile(user.getUserProfile());
			entity.setHasLocked(user.isHasLocked());
		}
	}

	public void changePwd(User user) {
        updateUser(user);
    }

	
	public void deleteUserByJobId(String jobId) {
		dao.deleteByJobId(jobId);
	}

	public List<User> findAllUsers() {
		return dao.findAllUsers();
	}

    @Override
    public List<User> findByType(String type) {
        // TODO
        return null;
    }

    public List<User> findDownUsers(String leaderId) {
		return dao.findDownUsers(leaderId);
	}

	public List<User> findAllDownUsers(String leaderId) {
		return dao.findAllDownUsers(leaderId);
	}

	public boolean isUserJobIdUnique(Integer id, String jobId) {
		User user = findByJobId(jobId);
		return ( user == null || ((id != null) && (user.getId() == id)));
	}
}
