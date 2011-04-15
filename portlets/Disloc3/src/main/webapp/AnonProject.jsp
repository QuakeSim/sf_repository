<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"><%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%> 
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%> 

<html>	  
  <body>  
	 <f:view>	
		<h:form id="hitTheDamnButtonform">
		  <h:commandButton id="DislocAnonProjectInit"
								 value="Create Anonymous Project"
								 action="#{DislocBean2.NewProjectThenEditProject}"/>
		</h:form>
		</f:view>
  </body>
</html>