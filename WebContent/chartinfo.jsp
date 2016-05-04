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
		    <img src="images/sdss3_logo.gif" border="0" width="40" height="50"></a></td>
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
			value='${param.ra ==null?337.2336:param.ra }' name='ra' id='ra' onChange='setra();' ></td><td>&nbsp;<span  ONMOUSEOVER="this.T_TEMP='3000';this.T_WIDTH='180';return escape('R.A. in decimal degrees,<br> HH MM SS, or HH:MM:SS')">deg			</span></td></tr></table>
		</td>
</tr>
<tr>
	<td><span  ONMOUSEOVER="this.T_TEMP='3000';this.T_WIDTH='180';return escape('Declination in decimal degrees,<br> DD MM SS, or DD:MM:SS')">dec</span></td>
		<td><table cellspacing=0 cellpadding=0><tr><td>
			<INPUT class='in' type='text' size='3' maxlength='20' align='right'
			value='${param.dec==null?-0.924:param.dec }' name='dec' id='dec' onChange='setdec();' ></td><td>&nbsp;<span  ONMOUSEOVER="this.T_TEMP='3000';this.T_WIDTH='180';return escape('Declination in decimal degrees,<br> DD MM SS, or DD:MM:SS')">deg			</span></td></tr></table>
		</td>
</tr>
<tr>
	<td><span  ONMOUSEOVER="this.T_TEMP='3000';this.T_WIDTH='180';return escape('Chart scale in arcsec per pixel')">scale</span></td>
		<td><table cellspacing=0 cellpadding=0><tr><td>
			<INPUT class='in' type='text' size='3' maxlength='20' align='right'
			value='0.79224' name='scale' id='scale' onChange='setscale(0)' ></td><td>&nbsp;<span  ONMOUSEOVER="this.T_TEMP='3000';this.T_WIDTH='180';return escape('Chart scale in arcsec per pixel')">''/pix			</span></td></tr></table>
		</td>
</tr>
<tr>
	<td><span  ONMOUSEOVER="this.T_TEMP='3000';this.T_WIDTH='180';return escape('Chart width in pixels')">width</span></td>
		<td><table cellspacing=0 cellpadding=0><tr><td>
			<INPUT class='in' type='text' size='3' maxlength='20' align='right'
			value='512' name='width' id='width' onChange='setwidth();' ></td><td>&nbsp;<span  ONMOUSEOVER="this.T_TEMP='3000';this.T_WIDTH='180';return escape('Chart width in pixels')">pix			</span></td></tr></table>
		</td>
</tr>
<tr>
	<td><span  ONMOUSEOVER="this.T_TEMP='3000';this.T_WIDTH='180';return escape('Chart height in pixels')">height</span></td>
		<td><table cellspacing=0 cellpadding=0><tr><td>
			<INPUT class='in' type='text' size='3' maxlength='20' align='right'
			value='512' name='height' id='height' onChange='setheight();' ></td><td>&nbsp;<span  ONMOUSEOVER="this.T_TEMP='3000';this.T_WIDTH='180';return escape('Chart height in pixels')">pix			</span></td></tr></table>
		</td>
