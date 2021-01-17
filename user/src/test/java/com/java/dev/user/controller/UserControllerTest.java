package com.java.dev.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.dev.user.entity.User;
import com.java.dev.user.repository.UserRepository;
import com.java.dev.user.service.UserService;
import com.java.dev.user.service.UserServiceImpl;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
	
	@InjectMocks
	UserController userController;
	
	@Mock
	private UserService userService = new UserServiceImpl();
	
	@Mock
	UserRepository userRepository;
	
	private MockMvc mockMvc;

	ObjectMapper objectMapper = new ObjectMapper();
	
	private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());
	
	@BeforeEach
    public void setup() {
		JacksonTester.initFields(this, new ObjectMapper());
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }
	
	private JacksonTester<User> json;

	@Test
	void testGetUsers() {
		MockHttpServletResponse response = null;
		try {
			response = this.mockMvc.perform(
			        get("/")
			                .accept(MediaType.APPLICATION_JSON))
			        .andReturn().getResponse();
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
        
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        

	}

	@Test
	void testCreateUser() {
		
		User user = new User();
		user.setId(262989);
		user.setFirstName("Test");
		user.setLastName("User");
		user.setEmail("test.user@testmail.com");
		user.setPassword("6877");
		
		MockHttpServletResponse response = null;
		try {
			response = this.mockMvc.perform(
			        post("/").contentType(MediaType.APPLICATION_JSON).content(
			                json.write(user).getJson()
			        )).andReturn().getResponse();
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
        
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
	}

}
