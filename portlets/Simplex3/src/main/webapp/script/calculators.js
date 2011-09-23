var calculators=calculators || (function() {
	 //--------------------------------------------------
	 //This function handles a fault's translation relative to the origin. 
	 //That is, it handles changes to the fault's (x,y)
	 //--------------------------------------------------
	 function updateLat0Lon0Lat1Lon1(origLat, origLon, xstart, ystart,latStart, lonStart, strike, dipAngle, length){
		  
		  //Find the new lat/lon value of the modified starting point from (x,y).
		  var theFactor=d2r* Math.cos(d2r * origLat.value) * 6378.139 * (1.0 - Math.sin(d2r * origLat.value) * Math.sin(d2r * origLat.value) * flatten);
		  console.log(theFactor);
		  lonStart.value = (xstart.value*1.0)/theFactor + (origLon.value*1.0);
		  latStart.value = ystart.value/111.32 + (origLat.value*1.0);
		  console.log(lonStart.value+" "+latStart.value);
	 	  
		  //Now find the lat/lon values of the translated endpoint.
		  //First, find the Cartesian coordinates of the endpoint.  
		  
		  if (strike.value == 0) {
				xend = 0; 
				yend = length.value;
		  }
		  else if (strike.value == 90) { xend = length.value; yend = 0;}
		  else if (strike.value == 180) { xend = 0; yend = (-1.0) * length.value;}
		  else if (strike.value == 270) { xend = (-1.0) * length.value; yend = 0;}
		  else {
				var sval = 90 - strike.value;
				var thetan = Math.tan(sval*d2r);
				var xend = length.value/Math.sqrt(1 + thetan*thetan);
				var yend = Math.sqrt(length.value*length.value - xend*xend);
				
				if (strike.value > 0 && strike.value < 90) { xend = xend*1.0; yend = yend*1.0;}
				else if (strike.value > 90 && strike.value < 180) { xend = xend*1.0; yend = yend* (-1.0);}
				else if (strike.value > 180 && strike.value < 270) { xend = xend*(-1.0); yend = yend*(-1.0);}
				else if (strike.value > 270 && strike.value < 360) { xend = xend*(-1.0); yend = yend*1.0;}
		  }
		  
		  //Note we use the lat, lon of the fault's starting point here, not the origin's lat, lon, because
		  //we are using the fault length (not the distance to the origin from the end point).
		  var theFactor=d2r* Math.cos(d2r * latStart.value) * 6378.139 * (1.0 - Math.sin(d2r * latStart.value) * Math.sin(d2r * latStart.value) * flatten);
		  console.log(theFactor);
		  lonEnd.value = (xend*1.0)/theFactor + (lonStart.value*1.0);
		  latEnd.value = yend/111.32 + (latStart.value*1.0);
		  
		  lonEnd.value = Math.round(lonEnd.value*100)/100.0;
		  latEnd.value = Math.round(latEnd.value*100)/100.0;
		  console.log(xstart.value+" "+ystart.value+" "+latStart.value+" "+lonStart.value+" "+latEnd.value+" "+lonEnd.value+" "+strike.value+" "+dipAngle.value+" "+length.value);
	 }
	 
	 //--------------------------------------------------
	 //This function handles changes in a fault's starting lat and lon.
	 //This causes a rotation, expansion/contraction, and change in the
	 //fault's Cartesian coordinates.
	 //--------------------------------------------------
	 function updateXYLengthStrike(origLat, origLon, xstart, ystart,latStart, lonStart, strike, dipAngle, length){

		  
		  //First, calculate the new (x,y) of the fault
		  var theFactor=d2r* Math.cos(d2r * origLat.value) * 6378.139 * (1.0 - Math.sin(d2r * origLat.value) * Math.sin(d2r * origLat.value) * flatten);
		  console.log("The factor:"+theFactor);
		  xstart.value=(lonStart.value-origLon.value)*theFactor;
		  ystart.value=(latStart.value-origLat.value)*111.32;
		  console.log("X and Y:"+xstart.value+" "+ystart.value);
		  
		  //Next, calculate the length
		  var theFactor=d2r* Math.cos(d2r * latStart.value) * 6378.139 * (1.0 - Math.sin(d2r * latStart.value) * Math.sin(d2r * latStart.value) * flatten);
		  console.log("The factor:"+theFactor);	
		  console.log(lonEnd.value+" "+lonStart.value+" "+latEnd.value+" "+latStart.value);				
		  xdiff=(lonEnd.value-lonStart.value)*theFactor;
		  ydiff=(latEnd.value-latStart.value)*111.32;
		  console.log("Sqrt:"+Math.sqrt(xdiff*xdiff-ydiff*ydiff));
		  lengthValue=Math.sqrt(xdiff*xdiff+ydiff*ydiff);
		  length.value=Math.round(lengthValue*1000/1000);
		  console.log("xdiff, ydiff, length:"+xdiff+" "+ydiff+" "+length.value);
		  
		  //And the strike
		  strikeValue=Math.atan2(xdiff,ydiff)/d2r;
		  if (strikeValue < 0) { strikeVaule = strikeValue + 360; }
		  strike.value=Math.round(strikeValue*1000)/1000;
		  console.log("Strike:"+strike.value);
		  console.log(xstart.value+" "+ystart.value+" "+latStart.value+" "+lonStart.value+" "+latEnd.value+" "+lonEnd.value+" "+strike.value+" "+dipAngle.value+" "+length.value);
	 }
	 
	 /**
	  * Public API
	  */
	 return {
		  updateLat0Lon0Lat1Lon1:updateLat0Lon0Lat1Lon1,
		  updateXYLengthStrike:updateXYLengthStrike
	 }

})();