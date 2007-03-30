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
String userName=request.getParameter("userName");
String antUrl=request.getParameter("antUrl");
String binPath=request.getParameter("binPath");
String fileServiceUrl=request.getParameter("fileServiceUrl");


//If a tar is selected, make one.
if(selectedFile.equalsIgnoreCase(".tar.gz")) {

	AntVisco ant=new AntViscoServiceLocator().getAntVisco(new URL(antUrl));

        String workDir=baseWorkDir+"/"+userName+"/"+selectedProject;

	String bf_loc=binPath+"/"+"build.xml";

	String[] args0=new String[5];
       	args0[0]="-DprojectName.prop="+selectedProject;
	args0[1]="-Dworkdir.prop="+workDir;
        args0[2]="-buildfile";
        args0[3]=bf_loc;
        args0[4]="meshgen.tar.all";

        ant.setArgs(args0);
   	ant.run();
}

if(selectedProject!=null && !selectedProject.equals("")
                         && selectedFile!=null && !selectedFile.equals("")) {
 
        //Set the download file name
        String downloadFile=baseWorkDir+"/"+userName+"/"+selectedProject+"/"+selectedProject+selectedFile;
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
