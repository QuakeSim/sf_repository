<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Google Maps</title>
<script
	src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAgYAii_xZWT_zf_1Dj7VvgBTM-mQfCm4234zOfC78cwKWdUeTuBSI6nfLv9WmRyyCQaUSnGm4oC9jzw"
	type="text/javascript"></script>



</head>
<body onunload="GUnload()">
<f:view>
<script type="text/javascript">
var googleMapTooltips_src='<h:outputText value="#{facesContext.externalContext.requestContextPath}/googleMapTooltips.js"/>';
var markermanager_src='<h:outputText value="#{facesContext.externalContext.requestContextPath}/markermanager.js"/>';

</script>

<script src='<h:outputText value="#{facesContext.externalContext.requestContextPath}/googleMapTooltips.js"/>'></script>
<script src='<h:outputText value="#{facesContext.externalContext.requestContextPath}/markermanager.js"/>'></script>


<style type="text/css">
      html  {
      	font-family: Verdana;
      }
      .listing {
      	padding: 5px;
        border:1px #fff solid;
        font-size: x-small;
      }
      .listing a {
      	font-weight: bold;
        font-size: small;
      }
      .mousedOver, .listing:hover {
        color: #000;
        background-color:#eee;
      }

      .tooltip {
        font-size: small;
        font-weight:bold;
        padding: 2px;
        border:1px #036 solid;
        border-width:1px 2px 2px 1px;
        border-color: #000 #666 #666 #000;
        background-color:#FF9;
        color: #000;
        width: 210px;
        height: 80px;
      }
      
      .listing .detail {
      	color: #fff;
      }
      .mousedOver .detail, .listing:hover .detail {
      	color: #333;
      }
    </style>

	<table border="1">
		<tr>

			<td width="150" valign="top">
			<div id="sidebar"></div>
			</td>
			<td>
			<div id="map" style="width: 800px; height: 600px"></div>
			</td>
		</tr>
	</table>


	<noscript><b>JavaScript must be enabled in order for you
	to use Google Maps.</b></noscript>


	<script type="text/javascript">
    //<![CDATA[

    if (GBrowserIsCompatible()) {
      // ==================================================
      // A function to create a tabbed marker and set up the event window
      // This version accepts a variable number of tabs, passed in the arrays htmls[] and labels[]
      function createTabbedMarker(point,htmls,labels,icon, image1, image2) {
	        var marker = new GMarker(point, icon);
        GEvent.addListener(marker, "click", function() {
          // adjust the width so that the info window is large enough for this many tabs
          if (htmls.length > 2) {
            htmls[0] = '<div style="width:'+htmls.length*88+'px">' + htmls[0] + '</div>';
          }
          var tabs = [];
          for (var i=0; i<htmls.length; i++) {
            tabs.push(new GInfoWindowTab(labels[i],htmls[i]));
          }
          marker.openInfoWindowTabsHtml(tabs);
        });
        
	GEvent.addListener(marker, 'mouseover', function() {
		marker.setImage(image2);
	});

	GEvent.addListener(marker, 'mouseout', function() {
		marker.setImage(image1);
	});

        return marker;
      }
      // ==================================================
      // === Create a custom Control ===
      var labelContainer;

      function LabelControl() {  }
      LabelControl.prototype = new GControl();

      LabelControl.prototype.initialize = function(map) {
        labelContainer = document.createElement("div");
        labelContainer.style.overflow="auto";
        labelContainer.style.textDecoration = "none";
        labelContainer.style.backgroundColor = "#E7E7F9";
        labelContainer.style.Color = "#000000";
        labelContainer.style.font = "small arial";
        labelContainer.style.border = "1px solid black";
        labelContainer.style.height="65px";
        labelContainer.style.width="160px";
        labelContainer.style.paddingLeft="5px";

        map.getContainer().appendChild(labelContainer);
        return labelContainer;
      }

      LabelControl.prototype.getDefaultPosition = function() {
        return new GControlPosition(G_ANCHOR_TOP_RIGHT, new GSize(7, 7));
      }

      // create the map
      var map = new GMap2(document.getElementById("map"));
      map.addControl(new GSmallMapControl());
			map.addControl(new LabelControl());
			
      var centerx='<h:outputText value="#{SimplexBean.currentProjectEntry.origin_lat}"/>';
      var centery='<h:outputText value="#{SimplexBean.currentProjectEntry.origin_lon}"/>';
      map.setCenter(new GLatLng(centerx,centery ), 9);

      var baseIcon = new GIcon();
      
      var select_flg="obsmarker";
			 // === This function picks up the click and opens the corresponding info window ===
      function myclick(i) {
      	if (i==1) {
      	  select_flg="obsmarker";
        }else if(i==2) {
        	select_flg="calmarker";
        }else if(i==3) {
        	select_flg="resmarker";
        }
        mgr.clearMarkers()
        AddmarkersToMap();
        mgr.refresh();
        //GEvent.trigger(markers[i], "click");
      }

			mgr = new MarkerManager(map, {maxZoom: 19});
	
	      // === put the assembled side_bar_html contents into the custom control ===
      var side_bar_html = "";
			side_bar_html += '<a href="javascript:myclick(' + 1 + ')">' + 'Observed Displacements' + '</a><br>';
			side_bar_html += '<a href="javascript:myclick(' + 2 + ')">' + 'Calculated Displacements' + '</a><br>';
			side_bar_html += '<a href="javascript:myclick(' + 3 + ')">' + 'Residual Displacements' + '</a><br>';
      labelContainer.innerHTML = side_bar_html;

			
      // ====== set up marker mouseover tooltip div ======
      var tooltip = document.createElement("div");
      map.getPane(G_MAP_FLOAT_PANE).appendChild(tooltip);
      tooltip.style.visibility="hidden";
			
			
			function AddmarkersToMap() {

			if(mgr) {
				mgr.clearMarkers();
			}
      // Read the data from the site
      var request = GXmlHttp.create();
      var remotehtmlname='<h:outputText value="#{SimplexBean.mapXmlUrl}"/>';
      request.open("GET", remotehtmlname, true);
      request.onreadystatechange = function() {
        if (request.readyState == 4) {
          var xmlDoc = request.responseXML;
          // obtain the array of markers and loop through it
          var markers = xmlDoc.documentElement.getElementsByTagName(select_flg);
          var mapinfo = xmlDoc.documentElement.getElementsByTagName("mapinfo");
          var original_lat= mapinfo[0].getAttribute("original_lat");
          var original_lon= mapinfo[0].getAttribute("original_lon");

		  // Build up non-map part of the document.
		  // We need some way to get at the elements that we 
		  // want to link to the map, but it doesn't matter how.
		  // I use a listings array here, but that wouldn't work on my
		  // real project. You might have an existing document,
		  // in which case you might just need to add ids to various
		  // elements.
	      var listings = [];
	      document.getElementById("sidebar").innerHTML = "";
          for (var i = 0; i < markers.length; i++) {
            // obtain the attribues of each marker
            var label = markers[i].getAttribute("label");
            var html = markers[i].getAttribute("tiphtml");
            var windowhtml = markers[i].getAttribute("windowhtml");
            
		      	var div = document.createElement("div");
			      div.className = "listing";
			      div.innerHTML = " ";
			
			      var a = document.createElement("a");
			      a.innerHTML = label;
			      div.appendChild(a);

			      //var detail = document.createElement("div");
			      //detail.innerHTML = html;
			      //detail.className = "detail";
			      //div.appendChild(detail);

			      document.getElementById("sidebar").appendChild(div);
			      listings[i] = div;

          }

          // Create the markers and link them up
          for (var i = 0; i < markers.length; i++) {
            // obtain the attribues of each marker
            var label = markers[i].getAttribute("label");
            var html = markers[i].getAttribute("tiphtml");
            var windowhtml = markers[i].getAttribute("windowhtml");
            var lat =( parseFloat(markers[i].getAttribute("lat")) )/100 + parseFloat(original_lat);
            var lng =( parseFloat(markers[i].getAttribute("lon")) )/100 + parseFloat(original_lon);
						var iconwidth=parseInt(markers[i].getAttribute("width"));
						var iconheight=parseInt(markers[i].getAttribute("height"));
						var mouseonimage='<h:outputText value="#{facesContext.externalContext.requestContextPath}/"/>'+markers[i].getAttribute("mouseonimage");
						var mouseoutimage='<h:outputText value="#{facesContext.externalContext.requestContextPath}/"/>'+markers[i].getAttribute("mouseoutimage");
						var textimage='<h:outputText value="#{facesContext.externalContext.requestContextPath}/"/>'+markers[i].getAttribute("textimage");


            var pt = new GLatLng(lat,lng);
            var lbl = markers[i].getAttribute("label");

						// Normal creating of a google marker
						//baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
			  		//baseIcon.iconSize = new GSize( 37,40 );
            baseIcon.iconSize = new GSize( iconwidth,iconheight );
			  		//baseIcon.shadowSize = new GSize(37, 34);
            baseIcon.iconAnchor = new GPoint(iconwidth/2, iconheight);
            baseIcon.infoWindowAnchor = new GPoint(iconwidth/2, 2);
			      baseIcon.infoShadowAnchor = new GPoint(iconwidth, 20);

			  		var icon = new GIcon(baseIcon);
			  		icon.image = mouseoutimage;
						//	        var marker = new GMarker(pt,icon);
				    //icon.label = {"url":"icon/50.png", "anchor":new GPoint(4,34), "size":new GSize(20,20)};
            icon.label = {"url":textimage, "anchor":new GPoint(4,iconheight-2), "size":new GSize(20,20)};
            
  					var marker = createInfoMarkerWithImage(pt,windowhtml,icon,mouseoutimage,mouseonimage);


            setupMarkerForTooltip(map, marker, html,i); // turn on tooltip support 
            setShowTooltipOnMouseover(marker);
						//map.addOverlay(marker);
						mgr.addMarker(marker,1,19);
						mgr.refresh();

			
						var e = listings[i]; // an element in the document we want to link
						// Tell this element to show the "i"th marker's tooltip.
						addTooltipOnMouseover(e, i);
						// Tell the marker to change this element's class when moused over
						changeClassOnMarkerMouseover(e, i);
          }

        }
      }
      request.send(null);
				
			}

			AddmarkersToMap();



    }

    else {
      alert("Sorry, the Google Maps API is not compatible with this browser");
    }

    //]]>
    </script>



	<h:form>
		<hr />
		<h:commandLink action="Simplex2-back">
			<h:outputText value="#{SimplexBean.codeName} Main Menu" />
		</h:commandLink>
	</h:form>
</f:view>
</body>

</html>



