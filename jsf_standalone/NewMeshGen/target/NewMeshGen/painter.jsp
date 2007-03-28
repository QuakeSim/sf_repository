<%@ page contentType="application/x-java-jnlp-file"%>
<%@ page import="sun.misc.BASE64Decoder"%>
<%@ page import="java.io.*"%>
<%@ page import="WebFlowClient.fsws.*" %>


<%! 

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
	

String workDir=request.getParameter("workDir");
String projectName=request.getParameter("projectName");
String fsURL=request.getParameter("fsURL");
if(workDir!=null) {
	workDir= getFromBASE64(workDir);
}
if(projectName!=null) {
	projectName= getFromBASE64(projectName);
}
if(fsURL!=null) {
	fsURL= getFromBASE64(fsURL);
}


String SEPARATOR="/";
FSClientStub fsclient=new FSClientStub();
fsclient.setBindingUrl(fsURL);    
String space=" ";
// Plot the mesh if requested.  If the request value is anything other than
// "true" then don't plot.
System.out.println("workdir is "+workDir);
System.out.println("plotMesh value is "+plotMesh);
String localUrl=null;
try {
   String localDest=application.getRealPath("/meshdownloads");
   //localDest=localDest+"/meshdownloads";
   System.out.println("localDest is "+localDest);
   String tetraFileName=projectName+".tetra";
   fsclient.downloadFile(workDir+File.separator+tetraFileName,
                         localDest+File.separator+tetraFileName);
   String nodeFileName=projectName+".node";
   fsclient.downloadFile(workDir+File.separator+nodeFileName,
                         localDest+File.separator+nodeFileName);

   localUrl=codebase+SEPARATOR+"meshdownloads"+SEPARATOR+projectName;
}
catch(Exception ex) {
   ex.printStackTrace();
   System.err.println("Download file not available");
}

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

