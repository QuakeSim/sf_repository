package cgl.quakesim.geofest;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.text.*;

import WebFlowClient.cm.*;

//import WebFlowSoap.*;

public class GeoFESTUtils {

	final String FAULTS = "Faults";

	final String LAYERS = "Layers";

	final String SEPARATOR = "/";

	final String SPC = " ";

	int layerInt = 0;

	int faultInt = 1;

	/**
	 * Call this when you start a new project.  This updates
	 * both the layer and fault counts, despite the name.  The
	 * layer count starts at zero, fault at one, for external
	 * compliance reasons (matching guivisco stuff).
	 */
	public void initLayerInteger() {
		layerInt = 0;
		faultInt = 1;
	}

	/**
	 * Write/update the group file.
	 */
	public void updateGroupFile(ContextManagerImp cm, String projectFullName,
			String projectName) throws Exception {

		long start = System.currentTimeMillis();

		//Set up the printwriter
		String cwd = cm.getCurrentProperty(projectFullName, "Directory");
		String outfile = cwd + "/" + projectName + ".grp";

		PrintWriter pw = new PrintWriter(new FileWriter(outfile));
		System.out.println(outfile);

		//Get list of Faults
		String cxt = projectFullName + "/Faults";
		String[] faults = cm.listContext(cxt);

		//Get list of Layers
		String[] layers = cm.listContext(projectFullName + "/Layers");

		//Write first line
		int length = faults.length + layers.length;
		pw.println("1.0 " + length);

		for (int i = 0; i < layers.length; i++) {
			pw.println(layers[i] + ".sld");
		}

		for (int i = 0; i < faults.length; i++) {
			pw.println(faults[i] + ".flt");
		}
		pw.flush();
		pw.close();

		long stop = System.currentTimeMillis();
		long expired = stop - start;
		System.out.println("Update group file " + expired);

	}

	/**
	 * Write/update the materials file. This is stored in the 
	 * layer directory.  Note the materials file is written in
	 * the "child" or layer directory but needs to get info
	 * from the grandchild (a specific layer).
	 */
	public void updateMaterialsFile(ContextManagerImp cm, String parent,
			String child, String grandchild) throws Exception {

		long start = System.currentTimeMillis();

		layerInt++;

		String context = parent + SEPARATOR + child + SEPARATOR + grandchild;

		//Set up the printwriter
		String cwd = cm.getCurrentProperty(parent + SEPARATOR + child,
				"Directory");
		String outfile = cwd + "/" + "materials";

		PrintWriter pw = new PrintWriter(new FileWriter(outfile, true));
		System.out.println(outfile);

		pw.println(layerInt + SPC + "lamelambda" + SPC
				+ cm.getCurrentProperty(context, "lameLambda"));
		pw.println(layerInt + SPC + "lamemu" + SPC
				+ cm.getCurrentProperty(context, "lameMu"));
		pw.println(layerInt + SPC + "viscosity" + SPC
				+ cm.getCurrentProperty(context, "viscosity"));
		pw.println(layerInt + SPC + "exponent" + SPC
				+ cm.getCurrentProperty(context, "exponent"));

		pw.flush();
		pw.close();
		long stop = System.currentTimeMillis();
		long expired = stop - start;
		System.out.println("UpdateMaterialsFile " + expired);

	}

	/**
	 * Write/update the materials file. This is stored in the 
	 * layer directory.  Note the materials file is written in
	 * the "child" or layer directory but needs to get info
	 * from the grandchild (a specific layer).
	 * 
	 * This version is used to write each layer's material
	 * properties in a different file.  The string grandchild
	 * is the actual layer name.
	 *
	 * Note this version does NOT append. It overwrites, as
	 * it should.
	 */
	public void updateMaterialsFile2(ContextManagerImp cm, String parent,
			String child, String grandchild) throws Exception {

		layerInt++;

		//Set up the printwriter
		String context = parent + SEPARATOR + child + SEPARATOR + grandchild;
		String cwd = cm.getCurrentProperty(context, "Directory");
		String outfile = cwd + "/" + grandchild + ".materials";

		PrintWriter pw = new PrintWriter(new FileWriter(outfile, false));
		System.out.println(outfile);

		pw.println("lamelambda" + SPC
				+ cm.getCurrentProperty(context, "lameLambda"));
		pw.println("lamemu" + SPC + cm.getCurrentProperty(context, "lameMu"));
		pw.println("viscosity" + SPC
				+ cm.getCurrentProperty(context, "viscosity"));
		pw.println("exponent" + SPC
				+ cm.getCurrentProperty(context, "exponent"));

		pw.flush();
		pw.close();

	}

