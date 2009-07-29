<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="WebFlowClient.fsws.*" %>

<%
String space=" ";

String hostName=request.getParameter("hostName");
String userName=request.getParameter("userName");
String projectName=request.getParameter("projectName");
String baseWorkDir=request.getParameter("baseWorkDir");
String fileExtension=request.getParameter("fileExtension");
String fileServiceUrl=request.getParameter("fileServiceUrl");

String downloadFile=baseWorkDir
	+File.separator+userName
	+File.separator+projectName
	+File.separator+projectName+fileExtension;

String tmpFile="/tmp/nodetest"+(new Date()).toString();

//--------------------------------------------------
// Set up the file service
//--------------------------------------------------
FSClientStub fsclient=new FSClientStub();
fsclient.setBindingUrl(fileServiceUrl);  


response=fsclient.getDownloadStream(response,downloadFile,tmpFile);  
%>

