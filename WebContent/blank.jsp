<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'blank.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<style type="text/css">
		a       {font-family:sans-serif;font-weight:bold;color:#000000;font-size:9pt;}
        .d		{font-size:9pt;font-family:sans-serif;color:#000000;}
        .c		{font-size:9pt;font-family:sans-serif;color:#88ff88;}
        #start  {position: absolute; left: 20px; top:475px;font-family:sans-serif;color:#000000;font-size:9pt}
        #qframe {position: absolute; left:  0px; top:20px;z-index:1;width:150px;}
	    <!--
	    #sframe {position: absolute; left:  0px; top:470px;z-index:1}-->
	    #sframe {position: absolute; left:  0px; top:401px;z-index:1}
        #query  {position: absolute; left: 24px; top:37px;z-index:5;}
        #links  {position: absolute; left: 20px; top:355px;}
        <!--
	    #thumbnail  {position: absolute; left: 20px; top:479px;z-index:5}
       -->
       #thumbnail  {position: absolute; left: 20px; top:410px;z-index:5}
        #check  {position: absolute; left:  0px; top: 0px;visibility:hidden;width:12px;}
        #zimage {position: absolute; left:11px; top:211px;z-index:3;}
        #zframe {position: absolute; left: 0px; top:200px;z-index:5;width:150px;}
        #nearlabel {position: absolute; left:32px; top:6px;z-index:5;
			    width:150px;font-family:sans-serif;color:#000000;
			    font-size:9pt;z-index:10;width:110px;}
        #test  {position: absolute; left: 20px; top:150px;font-family:sans-serif;color:#000000;font-size:9pt}
	</style>
	 <script src="/naviClass.js"></script>
    <script type="text/javascript">

var check;

function quicklook(id) {
	var url = "../quicklook/quickobj.aspx?id="+id;
	var w = window.open(url,'_blank');
	w.focus();
}

function explore(id) {
	var url = "../explore/obj.aspx?id="+id;
	var w = window.open(url,'_blank');
	w.focus();
}

function exploreAPOGEE(id) {
    var url = "../explore/summary.aspx?apid=" + id;
    var w = window.open(url, '_blank');
    w.focus();
}

function saveBook(id) {
	check.show();
	var url = "book.aspx?add=" + id;
	var frame = document.getElementById("test");
	frame.src = url;
}

function init() {
	check = new Div('check',120,416);
	//check.show();
	check.hide();
}

function showBook() {
	w = window.open("book.aspx","POPUP");
	w.focus();
}

function showSTARTBall() {
  w = window.open("STARTFrame.html","STARTBall","width=300,height=325");
  w.focus();
}

function showSTARTBallHelp() {
  w = window.open("skymapshelp.aspx","help","width=800,height=600,resizeable,scrollbars");
  w.focus();
}

function recenter(ra_, dec_) {
    
    window.parent.document.getElementById("ra").value = ra_;
    window.parent.document.getElementById("dec").value = dec_;
    //alert(window.parent.document.getElementById("getImageId").value);
    window.parent.document.getElementById('getImageId').disabled = false;
    window.parent.document.getElementById("getImage").click();
    
	//var s = "navictrl.aspx?ra="+ra_+"&dec="+dec_;
	//s += "&scale=<%="(4*qscale)"%>&opt=<%="opt"%>";
	//top.frames.ctrl.location.href=s;
}
</script>
  </head>
  
  <body onload="init()">
    <div id="nearlabel">Selected object </div>
	<div id="qframe"><img src='images/queryframe.gif' width="150" height="180"></div>
	<div id="query">
	<table width="100" border="0" cellspacing="1" bgcolor="#000000">
	<tbody><tr valign="top" onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Right ascension (ra) is like longitude in the sky')"><td width="20" align="left" class="c">ra</td><td align="right" class="c">${poa.ra }</td></tr>
	<tr valign="top" onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Declination (dec) is like latitude in the sky')"><td width="20" align="left" class="c">dec</td><td align="right" class="c">${poa.dec }</td></tr>
	<tr valign="top" onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('STAR or GALAXY (but some bright stars are misclassified as galaxies)')"><td width="20" align="left" class="c">type</td><td align="right" class="c">${poa.type }</td></tr>
	<tr valign="top" onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Magnitude (brightness) in the ultraviolet (u) wavelength')"><td width="20" align="left" class="c">u</td><td align="right" class="c">${poa.u }</td></tr>
	<tr valign="top" onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Magnitude (brightness) in the green (g) wavelength')"><td width="20" align="left" class="c">g</td><td align="right" class="c">${poa.g }</td></tr>
	<tr valign="top" onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Magnitude (brightness) in the red (r) wavelength')"><td width="20" align="left" class="c">r</td><td align="right" class="c">${poa.r }</td></tr>
	<tr valign="top" onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Magnitude (brightness) in the shorter infrared (i) wavelength')"><td width="20" align="left" class="c">i</td><td align="right" class="c">${poa.i }</td></tr>
	<tr valign="top" onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Magnitude (brightness) in the farther infrared (z) wavelength')"><td width="20" align="left" class="c">z</td><td align="right" class="c">${poa.z }</td></tr>
	</tbody></table>
	</div>
	
	<div id="zimage">
	<img src="${zImageUrl}" width="128" height="128">
	</div>
	<div id="zframe"><img src="images/mglass.gif" width="150" height="150" onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Close-up image of selected object')"></div>
 	<div id="links">
	  <table border="0" cellspacing="2" cellpadding="0">
		<tbody>
		<!--  <tr onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('See this object’s basic data in the Quick Look tool')"><td width="20"><a href="javascript:quicklook('1237654669203013744')">
		    <img src="images/button.gif" width="20" height="20" border="0"></a></td>
			<td width="80"><a href="javascript:quicklook('1237654669203013744')">Quick Look</a></td></tr>
			<tr onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('See this object’s complete data in the Object Explorer')"><td width="20"><a href="javascript:explore('1237654669203013744')">
			<img src="images/button.gif" width="20" height="20" border="0"></a></td>
			<td width="80"><a href="javascript:explore('1237654669203013744')">Explore</a></td></tr>-->
			<tr onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Redraw the image with this object in the center')"><td width="20"><a href="javascript:recenter(159.815,-0.655)">     <img src="images/button.gif" width="20" height="20" border="0"></a></td><td width="80"><a href="javascript:recenter(159.815,-0.655)">Recenter</a></td></tr>
			<!-- 
			<tr onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Save this object to your online notebook')"><td width="20"><a href="javascript:saveBook('1237654669203013744')">     <img src="images/button.gif" width="20" height="20" border="0"></a></td><td width="80"><a href="javascript:saveBook('1237654669203013744')">Add to notes</a></td></tr>
			<tr onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Show your online notebook')"><td width="20"><a href="javascript:showBook()">     <img src="images/button.gif" width="20" height="20" border="0"></a></td><td width="80"><a href="javascript:showBook()">Show notes</a></td></tr>
	 	 	 --></tbody>
	 	</table>
</div>
  <c:if test="${specObjID ne -1 }">
 	<div id="sframe"><img src="images/specframe.png"></div>
 	<div id="thumbnail">
		<table border="0">
			<tbody>
			  <tr>
				<td>
				<a href="<c:url value='/v1/image/SpecById/${specObjID}'/>" target="_blank"><img src="<c:url value='/v1/image/SpecById/${specObjID }'/>" width="128" height="102" border="0" align="left"></a>
				</td>
			  </tr>
			</tbody>
		</table>
	</div>
  </c:if>
  </body>
</html>
