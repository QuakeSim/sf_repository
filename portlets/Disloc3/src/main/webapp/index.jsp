<%@ page session="true" %>
<html>
<body>
<%
    if (request.getParameter("logout")!=null)
    {
        session.removeAttribute("openid");
        session.removeAttribute("openid-claimed");
%>
    Logged out!<p>
<%
    }
	if (session.getAttribute("openid")==null) {
%>
<form method="POST" action="consumer_redirect.jsp">
<strong><br>QuakeSim2 OpenID login page:<br><br>
</strong>
<input type="hidden" name="openid" value="https://www.google.com/accounts/o8/id" size="60"/><br>
<input type="submit" value="log in into google"/>
</form>
<%
} else {
java.util.Enumeration en=session.getAttributeNames();
while(en.hasMoreElements()) {
  String attr=(String)en.nextElement();									 
  out.println(attr+"  "+session.getAttribute(attr)+"<br>");
}
%>

Logged in as <%= session.getAttribute("openid") %><p>
email address <%= session.getAttribute("email")%><p>
<a href="?logout=true">Log out</a>

<% } %>

</body>
</html>
