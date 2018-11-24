package com.example.demo.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.persist.ReportDao;
import com.example.demo.services.utils.Mime;
import com.example.demo.services.utils.Report;
import com.example.demo.services.utils.ReportCriteria;
import com.example.demo.services.utils.ReportSource;
import com.example.demo.services.utils.StringUtil;
import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

@Service
public class ReportManager {
	protected final transient Log logger = LogFactory.getLog(getClass());	
	
	final public static int ZIP_COMPRESSION_THRESHOLD_BYTES = 2000000;// After 2meg, compress the data 
	
	@Autowired
	private ReportDao reportDao;
	
	@Autowired
	private ExternalGroupManager externalGroupManager;
	
	public static enum ReportProcessType { 

		// Output Types
		RPT_PROCESS_TYPE_HTTP("Http"),
		RPT_PROCESS_TYPE_EMAIL("Email"),
		RPT_PROCESS_TYPE_BATCH("Batch");

		String code;
		ReportProcessType(String code) {	// constructor
			this.code = code;
		}
		public String getCode() {
			return code;
		}

	}	
	
	public static enum ReportOutputType { 

		// Output Types
		RPT_OUTPUT_TYPE_PDF(Mime.TYPE.PDF),
		RPT_OUTPUT_TYPE_CSV(Mime.TYPE.CSV);

		Mime.TYPE mimeType = null;
		
		ReportOutputType(Mime.TYPE mimeType) {	// constructor
			this.mimeType = mimeType;
		}

		public Mime.TYPE getMimeType() {
			return mimeType;
		}

	}	
	
	/**
     * an enumeration specifying the report to
     * be processed by ReportManager.runJasperReport().
     */
	// SA3416 - added the security authority name code (TB_SCRTY_AUTH.SCRAUTH_NAME) to ReportType enum
    public static enum ReportType {

		// campground reports
    	REPORT_GROUPS("USER_GROUPS" ,"User Groups", "user_group", new String[] {"CUST_RPT_UNSUB", "CUST_RPT_SUB", "CUST_RPT_GRP_SUB"}),
    	REPORT_MEMBERS("USER_GROUP_DETAILS" ,"User Group Details", "TPCG0001_Daily", new String[] {"CUST_RPT_SUB", "CUST_RPT_GRP_SUB"}),
    	REPORT_GROUP_MEMBERS("GROUP_MEMBERS" ,"Group Member Details", "TPCG0001_Daily", new String[] {"CUST_RPT_GRP_SUB"});

    	private static final String reportPath = "/reports/";
    	
    	private String reportCd;
    	private String title;
    	private String filename;
    	private String[] anyAuthority;
     	

    	// Constructor
    	// SA3416 - added the security authority name code (TB_SCRTY_AUTH.SCRAUTH_NAME) to ReportType enum
    	ReportType(String reportCd, String title, String filename, String[] anyAuthority) {
    		this.reportCd = reportCd;
    		this.title = title;
    		this.filename = filename;
    		this.anyAuthority = anyAuthority;
     	}

        /**
         * @return Returns input stream to compiled JasperReport.
         */
    	public InputStream getReportInputStream() {
   			return this.getClass().getResourceAsStream(getFilePath());
    	}

		public String getReportCd() {
			return reportCd;
		}

		public String getTitle() {
			return title;
		}

		// SA3416 - added the security authority name code (TB_SCRTY_AUTH.SCRAUTH_NAME) to ReportType enum
		public String[] getAnyAuthority() {
			return anyAuthority;
		}

        /**
         * @return Returns short name of compiled JasperReport.
         */		
		public String getFilename() {
			return this.filename + ".jasper";
		}
		
		public String getName() {
			return this.filename;
		}		
		
        /**
         * @return Returns fully qualified name of compiled JasperReport.
         */		
		public String getFilePath() {
			return reportPath + getFilename();
		}		
    }	
	
	public Report generateReport(ReportCriteria reportCriteria) {

		String reportKey = StringUtil.randString(10,10); // helpful for logging
		logger.info("Starting Report: " + reportCriteria.getReportType() + " key=" + reportKey + " " + reportCriteria);
		
		byte[] data = null;
		Report report = null;

		JasperPrint jasperPrint = dataSourceJasperPrint(reportCriteria);
		ReportOutputType reportOutputType = reportCriteria.getReportOutputType();

		if (reportOutputType.equals(ReportOutputType.RPT_OUTPUT_TYPE_CSV)) {
			StringWriter stringWriter = csvExportList(jasperPrint);
    		String reportString = (stringWriter != null) ? stringWriter.toString() : reportCriteria.getFailString();
    		data = reportString.getBytes();
    		
    		if(candidateForCompression(data.length)) {
    			byte[] compressedData = compressTheData(reportCriteria.getReportType().getName(), data);
    			if(compressedData != null) {
    				report = new Report(reportCriteria.getReportType(), Mime.TYPE.ZIP, compressedData, reportCriteria.getReportTitle());
    				data = null;
    			} else {
    				// something happened, lets use the original data
    				report = new Report(reportCriteria.getReportType(), Mime.TYPE.CSV, data, reportCriteria.getReportTitle());
    			}
    		} else {
    			report = new Report(reportCriteria.getReportType(), Mime.TYPE.CSV, data, reportCriteria.getReportTitle());
    		}

		} else if (reportOutputType.equals(ReportOutputType.RPT_OUTPUT_TYPE_PDF)) {

			data = pdfExportList(jasperPrint);
			report = new Report(reportCriteria.getReportType(), Mime.TYPE.PDF, data, reportCriteria.getReportTitle());
			
		} 
			

		logger.info("Finished Report: " + reportCriteria.getReportType() + " key=" + reportKey + " " + reportCriteria);
		return report;
	}
	
