package cgl.quakesim.geofest;


import javax.faces.event.ActionEvent;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class MeshViewer {

	float zoomFactor = 1.2f;

	float rotateXFactor = 10.f;

	float rotateYFactor = 10.f;

	float rotateZFactor = 10.f;

	Float rx = new Float(0.0f);

	Float ry = new Float(0.0f);

	Float rz = new Float(0.0f);

	Float zoom = new Float(1.0f);

	String meshServerUrl = new String("http://gf2.ucs.indiana.edu:18084");
	String projectName="";
	String workDir = "";
	String mesh_gen_viz_base_dir = "";
	String fileServiceUrl = "";
	String mesh_gen_viz_fileServiceUrl = "" ;
	
	

	String imageJspUrl = new String("");
	String meshImageJspUrl= new String("");

	public MeshViewer() {
		rx = new Float(0.0f);
		ry = new Float(0.0f);
		rz = new Float(0.0f);
		zoom = new Float(1.0f);
	}

	public MeshViewer(String tmp_str) {
		rx = new Float(0.0f);
		ry = new Float(0.0f);
		rz = new Float(0.0f);
		zoom = new Float(1.0f);
		meshServerUrl = tmp_str;
	}

	public void reset() {
		rx = new Float(0.0f);
		ry = new Float(0.0f);
		rz = new Float(0.0f);
		zoom = new Float(1.0f);

	}

	public String getMeshImageJspUrl() {
		String tmp_str=getBASE64(meshServerUrl);
		this.meshImageJspUrl="DrawMesh.jsp?rx="+rx.toString()+"&ry="+ry.toString()+"&rz="+rz.toString()+"&zoom="+zoom.toString()+"&meshServerUrl="+tmp_str;
		tmp_str=getBASE64(projectName);
		this.meshImageJspUrl=this.meshImageJspUrl+"&projectName="+tmp_str;
		tmp_str=getBASE64(workDir);
		this.meshImageJspUrl=this.meshImageJspUrl+"&workDir="+tmp_str;
		tmp_str=getBASE64(fileServiceUrl);
		this.meshImageJspUrl=this.meshImageJspUrl+"&danurl="+tmp_str;
		tmp_str=getBASE64(mesh_gen_viz_base_dir);
		this.meshImageJspUrl=this.meshImageJspUrl+"&mesh_gen_viz_base_dir="+tmp_str;
		tmp_str=getBASE64(mesh_gen_viz_fileServiceUrl);
		this.meshImageJspUrl=this.meshImageJspUrl+"&kamurl="+tmp_str;
		

		return this.meshImageJspUrl;

	}

	public void setMeshImageJspUrl(String tmp_str) {
		this.meshImageJspUrl = tmp_str;

	}	
	public void toggleUpRotateXFactor(ActionEvent ev) {
		rx = new Float(+rotateXFactor);
	}

	public void toggleDownRotateXFactor(ActionEvent ev) {
		rx = new Float(-rotateXFactor);
	}

	public void toggleUpRotateYFactor(ActionEvent ev) {
		ry = new Float(+rotateYFactor);
	}

	public void toggleDownRotateYFactor(ActionEvent ev) {
		ry = new Float(-rotateYFactor);
	}

	public void toggleUpRotateZFactor(ActionEvent ev) {
		rz = new Float(+rotateZFactor);
	}

	public void toggleDownRotateZFactor(ActionEvent ev) {
		rz = new Float(-rotateZFactor);
	}

	public void toggleUpZoomLevel(ActionEvent ev) {
		zoom = new Float(zoomFactor);
	}

	public void toggleDownZoomLevel(ActionEvent ev) {
		zoom = new Float(1.0f / zoomFactor);
	}

	public String getImageJspUrl() {
		String tmp_str=getBASE64(meshServerUrl);
		this.imageJspUrl="Plot2.jsp?rx="+rx.toString()+"&ry="+ry.toString()+"&rz="+rz.toString()+"&zoom="+zoom.toString()+"&meshServerUrl="+tmp_str;
		return this.imageJspUrl;

	}

	public void setImageJspUrl(String tmp_str) {
		this.imageJspUrl = tmp_str;

	}
	
	public String getBASE64(String s) {
		if (s == null)
			return null;
		String tmp_str=(String) ( (new BASE64Encoder()).encode(s.getBytes()) );
		return tmp_str;
	}

	// BASE64 decode
	public String getFromBASE64(String s) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);
		} catch (Exception e) {
			return null;
		}
	}
	
	public void setServiceUrl(String tmp_str) {
		this.meshServerUrl= tmp_str;
	}

}
