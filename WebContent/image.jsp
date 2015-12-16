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
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<title> FASTDB Finding Chart Tool </title>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link href="css/tools.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
      #toc   {position:absolute;top:10px;left:10px;}
      #content {position:absolute;top:0px;left:200px;background-color:White;}
    </style>
    
    <link rel="SHORTCUT ICON" href="../../../FavIcon.ICO" />
    
    <script src="js/ctrlscript.js" type="text/javascript"></script>
    <script type="text/javascript">
        function init(def) {
          //loadZoombar();
          drawzoombar();
            if (def == "0") {	// the opt came from the caller, set the checkboxes
                setoptstr(0);

                //();
            }
        }

        function popup() {
            var s = "printchart.aspx" + qstring();
            var w = window.open(s, "PRINTIMAGE", "width=700,height=840,resizable=yes,scrollbars=auto,menubar=yes");
            w.focus();
            return false;
        }

        function qstring() {
            var s = "";
            s += "?ra=" + document.getElementById("getjpeg").ra.value;
            s += "&dec=" + document.getElementById("getjpeg").dec.value;
            s += "&scale=" + document.getElementById("getjpeg").scale.value;
            s += "&width=" + document.getElementById("getjpeg").width.value;
            s += "&height=" + document.getElementById("getjpeg").height.value;
            s += "&opt=" + document.getElementById("getjpeg").opt.value;
            // s += "&query="+document.getjpeg.query.value; // it doesn't work. Something is missing ???
            return s;
        }
    </script>
	
  </head>
  
  <body>
  	<script type="text/javascript">
        var branchname = "chart";
    </script>
    <form method="post" action="<c:url value='/v1/image/img'/>" id="getjpeg">
<div class="aspNetHidden">
	<input type="hidden" name="__VIEWSTATE" id="__VIEWSTATE" value="/wEPDwUKMTk5Njc3MjI4MGRks2s6M5cwgCiN0bDkRqvc0WT1rNonaEwthD93YOkkL1s=" />
</div>

<div class="aspNetHidden">
	<input type="hidden" name="__VIEWSTATEGENERATOR" id="__VIEWSTATEGENERATOR" value="344C55C3" />
</div>
    <div id="toc">
        <table width="180" border="0" cellspacing="0" cellpadding="2" bgcolor=black>
	    <tr>
		    <td width="40"><a href="/dr12/" TARGET="_top">
		    <img src="images/planet.jpg" border="0" width="40" height="50"></a></td>
		    <td class='title' align="left" width="140">&nbsp;&nbsp;FASTDB</td>
	    </tr>
        </table>
        <table border="0" cellspacing="0" cellpadding="0" >
	        <tr>
                <td class='s' align="left" ONMOUSEOVER="this.T_WIDTH='140';return escape('SkyServer Home page')">|<a target="_top" href="<c:url value='/index.jsp'/>">Home&nbsp;|</a></td>
                <td class='s' align="left" ONMOUSEOVER="this.T_WIDTH='140';return escape('Help on the current tool')"><a target="MosaicWindow" href="<c:url value='/chartinfo.jsp'/>">Help&nbsp;|</a></td>
                        <td class='s' align="left" ONMOUSEOVER="this.T_WIDTH='140';return escape('Go to the Image List')"><a href="<c:url value='/listinfo.jsp'/>" TARGET="_top">List&nbsp;|</a></td>
					    <td class='s' align="left" ONMOUSEOVER="this.T_WIDTH='140';return escape('Go to the Navigate Tool')"><a href="javascript:void(0)" onclick="return gotonavi();">Navi&nbsp;|</a></td>
		        <td class='s' align="left" ONMOUSEOVER="this.T_WIDTH='140';return escape('Go to the Object Explorer')"><A href="javascript:void(0)" onclick="return gotoExp();">Explore&nbsp;|</A></td>	
	        </tr>
        </table>

        <!-- <table width="180" cellpadding=0 cellspacing=0 border=0> -->
            <table width="180" bgColor="lightblue" border="1" cellspacing=1 cellpadding=1 valign="middle">
			    <tr>
				    <td colspan="2" align="center" bgcolor="skyblue">Parameters</td>
			    </tr>
                <tr>
	<td><span  onmouseover="this.T_TEMP='3000';this.T_WIDTH='180';return escape('R.A. in decimal degrees,<br> HH MM SS, or HH:MM:SS')">ra</span></td>
		<td><table cellspacing=0 cellpadding=0><tr><td>
			<INPUT class='in' type='text' size='3' maxlength='20' align='right'
			value='${ra==null?132.749:ra}' name='ra' id='ra' onChange='setra();' ></td><td>&nbsp;<span  ONMOUSEOVER="this.T_TEMP='3000';this.T_WIDTH='180';return escape('R.A. in decimal degrees,<br> HH MM SS, or HH:MM:SS')">deg			</span></td></tr></table>
		</td>
