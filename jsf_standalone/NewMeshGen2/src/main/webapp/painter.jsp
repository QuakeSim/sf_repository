<%@ page contentType="application/x-java-jnlp-file"%>
<%@ page import="sun.misc.BASE64Decoder"%>
<%@ page import="java.io.*"%>
<%@ page import="java.net.*"%>
<%@ page import="WebFlowClient.fsws.*" %>


<%! 
public void downloadFile(String urlOfFile, String destFile) throws Exception {
		 java.net.URL inputFileUrl=new java.net.URL(urlOfFile);
 		  java.net.URLConnection uconn=inputFileUrl.openConnection();
		  java.io.InputStream in=inputFileUrl.openStream();
		  java.io.OutputStream out=new java.io.FileOutputStream(destFile);
		  
		  //Extract the name of the file from the url.
		  
		  byte[] buf=new byte[1024];
		  int length;
		  while((length=in.read(buf))>0) {
				out.write(buf,0,length);
		  }
		  in.close();
		  out.close();
}

public String getFromBASE64(String s) {
	if (s == null)
		return null;
	BASE64Decoder decoder = new BASE64Decoder();
	try {
		byte[] b = decoder.decodeBuffer(s);
		return new String(b);
	} catch (Exception e) {
		return null;
	}
}


%>


<?xml version="1.0" encoding="utf-8"?>
<%
String Localhostname=request.getLocalName();
String Localhostport=Integer.toString(request.getLocalPort());
String query_string=request.getQueryString();
String request_url=request.getRequestURI();
int tokenIndex=request_url.lastIndexOf("/");
String codebase=request_url.substring(0,tokenIndex);
codebase = "http://"+Localhostname+":"+Localhostport+codebase;

%>
<jnlp spec="1.0+" codebase="<%=codebase %>" href="painter.jsp?<%=query_string%>">
   <information>
      <title>mesh viewer</title>
      <vendor>CGL</vendor>
      <description>mesh viewer</description>
      <description kind="short">meshviewer_deploy</description>
      <offline-allowed/>
   </information>
   <security>
      <all-permissions/>
   </security>
   <resources>
      <j2se href="http://java.sun.com/products/autodl/j2se" version="1.4+"/>
      <jar href="meshviewer.jar" main="true"/>
      <extension name="jogl" href="http://download.java.net/media/jogl/builds/archive/jsr-231-webstart-current/jogl.jnlp"/>
    </resources>
   <application-desc main-class="painter" width="600" height="480">


<%
String mylayers=request.getParameter("layers");

//Here we must loop over the number of layers
if(mylayers!=null) {
		String tmp_mylayers= getFromBASE64(mylayers);
		System.out.println(tmp_mylayers);
    while(tmp_mylayers.lastIndexOf("*")>-1) {
    	int cur_index=tmp_mylayers.lastIndexOf("*");
        String layer= tmp_mylayers.substring( cur_index+1);
        tmp_mylayers=tmp_mylayers.substring(0,cur_index);
        
%>
<argument><%=layer%></argument>
<%        
  }
}
%>

<%
String myfaults=request.getParameter("faults");

//Here we must loop over the number of layers
if(myfaults!=null) {
		String tmp_myfaults= getFromBASE64(myfaults);
		System.out.println(tmp_myfaults);
    while(tmp_myfaults.lastIndexOf("*")>-1) {
    	int cur_index=tmp_myfaults.lastIndexOf("*");
        String fault= tmp_myfaults.substring( cur_index+1);
        tmp_myfaults=tmp_myfaults.substring(0,cur_index);
        
%>
<argument><%=fault%></argument>
<%        
  }
}
%>
	

<%
//--------------------------------------------------
//Set up the file service
//--------------------------------------------------
String plotMesh=request.getParameter("plotMesh");
if ( (plotMesh!=null) && (plotMesh.equals("true")) ) {
	

String baseUrl=getFromBASE64(request.getParameter("gfHostName"));
String userName=request.getParameter("userName");
//This will be encoded for safe transit.		 
String projectName=getFromBASE64(request.getParameter("projectName"));
String jobId=request.getParameter("jobUIDStamp");

String slash="/";
String baseOutputUrl=baseUrl+slash
		 +userName+slash+projectName+slash+jobId+slash;

// Plot the mesh if requested.  If the request value is anything other than
// "true" then don't plot.
System.out.println("plotMesh value is "+plotMesh);
String localUrl=null;

String nodeUrl=baseOutputUrl+projectName+".node";		 
String tetraUrl=baseOutputUrl+projectName+".tetra";		 
System.out.println(nodeUrl);
System.out.println(tetraUrl);

try {
   String localDest=application.getRealPath("/meshdownloads");
   //localDest=localDest+"/meshdownloads";
   System.out.println("localDest is "+localDest);
   System.out.println("ProjectName is "+projectName);
	
	String localNodeFile=localDest+"/"+projectName+".node";
	String localTetraFile=localDest+"/"+projectName+".tetra";

	downloadFile(nodeUrl,localNodeFile);
	downloadFile(tetraUrl,localTetraFile);

   localUrl=codebase+slash+"meshdownloads"+slash+projectName;
}
catch(Exception ex) {
   ex.printStackTrace();
   System.err.println("Download file not available");
}

String space=" ";		 
if(plotMesh!=null && plotMesh.equals("true") && localUrl!=null) {
   	String meshCmd="mesh"+space;
	meshCmd+=localUrl+space;
	meshCmd+="1";
System.out.println("Mesh Command is "+meshCmd);
%>
      <argument><%=meshCmd%></argument>
<%
}
}
%>


   </application-desc>

</jnlp>