	private JasperPrint dataSourceJasperPrint(ReportCriteria reportCriteria) {
		JasperPrint jasperPrint = null;
		ReportSource reportSource = null;

		try{

			if (ReportType.REPORT_GROUPS.equals(reportCriteria.getReportType())){
				reportSource = new ReportSource(reportCriteria.getReportType(), getGroups(reportCriteria.getMap()));
			}

			if (reportSource != null){
				jasperPrint =  JasperFillManager.fillReport(reportSource.getReportType().getReportInputStream(), reportCriteria.getMap(), reportSource.getDataSource());

			}
		}catch (JRException ex) {
			logger.warn("pdfExportList(): Could not create the report " + ex.getMessage() + "<<>>" + ex.getLocalizedMessage());
		}
		return jasperPrint;
	}
	
	public JRDataSource getGroups(Map<String, Object> map) {
		
		List<?> groups = (List<?>) map.get("_DATA");


    	JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(groups);
    	return ds;
    }
	
	private boolean candidateForCompression(int byteLength) {
		if(byteLength > ZIP_COMPRESSION_THRESHOLD_BYTES) {
			return true;
		}
		return false;
	}
	
	private byte[] compressTheData(String name, byte[] b) {
		byte[] zippedBytes = null;
		
		File tempDataFile = null;
		FileOutputStream fos = null;
		
		try {
			
			// write the data in memory to a file
			tempDataFile = File.createTempFile("TMP" + name, ".csv"); 
			fos = new FileOutputStream(tempDataFile);
			fos.write(b);
		} catch(FileNotFoundException e) {
			logger.fatal(e);
		} catch(IOException ioe) {
			logger.fatal(ioe);
		} finally {
			// close the streams using close method
			try {
                if (fos != null) {
                    fos.close();
                }
            }
            catch (IOException ioe) {
            	logger.error("Error while closing stream: " + ioe);
            }
 
        }
		
    	byte[] buffer = new byte[2048];
    	File tempZipFile = null;
    	try{
 
    		// zip of the temp data into a temp temp ZIP file
    		tempZipFile = File.createTempFile("TMP" + name, ".zip");
    		fos = new FileOutputStream(tempZipFile);
    		ZipOutputStream zos = new ZipOutputStream(fos);
    		ZipEntry ze= new ZipEntry(name + ".csv");
    		zos.putNextEntry(ze);
    		
    		
    		FileInputStream in = new FileInputStream(tempDataFile);
 
    		int len;
    		while ((len = in.read(buffer)) > 0) {
    			zos.write(buffer, 0, len);
    		}
 
    		in.close();
    		zos.closeEntry();
 
    		//remember to close it
    		zos.close();

    		// get the Zip file and put it in memory
    		in = new FileInputStream(tempZipFile);
    		zippedBytes = IOUtils.toByteArray(in);
    		in.close();
    		
    	} catch(IOException ioe){
    		logger.fatal(ioe);
    	}

    	//clean up temp files
		if(tempDataFile != null) {
			tempDataFile.delete();
		}
		if(tempZipFile != null) {
			tempZipFile.delete();
		}

		return zippedBytes;
	}
	
	public StringWriter csvExportList(JasperPrint jasperPrint) {
    	StringWriter stringWriter = null;
    	if (jasperPrint != null) {
        	stringWriter = new StringWriter();
			JRCsvExporter exporter = new JRCsvExporter();
			
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleWriterExporterOutput(stringWriter));

//			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
//			exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, stringWriter);
//			exporter.getParameters().put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);

			
			try {
				exporter.exportReport();	// to stringWriter.

		    } catch (JRException ex) {
				logger.warn("csvExport(): Could not create the report " + ex.getMessage() + "<<>>" + ex.getLocalizedMessage());
				stringWriter = null;
			}
    	}
		return stringWriter;
    }
	
    public byte[] pdfExportList(JasperPrint jasperPrint) {
    	ByteArrayOutputStream outputStream = null;
    	if (jasperPrint != null) {
    		outputStream = new ByteArrayOutputStream();
    		
    		JRPdfExporter exporter = new JRPdfExporter();
    		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
    		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
    		SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
    		
    		configuration.setPermissions(PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING);
    		exporter.setConfiguration(configuration);

			try
		    {
				exporter.exportReport();	// OutputStream.
				logger.debug("ReportManager.pdfExportList() ===>>><<<===");
		    }
		    catch (JRException ex) {
				logger.warn("pdfExportList(): Could not create the report " + ex.getMessage() + "<<>>" + ex.getLocalizedMessage());
				outputStream = null;
			}
    	}
    	return (outputStream == null)? null : outputStream.toByteArray();
    }

	public ReportDao getReportDao() {
		return reportDao;
	}

	public void setReportDao(ReportDao reportDao) {
		this.reportDao = reportDao;
	}

	public ExternalGroupManager getExternalGroupManager() {
		return externalGroupManager;
	}

	public void setExternalGroupManager(ExternalGroupManager externalGroupManager) {
		this.externalGroupManager = externalGroupManager;
	}
}
