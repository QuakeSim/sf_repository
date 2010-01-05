package cgl.quakesim.simplex;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class KMLdescriptionparser {	
		
	Document dom;
	Element docEle;
	String desc = null;
	
    double dip = 0;
    double strike = 0;
    double depth = 0;
    double width = 0;
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
	
	
	private void parseXml(String xml){
		
		DocumentBuilderFactory dbf = DocumentBuilderFactoryImpl.newInstance();		
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(xml);
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
	}
	
	private String getDesc(String placemarkname){
		NodeList nodelist = docEle.getElementsByTagName("Placemark");
		
		
		for (int nA = 0 ; nA < nodelist.getLength() ; nA++){
			
			// System.out.println(((Element)nodelist.item(nA)).getElementsByTagName("name").item(0).getTextContent());
			
			String name = ((Element)nodelist.item(nA)).getElementsByTagName("name").item(0).getTextContent();
			
			if (name.compareTo(placemarkname) == 0) {
				desc = ((Element)(((Element)nodelist.item(nA)).getElementsByTagName("description").item(0).getParentNode())).getElementsByTagName("description").item(0).getTextContent();
				if (desc != null)
					return desc;
				
			}
			
		}
		
		return null;
	}
	
	public void setDesc(String desc){
		
		this.desc = desc;
		
	}
	
	public void parsevalues(){
		
		// System.out.println(desc.split("<br>")[0]);
		String[] splitvalues = desc.split("<br>");
		// System.out.println(desc.split("<br>").length);
		
		for (int nA = 0 ; nA < splitvalues.length ; nA++)
		{	
			String[] temp = splitvalues[nA].split(":");
			// System.out.println(temp[0]);
			if (temp[0].compareTo("<b>Dip</b>") == 0)
				this.dip = Double.parseDouble(temp[1]);
			else if (temp[0].compareTo("<b>Down Dip Width</b>") == 0)
				this.width = Double.parseDouble(temp[1].split("&")[0]);
			else if (temp[0].compareTo("<b>Location</b>") == 0) {
				
				String[] splitlocation = temp[1].split("\\], \\[");
				splitlocation[0] = splitlocation[0].split("\\[")[1];
				splitlocation[1] = splitlocation[1].split("\\]")[0];
				this.latStart = Double.parseDouble(splitlocation[0].split(", ")[0]);
				this.lonStart = Double.parseDouble(splitlocation[0].split(", ")[1]);
				
				this.latEnd = Double.parseDouble(splitlocation[1].split(", ")[0]);
				this.lonEnd = Double.parseDouble(splitlocation[1].split(", ")[1]);
			}
		}
		 
	    this.strike = 7.77;
	    this.depth = 7.7;
	    
	    System.out.println(this.dip);
	    System.out.println(this.width);
	    System.out.println(this.latStart);
	    System.out.println(this.lonStart);
	    System.out.println(this.latEnd);
	    System.out.println(this.lonEnd);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		KMLdescriptionparser kdp = new KMLdescriptionparser();
		// kdp.parseXml("QuakeTables_CGS_2002.kml");
		// kdp.getDesc("San Andreas (Coachella)");
		
		kdp.parseXml("QuakeTables_CGS_1996.kml");		
		kdp.getDesc("San Andreas - Coachella");
		
		kdp.parsevalues();
		
	}

}
