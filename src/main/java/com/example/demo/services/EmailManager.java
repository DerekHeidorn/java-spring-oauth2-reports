package com.example.demo.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.demo.services.models.persons.User;
import com.example.demo.services.reports.Report;
import com.example.demo.services.utils.EmailData;
import com.example.demo.services.utils.Mime;

@Service
public class EmailManager {

	private Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private Environment environment;
	
    public static enum EmailType {

    	REPORT("REPORT" ,"Report", "report.txt", "report.html");

    	private static final String EMAIL_TEMPLATE_DIR = "/email/templates/";
    	
    	private String emailCd;
    	private String subject;
    	private String txtTemplate;
    	private String htmlTemplate;


    	EmailType(String emailCd, String subject, String txtTemplate, String htmlTemplate) {
    		this.emailCd = emailCd;
    		this.subject = subject;
    		this.txtTemplate = txtTemplate;
    		this.htmlTemplate = htmlTemplate;
     	}

	
		public String getHtmlFilePath() {
			return EMAIL_TEMPLATE_DIR + getHtmlTemplate();
		}
		
		public String getTxtFilePath() {
			return EMAIL_TEMPLATE_DIR + getTxtTemplate();
		}

		public String getEmailCd() {
			return emailCd;
		}

		public void setEmailCd(String emailCd) {
			this.emailCd = emailCd;
		}

		public String getSubject() {
			return subject;
		}

		public String getTxtTemplate() {
			return txtTemplate;
		}

		public String getHtmlTemplate() {
			return htmlTemplate;
		}
	
    }	
	
	public EmailData generateReportEmail(User userProfile, Report report) {
		
//		ReportType reportType = report.getReportType();
		Mime.TYPE mimeType = report.getMimeType();
		
		EmailType emailType = EmailType.REPORT;
		
		Map<String, String> substitutes = new HashMap<>();
		substitutes.put("reportName", report.getReportTitle());
		substitutes.put("formattedName", userProfile.getFormattedName());

		
		// String userUuid, String to, String from, String subject, String bodyTxt, String bodyHtml
		String to = userProfile.getUsername();
		String from = environment.getRequiredProperty("REPORT_APP_EMAIL_DEFAULT_SENDER");
		String subject = "Report: ";
		String bodyTxt = getTemplateFileAsString(emailType.getTxtFilePath(), substitutes);
		String bodyHtml = getTemplateFileAsString(emailType.getHtmlFilePath(), substitutes);
		EmailData e = new EmailData("123", to, from, subject, bodyTxt, bodyHtml);
		// mimeMsgHelper.addAttachment(key, attachStream, tbNtfnContnt.getMimeType());
		// byte[] contentData, String contentType, String name, String mimeType
		e.addAttachment("reportAttachment", report.getData(), mimeType.getType());

		
		return e;
	}
	
	public String getTemplateFileAsString(String filepath, Map<String, String> substitutes) {
		
		InputStream inputStream = this.getClass().getResourceAsStream(filepath);
		

		String contents = null;
		try {
			contents = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	    StrSubstitutor sub = new StrSubstitutor(substitutes);
	    String result = sub.replace(contents);
		
		return result;
		
	}
	

	
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
						logger.debug("hasContent=" + hasContent + ",addrs=" + addrs);
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
					logger.fatal(" Throwable: " + th.getMessage() + " " + th.getLocalizedMessage(), th);
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
		
		
		String host = environment.getRequiredProperty("REPORT_APP_SMTP_HOST").trim();
		String port = environment.getRequiredProperty("REPORT_APP_SMTP_PORT").trim();
		javaMailSenderImpl.setHost(host);
		javaMailSenderImpl.setPort(new Integer(port).intValue());
		if(logger.isDebugEnabled()) {
			logger.debug("Using SMTP (host,port)=" + javaMailSenderImpl.getHost() + "," + javaMailSenderImpl.getPort());
		}

		return javaMailSenderImpl;
	}
	
//	private String getMailPort(){
//		String port = environment.getRequiredProperty("REPORT_APP_SMTP_PORT");
//		if(StringUtils.isNotBlank(port)){
//			return port.trim();
//		}
//		return port;
//	}
	
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
