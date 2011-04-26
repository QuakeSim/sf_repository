<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"><%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%> 
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%> 
<html>
<head>
  <!-- Replace this with your google map key -->
	 <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=put.google.map.key.here" 
				type="text/javascript"></script>
</head>
<body>    
  <f:view>	
	 <h:form id="hitTheButtonform">
		<f:verbatim>
		  QuakeSim's elastic forward modeling code calculates surface deformations based on
		  geometric fault models: see image below for parameter definitions. 
		  <p/>
		</f:verbatim>
		<f:verbatim>
		  <b>Click here it to run a simulation:</b>
		</f:verbatim>
		<h:commandButton id="DislocAnonProjectInit"
							  value="Start"
							  action="#{DislocBean2.NewProjectThenEditProject}"/>
		<f:verbatim><p/></f:verbatim>
		<h:graphicImage value="images/FaultGeometry.jpg" 
							 style="border:thin solid black"
							 width="800px"/>
		<f:verbatim>
		  <p/>To create persistent projects, you must create an account in the 
		  <a href="http://portal.quakesim.org/">QuakeSim Portal</a>.  Account creation is 
		  automated and requires only a response to an automated confirmation email.
		</f:verbatim>
	 </h:form>	 
  </f:view>
</body>
</html>
