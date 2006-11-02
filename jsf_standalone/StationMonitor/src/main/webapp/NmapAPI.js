//N_map_API 0.1
//Load Javascript function.
function K_LoadJS(src,handle)
{
  var jsFile= document.createElement("script");
  jsFile.src=src;
  jsFile.onreadystatechange=function(){if(this.readyState=="complete" || this.readyState=="loaded"){if(handle)handle.call()}};
  document.body.appendChild(jsFile);
}
//read URL function
function K_GetQueryString(key)
{
  var returnValue =""; 
  var sURL = window.document.URL;
  if (sURL.indexOf("?") > 0)
  {
    var arrayParams = sURL.split("?");
    var arrayURLParams = arrayParams[1].split("&");
    for (var i = 0; i < arrayURLParams.length; i++)
    {
      var sParam =  arrayURLParams[i].split("=");

      if ((sParam[0] ==key) && (sParam[1] != ""))
        returnValue=sParam[1];
    }
  }
  return returnValue;
}
//read cookie function.
function K_GetCookieVal(offset)
{
  var endstr = document.cookie.indexOf (";", offset);
  if (endstr == -1)
    endstr = document.cookie.length;
  return unescape(document.cookie.substring(offset, endstr));
}
function K_SetCookie(name, value)
{
  var expdate = new Date();
  var argv = K_SetCookie.arguments;
  var argc = K_SetCookie.arguments.length;
  var expires = (argc > 2) ? argv[2] : null;
  var path = (argc > 3) ? argv[3] : null;
  var domain = (argc > 4) ? argv[4] : null;
  var secure = (argc > 5) ? argv[5] : false;
  if(expires!=null) expdate.setTime(expdate.getTime() + ( expires * 1000 ));
  document.cookie = name + "=" + escape (value) +((expires == null) ? "" : ("; expires="+ expdate.toGMTString()))+((path == null) ? "" : ("; path=" + path)) +((domain == null) ? "" : ("; domain=" + domain))+((secure == true) ? "; secure" : "");
}
function K_DelCookie(name)
{
  var exp = new Date();
  exp.setTime(exp.getTime() - 1);
  var cval =K_GetCookie(name);
  document.cookie = name + "=" + cval + "; expires="+ exp.toGMTString();
}
function K_GetCookie(name)
{
  var arg = name + "=";
  var alen = arg.length;
  var clen = document.cookie.length;
  var i = 0;
  while (i < clen)
  {
    var j = i + alen;
    if (document.cookie.substring(i, j) == arg)
      return K_GetCookieVal(j);
    i = document.cookie.indexOf(" ", i) + 1;
    if (i == 0) break;
  }
  return null;
}
//callback function.
function K_GetCallBack(obj,func)
{
  return function(){return func.apply(obj,arguments)};
}

// Create the marker and corresponding information window 
function createInfoMarker(point, address, icon) {
  var marker = new GMarker(point,icon);
  GEvent.addListener(marker, "click", function()
  {
    marker.openInfoWindowHtml(address);
  } );
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

function refimg(elementId,url){
	objimg = document.getElementById(elementId);
	objimg.setAttribute('src',url,0);
}

function load() {
  if (GBrowserIsCompatible()) {
    var map = new GMap2(document.getElementById("map"));
    map.addControl(new GSmallMapControl());
    map.addControl(new GMapTypeControl());
    map.setCenter(new GLatLng(37.4419, -122.1419), 13);
    // Our info window content
    var infoTabs = [
      new GInfoWindowTab("Tab #1", "This is tab #1 content"),
        new GInfoWindowTab("Tab #2", "This is tab #2 content")
    ];

    // Place a marker in the center of the map and open the info window
    // automatically
    var marker = new GMarker(map.getCenter());
    GEvent.addListener(marker, "click", function() {
      marker.openInfoWindowTabsHtml(infoTabs);
    });
    map.addOverlay(marker);
    marker.openInfoWindowTabsHtml(infoTabs);
  }
}  

