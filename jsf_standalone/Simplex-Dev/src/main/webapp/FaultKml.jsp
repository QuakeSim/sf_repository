<jsp:useBean id="SimplexBean" scope="session" class="cgl.quakesim.simplex.SimplexBean"/>
<%@page import="cgl.quakesim.simplex.Fault"%>
<%@page import="java.util.*"%>
<%
//Get the request params.
String userName=request.getParameter("userName");
String projectName=request.getParameter("projectName");
String codenName=request.getParameter("codeName");


//This is a hackish way of getting the faults back to javascript client.
String basePath=session.getServletContext().getRealPath("");
String webapps="webapps";
basePath=basePath.substring(0,basePath.indexOf(webapps)+webapps.length()+1);
String relPath="/WebServices/WEB-INF/Descriptors/users/";

Fault[] faults=SimplexBean.getProjectFaultsFromDB(userName,
																  projectName,
																  codenName,	 
																  basePath,
																  relPath);
%>
