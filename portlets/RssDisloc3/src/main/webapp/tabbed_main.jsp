<%@ page session="true" %>
<%@ page import="javax.servlet.http.HttpServletResponse"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml2/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<script type="text/javascript" src="ajaxtabs/dropdowntabs.js">

/***********************************************
* Drop Down Tabs Menu- (c) Dynamic Drive DHTML code library (www.dynamicdrive.com)
* This notice MUST stay intact for legal use
* Visit Dynamic Drive at http://www.dynamicdrive.com/ for full source code
***********************************************/

</script>

<!-- CSS for Drop Down Tabs Menu #1 -->
<link rel="stylesheet" type="text/css" href="ajaxtabs/ddcolortabs.css" />

<script type="text/javascript">


function TapAccess(index){

      var s = '<iframe scrolling="yes" width=100% height="700" frameborder="1" src="@host.base.url@@artifactId@/' + index + '.faces?email=<%=session.getAttribute("email")%>"></iframe>'
      document.getElementById("TabDiv").innerHTML = s;
 }

</script>

</head>

<body>
<h2>QuakeSim2 RssDisloc3 Gadget</h2>
<div id ="maintab" class="slidetabsmenu">
<ul>
<li><a href="javascript:TapAccess('ResultsMap')" title= "ResultMap"><span>ResultsMap</span></a></li>
<li><a href="javascript:TapAccess('LoadProject')" title="Advanced" rel="Tab2"><span>Advanced</span></a></li>
</ul>
</div>


<div id="Tab2" class="dropmenudiv_c" style="width: 150px;">
<a href="javascript:TapAccess('LoadProject')">LoadProject</a>
<a href="javascript:TapAccess('FetchResults')">FetchResults</a>
<a href="javascript:TapAccess('InSarPlots')">InSarPlots</a>
</div>


<div id="TabDiv" name="TabDiv" style="display:select">
<iframe scrolling="yes" width=100% height="700" frameborder="1" src="@host.base.url@@artifactId@/LoadProject.faces?email=<%=session.getAttribute("email")%>"></iframe>
</div>

<script type="text/javascript">
//SYNTAX: tabdropdown.init("menu_id", [integer OR "auto"])
tabdropdown.init("maintab","auto")
</script>