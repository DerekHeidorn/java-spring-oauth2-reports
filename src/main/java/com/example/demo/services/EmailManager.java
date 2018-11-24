package com.example.demo.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.demo.services.utils.EmailData;

@Service
public class EmailManager {

	private Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private Environment environment;
	
	public boolean sendEmail(EmailData emailData) {
		
		boolean delivered = false;
		
		JavaMailSender javaMailSender = getJavaMailSender();		
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		
		/* ------------------------------------- */
		/* Get Header Information                */
		/* ------------------------------------- */
		EmailAddressFilteringList sendTo = new EmailAddressFilteringList();
		EmailAddressFilteringList sendCc = new EmailAddressFilteringList();
		EmailAddressFilteringList sendBcc = new EmailAddressFilteringList();
		String subject = emailData.getSubject();
		String from = emailData.getFrom();
		
		for (String sendToEmailAddress : emailData.getSendTo()) {
			sendTo.add(sendToEmailAddress);
		}
		
		for (String sendToEmailAddress : emailData.getSendCc()) {
			sendCc.add(sendToEmailAddress);
		}
		
		for (String sendToEmailAddress : emailData.getSendBcc()) {
			sendBcc.add(sendToEmailAddress);
		}

		if(sendTo.size() > 0 || sendCc.size() > 0 || sendBcc.size() > 0){
			// if from is empty, sent default email
			if (from == null) {
				from = environment.getRequiredProperty("REPORT_APP_EMAIL_DEFAULT_SENDER");
			}		

			/* ------------------------------------- */
			/* Get Body/Attachment Information       */
			/* ------------------------------------- */
			byte[] bodyText = null;
			if(emailData.getBodyTxt() != null) {
				bodyText = emailData.getBodyTxt().getBytes();
			}
			byte[] bodyHtml = null;	
			if(emailData.getBodyHtml() != null) {
				bodyHtml = emailData.getBodyHtml().getBytes();
			}

			List<EmailData.Attachment> attachments = new ArrayList<>();
			for (EmailData.Attachment a : emailData.getAttachments()) {
				attachments.add(a);
			}
			
			/* ------------------------------------- */
			/* Prepare the Email Nofication          */
			/* ------------------------------------- */		
			MimeMessageHelper mimeMsgHelper = getMimeMessageHelper(mimeMessage, !attachments.isEmpty());
			
			if(mimeMsgHelper != null){
				boolean hasContent = false;
				
				try {
					if (sendTo.size() > 0) mimeMsgHelper.setTo(sendTo.toArray());
					if (sendCc.size() > 0) mimeMsgHelper.setCc(sendCc.toArray());
					if (sendBcc.size() > 0) mimeMsgHelper.setBcc(sendBcc.toArray());
					
					mimeMsgHelper.setSentDate(new Date());
					mimeMsgHelper.setSubject(subject);
					mimeMsgHelper.setFrom(from);
					if(bodyText != null) {
						mimeMsgHelper.setText(new String(bodyText), false);
						hasContent = true;
					}
					if(bodyHtml != null) {
						mimeMsgHelper.setText(new String(bodyHtml), true);
						hasContent = true;
					}
					
					// set blank message for email that has no body (may just have an attachment
					if(!hasContent) {
						logger.warn("Email has no body!");
						mimeMsgHelper.setText("", false);
					}			
					
					// add attachments
					if (!attachments.isEmpty()) {
						for (EmailData.Attachment a : attachments) {

							ByteArrayResource attachStream = new ByteArrayResource(a.getContentData());
							mimeMsgHelper.addAttachment(a.getName(), attachStream, a.getMimeType());
						}
						hasContent = true;
					}
					
					
					
					Address[] addrs = mimeMsgHelper.getMimeMessage().getAllRecipients();
					if(logger.isDebugEnabled()) {
						logger.debug("hasContent" + hasContent + ",addrs" + addrs);
					}
					
					if(addrs != null && hasContent){
						logger.info("Sending email message: " + emailData.toString());
						javaMailSender.send(mimeMessage);
						logger.info("Finished sending email message: " + emailData.toString());
						delivered = true;
					}else{
						logger.error("Can't send email: " + emailData.toString() + " hasContent=" + hasContent + ", addrs" + addrs);
					}
				} catch (MessagingException me) {
					logger.info(" MessagingException: " + me.getMessage() + " " + me.getLocalizedMessage());
				} catch(MailException me) {
					logger.fatal(" MailException: " + me.getMessage() + " " + me.getLocalizedMessage());
				} catch(Throwable th){
					logger.fatal(" MailException: " + th.getMessage() + " " + th.getLocalizedMessage(), th);
				}
			}
			if(logger.isDebugEnabled()) {
				logger.debug("~~~"+Thread.currentThread().getName()+" Notification : " + emailData.toString() + ", Delivered="+delivered);
			}
		}
		
		return delivered;
	}
	
