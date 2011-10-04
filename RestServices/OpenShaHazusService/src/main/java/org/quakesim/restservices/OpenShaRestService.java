package org.quakesim.restservices;

import java.net.URL;

import java.net.HttpURLConnection;
import java.io.*;

//Logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//These are Jersey jars
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.UriInfo;

//These are OpenSha dependencies
import org.opensha.commons.geo.Location;
import org.opensha.sha.imr.AttenuationRelationship;
import org.opensha.sha.earthquake.EqkRupture;
import org.opensha.sha.faultSurface.EvenlyGriddedSurface;
import org.opensha.sha.faultSurface.PointSurface;

@Path("/openhazus/")
public class OpenShaRestService {

	 public OpenShaRestService() {
	 }


	 /**
	  * This is the primary exposed method.
	  */ 
	 @GET
	 @Produces("text/plain")
	 public void getOpenShaHazusOutput(double mag, double rake, double lat, double lon, double depth) {
		  createAttenuationRelationship();
		  
		  createEarthquakeRupture(mag,rake,lat,lon,depth);
		  
		  getGMTParamsForHazus();

		  makeHazusShapeFilesAndMap();

		  makeMapForHazus();
	 }

	 protected void createAttenuationRelationship() {
	 }

	 protected EqkRupture createEarthquakeRupture(double mag, double rake, double lat, double lon, double depth) {
		  Location location=new Location(lat,lon);
		  PointSurface surface=new PointSurface(lat,lon,depth);
		  return new EqkRupture(mag,rake,surface,location);
	 }

	 protected void getGMTParamsForHazus() {
	 }

	 private void openConnectionToOpenShaServer() {
	 }

	 protected void makeHazusShapeFilesAndMap() {
	 }

	 protected void makeMapForHazus() {
	 }

}

