// $Id: bfograph.js,v 1.7 2006/08/01 00:27:51 mike Exp $

// This file contains JavaScript functions that can be used to create
// interactive graphs. The JavaScript is used for both interactive
// bitmap graphs (image rollovers) and SVG.
//
// The first three functions are required for correct display of values on
// Line series. They should not be altered.
//
// The remaining functions can be used to create "tooltip" type windows
// in rollovers, but are not required for proper functioning and may be
// modified or deleted as required.
//
// Methods in this file have been tested with Firefox, Safari 2.0 and IE6.
// The SVG code doesn't yet work on Safari, which appears to have trouble
// loading externally references JavaScript files from an SVG.
//
// All code in this file is under the Public Domain, and end users may do
// anything they like with it.
//

/**
 * Given a date as a numeric value as used inside the Graph library, and an
 * optional format in the same format as the java.util.SimpleDateFormat class,
 * return the date formatted as a String
 * 
 * date   - the date as a numeric. Usually the value of "seriesx"
 * format - (optional) the format to use to format it - defaults to "dd-MMM-yyyy"
 */
function bfgToDate(date,format)
{
    if (date!=date) return null;
    date = new Date((date+788940000)*1000);
    var MONTH_NAMES=new Array('January','February','March','April','May','June','July','August','September','October','November','December','Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec');
    var DAY_NAMES=new Array('Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sun','Mon','Tue','Wed','Thu','Fri','Sat');

    if (!format) format="dd-MMM-yyyy";
    var y=date.getYear()+"";
    var M=date.getMonth()+1;
    var d=date.getDate();
    var E=date.getDay();
    var H=date.getHours();
    var m=date.getMinutes();
    var s=date.getSeconds();

    var value=new Object();
    if (y.length < 4) y=""+(y-0+1900);
    value["y"]=""+y;
    value["yyyy"]=y;
    value["yy"]=y.substring(2,4);
    value["M"]=M;
    value["MM"]=M<10 ? "0"+M : M;
    value["MMMM"]=MONTH_NAMES[M-1];
    value["MMM"]=MONTH_NAMES[M+11];
    value["d"]=d;
    value["dd"]=d<10 ? "0"+d : d;
    value["E"]=DAY_NAMES[E+7];
    value["EE"]=DAY_NAMES[E];
    value["H"]=H;
    value["HH"]=H<10 ? "0"+H : H;
    value["h"] = H>=12 ? H-12 : H;
    value["hh"]=value["h"]<10 ? "0"+value["h"] : value["h"];
    value["K"]=H>11 ? H-12 : H;
    value["k"]=H+1;
    value["KK"]=value["K"]<10 ? "0"+value["K"] : value["K"];
    value["kk"]=value["k"]<10 ? "0"+value["k"] : value["k"];
    value["a"]=H>11 ? "PM" : "AM";
    value["m"]=m;
    value["mm"]=m<10 ? "0"+m : m;
    value["s"]=s;
    value["ss"]=s<10 ? "0"+s : s;

    var i=0;
    var result="";
    while (i<format.length) {
	var c=format.charAt(i);
	var token="";
	while (i<format.length && c==format.charAt(i)) {
	    token += format.charAt(i++);
	}
	result += value[token]==null ? token : value[token];
    }
    return result;
}

/**
 * Given an X and an array of 4-tuple values of the form (x1,y1,x2,y2),
 * interpolate x between the appropriate x1 and x2 and return the y value.
 * Called internally, no need for users to call it.
 *
 * x - the X co-ordinate
 * a - the array of co-ordinates
 */
function bfgFunc(x,a)
{
    for (var i=0;i<a.length;i+=4) {
        if (x>=a[i] && x<=a[i+2]) {
	    var y = (((x-a[i])/(a[i+2]-a[i])) * (a[i+3]-a[i+1])) + a[i+1];
	    return y;
	}
    }
    return NaN;
}

/**
 * Round val to the specified number of decimal places
 * val the value to round
 * dp the number if decimal places to use
 */
function bfgRound(val, dp)
{
    if (dp<=0) {
        return Math.round(val);
    } else {
        var z = ""+val;
        var ix = z.indexOf(".");
        return z.substring(0, Math.min(ix+dp+1, z.length));
    }
}


