package com.java.dev.messaging.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.gson.Gson;
import com.java.dev.messaging.model.User;


@Service
public class MailService {
	
	@Autowired
    private KafkaTemplate<String, String> kafkaOrderTemplate;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	private static final Logger LOGGER = Logger.getLogger(MailService.class.getName());
 
    private TemplateEngine templateEngine;
 
    @Autowired
    public MailService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
 
    public String build(String name) {
        Context context = new Context();
        context.setVariable("name", name);
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Date date = new Date();
		String curdate = dateFormat.format(date);
        context.setVariable("currentdate", curdate);
        
        String template = "welcome";
       
        return templateEngine.process(template, context);
    }  
    
    @KafkaListener(topics = "email", groupId = "dev")
	public void receiveOrder(String json) {
    	LOGGER.info(String.format("Order received -> %s", json));
		Gson gson = new Gson();
		User user = gson.fromJson(json, User.class);
		String email = user.getEmail();
		String name = user.getFirstName() + " " + user.getLastName();
		
		sendMail("Welcome",email, name);
		
    }
    
    private void sendMail(String subject, String recipient, String name) {
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom("EMAIL_ACCOUNT", "EMAIL_NAME");
			messageHelper.setTo(recipient);
			messageHelper.setSubject(subject);
			String content = build(name);
			messageHelper.setText(content, true);
		};
		try {
			javaMailSender.send(messagePreparator);
		} catch (MailException e) {
			LOGGER.severe(e.getMessage());
		}
	}
		
    
 
}
