<%@ page import="WebFlowClient.fsws.*" %>
<%@ page import="cgl.webclients.*"%>
<%@ page import="java.net.*"%>

<%
//--------------------------------------------------
// Get the CM
//--------------------------------------------------

//--------------------------------------------------
// Set up the file service
//--------------------------------------------------
FSClientStub fsclient=new FSClientStub();


//--------------------------------------------------
// Process the form information
//--------------------------------------------------
String selectedProject=request.getParameter("ProjectSelect");
String selectedFile=request.getParameter("MeshChoice");
String baseWorkDir=request.getParameter("baseWorkDir");
String codeName=request.getParameter("codeName");
String userName=request.getParameter("userName");
String fileServiceUrl=request.getParameter("fileServiceUrl");


if(selectedProject!=null && !selectedProject.equals("")
                         && selectedFile!=null && !selectedFile.equals("")) {
 
        //Set the download file name
        String downloadFile=baseWorkDir+"/"+userName+"/"+ selectedProject+"/"+selectedProject+selectedFile;
        String tmpFile="/tmp/nodetest";

        //Now set up the file service to download the file.
        fsclient.setBindingUrl(fileServiceUrl);    
        
	try {	
        response=fsclient.getDownloadStream(response,downloadFile,tmpFile);
	} 
	catch(Exception ex){
	ex.printStackTrace();
	out.println("Data not available");
	}

 } 


%>
