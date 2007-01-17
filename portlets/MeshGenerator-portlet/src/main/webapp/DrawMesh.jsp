<%@ page import="java.io.*"%>
<%@ page import="java.net.*"%>
<%@ page import="WebFlowClient.ViscoViz.*"%>
<%@ page import="javax.xml.rpc.holders.*"%>
<%@ page import="WebFlowClient.fsws.*"%>
<%@ page import="sun.misc.BASE64Decoder"%>

<jsp:useBean id="props" class="extensions.generic.PortalProperties"
	scope="session" />

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
		String projectName = getFromBASE64(request.getParameter("projectName"));
		String workDir = getFromBASE64(request.getParameter("workDir"));
		String mesh_gen_viz_base_dir = getFromBASE64(request
		.getParameter("mesh_gen_viz_base_dir"));
		String meshServerUrl = getFromBASE64(request.getParameter("meshServerUrl"));
		String danurl = getFromBASE64(request.getParameter("danurl"));
		String kamurl = getFromBASE64(request.getParameter("kamurl"));


		int tetraMode = 1;
		float zoom = 1.0f;

		FSClientStub fsClient = new FSClientStub();

		String fromFileName = workDir + projectName + ".node";
		String toFileName = mesh_gen_viz_base_dir + projectName
		+ ".node";
		fsClient.setBindingUrl(danurl);
		fsClient.crossload(fromFileName, kamurl, toFileName);

		fromFileName = workDir + projectName + ".tetra";
		toFileName = mesh_gen_viz_base_dir + projectName + ".tetra";
		fsClient.setBindingUrl(danurl);
		fsClient.crossload(fromFileName, kamurl, toFileName);

		MyVTKService service = new MyVTKServiceLocator();
		URL url = new URL(meshServerUrl);
		MyVTKServicePortType myVTK = service.getMyVTKService(url);

		OutputStream outstream = response.getOutputStream();
		ByteArrayHolder meshReturn = new ByteArrayHolder();
		IntHolder width = new IntHolder();
		IntHolder height = new IntHolder();

		myVTK.rotate(rx, ry, rz);

		myVTK.mesh(tetraMode, projectName, meshReturn, width, height);

		byte[] bytes = meshReturn.value;
		outstream.write(bytes);
		outstream.flush();

	} catch (Exception ex) {
	}
%>



