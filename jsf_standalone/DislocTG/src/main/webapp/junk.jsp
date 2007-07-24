<%@ page import="TestClient.Select.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.*"%>
<%@ page import="java.io.*"%>

<%
String getFaultParams = "SELECT F.FaultName, F.SegmentName, F.LatStart, F.LatEnd, F.LonStart, F.LonEnd FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId";

String getFaultSegmentList = "SELECT F.FaultName, F.SegmentName FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId";

String getFaultOnlyList = "SELECT F.FaultName FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId";

String getAuthorList = "SELECT R.Author1 FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId";
String getSegmentList = "SELECT F.SegmentName FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId";
String getLatStartList = "SELECT F.LatStart FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId";
String getLatEndList = "SELECT F.LatEnd FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId";
String getLonStartList = "SELECT F.LonStart FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId";
String getLonEndList = "SELECT F.LonEnd FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId";

String getNorthridge = "SELECT F.SegmentName FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and F.FaultName=\'San Andreas\'";

String dbUrl="http://gf2.ucs.indiana.edu:9090/axis/services/Select";

String DB_RESPONSE_HEADER = "results of the query:";
SelectService ss = new SelectServiceLocator();
Select select = ss.getSelect(new URL(dbUrl));

						  PrintWriter pw=new PrintWriter(new FileWriter("/tmp/junk.out"));

		 				String tmp_fault=select.select(getFaultOnlyList);
						tmp_fault = tmp_fault.substring(DB_RESPONSE_HEADER.length());

						StringTokenizer st1=new StringTokenizer(tmp_fault, "\n");
						st1.nextToken();
						st1.nextToken();
						ArrayList faultNames=new ArrayList();
						String tmpOld="";
						while(st1.hasMoreTokens()) {	
							 String tmp1 = st1.nextToken().trim();
							 if(!tmp1.equals(tmpOld)) {
							 	 faultNames.add(tmp1);
								 tmpOld=tmp1;
//							 out.println(tmp1+"<br>");						    
							 }
						}

						for(int i=0;i<faultNames.size();i++) {
						String faultName=(String)faultNames.get(i);
						String getSegmentForFault = "SELECT F.SegmentName FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and F.FaultName=\'"+faultName+"\'";

		 				String tmp_segment=select.select(getSegmentForFault);
						  tmp_segment = tmp_segment.substring(DB_RESPONSE_HEADER.length());
						  StringTokenizer st2=new StringTokenizer(tmp_segment, "\n");
						  st2.nextToken();
						  st2.nextToken();
						  ArrayList segmentNames=new ArrayList();
						  String tmpOld2="";
					  	  while(st2.hasMoreTokens()) {	
							 String tmp2 = st2.nextToken().trim();
							 if(!tmp2.equals(tmpOld2) && tmp2!=null && !tmp2.equals("null")) {
							 	 segmentNames.add(tmp2);
								 tmpOld2=tmp2;
//							 out.println(faultName+" "+tmp2+"<br>");						    
							 }
						  }

						  for(int j=0;j<segmentNames.size();j++) {
						    String segmentName=(String)segmentNames.get(j);
						    String getInfo="SELECT F.LatStart,F.LatEnd,F.LonStart,F.LonEnd FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and F.FaultName=\'"+faultName+"\' and F.SegmentName=\'"+segmentName+"\'";
							 String tmp_info=select.select(getInfo);
							 tmp_info=tmp_info.substring(DB_RESPONSE_HEADER.length());
							 StringTokenizer st3=new StringTokenizer(tmp_info,"\n");
							 st3.nextToken();
							 st3.nextToken();
						    String tmpOld3="";
					  	    while(st3.hasMoreTokens()) {	
							    String tmp3 = st3.nextToken().trim();
							    if(tmp3!=null && tmp3.indexOf("null")<0 && tmp3.indexOf("no data")<0) {
									StringTokenizer st4=new StringTokenizer(tmp3);
									String latStart=st4.nextToken();
									String latEnd=st4.nextToken();
									String lonStart=st4.nextToken();
									String lonEnd=st4.nextToken();
									String space=" ";
//									out.println(faultName+space+segmentName+latStart+space+latEnd+space+lonStart+space+lonEnd+"<br>");
									String escape="\"";
									pw.println("<fault "+"faultName="+escape+faultName+escape+space
									+"segmentName="+escape+segmentName+escape+space
									+"latStart="+escape+latStart+escape+space
									+"latEnd="+escape+latEnd+escape+space
									+"lonStart="+escape+lonStart+escape+space
									+"lonEnd="+escape+lonEnd+escape+space
									+"/>");
									
							 	}
							}

						  }

						}
						pw.close();
%>