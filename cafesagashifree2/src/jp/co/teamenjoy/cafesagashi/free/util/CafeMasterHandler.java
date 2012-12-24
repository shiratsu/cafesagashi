package jp.co.teamenjoy.cafesagashi.free.util;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class CafeMasterHandler extends DefaultHandler{
	
	  public List<HashMap> xmlParceData = new ArrayList<HashMap>();
	  public HashMap xmlData = new HashMap();
	  public String statusCode;
	  public String elementData;
	  public String elementName;
	
	  // 文書の開始通知
	  public void startDocument() {
	    System.out.println("startDocument");
	  }
	  
	  // 要素の開始通知
	  public void startElement(String namespaceURI,
	                           String localName,
	                           String qName,
	                           Attributes atts) {
	 
	    //System.out.println("startElement: " + qName);
	    if ("Result".equals(qName)) {
	    	xmlData = new HashMap();
		}else if("statusCode".equals(qName)){	
			
			statusCode = null;
		}else if(!xmlData.isEmpty()){
			elementData = null;
			elementName = qName;
		}
	    
	  }
	  
	  // 要素内の文字データの通知
	  public void characters(char[] ch, int start, int length) {
		elementData = new String(ch, start, length);
	  }
	  
	  // 要素の終了通知
	  public void endElement(String namespaceURI,
	                         String localName,
	                         String qName) {
	 
	    //System.out.println("endElement: " + qName);
		if ("Result".equals(qName)) {
	    	xmlParceData.add(xmlData);
		}else if("statusCode".equals(qName)){	
			
			statusCode = elementData;
		}else if(!xmlData.isEmpty()){
			xmlData.put(qName, elementData);
		}  
		  
		  
	  }
	  
	  // 文書の終了通知
	  public void endDocument() {
	    System.out.println("endDocument");
	  }

	  public List<HashMap> getXmlParceData() {
			return xmlParceData;
		}

		public void setXmlParceData(List<HashMap> xmlParceData) {
			this.xmlParceData = xmlParceData;
		}

		public HashMap getXmlData() {
			return xmlData;
		}

		public void setXmlData(HashMap xmlData) {
			this.xmlData = xmlData;
		}

		public String getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(String statusCode) {
			this.statusCode = statusCode;
		}
}
