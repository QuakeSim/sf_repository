package cgl.webservices.KmlGenerator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import cgl.webservices.KmlGenerator.gekmlib.Document;
import cgl.webservices.KmlGenerator.gekmlib.Folder;
import cgl.webservices.KmlGenerator.gekmlib.Kml;
import cgl.webservices.KmlGenerator.gekmlib.Link;
import cgl.webservices.KmlGenerator.gekmlib.NetworkLink;

public class ExtendedSimpleXDataKml extends SimpleXDataKml {
	
	private Kml pdoc = new Kml(); 	 
	private Folder proot = new Folder();	 
	
	
	public ExtendedSimpleXDataKml() {
		// TODO Auto-generated constructor stub
		
		proot.setName("root Folder"); 
		proot.setDescription("This is the root folder"); 
		pdoc.addFolder(proot);
	}
	
	public void init() {
		pdoc = new Kml(); 	 
		proot = new Folder();
		proot.setName("root Folder"); 
		proot.setDescription("This is the root folder"); 
		pdoc.addFolder(proot);		
	}
	
	public void setFaultPlot(String folderName, String faultName, String lonstart, String latstart, String lonend, String latend, String LineColor, double LineWidth){
		             
		super.setFaultPlot(folderName, faultName, lonstart, latstart, lonend, latend, LineColor, LineWidth);
	} 
	
	public void setOriginalCoordinate(String lon, String lat) {
		System.out.println("hello");
		super.setOriginalCoordinate(lon, lat);
	} 
	public void setCoordinateUnit(String Unit) { 
		super.setCoordinateUnit(Unit);
	} 
		
	
	 public String runMakeSubKml(String ServerTag, String UserName, String ProjectName, String JobUID, String destDir, int fileindex) {
		 System.out.println("[ExtendedSimpleXDataKml/runMakeSubKml] started");		  
		  
		 // String baseUrl=generateBaseUrl(ServerTag,UserName,ProjectName,JobUID);
		  
		 // System.out.println("Printing to file");
		 // this.printToFile(this.doc.toKML(), destDir + "/" + ProjectName + JobUID +".kml");
		 
		 
		  
		  try {
			  
				System.out.println("[ExtendedSimpleXDataKml/runMakeSubKml] Directly printing KML to "+destDir + "/" + JobUID +".kml");
				
				System.out.println("[ExtendedSimpleXDataKml/runMakeSubKml] this.doc will generate a KML");
				long start = System.currentTimeMillis();
				String kmlcontent = doc.toKML();

				System.out.println("[ExtendedSimpleXDataKml/runMakeSubKml] the size of the doc : " + kmlcontent.length());
				System.out.println("[ExtendedSimpleXDataKml/runMakeSubKml] the KML generated in " + (System.currentTimeMillis() -start)/1000+"Secs");
				
				PrintStream out = new PrintStream(new FileOutputStream(destDir + "/" + fileindex +".kml"));
				
				System.out.println("[ExtendedSimpleXDataKml/runMakeSubKml] now it will make a file using the pre-executed this.doc.toKML");
				start = System.currentTimeMillis();
				out.println(kmlcontent);
				System.out.println("[ExtendedSimpleXDataKml/runMakeSubKml] the size of the doc : " + kmlcontent.length());
				System.out.println("[ExtendedSimpleXDataKml/runMakeSubKml] this is finished in " + (System.currentTimeMillis() -start)/1000+"Secs");
				out.close();
				
		  }
		  
		  catch (FileNotFoundException e) { 
				e.printStackTrace(); 
		  } 
		  
		  
		  catch(Exception ex){
				ex.printStackTrace();
		  }
		  
		  System.out.println("[ExtendedSimpleXDataKml/runMakeSubKml] Finished");
		  // return baseUrl + "/" + ProjectName + JobUID +".kml";		  
		  
		  
		  // return "sub/" + ProjectName + JobUID +".kml";
		  String baseUrl=generateBaseUrl(ServerTag,UserName,ProjectName,JobUID);
		  return baseUrl + "/sub/" + fileindex +".kml";
	 } 
	 
	 
	
