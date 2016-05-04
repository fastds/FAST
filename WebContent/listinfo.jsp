<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	 <base href="<%=basePath%>"/>
	<link href="css/tools.css" rel="stylesheet" type="text/css" /> 
    <style type="text/css">
      #toc   {position:absolute;top:10px;left:10px;}
      #content {position:absolute;top:0px;left:200px;background-color:White;}
      .tx	 	{WIDTH:160px; TEXT-ALIGN: left; FONT-SIZE: 8pt;}
    </style>
    <script src="js/ctrlscript.js" type="text/javascript"></script>
    <title>FASTDB listinfo</title>
</head>
<body>
    <script type="text/javascript">
        var branchname = "list";
    </script>
    <script type="text/javascript">
      function init(def) {
            //loadZoombar();
            drawzoombar();
            
            if (def == "0") {	// the opt came from the caller, set the checkboxes
                setoptstr(0);
                //drawzoombar();
                //resubmit();
            }
        }

        function popup() {
            var s = "blank.htm";
            var w = window.open(s, "PRINTIMAGE",
			    "width=720,height=940,resizable=yes,scrollbars,menubar=yes");
            document.getElementById("getjpeg").target = "PRINTIMAGE";
            document.getElementById("getjpeg").action = "printlist.aspx";
            document.getElementById("getjpeg").method = "post";
            document.getElementById("getjpeg").submit();
            document.getElementById("getjpeg").target = "main";
            document.getElementById("getjpeg").action = "list.aspx";
            w.focus();
            return false;
        }
    </script>
    <form method="post" action="<c:url value='/v1/image/JpegList'/>" id="getjpeg">
		<div class="aspNetHidden">
		<input type="hidden" name="__VIEWSTATE" id="__VIEWSTATE" value="/wEPDwUJMzI3MzEzMjQ0ZGT7oXGsEWmshnfYo21h3E3jsulOZD+E7xet6xyg1zxH4g==" />
		</div>

		<div class="aspNetHidden">
			<input type="hidden" name="__VIEWSTATEGENERATOR" id="__VIEWSTATEGENERATOR" value="2CA4315D" />
		</div>
	    <div id="toc">
	      <table width="180" border="0" cellspacing="0" cellpadding="2" bgcolor="black">
		    <tr>
		      <td width="40"><a href="/dr12/" target="_top">
		        <img src="images/planet.jpg" border="0" width="40" height="50"/></a></td>
		      <td class='title' align="left" width="140">&nbsp;&nbsp;FASTDS</td>
	      </tr>
        </table>
        <!-- 导航区 -->
        <table border="0" cellspacing="0" cellpadding="0" >
	        <tr>
                <td class='s' align="left" onmouseover="this.T_WIDTH='140';return escape('FASTDB Home page')">|<a target="_top" href="<c:url value='/index.jsp'/>">Home&nbsp;|</a></td>
                <td class='s' align="left" onmouseover="this.T_WIDTH='140';return escape('Help on the current tool')"><a target="MosaicWindow" href="<c:url value='/listinfo.jsp'/>">Help&nbsp;|</a></td>
	            <td class='s' align="left" onmouseover="this.T_WIDTH='140';return escape('Go to the Finding Chart')"><a href="javascript:void(0)" onclick="return gotochart();">Chart&nbsp;|</a></td>
			    <td class='s' align="left" onmouseover="this.T_WIDTH='140';return escape('Go to the Navigate Tool')"><a href="navi.jsp?ra=337.138&dec=-0.948">Navi&nbsp;|</a></td>
	       		<td class='s' align="left" onmouseover="this.T_WIDTH='140';return escape('Go to the Object Explorer')"><A href="javascript:void(0)" onclick="return gotoExp();">Explore&nbsp;|</A></td>	
	        </tr>
        </table>

        <table width="180" cellpadding="0" cellspacing="0" border="0">
        <tr><td class='s'><a href="sqltoform.aspx" target="_top">Use query to fill form</a>
        </td></tr>
        <tr><td>
	        <textarea wrap="off" class='tx' cols="12" 
		        name="paste" rows="5">     name        ra     dec  
