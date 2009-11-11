<%@ page session="true" %>
<%@ page import="javax.servlet.http.HttpServletResponse"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml2/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" type="text/css" href="ajaxtabs/ajaxtabs.css" />

<script type="text/javascript" src="ajaxtabs/ajaxtabs.js">
/***********************************************
* Ajax Tabs Content script v2.2- © Dynamic Drive DHTML code library (www.dynamicdrive.com)
* This notice MUST stay intact for legal use
* Visit Dynamic Drive at http://www.dynamicdrive.com/ for full source code
***********************************************/
</script>

<script type="text/javascript">


function TapAcess(index){
	

	<%
	 	System.out.println("session.getAttribute(\"email\") : " + session.getAttribute("email"));
	%>
	
	document.getElementById("TabDiv").innerHTML = document.getElementById("TabDiv"+index).innerHTML;
 }

</script>

</head>

<body>
<h2>QuakeSim2 Disloc Gadget</h1>
<ul id="countrytabs" class="shadetabs">
<li><a href="javascript:TapAcess('1')" rel="TabDiv">LoadProject</a></li>
<li><a href="javascript:TapAcess('2')" rel="TabDiv">FetchResults</a></li>
<li><a href="javascript:TapAcess('3')" rel="TabDiv">LocateProject</a></li>
</ul>

<div id="TabDiv1" name="TabDiv1" style="display:none">
<iframe scrolling="yes" width=100% height="700" frameborder="1" src="http://129.79.49.68:8080//Disloc2/LoadProject.faces?email=<%=session.getAttribute("email")%>"></iframe>
</div>

<div id="TabDiv2" name="TabDiv2" style="display:none">
<iframe scrolling="yes" width=100% height="700" frameborder="1" src="http://129.79.49.68:8080//Disloc2/FetchResults.faces?email=<%=session.getAttribute("email")%>"></iframe>
</div>


<div id="TabDiv3" name="TabDiv3" style="display:none">
<iframe scrolling="yes" width=100% height="700" frameborder="1" src="http://129.79.49.68:8080//Disloc2/LocateProject.faces?email=<%=session.getAttribute("email")%>"></iframe>
</div>


<div id="TabDiv" name="TabDiv" style="display:select">
<iframe scrolling="yes" width=100% height="700" frameborder="1" src="http://129.79.49.68:8080//Disloc2/LoadProject.faces?email=<%=session.getAttribute("email")%>"></iframe>
</div>


<hr/>








