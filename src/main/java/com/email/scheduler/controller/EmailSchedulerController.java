package com.email.scheduler.controller;

import java.util.Date;
import java.time.ZonedDateTime;

import java.util.UUID;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.email.scheduler.entity.EmailRequest;
import com.email.scheduler.entity.EmailResponse;
import com.email.scheduler.service.EmailJob;

import jakarta.validation.Valid;

@RestController
public class EmailSchedulerController {
	
	private static final Logger logger = LoggerFactory.getLogger(EmailSchedulerController.class);
	
	@Autowired
	private Scheduler scheduler;
	
	@PostMapping("/schedule/email")
	public ResponseEntity<EmailResponse> scheduleEmail(@Valid @RequestBody EmailRequest emailRequest){
		try {
			ZonedDateTime zonaldateTime = ZonedDateTime.of(emailRequest.getDateTime(), emailRequest.getTimezone());
			
			if (zonaldateTime.isBefore(ZonedDateTime.now())) {
				EmailResponse emailResponse = new EmailResponse(false, "Provide Date and Time cann't be in past. Please provide valid date and time (after current time) to schedule Email.!!!");
				
				logger.info("Provide Date and Time cann't be in past. Please provide valid date and time (after current time) to schedule Email.!!!");
				return ResponseEntity.badRequest().body(emailResponse);
			}
			
			logger.info("Scheduling Job for "+emailRequest.getEmail() + " at " +ZonedDateTime.now());
			JobDetail jobDetail = builJobDetail(emailRequest);
			Trigger trigger = buildTrigger(jobDetail, zonaldateTime);
			scheduler.scheduleJob(jobDetail, trigger);
			
			EmailResponse emailResponse = new EmailResponse(true, jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), "Congrats!!!.. Email Scheduled Successfully.!");
			
			logger.info("Job Scheduled Successfully using Quartz for "+emailRequest.getEmail() + " at " +ZonedDateTime.now());
			return ResponseEntity.ok(emailResponse);
		} catch (SchedulerException se) {

			logger.error("Error Scheduling email",se);
			EmailResponse emailResponse = new EmailResponse(false, "Error while scheduling the email. Please Try again later.!!!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(emailResponse);
		}catch (Exception e) {
			logger.error("Something went wrong while scheduling email",e);
			EmailResponse emailResponse = new EmailResponse(false, "Something went wrong while scheduling email. Please Try again later.!!!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(emailResponse);
		}
	}
	
	
	private JobDetail builJobDetail(EmailRequest emailRequest) {
		
		JobDataMap jobDataMap = new JobDataMap();
		
		jobDataMap.put("email", emailRequest.getEmail());
		jobDataMap.put("subject", emailRequest.getSubject());
		jobDataMap.put("body", emailRequest.getBody());
		
		return JobBuilder.newJob(EmailJob.class)
				.withIdentity(UUID.randomUUID().toString(), "email-jobs")
				.withDescription("Send Email Job")
				.usingJobData(jobDataMap)
				.storeDurably()
				.build();
	}
	
	private Trigger buildTrigger(JobDetail jobDetail, ZonedDateTime zonedDateTime) {
		return TriggerBuilder.newTrigger()
				.forJob(jobDetail)
				.withIdentity(jobDetail.getKey().getName(), "email-triggers")
				.withDescription("Send Email Trigger")
				.startAt(Date.from(zonedDateTime.toInstant()))
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
				.build();
	}
}
