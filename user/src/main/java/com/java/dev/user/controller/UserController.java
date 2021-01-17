package com.java.dev.user.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.dev.user.dto.UserDto;
import com.java.dev.user.entity.User;
import com.java.dev.user.service.MessageService;
import com.java.dev.user.service.UserService;
import com.java.dev.user.util.StringUtil;

@RestController
@RequestMapping(value = { "/users" })
public class UserController {
	
	private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());
  
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	UserService userService;
	
	@Autowired
	MessageService messageService;
	
	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserDto>> getUsers() {
		List<User> userlist = userService.getUsers();
		List<UserDto> userdtolist = new ArrayList<UserDto>();
		for(User user : userlist) {
			UserDto userdto = new UserDto();
			userdto.setId(user.getId());
			userdto.setFirstName(user.getFirstName());
			userdto.setLastName(user.getLastName());
			userdto.setEmail(user.getEmail());
			userdtolist.add(userdto);
		}
		
		return new ResponseEntity<List<UserDto>>(userdtolist, HttpStatus.OK);
	}
	
	@PostMapping(value = "/", headers = "Accept=application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createUser(@RequestBody UserDto userdto) {
		
		User user = new User();
		user.setFirstName(userdto.getFirstName());
		user.setLastName(userdto.getLastName());
		user.setEmail(userdto.getEmail());
		
		String pwd = StringUtil.randomString(12);
		bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String password = bCryptPasswordEncoder.encode(pwd);
		
		user.setPassword(password);
		
		userService.createUser(user);
		
		String message = "User created";
		
		messageService.SendMessage(userdto);
		
		
		return new ResponseEntity<String>(message, HttpStatus.CREATED);
		
	}


}
