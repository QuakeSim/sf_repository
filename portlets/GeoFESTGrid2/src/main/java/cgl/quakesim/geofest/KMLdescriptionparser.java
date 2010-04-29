package cgl.quakesim.geofest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.servogrid.genericproject.FaultDBEntry;


public class KMLdescriptionparser {	
		
	Document dom = null;
	Element docEle = null;
	String desc = null;
	ArrayList listofnodelist = new ArrayList();
	
	String faultname = "";
    double dip = 0;
    double strike = 0;
    double depth = 0;
    double width = 0;
    double length = 0;
    double latEnd = 0;
    double latStart = 0;
    double lonStart = 0;
    double lonEnd = 0;
    
    public double getdip(){    	
    	return dip;
    }
    
    public double getstrike(){    	
    	return strike;
    }
    
    public double getdepth(){    	
    	return depth;
    }
    
    public double getwidth(){	
    	return width;
    }
    
    public double getlength(){	
    	return length;
    }
    
    public double getlatEnd(){    	
    	return latEnd;
    }
    
    public double getlatStart(){
    	return latStart;
    }
    
    public double getlonStart(){
    	return lonStart;
    }
    
    public double getlonEnd(){
    	return lonEnd;
    }
	
	
	public void parseXml(String dir, String xml){
		
		String[] temp = xml.split(" ");
		for (int nA = 0 ; nA < temp.length ; nA++)
		{		
			DocumentBuilderFactory dbf = DocumentBuilderFactoryImpl.newInstance();		
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				System.out.println("[KMLdescriptionparser] " + "dir : " + dir + " temp[nA] : " + temp[nA]);
				dom = db.parse(dir + temp[nA]);
				docEle = dom.getDocumentElement();
				
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			listofnodelist.add(docEle.getElementsByTagName("Placemark"));
		}
		
	}
	
	public String getDesc(String faultname){
				
		for (int nA = 0 ; nA < listofnodelist.size() ; nA++)
		{
			NodeList nodelist = (NodeList) listofnodelist.get(nA);
			
			for (int nB = 0 ; nB < nodelist.getLength() ; nB++){
				
				// System.out.println(((Element)nodelist.item(nA)).getElementsByTagName("name").item(0).getTextContent());
				
				String name = ((Element)nodelist.item(nB)).getElementsByTagName("name").item(0).getTextContent();
				
				if (name.compareTo(faultname) == 0) {
					desc = ((Element)(((Element)nodelist.item(nB)).getElementsByTagName("description").item(0).getParentNode())).getElementsByTagName("description").item(0).getTextContent();
					if (desc != null)
						return desc;					
				}
			}
				
		}
		
		System.out.println("none");
		
		return null;	
	}
	
	// box consists of starting lat, ending lat, starting lon, ending lon
	private boolean isItinaBox(String box) {
		
		String[] temp = box.split(" ");
		if (temp.length != 4){
			System.out.println ("[isItinaBox] String box hasn't 4 elements");
			return false;
		}			
		else {
			
			double lonmin, lonmax, latmin, latmax;
			
			if (lonStart >= lonEnd) {
				lonmin = lonEnd;
				lonmax = lonStart;
			}
			else {
				lonmin = lonStart;
				lonmax = lonEnd;				
			}
				
			
			if (latStart >= latEnd) {
				latmin = latEnd;
				latmax = latStart;
			}
			else {
				latmin = latStart;
				latmax = latEnd;				
			}
			
			double key_lonmin, key_lonmax, key_latmin, key_latmax;
			
			
			if (Double.parseDouble(temp[2]) >= Double.parseDouble(temp[3])) {
				key_lonmin = Double.parseDouble(temp[3]);
				key_lonmax = Double.parseDouble(temp[2]);
			}
			else {
				key_lonmin = Double.parseDouble(temp[2]);
				key_lonmax = Double.parseDouble(temp[3]);				
			}
			
			if (Double.parseDouble(temp[0]) >= Double.parseDouble(temp[1])) {
				key_latmin = Double.parseDouble(temp[1]);
				key_latmax = Double.parseDouble(temp[0]);
			}
			else {
				key_latmin = Double.parseDouble(temp[0]);
				key_latmax = Double.parseDouble(temp[1]);				
			}
			
			/*
			System.out.println("original keyword : " + temp[0] + " " + temp[1] + temp[2] + " " + temp[3]);
			System.out.println("keyword_lonmin : " + key_lonmin);
			System.out.println("keyword_lonmax : " + key_lonmax);
			System.out.println("keyword_latmin : " + key_latmin);
			System.out.println("keyword_latmax : " + key_latmax);
			
			System.out.println("lonmin : " + lonmin);
			System.out.println("lonmax : " + lonmax);
			System.out.println("latmin : " + latmin);
			System.out.println("latmax : " + latmax);
			*/
			
			if ((lonmin >= key_lonmin && lonmax <= key_lonmax)
					&& (latmin >= key_latmin && latmax <= key_latmax))
				return true;
			
		}
		
		return false;
	}
	
