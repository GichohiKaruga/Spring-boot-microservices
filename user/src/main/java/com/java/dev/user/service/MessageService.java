package com.java.dev.user.service;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;


import com.google.gson.Gson;
import com.java.dev.user.controller.UserController;
import com.java.dev.user.dto.UserDto;

@Service
public class MessageService {
	
	private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

	
	@Autowired
    private KafkaTemplate<String, String> kafkaOrderTemplate;
	
	
	public void SendMessage(UserDto user) {	
		Gson gson = new Gson();
		String json = gson.toJson(user);
		LOGGER.info("User Data sent: " +  user.getId());
		ListenableFuture<SendResult<String, String>> future = kafkaOrderTemplate.send("email", user.getEmail(), json);
		
		future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
		      @Override
		      public void onSuccess(SendResult<String, String> result) {
		    	  LOGGER.info("Message [{}] delivered with offset {}" +
		          user.getEmail() + " : " +
		          result.getRecordMetadata().offset());
		      }
		  
		      @Override
		      public void onFailure(Throwable ex) {
		    	  LOGGER.severe("Unable to deliver message [{}]. {}" + " : " + 
		          user.getEmail() + " : " + 
		          ex.getMessage());
		      }
		    });
	}

}