</tr>
<tr>
	<td><span  ONMOUSEOVER="this.T_TEMP='3000';this.T_WIDTH='180';return escape('See short-hand Drawing Options on Help page')">opt</span></td>
		<td><table cellspacing=0 cellpadding=0><tr><td>
			<INPUT class='in' type='text' size='3' maxlength='20' align='right'
			value='' name='opt' id='opt' onChange='setoptstr(1);' ></td><td>&nbsp;<span  ONMOUSEOVER="this.T_TEMP='3000';this.T_WIDTH='180';return escape('See short-hand Drawing Options on Help page')">			</span></td></tr></table>
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
            </map>

            <table width="180" cellpadding=0 cellspacing=0 border=0>
	            <tr>
		            <td ONMOUSEOVER="this.T_OFFSETY='15';this.T_TEMP='3000';this.T_WIDTH='180';return escape('Mark selected objects with a purple triangle. See &quot;Help&quot; for options.')">Use query to mark objects</td>
	            </tr>
	

            </table>
            <table border=0 ONMOUSEOVER="this.T_OFFSETY='15';this.T_TEMP='3000';this.T_WIDTH='180';return escape('Mark selected objects with a purple triangle. See &quot;Help&quot; for options.')">
              <tr>
                <td><!-- 
                  <TEXTAREA wrap=off class='tx' cols=17 name="query" rows=5></TEXTAREA>  -->
	            </td>
              </tr>
            </table>
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
	<tr><td bgColor="lightblue" ONMOUSEOVER="this.T_TEMP='5000';this.T_WIDTH='140';return escape('Draws the outline (green) of each photometric object except at the largest zoom-out scales (where they are not legible)')"><INPUT type="checkbox" onclick="setopt(this,'O')" name="Outline"  id="Outline">SDSS Outlines</td></tr>-->
	<tr><td bgColor="lightblue" ONMOUSEOVER="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Draws a rectangular box (pink) around each photometric object')"><INPUT type="checkbox" onclick="setopt(this,'B')" name="BoundingBox" id="BoundingBox">SDSS Bounding Boxes</td></tr>
	<tr><td bgColor="lightblue" ONMOUSEOVER="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Shows each SDSS field (~10x14 arcmin) in gray')"><INPUT type="checkbox" onclick="setopt(this,'F')" name="Fields" id="Fields">SDSS Fields</td></tr>
	<tr><td bgColor="lightblue" ONMOUSEOVER="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Shows masks (pink) around bright objects and data artifacts')"><INPUT type="checkbox" onclick="setopt(this,'M')" name="Masks" id="Masks">SDSS Masks</td></tr>
	<tr><td bgColor="lightblue" ONMOUSEOVER="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Shows plates (lavender) used to collect spectra')"><INPUT type="checkbox" onclick="setopt(this,'Q')" name="Plates" id="Plates">SDSS Plates</td></tr>

           
           </table>
    </div>
        <div id="content">
            
		<h1>FASTDB
			Finding Chart Tool</h1>
		<hr width="600" align="left">
		<h3>Finding Chart (ra, dec, scale, height, width, opt, query)</h3>
		<ul>
			<table width="560">
				<tr>
					<td>
						returns a JPEG image centered on <i>(ra,dec)</i>, of size <i>(height x width)</i>
						where the image is scaled to an arbitrary scale <i>(scale)</i>. In SDSS the 
						default scale is 0.396127 arcsec/pix. Various drawing options can be specified <i>(opt)</i>.
						Use mark query to select objects of special interest.
						<p id="list">
						If you're new to the Finding Chart, please see the <a href="http://skyserver.sdss.org/dr12/en/tools/chart/default.aspx" target="Visual">Visual Tools main page</a>
						and <a href="#list" onclick="window.open('../started/chart.aspx','popup','width=440,height=580,resizeable,scrollbars');">
						Getting Started with Finding Chart</a>.
					</td>
				</tr>
			</table>
		</ul>
		<hr width="540" align="left">
		<i>Parameters:</i>
		<ul>
			<table>
				<tr>
					<td class='q' width="60">ra</td>
					<td>
						center point right ascension in J2000 decimal degrees, hh mm ss.s, or hh:mm:ss.s</td>
				</tr>
				<tr>
					<td class='q'>dec</td>
					<td>
						center point declination in J2000 decimal degrees, dd mm ss.s, or dd:mm:ss.s</td>
				</tr>
				<tr>
					<td class='q'>scale</td>
					<td>
						arcsec/pixel (the natural scale of SDSS is 0.396127)</td>
				</tr>
				<tr>
					<td class='q'>height</td>
					<td>
						image height in pixels, limited to [64..2048]</td>
				</tr>
				<tr>
					<td class='q'>width
					</td>
					<td>
						image width in pixels, limited to [64..2048]</td>
				</tr>
				<tr>
					<td class='q' valign="top">opt
					</td>
					<td>
						options string, a set of upper-case characters, like 'GPST'.<br>
					</td>
				</tr>
			</table>
		</ul>
		<hr width="540" align="left">
		<i>Use query to mark objects:</i>
		<ul>
			This option will draw a triangle on top of objects selected by a marking string.<br/>
			Objects must be inside the field of view of the image to be displayed.<br/>
			The format of the string can be from the following choices:<br/><br/>
			<ol>	
				<LI><b>List of objects.</b> A header with RA and DEC columns must be included. 
				<br>Columns must be separated by tabs, spaces, commas or semicolons.
				<br>The list may contain as many columns as wished. 
				<p>
				<TABLE>
				<TR>
					<TD><b>ObjId</b></TD>
					<TD><b>RA</b></TD>
					<TD><b>DEC</b></TD>
					<TD><b>RMag</b></TD>
				</TR>
				<TR>
					<TD>1237666338116206715</TD>
					<TD>18.877321</TD>
					<TD>-0.859906</TD>
					<TD>21.57310</TD>

				</TR>
				<TR>
					<TD>1237666338116206712</TD>
					<TD>18.876854</TD>
					<TD>-0.860976</TD>
					<TD>12.82945</TD>
				</TR>
			</TABLE><br>

			<LI><b>SQL SELECT query.</b> RA and DEC columns must be included. 
			<p style="font-size:10pt">
				SELECT TOP 10 p.objID, p.ra, p.dec, p.r <br/>
				FROM fGetObjFromRectEq(&lt;ra1&gt;,&lt;dec1&gt;,&lt;ra2&gt;,&lt;dec2&gt;) n, PhotoPrimary p <br />
				WHERE n.objID=p.objID<br/><br/>
		  </p>
			<LI><b>String following the pattern:</b> <I>ObjType Band (low_mag, high_mag)</I>
			<p>
				<table cellpadding="2">
					<tr>
						<td><b>ObjType:</b></td>
						<td>S | G | P </td> 
					</tr>
					<tr>
						<td></td>
						<td> marks Stars, Galaxies or PhotoPrimary objects.</td>
					</tr>					
					<tr>
						<td><b>Band:</b>
						<td>U | G | R | I | Z | A </td>
					<tr>	
						<td></td>
						<td> restricts marks to objects with <i>Band </i>BETWEEN <i>low_mag</i> AND <i>high_mag</i><br> 
						Band 'A' will mark all objects within the specified magnitude range in any band (ORs composition).
						</td>						
					</tr>
					<tr>
						<td><b>Examples:</b></td>
						<td>S</td>
					</tr>
					<tr>
						<td></td>
						<td>S R (0.0, 23.5)</td>
					</tr>
					<tr>
						<td></td>
						<td>G A (20, 30)</td>
					</tr>				
				</table>
			</ol>
		</ul>		

				
		<hr width="540" align="left">
		<i>Drawing options:</i>
		<ul>
			The characters present will select the corresponding option<br>
			from the list below. Characters not in the list are ignored.
			<p>
				<table cellpadding="2">
					<tr>
						<td class='q'>G</td>
						<td>Grid</td>
						<td>Draw a N-S E-W grid through the center</td>
					</tr>
					<tr>
						<td class='q'>L</td>
						<td>Label</td>
						<td>Draw the name, scale, ra, and dec on image</td>
					</tr>
					<tr>
						<td class='q'>P</td>
						<td>PhotoObj</td>
						<td>Draw a small cicle around each primary photoObj</td>
					</tr>
					<tr>
						<td class='q'>S</td>
						<td>SpecObj</td>
						<td>Draw a small square around each specObj</td>
					</tr>