274-51913-230 337.238 -0.928
275-51910-275 337.316 -0.960
275-51910-525 337.240 -0.897
276-51909-19  337.317 -1.005
278-51900-39  337.545 -0.892
278-51900-112 337.585 -0.949
278-51900-225 337.316 -0.875
278-51900-430 337.380 -0.927
279-51984-456 337.369 -0.994
279-51984-520 337.024 -0.986
281-51614-230 354.651 -0.933
282-51658-167 354.504 -0.922
285-51930-309 354.744 -0.961
286-51999-359 340.154 -0.876
288-52000-173 340.466 -0.911
349-51699-582 341.264 -0.948
353-51703-328 341.747 -0.946
353-51703-365 341.999 -0.946
355-51788-167 342.206 -0.985
355-51788-563 339.920 -0.976
358-51818-349 339.364 -0.931
387-51791-72    2.380 -0.895
389-51795-481   2.620 -1.014
390-51900-196   339.646 -0.888
390-51900-464   339.484 -0.976</textarea> 
        </td></tr>
        <tr><td class='s'>
        Cut and paste ra/dec list<br/>
        </td></tr>
        </table>

        <p />
        <table width="180" bgcolor="lightblue" border="1" cellspacing="0" cellpadding="0" valign="middle">
	        <tr><td colspan="2" align="center" bgcolor="skyblue">Parameters</td></tr>
        <tr>
	<td><span  onmouseover="this.T_TEMP='3000';this.T_WIDTH='180';return escape('')">scale</span></td>
		<td>
		<table cellspacing="0" cellpadding="0">
		<tr><td>
			<input class='in' type='text' size='3' maxlength='20' align='right'
			value='0.4' name='scale' id='scale' onchange='setscale(0)' /></td><td>&nbsp;<span  onmouseover="this.T_TEMP='3000';this.T_WIDTH='180';return escape('')">''/pix			</span></td></tr></table>
		</td>
		</tr>
<tr>
	<td><span  onmouseover="this.T_TEMP='3000';this.T_WIDTH='180';return escape('')">opt </span></td>
		<td>
		<table cellspacing="0" cellpadding="0"><tr><td>
			<input class='in' type='text' size='3' maxlength='20' align='right'
			value='' name='opt' id='opt' onchange='setoptstr(1);' /></td><td>&nbsp;<span  onmouseover="this.T_TEMP='3000';this.T_WIDTH='180';return escape('')">			</span></td></tr></table>
		</td>
