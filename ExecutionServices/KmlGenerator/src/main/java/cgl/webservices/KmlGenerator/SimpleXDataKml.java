/* 
	GmapDataXml generator. 
*/ 

// Modified by Jun Ji of CGL, May 2009		email : jid@cs.indiana.edu 

package cgl.webservices.KmlGenerator; 

import java.awt.Color;
import java.io.BufferedReader; 
import java.io.File; 
import java.io.FileInputStream; 
import java.io.FileNotFoundException; 
import java.io.FileOutputStream; 
import java.io.FileReader; 
import java.io.IOException; 
import java.io.PrintStream; 
import java.util.ArrayList; 
import java.util.Hashtable; 
import java.util.Properties; 
import java.util.regex.Pattern; 
 
import javax.servlet.ServletContext; 
import javax.servlet.http.HttpServlet; 
 
import org.apache.axis.MessageContext; 
import org.apache.axis.transport.http.HTTPConstants; 
 
import cgl.webservices.KmlGenerator.gekmlib.*; 

import com.sun.org.apache.xml.internal.serialize.OutputFormat; 
import com.sun.org.apache.xml.internal.serialize.XMLSerializer; 
 
/** 
 * Make up and write an XML document, using DOM UPDATED FOR JAXP. 
 */ 
public class SimpleXDataKml { 
	 // private Document doc; 
	 
	 //These are used for (x,y) <-> (lat,lon) conversions
	 private double eqrad= 6378.139;
	 private double flattening= 1.0/298.247;
	 private double yfactor=111.32;
 
	 //This is used to control the number of arrows
	 //plotted.
	 private int decimationFactor;

	 private double original_lat = 0; 
	 private double original_lon = 0; 
	 private double coordinateUnit = 1000.0; 
	 private double latref = 0; 
	 private double lonref = 0; 
	 
	 protected Kml doc = new Kml(); 	 
	 protected Folder root = new Folder(); 	 
	 protected Document kmlDocument=new Document(); 
	 protected PointEntry[] datalist = null; 
	 
	 private String serverUrl;	 
	 private String baseOutputDestDir; 
	 
	 private String length_measure = "km"; 
	 
	 private Properties properties; 
	 
	 private double arrowScale, defaultArrowScale; 
	 
	 //Some useful string constants 
	 
	 String fontStart="<font color=blue>"; 
	 String fontEnd="</font>"; 
	 String br="<br/>"; 
	 String cdataStart="<![CDATA[ <br/> <hr/>"; 
	 String cdataEnd="]]>"; 
 
	 public SimpleXDataKml() { 
		  this(false); 
	 } 
 
	 public SimpleXDataKml(boolean useClassLoader) { 
		  init(useClassLoader); 
	 }
	 
	public void doc_init(){
		
		doc = new Kml();
		kmlDocument=new Document();
		root = new Folder();
		root.setName("root Folder");
		root.setDescription("This is the root folder");
		kmlDocument.addFolder(root);
		doc.addDocument(kmlDocument);
	}
 
	public SimpleXDataKml(String OriginalLon, String OriginalLat, boolean useClassLoader) { 
		this.setOriginal_lon(OriginalLon); 
		this.setOriginal_lat(OriginalLat); 
		init(useClassLoader); 
	} 
 
	public SimpleXDataKml(String OriginalLon, String OriginalLat) { 
		this(OriginalLon, OriginalLat, false); 
	} 
	 
	 /** 
	  * This is a main method for testing. 
	  */
	 public static void main(String[] av) throws IOException {
		  doTest();
	 }
	 
	 public void setDatalist(PointEntry[] InputDataList) {
		  datalist = InputDataList; 
		  System.out.println("datalist.length = " + datalist.length);
	 }
	 
	 public String runMakeKml (String ServerTag,String UserName, String ProjectName, String JobUID) {
		  System.out.println("[SimpleXDataKml/runMakeKml] started");		  
		  String destDir=generateOutputDestDir(ServerTag,UserName,ProjectName,JobUID); 
		  String baseUrl=generateBaseUrl(ServerTag,UserName,ProjectName,JobUID); 
		  
		  try{ 
				makeWorkDir(destDir);
		  } 
		  catch (Exception e) { 
			 e.printStackTrace();			  
		 } 
		  
		 // System.out.println("Printing to file");
		 // this.printToFile(this.doc.toKML(), destDir + "/" + ProjectName + JobUID +".kml"); 
		  
		  try { 
				System.out.println("[SimpleXDataKml/runMakeKml] Directly printing KML to "+destDir + "/" + ProjectName + JobUID +".kml");
				
				System.out.println("[SimpleXDataKml/runMakeKml] this.doc will generate a KML");
				long start = System.currentTimeMillis();
				String kmlcontent = this.doc.toKML();
				// System.out.println("[runMakeKml] [1] the size of the doc : " + this.doc.toKML().length());
				System.out.println("[SimpleXDataKml/runMakeKml] the size of the doc : " + kmlcontent.length());
				System.out.println("[SimpleXDataKml/runMakeKml] the KML generated in " + (System.currentTimeMillis() -start)/1000+"Secs");
				
				PrintStream out = new PrintStream(new FileOutputStream(destDir + "/" + ProjectName + JobUID +".kml"));
				
				System.out.println("[SimpleXDataKml/runMakeKml] now it will make a file using the pre-executed this.doc.toKML");
				start = System.currentTimeMillis();
				out.println(kmlcontent);
				System.out.println("[SimpleXDataKml/runMakeKml] the size of the doc : " + kmlcontent.length());
				System.out.println("[SimpleXDataKml/runMakeKml] this is finished in " + (System.currentTimeMillis() -start)/1000+"Secs");
				out.close();
				
		  }
		  
		  catch (FileNotFoundException e) { 
				e.printStackTrace(); 
		  } 
		  
		  
		  catch(Exception ex){
				ex.printStackTrace();
		  }
		  
		  System.out.println("[SimpleXDataKml/runMakeKml] Finished");
		 return baseUrl + "/" + ProjectName + JobUID +".kml";		  
	 } 
	  
