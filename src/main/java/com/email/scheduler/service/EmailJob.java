package com.email.scheduler.service;

import java.nio.charset.StandardCharsets;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailJob extends QuartzJobBean{
	
	private static final Logger logger = LoggerFactory.getLogger(EmailJob.class);
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private MailProperties mailProperties;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		logger.info("Executing Job with key { " + context.getJobDetail().getKey()+ " }");
		
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		String subject = jobDataMap.getString("subject");
		String body = jobDataMap.getString("body");
		String recipientEmail = jobDataMap.getString("email");
		String from = "Mail Sender <"+mailProperties.getUsername()+">";
		
		sendemail(from, recipientEmail, subject, body);
	}
	
	private void sendemail(String from, String toEmail, String subject, String body) {
		try {
			logger.info("Sending Mail to " + toEmail);
			
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.toString());
			
			messageHelper.setSubject(subject);
			messageHelper.setText(body);
			messageHelper.setTo(toEmail);
			messageHelper.setFrom(from);
			
			mailSender.send(mimeMessage);
			
			logger.info("Mail sent Successfully to "+toEmail);
			
		} catch (MessagingException e) {
			logger.info("Something went wrong while sending email to "+toEmail);
		}
	}
}
