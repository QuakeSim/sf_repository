<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"><%@ page session="true" %>
<%@ page import="java.util.Map,java.util.Iterator,org.openid4java.discovery.Identifier, org.openid4java.discovery.*, org.openid4java.message.ax.FetchRequest, org.openid4java.message.ax.FetchResponse, org.openid4java.message.ax.AxMessage,  org.openid4java.message.*, org.openid4java.OpenIDException, java.util.List, java.io.IOException, javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.openid4java.consumer.ConsumerManager, org.openid4java.consumer.InMemoryConsumerAssociationStore, org.openid4java.consumer.InMemoryNonceVerifier" %>

<jsp:scriptlet>
  // README:
  // Set the returnToUrl string to the appropriate value for this JSP
  // Since you may be deployed behind apache, etc, the jsp has no real idea what the
  // absolute URI is to get back here.
  
  Object o = pageContext.getAttribute("consumermanager", PageContext.APPLICATION_SCOPE);
  if (o == null) {
  ConsumerManager newmgr=new ConsumerManager();
  newmgr.setAssociations(new InMemoryConsumerAssociationStore());
  newmgr.setNonceVerifier(new InMemoryNonceVerifier(5000));
  pageContext.setAttribute("consumermanager", newmgr, PageContext.APPLICATION_SCOPE);
  System.out.println("consumermanager is created");
  }
  
  
  ConsumerManager manager=(ConsumerManager) pageContext.getAttribute("consumermanager", PageContext.APPLICATION_SCOPE);	
  String openid=request.getParameter("openid");
  
  try {
  // determine a return_to URL where your application will receive
  // the authentication responses from the OpenID provider
  // YOU SHOULD CHANGE THIS TO GO TO THE
  String returnToUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/@artifactId@/consumer_returnurl.jsp";
  
  
  // perform discovery on the user-supplied identifier
  Discovery d = new Discovery();
  Identifier identifier = d.parseIdentifier(openid);
  openid = identifier.getIdentifier();
  
  List discoveries = manager.discover(openid);
  
  // attempt to associate with an OpenID provider
  // and retrieve one service endpoint for authentication
  DiscoveryInformation discovered = manager.associate(discoveries);
  
  // store the discovery information in the user's session
  session.setAttribute("openid-disco", discovered);
  
  // obtain a AuthRequest message to be sent to the OpenID provider
  AuthRequest authReq = manager.authenticate(discovered, returnToUrl);
  
  // Attribute Exchange example: fetching the 'email' attribute
  FetchRequest fetch = FetchRequest.createFetchRequest();
  fetch.addAttribute("email",
  // attribute alias
  "http://axschema.org/contact/email",   // type URI
  true);                                      // required
  
  // attach the extension to the authentication request
  authReq.addExtension(fetch);
  
  if (discovered.isVersion2()) {
  // Option 1: GET HTTP-redirect to the OpenID Provider endpoint
  // The only method supported in OpenID 1.x
  // redirect-URL usually limited ~2048 bytes
  System.out.println("!discovered.isVersion2()");
  response.sendRedirect(authReq.getDestinationUrl(true));
  
  }
  else
  {
  // Option 2: HTML FORM Redirection
  // Allows payloads > 2048 bytes
  
  // 
  // see samples/formredirection.jsp for a JSP example
  //authReq.getOPEndpoint();
  
  // build a HTML FORM with the message parameters
  //authReq.getParameterMap();

</jsp:scriptlet>

<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
	 <title>OpenID HTML FORM Redirection</title>
	 
	 <script type="text/javascript">
		
		function clicksubmit(){
		
		<jsp:scriptlet>
		System.out.println("submitted");
		</jsp:scriptlet>

		document.forms['openid-form-redirection'].submit();
		}
	 </script>
  </head>

  <body onload="clicksubmit();">
	 <!-- <body> -->
	 
	 <jsp:scriptlet>
		System.out.println("authReq.getOPEndpoint() : " + authReq.getOPEndpoint());
	 </jsp:scriptlet>
	 
	 <form name="openid-form-redirection" 
			 action="<%=authReq.getOPEndpoint()%>" method="post" accept-charset="utf-8">
	
	 <jsp:scriptlet>
		Map pm=authReq.getParameterMap();
		Iterator keyit=pm.keySet().iterator();
		
		Object key;
		Object value;
		
		while (keyit.hasNext()) {
		key=keyit.next();
		value=pm.get(key);
		
		System.out.println("key : " + key);
		System.out.println("value : " + value);
		System.out.println("");
		
	 </jsp:scriptlet>
	 
	<input type="hidden" name="<%= key%>" value="<%= value%>"/>	
	<jsp:scriptlet>
		}		
	</jsp:scriptlet>
	<button type="submit" style="display:none">Continue...</button>
	
 </form>
</body>
</html>
<jsp:scriptlet>
  }
  }
  catch (OpenIDException e) {
  // present error to the user			
  
  
  response.sendError(500);
  }
</jsp:scriptlet>