	 // 
	 public PointEntry[] LoadDataFromFile(String InputFileName) {

		 ArrayList dataset = new ArrayList();

		 try {
			 String line = new String();
			 
			 BufferedReader in = new BufferedReader(new FileReader(InputFileName));
			 in = new BufferedReader(new FileReader(InputFileName));
			 
			 int skipthreelines = 1;
			 
			 while ((line = in.readLine()) != null) {
				 if (skipthreelines <= 4) {

				 } else {

					 if (!line.trim().equalsIgnoreCase("")) {
						 
						 PointEntry tempPoint = new PointEntry();
						 Pattern p = Pattern.compile(" {1,20}");
						 String tmp[] = p.split(line);
						 tempPoint.setX(tmp[1].trim());
						 tempPoint.setY(tmp[2].trim());
						 tempPoint.setDeltaXName("dx");
						 tempPoint.setDeltaXValue(tmp[3].trim());
						 tempPoint.setDeltaYName("dy");
						 tempPoint.setDeltaYValue(tmp[4].trim());
						 tempPoint.setDeltaZName("dz");
						 tempPoint.setDeltaZValue(tmp[5].trim());
						 tempPoint.setFolderTag("point");
						 dataset.add(tempPoint);
						 
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

		 return (PointEntry[]) (dataset.toArray(new PointEntry[dataset.size()])); 
	} 
 
 
	public void init(boolean useClassLoader) { 
		 System.out.println("[SimpleXDataKml/init] ----------------------------------"); 
		 System.out.println("[SimpleXDataKml/init] Initializing KML service."); 
		 System.out.println("[SimpleXDataKml/init] ----------------------------------"); 
 
		 try { 
			  if(useClassLoader) { 
					 
					// System.out.println("[SimpleXDataKml/init] Using classloader"); 
					//This is useful for command line clients but does not work 
					//inside Tomcat. 
					ClassLoader loader=ClassLoader.getSystemClassLoader(); 
					properties=new Properties(); 
					 
					//This works if you are using the classloader but not inside 
					//Tomcat. 
					properties.load(loader.getResourceAsStream("kmlgenerator.properties")); 
			  } 
			  else { 
					// Extract the Servlet Context 
					// System.out.println("Using Servlet Context"); 
					MessageContext msgC = MessageContext.getCurrentContext(); 
					ServletContext context = ((HttpServlet) msgC.getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext(); 
					String propertyFile = context.getRealPath("/") + "/WEB-INF/classes/kmlgenerator.properties"; 
					System.out.println("Prop file location " + propertyFile); 
					properties = new Properties(); 
					properties.load(new FileInputStream(propertyFile)); 
			  } 
 
			   
			  // Note these will be "global" for this class, so 
			  // I will not explicitly pass them around. 
			  serverUrl = properties.getProperty("KmlGenerator.service.url"); 
			  baseOutputDestDir = properties.getProperty("output.dest.dir"); 
			  defaultArrowScale=Double.parseDouble(properties.getProperty("default.arrow.scale")); 
			  arrowScale=defaultArrowScale; 
		 } 
 
		 catch (Exception ex) { 
			  ex.printStackTrace(); 
		 } 
		 doc_init();
		 System.out.println("----------------------------------"); 
		 System.out.println("Initializing KML service finished."); 
		 System.out.println("----------------------------------"); 
	} 
 
	public void setLineStyle(Folder curfolder, String id, String Color_value, double line_width) {
		System.out.println("[SimpleXDataKml/setLineStyle] Color and width for arrowed: "+id+" "+Color_value+" "+line_width);
		Style gridlineStyle = new Style();
		gridlineStyle.setId(id);
		LineStyle newlineStyle = new LineStyle();
		newlineStyle.setWidth((float) (line_width));
		newlineStyle.setColor(Color_value);
		gridlineStyle.addLineStyle(newlineStyle);
		// curfolder.addStyle(gridlineStyle);
		kmlDocument.addStyle(gridlineStyle);
	} 
 
	public void setIconStyle(String id, String iconhref) { 
		Style style1 = new Style(); 
		style1.setId(id); 
		IconStyle newiconstyle= new IconStyle(); 
		Icon newicon= new Icon(); 
		newicon.setHref(iconhref); 
		newiconstyle.addIcon(newicon); 
		 
		style1.addIconStyle(newiconstyle); 
		kmlDocument.addStyle(style1);		 
	} 
 
	public void set360IconStyle(String href_prefix, String href_postfix, String id_prefix) { 
		int i=0; 
		for (i=0;i<=360;i++) { 
			setIconStyle(id_prefix+i,href_prefix+i+href_postfix); 
		} 
	} 
	 
	public void setGridLine(String folderName, double start_x, double start_y, 
			double end_x, double end_y, double xinterval,double yinterval) { 
		 
		if (xinterval <= 0 || yinterval<=0) { 
			return; 
		} 
		 
		Folder container = new Folder(); 
		if (!folderName.equals("") && !folderName.equals("null")) { 
			// create and add another folder to root 
			container.setName(folderName); 
			container.setDescription("This is a folder contained by the root"); 
			root.addFolder(container); 
		} else { 
			System.out.println("roooooooot debug point");
			container = root; 
		} 
		setLineStyle(container, "GridLineStyle", "5f1478DC", 2); 
		double smallx = Math.min(start_x, end_x) * coordinateUnit; 
		double smally = Math.min(start_y, end_y) * coordinateUnit; 
		double bigx = Math.max(start_x, end_x) * coordinateUnit; 
		double bigy = Math.max(start_y, end_y) * coordinateUnit; 
		xinterval = xinterval * coordinateUnit; 
		yinterval = yinterval * coordinateUnit; 
		 
		Placemark mark1 = new Placemark(); 
		mark1.setName("gridline"); 
		LineString newline = new LineString(); 
		mark1.setStyleUrl("#GridLineStyle"); 
		String line_value = ""; 
		double tmp_x = smallx; 
		String UTMZone=ConverterUTM.getUTMZone(23, original_lon, original_lat); 
		Coordinate original_xy = ConverterUTM.LLtoUTM(23,original_lon, original_lat, UTMZone);		 
		int odd_flg = 1; 
//		Coordinate mercator_lonlat = new Coordinate(); 
		// int even_flg=0; 
		while (tmp_x <= bigx) { 
			double mapx = original_xy.getX() + tmp_x; 
			double mapy = original_xy.getY() + smally * (odd_flg % 2) + bigy * ((odd_flg + 1) % 2); 
			Coordinate tmp_lonlat = ConverterUTM.UTMtoLL(23,mapx, mapy, UTMZone); 
			 
//			mercator_lonlat = MapFunction.MercatorProject(tmp_lonlat.getX(), 
//					tmp_lonlat.getY()); 
//			line_value = line_value + mercator_lonlat.getX() + "," 
//					+ mercator_lonlat.getY() + ",0  "; 
			line_value=line_value +tmp_lonlat.getX() + "," + tmp_lonlat.getY() + ",0  "; 
			mapy = original_xy.getY() + bigy * (odd_flg % 2) + smally * ((odd_flg + 1) % 2); 
			tmp_lonlat = ConverterUTM.UTMtoLL(23,mapx, mapy, UTMZone); 
//			mercator_lonlat = MapFunction.MercatorProject(tmp_lonlat.getX(), 
//					tmp_lonlat.getY()); 
//			line_value = line_value + mercator_lonlat.getX() + "," 
//					+ mercator_lonlat.getY() + ",0  "; 
			line_value=line_value +tmp_lonlat.getX() + "," + tmp_lonlat.getY() + ",0  "; 
			tmp_x = tmp_x + xinterval; 
			odd_flg++; 
		} 
		// rollback 
		double mapx1 = original_xy.getX() + bigx; 
		double mapy1 = original_xy.getY() + smally; 
		Coordinate tmp_lonlat1 = ConverterUTM.UTMtoLL(23,mapx1, mapy1, UTMZone); 
//		mercator_lonlat = MapFunction.MercatorProject(tmp_lonlat1.getX(), 
//				tmp_lonlat1.getY()); 
//		line_value = line_value + mercator_lonlat.getX() + "," 
//				+ mercator_lonlat.getY() + ",0  "; 
		line_value=line_value +tmp_lonlat1.getX() + "," + tmp_lonlat1.getY() + ",0  "; 
		mapy1 = original_xy.getY() + bigy; 
		tmp_lonlat1 = ConverterUTM.UTMtoLL(23,mapx1, mapy1, UTMZone); 
//		mercator_lonlat = MapFunction.MercatorProject(tmp_lonlat1.getX(), 
//				tmp_lonlat1.getY()); 
//		line_value = line_value + mercator_lonlat.getX() + "," 
//				+ mercator_lonlat.getY() + ",0  "; 
		line_value=line_value +tmp_lonlat1.getX() + "," + tmp_lonlat1.getY() + ",0  "; 
		mapy1 = original_xy.getY() + smally; 
		tmp_lonlat1 = ConverterUTM.UTMtoLL(23,mapx1, mapy1, UTMZone); 
//		mercator_lonlat = MapFunction.MercatorProject(tmp_lonlat1.getX(), 
//				tmp_lonlat1.getY()); 
//		line_value = line_value + mercator_lonlat.getX() + "," 
//				+ mercator_lonlat.getY() + ",0  "; 
		line_value=line_value +tmp_lonlat1.getX() + "," + tmp_lonlat1.getY() + ",0  "; 
 
		double tmp_y = smally; 
		odd_flg = 1; 
		while (tmp_y < bigy) { 
			double mapy = original_xy.getY() + tmp_y; 
			double mapx = original_xy.getX() + smallx * (odd_flg % 2) + bigx 
					* ((odd_flg + 1) % 2); 
			Coordinate tmp_lonlat = ConverterUTM.UTMtoLL(23,mapx, mapy, UTMZone); 
//			mercator_lonlat = MapFunction.MercatorProject(tmp_lonlat.getX(), 
//					tmp_lonlat.getY()); 
//			line_value = line_value + mercator_lonlat.getX() + "," 
//					+ mercator_lonlat.getY() + ",0  "; 
			line_value=line_value +tmp_lonlat.getX() + "," + tmp_lonlat.getY() + ",0  "; 
			mapx = original_xy.getX() + bigx * (odd_flg % 2) + smallx * ((odd_flg + 1) % 2); 
			tmp_lonlat = ConverterUTM.UTMtoLL(23,mapx, mapy, UTMZone); 
//			mercator_lonlat = MapFunction.MercatorProject(tmp_lonlat.getX(), 
//					tmp_lonlat.getY()); 
//			line_value = line_value + mercator_lonlat.getX() + "," 
//					+ mercator_lonlat.getY() + ",0  "; 
			line_value=line_value +tmp_lonlat.getX() + "," + tmp_lonlat.getY() + ",0  "; 
			tmp_y = tmp_y + yinterval; 
			odd_flg++; 
		} 
		// rollback 
		double mapy2 = original_xy.getY() + bigy; 
		double mapx2 = original_xy.getX() + smallx; 
		Coordinate tmp_lonlat2 = ConverterUTM.UTMtoLL(23,mapx2, mapy2, UTMZone); 
//		mercator_lonlat = MapFunction.MercatorProject(tmp_lonlat2.getX(), 
//				tmp_lonlat2.getY()); 
//		line_value = line_value + mercator_lonlat.getX() + "," 
//				+ mercator_lonlat.getY() + ",0  "; 
		line_value=line_value +tmp_lonlat2.getX() + "," + tmp_lonlat2.getY() + ",0  "; 
		mapx2 = original_xy.getX() + bigx; 
		tmp_lonlat2 = ConverterUTM.UTMtoLL(23,mapx2, mapy2, UTMZone); 
//		mercator_lonlat = MapFunction.MercatorProject(tmp_lonlat2.getX(), 
//				tmp_lonlat2.getY()); 
//		line_value = line_value + mercator_lonlat.getX() + "," 
//				+ mercator_lonlat.getY() + ",0  "; 
		line_value=line_value +tmp_lonlat2.getX() + "," + tmp_lonlat2.getY() + ",0  ";
		mapx2 = original_xy.getX() + smallx;
		tmp_lonlat2 = ConverterUTM.UTMtoLL(23,mapx2, mapy2, UTMZone); 
//		mercator_lonlat = MapFunction.MercatorProject(tmp_lonlat2.getX(), 
//				tmp_lonlat2.getY()); 
//		line_value = line_value + mercator_lonlat.getX() + "," 
//				+ mercator_lonlat.getY() + ",0  "; 
		line_value=line_value +tmp_lonlat2.getX() + "," + tmp_lonlat2.getY() + ",0  "; 
 
		newline.setCoordinates(line_value); 
		mark1.addLineString(newline); 
		container.addPlacemark(mark1); 
	} 
 
	public void setPointPlacemark(String folderName) { 
		Folder container = new Folder(); 
		if (!folderName.equals("") && !folderName.equals("null")) { 
			// create and add another folder to root 
			container.setName(folderName); 
			container.setDescription("This is a folder contained by the root"); 
			root.addFolder(container); 
		} else { 
			container = root; 
		} 
		set360IconStyle(serverUrl+"/icon/base_green",".png","pointstyle"); 
		 
		//set360IconStyle(container,"http://156.56.104.146/maps/"+"/icon/base_green",".png","pointstyle");
		System.out.println("datalist.length = " + datalist.length);
		for (int i = 0; i < datalist.length; i++) { 
			// create and add a Placemark containing a Point 
			Placemark mark1 = new Placemark(); 
			mark1.setName(datalist[i].getFolderTag() + i); 
			double rads; 
			int degs; 
			double dx = Double.valueOf(datalist[i].getDeltaXValue()).doubleValue(); 
			double dy = Double.valueOf(datalist[i].getDeltaYValue()).doubleValue(); 
			double x = Double.valueOf(datalist[i].getX()).doubleValue(); 
			double y = Double.valueOf(datalist[i].getY()).doubleValue(); 
			// Obtain angle in degrees from user 
			degs = (int) Math.toDegrees(Math.atan2(dx, dy)); 
			degs = (360 + degs) % 360; 
			// Convert degrees to radian 
			rads = Math.toRadians((double) degs); 
			double length = Math.sqrt(dx * dx + dy * dy);
			String UTMZone=ConverterUTM.getUTMZone(23, original_lon, original_lat); 
			Coordinate original_xy = ConverterUTM.LLtoUTM(23,original_lon, original_lat, UTMZone); 
			double tmp_x = original_xy.getX() + x * coordinateUnit; 
			double tmp_y = original_xy.getY() + y * coordinateUnit; 
			Coordinate new_lonlat = ConverterUTM.UTMtoLL(23, tmp_x, tmp_y, UTMZone); 
			double lat = new_lonlat.getY(); 
			double lon = new_lonlat.getX(); 
 
			String descriptionValue = "<![CDATA[ <br/> <hr/>"; 
			descriptionValue = descriptionValue 
					+ "<font color=blue>lat: </font>" + lat + "<br/>"; 
			descriptionValue = descriptionValue 
					+ "<font color=blue>lon: </font>" + lon + "<br/>"; 
			descriptionValue = descriptionValue 
					+ "<font color=blue>length: </font>" + length + "cm <br/>"; 
			descriptionValue = descriptionValue 
					+ "<font color=blue>degrees: </font>" + degs + "<br/>"; 
			descriptionValue = descriptionValue + fontStart+datalist[i].getDeltaXName() 
					+ ": " +fontEnd+ datalist[i].getDeltaXValue() + "<br/>"; 
			descriptionValue = descriptionValue + fontStart+datalist[i].getDeltaYName() 
					+ ": " +fontEnd+ datalist[i].getDeltaYValue() + "<br/>"; 
			descriptionValue = descriptionValue + fontStart+datalist[i].getDeltaZName() 
					+ ": " +fontEnd+ datalist[i].getDeltaZValue() + "<br/>"; 
			descriptionValue = descriptionValue 
					+ "<font color=blue>tag name: </font>" 
					+ datalist[i].getFolderTag(); 
			descriptionValue = descriptionValue + "<br/>]]>"; 
			mark1.setDescription(descriptionValue); 
 
			mark1.setStyleUrl("#pointstyle" + degs); 
			Point point1 = new Point(); 
			point1.setCoordinates(lon + ", " + lat + ", " + "0"); // NA will 
																	// go to 0.0 
			mark1.addPoint(point1); 
			container.addPlacemark(mark1); 
		} 
 	} 
	 
	public void setFaultPlot(String folderName, String faultName, String lonstart, String latstart, String lonend, String latend, String LineColor, double LineWidth){
		
		Folder container = new Folder(); 
		if (!folderName.equals("") && !folderName.equals("null")) { 
			// create and add another folder to root 
			container.setName(folderName); 
			container.setDescription("This is a folder contained by the root"); 
			root.addFolder(container); 
		} else { 
			container = root; 
		} 

		String linestyleid=faultName+"Style"; 
		//We will put the styling information in the root folder
		setLineStyle(root, linestyleid, LineColor, LineWidth); 

		Placemark mark1 = new Placemark(); 
		mark1.setName(faultName); 
		 
		LineString newline = new LineString(); 
		String line_value = "";		 
		 
		// plot start point 
		Coordinate tmp_lonlat ; 
		tmp_lonlat = new Coordinate(Double.valueOf(lonstart).doubleValue(), Double.valueOf(latstart).doubleValue()); 

		line_value = line_value + tmp_lonlat.getX() + "," + tmp_lonlat.getY() + ",0  "; 
		//Set the fault description in the popup window.
		String descriptionValue= cdataStart+fontStart+"Starting lat: "+fontEnd+tmp_lonlat.getY()+" "+br; 
		descriptionValue=descriptionValue + fontStart+"Starting lon: "+fontEnd+tmp_lonlat.getX()+" "+br; 
		// plot end point 
		tmp_lonlat = new Coordinate(Double.valueOf(lonend).doubleValue(), Double.valueOf(latend).doubleValue()); 
		line_value = line_value + tmp_lonlat.getX() + "," 
				+ tmp_lonlat.getY() + ",0  ";	 
		descriptionValue=descriptionValue + fontStart+"Ending lat: "+fontEnd+tmp_lonlat.getY()+" "+br; 
		descriptionValue=descriptionValue + fontStart+"Ending lon: "+fontEnd+tmp_lonlat.getX()+" "+br; 
		descriptionValue+=cdataEnd; 
 
		mark1.setDescription(descriptionValue); 
		mark1.setStyleUrl("#" + linestyleid); 
		//		mark1.setStyleUrl(linestyleid); 
		newline.setCoordinates(line_value); 
		mark1.addLineString(newline); 
		container.addPlacemark(mark1);		 
	} 
	 
	 public void setArrowPlacemark(String folderName, String LineColor, double LineWidth) { 
		  setArrowPlacemark(folderName, LineColor, LineWidth, defaultArrowScale); 
	 }
	 
	 public void setArrowPlacemark(String folderName, String LineColor, double LineWidth, double arrowScale) {
		 
			double longestlength = 0.;
			double projectMinX=Double.valueOf(datalist[0].getX());
			double projectMaxX=Double.valueOf(datalist[0].getX());
			double projectMinY=Double.valueOf(datalist[0].getY());
			double projectMaxY=Double.valueOf(datalist[0].getY());
			
			System.out.println("[SimplexDataKml/setArrowPlacemark] datalist.length : " + datalist.length);
			for (int i = 0; i < datalist.length; i++) {
				
				double x=Double.valueOf(datalist[i].getX());
				double y=Double.valueOf(datalist[i].getX());
				if(x<projectMinX) projectMinX=x;
				if(x>projectMaxX) projectMaxX=x;
				if(y<projectMinY) projectMinY=y;
				if(y>projectMaxY) projectMaxY=y;
				
				double dx = Double.valueOf(datalist[i].getDeltaXValue()).doubleValue(); 
				double dy = Double.valueOf(datalist[i].getDeltaYValue()).doubleValue();			 
				double length = Math.sqrt(dx * dx + dy * dy);
				
				// System.out.println("[SimplexDataKml/setArrowPlacemark] dx : " + dx);
				// System.out.println("[SimplexDataKml/setArrowPlacemark] dy : " + dy);
				
				
				if (i == 0)
					longestlength = length; 
				
				else if (length > longestlength)
					longestlength = length; 
			}
			System.out.println("[SimplexDataKml/setArrowPlacemark] longestlength : " + longestlength);			
			
			double projectLength=(projectMaxX-projectMinX)*(projectMaxX-projectMinX);
			projectLength+=(projectMaxY-projectMinY)*(projectMaxY-projectMinY);
			projectLength=Math.sqrt(projectLength);
			
			//We arbitrarly set the longest displacement arrow to be 10% of the 
			//project dimension.
			double scaling = 0.7*projectLength/longestlength;
			
			System.out.println("[SimplexDataKml/setArrowPlacemark] projectLength : " + projectLength);			
			
			setArrowPlacemarkProcess(folderName, LineColor, LineWidth, arrowScale, longestlength, scaling);			
	 }
	 
	 public void setArrowPlacemarkProcess(String folderName, 
													  String LineColor, 
													  double LineWidth, 
													  double longestlength, 
													  double scaling) {
		  setArrowPlacemarkProcess(folderName, LineColor, LineWidth, arrowScale, longestlength, scaling);		 
	 }
	 
	 public void setArrowPlacemarkProcess(String folderName, 
													  String LineColor, 
													  double LineWidth, 
													  double arrowScale, 
													  double longestlength, 
													  double scaling) {
		  
		  System.out.println("[SimpleXDataKml/setArrowPlacemark] started");
		  Folder container = new Folder();
		  if (!folderName.equals("") && !folderName.equals("null")) { 
				
				container.setName(folderName); 
				container.setDescription("This is a folder contained by the root"); 
				root.addFolder(container); 
		  } else {
				container = root; 
		  }
		  
		  //REVIEW: This ID should be a passed in method parameter, but this is 
		  //hard to easily (must change out the client stubs and who knows what else
		  //will break.  Setting the linestyle to the LineColor is a shameful workaround.
		  String linestyleid=LineColor;
		  
		  Style gridlineStyle = new Style();
		  gridlineStyle.setId(linestyleid);
		  LineStyle newlineStyle = new LineStyle();
		  newlineStyle.setWidth((float) (LineWidth));
		  newlineStyle.setColor(LineColor);
		  gridlineStyle.addLineStyle(newlineStyle);
		  // curfolder.addStyle(gridlineStyle);
		  
			PolyStyle ps = new PolyStyle();
			ps.setFill(true);
			ps.setColor(LineColor);
			
			gridlineStyle.addPolyStyle(ps);
			
			kmlDocument.addStyle(gridlineStyle);
			
			//		System.out.println("Scale rate: "+scaling+" "+longestlength+" "+projectLength);
			
			System.out.println("[SimplexDataKml/setArrowPlacemark] the size of the datalist : " 
									 + datalist.length);
			
			//"Decimation factor" keeps the number of arrows under control.
			//Integer division used here on purpose. 
			//			decimationFactor=datalist.length/100+1;
			//			decimationFactor=1;
			//System.out.println("Decimation factor:"+decimationFactor);
			for (int i = 0; i < datalist.length; i++) { 
				 // create and add a Placemark containing a Point 
				 Placemark mark1 = new Placemark();			 
				 mark1.setName(datalist[i].getFolderTag() + i); 
				 double rads; 
				 int degs; 
				double dx = Double.valueOf(datalist[i].getDeltaXValue()) 
					 .doubleValue(); 
				double dy = Double.valueOf(datalist[i].getDeltaYValue()) 
				.doubleValue(); 
				double x = Double.valueOf(datalist[i].getX()).doubleValue(); 
				double y = Double.valueOf(datalist[i].getY()).doubleValue(); 
				// Obtain angle in degrees from user 
				degs = (int) Math.toDegrees(Math.atan2(dx, dy)); 
				degs = (360 + degs) % 360; 
				// Convert degrees to radian 
				rads = Math.toRadians((double) degs); 
				
				double length = Math.sqrt(dx * dx + dy * dy);         ////////  
				
				//Create the popup description of the point
				String br="<br/>"; 
				String fontStart="<font color=blue>"; 
				String fontEnd="</font>"; 
				
				//Point origin
				double lon=x/factor(original_lon, original_lat)+original_lon;
				double lat=y/111.32+original_lat;
				
				String descriptionValue = "<![CDATA["; 
				descriptionValue = descriptionValue 
				+ "<font color=blue>lat: </font>" + lat + ""+br; 
				descriptionValue = descriptionValue 
				+ "<font color=blue>lon: </font>" + lon + ""+br; 
				descriptionValue = descriptionValue 
				+ "<font color=blue>length: </font>" + length + " cm "+br; 
				descriptionValue = descriptionValue 
				+ "<font color=blue>degree: </font>" + degs + ""+br; 
				
				descriptionValue = descriptionValue + fontStart+datalist[i].getDeltaXName() 
				+ ": " +fontEnd+ datalist[i].getDeltaXValue() + " cm <br/>"; 
				descriptionValue = descriptionValue + fontStart+datalist[i].getDeltaYName() 
				+ ": " +fontEnd+ datalist[i].getDeltaYValue() + " cm <br/>"; 
				descriptionValue = descriptionValue + fontStart+datalist[i].getDeltaZName() 
				+ ": " +fontEnd+ datalist[i].getDeltaZValue() + " cm <br/>"; 
				descriptionValue = descriptionValue + "<font color=blue>scale rate </font>" 
				+ ":" +fontEnd+ longestlength + "cm  : " + scaling + "km <br/>";			 
				descriptionValue = descriptionValue 
				+ "<font color=blue>tag name:</font>" 
				+ datalist[i].getFolderTag()+br; 
				descriptionValue = descriptionValue + "]]>"; 
				mark1.setDescription(descriptionValue); 
				
				LineString newline = new LineString();
	
				mark1.setStyleUrl("#"+linestyleid);
				
				double startx = x;
				double starty = y;
				
				// System.out.println("[SimplexDataKml/setArrowPlacemark] x : " + x);
				// System.out.println("[SimplexDataKml/setArrowPlacemark] y : " + y);
				
				double endx = startx + dx*scaling;
				double endy = starty + dy*scaling;
				
				// System.out.println("[SimplexDataKml/setArrowPlacemark] endx = startx + dx*scaling / " + endx + " = " + startx + " + " + dx + "*" + scaling);
				// System.out.println("[SimplexDataKml/setArrowPlacemark] endy = starty + dy*scaling / " + endy + " = " + starty + " + " + dy + "*" + scaling);
				
				ArrowLine curarrow = CreateArrowByCoordinate(startx,starty,endx,endy); 
				//			System.out.println("Plotting: "+startx+" "+starty+" "+endx+" "+endy);
				
				//Plot the arrow's base relative to the project's origin.
				double arrowOrigX = curarrow.getStartPoint().getX(); 
				double arrowOrigY = curarrow.getStartPoint().getY();
				double arrowLonStart=arrowOrigX/factor(original_lon, original_lat)+original_lon;
				double arrowLatStart=arrowOrigY/111.32+original_lat;
				
				//Plot the arrow's ending point (head) relative to the project
				//origin.
				double arrowEndX = curarrow.getEndPoint().getX(); 
				double arrowEndY = curarrow.getEndPoint().getY();
				double arrowLonEnd=arrowEndX/factor(original_lon, original_lat)+original_lon;
				double arrowLatEnd=arrowEndY/111.32+original_lat;
				
				/*
				System.out.println("[SimplexDataKml/setArrowPlacemark] original_lat : " + original_lat);
				System.out.println("[SimplexDataKml/setArrowPlacemark] original_lon : " + original_lon);			
				System.out.println("[SimplexDataKml/setArrowPlacemark] curarrow.getEndPoint().getX() : " + curarrow.getEndPoint().getX());
				System.out.println("[SimplexDataKml/setArrowPlacemark] curarrow.getEndPoint().getY() : " + curarrow.getEndPoint().getY());
				*/
				
				//Plot the arrow. These are just polylines 
				String line_value = arrowLonStart+","+arrowLatStart+",0 ";
				line_value+=arrowLonEnd+","+arrowLatEnd+",0 ";
				newline.setCoordinates(line_value); 
				// mark1.addLineString(newline); 
				
				// Draw the arrow head by a polygon.
				
				Polygon p = new Polygon();			
				
				p.setExtrude(true);
				innerBoundaryIs ob = new innerBoundaryIs();
				
				LinearRing lr = new LinearRing();
				
				//Plot the arrow tails
				double arrowTail1X = curarrow.getArrowTail1().getX(); 
				double arrowTail1Y = curarrow.getArrowTail1().getY();
				double arrowLonTail1=arrowTail1X/factor(original_lon, original_lat)+original_lon;
				double arrowLatTail1=arrowTail1Y/111.32+original_lat;
	
				double arrowTail2X = curarrow.getArrowTail2().getX(); 
				double arrowTail2Y = curarrow.getArrowTail2().getY();
				double arrowLonTail2=arrowTail2X/factor(original_lon, original_lat)+original_lon;
				double arrowLatTail2=arrowTail2Y/111.32+original_lat;
	
				//Plot the arrow. These are just polylines
				/*
				line_value = arrowLonEnd+","+arrowLatEnd+",0 ";
				line_value+=arrowLonTail1+","+arrowLatTail1+",0 ";
				line_value+=arrowLonTail2+","+arrowLatTail2+",0 ";
				*/
				
				
				/*
				 * The below part drawing the arrow head referred to UNAVCO's arrows map
				 * <!-- Copyright (C) 2009 UNAVCO , Inc. Boulder Colorado --> */
				
				double h = Math.sqrt(dx*dx + dy*dy);
				//				double dl = 0.15142;
				double dl = 0.05;
				double fracx = dx/h;
				double fracy = dy/h;
				double merccorr = Math.cos(arrowLatStart * 3.141592/180.0); 
				double headerLonEnd = arrowLonEnd + dl*fracx;
				double headerLatEnd = arrowLatEnd + dl*fracy;
				double headerLonP1 = (arrowLonEnd)-(dl*fracy / 4.0);
				double headerLatP1 = (arrowLatEnd)+( (dl*fracx / 4.0) *merccorr);    
				double headerLonP2 = (arrowLonEnd)+(dl*fracy / 4.0);
				double headerLatP2 = (arrowLatEnd)-( (dl*fracx / 4.0) *merccorr);			
				String space=" ";
				// System.out.println("Arrow Head:"+arrowLonEnd+space+arrowLatEnd+space+h+space+fracx+space+fracy+space+merccorr);
				// System.out.println(dl*fracx+space+dl*fracy+space+dl*fracy/4.0+space+(dl*fracx/4.0)*merccorr);
				
				// until here
				
				line_value = headerLonEnd+","+headerLatEnd +",0 ";
				line_value+=headerLonP1+","+headerLatP1+",0 ";
				line_value+=headerLonP2+","+headerLatP2+",0 ";
				
				MultiGeometry mg = new MultiGeometry();
				mg.addLineString(newline);
				mg.addPolygon(p);
				
				mark1.addMultiGeometry(mg);
				
				lr.setCoordinates(line_value);
				ob.addLinearRing(lr);
				p.addOuterBoundaryIs(ob);
				// mark1.addPolygon(p);
				container.addPlacemark(mark1);
				
			}
			
			System.out.println("[SimplexDataKml/setArrowPlacemark] Finished");
	 }
 
	private ArrowLine CreateArrowByCoordinate(double startx, double starty, 
			double endx, double endy) { 
		ArrowLine myArrow = new ArrowLine(); 
 
		double dx = endx - startx; 
		double dy = endy - starty; 
		double length = Math.sqrt(dx * dx + dy * dy); 
		double rads; 
		int degs; 
		// Obtain angle in degrees from user 
		degs = (int) Math.toDegrees(Math.atan2(dx, dy)); 
		degs = (360 + degs) % 360; 
 
		// Convert degrees to radian 
		rads = Math.toRadians((double) degs); 
 
		double arrowlength = length * 0.08; 
 
		int arrowdegs = 30; 
		double arrowrads = Math.toRadians((double) arrowdegs); 
		double LeftArrowX = -arrowlength * Math.sin(arrowrads); 
		double LeftArrowY = length - arrowlength * Math.cos(arrowrads); 
		double RightArrowX = +arrowlength * Math.sin(arrowrads); 
		double RightArrowY = length - arrowlength * Math.cos(arrowrads); 
		int offsetdegs = (int) Math.toDegrees(Math 
				.atan2(LeftArrowX, LeftArrowY)); 
		double newlinelength = Math.sqrt(LeftArrowX * LeftArrowX + LeftArrowY 
				* LeftArrowY); 
		double newleftrads = Math.toRadians((int) (degs + offsetdegs)); 
		double newrightrads = Math.toRadians((int) (degs - offsetdegs)); 
		LeftArrowX = startx + newlinelength * Math.sin(newleftrads); 
		LeftArrowY = starty + newlinelength * Math.cos(newleftrads); 
		RightArrowX = startx + newlinelength * Math.sin(newrightrads); 
		RightArrowY = starty + newlinelength * Math.cos(newrightrads); 
		myArrow.setStartPoint(new Coordinate(startx, starty)); 
		myArrow.setEndPoint(new Coordinate(endx, endy)); 
		myArrow.setArrowTail1(new Coordinate(LeftArrowX, LeftArrowY)); 
		myArrow.setArrowTail2(new Coordinate(RightArrowX, RightArrowY)); 
 
		return myArrow; 
	} 
 
	/** 
	 * This method uses Xerces specific classes prints the XML document to 
	 * console. 
	 */ 
	public void DocPrintToConsole(org.w3c.dom.Document dom) { 
		try { 
 
			// print 
			OutputFormat format = new OutputFormat(dom); 
			format.setIndenting(true); 
 
			// to generate output to console use this serializer 
			XMLSerializer serializer = new XMLSerializer(System.out, format); 
 
			serializer.serialize(dom); 
 
		} catch (IOException ie) { 
			ie.printStackTrace(); 
		} 
	} 
 
	/** 
	 * This method uses Xerces specific classes prints the XML document to file. 
	 */ 
	public void DocPrintToFile(org.w3c.dom.Document dom, String xmlfilename) { 
		try { 
			// print 
			OutputFormat format = new OutputFormat(dom); 
			format.setIndenting(true); 
 
			// to generate a file output use fileoutputstream instead of 
			// system.out 
			XMLSerializer serializer = new XMLSerializer(new FileOutputStream( 
					new File(xmlfilename)), format); 
 
			serializer.serialize(dom); 
 
		} catch (IOException ie) { 
			ie.printStackTrace(); 
		} 
	} 
	 
	 /** 
	  * This method uses Xerces specific classes prints the XML document to file. 
	  */ 
	 public void printToFile(String detail, String xmlfilename) { 
		  // System.out.println("[SimplexDataKml/printToFile] Printing a file");
		  try { 
				System.out.println("[SimplexDataKml/printToFile] Printing KML to "+xmlfilename + detail);
				PrintStream out = new PrintStream(new FileOutputStream(xmlfilename));				
				out.println(detail);
				out.close(); 
		  }
		  
		  catch (FileNotFoundException e) { 
				e.printStackTrace(); 
		  } 
		  catch(Exception ex){
				ex.printStackTrace();
		  }
	 } 
 
	public void setLonref(String tmp_str) { 
		this.lonref = Double.valueOf(tmp_str.trim()).doubleValue(); 
	} 
 
	public void setLatref(String tmp_str) { 
		this.latref = Double.valueOf(tmp_str.trim()).doubleValue(); 
	} 
 
	public void setCoordinateUnit(String Unit) { 
		this.coordinateUnit = Double.valueOf(Unit.trim()).doubleValue(); 
	} 
 
	public void setOriginal_lat(String tmp_str) { 
		this.original_lat = Double.valueOf(tmp_str.trim()).doubleValue(); 
	} 
 
	public void setOriginal_lon(String tmp_str) { 
		this.original_lon = Double.valueOf(tmp_str.trim()).doubleValue(); 
	} 
	 
	public void setOriginalCoordinate(String lon, String lat  ) { 
		setOriginal_lon(lon); 
		setOriginal_lat(lat); 
	} 
	 
	public void setRefCoordinate(String lon, String lat ) { 
		setLonref(lon); 
		setLatref(lat); 
	} 
	 
	public String generateBaseUrl(String foldertag, String userName, String projectName, String timeStamp) { 
 
		// Need to be careful here because this must follow 
		// the workDir convention also. 
		String baseUrl = serverUrl + "/" + foldertag +"/" + userName + "/" + projectName + "/" 
				+ "/" + timeStamp; 
 
		return baseUrl; 
	} 
	 
	/** 
	 *  
	 */ 
	protected String generateOutputDestDir(String foldertag, String userName, String projectName, String timeStamp) { 
 
		String outputDestDir = baseOutputDestDir + File.separator + foldertag + File.separator + userName + File.separator + projectName + File.separator + timeStamp; 
 
		return outputDestDir; 
 
	} 
 
	protected void makeWorkDir(String workDir) throws Exception { 
 
		System.out.println("[SimplexDataKml/makeWorkDir] Working Directory is " + workDir); 
		new File(workDir).mkdirs(); 
	} 
	 
	 protected double factor(double refLon, double refLat) {
		  double d2r = Math.acos(-1.0) / 180.0;
		  double flatten=1.0/298.247;
		  
		  double theFactor = d2r* Math.cos(d2r * refLat)
				* 6378.139 * (1.0 - Math.sin(d2r * refLon) * Math.sin(d2r * refLon) * flatten);
		  
		  return theFactor;
	 }
	 
	 /**
	  * This is called by the main() for testing.
	  */ 
	 private static void doTest() {

		 SimpleXDataKml test = new SimpleXDataKml(true); 
		 PointEntry[] pointEntryList=new PointEntry[4]; 
		 pointEntryList[0]=new PointEntry(); 
		 pointEntryList[0].setX("-10.0"); 
		 pointEntryList[0].setY("-10.0"); 
		 pointEntryList[0].setDeltaXName("dx"); 
		 pointEntryList[0].setDeltaXValue("-10"); 
		 pointEntryList[0].setDeltaYName("dy"); 
		 pointEntryList[0].setDeltaYValue("-100"); 
		 pointEntryList[0].setDeltaZName("dz"); 
		 pointEntryList[0].setDeltaZValue("0"); 
		 pointEntryList[0].setFolderTag("Arrow"); 
		  
		 pointEntryList[1]=new PointEntry(); 
		 pointEntryList[1].setX("-30.0"); 
		 pointEntryList[1].setY("-27.0"); 
		 pointEntryList[1].setDeltaXName("dx"); 
		 pointEntryList[1].setDeltaXValue("45"); 
		 pointEntryList[1].setDeltaYName("dy"); 
		 pointEntryList[1].setDeltaYValue("10"); 
		 pointEntryList[1].setDeltaZName("dz"); 
		 pointEntryList[1].setDeltaZValue("2"); 
		 pointEntryList[1].setFolderTag("Arrow"); 
		  
		 pointEntryList[2]=new PointEntry(); 
		 pointEntryList[2].setX("-86.0"); 
		 pointEntryList[2].setY("-120.0"); 
		 pointEntryList[2].setDeltaXName("dx"); 
		 pointEntryList[2].setDeltaXValue("87"); 
		 pointEntryList[2].setDeltaYName("dy"); 
		 pointEntryList[2].setDeltaYValue("35"); 
		 pointEntryList[2].setDeltaZName("dz"); 
		 pointEntryList[2].setDeltaZValue("-1"); 
		 pointEntryList[2].setFolderTag("Arrow"); 
		  
		 pointEntryList[3]=new PointEntry(); 
		 pointEntryList[3].setX("16.0"); 
		 pointEntryList[3].setY("36.0"); 
		 pointEntryList[3].setDeltaXName("dx"); 
		 pointEntryList[3].setDeltaXValue("68"); 
		 pointEntryList[3].setDeltaYName("dy"); 
		 pointEntryList[3].setDeltaYValue("12"); 
		 pointEntryList[3].setDeltaZName("dz"); 
		 pointEntryList[3].setDeltaZValue("0"); 
		 pointEntryList[3].setFolderTag("Arrow"); 
  
		 test.setDatalist(pointEntryList); 
		 test.setOriginalCoordinate("-115.72","30.25"); 
		 test.setCoordinateUnit("1000"); 
		 //		 test.setGridLine("fault folder", -117.35, 32.59, -115.35, 30.59, 1, 1); 
		 // test.setFaultPlot("fault folder", "test fault1", "-117.35", "32.59", "-115.35", "30.59", "641478DC", 3); 
		 test.setArrowPlacemark("Arrowed","B22222FF", 0.95); 
		 // test.setArrowPlacemark("Arrowed","FF191970", 3); 
		 // test.setPointPlacemark("point"); 
 
		 test.printToFile(test.doc.toKML(),"test_original.kml"); 
		 System.out.println("end");	 

	 }
} 
