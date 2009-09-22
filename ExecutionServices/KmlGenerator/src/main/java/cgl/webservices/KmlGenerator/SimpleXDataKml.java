/* 
 GmapDataXml generator. 
 */ 
 
 
// Modified by Jun Ji of CGL, May 2009		email : jid@cs.indiana.edu 
 
package cgl.webservices.KmlGenerator; 
 
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
 
 
 
import com.keithpower.gekmlib.Folder; 
import com.keithpower.gekmlib.Kml; 
import com.keithpower.gekmlib.LineString; 
import com.keithpower.gekmlib.Placemark; 
import com.keithpower.gekmlib.Point; 
import com.keithpower.gekmlib.Style; 
import com.keithpower.gekmlib.LineStyle; 
import com.keithpower.gekmlib.IconStyle; 
import com.keithpower.gekmlib.Icon; 
import com.keithpower.gekmlib.vec2; 
import com.keithpower.gekmlib.Node; 
import com.keithpower.gekmlib.Document; 
 
import com.sun.org.apache.xml.internal.serialize.OutputFormat; 
import com.sun.org.apache.xml.internal.serialize.XMLSerializer; 
 
/** 
 * Make up and write an XML document, using DOM UPDATED FOR JAXP. 
 */ 
public class SimpleXDataKml { 
 
	// private Document doc; 
 
	private double original_lat = 0; 
	private double original_lon = 0; 
 
	private double coordinateUnit = 1000.0; 
 
	private double latref = 0; 
	private double lonref = 0; 
 
	private Kml doc = new Kml(); 
	 
	private Folder root = new Folder(); 
	 
	private Document kmlDocument=new Document(); 
 
	private PointEntry[] datalist = null; 
	 
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
		  
		  
		  
		 test.setGridLine("fault folder", -117.35, 32.59, -115.35, 30.59, 1, 1); 
		 test.setFaultPlot("fault folder", "test fault1", "-117.35", "32.59", "-115.35", "30.59", "641478DC", 3); 
 
		  
		 test.setArrowPlacemark("Arrowed","5f1478DC", 3); 
		 // test.setArrowPlacemark("Arrowed","641478DC", 3); 
		 // test.setPointPlacemark("point"); 
 