<!--
					<tr>
						<td class='q'>T</td>
						<td>Target</td>
						<td>Draw a small square around each Target</td>
					</tr>
-->
					<tr>
						<td class='q'>O</td>
						<td>Outline</td>
						<td>Draw the outline of each photoObj
						</td>
					</tr>
					<tr>
						<td class='q'>B</td>
						<td>Bounding Box</td>
						<td>Draw the bounding box of each photoObj
						</td>
					</tr>
					<tr>
						<td class='q'>F</td>
						<td>Fields</td>
						<td>Draw the outline of each field
						</td>
					</tr>
					<tr>
						<td class='q'>M</td>
						<td>Masks</td>
						<td>Draw the outline of each mask considered to be important
						</td>
					</tr>
					<tr>
						<td class='q'>Q</td>
						<td>Plates</td>
						<td>Draw the outline of each plate
						</td>
					</tr>
					<tr>
						<td class='q'>I</td>
						<td>Invert</td>
						<td>Invert the image (B on W)</td>
					</tr>
				</table>
		</ul>
		<hr width="540" align="left">
		<i>Direct access for web pages:</i>
		<ul>
			<table width="560">
				<tr>
					<td>
						This application is based on an underlying web service, <b>ImgCutout.asmx</b> which 
						can be called in many different ways, using the SOAP protocol, or just using the 
						standard HTTP GET and PUT interfaces. The formal description is contained in 
						the <A href="http://skyservice.pha.jhu.edu/DR12/ImgCutout/ImgCutout.asmx?WSDL">WSDL</A>, Web Service Description 
						Language document.</p>
					</td>
				</tr>
				<tr>
					<td>
						The getjpeg service can be directly called from any web page through the HTTP 
						GET protocol. In order to build a dynamic cutout into your own web page, insert 
						the following example. Naturally, replace the parameter values with your own.
						<p>
							<samp><font color="blue">&lt;IMG SRC="http://skyservice.pha.jhu.edu/DR12/ImgCutout
									<br/>
									&nbsp;&nbsp;/getjpeg.aspx?ra=132.749&amp;dec=11.656&amp;scale=0.79224 
									&nbsp;&nbsp;&amp;width=400&amp;height=400&amp;opt=GST&amp;query=SR(10,20)"&gt;</font></samp>
							<br/>
						</p>
					</td>
				</tr>
			</table>
		</ul>
		<!--
				<span class='s'>&nbsp;*&nbsp; to be implemented later </span></p>
-->
		<hr width="540" align="left">
   		</div>
    </form>
    <script type="text/javascript">init('1');</script>
    <script language="JavaScript" type="text/javascript" src="js/wz_tooltip.js"></script>
  	
  </body>
</html>
