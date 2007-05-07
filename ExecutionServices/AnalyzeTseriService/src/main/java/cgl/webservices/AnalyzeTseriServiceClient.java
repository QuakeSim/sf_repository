package cgl.webservices;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.xml.namespace.QName;

public class AnalyzeTseriServiceClient {
	public static String sopacDataFileUrl = "http://geoapp02.ucsd.edu/xml/geodesy/reason/grws/resources/output/procCoords/4-94488-20070505080435.txt";
	public static String data = 
		"dhlg 2004-01-01T12:00:00 -2319099.2349 -4799846.5086 3490090.4483 0.0035 0.0050 0.0038\n"+
		"dhlg 2004-01-02T12:00:00 -2319099.2377 -4799846.5114 3490090.4537 0.0031 0.0042 0.0033\n"+
		"dhlg 2004-01-03T12:00:00 -2319099.2410 -4799846.5170 3490090.4584 0.0031 0.0044 0.0034\n"+
		"dhlg 2004-01-04T12:00:00 -2319099.2347 -4799846.5105 3490090.4528 0.0026 0.0035 0.0028\n"+
		"dhlg 2004-01-05T12:00:00 -2319099.2338 -4799846.5114 3490090.4530 0.0025 0.0035 0.0027\n"+
		"dhlg 2004-01-06T12:00:00 -2319099.2362 -4799846.5127 3490090.4518 0.0026 0.0035 0.0028\n"+
		"dhlg 2004-01-07T12:00:00 -2319099.2392 -4799846.5143 3490090.4511 0.0028 0.0038 0.0030\n"+
		"dhlg 2004-01-08T12:00:00 -2319099.2363 -4799846.5112 3490090.4508 0.0025 0.0035 0.0028\n"+
		"dhlg 2004-01-09T12:00:00 -2319099.2354 -4799846.5085 3490090.4487 0.0025 0.0034 0.0027\n"+
		"dhlg 2004-01-10T12:00:00 -2319099.2370 -4799846.5114 3490090.4506 0.0027 0.0036 0.0029\n"+
		"dhlg 2004-01-11T12:00:00 -2319099.2360 -4799846.5120 3490090.4492 0.0025 0.0034 0.0026\n"+
		"dhlg 2004-01-12T12:00:00 -2319099.2344 -4799846.5120 3490090.4493 0.0024 0.0033 0.0026\n"+
		"dhlg 2004-01-13T12:00:00 -2319099.2380 -4799846.5129 3490090.4470 0.0026 0.0035 0.0027\n"+
		"dhlg 2004-01-14T12:00:00 -2319099.2342 -4799846.5131 3490090.4474 0.0025 0.0036 0.0027\n"+
		"dhlg 2004-01-15T12:00:00 -2319099.2351 -4799846.5125 3490090.4476 0.0026 0.0037 0.0028\n"+
		"dhlg 2004-01-16T12:00:00 -2319099.2389 -4799846.5147 3490090.4454 0.0027 0.0037 0.0029\n"+
		"dhlg 2004-01-17T12:00:00 -2319099.2345 -4799846.5134 3490090.4502 0.0027 0.0038 0.0029\n"+
		"dhlg 2004-01-18T12:00:00 -2319099.2364 -4799846.5096 3490090.4476 0.0025 0.0035 0.0027\n"+
		"dhlg 2004-01-19T12:00:00 -2319099.2371 -4799846.5128 3490090.4539 0.0026 0.0037 0.0028\n"+
		"dhlg 2004-01-20T12:00:00 -2319099.2411 -4799846.5106 3490090.4507 0.0026 0.0037 0.0029\n"+
		"dhlg 2004-01-21T12:00:00 -2319099.2403 -4799846.5078 3490090.4489 0.0029 0.0041 0.0031\n"+
		"dhlg 2004-01-22T12:00:00 -2319099.2416 -4799846.5097 3490090.4514 0.0028 0.0040 0.0030\n"+
		"dhlg 2004-01-23T12:00:00 -2319099.2419 -4799846.5147 3490090.4497 0.0028 0.0039 0.0030\n"+
		"dhlg 2004-01-24T12:00:00 -2319099.2393 -4799846.5128 3490090.4499 0.0025 0.0035 0.0027\n"+
		"dhlg 2004-01-25T12:00:00 -2319099.2406 -4799846.5142 3490090.4498 0.0027 0.0038 0.0029\n"+
		"dhlg 2004-01-26T12:00:00 -2319099.2392 -4799846.5113 3490090.4470 0.0026 0.0036 0.0028\n"+
		"dhlg 2004-01-27T12:00:00 -2319099.2375 -4799846.5080 3490090.4454 0.0025 0.0035 0.0027\n"+
		"dhlg 2004-01-28T12:00:00 -2319099.2369 -4799846.5085 3490090.4454 0.0027 0.0038 0.0029\n"+
		"dhlg 2004-01-29T12:00:00 -2319099.2371 -4799846.5141 3490090.4503 0.0025 0.0035 0.0027\n"+
		"dhlg 2004-01-30T12:00:00 -2319099.2408 -4799846.5144 3490090.4516 0.0033 0.0049 0.0036\n"+
		"dhlg 2004-01-31T12:00:00 -2319099.2414 -4799846.5112 3490090.4493 0.0025 0.0036 0.0028\n"+
		"dhlg 2004-02-01T12:00:00 -2319099.2385 -4799846.5096 3490090.4491 0.0024 0.0035 0.0027\n"+
		"dhlg 2004-02-02T12:00:00 -2319099.2397 -4799846.5133 3490090.4526 0.0025 0.0035 0.0027\n"+
		"dhlg 2004-02-03T12:00:00 -2319099.2383 -4799846.5150 3490090.4514 0.0027 0.0039 0.0029\n"+
		"dhlg 2004-02-04T12:00:00 -2319099.2393 -4799846.5084 3490090.4484 0.0025 0.0036 0.0028\n"+
		"dhlg 2004-02-05T12:00:00 -2319099.2425 -4799846.5108 3490090.4510 0.0026 0.0039 0.0029\n"+
		"dhlg 2004-02-06T12:00:00 -2319099.2382 -4799846.5090 3490090.4484 0.0026 0.0038 0.0029\n"+
		"dhlg 2004-02-07T12:00:00 -2319099.2407 -4799846.5090 3490090.4500 0.0025 0.0036 0.0027\n"+
		"dhlg 2004-02-08T12:00:00 -2319099.2375 -4799846.5081 3490090.4497 0.0024 0.0035 0.0027\n"+
		"dhlg 2004-02-09T12:00:00 -2319099.2403 -4799846.5106 3490090.4511 0.0024 0.0035 0.0027\n"+
		"dhlg 2004-02-10T12:00:00 -2319099.2376 -4799846.5067 3490090.4482 0.0023 0.0034 0.0026\n"+
		"dhlg 2004-02-11T12:00:00 -2319099.2405 -4799846.5080 3490090.4485 0.0023 0.0033 0.0026\n"+
		"dhlg 2004-02-12T12:00:00 -2319099.2395 -4799846.5084 3490090.4498 0.0024 0.0036 0.0027\n"+
		"dhlg 2004-02-13T12:00:00 -2319099.2384 -4799846.5059 3490090.4475 0.0024 0.0035 0.0027\n"+
		"dhlg 2004-02-14T12:00:00 -2319099.2404 -4799846.5094 3490090.4519 0.0025 0.0037 0.0028\n"+
		"dhlg 2004-02-15T12:00:00 -2319099.2412 -4799846.5082 3490090.4494 0.0026 0.0036 0.0028\n"+
		"dhlg 2004-02-16T12:00:00 -2319099.2418 -4799846.5100 3490090.4509 0.0034 0.0052 0.0038\n"+
		"dhlg 2004-02-17T12:00:00 -2319099.2414 -4799846.5113 3490090.4490 0.0024 0.0037 0.0028\n"+
		"dhlg 2004-02-18T12:00:00 -2319099.2436 -4799846.5073 3490090.4474 0.0025 0.0037 0.0029\n"+
		"dhlg 2004-02-19T12:00:00 -2319099.2438 -4799846.5099 3490090.4491 0.0028 0.0043 0.0032\n"+
		"dhlg 2004-02-20T12:00:00 -2319099.2437 -4799846.5115 3490090.4523 0.0025 0.0037 0.0028\n"+
		"dhlg 2004-02-21T12:00:00 -2319099.2424 -4799846.5079 3490090.4463 0.0024 0.0036 0.0027\n"+
		"dhlg 2004-02-22T12:00:00 -2319099.2382 -4799846.5061 3490090.4453 0.0026 0.0038 0.0029\n"+
		"dhlg 2004-02-23T12:00:00 -2319099.2392 -4799846.5048 3490090.4443 0.0023 0.0034 0.0026\n"+
		"dhlg 2004-02-24T12:00:00 -2319099.2384 -4799846.4979 3490090.4413 0.0035 0.0054 0.0040\n"+
		"dhlg 2004-02-25T12:00:00 -2319099.2391 -4799846.5056 3490090.4446 0.0024 0.0035 0.0027\n"+
		"dhlg 2004-02-26T12:00:00 -2319099.2400 -4799846.5121 3490090.4494 0.0027 0.0040 0.0031\n"+
		"dhlg 2004-02-27T12:00:00 -2319099.2402 -4799846.5031 3490090.4428 0.0027 0.0040 0.0030\n"+
		"dhlg 2004-02-28T12:00:00 -2319099.2382 -4799846.5055 3490090.4466 0.0027 0.0038 0.0031\n"+
		"dhlg 2004-02-29T12:00:00 -2319099.2406 -4799846.5056 3490090.4442 0.0026 0.0037 0.0029\n"+
		"dhlg 2004-03-01T12:00:00 -2319099.2394 -4799846.5040 3490090.4443 0.0026 0.0037 0.0028\n"+
		"dhlg 2004-03-02T12:00:00 -2319099.2402 -4799846.5046 3490090.4459 0.0029 0.0042 0.0032\n"+
		"dhlg 2004-03-03T12:00:00 -2319099.2451 -4799846.5098 3490090.4487 0.0037 0.0056 0.0041\n"+
		"dhlg 2004-03-04T12:00:00 -2319099.2418 -4799846.5058 3490090.4429 0.0027 0.0038 0.0030\n"+
		"dhlg 2004-03-05T12:00:00 -2319099.2410 -4799846.5064 3490090.4435 0.0027 0.0038 0.0029\n"+
		"dhlg 2004-03-06T12:00:00 -2319099.2419 -4799846.5019 3490090.4428 0.0027 0.0037 0.0029\n"+
		"dhlg 2004-03-07T12:00:00 -2319099.2399 -4799846.5044 3490090.4478 0.0026 0.0036 0.0028\n"+
		"dhlg 2004-03-08T12:00:00 -2319099.2395 -4799846.5052 3490090.4452 0.0026 0.0036 0.0028\n"+
		"dhlg 2004-03-09T12:00:00 -2319099.2414 -4799846.5052 3490090.4484 0.0029 0.0039 0.0030\n"+
		"dhlg 2004-03-10T12:00:00 -2319099.2421 -4799846.5126 3490090.4495 0.0026 0.0037 0.0029\n"+
		"dhlg 2004-03-11T12:00:00 -2319099.2398 -4799846.5110 3490090.4496 0.0027 0.0039 0.0030\n"+
		"dhlg 2004-03-12T12:00:00 -2319099.2401 -4799846.5105 3490090.4509 0.0026 0.0036 0.0028\n"+
		"dhlg 2004-03-13T12:00:00 -2319099.2399 -4799846.5073 3490090.4465 0.0028 0.0040 0.0030\n"+
		"dhlg 2004-03-14T12:00:00 -2319099.2391 -4799846.5122 3490090.4507 0.0026 0.0037 0.0029\n"+
		"dhlg 2004-03-15T12:00:00 -2319099.2388 -4799846.5096 3490090.4482 0.0026 0.0037 0.0029\n"+
		"dhlg 2004-03-16T12:00:00 -2319099.2382 -4799846.5091 3490090.4498 0.0026 0.0037 0.0029\n"+
		"dhlg 2004-03-17T12:00:00 -2319099.2382 -4799846.5099 3490090.4505 0.0027 0.0040 0.0031\n"+
		"dhlg 2004-03-18T12:00:00 -2319099.2413 -4799846.5123 3490090.4525 0.0027 0.0040 0.0031\n"+
		"dhlg 2004-03-19T12:00:00 -2319099.2401 -4799846.5120 3490090.4484 0.0026 0.0039 0.0030\n"+
		"dhlg 2004-03-20T12:00:00 -2319099.2372 -4799846.5070 3490090.4463 0.0025 0.0038 0.0030\n"+
		"dhlg 2004-03-21T12:00:00 -2319099.2370 -4799846.5077 3490090.4469 0.0025 0.0038 0.0030\n"+
		"dhlg 2004-03-22T12:00:00 -2319099.2408 -4799846.5103 3490090.4485 0.0027 0.0040 0.0031\n"+
		"dhlg 2004-03-23T12:00:00 -2319099.2360 -4799846.5069 3490090.4444 0.0029 0.0043 0.0033\n"+
		"dhlg 2004-03-24T12:00:00 -2319099.2433 -4799846.5025 3490090.4442 0.0029 0.0042 0.0033\n"+
		"dhlg 2004-03-25T12:00:00 -2319099.2394 -4799846.5063 3490090.4450 0.0026 0.0039 0.0030\n"+
		"dhlg 2004-03-26T12:00:00 -2319099.2366 -4799846.5079 3490090.4457 0.0030 0.0044 0.0035\n"+
		"dhlg 2004-03-27T12:00:00 -2319099.2408 -4799846.5092 3490090.4478 0.0028 0.0040 0.0031\n"+
		"dhlg 2004-03-28T12:00:00 -2319099.2401 -4799846.5097 3490090.4480 0.0026 0.0038 0.0030\n"+
		"dhlg 2004-03-29T12:00:00 -2319099.2418 -4799846.5068 3490090.4455 0.0026 0.0035 0.0028\n"+
		"dhlg 2004-03-30T12:00:00 -2319099.2449 -4799846.5087 3490090.4473 0.0025 0.0035 0.0028\n"+
		"dhlg 2004-03-31T12:00:00 -2319099.2404 -4799846.5093 3490090.4444 0.0028 0.0039 0.0031\n"+
		"dhlg 2004-04-01T12:00:00 -2319099.2359 -4799846.5027 3490090.4409 0.0033 0.0048 0.0037\n"+
		"dhlg 2004-04-02T12:00:00 -2319099.2436 -4799846.5025 3490090.4460 0.0030 0.0043 0.0033\n"+
		"dhlg 2004-04-03T12:00:00 -2319099.2419 -4799846.5042 3490090.4427 0.0028 0.0039 0.0032\n"+
		"dhlg 2004-04-04T12:00:00 -2319099.2405 -4799846.5044 3490090.4407 0.0027 0.0039 0.0032\n";

