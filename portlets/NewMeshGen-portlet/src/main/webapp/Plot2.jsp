<%@ page import="java.io.*"%>
<%@ page import="java.net.*"%>
<%@ page import="WebFlowClient.ViscoViz.*"%>
<%@ page import="javax.xml.rpc.holders.*"%>
<%@ page import="sun.misc.BASE64Decoder"%>

<%!public String getFromBASE64(String s) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);
		} catch (Exception e) {
			return null;
		}
	}%>

<%
		try {
		float rx = Float.parseFloat(request.getParameter("rx"));
		float ry = Float.parseFloat(request.getParameter("ry"));
		float rz = Float.parseFloat(request.getParameter("rz"));
		float zoom = Float.parseFloat(request.getParameter("zoom"));
		String meshServerUrl = getFromBASE64(request.getParameter("meshServerUrl"));

		MyVTKService service = new MyVTKServiceLocator();
		MyVTKServicePortType myVTK = service.getMyVTKService(new URL(
		meshServerUrl));

		OutputStream outstream = response.getOutputStream();
		ByteArrayHolder plotReturn = new ByteArrayHolder();
		IntHolder width = new IntHolder();
		IntHolder height = new IntHolder();

		if (rx == -80.f && ry == 10.f) {
			myVTK.rotate(-90.f, 0.f, 0.f);
			myVTK.plot(plotReturn, width, height);
			myVTK.rotate(0.f, 10.f, 0.f);
			myVTK.plot(plotReturn, width, height);
			myVTK.rotate(10.f, 0.f, 0.f);
			//myVTK.plot(plotReturn,width,height);
		} else {
			myVTK.rotate(rx, ry, rz);
		}
		myVTK.zoomIn(zoom, zoom, zoom);
		myVTK.plot(plotReturn, width, height);
		byte[] bytes = plotReturn.value;
		outstream.write(bytes);
		outstream.flush();
	} catch (Exception ex) {
	}
%>
