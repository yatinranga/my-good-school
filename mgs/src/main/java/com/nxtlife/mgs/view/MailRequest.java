package com.nxtlife.mgs.view;

import java.util.List;

public class MailRequest {

	private String subject;
	private String content;
	private List<String> filePaths;
	private String to;
	private String from;

	public MailRequest() {
	}


	public MailRequest(String subject, String content, List<String> filePaths, String to, String from) {
		this.subject = subject;
		this.content = content;
		this.filePaths = filePaths;
		this.to = to;
		this.from = from;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public List<String> getFilePaths() {
		return filePaths;
	}

	public void setFilePaths(List<String> filePaths) {
		this.filePaths = filePaths;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
	
	

}
