package com.example.demo.services.utils;

import java.util.HashMap;

/**
 * @author WJW
 *
 */
public interface Mime {
	
	/**
	 * An enumeration encapsulating MIME types.
	 */
	public static enum TYPE { 
		//   extension,  type
		CSV("csv","text/csv"),
		HTML("html","text/html"),
		JSON("json","application/json"),
		PDF("pdf","application/pdf"),
		RTF("rtf","text/rtf"),
		TXT("txt","text/plain"),
		XLS("xls","application/vnd.ms-excel"),
		ZIP("zip","application/zip");
		
		private String mimeExt;
		private String mimeType;
		
		TYPE(String mimeExt, String mimeType) {	// constructor
			this.mimeExt = mimeExt;
			this.mimeType = mimeType;
			mimeExtMap.put(mimeExt, this);
		}
		public String getType() {
			return this.mimeType;
		}
		public String getExtension() {
			return mimeExt;
		}
		
		public TYPE getTypeByExtension(String mExt) {
			return (TYPE)mimeExtMap.get(mExt);
		}
	}

	public static final HashMap<String,Object> mimeExtMap = new HashMap<String,Object>();
}
