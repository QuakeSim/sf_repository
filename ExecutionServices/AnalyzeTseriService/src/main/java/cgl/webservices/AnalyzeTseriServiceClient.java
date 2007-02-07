package cgl.webservices;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.xml.namespace.QName;

public class AnalyzeTseriServiceClient {
	public static void main(String[] args) {
		try {
			String endpoint = "http://gf1.ucs.indiana.edu:8888/analyze-tseri-exec/services/AnalyzeTseriExec";

			String siteCode = "dhlg";
			String dataUrl = "http://gf1.ucs.indiana.edu:8888/inputTest.xyz";
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
			
			String[] ret = (String[]) call.invoke(new Object[] {siteCode, dataUrl, globalParam, siteParam});
			
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
