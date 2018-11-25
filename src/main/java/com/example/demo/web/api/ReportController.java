package com.example.demo.web.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.configuration.WebConfiguration;
import com.example.demo.services.CryptoManager;
import com.example.demo.services.EmailManager;
import com.example.demo.services.ExternalGroupManager;
import com.example.demo.services.ReportManager;
import com.example.demo.services.ReportManager.ReportOutputType;
import com.example.demo.services.ReportManager.ReportProcessType;
import com.example.demo.services.ReportManager.ReportType;
import com.example.demo.services.exceptions.CryptoException;
import com.example.demo.services.models.groups.Group;
import com.example.demo.services.reports.Report;
import com.example.demo.services.reports.input.GroupReportInput;
import com.example.demo.services.reports.input.ReportCriteria;
import com.example.demo.services.reports.input.ReportInput;
import com.example.demo.services.utils.EmailData;
import com.example.demo.services.utils.Mime;
import com.example.demo.web.dtos.DtoReportCriteriaRequest;
import com.example.demo.web.dtos.DtoReportCriteriaResponseWithKey;
import com.example.demo.web.dtos.DtoStringListItem;
import com.example.demo.web.dtos.RestApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;





@RestController
public class ReportController {
	
	protected final transient Log logger = LogFactory.getLog(getClass());	

	@Autowired
	private ExternalGroupManager externalGroupManager;
	
	@Autowired
	private ReportManager reportManager;
	
	@Autowired
	private EmailManager emailManager;
	
	@Autowired
	private CryptoManager cryptoManager;

