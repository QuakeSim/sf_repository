/*
 * Created on Dec 8, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author rjc
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package edu.ucsd.sopac.portlet;

import javax.portlet.*;
import java.io.*;

import edu.ucsd.sopac.reason.grws.client.GRWS_SubmitQuery;

public class CoordQueryPortlet extends GenericPortlet {

	public void processAction(ActionRequest request, ActionResponse response)
			throws UnavailableException, PortletSecurityException,
			PortletException, IOException {

		String title = "Coordinate Query Service Results";
		String siteCodes = request.getParameter("site_codes");
		String contextId = request.getParameter("context_id");
		String beginDate = request.getParameter("begin_date");
		String endDate = request.getParameter("end_date");
		String contextGroup = request.getParameter("context_group");
		String resource = request.getParameter("resource");
		String minLat = request.getParameter("min_lat");
		String maxLat = request.getParameter("max_lat");
		String minLon = request.getParameter("min_lon");
		String maxLon = request.getParameter("max_lon");
		String userBbox[] = request.getParameterValues("user_bbox");
		String minMaxLatLon = null;

		if (userBbox != null) {
			for (int i = 0; i < userBbox.length; i++) {
				if (userBbox[i] != null)
					minMaxLatLon = minLat + " " + minLon + " " + maxLat + " "
							+ maxLon;
			}
		}
		GRWS_SubmitQuery gsq = new GRWS_SubmitQuery();

		gsq.setFromServlet(siteCodes, beginDate, endDate, resource,
				contextGroup, contextId, minMaxLatLon);
		String returnedResource = null;
		returnedResource = gsq.getResource();
		if (returnedResource == null) {
			returnedResource = "no coordinates found for your input parameters";
		}
		response.setRenderParameter("returnedResource", returnedResource);

		//set the portlet mode back to view
		response.setPortletMode(PortletMode.VIEW);

	}

	public void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String returnedResource = request.getParameter("returnedResource");
		//get the action url
		PortletURL actionURL = response.createActionURL();

		out.println("<hr>");

		PortletContext context = getPortletContext();

		//rjc
		PortletRequestDispatcher reqDP = context
				.getRequestDispatcher("/jsp/coordQueryBody.jsp");
		reqDP.include(request, response);
		if (returnedResource.startsWith("ERROR")) {

			out.println("<H3>Coordinate Query Service Results</H1>");

			out
					.println("<DIV STYLE=\"overflow: auto;width: 500px; height: 100px; "
							+ "border-left:1px gray solid; border-bottom: 1px gray solid; "
							+ "border-right:1px gray solid; border-top: 1px gray solid; padding: 0px; margin: 0px\">");

			out.println("no coordinates returned for your query");

		} else {
			String[] list = returnedResource.split("\n");
			out.println("<H3>Coordinate Query Service Results</H1>");

			out
					.println("<DIV STYLE=\"overflow: auto;width: 500px; height: 100px; "
							+ "border-left:1px gray solid; border-bottom: 1px gray solid; "
							+ "border-right:1px gray solid; border-top: 1px gray solid; padding: 0px; margin: 0px\">");

			out.println("<TABLE cellspacing=0 cellpadding=0>");

			for (int i = 0; i < list.length; i++) {
				String[] list2 = list[i].split("\r");
				out.println("<tr>");
				out.println("<td>");
				out.println(list2[0]);
				out.println("</td>");
				out.println("</tr>");
			}
			out
					.println("<tr><td><img src=\"/gridsphere/images/pixel.png\" width=650 height=0></td></tr>");
			out.println("</TABLE>");
		}

		out.println("</hr>");
	}

	public void doEdit(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("This is the Edit mode of Coordinate Query Portlet.");
	}

	protected void doHelp(RenderRequest request, RenderResponse response)
			throws UnavailableException, PortletSecurityException,
			PortletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<table>");
		out.println("<tr>");
		out.println("<td>");
		out
				.println("The Coordinate Query Portlet provides Web Services (SOAP/WSDL) based coordinate query service.");
		out.println("</td>");
		out.println("</tr>");
		out.println("</form>");
		out.println("</table>");
	}

}