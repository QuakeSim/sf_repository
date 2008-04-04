<jsp:useBean id="SimplexBean" scope="session" class="cgl.quakesim.simplex.SimplexBean"/>
<%@page import="cgl.quakesim.simplex.Fault"%>
<%@page import="java.util.*"%>

<%
out.println(SimplexBean.toString());
//Fault[] fault=SimplexBean.getFaultsFromDB();
Enumeration en=session.getAttributeNames();
out.println("Here are attributes");
while(en.hasMoreElements()) {
   out.println(en.nextElement());
}
out.println(SimplexBean.getProjectName());
%>
Must have worked....