	/**
	 * Write also a parameter file for the fault that is 
	 * expected by geotrans. The file is always called 
	 * <faultname>.params.  The faultname is given by the
	 * gchild string.
	 */
	public void writeFaultParamFile2(ContextManagerImp cm, String parent,
			String child, String gchild) throws Exception {

		String PARAMS = gchild + ".params";
		String tab = "\t";

		String context = parent + SEPARATOR + child + SEPARATOR + gchild;
		String cwd = cm.getCurrentProperty(context, "Directory");
		String outfile = cwd + File.separator + PARAMS;
		String headerline = "number" + tab + "dip(o)" + tab + "strike(o)" + tab
				+ "slip(m)" + tab + "rake(o)" + tab + "length(km)" + tab
				+ "width(km)" + tab + "depth(km)";

		String number = cm.getCurrentProperty(context, "faultNumber");
		String dip = cm.getCurrentProperty(context, "faultDipAngle");
		String strike = cm.getCurrentProperty(context, "faultStrikeAngle");
		String slip = cm.getCurrentProperty(context, "faultSlip");
		String rake = cm.getCurrentProperty(context, "faultRakeAngle");
		String length = cm.getCurrentProperty(context, "faultLength");
		String width = cm.getCurrentProperty(context, "faultWidth");
		String depth = cm.getCurrentProperty(context, "faultDepth");
		String orig_x = cm.getCurrentProperty(context, "faultOriginX");
		String orig_y = cm.getCurrentProperty(context, "faultOriginY");
		PrintWriter pw = new PrintWriter(new FileWriter(outfile));
		pw.println(headerline);
		pw.print(number + tab + dip + tab + strike + tab + slip + tab + rake
				+ tab + length + tab + width + tab + depth + tab + orig_x + tab
				+ orig_y);
		pw.flush();
		pw.close();
	}

	/**
	 * Write also a parameter file for the fault that is 
	 * expected by lee2geof. The file is always called "params".
	 */
	public void writeFaultParamFile(ContextManagerImp cm, String parent,
			String child, String gchild) throws Exception {

		long start = System.currentTimeMillis();

		String PARAMS = "params";
		String tab = "\t";

		String context = parent + SEPARATOR + child + SEPARATOR + gchild;
		String cwd = cm.getCurrentProperty(context, "Directory");
		String outfile = cwd + File.separator + PARAMS;
		String headerline = "number" + tab + "dip(o)" + tab + "strike(o)" + tab
				+ "slip(m)" + tab + "rake(o)" + tab + "length(km)" + tab
				+ "width(km)" + tab + "depth(km)";

		String number = cm.getCurrentProperty(context, "faultNumber");
		String dip = cm.getCurrentProperty(context, "faultDipAngle");
		String strike = cm.getCurrentProperty(context, "faultStrikeAngle");
		String slip = cm.getCurrentProperty(context, "faultSlip");
		String rake = cm.getCurrentProperty(context, "faultRakeAngle");
		String length = cm.getCurrentProperty(context, "faultLength");
		String width = cm.getCurrentProperty(context, "faultWidth");
		String depth = cm.getCurrentProperty(context, "faultDepth");
		PrintWriter pw = new PrintWriter(new FileWriter(outfile));
		pw.println(headerline);
		pw.print(number + tab + dip + tab + strike + tab + slip + tab + rake
				+ tab + length + tab + width + tab + depth);
		pw.flush();
		pw.close();
		long stop = System.currentTimeMillis();
		long expired = stop - start;
		System.out.println("WriteFaultParamFile " + expired);
	}

