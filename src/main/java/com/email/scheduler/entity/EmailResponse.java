package com.email.scheduler.entity;

public class EmailResponse {
	private Boolean success;
	private String jobId;
	private String jobGroup;
	private String message;
	public EmailResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public EmailResponse(Boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}
	public EmailResponse(Boolean success, String jobId, String jobGroup, String message) {
		super();
		this.success = success;
		this.jobId = jobId;
		this.jobGroup = jobGroup;
		this.message = message;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getJobGroup() {
		return jobGroup;
	}
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
