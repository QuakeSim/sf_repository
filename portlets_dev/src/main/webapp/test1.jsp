<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

	<meta http-equiv="content-type" content="text/html; charset=iso-8859-1"/>
	<title>jQuery treeView</title>
	
<link rel="stylesheet" type="text/css" href="jquery.treeview.css">

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.min.js"></script>
<script type="text/javascript" src="@host.base.url@Disloc3/lib/jquery.cookie.js"></script>
<script type="text/javascript" src="@host.base.url@Disloc3/jquery.treeview.js"></script>

<script type="text/javascript" src="@host.base.url@Disloc3/simpletreemenu.js"></script>

<script type="text/javascript" src="@host.base.url@Disloc3/demo.js"></script>
	
</head>


<body>

<div id="div1">
</div>


<script type="text/javascript">


function showKML(div1){

document.getElementById(div1).innerHTML = '<ul id="navigation"> <li>Item 1 <ul> <li>Item 1.1</li> </ul> </li> </ul>';

}

function showKML2(){

showKML('div1');

}


// showKML('div1');
// showKML2();
</script>

<a href="#" onclick="showKML('div1')">Show allQTFaults.kml</a>


<hr />


