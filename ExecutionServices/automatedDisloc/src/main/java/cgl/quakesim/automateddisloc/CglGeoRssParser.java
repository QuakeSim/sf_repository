package cgl.quakesim.automateddisloc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class CglGeoRssParser {
	
	DocumentBuilder db = null;
	Document doc = null;
	Element doce = null;

	public CglGeoRssParser() {
		
	}
	
	public void parse(String url) {		
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			
			URL u = null;
			try {
				u = new URL(url);
				db = null;
				try {
					db = dbf.newDocumentBuilder();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				doc = db.parse(u.openStream());
				doce = doc.getDocumentElement();
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch blockEnr
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
	}
	
	public List getEntryList() {
		
		List entry_list = new ArrayList();
		
		NodeList listofnodelist = doce.getElementsByTagName("entry");
		
		for (int nA = 0 ; nA < listofnodelist.getLength() ; nA++) {
			Entry en = new Entry();
			Element e = (Element)listofnodelist.item(nA);
			en.setId(e.getElementsByTagName("id").item(0).getTextContent());
			en.setTitle(e.getElementsByTagName("title").item(0).getTextContent());
			en.setUpdated(e.getElementsByTagName("updated").item(0).getTextContent());
			en.setGeorss_point(e.getElementsByTagName("georss:point").item(0).getTextContent());
			en.setGeorss_elev(e.getElementsByTagName("georss:elev").item(0).getTextContent());
			
			/*
			System.out.println(en.getId());			
			System.out.println(en.getTitle());
			System.out.println(en.getUpdated());
			System.out.println(en.getGeorss_point());
			System.out.println(en.getGeorss_elev());
			*/
			
			entry_list.add(en);
		}
		
		return entry_list;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		CglGeoRssParser cgrp = new CglGeoRssParser();
		cgrp.parse("http://earthquake.usgs.gov/earthquakes/catalogs/7day-M5.xml");
		cgrp.getEntryList();
	}
}