</tr>
<tr>
	<td><span  ONMOUSEOVER="this.T_TEMP='3000';this.T_WIDTH='180';return escape('Declination in decimal degrees,<br> DD MM SS, or DD:MM:SS')">dec</span></td>
		<td><table cellspacing=0 cellpadding=0><tr><td>
			<INPUT class='in' type='text' size='3' maxlength='20' align='right'
			value='${dec==null?11.656:dec}' name='dec' id='dec' onChange='setdec();' ></td><td>&nbsp;<span  ONMOUSEOVER="this.T_TEMP='3000';this.T_WIDTH='180';return escape('Declination in decimal degrees,<br> DD MM SS, or DD:MM:SS')">deg			</span></td></tr></table>
		</td>
</tr>
<tr>
	<td><span  ONMOUSEOVER="this.T_TEMP='3000';this.T_WIDTH='180';return escape('Chart scale in arcsec per pixel')">scale</span></td>
		<td><table cellspacing=0 cellpadding=0><tr><td>
			<INPUT class='in' type='text' size='3' maxlength='20' align='right'
			value='${scale==null?0.79224:scale}' name='scale' id='scale' onChange='setscale(0)' ></td><td>&nbsp;<span  ONMOUSEOVER="this.T_TEMP='3000';this.T_WIDTH='180';return escape('Chart scale in arcsec per pixel')">''/pix			</span></td></tr></table>
		</td>
</tr>
<tr>
	<td><span  ONMOUSEOVER="this.T_TEMP='3000';this.T_WIDTH='180';return escape('Chart width in pixels')">width</span></td>
		<td><table cellspacing=0 cellpadding=0><tr><td>
			<INPUT class='in' type='text' size='3' maxlength='20' align='right'
			value='${width==null?512:width}' name='width' id='width' onChange='setwidth();' ></td><td>&nbsp;<span  ONMOUSEOVER="this.T_TEMP='3000';this.T_WIDTH='180';return escape('Chart width in pixels')">pix			</span></td></tr></table>
		</td>
</tr>
<tr>
	<td><span  ONMOUSEOVER="this.T_TEMP='3000';this.T_WIDTH='180';return escape('Chart height in pixels')">height</span></td>
		<td><table cellspacing=0 cellpadding=0><tr><td>
			<INPUT class='in' type='text' size='3' maxlength='20' align='right'
			value='${height==null?512:height}' name='height' id='height' onChange='setheight();' ></td><td>&nbsp;<span  ONMOUSEOVER="this.T_TEMP='3000';this.T_WIDTH='180';return escape('Chart height in pixels')">pix			</span></td></tr></table>
		</td>
</tr>
<tr>
	<td><span  ONMOUSEOVER="this.T_TEMP='3000';this.T_WIDTH='180';return escape('See short-hand Drawing Options on Help page')">opt</span></td>
		<td><table cellspacing=0 cellpadding=0><tr><td>
			<INPUT class='in' type='text' size='3' maxlength='20' align='right'
			value="${opt}" name='opt' id='opt' onChange='setoptstr(1);' ></td><td>&nbsp;<span  ONMOUSEOVER="this.T_TEMP='3000';this.T_WIDTH='180';return escape('See short-hand Drawing Options on Help page')">			</span></td></tr></table>
		</td>