	public static void execAnalyzeTseriTest(String[] args) {
		try {
			//String endpoint = "http://gf3.ucs.indiana.edu:8888/analyze-tseri-exec/services/AnalyzeTseriExec";
			
			if (args.length < 1) {
				System.out.println("Usage : AnalyzeTseriServiceClient endpoint_url");
				return;
			}
			
			String endpoint = args[0];
			String siteCode = "dhlg";
			double[][] globalParam = { 
					{ 7, 0.0, 50.0, 2004.0, 2008.0 },
					{ 9, 0.0, 50.0, 2004.0, 2008.0 } };
			double[][] siteParam = { 
					{ 8, 0.0, 50.0, 2004.0, 2008.0 } };

			Service service = new Service();
			Call call = (Call) service.createCall();

			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName("http://soapinterop.org/",
					"execAnalyzeTseri"));
			
			int resOption = 1; 
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
			double timeIntervalBeginTime = 2004.0; 
			double timeIntervalEndTime = 2005.0;

			String[] ret = (String[]) call.invoke(new Object[] { siteCode,
					new Integer(resOption), new Integer(termOption), 
					new Double(cutoffCriterion), new Double(estJumpSpan),
					new Double(weakObsCriteriaEast), new Double(weakObsCriteriaNorth), new Double(weakObsCriteriaUp),
					new Double(outlierCriteriaEast), new Double(outlierCriteriaNorth), new Double(outlierCriteriaUp),
					new Double(badObsCriteriaEast), new Double(badObsCriteriaNorth), new Double(badObsCriteriaUp),
					new Double(timeIntervalBeginTime), new Double(timeIntervalEndTime),
					sopacDataFileUrl, globalParam, siteParam });
			
			System.out.println("Output: ");
			for (int i = 0; i < ret.length; i++) {
				System.out.println(ret[i]);
			}

		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	
	public static void execAnalyzeTseriBeanTest(String[] args) {
		try {
			//String endpoint = "http://gf3.ucs.indiana.edu:8888/analyze-tseri-exec/services/AnalyzeTseriExec";
			
			if (args.length < 1) {
				System.out.println("Usage : AnalyzeTseriServiceClient endpoint_url");
				return;
			}
			
			String endpoint = args[0];
			String siteCode = "dhlg";
			double[][] globalParam = { 
					{ 7, 0.0, 50.0, 2004.0, 2008.0 },
					{ 9, 0.0, 50.0, 2004.0, 2008.0 } };
			double[][] siteParam = { 
					{ 8, 0.0, 50.0, 2004.0, 2008.0 } };

			Service service = new Service();
			Call call = (Call) service.createCall();
			QName qn  = new QName ("urn:BeanService", "AnalyzeTseriBean");
	        call.registerTypeMapping(AnalyzeTseriBean.class, qn,
                    new org.apache.axis.encoding.ser.BeanSerializerFactory(AnalyzeTseriBean.class, qn),
                    new org.apache.axis.encoding.ser.BeanDeserializerFactory(AnalyzeTseriBean.class, qn));			

			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName("execAnalyzeTseri", "execAnalyzeTseri"));
			
			int resOption = 1; 
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
			double timeIntervalBeginTime = 2004.0; 
			double timeIntervalEndTime = 2005.0;

			AnalyzeTseriBean bean = new AnalyzeTseriBean();
			bean.setSiteCode(siteCode);
			bean.setResOption(resOption);
			bean.setTermOption(termOption);
			bean.setCutoffCriterion(cutoffCriterion);
			bean.setEstJumpSpan(estJumpSpan);
			bean.setWeakObsCriteriaEast(weakObsCriteriaEast);
			bean.setWeakObsCriteriaNorth(weakObsCriteriaNorth);
			bean.setWeakObsCriteriaUp(weakObsCriteriaUp);
			bean.setOutlierCriteriaEast(outlierCriteriaEast);
			bean.setOutlierCriteriaNorth(outlierCriteriaNorth);
			bean.setOutlierCriteriaUp(outlierCriteriaUp);
			bean.setBadObsCriteriaEast(badObsCriteriaEast);
			bean.setBadObsCriteriaNorth(badObsCriteriaNorth);
			bean.setBadObsCriteriaUp(badObsCriteriaUp);
			bean.setTimeIntervalBeginTime(timeIntervalBeginTime);
			bean.setTimeIntervalEndTime(timeIntervalEndTime);
			bean.setGlobalParam(globalParam);
			bean.setSiteParam(siteParam);
			bean.setSopacDataFileUrl(sopacDataFileUrl);
			
			String[] ret = (String[]) call.invoke(new Object[] { bean });
			
			System.out.println("Output: ");
			for (int i = 0; i < ret.length; i++) {
				System.out.println(ret[i]);
			}

		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	
	public static void execAnalyzeTseriSimpleBeanTest(String[] args) {
		try {
			//String endpoint = "http://gf3.ucs.indiana.edu:8888/analyze-tseri-exec/services/AnalyzeTseriExec";
			
			if (args.length < 1) {
				System.out.println("Usage : AnalyzeTseriServiceClient endpoint_url");
				return;
			}
			String endpoint = args[0];
			System.out.println(endpoint);

			Service service = new Service();
			Call call = (Call) service.createCall();
			QName qn  = new QName ("urn:BeanService", "AnalyzeTseriSimpleBean");
	        call.registerTypeMapping(AnalyzeTseriBean.class, qn,
                    new org.apache.axis.encoding.ser.BeanSerializerFactory(AnalyzeTseriSimpleBean.class, qn),
                    new org.apache.axis.encoding.ser.BeanDeserializerFactory(AnalyzeTseriSimpleBean.class, qn));			

			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName("execAnalyzeTseriSimpleBean", "execAnalyzeTseriSimpleBean"));
			

			AnalyzeTseriSimpleBean bean = new AnalyzeTseriSimpleBean();
			bean.setId(10);
			
			String ret = (String) call.invoke(new Object[] { bean });
			
			System.out.println("Output: " + ret);

		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	public static void main(String[] args) {
		execAnalyzeTseriTest(args);
		//execAnalyzeTseriBeanTest(args);
		//execAnalyzeTseriSimpleBeanTest(args);
	}
}