		 test.printToFile(test.doc.toKML(),"test_original.kml"); 
	}

 
	  
	 public void setDatalist(PointEntry[] InputDataList) { 
		 datalist = InputDataList; 
	 } 
	  
	 public String runMakeKml (String ServerTag,String UserName, String ProjectName, String JobUID) { 
		  
		 String destDir=generateOutputDestDir(ServerTag,UserName,ProjectName,JobUID); 
		 String baseUrl=generateBaseUrl(ServerTag,UserName,ProjectName,JobUID); 
		  
		 try{ 
			 makeWorkDir(destDir);			  
		 } 
		  
		 catch (Exception e) { 
			 e.printStackTrace();			  
		 } 
		  
		 this.printToFile(this.doc.toKML(), destDir + "/" + ProjectName + JobUID +".kml"); 
		 return baseUrl + "/" + ProjectName + JobUID +".kml";		  
	 } 
	  
	 // 
	 public PointEntry[] LoadDataFromFile(String InputFileName) { 
		 ArrayList dataset = new ArrayList(); 
		  
		 try {			  
			 String line = new String(); 
			 int skipthreelines = 1; 
			 BufferedReader in = new BufferedReader(new FileReader(InputFileName)); 
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
		 System.out.println("----------------------------------"); 
		 System.out.println("Initializing KML service."); 
		 System.out.println("----------------------------------"); 
 
		 try { 
			  if(useClassLoader) { 
					 
					System.out.println("Using classloader"); 
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
					System.out.println("Using Servlet Context"); 
					MessageContext msgC = MessageContext.getCurrentContext(); 
					ServletContext context = ((HttpServlet) msgC 
													  .getProperty(HTTPConstants.MC_HTTP_SERVLET)) 
						 .getServletContext(); 
					String propertyFile = context.getRealPath("/") 
						 + "/WEB-INF/classes/kmlgenerator.properties"; 
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
		 root.setName("root Folder"); 
		 root.setDescription("This is the root folder"); 
		 kmlDocument.addFolder(root); 
		 doc.addDocument(kmlDocument); 
	} 
 
	public void setLineStyle(Folder curfolder, String id, String Color_value, double line_width) { 
		 Style gridlineStyle = new Style(); 
		 gridlineStyle.setId(id); 
		 LineStyle newlineStyle = new LineStyle(); 
		 newlineStyle.setWidth((float) (line_width)); 
		 newlineStyle.setColor(Color_value); 
		 gridlineStyle.addLineStyle(newlineStyle); 
		 curfolder.addStyle(gridlineStyle); 
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
			container = root; 
		} 
		setLineStyle(container, "GridLineStyle", "ffcc00cc", 2); 
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
	 
	public void setFaultPlot(String folderName, 
									 String faultName, 
									 String lonstart, 
									 String latstart, 
									 String lonend, 
									 String latend, 
									 String LineColor, 
									 double LineWidth){ 
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
		//		String linestyleid=""; 
		setLineStyle(container, linestyleid, LineColor, LineWidth); 
		Placemark mark1 = new Placemark(); 
		mark1.setName(faultName); 
		 
		LineString newline = new LineString(); 
		String line_value = "";		 
		 
		// plot start point 
		Coordinate tmp_lonlat ; 
		tmp_lonlat = new Coordinate(Double.valueOf(lonstart).doubleValue(), Double.valueOf(latstart).doubleValue()); 
//		tmp_lonlat = MapFunction.MercatorProject(tmp_lonlat.getX(), 
//				tmp_lonlat.getY()); 
		line_value = line_value + tmp_lonlat.getX() + "," + tmp_lonlat.getY() + ",0  "; 
		String descriptionValue= cdataStart+fontStart+"Starting lat: "+fontEnd+tmp_lonlat.getY()+" "+br; 
		descriptionValue=descriptionValue + fontStart+"Starting lon: "+fontEnd+tmp_lonlat.getX()+" "+br; 
		// plot end point 
		tmp_lonlat = new Coordinate(Double.valueOf(lonend).doubleValue(), Double.valueOf(latend).doubleValue()); 
//		tmp_lonlat = MapFunction.MercatorProject(tmp_lonlat.getX(), 
//				tmp_lonlat.getY()); 
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
		   
		Folder container = new Folder(); 
		if (!folderName.equals("") && !folderName.equals("null")) { 
			// create and add another folder to root 
			container.setName(folderName); 
			container.setDescription("This is a folder contained by the root"); 
			root.addFolder(container); 
		} else { 
			container = root; 
		} 
		 
		//This isn't done correctly so disable in anticipation of future brilliance. 
		String linestyleid="arrowedStyle"; 
			 //String linestyleid=""; 
		setLineStyle(container, linestyleid, LineColor, LineWidth); 
		 
		double longestlength = 0; 
		 
		for (int i = 0; i < datalist.length; i++) { 
			 
			double dx = Double.valueOf(datalist[i].getDeltaXValue()) 
			.doubleValue(); 
			double dy = Double.valueOf(datalist[i].getDeltaYValue()) 
			.doubleValue();			 
			double length = Math.sqrt(dx * dx + dy * dy); 
			 
			if (i == 0) 
				longestlength = length; 
			 
			else 
			{ 
				if (length > longestlength) 
					longestlength = length;				 
			} 
		}		 
		 
		double scale_rate = 0; 
		 
		if (longestlength != 0)			 
			scale_rate = (100 * arrowScale)/longestlength;		 
		 		 
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
			 
			 
			String UTMZone=ConverterUTM.getUTMZone(23, original_lon, original_lat); 
			Coordinate original_xy = ConverterUTM.LLtoUTM(23, original_lon, original_lat,UTMZone); 
			double tmp_x = original_xy.getX() + x * coordinateUnit; 
			double tmp_y = original_xy.getY() + y * coordinateUnit; 
			Coordinate new_lonlat = ConverterUTM.UTMtoLL(23, tmp_x, tmp_y, UTMZone);			 			 
//			new_lonlat = MapFunction.MercatorProject(new_lonlat.getX(), 
//					new_lonlat.getY()); 
			double lat = new_lonlat.getY(); 
			double lon = new_lonlat.getX(); 
			 
			String br="<br/>"; 
			String fontStart="<font color=blue>"; 
			String fontEnd="</font>"; 
 
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
			+ " :" +fontEnd+ longestlength + "cm  : " + arrowScale/1000 + "km <br/>";			 
			descriptionValue = descriptionValue 
					+ "<font color=blue>tag name:</font>" 
					+ datalist[i].getFolderTag()+br; 
			descriptionValue = descriptionValue + "]]>"; 
			mark1.setDescription(descriptionValue); 
 
			LineString newline = new LineString(); 
			mark1.setStyleUrl("#"+linestyleid); 
			String line_value = ""; 
			// double startx = original_xy.getX() + x * coordinateUnit; 
			// double starty = original_xy.getY() + y * coordinateUnit; 
 			// double endx = startx + dx * coordinateUnit; 
			// double endy = starty + dy * coordinateUnit; 
			// double endx = startx + dx*arrowScale; 
			// double endy = starty + dy*arrowScale; 
			 
			double startx = original_xy.getX() + x * coordinateUnit; 
			double starty = original_xy.getY() + y * coordinateUnit; 
			double endx = startx + dx * 0.01; 
			double endy = starty + dy * 0.01;			 
			 
			double x_sub_adj = (endx-startx)/2; 
			double y_sub_adj = (endy-starty)/2; 
			 			 
			/*   
			double x_sub_adj = 0; 
			double y_sub_adj = 0; 
			*/ 
			 
			// System.out.println("end : x = " + endx + " y = " + endy);			 
			// System.out.println("scale_rate : " + scale_rate); 
			 
			// endx = (double)(endx * scale_rate); 
			// endy = (double)(endy * scale_rate);		 
			 			 
			endx = startx + dx * 0.01 * scale_rate; 
			endy = starty + dy * 0.01 * scale_rate; 
			 
			// System.out.println("original_xy : x = " + original_xy.getX() + " y = " + original_xy.getY());			 
			// System.out.println("x = " + x + " y = " + y); 
			// System.out.println("tmp_xy : x = " + tmp_x + " y = " + tmp_y); 
			 
			 
			// System.out.println("start_xy : x = " + startx + " y = " + starty); 
			// System.out.println("end_xy : x = " + endx + " y = " + endy); 
			 
			// System.out.println("orignial lat, lon : x = " + original_lat + " y = " + original_lon); 
			// System.out.println("new lat, lon : x = " + lat + " y = " + lon);			 
			 
			double x_sub = (endx-startx)/2; 
			double y_sub = (endy-starty)/2; 
			 
			// System.out.println("sub : x = " + x_sub + " y = " + y_sub); 
			// System.out.println("sub_adj : x = " + x_sub_adj + " y = " + y_sub_adj); 
			 
			/* 
			double x_sub = 0; 
			double y_sub = 0; 
			*/ 
			 
			ArrowLine curarrow = CreateArrowByCoordinate(startx,starty,endx,endy); 

			// ArrowLine curarrow = CreateArrowByCoordinate(startx - x_sub + x_sub_adj, starty - y_sub + y_sub_adj, endx - x_sub + x_sub_adj, endy - y_sub + y_sub_adj); 
			 
			// ArrowLine curarrow = CreateArrowByCoordinate(startx - x_sub, starty - y_sub, endx - x_sub, endy - y_sub); 
			 
			// plot start point 
			double mapx = curarrow.getStartPoint().getX(); 
			double mapy = curarrow.getStartPoint().getY(); 
			Coordinate tmp_lonlat = ConverterUTM.UTMtoLL(23, mapx, mapy, UTMZone); 
//			tmp_lonlat = MapFunction.MercatorProject(tmp_lonlat.getX(), 
//					tmp_lonlat.getY()); 
			line_value = line_value + tmp_lonlat.getX() + "," 
					+ tmp_lonlat.getY() + ",0  "; 
			// plot end point 
			mapx = curarrow.getEndPoint().getX(); 
			mapy = curarrow.getEndPoint().getY(); 
			tmp_lonlat = ConverterUTM.UTMtoLL(23, mapx, mapy, UTMZone); 
//			tmp_lonlat = MapFunction.MercatorProject(tmp_lonlat.getX(), 
//					tmp_lonlat.getY()); 
			line_value = line_value + tmp_lonlat.getX() + "," 
					+ tmp_lonlat.getY() + ",0  "; 
			// plot arrow tail1 
			mapx = curarrow.getArrowTail1().getX(); 
			mapy = curarrow.getArrowTail1().getY(); 
			tmp_lonlat = ConverterUTM.UTMtoLL(23, mapx, mapy, UTMZone); 
//			tmp_lonlat = MapFunction.MercatorProject(tmp_lonlat.getX(), 
//					tmp_lonlat.getY()); 
			line_value = line_value + tmp_lonlat.getX() + "," 
					+ tmp_lonlat.getY() + ",0  "; 
			// roll back to end point 
			mapx = curarrow.getEndPoint().getX(); 
			mapy = curarrow.getEndPoint().getY(); 
			tmp_lonlat = ConverterUTM.UTMtoLL(23, mapx, mapy, UTMZone); 
//			tmp_lonlat = MapFunction.MercatorProject(tmp_lonlat.getX(), 
//					tmp_lonlat.getY()); 
			line_value = line_value + tmp_lonlat.getX() + "," 
					+ tmp_lonlat.getY() + ",0  "; 
			// plot arrow tail2 
			mapx = curarrow.getArrowTail2().getX(); 
			mapy = curarrow.getArrowTail2().getY(); 
			tmp_lonlat = ConverterUTM.UTMtoLL(23, mapx, mapy, UTMZone); 
//			tmp_lonlat = MapFunction.MercatorProject(tmp_lonlat.getX(), 
//					tmp_lonlat.getY()); 
			line_value = line_value + tmp_lonlat.getX() + "," 
					+ tmp_lonlat.getY() + ",0  "; 
			// roll back to end point 
			mapx = curarrow.getEndPoint().getX(); 
			mapy = curarrow.getEndPoint().getY(); 
			tmp_lonlat = ConverterUTM.UTMtoLL(23, mapx, mapy, UTMZone); 
//			tmp_lonlat = MapFunction.MercatorProject(tmp_lonlat.getX(), 
//					tmp_lonlat.getY()); 
			line_value = line_value + tmp_lonlat.getX() + "," 
					+ tmp_lonlat.getY() + ",0  "; 
 
			newline.setCoordinates(line_value); 
			 
			/* 
			Style gridlineStyle = new Style(); 
			// gridlineStyle.setId(id); 
			LineStyle newlineStyle = new LineStyle(); 
			 
			int w = 0; 
			 
			if (length >= 0 && length < 1) 
				w = 1; 
			else if (length >= 1 && length < 5) 
				w = 2; 
			else if (length >= 5 && length < 10) 
				w = 3; 
			else if (length >= 10 && length < 15) 
				w = 4; 
			else if (length >= 15 && length < 20) 
				w = 5; 
			else if (length >= 20 && length < 25) 
				w = 6; 
			else if (length >= 25 && length < 30) 
				w = 7; 
			else if (length >= 30 && length < 35) 
				w = 8; 
			else if (length >= 35 && length < 40) 
				w = 9; 
			else if (length >= 40) 
				w = 10;			 
			 
			newlineStyle.setWidth((float) w);			 
			gridlineStyle.addLineStyle(newlineStyle); 
			mark1.addStyle(gridlineStyle); 
			*/ 
			 
			 
			mark1.addLineString(newline); 
			 
			container.addPlacemark(mark1); 
		} 
	} 
 
	public ArrowLine CreateArrowByCoordinate(double startx, double starty, 
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
 
		double arrowlength = length * 0.22; 
 
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
 
		// System.out.println("degs:"+degs); 
		// System.out.println("arrowlength:"+arrowlength); 
		// System.out.println("Math.sin(arrowrads):"+Math.sin(arrowrads)); 
		// System.out.println("offsetdegs:"+offsetdegs); 
		// System.out.println("newlinelength:"+newlinelength); 
		// System.out.println("LeftArrowX:"+LeftArrowX); 
		// System.out.println("LeftArrowY:"+LeftArrowY); 
		// System.out.println("RightArrowX:"+RightArrowX); 
		// System.out.println("rightArrowY:"+RightArrowY); 
 
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
		try { 
			PrintStream out = new PrintStream(new FileOutputStream(xmlfilename)); 
			out.println(detail); 
 
			out.close(); 
 
		} catch (FileNotFoundException e) { 
			e.printStackTrace(); 
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
	 
	public String generateBaseUrl(String foldertag, String userName, String projectName, 
			String timeStamp) { 
 
		// Need to be careful here because this must follow 
		// the workDir convention also. 
		String baseUrl = serverUrl + "/" + foldertag +"/" + userName + "/" + projectName + "/" 
				+ "/" + timeStamp; 
 
		return baseUrl; 
	} 
	 
	/** 
	 *  
	 */ 
	protected String generateOutputDestDir(String foldertag,String userName, String projectName, 
			String timeStamp) { 
 
		String outputDestDir = baseOutputDestDir + File.separator + foldertag + File.separator + userName 
				+ File.separator + projectName + File.separator + timeStamp; 
 
		return outputDestDir; 
 
	} 
 
	private void makeWorkDir(String workDir) throws Exception { 
 
		System.out.println("Working Directory is " + workDir); 
		new File(workDir).mkdirs(); 
	} 
 
} 
