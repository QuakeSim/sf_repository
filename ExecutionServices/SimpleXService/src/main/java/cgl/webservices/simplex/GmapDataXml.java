/*
 GmapDataXml generator.
 */
package cgl.webservices.simplex;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * Make up and write an XML document, using DOM UPDATED FOR JAXP.
 */
public class GmapDataXml {

	// private Document doc;

	private double original_lat = 37.0;

	private double original_lon = -122.157675;

	private Hashtable MyObsPoints = new Hashtable();

	private ArrayList ObservMarkers = new ArrayList();

	private ArrayList CalculatedMarkers = new ArrayList();

	private ArrayList ResidualMarkers = new ArrayList();

	public static void main(String[] av) throws IOException {

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex1) {
			ex1.printStackTrace();
		}

		GmapDataXml dw = new GmapDataXml();
		dw.LoadDataFromFile("test.output");
		dw.CalculateMarker();
		Document doc = dw.makeDoc();
		dw.printToConsole(doc);
		dw.printToFile(doc, "googleMapPoints.xml");

	}
	
	public void setOriginal_lat (String tmp_str) {
		this.original_lat = Double.valueOf(tmp_str.trim() ).doubleValue();
	}
	
	public void setOriginal_lon (String tmp_str) {
		this.original_lon = Double.valueOf(tmp_str.trim() ).doubleValue();
	}

	//
	public void LoadDataFromFile(String InputFileName) {
		try {
			String line = new String();
			int skipthreelines = 1;
			BufferedReader in = new BufferedReader(
					new FileReader(InputFileName));
			while ((line = in.readLine()) != null) {
				if (skipthreelines <= 4) {

				} else {
					if (!line.trim().equalsIgnoreCase("")) {

						String tmp[] = line.split("\t");

						//  //See what we have.
						//  System.out.println("Original line: "+line);
						//  for (int i=0;i<tmp.length;i++) {
						// 	  System.out.println(tmp[i]);
						//  }
						

						double tmp_lat = Double.valueOf(tmp[1].trim())
								.doubleValue();
						double tmp_lon = Double.valueOf(tmp[2].trim())
								.doubleValue();
						int tmp_type = Integer.valueOf(tmp[0].trim())
								.intValue();
						double tmp_observ = Double.valueOf(tmp[3].trim())
								.doubleValue();
						double tmp_calc = Double.valueOf(tmp[4].trim())
								.doubleValue();
						double tmp_o_c = Double.valueOf(tmp[5].trim())
								.doubleValue();
						double tmp_error = Double.valueOf(tmp[6].trim())
								.doubleValue();

						ObservationPoint TmpObsPoint = new ObservationPoint();
						if (MyObsPoints.get(new Coordinate(tmp_lat, tmp_lon)) != null) {
							TmpObsPoint = (ObservationPoint) MyObsPoints
									.get(new Coordinate(tmp_lat, tmp_lon));
						}
						if (tmp_type == 1) {
							TmpObsPoint.EastVec.type = tmp_type;
							TmpObsPoint.EastVec.observ = tmp_observ;
							TmpObsPoint.EastVec.calc = tmp_calc;
							TmpObsPoint.EastVec.o_c = tmp_o_c;
							TmpObsPoint.EastVec.error = tmp_error;
						} else if (tmp_type == 2) {
							TmpObsPoint.NorthVec.type = tmp_type;
							TmpObsPoint.NorthVec.observ = tmp_observ;
							TmpObsPoint.NorthVec.calc = tmp_calc;
							TmpObsPoint.NorthVec.o_c = tmp_o_c;
							TmpObsPoint.NorthVec.error = tmp_error;
						} else if (tmp_type == 3) {
							TmpObsPoint.UpVec.type = tmp_type;
							TmpObsPoint.UpVec.observ = tmp_observ;
							TmpObsPoint.UpVec.calc = tmp_calc;
							TmpObsPoint.UpVec.o_c = tmp_o_c;
							TmpObsPoint.UpVec.error = tmp_error;
						}
						TmpObsPoint.xloc = tmp_lat;
						TmpObsPoint.yloc = tmp_lon;
						MyObsPoints.put(new Coordinate(TmpObsPoint.xloc,
								TmpObsPoint.yloc), TmpObsPoint);

						// System.out.println(line);
					} else {
						break;
					}
				}
				skipthreelines++;
			}
			in.close();
		} catch (IOException ex1) {
			ex1.printStackTrace();
		}

	}

	public void CalculateMarker() {
		Object obj;
		Enumeration e = MyObsPoints.keys();
		ObservationPoint tmp_obs = new ObservationPoint();
		while (e.hasMoreElements()) {
			obj = e.nextElement();
			tmp_obs = (ObservationPoint) MyObsPoints.get(obj);
			double xloc = tmp_obs.xloc, yloc = tmp_obs.yloc;
			System.out.println(tmp_obs.NorthVec.observ + ":"
					+ tmp_obs.EastVec.observ + ":" + tmp_obs.UpVec.observ);

			// double rads;
			// int degs;
			// double xloc=tmp_obs.xloc,yloc=tmp_obs.yloc;
			// // Obtain angle in degrees from user
			// degs =(int) Math.toDegrees(Math.atan2(
			// tmp_obs.EastVec.observ,tmp_obs.NorthVec.observ));
			// degs = (360 + degs)%360;
			// // Convert degrees to radian
			// rads = Math.toRadians((double)degs);
			// int a = 20, b=34,A=20,B=34;
			// A=(int) ( a* Math.abs(Math.cos(rads))+b*Math.abs(Math.sin(rads))
			// );
			// B=(int) ( a*Math.abs(Math.sin(rads))+b*Math.abs(Math.cos(rads))
			// );
			// System.out.println( degs+"A:"+A+"B:"+B );
			// System.out.println(obj + ":" + tmp_obs);

			ObservMarkers.add(calDegsAndMakeMarker(xloc, yloc,
					tmp_obs.EastVec.observ, tmp_obs.NorthVec.observ, tmp_obs));
			CalculatedMarkers.add(calDegsAndMakeMarker(xloc, yloc,
					tmp_obs.EastVec.calc, tmp_obs.NorthVec.calc, tmp_obs));
			ResidualMarkers.add(calDegsAndMakeMarker(xloc, yloc,
					tmp_obs.EastVec.o_c, tmp_obs.NorthVec.o_c, tmp_obs));

		}

		// MyMarkers.add(arg0);
	}

	public PointEntry[] getO_cList() {
		Object obj;
		ArrayList dataset = new ArrayList();
		Enumeration e = MyObsPoints.keys();
		ObservationPoint tmp_obs = new ObservationPoint();
		while (e.hasMoreElements()) {
			obj = e.nextElement();

			PointEntry tempPoint = new PointEntry();			
			tmp_obs = (ObservationPoint) MyObsPoints.get(obj);

			tempPoint.setX(tmp_obs.xloc+"");
			tempPoint.setY(tmp_obs.yloc+"");
			tempPoint.setDeltaXName("dx");
			tempPoint.setDeltaXValue(tmp_obs.EastVec.o_c+"");
			tempPoint.setDeltaYName("dy");
			tempPoint.setDeltaYValue(tmp_obs.NorthVec.o_c+"");
			tempPoint.setDeltaZName("dz");
			tempPoint.setDeltaZValue(tmp_obs.UpVec.o_c+"");
			tempPoint.setFolderTag("o_c");
			dataset.add(tempPoint);			
			
		}		
		return (PointEntry[]) (dataset.toArray(new PointEntry[dataset.size()]));
	}
	
	public PointEntry[] getCalcList() {
		Object obj;
		ArrayList dataset = new ArrayList();
		Enumeration e = MyObsPoints.keys();
		ObservationPoint tmp_obs = new ObservationPoint();
		while (e.hasMoreElements()) {
			obj = e.nextElement();

			PointEntry tempPoint = new PointEntry();			
			tmp_obs = (ObservationPoint) MyObsPoints.get(obj);

			tempPoint.setX(tmp_obs.xloc+"");
			tempPoint.setY(tmp_obs.yloc+"");
			tempPoint.setDeltaXName("dx");
			tempPoint.setDeltaXValue(tmp_obs.EastVec.calc+"");
			tempPoint.setDeltaYName("dy");
			tempPoint.setDeltaYValue(tmp_obs.NorthVec.calc+"");
			tempPoint.setDeltaZName("dz");
			tempPoint.setDeltaZValue(tmp_obs.UpVec.calc+"");
			tempPoint.setFolderTag("calc");
			dataset.add(tempPoint);			
			
		}		
		return (PointEntry[]) (dataset.toArray(new PointEntry[dataset.size()]));
	}
	
	public PointEntry[] getObservList() {
		Object obj;
		ArrayList dataset = new ArrayList();
		Enumeration e = MyObsPoints.keys();
		ObservationPoint tmp_obs = new ObservationPoint();
		while (e.hasMoreElements()) {
			obj = e.nextElement();

			PointEntry tempPoint = new PointEntry();			
			tmp_obs = (ObservationPoint) MyObsPoints.get(obj);

			tempPoint.setX(tmp_obs.xloc+"");
			tempPoint.setY(tmp_obs.yloc+"");
			tempPoint.setDeltaXName("dx");
			tempPoint.setDeltaXValue(tmp_obs.EastVec.observ+"");
			tempPoint.setDeltaYName("dy");
			tempPoint.setDeltaYValue(tmp_obs.NorthVec.observ+"");
			tempPoint.setDeltaZName("dz");
			tempPoint.setDeltaZValue(tmp_obs.UpVec.observ+"");
			tempPoint.setFolderTag("observ");
			dataset.add(tempPoint);			
			
		}		
		return (PointEntry[]) (dataset.toArray(new PointEntry[dataset.size()]));
	}


	private googleMapMarker calDegsAndMakeMarker(double xloc, double yloc,
			double eastobserv, double northobserv, ObservationPoint tmp_obs) {
		String tiphtml = "";
		String windowhtml = "";
		double rads;
		int degs;
		// Obtain angle in degrees from user
		degs = (int) Math.toDegrees(Math.atan2(eastobserv, northobserv));
		degs = (360 + degs) % 360;
		// Convert degrees to radian
		rads = Math.toRadians((double) degs);
		int a = 20, b = 34, A = 20, B = 34;
		A = (int) (a * Math.abs(Math.cos(rads)) + b * Math.abs(Math.sin(rads)));
		B = (int) (a * Math.abs(Math.sin(rads)) + b * Math.abs(Math.cos(rads)));
		tiphtml += "<b>xloc:</b>" + xloc + "<br>";
		tiphtml += "<b>yloc:</b>" + yloc + "<br>";
		tiphtml += "<b>length:</b><font color=red>"
				+ (Math.sqrt(eastobserv * eastobserv + northobserv
						* northobserv)) + "</font><br>";
		tiphtml += "<b>degree:</b><font color=blue>" + degs + "</font><br>";
		windowhtml += "<b>xloc:</b>" + xloc + "<br>";
		windowhtml += "<b>yloc:</b>" + yloc + "<br>";
		if (tmp_obs.NorthVec.type == 1) {
			windowhtml += "<b>North vector:</b>" + tmp_obs.NorthVec + "<br>";
		}
		if (tmp_obs.EastVec.type == 2) {
			windowhtml += "<b>East vector:</b>" + tmp_obs.EastVec + "<br>";
		}
		if (tmp_obs.UpVec.type == 3) {
			windowhtml += "<b>Up vector:</b>" + tmp_obs.UpVec + "<br>";
		}

		return makeMarker(xloc, yloc, A, B, degs, tiphtml, windowhtml);

	}

	private googleMapMarker makeMarker(double x, double y, int A, int B,
			int degs, String thtml, String whtml) {
		googleMapMarker tmp_marker = new googleMapMarker();
		//tmp_marker.setLat(x / 100 + this.original_lat);
		//tmp_marker.setLon(y / 100 + this.original_lon);
		tmp_marker.setLat(x);
		tmp_marker.setLon(y);
		tmp_marker.setWidth(A);
		tmp_marker.setHeight(B);
		tmp_marker.setMouseoutimage("icon/base_green" + degs + ".png");
		tmp_marker.setMouseonimage("icon/base_red" + degs + ".png");
		tmp_marker.setTextimage("icon/" + degs + ".png");
		tmp_marker.setLabel("");
		tmp_marker.setWindowhtml(whtml);
		tmp_marker.setTiphtml(thtml);
		return tmp_marker;
	}

	/** Generate the XML document */
	public Document makeDoc() {
		try {
			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = fact.newDocumentBuilder();
			Document doc = parser.newDocument();

			Element root = doc.createElement("markers");
			// root.setAttribute("gender", "f");
			doc.appendChild(root);

			Element mapinfo = doc.createElement("mapinfo");
			mapinfo.setAttribute("original_lat", this.original_lat	+ "");
			mapinfo.setAttribute("original_lon", this.original_lon	+ "");
			root.appendChild(mapinfo);
			
			
			for (int i = 0; i < ObservMarkers.size(); i++) {
				Element marker = doc.createElement("obsmarker");
				marker.setAttribute("lat", ((googleMapMarker) ObservMarkers
						.get(i)).getLat()
						+ "");
				marker.setAttribute("lon", ((googleMapMarker) ObservMarkers
						.get(i)).getLon()
						+ "");
				marker.setAttribute("width", ((googleMapMarker) ObservMarkers
						.get(i)).getWidth()
						+ "");
				marker.setAttribute("height", ((googleMapMarker) ObservMarkers
						.get(i)).getHeight()
						+ "");
				marker.setAttribute("mouseoutimage",
						((googleMapMarker) ObservMarkers.get(i))
								.getMouseoutimage()
								+ "");
				marker.setAttribute("mouseonimage",
						((googleMapMarker) ObservMarkers.get(i))
								.getMouseonimage()
								+ "");
				marker.setAttribute("textimage",
						((googleMapMarker) ObservMarkers.get(i)).getTextimage()
								+ "");
				marker.setAttribute("label", ((googleMapMarker) ObservMarkers
						.get(i)).getLabel()
						+ "obsMarker" + i + 1);
				marker.setAttribute("windowhtml",
						((googleMapMarker) ObservMarkers.get(i))
								.getWindowhtml()
								+ "");
				marker.setAttribute("tiphtml", ((googleMapMarker) ObservMarkers
						.get(i)).getTiphtml()
						+ "");
				root.appendChild(marker);
			}

			for (int i = 0; i < CalculatedMarkers.size(); i++) {
				Element marker = doc.createElement("calmarker");
				marker.setAttribute("lat", ((googleMapMarker) CalculatedMarkers
						.get(i)).getLat()
						+ "");
				marker.setAttribute("lon", ((googleMapMarker) CalculatedMarkers
						.get(i)).getLon()
						+ "");
				marker.setAttribute("width",
						((googleMapMarker) CalculatedMarkers.get(i)).getWidth()
								+ "");
				marker.setAttribute("height",
						((googleMapMarker) CalculatedMarkers.get(i))
								.getHeight()
								+ "");
				marker.setAttribute("mouseoutimage",
						((googleMapMarker) CalculatedMarkers.get(i))
								.getMouseoutimage()
								+ "");
				marker.setAttribute("mouseonimage",
						((googleMapMarker) CalculatedMarkers.get(i))
								.getMouseonimage()
								+ "");
				marker.setAttribute("textimage",
						((googleMapMarker) CalculatedMarkers.get(i))
								.getTextimage()
								+ "");
				marker.setAttribute("label",
						((googleMapMarker) CalculatedMarkers.get(i)).getLabel()
								+ "calMarker" + i + 1);
				marker.setAttribute("windowhtml",
						((googleMapMarker) CalculatedMarkers.get(i))
								.getWindowhtml()
								+ "");
				marker.setAttribute("tiphtml",
						((googleMapMarker) CalculatedMarkers.get(i))
								.getTiphtml()
								+ "");
				root.appendChild(marker);
			}

			for (int i = 0; i < ResidualMarkers.size(); i++) {
				Element marker = doc.createElement("resmarker");
				marker.setAttribute("lat", ((googleMapMarker) ResidualMarkers
						.get(i)).getLat()
						+ "");
				marker.setAttribute("lon", ((googleMapMarker) ResidualMarkers
						.get(i)).getLon()
						+ "");
				marker.setAttribute("width", ((googleMapMarker) ResidualMarkers
						.get(i)).getWidth()
						+ "");
				marker.setAttribute("height",
						((googleMapMarker) ResidualMarkers.get(i)).getHeight()
								+ "");
				marker.setAttribute("mouseoutimage",
						((googleMapMarker) ResidualMarkers.get(i))
								.getMouseoutimage()
								+ "");
				marker.setAttribute("mouseonimage",
						((googleMapMarker) ResidualMarkers.get(i))
								.getMouseonimage()
								+ "");
				marker.setAttribute("textimage",
						((googleMapMarker) ResidualMarkers.get(i))
								.getTextimage()
								+ "");
				marker.setAttribute("label", ((googleMapMarker) ResidualMarkers
						.get(i)).getLabel()
						+ "resMarker" + i + 1);
				marker.setAttribute("windowhtml",
						((googleMapMarker) ResidualMarkers.get(i))
								.getWindowhtml()
								+ "");
				marker.setAttribute("tiphtml",
						((googleMapMarker) ResidualMarkers.get(i)).getTiphtml()
								+ "");
				root.appendChild(marker);
			}

			// Element line = doc.createElement("Line");
			// marker.appendChild(line);
			// line.appendChild(doc.createTextNode("Once, upon a midnight
			// dreary"));
			// line = doc.createElement("Line");
			// marker.appendChild(line);
			// line.appendChild(doc.createTextNode("While I pondered, weak and
			// weary"));

			return doc;

		} catch (Exception ex) {
			System.err.println("+============================+");
			System.err.println("|        XML Error           |");
			System.err.println("+============================+");
			System.err.println(ex.getClass());
			System.err.println(ex.getMessage());
			System.err.println("+============================+");
			return null;
		}
	}

	/**
	 * This method uses Xerces specific classes prints the XML document to
	 * console.
	 */
	public void printToConsole(Document dom) {
		try {

			// print
			OutputFormat format = new OutputFormat(dom);
			format.setIndenting(true);

			// to generate output to console use this serializer
			// XMLSerializer serializer = new XMLSerializer(System.out, format);

			// to generate a file output use fileoutputstream instead of
			// system.out
			XMLSerializer serializer = new XMLSerializer(System.out, format);

			serializer.serialize(dom);

		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	/**
	 * This method uses Xerces specific classes prints the XML document to file.
	 */
	public void printToFile(Document dom, String xmlfilename) {
		try {
			// print
			OutputFormat format = new OutputFormat(dom);
			format.setIndenting(true);

			// to generate output to console use this serializer
			// XMLSerializer serializer = new XMLSerializer(System.out, format);

			// to generate a file output use fileoutputstream instead of
			// system.out
			XMLSerializer serializer = new XMLSerializer(new FileOutputStream(
					new File(xmlfilename)), format);

			serializer.serialize(dom);

		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}
}
