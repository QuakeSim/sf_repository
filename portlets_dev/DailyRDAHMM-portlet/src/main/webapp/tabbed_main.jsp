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


function calcHeight()
{
	var the_height = document.getElementById("TabDiv").contentWindow.document.body.scrollHeight;
	
	document.getElementById("TabDiv").height=the_height;
}


function TapAcess(index){
	
	<%
	 	System.out.println("session.getAttribute(\"email\") : " + session.getAttribute("email"));
	%>
	
	document.getElementById("TabDiv").innerHTML = document.getElementById("TabDiv"+index).innerHTML;
 }

</script>

</head>

<body>
<h2>QuakeSim2 DailyRdahmm Gadget</h1>
<ul id="countrytabs" class="shadetabs">
<li><a href="javascript:TapAcess('1')" rel="TabDiv">DailyRdahmm_sopac</a></li>
</ul>

<div id="TabDiv1" name="TabDiv1" style="display:none">
<iframe scrolling="yes" width=100% height="700" onLoad="calcHeight();" frameborder="1" src="http://156.56.104.158:8080//DailyRDAHMM-portlet/DailyRdahmm_sopac.faces?email=<%=session.getAttribute("email")%>"></iframe>
</div>


<div id="TabDiv" name="TabDiv" style="display:select">
<iframe scrolling="yes" width=100% height="700" onLoad="calcHeight();" frameborder="1" src="http://156.56.104.158:8080//DailyRDAHMM-portlet/DailyRdahmm_sopac.faces?email=<%=session.getAttribute("email")%>"></iframe>
</div>

<script type="text/javascript">

var countries=new ddajaxtabs("countrytabs", "TabDiv")
countries.setpersist(true)
countries.setselectedClassTarget("link") //"link" or "linkparent"
countries.init()

</script>

<hr/>