	public MimeMessageHelper getMimeMessageHelper(MimeMessage mimeMessage, boolean isMultipPart){
		MimeMessageHelper mimeMsgHelper = null;
		try {
			mimeMsgHelper = new MimeMessageHelper(mimeMessage, isMultipPart);
			return mimeMsgHelper;
		}
		catch (MessagingException me) {
			logger.error("~~~"+Thread.currentThread().getName()+" MessagingException: " + me.getMessage() + " " + me.getLocalizedMessage());
			logger.error("~~~ Failed to create " + MimeMessageHelper.class.getCanonicalName() + " instance. Email message was not sent.");
			logger.error("~~~"+Thread.currentThread().getName()+me.getStackTrace());
		}  
		return mimeMsgHelper;
	}
	
	private JavaMailSenderImpl getJavaMailSender() {
		JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();

		Properties jmProps = javaMailSenderImpl.getJavaMailProperties();
		jmProps.setProperty("mail.pop3.connectionpooltimeout", "16000");
		jmProps.setProperty("mail.pop3.connectiontimeout", "16000");
		jmProps.setProperty("mail.pop3.timeout", "16000");
		jmProps.setProperty("mail.smtp.connectiontimeout", "16000");
		jmProps.setProperty("mail.smtp.timeout", "16000");
		javaMailSenderImpl.setJavaMailProperties(jmProps);
		javaMailSenderImpl.setPort(25);
		
		String host = environment.getRequiredProperty("REPORT_APP_SMTP_HOST").trim();
		javaMailSenderImpl.setHost(host);
		
		String port = getMailPort();
		Integer portNum = null;
		
		try{
			portNum = Integer.parseInt(port);
		}catch(NumberFormatException ex){
			logger.warn("~~~"+Thread.currentThread().getName()+" Cannot convert " + port + " sendTo integer, use 25 as default port number.");
		}
		
		if(portNum != null){
			javaMailSenderImpl.setPort(portNum);
		}
		logger.debug("Use SMTP port " + javaMailSenderImpl.getPort());
		return javaMailSenderImpl;
	}
	
	private String getMailPort(){
		String port = environment.getRequiredProperty("REPORT_APP_SMTP_PORT");
		if(StringUtils.isNotBlank(port)){
			return port.trim();
		}
		return port;
	}
	
	private class EmailAddressFilteringList{
		private List<String> emailList = new ArrayList<String>();
		private static final String INVALID_EMAIL_SUFFIX_MARKER = "invalid";
		private static final String INVALID_EMAIL_SUFFIX_MARKER2 = "invali";
		private static final String INVALID_EMAIL_PREFIX_MARKER = "@";
		public EmailAddressFilteringList(){
			
		}
		
		public void add(String emailAddress){
			if(StringUtils.isNotBlank(emailAddress)){
				String lowercaseEmail = emailAddress.toLowerCase();
				
				/* 
				 * email addresses have already been validated at time of user entering them
				 * using web form. The simple filtering here is for stop sending emails that
				 * are obfuscated from production database.
				 */
				if(lowercaseEmail.endsWith(INVALID_EMAIL_SUFFIX_MARKER)){
					logger.debug("Invalid email address ending with " + INVALID_EMAIL_SUFFIX_MARKER + ".");
				}else if(lowercaseEmail.endsWith(INVALID_EMAIL_SUFFIX_MARKER2)){
					logger.debug("Invalid email address ending with " + INVALID_EMAIL_SUFFIX_MARKER2 + ".");
				}else if(lowercaseEmail.startsWith(INVALID_EMAIL_PREFIX_MARKER)){
					logger.debug("Invalid email address ending with " + INVALID_EMAIL_PREFIX_MARKER + ".");
				}else if(lowercaseEmail.indexOf("@") == -1 ){
					logger.debug("Invalid email address not containing @ sign.");
				}else{
					emailList.add(emailAddress);
				}
			}
		}
		
		@SuppressWarnings("unused")
		public List<String> getEmailList(){
			return emailList;
		}
		public int size(){
			return emailList.size();
		}
		
		public String[] toArray(){
			return emailList.toArray(new String[0]);
		}
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	
}