</tr>

            </table>

            <table width="180" cellspacing=4 cellpadding=0 border=0>
	            <tr><td align="center" ONMOUSEOVER="this.T_WIDTH='140';return escape('Get an image of the sky at the specified coordinates')">
	            <a href="javascript:void(0);" onClick="return resubmit();"><img 
			            src="images/get_image.jpg" ALT="Submit" 
			            border="0" WIDTH="112" HEIGHT="40"></a>
	            </td>
	            <td align="center" ONMOUSEOVER="this.T_WIDTH='140';return escape('Open a new window with a printable image')">
	            <a href="javascript:void(0);" onClick="return popup();"><img 
			            src="images/printer_icon_blue.jpg" ALT="Printable Image" 
			            border="0" WIDTH="40" HEIGHT="40"></a>
	            </td></tr>
	
            </table>

            <table width="180" cellpadding=0 cellspacing=0 border=0>
            <tr>
	            <td><img src="./images/zoombar6.jpg" ALT="Zoom In" 
			            border="0" WIDTH="160" HEIGHT="32" ISMAP 
			            USEMAP="#zoom_bar_map" id="zoombar"></td>
            </tr>
            </table>

            <p />

            <map name="zoom_bar_map">
            <area shape="rect" alt="zoom in" coords=" 2,8,24,28" href="javascript:void(0);" onmouseover="this.T_WIDTH='140';return escape('Zoom in')" onclick="return stepzoom(0.5)"/>
            <area shape="rect" alt="zoom=5"  coords="25,8,30,24" href="javascript:void(0);" onclick="return setzoom(5)"/>
            <area shape="rect" alt="zoom=4"  coords="33,8,38,24" href="javascript:void(0);" onclick="return setzoom(4)"/>
            <area shape="rect" alt="zoom=3"  coords="41,8,46,24" href="javascript:void(0);" onclick="return setzoom(3)"/>
            <area shape="rect" alt="zoom=2"  coords="50,8,55,24" href="javascript:void(0);" onclick="return setzoom(2)"/>
            <area shape="rect" alt="zoom=1"  coords="58,8,63,22" href="javascript:void(0);" onclick="return setzoom(1)"/>
            <area shape="rect" alt="zoom=0"  coords="66,8,71,24" href="javascript:void(0);" onclick="return setzoom(0)"/>
            <area shape="rect" alt="zoom=-1" coords="75,8,80,24" href="javascript:void(0);" onclick="return setzoom(-1)"/>
            <area shape="rect" alt="zoom=-2" coords="83,8,88,24" href="javascript:void(0);" onclick="return setzoom(-2)"/>
            <area shape="rect" alt="zoom=-3" coords="91,8,96,24" href="javascript:void(0);" onclick="return setzoom(-3)"/>
            <area shape="rect" alt="zoom=-4" coords="99,8,104,24" href="javascript:void(0);" onclick="return setzoom(-4)"/>
            <area shape="rect" alt="zoom=-5" coords="107,8,112,24" href="javascript:void(0);" onclick="return setzoom(-5)"/>
            <area shape="rect" alt="zoom=-6" coords="115,8,120,24" href="javascript:void(0);" onclick="return setzoom(-6)"/>
            <area shape="rect" alt="zoom=-7" coords="123,8,128,24" href="javascript:void(0);" onclick="return setzoom(-7)"/>
            <area shape="rect" alt="zoom out" coords="132,6,156,28" href="javascript:void(0);" onmouseover="this.T_WIDTH='140';return escape('Zoom out')" onclick="return stepzoom(2.0)">
            </map><!-- 
            <table width="180" cellpadding=0 cellspacing=0 border=0>
	            <tr>
		            <td ONMOUSEOVER="this.T_OFFSETY='15';this.T_TEMP='3000';this.T_WIDTH='180';return escape('Mark selected objects with a purple triangle. See &quot;Help&quot; for options.')">Use query to mark objects</td>
	            </tr>
	

            </table>
            <table border=0 ONMOUSEOVER="this.T_OFFSETY='15';this.T_TEMP='3000';this.T_WIDTH='180';return escape('Mark selected objects with a purple triangle. See &quot;Help&quot; for options.')">
              <tr>
                <td>
                  <TEXTAREA wrap=off class='tx' cols=17 name="query" rows=5></TEXTAREA> 
	            </td>
              </tr> 
            </table>-->
            <table width="180" cellpadding=0 cellspacing=0 border=1>
	         
           <tr><td align=middle bgcolor="skyblue" ONMOUSEOVER="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Mark various options on the image')">Drawing options</td></tr>
	<tr><td bgColor="lightblue" ONMOUSEOVER="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Grid with tickmarks to show image scale')"><INPUT type="checkbox" onclick="setopt(this,'G')" name="Grid"  id="Grid"> Grid</td></tr>
	<tr><td bgColor="lightblue" ONMOUSEOVER="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Information on the Navigation window view')"><INPUT type="checkbox" onclick="setopt(this,'L')" name="Label"  id="Label"> Label</td></tr>
	<tr><td bgColor="lightblue" ONMOUSEOVER="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Marks photometrically identified objects with light blue circles')"><INPUT type="checkbox" onclick="setopt(this,'P')" name="PhotoObjs"  id="PhotoObjs"> Photometric objects</td></tr>
	<tr><td bgColor="lightblue" ONMOUSEOVER="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Marks spectroscopic objects with red squares')"><INPUT type="checkbox" onclick="setopt(this,'S')" name="SpecObjs"  id="SpecObjs"> Objects with spectra</td></tr>
	<tr><td bgColor="lightblue" ONMOUSEOVER="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Inverts black and white in the image')"><INPUT type="checkbox" onclick="setopt(this,'I')" name="InvertImage"   id="InvertImage"> Invert Image</td></tr>	
	<tr><td align=middle bgcolor="skyblue">Advanced options</td></tr>	
	<!--
	<tr><td bgColor="lightblue" ONMOUSEOVER="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Marks potential spectroscopy targets with green Xs')"><INPUT type="checkbox" onclick="setopt(this,'T')" name="TargetObjs"  id="TargetObjs"> Spectroscopic Targets</td></tr>

    <tr><td bgColor="lightblue" ONMOUSEOVER="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Shows APOGEE DATA')"><INPUT type="checkbox" onclick="setopt(this,'A')" name="APOGEE" id="Checkbox3"> APOGEE Spectra</td></tr>		
	<tr><td bgColor="lightblue" ONMOUSEOVER="this.T_TEMP='5000';this.T_WIDTH='140';return escape('Draws the outline (green) of each photometric object except at the largest zoom-out scales (where they are not legible)')"><INPUT type="checkbox" onclick="setopt(this,'O')" name="Outline"  id="Outline">SDSS Outlines</td></tr>
	<tr><td bgColor="lightblue" ONMOUSEOVER="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Draws a rectangular box (pink) around each photometric object')"><INPUT type="checkbox" onclick="setopt(this,'B')" name="BoundingBox" id="BoundingBox">SDSS Bounding Boxes</td></tr> -->
	<tr><td bgColor="lightblue" ONMOUSEOVER="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Shows each SDSS field (~10x14 arcmin) in gray')"><INPUT type="checkbox" onclick="setopt(this,'F')" name="Fields" id="Fields">FAST Fields</td></tr>
<!-- <tr><td bgColor="lightblue" ONMOUSEOVER="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Shows masks (pink) around bright objects and data artifacts')"><INPUT type="checkbox" onclick="setopt(this,'M')" name="Masks" id="Masks">SDSS Masks</td></tr> 
	<tr><td bgColor="lightblue" ONMOUSEOVER="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Shows plates (lavender) used to collect spectra')"><INPUT type="checkbox" onclick="setopt(this,'Q')" name="Plates" id="Plates">SDSS Plates</td></tr>		-->

           
           </table>
    </div>
        <div id="content">
        <img src="<c:url value='/v1/image/Jpeg?ra=${ra}&dec=${dec}&scale=${scale}&height=${height}&width=${width}&opt=${opt}'/>"/>
   		</div>
    </form>
    <script type="text/javascript">init('1');</script>
    <script language="JavaScript" type="text/javascript" src="js/wz_tooltip.js"></script>
  	
  </body>
</html>