	public String runMakeSubKmls(PointEntry[] InputDataList, String ServerTag,String UserName, String ProjectName, String JobUID) {
		
		int ksize = InputDataList.length/100;
		
		List<PointEntry> list = new ArrayList<PointEntry>();
		list = Arrays.asList(InputDataList);
		
		String destDir = generateOutputDestDir(ServerTag,UserName,ProjectName,JobUID);
		
		try{ 
			 makeWorkDir(destDir + "/sub");			  
		 } 
		  
		 catch (Exception e) { 
			 e.printStackTrace();			  
		 }
		 
		 double longestlength = 0.;
		 double projectMinX=Double.valueOf(InputDataList[0].getX());
		 double projectMaxX=Double.valueOf(InputDataList[0].getX());
		 double projectMinY=Double.valueOf(InputDataList[0].getY());
		 double projectMaxY=Double.valueOf(InputDataList[0].getY());
			
		 System.out.println("[SimplexDataKml/runMakeSubKmls] InputDataList.length : " + InputDataList.length);
		 for (int i = 0; i < InputDataList.length; i++) {

			 double x = Double.valueOf(InputDataList[i].getX());
			 double y = Double.valueOf(InputDataList[i].getX());
			 if (x < projectMinX)
				 projectMinX = x;
			 
			 if (x > projectMaxX)
				 projectMaxX = x;
			 
			 if(y < projectMinY)
				 projectMinY = y;
			 
			 if(y > projectMaxY)
				 projectMaxY = y;

			 double dx = Double.valueOf(InputDataList[i].getDeltaXValue()).doubleValue(); 
			 double dy = Double.valueOf(InputDataList[i].getDeltaYValue()).doubleValue();			 
			 double length = Math.sqrt(dx * dx + dy * dy);

			 // System.out.println("[SimplexDataKml/setArrowPlacemark] dx : " + dx);
			 // System.out.println("[SimplexDataKml/setArrowPlacemark] dy : " + dy);

			 if (i == 0)
				 longestlength = length; 

			 else if (length > longestlength)
				 longestlength = length; 
		 }
		 System.out.println("[ExtendedSimplexDataKml/runMakeSubKmls] longestlength : " + longestlength);			

		 double projectLength=(projectMaxX-projectMinX)*(projectMaxX-projectMinX);
		 projectLength+=(projectMaxY-projectMinY)*(projectMaxY-projectMinY);
		 projectLength=Math.sqrt(projectLength);

		 //We arbitrarly set the longest displacement arrow to be 10% of the 
		 //project dimension.
		 double scaling = 0.7*projectLength/longestlength;

		 System.out.println("[ExtendedSimplexDataKml/runMakeSubKmls] projectLength : " + projectLength);


		
		for (int nA = 0 ; nA < ksize ; nA++) {
			

			List<PointEntry> slist = list.subList((nA*100), (nA*100)+100);

			PointEntry[] pe = new PointEntry[100];
			pe = slist.toArray(pe);
			// pe[0] = InputDataList[0];
			// pe[1] = InputDataList[1];

			super.setDatalist(pe);
			
			doc_init();
			super.setArrowPlacemarkProcess("Arrow Layer", "#000000", 0.95, scaling);			
			
			// (new UID().toString())			
			String subkmlurl = runMakeSubKml(ServerTag, UserName, ProjectName, JobUID, destDir + "/sub", nA);

			NetworkLink nk = new NetworkLink();
			Link l = new Link();
			l.setHref(subkmlurl);
			nk.addLink(l);
			proot.addNetworkLink(nk);

		}
		
		  try { 
				System.out.println("[ExtendedSimpleXDataKml/runMakeSubKmls] Directly printing KML to "+destDir + "/" + ProjectName + JobUID +".kml");
				
				System.out.println("[ExtendedSimpleXDataKml/runMakeSubKmls] this.doc will generate a KML");
				long start = System.currentTimeMillis();
				
				
				String kmlcontent = this.pdoc.toKML();
				
				// System.out.println("[runMakeKml] [1] the size of the doc : " + this.doc.toKML().length());
				System.out.println("[ExtendedSimpleXDataKml/runMakeSubKmls] the size of the doc : " + kmlcontent.length());
				System.out.println("[ExtendedSimpleXDataKml/runMakeSubKmls] the KML generated in " + (System.currentTimeMillis() -start)/1000+"Secs");
				
				PrintStream out = new PrintStream(new FileOutputStream(destDir + "/" + ProjectName + JobUID +".kml"));
				
				System.out.println("[ExtendedSimpleXDataKml/runMakeSubKmls] now it will make a file using the pre-executed this.doc.toKML");
				start = System.currentTimeMillis();
				out.println(kmlcontent);
				System.out.println("[ExtendedSimpleXDataKml/runMakeSubKmls] the size of the doc : " + kmlcontent.length());
				System.out.println("[ExtendedSimpleXDataKml/runMakeSubKmls] this is finished in " + (System.currentTimeMillis() -start)/1000+"Secs");
				out.close();
				
		  }
		  
		  catch (FileNotFoundException e) { 
				e.printStackTrace(); 
		  } 
		  
		  
		  catch(Exception ex){
				ex.printStackTrace();
		  }
		  
		  System.out.println("[SimpleXDataKml/runMakeKml] Finished");
		  
		  
		  // destDir + "/sub";
		  
		  
		  String baseUrl=generateBaseUrl(ServerTag,UserName,ProjectName,JobUID);		  
		  return baseUrl + "/" + ProjectName + JobUID +".kml";
		
	}
	
	public String zipfiles (String folder, String zipfilename) {
		
		try {

			File inFolder=new File(folder);
			File outFolder=new File(folder);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outFolder)));
			BufferedInputStream in = null;
			byte[] data = new byte[1000];
			String files[] = inFolder.list();
			
			for (int i=0; i<files.length; i++) {
				in = new BufferedInputStream(new FileInputStream (inFolder.getPath() + "/" + files[i]), 1000);
				out.putNextEntry(new ZipEntry(files[i]));

				int count;
				while((count = in.read(data,0,1000)) != -1) {
					out.write(data, 0, count);					
				}
				out.closeEntry();				
			}
			
			

			out.flush();
			out.close();			
		}

		catch(Exception e) {			
			e.printStackTrace();			
		}

		return zipfilename;		
	}
	
}
