

/**
Markers must be registered here with some index in order for much of this code to work.
*/
var gMarkers = [];

/**
  * Set up the events to show and hide the tooltip
  * @param map GMap2
  * @param m GMarker
  */
function setupMarkerForTooltip(map, m, tooltip, i) {
	m.tooltip = '<div class="tooltip">'+tooltip+'</div>';
	m.ownerMap = map;
	m.gMarkerIndex=i;
    gMarkers[i] = m; // remember them in an array
}

/**
  * Set up the events to show and hide the tooltip
  * @param map GMap2
  * @param m GMarker
  */
function setShowTooltipOnMouseover(m) {
	GEvent.addListener(m,"mouseover", function() {
	  showTooltip(m);
	});        
	GEvent.addListener(m,"mouseout", function() {
		hideTooltip();
	});  
}

/** 
  * Shows the tooltip.
  * It must have been set with #setupMarkerForTooltip.
  */
function showTooltip(marker) {
	tooltip.innerHTML = marker.tooltip;
	var map = marker.ownerMap;
	var pt=map.getCurrentMapType().getProjection().fromLatLngToPixel(map.fromDivPixelToLatLng(new GPoint(0,0),true),map.getZoom());
	var offset=map.getCurrentMapType().getProjection().fromLatLngToPixel(marker.getPoint(),map.getZoom());
	var anchor=marker.getIcon().iconAnchor;
	var wd=marker.getIcon().iconSize.width;
	var ht=tooltip.clientHeight;
	var pos = new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(offset.x - pt.x - anchor.x + wd, offset.y - pt.y -anchor.y -ht)); 
	pos.apply(tooltip);
	tooltip.style.visibility="visible";
}

/** 
  * Hides the tooltip.
  */
function hideTooltip() {
	tooltip.style.visibility="hidden";
}

/**
  * Makes mouseover of the given element bring up the map tooltips.
  *
  * @param e Element
  * @param mi marker index into gMarkers[] array
  */
function addTooltipOnMouseover(e, mi) {
	e.setAttribute("onmouseover","showTooltip(gMarkers['"+mi+"'])");
	e.setAttribute("onmouseout","hideTooltip()");
}

/**
  * Makes mouse over of the map marker change the style of the 
  * listing element to contain "mouseOver".
  *
  * @param e Element
  * @param mi marker index into gMarkers[] array
  */
function changeClassOnMarkerMouseover(e, mi) {
	GEvent.addListener(gMarkers[mi],"mouseover", function() {
	  e.className += " mousedOver";
	});        
	GEvent.addListener(gMarkers[mi],"mouseout", function() {
	  e.className = e.className.replace(/ ?mousedOver/," ");
	});        
}

// Create the marker and corresponding information window 
function createInfoMarkerWithImage(point, address, icon, image1, image2) {
  var marker = new GMarker(point,icon);
  GEvent.addListener(marker, "click", function()
  {
    marker.openInfoWindowHtml(address);
  } );

	GEvent.addListener(marker, 'mouseover', function() {
		marker.setImage(image2);
	});

	GEvent.addListener(marker, 'mouseout', function() {
		marker.setImage(image1);
	});
  
  return marker;
}


// Create the marker and corresponding information window 
function createInfoMarker(point, address, icon) {
  var marker = new GMarker(point,icon);
  GEvent.addListener(marker, "click", function()
  {
    marker.openInfoWindowHtml(address);
  } );

	GEvent.addListener(marker, 'mouseover', function() {
		marker.setImage('http://www.google.com/mapfiles/markerB.png');
	});

	GEvent.addListener(marker, 'mouseout', function() {
		marker.setImage('http://www.google.com/mapfiles/markerA.png');
	});
  
  return marker;
}

// Create the marker and corresponding information window 
function createTabsInfoMarker(point, infoTabs ,icon) {
  var marker = new GMarker(point,icon);
  GEvent.addListener(marker, "click", function() {
    marker.openInfoWindowTabsHtml(infoTabs);
  });
  return marker;
}