// ----------- Remaining functions are convenient ways to use -------------
// ----------- popup windows with SVG and HTML images.        -------------
// ----------- They can be altered or removed as necessary.   -------------


var bfgHider;

function bfgIsSVG() {
    return this.SVGDocument && document instanceof SVGDocument;
}

function bfgShowPopup(msg, event)
{
    if (bfgIsSVG()) {
        if (!document.getElementById("bfgPopup.rect")) {
            var popupText = document.createElementNS("http://www.w3.org/2000/svg", "text");
            popupText.setAttribute("id", "bfgPopup.text");
            popupText.setAttribute("visibility", "hidden");
            popupText.setAttribute("fill", "black");
            popupText.setAttribute("font-family", "sans-serif");
            popupText.setAttribute("font-size", 10);
            popupText.appendChild(document.createTextNode("label"));

            var popupRect = document.createElementNS("http://www.w3.org/2000/svg", "rect");
            popupRect.setAttribute("id", "bfgPopup.rect");
            popupRect.setAttribute("visibility", "hidden");
            popupRect.setAttribute("fill", "lightyellow");
            popupRect.setAttribute("height", "15");
            popupRect.setAttribute("stroke", "black");
            popupRect.setAttribute("stroke-width", "0.5");
            
            // Add them to the first group
            //
            var firstGroup = document.getElementsByTagName("g").item(0);
            firstGroup.appendChild(popupRect);
            firstGroup.appendChild(popupText);
        }
        var popupRect = document.getElementById("bfgPopup.rect");
        var popupText = document.getElementById("bfgPopup.text");
        
        // Get the title attribute from the firt node of the class
        //
        var child = popupText.firstChild;
        if (child.data != msg) {
            popupText.replaceChild(document.createTextNode(msg), child);
        }

        popupRect.setAttribute("x", event.clientX+10);
        popupRect.setAttribute("y", event.clientY);
        popupRect.setAttribute("width", msg.length*6);      // TODO Could be more accurate?
        popupText.setAttribute("x", event.clientX+15);
        popupText.setAttribute("y", event.clientY+10);
        popupText.setAttribute("visibility", "visible");
        popupRect.setAttribute("visibility", "visible");
    } else {
        var popup = document.getElementById("bfgPopup");
        if (popup==null) {
            popup = document.createElement("div");
            popup.style.position = "absolute";
            popup.id = "bfgPopup";
            var body = document.getElementsByTagName("body")[0];
            if (body==null) {               // No body, try appending to document
                body = document;
            }
            body.appendChild(popup);
        }


        // Here are the values we can expect to be set in the four main browsers:
        // 				pageY	clientY		documentElement.scrollTop	body.scrollTop
        // Safari (quirks/strict)	page	window		set				set
        // Mozilla quirks		page	window		set				set
        // Opera (quirks/strict)	page	window		set				set
        // Mozilla strict		page	window		set				0
        // IE6 quirks			undef	window		0				set
        // IE6 strict			undef	window		set				0
        //
        var x = event.pageX ? event.pageX : event.clientX + document.documentElement.scrollLeft + document.body.scrollLeft;
        var y = event.pageY ? event.pageY : event.clientY + document.documentElement.scrollTop  + document.body.scrollTop;
        popup.innerHTML="<div style='padding:2px; border:1px solid #000; background-color:#ffd; white-space:nowrap; font:8pt sans-serif'>"+msg+"</div>";
        popup.style.left = (x+15)+"px";
        popup.style.top = (y-5)+"px";
        popup.style.visibility = "visible";
    }
    if (bfgHider) clearTimeout(bfgHider);
}

function bfgHidePopup(){
    bfgHider = setTimeout("_bfgHidePopup()", 500);
}

function _bfgHidePopup()
{
    if (bfgIsSVG()) {
        document.getElementById("bfgPopup.rect").setAttribute("visibility", "hidden");
        document.getElementById("bfgPopup.text").setAttribute("visibility", "hidden");
    } else {
        document.getElementById("bfgPopup").style.visibility = "hidden";
    }
}
