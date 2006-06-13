/*******************************************************************************
 * CoordinateQuery.java - You may not remove or change this notice. Do not sell
 * this as your own work or remove this copyright notice.  
 * 
 * Credit given (and copyright statements maintained) where code used directly
 * or modified from other sources.
 * 
 * This software is Copyright ￿ 2006 The Regents of the University of
 * California. All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for educational, research and non-profit purposes, without fee,
 * and without a written agreement is hereby granted, provided that the above
 * copyright notice, this paragraph and the following three paragraphs appear in
 * all copies.
 * 
 * Permission to incorporate this software into commercial products may be
 * obtained by contacting Technology Transfer Office 9500 Gilman Drive, Mail
 * Code 0910 University of California La Jolla, CA 92093-0910 (858) 534-5815
 * invent@ucsd.edu
 * 
 * This software program and documentation are copyrighted by The Regents of the
 * University of California. The software program and documentation are supplied
 * "as is", without any accompanying services from The Regents. The Regents does
 * not warrant that the operation of the program will be uninterrupted or
 * error-free. The end-user understands that the program was developed for
 * research purposes and is advised not to rely exclusively on the program for
 * any reason.
 * 
 * IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING
 * LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,
 * EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE PROVIDED
 * HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO
 * OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR
 * MODIFICATIONS.
 *  
 ******************************************************************************/
// $Id: CoordinateQuery.java,v 1.7 2006/06/12 22:44:00 pjamason Exp $

/* Servlet that invokes GRWS_SubmitQuery web service client's "setFromServlet" and
 * "getResource" methods.  The latter method invokes the GRWS_Query web service to
 * obtain coordinates for the spatial and temporal filters given.
 * 
 * Author: Paul Jamason, SOPAC
 * Date: 06/2006
 */
package edu.ucsd.sopac.reason.grws.servlets;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import edu.ucsd.sopac.utils.servlet.ServletUtilities;
import edu.ucsd.sopac.reason.grws.GRWS_Config;
import edu.ucsd.sopac.reason.grws.client.GRWS_SubmitQuery;

public class CoordinateQuery extends HttpServlet implements GRWS_Config {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			PropertyConfigurator.configure(new URL(
					GRWS_Config.WEB_LOG4J_PROPS_FILE));
		} catch (MalformedURLException event) {
			System.err.println(event.toString());
		}
		Logger logger = Logger.getLogger(CoordinateQuery.class);

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
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
		logger.debug("CoordinateQuery");

		if (userBbox != null) {
			for (int i = 0; i < userBbox.length; i++) {
				if (userBbox[i] != null)
					logger.debug("CoordinateQuery: setting minMaxLatLon");
				minMaxLatLon = minLat + " " + minLon + " " + maxLat + " "
						+ maxLon;
			}
		}
		GRWS_SubmitQuery gsq = new GRWS_SubmitQuery();

		gsq.setFromServlet(siteCodes, beginDate, endDate, resource,
				contextGroup, contextId, minMaxLatLon);
		String returnedResource = null;
		returnedResource = gsq.getResource();
		if (returnedResource.startsWith("ERROR")) {
			out.println(ServletUtilities.headWithTitle(title) + "<BODY>\n"
					+ "<H1 ALIGN=CENTER>" + title + "</H1>\n"
					+ returnedResource + "</BODY></HTML>");
		} else {
			out.println(ServletUtilities.headWithTitle(title) + "<BODY>\n"
					+ "<H1 ALIGN=CENTER>" + title + "</H1>\n" + "<pre>"
					+ returnedResource + "</pre>" + "</BODY></HTML>");
		}
	}

	public static void main(String[] args) {
	}
}