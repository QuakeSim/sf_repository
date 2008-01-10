package cgl.quakesim.geofest;

//Not explicitly naming these because they are famous.
import java.util.*;
import java.io.*;
import java.net.*;

/**
 * This sample file shows how to set up and invoke the GeoFEST Web service
 * using the Northridge simulation example.  This sends 3 layers and 1 fault
 * to the service and returns back URLs for the answers.
 */

public class GeoFESTClient {
    /**
     * This is a main() for testing.
     */
    public static void main(String[] args) {
	
	final String DEFAULT_SERVICE_URL="http://gf19.ucs.indiana.edu:8080/geofestexec/services/GeoFESTExec";
	
	String serviceUrl="";
	System.out.println(args);
	if(args!=null && args.length>0  && args[0].indexOf("http://")>-1) {
	    //Use the service chosen by user
	    serviceUrl=args[0];
	    System.out.println("Using "+args[0]);
	}
	else {
	    serviceUrl=DEFAULT_SERVICE_URL;
	}
	
	//Create a fault array.
	Fault[] faults=new Fault[1];
	
	faults[0]=new Fault();
	faults[0].setFaultName("Northridge");
	faults[0].setFaultLocationX("0.0");
	faults[0].setFaultLocationY("0.0");
	faults[0].setFaultLocationZ("0.0");
	faults[0].setFaultLength("14.009");
	faults[0].setFaultWidth("21.0");
	faults[0].setFaultDepth("19.5");
	faults[0].setFaultDipAngle("40.0");
	faults[0].setFaultStrikeAngle("122.0");
	faults[0].setFaultSlip("1.22");
	faults[0].setFaultRakeAngle("101.0");
	faults[0].setFaultLatStart("34.243");
	faults[0].setFaultLatEnd("34.176");
	faults[0].setFaultLonStart("-118.72");
	faults[0].setFaultLonEnd("-118.591");
	
	
	//Create layers.
	Layer[] layers=new Layer[3];
	layers[0]=new Layer();
	layers[0].setLayerName("NorthridgeAreaMantle");
	layers[0].setLayerOriginX("-103.0");
	layers[0].setLayerOriginY("-106.0");
	layers[0].setLayerOriginZ("-35.0");
	layers[0].setLayerLatOrigin("34.243");
	layers[0].setLayerLonOrigin("-118.72");
	layers[0].setLayerLength("240.0");
	layers[0].setLayerWidth("240.0");
	layers[0].setLayerDepth("65.0");
	layers[0].setLameLambda("70.0");
	layers[0].setLameMu("70.0");
	layers[0].setViscosity("7000");
	layers[0].setExponent("1.0");
	
	layers[1]=new Layer();
	layers[1].setLayerName("NorthridgeAreaMidCrust");
	layers[1].setLayerOriginX("-103.0");
	layers[1].setLayerOriginY("-106.0");
	layers[1].setLayerOriginZ("-5.0");
	layers[1].setLayerLatOrigin("34.243");
	layers[1].setLayerLonOrigin("-118.72");
	layers[1].setLayerLength("240.0");
	layers[1].setLayerWidth("240.0");
	layers[1].setLayerDepth("30.0");
	layers[1].setLameLambda("35.0");
	layers[1].setLameMu("35.0");
	layers[1].setViscosity("0.0");
	layers[1].setExponent("0.0");
	
	layers[2]=new Layer();
	layers[2].setLayerName("NorthridgeAreaUpper");
	layers[2].setLayerOriginX("-103.0");
	layers[2].setLayerOriginY("-106.0");
	layers[2].setLayerOriginZ("0.0");
	layers[2].setLayerLatOrigin("34.243");
	layers[2].setLayerLonOrigin("-118.72");
	layers[2].setLayerLength("240.0");
	layers[2].setLayerWidth("240.0");
	layers[2].setLayerDepth("5.0");
	layers[2].setLameLambda("17.0");
	layers[2].setLameMu("17.0");
	layers[2].setViscosity("17.0");
	layers[2].setExponent("1.0");
	
	
	
	//Create geotrans params.  This just uses the 
	//default parameters, but you can set() them 
	//as with the layers and faults.
	GeotransParamsBean gpb=new GeotransParamsBean();
	
	String userName="duhFaultUser";
	String projectName="faultsatmyfeet";
	
	try {
	    //Make the mesh.
	    GeoFESTService gfs=new GeoFESTServiceServiceLocator().
		getGeoFESTExec(new URL(serviceUrl));
	    
	    System.out.println("Running blocking version");
	    //Invoke the mesh creation step.  Uses the "rare"
	    //option.
	    MeshRunBean mrb=gfs.runBlockingMeshGenerator(userName,
																	 projectName,
																	 faults,
																	 layers,
																	 "rare");
	    //Print out the URLs of the resulting mesh files
// 	    System.out.println("These are the URLs of the mesh input files.");
// 	    for(int i=0;i<returnUrls1.length;i++){
// 			  System.out.println(returnUrls1[i]);
// 	    }
	    
	    System.out.println("Running GeoFEST");
	    
	    //Run GeoFEST.
	    GFOutputBean gfoutput=
			  gfs.runGeoFEST(userName,projectName,gpb,mrb.getJobUIDStamp());
	    
	    //Print out the URLs of the GeoFEST output.
		 System.out.println(gfoutput.getNodeUrl());
		 System.out.println(gfoutput.getCghistUrl());
	    System.out.println("These are the URLs of the GeoFEST output files.");			
	    System.out.println("Not all of these will be immediately active.");

// 	    for(int i=0;i<returnUrls2.length;i++){
// 			  System.out.println(returnUrls2[i]);
// 	    }
	    
	}
	catch (Exception ex) {
	    ex.printStackTrace();
	}
    }
}