	/**
	 * Write out the fault to the context using guiVisco/geoFEST format.
	 * This calculates the fault origin in cartesian coordinates
	 * relative to the origin of the layer being used.
	 */
	public void writeFaultOutputFile(ContextManagerImp cm, String parent,
			String fault_child, String layer_child, String fault_gchild,
			String layer_gchild) throws Exception {
		long start = System.currentTimeMillis();

		String EXT = ".flt";
		String fault_context = parent + SEPARATOR + fault_child + SEPARATOR
				+ fault_gchild;
		String layer_context = parent + SEPARATOR + layer_child + SEPARATOR
				+ layer_gchild;
		String cwd = cm.getCurrentProperty(fault_context, "Directory");
		String outfile = cwd + File.separator + fault_gchild + EXT;

		double locX, locY;
		try {
			locX = Double.parseDouble(cm.getCurrentProperty(fault_context,
					"faultOriginX"));
			locY = Double.parseDouble(cm.getCurrentProperty(fault_context,
					"faultOriginY"));
		} catch (Exception ex) {
			//Do this for backward compatibility.  You will
			//get a number format exception for old style
			//fault contexts.
			//Get out the lat and lon from the context.
			double latstart = Double.parseDouble(cm.getCurrentProperty(
					fault_context, "faultLatStart"));
			double lonstart = Double.parseDouble(cm.getCurrentProperty(
					fault_context, "faultLonStart"));
			double latend = Double.parseDouble(cm.getCurrentProperty(
					fault_context, "faultLatEnd"));
			double lonend = Double.parseDouble(cm.getCurrentProperty(
					fault_context, "faultLonEnd"));

			//Get the layer origin's lat and lon
			double layerLatOrigin = Double.parseDouble(cm.getCurrentProperty(
					layer_context, "layerLatOrigin"));
			double layerLonOrigin = Double.parseDouble(cm.getCurrentProperty(
					layer_context, "layerLonOrigin"));

			//Calculate the fault start in cartesian coordinates relative to 
			//the layer origin.  Layer origin cart coordinates are (0,0,0) of course.
			//Calculate the length
			NumberFormat format = NumberFormat.getInstance();
			double d2r = Math.acos(-1.0) / 180.0;
			double factor = d2r
					* Math.cos(d2r * layerLatOrigin)
					* (6378.139 * (1.0 - Math.sin(d2r * layerLatOrigin)
							* Math.sin(d2r * layerLatOrigin) / 298.247));

			//These are the (x,y) for the fault's start.
			locX = (lonstart - layerLonOrigin) * factor;
			locY = (latstart - layerLatOrigin) * 111.32;
		}

		//Get out the stuff directly stored in the context.
		double locZ = Double.parseDouble(cm.getCurrentProperty(fault_context,
				"faultOriginZ"));
		double length = Double.parseDouble(cm.getCurrentProperty(fault_context,
				"faultLength"));
		double width = Double.parseDouble(cm.getCurrentProperty(fault_context,
				"faultWidth"));
		double depth = Double.parseDouble(cm.getCurrentProperty(fault_context,
				"faultDepth"));
		double dip = Double.parseDouble(cm.getCurrentProperty(fault_context,
				"faultDipAngle"));
		double strike = Double.parseDouble(cm.getCurrentProperty(fault_context,
				"faultStrikeAngle"));

		//Useful math.  See guiVisco documents for details.
		double minus_depth = -depth;
		double strike_deg = strike * Math.PI / 180.0;
		double dip_deg = dip * Math.PI / 180.0;

		double P00 = locX;
		double P01 = locY;
		double P02 = minus_depth;

		double P10 = locX + length * Math.sin(strike_deg);
		double P11 = locY + length * Math.cos(strike_deg);
		double P12 = minus_depth;

		double P20 = locX + length * Math.sin(strike_deg) - width
				* Math.cos(strike_deg) * Math.cos(dip_deg);
		double P21 = locY + length * Math.cos(strike_deg) + width
				* Math.sin(strike_deg) * Math.cos(dip_deg);
		double P22 = minus_depth + width * Math.sin(dip_deg);

		double P30 = locX - width * Math.cos(strike_deg) * Math.cos(dip_deg);
		double P31 = locY + width * Math.sin(strike_deg) * Math.cos(dip_deg);
		double P32 = minus_depth + width * Math.sin(dip_deg);

		//Write it to file.
		PrintWriter pw = new PrintWriter(new FileWriter(outfile));
		pw.println("4");
		pw.println(P00 + SPC + P01 + SPC + P02);
		pw.println(P10 + SPC + P11 + SPC + P12);
		pw.println(P20 + SPC + P21 + SPC + P22);
		pw.println(P30 + SPC + P31 + SPC + P32);

		//Finally, write the following stuff

		pw.println("1");
		pw.println(faultInt + " 1");
		pw.println("\t 4 1 2 3 4");

		pw.flush();
		pw.close();
		// Update the fault int.
		faultInt++;

		//       long stop=System.currentTimeMillis();  
		//       long expired=stop-start;
		//       System.out.println("WriteFaultOutputFile"+expired);

	}

