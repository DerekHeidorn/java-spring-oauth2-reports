package com.example.demo.services.utils;

import java.util.ArrayList;
import java.util.List;

public class EmailData {
	
	private String userUuid = null;


	private List<String> sendTo = new ArrayList<>();
	private List<String> sendCc = new ArrayList<>();
	private List<String> sendBcc = new ArrayList<>();
	
	private String from = new String();
	private String subject = new String();
	
	private String bodyTxt = new String();
	private String bodyHtml = new String();
	
	private List<Attachment> attachments = new ArrayList<>();
	
	public EmailData(String userUuid, String to, String from, String subject, String bodyTxt, String bodyHtml) {
		super();
		this.userUuid = userUuid;
		this.sendTo.add(to);
		this.from = from;
		this.subject = subject;
		this.bodyTxt = bodyTxt;
		this.bodyHtml = bodyHtml;
	}
	
	public List<String> getSendTo() {
		return sendTo;
	}

	public void setSendTo(List<String> sendTo) {
		this.sendTo = sendTo;
	}

	public List<String> getSendCc() {
		return sendCc;
	}

	public void setSendCc(List<String> sendCc) {
		this.sendCc = sendCc;
	}

	public List<String> getSendBcc() {
		return sendBcc;
	}

	public void setSendBcc(List<String> sendBcc) {
		this.sendBcc = sendBcc;
	}

	public String getBodyTxt() {
		return bodyTxt;
	}

	public void setBodyTxt(String bodyTxt) {
		this.bodyTxt = bodyTxt;
	}

	public String getBodyHtml() {
		return bodyHtml;
	}

	public void setBodyHtml(String bodyHtml) {
		this.bodyHtml = bodyHtml;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Override
	public String toString() {
		return "EmailData [userUuid=" + userUuid + ", subject=" + subject + "]";
	}
	
	public class Attachment {
		
		private byte[] contentData;	
	    
	    private String contentType;    

	    private String name;

	    private String mimeType;

		public byte[] getContentData() {
			return contentData;
		}

		public void setContentData(byte[] contentData) {
			this.contentData = contentData;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getMimeType() {
			return mimeType;
		}

		public void setMimeType(String mimeType) {
			this.mimeType = mimeType;
		} 
	}
}