    public List getFaultList (String attribute, String keyword) {
    	    	
		
		List myFaultDBEntryList= new ArrayList();		
		
		for (int nA = 0 ; nA < listofnodelist.size() ; nA++)
		{
			NodeList nodelist = (NodeList) listofnodelist.get(nA);
			
			{
				
				for (int nB = 0 ; nB < nodelist.getLength() ; nB++){
					
					// System.out.println(((Element)nodelist.item(nB)).getElementsByTagnBme("nBme").item(0).getTextContent());
				
					
					desc = ((Element)(((Element)nodelist.item(nB)).getElementsByTagName("description").item(0).getParentNode())).getElementsByTagName("description").item(0).getTextContent();
					parsevalues();
					
					if ((attribute.compareTo("Name") == 0 && keyword.compareToIgnoreCase(faultname) == 0)
							|| (attribute.compareTo("LonLat") == 0 && isItinaBox(keyword)) || (attribute.compareTo("All") == 0))
					{
						FaultDBEntry tmp_FaultDBEntry = new FaultDBEntry();
						tmp_FaultDBEntry.setFaultName(new SelectItem(this.faultname, this.faultname));
						tmp_FaultDBEntry.setFaultAuthor("");
						
						String[] temp = this.faultname.split("-");
						if (temp.length > 1)
							tmp_FaultDBEntry.setFaultSegmentName(temp[1]);
						else if (temp.length == 1)
							tmp_FaultDBEntry.setFaultSegmentName(temp[0]);
						
						tmp_FaultDBEntry.setFaultSegmentCoordinates("("
										+ latStart + ","
										+ latEnd + ")-("
										+ lonStart + ","
										+ lonEnd + ")");
						tmp_FaultDBEntry.setInterpId("");
						myFaultDBEntryList.add(tmp_FaultDBEntry);
					}
				}				
			}
		}
		return myFaultDBEntryList;		
    }
	
	public void setDesc(String desc){
		
		this.desc = desc;
		
	}
	
	public void parsevalues(){		

		String[] splitvalues = desc.split("<br>");
		
		for (int nA = 0 ; nA < splitvalues.length ; nA++)
		{	
			String[] temp = splitvalues[nA].split(":");
			// System.out.println(temp[0]);
			if (temp[0].compareTo("<b>Fault Name</b>") == 0)
				this.faultname = temp[1];
			
			else if (temp[0].compareTo("<b>DipAngel</b>") == 0)
				this.dip = Double.parseDouble(temp[1]);
			
			else if (temp[0].compareTo("<b>Width</b>") == 0)
				this.width = Double.parseDouble(temp[1].split("&")[0]);
			
			else if (temp[0].compareTo("<b>Depth</b>") == 0)
				this.depth = Double.parseDouble(temp[1].split("&")[0]);
			
			else if (temp[0].compareTo("<b>Length</b>") == 0)
				this.length = Double.parseDouble(temp[1].split("&")[0]);			
			
			else if (temp[0].compareTo("<b>Strike Angel</b>") == 0)
				this.strike= Double.parseDouble(temp[1].split("&")[0]);
			
			else if (temp[0].compareTo("<b>Location [lat, lon]</b>") == 0) {
				
				String[] splitlocation = temp[1].split("\\], \\[");
				splitlocation[0] = splitlocation[0].split("\\[")[1];
				splitlocation[1] = splitlocation[1].split("\\]")[0];
				this.latStart = Double.parseDouble(splitlocation[0].split(", ")[0]);
				this.lonStart = Double.parseDouble(splitlocation[0].split(", ")[1]);
				
				this.latEnd = Double.parseDouble(splitlocation[1].split(", ")[0]);
				this.lonEnd = Double.parseDouble(splitlocation[1].split(", ")[1]);
			}
		}
		 
	    // this.strike = 0; // will be calculated in each application. (QueryFaultFromDB)
	    
	    /*
	    System.out.println(this.dip);
	    System.out.println(this.width);
	    System.out.println(this.latStart);
	    System.out.println(this.lonStart);
	    System.out.println(this.latEnd);
	    System.out.println(this.lonEnd);
	    */
	}
	
	
	/**
	 * @param args
	 */
	/*
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		KMLdescriptionparser kdp = new KMLdescriptionparser();
		kdp.parseXml("", "QuakeTables_CGS_2002.kml QuakeTables_CGS_1996.kml");
		
		// kdp.getDesc("San Andreas (Coachella)");
		
			
		// kdp.getDesc("San Andreas - Coachella");
		// kdp.getFaultList("Name", "San Andreas (Coachella)");
		
		// box consists of starting lat, ending lat, starting lon, ending lon
		System.out.println("List : " + kdp.getFaultList("LonLat", "30.92 33.35 -116.48 -115.71").size());

		for (int nA = 0; nA < kdp.getFaultList("LonLat", "30.92 33.35 -116.48 -115.71").size() ; nA++)
		{			
			System.out.println("List : " + ((FaultDBEntry)kdp.getFaultList("LonLat", "30.92 33.35 -116.48 -115.71").get(nA)).faultName.getValue());
		}
	}
	*/

}
