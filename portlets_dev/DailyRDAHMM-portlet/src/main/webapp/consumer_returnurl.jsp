<%@ page session="true" %>
<%@ page import="org.openid4java.discovery.Identifier, org.openid4java.discovery.DiscoveryInformation, org.openid4java.message.ax.FetchRequest, org.openid4java.message.ax.FetchResponse, org.openid4java.message.ax.AxMessage, org.openid4java.message.*, org.openid4java.OpenIDException, java.util.List, java.io.IOException, javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.openid4java.consumer.ConsumerManager, org.openid4java.consumer.InMemoryConsumerAssociationStore, org.openid4java.consumer.VerificationResult" %>

<%
    ConsumerManager manager=(ConsumerManager) pageContext.getAttribute("consumermanager", PageContext.APPLICATION_SCOPE);
	
try
        {
            // --- processing the authentication response

            // extract the parameters from the authentication response
            // (which comes in as a HTTP request from the OpenID provider)
            ParameterList responselist =
                    new ParameterList(request.getParameterMap());

            // retrieve the previously stored discovery information
            DiscoveryInformation discovered =
                    (DiscoveryInformation) session.getAttribute("openid-disco");

	
            // extract the receiving URL from the HTTP request
            StringBuffer receivingURL = request.getRequestURL();
            String queryString = request.getQueryString();
            if (queryString != null && queryString.length() > 0)
                receivingURL.append("?").append(request.getQueryString());

            // verify the response; ConsumerManager needs to be the same
            // (static) instance used to place the authentication request
            VerificationResult verification = manager.verify(
                    receivingURL.toString(),
                    responselist, discovered);

            // examine the verification result and extract the verified identifier
            Identifier verified = verification.getVerifiedId();
            if (verified != null)
            {
                AuthSuccess authSuccess =
                        (AuthSuccess) verification.getAuthResponse();

                session.setAttribute("openid", authSuccess.getIdentity());
                session.setAttribute("openid-claimed", authSuccess.getClaimed());

	    if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX))
	    {
		MessageExtension ext = authSuccess.getExtension(AxMessage.OPENID_NS_AX);

		if (ext instanceof FetchResponse)

	        {
		    FetchResponse fetchResp = (FetchResponse) ext;
        
    	            // can have multiple values
        	    List emails = fetchResp.getAttributeValues("email");

		    System.out.println("email address : " + emails.get(0));
			session.setAttribute("email", emails.get(0));
	        } 
	    }
		// response.sendRedirect("http://localhost:8080/Simplex-portlet/LoadProject.faces");  // success
		RequestDispatcher dispatcher = request.getRequestDispatcher("/tabbed_main.faces");
		dispatcher.forward(request, response);

            }
            else
            {
%>
            Failed to login!
<%
            }
        }
        catch (OpenIDException e)
        {
%>
            Login error!
<%
            // present error to the user
        }

%>
