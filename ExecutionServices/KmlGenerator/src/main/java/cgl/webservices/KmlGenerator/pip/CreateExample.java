package cgl.webservices.KmlGenerator.pip;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import cgl.webservices.KmlGenerator.gekmlib.*;

/**
 * A simple example showing how to create a Kml document from
 * scratch.
 *  
 * @author Keith Power March 2007
 * @version 0.01
 */

public class CreateExample
{
    public static void main(String[] args)
    {
	// Start with a blank Kml object, give it a URL
	Kml doc = new Kml();
	doc.setHref("http://localhost/random");

	// create and add root folder
	Folder root = new Folder();
	root.setName("Root Folder");
	root.setDescription("This is the root folder");
	doc.addFolder(root);
	
	// create and add another folder to root
	Folder container = new Folder();
	container.setName("First Sub Folder");
	container.setDescription("This is the a folder contained by the root");
	root.addFolder(container);
	
	// create and add a Placemark containing a Point
	Placemark mark1 = new Placemark();
	Point point1 = new Point();
	point1.setCoordinates("1, 2, 0");	// NA will go to 0.0
	mark1.addPoint(point1);
	container.addPlacemark(mark1);
	System.out.println(doc.toKML()+"\n\n");
    }    
}