	/**
	 *Formatting follows visco_layer.c from original code.
	 */
	public void writeLayerOutputFile(ContextManagerImp cm, String parent,
			String child, String gchild) throws Exception {
		long start = System.currentTimeMillis();
		String EXT = ".sld";
		String context = parent + SEPARATOR + child + SEPARATOR + gchild;
		String cwd = cm.getCurrentProperty(context, "Directory");
		String outfile = cwd + File.separator + gchild + EXT;

		double originX = Double.parseDouble(cm.getCurrentProperty(context,
				"layerOriginX"));
		double originY = Double.parseDouble(cm.getCurrentProperty(context,
				"layerOriginY"));
		double originZ = Double.parseDouble(cm.getCurrentProperty(context,
				"layerOriginZ"));
		double length = Double.parseDouble(cm.getCurrentProperty(context,
				"layerLength"));
		double width = Double.parseDouble(cm.getCurrentProperty(context,
				"layerWidth"));
		double depth = Double.parseDouble(cm.getCurrentProperty(context,
				"layerDepth"));

		double P00 = originX;
		double P01 = originY;
		double P02 = originZ;

		double P10 = originX + length;
		double P11 = originY;
		double P12 = originZ;

		double P20 = originX + length;
		double P21 = originY + width;
		double P22 = originZ;

		double P30 = originX;
		double P31 = originY + width;
		double P32 = originZ;

		double P40 = originX;
		double P41 = originY;
		double P42 = originZ - depth;

		double P50 = originX + length;
		double P51 = originY;
		double P52 = originZ - depth;

		double P60 = originX + length;
		double P61 = originY + width;
		double P62 = originZ - depth;

		double P70 = originX;
		double P71 = originY + width;
		double P72 = originZ - depth;

		//Write it to file.
		String TAB = "\t";
		PrintWriter pw = new PrintWriter(new FileWriter(outfile));
		//Print the 8 points of the cube.
		pw.println("8");
		pw.println(P00 + SPC + P01 + SPC + P02);
		pw.println(P10 + SPC + P11 + SPC + P12);
		pw.println(P20 + SPC + P21 + SPC + P22);
		pw.println(P30 + SPC + P31 + SPC + P32);
		pw.println(P40 + SPC + P41 + SPC + P42);
		pw.println(P50 + SPC + P51 + SPC + P52);
		pw.println(P60 + SPC + P61 + SPC + P62);
		pw.println(P70 + SPC + P71 + SPC + P72);

		//Now print this other mysterious stuff.
		pw.println("6");
		pw.println("0 1");
		pw.println("\t 4 1 2 3 4");
		pw.println("0 1");
		pw.println("\t 4 1 2 6 5");
		pw.println("0 1");
		pw.println("\t 4 2 3 7 6");
		pw.println("0 1");
		pw.println("\t 4 3 4 8 7");
		pw.println("0 1");
		pw.println("\t 4 4 1 5 8");
		pw.println("0 1");
		pw.println("\t 4 5 6 7 8");

		pw.flush();
		pw.close();

		long stop = System.currentTimeMillis();
		long expired = stop - start;
		System.out.println("WriteLayerOutputFile:" + expired);

	}

	/**
	 * Get all the request parameters and put them in the appropriate
	 * context.
	 */
	public void setContextProperties(ContextManagerImp cm, String parent,
			String child, String grandchild, GeoFESTElement myelement)
			throws Exception {

		long start = System.currentTimeMillis();

		String current = parent + SEPARATOR + child + SEPARATOR + grandchild;
		cm.addContext(current);
		for (int i=0; i< myelement.size() ; i++ ) {
			String theparam = (String) myelement.getName(i);
			String value = (String) myelement.getValue(i);
			cm.setCurrentProperty(current, theparam, value);
		}

		long stop = System.currentTimeMillis();
		long expired = stop - start;
		System.out.println("SetContextProperties:" + expired);

	}
}
