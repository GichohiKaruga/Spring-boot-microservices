package com.java.dev.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.java.dev.user.entity.User;
import com.java.dev.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
	
	@Mock
	UserRepository userRepository;
	
	@InjectMocks
	private UserService userService = new UserServiceImpl();
	
	@Test
	public void testCreateUser() {
		
		
		User user = new User();
		user.setId(262989);
		user.setFirstName("Test");
		user.setLastName("User");
		user.setEmail("test.user@testmail.com");
		user.setPassword("6877");
		
		when(userRepository.save(isA(User.class)))
				.thenAnswer(invocation -> (User) invocation.getArguments()[0]);

		User savedUser = userRepository.save(user);

		assertThat(user).isNotNull();
		
		assertEquals(savedUser.getId(),user.getId());

		verify(userRepository).save(any(User.class));

	}
	
	
	
	
	
	
	
	

}