    @RequestMapping(value = "/api/v1.0/public/report/list", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('CUST_ACCESS')")
    public ResponseEntity<RestApiResponse> getPublicReportList(HttpServletRequest request) {
    	
    	String bearerToken = request.getHeader("Authorization");
    	logger.debug("bearerToken=" + bearerToken);

    	
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	Collection<? extends GrantedAuthority>  authorities = authentication.getAuthorities();
    	
    	
    	
    	List<DtoStringListItem> returnList = new ArrayList<>();
    	ReportType[] reportTypes = ReportManager.ReportType.values();
    	for (ReportType reportType : reportTypes) {
    		for(GrantedAuthority a: authorities) {
    			String[] anyAuthority = reportType.getAnyAuthority();
    			for (String authorityName : anyAuthority) {
        			if(a.getAuthority().equals(authorityName)) {
        				returnList.add(new DtoStringListItem(reportType.getReportCd(), reportType.getTitle()));
        			}
				}
    			

    		}
		}
    	
		RestApiResponse r = new RestApiResponse();
		r.setData(returnList);
		return new ResponseEntity<RestApiResponse>(r, HttpStatus.OK);
    }

    
	@Transactional(readOnly = true)
	@RequestMapping(value = "/api/v1.0/public/reports/create/{key}", headers="Accept=*/*", method = RequestMethod.OPTIONS, produces={"application/pdf", "text/csv", "application/json"}) 
	public ResponseEntity<String>  optionsGenerateReportWithKey(@PathVariable String key, HttpServletResponse response) {
		response.setHeader("Allow", "GET,OPTIONS");
	    return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/api/v1.0/public/reports/create/{key}", headers="Accept=*/*", method = RequestMethod.GET, produces={"application/pdf", "text/csv", "application/json"})
	public void generateReportWithKey(HttpServletRequest request, @PathVariable String key, @Valid DtoReportCriteriaRequest reportCriteriaRequest, HttpServletResponse response) throws IOException, CryptoException {
		
		String bearerToken = request.getHeader("Authorization");
		Map<String,String> map = cryptoManager.decryptMap(key);
//		String userUuid = map.get("userUuid");
		String timeString = map.get("time");	
		String reportCd = reportCriteriaRequest.getReportCd();
		
		Long timeLong = new Long(timeString);
		Date reportRunDate = new Date(timeLong);
		
		if(logger.isDebugEnabled()) {
//			logger.debug("userUuid=" + userUuid);
			logger.debug("timeString=" + timeString);
			logger.debug("reportRunDate=" + reportRunDate);
			logger.debug("reportCd=" + reportCd);
			logger.debug("reportCriteriaRequest=" + reportCriteriaRequest);
		}

		Date yesterday = DateUtils.addHours(new Date(), -24);
		boolean within24Hours = reportRunDate.after(yesterday);

		 // only comparing staff userId, reportName and ran within 24 hours
		if(reportCd.equals(reportCriteriaRequest.getReportCd()) && within24Hours) {
			generateReportInternal(bearerToken, reportCriteriaRequest, response, false);
		} else {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);	
		}
	}
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/api/v1.0/public/reports/create", headers="Accept=*/*", method = RequestMethod.OPTIONS, produces={"application/pdf", "text/csv", "application/json"}) 
	public ResponseEntity<String>  optionsGenerateReport(HttpServletResponse response) {
		response.setHeader("Allow", "POST,OPTIONS");
	    return new ResponseEntity<String>(HttpStatus.OK);
	}	
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/api/v1.0/public/reports/create", headers="Accept=*/*", method = RequestMethod.POST, produces={"application/pdf", "text/csv", "application/json"})
	@PreAuthorize("hasAuthority('CUST_ACCESS')")
	public void generateReport(HttpServletRequest request, @RequestBody @Valid DtoReportCriteriaRequest reportCriteriaRequest, HttpServletResponse response) throws IOException, CryptoException {
		String bearerToken = request.getHeader("Authorization");
		generateReportInternal(bearerToken, reportCriteriaRequest, response, true);
	}
	
	
	private String generateEncryptedKey(DtoReportCriteriaRequest reportCriteriaRequest) throws CryptoException {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		
		logger.debug("authentication=" + authentication);
		logger.debug("principal=" + principal);
		logger.debug("authentication.getName()=" + authentication.getName());

		Map<String,String> map = new HashMap<String,String>();
		//map.put("userUuid", authentication.getName()); // TODO get UUID from token
		map.put("reportCd", reportCriteriaRequest.getReportCd());
		map.put("time", "" + new Date().getTime()); // just to make the key unique.
		
		String key = cryptoManager.encryptMap(map);
		
		return key;
		
	}	
	
	private void generateReportInternal(String bearerToken, DtoReportCriteriaRequest reportCriteriaRequest, HttpServletResponse response, boolean responseWithKey) throws IOException, CryptoException {
		
		ReportInput reportInput = getReportCriteria(bearerToken, reportCriteriaRequest);
		logger.debug("*** reportCriteria: " + reportInput);
		
		ReportProcessType reportProcessType = getReportProcessType(reportCriteriaRequest);
		
		if(ReportProcessType.RPT_PROCESS_TYPE_HTTP.name().equals(reportProcessType.name())) { 
			
			// put report criteria in the session for the report servlet to pick it up
			generateReportInternal(reportCriteriaRequest, reportInput, response, responseWithKey);					
			
		} else if(ReportProcessType.RPT_PROCESS_TYPE_EMAIL.name().equals(reportProcessType.name())) { 
				
			Report report = this.getReportManager().generateReport(reportInput);
			
			if(report.getHasError()) {
				
				RestApiResponse r = new RestApiResponse();
				r.addGlobalError(report.getErrorMessage());			
				returnJson(response, r);				
				
			} else {			

				// User user = UserUtil.getCurrentUser();
	
				Object[] args = new Object[1];
				args[0] = report;
	
				//Notice notice = getNoticeManager().createNotice(Notice.TOKEN.REPORT, user.getUserId(), args);
				// Send email
				EmailData emailData = null; // = new EmailData();  TODO
				getEmailManager().sendEmail(emailData);

				
				String fileType = getReportOutputType(reportCriteriaRequest).getMimeType().getExtension().toUpperCase();
				logger.debug("fileType=" + fileType);
				
				RestApiResponse r = new RestApiResponse();
				// r.addGlobalInfoMsg(getMessage(messageSource, "admin.reports.file.sent", new String[]{fileType}));  TODO			
				r.addGlobalInfoMsg("File Sent");
				returnJson(response, r);
			}
		} 	
	}

	private void generateReportInternal(DtoReportCriteriaRequest reportCriteriaRequest, ReportInput reportInput, HttpServletResponse response, boolean responseWithKey) throws IOException, CryptoException {  
		
		if (reportInput == null) {
			
			// give an error message when the reportCriteria is null, most likely their session timed out.
			RestApiResponse r = new RestApiResponse();
			r.addGlobalError("Unable to generate your report.  Please try again.");			
			returnJson(response, r);			
			

		} else {
			
			if(logger.isDebugEnabled()) {
				logger.debug(reportInput);
			}
			
			
			if(responseWithKey) {
				DtoReportCriteriaResponseWithKey reportResponse = new DtoReportCriteriaResponseWithKey();
				reportResponse.setCriteria(reportCriteriaRequest);
				reportResponse.setKey(this.generateEncryptedKey(reportCriteriaRequest)); // set new key 

				RestApiResponse r = new RestApiResponse();
				r.setData(reportResponse);
				returnJson(response, r);				
				
			} else {
				Report report = this.getReportManager().generateReport(reportInput);

				if(report.getHasError()) {
					
					RestApiResponse r = new RestApiResponse();
					r.addGlobalError(report.getErrorMessage());			
					returnJson(response, r);				
					
				} else {
				
//					if(logger.isDebugEnabled()) {
						logger.info(report);
//					}

					Mime.TYPE mimeType = report.getMimeType();
					byte[] contents = report.getData();
			
					if (mimeType != null && contents != null) {
						response.setContentType(mimeType.getType());
						
						int contentLength = contents.length;
		
						response.setStatus(HttpServletResponse.SC_CREATED);
						response.setContentLength(contentLength);
						response.getOutputStream().write(contents);
					}		
				}
			}
			

		}

		response.getOutputStream().flush();
		response.getOutputStream().close();		
		
	}
	
	protected void returnJson(HttpServletResponse response, RestApiResponse r) throws IOException {
		
        ObjectMapper mapper = new ObjectMapper();
        WebConfiguration.applyCustomMappings(mapper);
        String s =  mapper.writeValueAsString(r);		
		
		byte[] printBytes = s.getBytes();
		if(r.hasErrors()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			response.setStatus(HttpServletResponse.SC_CREATED);
		}
		response.setContentType(Mime.TYPE.JSON.getType());
		response.setContentLength(printBytes.length);
		response.getOutputStream().write(printBytes);			
	}
	
	private ReportInput getReportCriteria(String bearerToken, DtoReportCriteriaRequest c) {
		
		if (ReportType.REPORT_USER_GROUPS.getReportCd().equals(c.getReportCd())) {   
			Group[] groups = externalGroupManager.getGroups(bearerToken);
			List<Group> list = new ArrayList<>(groups.length);
			for(Group g: groups) {
				list.add(g);
			}

			ReportCriteria reportCriteria = new ReportCriteria(ReportType.REPORT_USER_GROUPS, getReportOutputType(c), new Date());  // TODO
			return new GroupReportInput(reportCriteria, list);
		}

		return null;
    }
	
	private ReportOutputType getReportOutputType(DtoReportCriteriaRequest c) {
		if (ReportOutputType.RPT_OUTPUT_TYPE_CSV.getMimeType().getExtension()
				.equalsIgnoreCase(c.getReportOutputType())) {
			return ReportOutputType.RPT_OUTPUT_TYPE_CSV;
		} else {
			return ReportOutputType.RPT_OUTPUT_TYPE_PDF;
		}
	}

	private ReportProcessType getReportProcessType(DtoReportCriteriaRequest c) {
		if (ReportProcessType.RPT_PROCESS_TYPE_BATCH.getCode().equalsIgnoreCase(c.getReportProcessType())) {
			return ReportProcessType.RPT_PROCESS_TYPE_BATCH;
		} else if (ReportProcessType.RPT_PROCESS_TYPE_EMAIL.getCode().equalsIgnoreCase(c.getReportProcessType())) {
			return ReportProcessType.RPT_PROCESS_TYPE_EMAIL;
		} else {
			return ReportProcessType.RPT_PROCESS_TYPE_HTTP;
		}
	}
	
    public ReportManager getReportManager() {
		return reportManager;
	}

	public void setReportManager(ReportManager reportManager) {
		this.reportManager = reportManager;
	}

	public CryptoManager getCryptoManager() {
		return cryptoManager;
	}

	public void setCryptoManager(CryptoManager cryptoManager) {
		this.cryptoManager = cryptoManager;
	}

	public EmailManager getEmailManager() {
		return emailManager;
	}

	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}
}