</tr>

        </table>

            <table  width="180" cellspacing="4" cellpadding="0" border="0">
	            <tr><td align="center" onmouseover="this.T_WIDTH='140';return escape('Get an image of the sky at the specified coordinates')">
	            <a href="javascript:void(0);" onclick="return resubmit();"><img 
			            src="images/get_image.jpg" alt="Submit" 
			            border="0" width="112" height="40"/></a>
	            </td>
	            <!-- 
	            <td align="center" onmouseover="this.T_WIDTH='140';return escape('Open a new window with a printable image')">
	            <a href="javascript:void(0);" onclick="return popup();"><img 
			            src="images/printer_icon_blue.jpg" alt="Printable Image" 
			            border="0" width="40" height="40"/></a>
	            </td> -->
	            </tr>
	
            </table>

            <table width="180" cellpadding="0" cellspacing="0" border="0">
            <tr>
	            <td><img src="./images/zoombar5.jpg" alt="Zoom In" 
			            border="0" width="160" height="32" ismap="ismap" 
			            usemap="#zoom_bar_map" id="zoombar"/></td>
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
            <area shape="rect" alt="zoom out" coords="132,6,156,28" href="javascript:void(0);" onmouseover="this.T_WIDTH='140';return escape('Zoom out')" onclick="return stepzoom(2.0)"/>
            </map>
	

            <table width="180" cellpadding="0" cellspacing="0" border="1"> 
              <tr><td align="center" bgcolor="skyblue" onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Mark various options on the image')">Drawing options</td></tr>
			  <tr><td  onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Grid with tickmarks to show image scale')"><input type="checkbox" onclick="setopt(this,'G')" name="Grid"  id="Grid" /> Grid</td></tr>
			  <tr><td  onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Information on the Navigation window view')"><input type="checkbox" onclick="setopt(this,'L')" name="Label"  id="Label"/> Label</td></tr>
		      <tr><td  onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Marks photometrically identified objects with light blue circles')"><input type="checkbox" onclick="setopt(this,'P')" name="PhotoObjs"  id="PhotoObjs"/> Photometric objects</td></tr>
		      <tr><td  onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Marks spectroscopic objects with red squares')"><input type="checkbox" onclick="setopt(this,'S')" name="SpecObjs"  id="SpecObjs"/> Objects with spectra</td></tr>
			  <tr><td  onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Inverts black and white in the image')"><input type="checkbox" onclick="setopt(this,'I')" name="InvertImage"   id="InvertImage"/> Invert Image</td></tr>	
			  <tr><td align="center" bgcolor="skyblue">Advanced options</td></tr>	
		     <!-- <tr><td  onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Shows APOGEE DATA')"><input type="checkbox" onclick="setopt(this,'A')" name="APOGEE" id="Checkbox3"/> APOGEE Spectra</td></tr>		-->
			  <tr><td  onmouseover="this.T_TEMP='5000';this.T_WIDTH='140';return escape('Draws the outline (green) of each photometric object except at the largest zoom-out scales (where they are not legible)')"><input type="checkbox" onclick="setopt(this,'O')" name="Outline"  id="Outline"/>SDSS Outlines</td></tr>
			  <tr><td  onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Draws a rectangular box (pink) around each photometric object')"><input type="checkbox" onclick="setopt(this,'B')" name="BoundingBox" id="BoundingBox"/>SDSS Bounding Boxes</td></tr> 
			  <tr><td  onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Shows each SDSS field (~10x14 arcmin) in gray')"><input type="checkbox" onclick="setopt(this,'F')" name="Fields" id="Fields"/>SDSS Fields</td></tr>
			  <tr><td  onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Shows masks (pink) around bright objects and data artifacts')"><input type="checkbox" onclick="setopt(this,'M')" name="Masks" id="Masks"/>SDSS Masks</td></tr>
			  <tr><td  onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Shows plates (lavender) used to collect spectra')"><input type="checkbox" onclick="setopt(this,'Q')" name="Plates" id="Plates"/>SDSS Plates</td></tr>		
			</table>

    </div>
    <!-- 
    <input type="hidden" value="1" name="page"/>
     -->
    </form>
    <div id="content">
        
<div class="content">
${errors}
<h1>FASTDB Image List Tool</h1>

<table border="0" width="440"  cellpadding="1" cellspacing="1">
	<tr valign="top"><td><!-- valign 调格子中内容的垂向 -->
		This page is to generate image cutouts of FSATDB images based upon a user defined list 
		of object positions. In order to avoid congestion on the server, the list is currently 
		limited to 1000 objects. If this is a problem, please submit your list in pieces.
		<p></p>]
		If you're new to the Image List tool, please see the <a href="http://skyserver.sdss.org/dr12/en/tools/chart/default.aspx"
		target="Visual">Visual Tools main page</a> and <a href="#list" onclick=
		"window.open('../started/list.aspx','popup','width=440,height=580,resizeable,scrollbars');">
		Getting Started with Image List</a>.
		<br/>&nbsp;<br/>
For the description of the other options see the Help section of the <a href="chartinfo.aspx">Finding Chart</a>. The format of the list can be from the following choices:
 	<br/>
	<ol>

		<li><b>List of (ra,dec) pairs</b></li><br/>
Always ra comes first, followed by dec. Both ra and dec can be in degrees or hh:mm:ss.s dd:mm:ss.s format. The separator can be any white space or a comma.<br/>

		<li><b>List of (name,ra,dec) triplets</b></li><br />
The fields must always be in this order. The name can be any single alphanumeric string containing at most an underscore and a dot (like ABC_1234.32). Both ra and dec can be in degrees or hh:mm:ss.s dd:mm:ss.s format. The separator can be any white space or a comma.<br/>

		<li><b>Same as above, with a single header line</b></li><br />
The formats (1) and (2) can also contain a single header line, containing the column names. The header must use the same separator as the data. The names ra and dec are mandatory.

		<li><b>Lists in the IRSA Gator format</b></li><br />
For details see the IRSA website. 

	</ol>
	</td></tr>
</table>

</div>

    </div>
    <script type="text/javascript">init('1');</script>
    <script language="JavaScript" type="text/javascript" src="js/wz_tooltip.js"></script>
</body>
</html>
