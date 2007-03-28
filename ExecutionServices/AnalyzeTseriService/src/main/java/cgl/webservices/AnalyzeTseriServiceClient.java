package cgl.webservices;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.xml.namespace.QName;

public class AnalyzeTseriServiceClient {
	public static void main(String[] args) {
		try {
			String endpoint = "http://gf3.ucs.indiana.edu:8888/analyze-tseri-exec/services/AnalyzeTseriExec";

			String siteCode = "dhlg";
			String dataUrl = "http://gf3.ucs.indiana.edu:8888/inputTest.xyz";
			double[][] globalParam = { 
					{ 7, 0.0, 50.0, 2005.0, 2008.0 },
					{ 9, 0.0, 50.0, 2005.0, 2008.0 } };
			double[][] siteParam = { 
					{ 8, 0.0, 50.0, 2005.0, 2008.0 } };

			Service service = new Service();
			Call call = (Call) service.createCall();

			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName("http://soapinterop.org/",
					"execATS"));
			
			int resOption = 387; 
			int termOption = 556; 
			double cutoffCriterion = 1.0; 
			double estJumpSpan = 1.0;
			double weakObsCriteriaEast = 30.0; 
			double weakObsCriteriaNorth = 30.0; 
			double weakObsCriteriaUp = 50.0;
			double outlierCriteriaEast = 800.0; 
			double outlierCriteriaNorth = 800.0; 
			double outlierCriteriaUp = 800.0;
			double badObsCriteriaEast = 10000.0; 
			double badObsCriteriaNorth = 10000.0; 
			double badObsCriteriaUp = 10000.0;
			double timeIntervalBeginTime = 2004.8; 
			double timeIntervalEndTime = 2006.8;

			String[] ret = (String[]) call.invoke(new Object[] { siteCode,
					new Integer(resOption), new Integer(termOption), 
					new Double(cutoffCriterion), new Double(estJumpSpan),
					new Double(weakObsCriteriaEast), new Double(weakObsCriteriaNorth), new Double(weakObsCriteriaUp),
					new Double(outlierCriteriaEast), new Double(outlierCriteriaNorth), new Double(outlierCriteriaUp),
					new Double(badObsCriteriaEast), new Double(badObsCriteriaNorth), new Double(badObsCriteriaUp),
					new Double(timeIntervalBeginTime), new Double(timeIntervalEndTime),
					dataUrl, globalParam, siteParam });
			
			System.out.println("Output: ");
			for (int i = 0; i < ret.length; i++) {
				System.out.println(ret[i]);
			}

			//String ret = (String[]) call.invoke(new Object[] { });

			//System.out.println("Output: " + ret);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
}
