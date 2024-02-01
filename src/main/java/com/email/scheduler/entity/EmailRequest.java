package com.email.scheduler.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class EmailRequest {
	
	@Email
	@NotEmpty
	private String email;
	
	@NotEmpty
	private String subject;
	
	@NotEmpty
	private String body;
	
	@NotNull
	private LocalDateTime dateTime;
	
	@NotNull
	private ZoneId timezone;

	public EmailRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmailRequest(@Email @NotEmpty String email, @NotEmpty String subject, @NotEmpty String body,
			@NotEmpty LocalDateTime dateTime, @NotEmpty ZoneId timezone) {
		super();
		this.email = email;
		this.subject = subject;
		this.body = body;
		this.dateTime = dateTime;
		this.timezone = timezone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public ZoneId getTimezone() {
		return timezone;
	}

	public void setTimezone(ZoneId timezone) {
		this.timezone = timezone;
	}

	@Override
	public String toString() {
		return "EmailRequest [email=" + email + ", subject=" + subject + ", body=" + body + ", dateTime=" + dateTime
				+ ", timezone=" + timezone + "]";
	}
	
	
}
