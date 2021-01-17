package com.java.dev.user.service;

import java.util.List;

import com.java.dev.user.entity.User;

public interface UserService {
	
	public User createUser(User user);
	public List<User> getUsers();

}
