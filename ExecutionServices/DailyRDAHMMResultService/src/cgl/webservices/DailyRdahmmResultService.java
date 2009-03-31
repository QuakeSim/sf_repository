package cgl.webservices;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class DailyRdahmmResultService {
	
	protected HashMap<String, DailyRdahmmResultAnalyzer> analyzerTable;
	
	public DailyRdahmmResultService() {
		analyzerTable = new HashMap<String, DailyRdahmmResultAnalyzer>();
	}
	
	public String calcStationColors(String date, String resUrl) {
		DailyRdahmmResultAnalyzer analyzer = analyzerTable.get(resUrl);
		if (analyzer == null) {
			analyzer = addAnalyzer(resUrl);
		}
		
		return analyzer.calcStationColors(date);
	}
	
	public String getLatLongForStation(String stationId, String resUrl) {
		DailyRdahmmResultAnalyzer analyzer = analyzerTable.get(resUrl);
		if (analyzer == null) {
			analyzer = addAnalyzer(resUrl);
		}
		
		return analyzer.getLatLongForStation(stationId);
	}
	
	public String getDataLatestDate(String resUrl) {
		DailyRdahmmResultAnalyzer analyzer = analyzerTable.get(resUrl);
		if (analyzer == null) {
			analyzer = addAnalyzer(resUrl);
		}
		
		return analyzer.getDataLatestDate();
	}
	
	// add an analyzer to the table of analyzers
	protected DailyRdahmmResultAnalyzer addAnalyzer(String xmlResUrl) {
		DailyRdahmmResultAnalyzer analyzer = new DailyRdahmmResultAnalyzer(xmlResUrl);
		analyzerTable.put(xmlResUrl, analyzer);
		return analyzer;
	}
	
}
