package cgl.webservices.geofest;

//Need to import this parent.
import cgl.webservices.*;

//Not explicitly naming these because they are famous.
import java.util.*;
import java.io.*;
import java.net.*;

import org.apache.tools.ant.Main;
import org.apache.log4j.*;

//Needed to get the ServletContext to read the properties.
import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

//SOPAC Client Stuff
import edu.ucsd.sopac.reason.grws.client.GRWS_SubmitQuery;

//Needed for some number formatting.
import java.text.*;

/**
 * A simple wrapper for Ant.
 */

public class GeoFESTService extends AntVisco implements Runnable{    
    static Logger logger=Logger.getLogger(GeoFESTService.class);

    final String FILE_PROTOCOL="file";
    final String HTTP_PROTOCOL="http";

    //These are the system properties that may have
    //default values.
    Properties properties;
    String serverUrl;
    String baseWorkDir;
    String baseDestDir;
    String outputDestDir;
    String projectName;
    String binPath;
    String buildFilePath;
    String antTarget;
    
    
    public GeoFESTService(boolean useClassLoader) 
		  throws Exception {

		  super();
		  
		  if(useClassLoader) {
				System.out.println("Using classloader");
				//This is useful for command line clients but does not work
				//inside Tomcat.
				ClassLoader loader=ClassLoader.getSystemClassLoader();
				properties=new Properties();
				
				//This works if you are using the classloader but not inside
				//Tomcat.
				properties.load(loader.getResourceAsStream("geofestconfig.properties"));
		  }
		  else {
				//Extract the Servlet Context
				System.out.println("Using Servlet Context");
				MessageContext msgC=MessageContext.getCurrentContext();
				ServletContext context=((HttpServlet)msgC.getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext();
				
				String propertyFile=context.getRealPath("/")
					 +"/WEB-INF/classes/geofestconfig.properties";
				System.out.println("Prop file location "+propertyFile);
				
				properties=new Properties();	    
				properties.load(new FileInputStream(propertyFile));
		  }
		  
		  serverUrl=properties.getProperty("geofest.service.url");
		  baseWorkDir=properties.getProperty("base.workdir");
		  baseDestDir=properties.getProperty("base.dest.dir");
		  projectName=properties.getProperty("project.name");
		  binPath=properties.getProperty("bin.path");
		  buildFilePath=properties.getProperty("build.file.path");
		  antTarget=properties.getProperty("ant.target");
	 }
    
    public GeoFESTService() throws Exception{
		  this(false);
    }
	 
	 /**
	  * This runs the mesh generator code in blocking mode,
	  * i.e., it does not return until the mesh is done.
	  */
	 public void runBlockingMeshGenerator(String projectDir,
													  String projectName,
													  Fault[] faults,
													  Layer[] layers) 
		  throws Exception {

		  createGeometryFiles(projectDir,projectName,faults,layers);
		  String[] args=setUpArgs();
		  //Methods from parent
		  setArgs(args);
		  run();
	 }
	 
	 /**
	  * Runs the meshgenerator in non-blocking mode, which
	  * is necessary for large meshes.
	  */
	 public String runNonBlockingMeshGenerator(String projectDir,
															 String projectName,
															 Fault[] faults,
															 Layer[] layers) 
		  throws Exception{

		  String ticket=generateTicket();
		  createGeometryFiles(projectDir,projectName,faults, layers);
		  String[] args=setUpArgs();
		  
		  //Methods from parent
		  setArgs(args);
		  execute();
		  return ticket;
	 }
	 
	 /**
	  * Checks the running Mesh Generator service.
	  * Useful in non-blocking execution.
	  */
	 public void queryMeshGeneratorStatus() 
		  throws Exception {
	 }
	 
	 /**
	  * This method is used to tar up the mesh and input files.
	  * This always runs in blocking mode.
	  */
	 public void runPackageGeoFESTFiles()
		  throws Exception {
	 }
	 
	 /**
	  * Actually runs GeoFEST.  Always runs in non-blocking mode.
	  */
	 public void runGeoFEST() 
		  throws Exception {
	 }

	 /**
	  * Checks the status of a running GeoFEST job.
	  */
	 public void queryGeoFESTStatus() 
		  throws Exception {
	 }


	 /**
	  * This method writes out all the input files
	  * that are needed for creating the mesh.
	  */
	 protected void createGeometryFiles(String projectDir,
												  String projectName,
												  Fault[] faults,
												  Layer[] layers) 
		  throws Exception {
		  
		  writeGroupFile(projectDir,projectName,faults,layers);
		  writeAllFaultParamFiles(projectDir,faults,layers[0]);
		  writeAllMaterialsFiles(projectDir,layers);
		  writeAllFaultOutputFiles(projectDir,faults,layers[0]);
		  writeAllLayerOutputFiles(projectDir,layers);
	 }

	 
    /**
     * This merges multiple files into a single file,
     * duplicating UNIX paste.
     */ 
    public void mergeInputFiles(String[] inputFileArray, 
										  String mergedFileName) {
		  
		  //Find the shortest of the input files.
		  int shortCount=Integer.MAX_VALUE;
		  System.out.println("Max integer Value="+shortCount);
		  
		  for(int i=0;i<inputFileArray.length;i++){
				int lineCount=getLineCount(inputFileArray[i]);
				if(lineCount < shortCount) shortCount=lineCount;
		  }
		  System.out.println("Shortest file length="+shortCount);	
		  
		  // Now do the thing.
		  try {
				//This is our output file.
				PrintWriter pw=
					 new PrintWriter(new FileWriter(mergedFileName),true);
				
				//Set up bufferedreader array
				BufferedReader[] br=new BufferedReader[inputFileArray.length];
				for(int i=0;i<br.length;i++){
					 br[i]=new BufferedReader(new FileReader(inputFileArray[i]));
				}
				
				//Loop over each line of the file
				for(int i=0;i<shortCount;i++) {
					 String line="";		
					 for(int j=0;j<br.length;j++) {
						  line+=br[j].readLine();
					 }
					 pw.println(line);
				}
	    
		  }
		  catch(Exception ex) {
				ex.printStackTrace();
		  }
    }
	 
    /**
     * This is a helper method to convert token-separated
     * inputFileUrlStrings into arrays.
     */
    private String[] convertInputUrlStringToArray(String inputFileUrlString){
		  inputFileUrlString.trim();
		  
		  String[] returnArray;
	
		  StringTokenizer st=new StringTokenizer(inputFileUrlString);
		  int arrayDim=st.countTokens();
		  if(arrayDim<2) {
				returnArray=new String[1];
				returnArray[0]=inputFileUrlString.trim();
		  }
		  else {
				int i=0;
				returnArray=new String[arrayDim];
				while(st.hasMoreTokens()) {
					 returnArray[i]=st.nextToken();
					 i++;
				}
		  }
		  return returnArray;
    }
    

    /**
     * This helper method assumes input is a multlined
     * String of tabbed columns.  It cuts out the number of
     * columns on the left specified by cutLeftColumns and 
     * number on the right by cutRightColumns.
     *
     * This method can accepted either single-valued or
     * multiple valued entries. 
     */
    protected String[] filterResults(String[] tabbedFile,
												 int cutLeftColumns,
												 int cutRightColumns) throws Exception {
		  String[] filteredFileArray=new String[tabbedFile.length];
		  String space=" ";
		  StringTokenizer st;
		  for(int i=0;i<tabbedFile.length;i++){
				filteredFileArray[i]=tabbedFile[i]+".filtered";
				BufferedReader br=
					 new BufferedReader(new FileReader(tabbedFile[i]));
				PrintWriter printer=
					 new PrintWriter(new FileWriter(filteredFileArray[i]),true);
				String line=br.readLine();
				while(line!=null) {
					 //	    System.out.println(line);
					 st=new StringTokenizer(line);
					 String newLine="";
					 int tokenCount=st.countTokens();
					 for (int j=0;j<tokenCount;j++) {
						  String temp=st.nextToken();
						  if(j>=cutLeftColumns && j<(tokenCount-cutRightColumns)) {
								newLine+=temp+space;
						  }
					 }
					 //	    System.out.println(newLine);
					 printer.println(newLine);
					 line=br.readLine();
				}
		  }
		  return filteredFileArray;
    }
	 
    private void makeWorkDir(String workDir, 
									  String bf_loc)
		  throws Exception {
		  System.out.println("Working Directory is "+workDir);
		  
		  String[] args0=new String[4];
        args0[0]="-DworkDir.prop="+workDir;
        args0[1]="-buildfile";
        args0[2]=bf_loc;
        args0[3]="MakeWorkDir";
		  
        setArgs(args0);
        run();
    }  
	 
    private String extractSimpleName(String extendedName) {
		  return (new File(extendedName)).getName();
    }
   
    
    /**
     * Note that inputFileUrlString can be either single values or
     * else have multiple, space separated values.  It also
     * returns a space-separated set of values.
     * All files are written to the same directory.
     */
    private String[] downloadInputFile(String[] inputFileUrlString,
													String inputFileDestDir)
		  throws Exception {
		  
		  //Convert to a URL. This will throw an exception if
		  //malformed.
		  
		  String[] fileLocalFullName=new String[inputFileUrlString.length];
		  for(int i=0;i<inputFileUrlString.length;i++) {
				
				URL inputFileUrl=new URL(inputFileUrlString[i]);
				
				String protocol=inputFileUrl.getProtocol();
				System.out.println("Protocol: "+protocol);
				String fileSimpleName=extractSimpleName(inputFileUrl.getFile());
				System.out.println(fileSimpleName);
				
				fileLocalFullName[i]=inputFileDestDir+File.separator
					 +fileSimpleName;
				
				if(protocol.equals(FILE_PROTOCOL)) {
					 String filePath=inputFileUrl.getFile();
					 fileSimpleName=inputFileUrl.getFile();
					 
					 System.out.println("File path is "+filePath);
					 File filePathObject=new File(filePath);
					 File destFileObject=new File(fileLocalFullName[i]);
					 
					 //See if the inputFileUrl and the dest file are the same.
					 if(filePathObject.getCanonicalPath().
						 equals(destFileObject.getCanonicalPath())) {
						  System.out.println("Files are the same.  We're done.");
						  return fileLocalFullName;
					 }
					 
					 //Otherwise, we will have to copy it.
					 copyFileToFile(filePathObject, destFileObject);
					 return fileLocalFullName;
				}
				
				else if(protocol.equals(HTTP_PROTOCOL)) {
					 copyUrlToFile(inputFileUrl,fileLocalFullName[i]);
				}
				
				else {
					 System.out.println("Unknown protocol for accessing inputfile");
					 throw new Exception("Unknown protocol");
				}
		  }
		  return fileLocalFullName;
    }
	 
	 
    /**
     * Famous method that I googled. This copies a file to a new
     * place on the file system.
     */
    private void copyFileToFile(File sourceFile,File destFile) 
		  throws Exception {
		  InputStream in=new FileInputStream(sourceFile);
		  OutputStream out=new FileOutputStream(destFile);
		  byte[] buf=new byte[1024];
		  int length;
		  while((length=in.read(buf))>0) {
				out.write(buf,0,length);
		  }
		  in.close();
		  out.close();
    }
	 
    /**
     * Another famous method that I googled. This downloads contents
     * from the given URL to a local file.
     */
    
    private void copyUrlToFile(URL inputFileUrl,String destFile) 
		  throws Exception {
		  
		  URLConnection uconn=inputFileUrl.openConnection();
		  InputStream in=inputFileUrl.openStream();
		  OutputStream out=new FileOutputStream(destFile);
		  
		  //Extract the name of the file from the url.
		  
		  byte[] buf=new byte[1024];
		  int length;
		  while((length=in.read(buf))>0) {
				out.write(buf,0,length);
		  }
		  in.close();
		  out.close();
		  
    }
	 
    //--------------------------------------------------
    // Find the first non-blank line and count columns.
    // Note this can screw up if input file is not
    // formated correctly, but then GeoFEST itself 
    // would probably not work either.
    //--------------------------------------------------
    protected int getFileDimension(String fileFullName) {
	
		  boolean success=false;
		  int ndim=0;
		  StringTokenizer st;
		  try {
				
				BufferedReader buf=
					 new BufferedReader(new FileReader(fileFullName));
				
				String line=buf.readLine();	
				if(line!=null){
					 while(!success) {
						  if(line.trim().equals("")) {
								line=buf.readLine();
						  }
						  else {
								success=true;
								st=new StringTokenizer(line);
								ndim=st.countTokens();
						  }		   
					 }
				}
				buf.close();
		  }
		  catch(Exception ex) {
				ex.printStackTrace();
		  }
		  return ndim;
    }
	 
    //--------------------------------------------------
    // This counts the line number.
    //--------------------------------------------------
    protected int getLineCount(String fileFullName) {
		  int nobsv=0;
		  try {
				LineNumberReader lnr=
					 new LineNumberReader(new FileReader(fileFullName));
				
				String line2=lnr.readLine();
				while(line2!=null) {
					 line2=lnr.readLine();
				}
				lnr.close();
				nobsv=lnr.getLineNumber();
		  }
		  catch(Exception ex) {
				ex.printStackTrace();
		  }
		  
		  return nobsv;
		  
    }
	 
	 
    /**
     * A dumb little method for constructing the URL outputs. This
     * will not get called if the execute()/run() method fails.
     */ 
    protected String[] getTheReturnFiles() {
		  
		  String[] extensions={".input",".range",".Q",".pi",".A",
									  ".minval",".maxval",".L",".B",
									  ".Q",".stdout",
									  ".input.X.png",".input.Y.png",".input.Z.png"};
		  
		  String[] returnFiles=new String[extensions.length];
		  for(int i=0;i<extensions.length;i++) {
				returnFiles[i]=serverUrl+"/"+projectName
					 +"/"+projectName+extensions[i];
		  }
		  
		  return returnFiles;
    }
	 
	 /**
	  * Generate a ticket.  This can be used to 
	  * make "gentle" status queries later.
	  */
	 protected String generateTicket(){
		  String stringDate=(new Date().getTime())+"";
		  return stringDate;
	 }

	 /**
	  * Write the group file
	  */
	 public void writeGroupFile(String projectDir,
										 String projectName,
										 Fault[] faults,
										 Layer[] layers) throws Exception {
		  
		  String outfile=projectDir+File.separator+projectName+".grp";

		  PrintWriter pw = new PrintWriter(new FileWriter(outfile));
		  System.out.println(outfile);
		  
		  //Write first line
		  int length = faults.length + layers.length;
		  pw.println("1.0 " + length);
		  
		  for (int i = 0; i < layers.length; i++) {
				pw.println(layers[i].getLayerName() + ".sld");
		  }
		  
		  for (int i = 0; i < faults.length; i++) {
				pw.println(faults[i].getFaultName() + ".flt");
		  }
		  pw.flush();
		  pw.close();
	 }

	/**
	 * Write/update the materials file for a specific layer.
	 */
	 public void writeAllMaterialsFiles(String projectDir,
													Layer[] layers) 
		  throws Exception {

		  for(int i=0;i<layers.length;i++){
				writeMaterialsFile(projectDir,layers[i]);
		  }
	 }

	 public void writeMaterialsFile(String projectDir,
											  Layer layer)
		  throws Exception {
		  
		  String SPC=" ";
		  //Set up the printwriter
		  String outfile=projectDir
				+File.separator
				+layer.getLayerName()
				+".materials";
		  
		  PrintWriter pw = new PrintWriter(new FileWriter(outfile, false));
		  
		  pw.println("lamelambda" + SPC+layer.getLameLambda());
		  pw.println("lamemu" + SPC+layer.getLameMu());
		  pw.println("viscosity" +SPC+layer.getViscosity());
		  pw.println("exponent"+SPC+layer.getExponent());

		  pw.flush();
		  pw.close();
		  
	 }

	 /**
	  * Write out the fault parameters to a file named after the
	  * fault (ie Northridge2.params).
	  *
	  * NOTE: The "number" parameter I think is a constant set to 1.0.
	  * That is, it is obsolete/unimportant.
	  */ 

	 public void writeAllFaultParamFiles(String projectDir,
													 Fault[] faults,
													 Layer layer) 
		  throws Exception {
		  
		  for(int i=0;i<faults.length;i++){
				writeFaultParamFile(projectDir,
										  faults[i],
										  layer,
										  i);
		  }
	 }
	 
	 public void writeFaultParamFile(String projectDir,
												Fault fault,
												Layer layer,
												int faultInt)
		  throws Exception {
		  
		  String outputFile=projectDir
				+ File.separator
				+ fault.getFaultName()
				+ ".params";

		  String tab = "\t";
		  String headerline = "number" + tab + "dip(o)" + tab + "strike(o)" + tab
				+ "slip(m)" + tab + "rake(o)" + tab + "length(km)" + tab
				+ "width(km)" + tab + "depth(km)";
		  
		  String number="1.0";  //Note this is a legacy parameter.
		  String dip = fault.getFaultDipAngle();
		  String strike = fault.getFaultStrikeAngle();

		  String slip = fault.getFaultSlip();

		  String rake = fault.getFaultRakeAngle();

		  String length = fault.getFaultLength();
		  String width = fault.getFaultWidth();
		  String depth = fault.getFaultDepth();
		  String orig_x = fault.getFaultLocationX();
		  String orig_y = fault.getFaultLocationY();
		  PrintWriter pw = new PrintWriter(new FileWriter(outputFile));
		  pw.println(headerline);
		  pw.print(number + tab + dip + tab + strike + tab + slip + tab + rake
					  + tab + length + tab + width + tab + depth + tab + orig_x 
					  + tab + orig_y);
		  pw.flush();
		  pw.close();
	 }

	/**
	 * Write out the fault to the context using guiVisco/geoFEST format.
	 * This calculates the fault origin in cartesian coordinates
	 * relative to the origin of the layer being used.
	 */
	 public void writeAllFaultOutputFiles(String projectDir,
													  Fault[] faults,
													  Layer layer) 
		  throws Exception {
		  
		  for(int i=0;i<faults.length;i++) {
				writeFaultOutputFile(projectDir,
											faults[i],
											layer,
											i);
		  }
	 }													  

	 public void writeFaultOutputFile(String projectDir,
												 Fault fault,
												 Layer layer,
												 int faultInt)
		  throws Exception {
		  
		  String SPC=" ";
		  String EXT = ".flt";
		  String outputFile=projectDir+File.separator+fault.getFaultName()+EXT;
		  
		  double locX, locY;
		  
		  //Do this for backward compatibility.  You will
		  //get a number format exception for old style
		  //fault contexts.
		  //Get out the lat and lon from the context.
		  double latstart = Double.parseDouble(fault.getFaultLatStart());
		  double lonstart = Double.parseDouble(fault.getFaultLonStart());
		  double latend = Double.parseDouble(fault.getFaultLatEnd());
		  double lonend = Double.parseDouble(fault.getFaultLonEnd());
		  
		  //Get the layer origin's lat and lon
		  double layerLatOrigin = Double.parseDouble(layer.getLayerLatOrigin());
		  double layerLonOrigin = Double.parseDouble(layer.getLayerLonOrigin());
		  
		  //Calculate the fault start in cartesian coordinates relative to 
		  //the layer origin.  Layer origin cart coordinates are (0,0,0) 
		  //of course.
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
		  
		  //Get out the stuff directly stored in the context.
		  double locZ = Double.parseDouble(fault.getFaultLocationZ());
		  double length = Double.parseDouble(fault.getFaultLength());
		  double width = Double.parseDouble(fault.getFaultWidth());
		  double depth = Double.parseDouble(fault.getFaultDepth());
		  
		  double dip = Double.parseDouble(fault.getFaultDipAngle());
		  double strike = Double.parseDouble(fault.getFaultStrikeAngle());
		  
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
		  PrintWriter pw = new PrintWriter(new FileWriter(outputFile));
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
	 }
	 
	 /**
	  *Formatting follows visco_layer.c from original code.
	  */
	 public void writeAllLayerOutputFiles(String projectDir,
													  Layer[] layers)
		  throws Exception {

		  for(int i=0;i<layers.length;i++) {
				writeLayerOutputFile(projectDir,layers[i]);
		  }
	 }

	 public void writeLayerOutputFile(String projectDir,
												 Layer layer)
		  throws Exception {
		  
		  String SPC=" ";
		  String EXT = ".sld";
		  String outfile=projectDir+File.separator+layer.getLayerName() + EXT;
		  
		  double originX = Double.parseDouble(layer.getLayerOriginX());
		  double originY = Double.parseDouble(layer.getLayerOriginY());
		  double originZ = Double.parseDouble(layer.getLayerOriginZ());
		  
		  double length = Double.parseDouble(layer.getLayerLength());
		  double width = Double.parseDouble(layer.getLayerWidth());
		  double depth = Double.parseDouble(layer.getLayerDepth());
		  
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
	 }
	 
	 protected String[] setUpArgs() {
		  String[] args=new String[1];
		  return args;
	 }
}
