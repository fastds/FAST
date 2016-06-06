<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="edu.gzu.utils.Utilities"  %>
<%@ page import="org.fastds.model.ApogeeControl"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<link href="../css/tools.css" rel="stylesheet" type="text/css" />
	<link href="../css/alerts.css" rel="stylesheet" type="text/css" />
	
	<title>
		Object Explorer
	</title>
	
    <script src="../js/jquery-1.7.1.js"></script>
    <script type="text/javascript" src="../js/master.js"> </script>
    <script type="text/javascript" src="../js/explore.js"></script>
</head>
<body>
<form id="form1" method="post" action="<c:url value='/explore/summary.jsp'/>" >
  <div>        
    <table border="0" cellspacing="0" cellpadding="2" bgcolor="black">
	 <tr>
		<td width="40"><a href="${master.enUrl }" target="_top"><img src="../images/planet.jpg" border=0 width="40" height="50"></a></td>
		<td class="title" align="left" width="${ master.tabwidth-60 }">&nbsp;&nbsp;${ master.globals.release }</td>
	</tr>
   </table>
   <div id="search" style="display:none;">
	 	<table>
	    	<tr>
		      <td width="120px" align="center"><h2>Search by</h2><input type="button" onclick="toggleSearch()" value="Hide"></input></td>
		      <td>
		        <table>
		          <tr><td align="right" class="s">Name:</td><td><input type="text" id="searchName" name="searchName"></input></td><td><input type="button" onclick="resolveName()" value="Find"/></td></tr>
		          <tr><td align="right" class="s">RA/Dec:</td><td><input type="text" size="8" id="searchRA" name="searchRA"></input>/<input type="text" size="8" id="searchDec" name="searchDec"></input></td><td><input type="button" onclick="press_ok('radec')" value="Go"></input></td></tr>
		           <tr><td align="right" class="s">ObjID:</td><td><input type="text" name="searchObjID"></input></td><td><input type="button" onclick="press_ok('objid')" value="Go"></input></td></tr>
		        </table>
	      		 </td>
	     		 <td>
		        <table>
		        <tr><td align="right" class="s">SpecID or APOGEE ID:</td><td><input type="text" name="searchSpecID"></input></td><td><input type="button" onclick="press_ok('specid')" value="Go"></input></td></tr>
		        <tr><td align="right" class="s">5-part SDSS:</td><td><input type="text" name="searchSDSS"></input></td><td><input type="button" onclick="press_ok('sdss')" value="Go"></input></td></tr>
		        <tr><td align="right" class="s">Plate-MJD-Fiber:</td><td><input type="text" size="4" name="searchPlate"></input>-<input type="text" size="4" name="searchMJD"></input>-<input type="text" size="4" name="searchFiber"></input></td><td><input type="button" onclick="press_ok('plfib')" value="Go"></input></td></tr>
		       </table>
	      		</td>
	    	   </tr>
	  		</table>
	</div>
	<div id="toc" >
	    <table width="110" cellspacing=0 cellpadding=0>
	     <tr><td><a class="med" target="_top" href='default.aspx' title="Return to the Explore tool default page">Explore Home</a><br/></td></tr>
	        <tr><td class="s" align="right"><hr width="90"/></td></tr>
	        
	        <tr><td class="med"><a id="searchLabel" href="#" onclick="toggleSearch();">Search</a></td></tr>
	        <tr><td class="s" align="right"><hr width="90" /></td></tr>
	        
	        <!-- // If this object has a numerical id -- i.e. if it is a SDSS-I/SEGUE/BOSS object... -->
	        <c:if test="${not empty master.id }">
	        	<tr><td align="left"><a class="med" href='${master.hrefs.summary}#imaging' title="Parameters for Imaging Observations.">Imaging Summary</a></td></tr>
	            <tr><td class="s" align="right"><a target="_top" href="${master.hrefs.FITS}" title="Get FITS images of the SDSS fields containing this object.">FITS</a></td></tr>
                <tr><td class="s" align="right"><a href='${master.hrefs.chart}' title="Link to Finding Chart for this object.">Finding chart<img src="../../images/new_window_black.png" alt=" (new window)" /></a></td></tr>
	            <tr><td class="s" align="right"><a target="_top" href="${master.hrefs.matches}" title="Find other imaging observations for this object in the Match table.">Other Observations</a></td></tr>
	            <tr><td class="s" align="right"><a target="_top" href="${master.hrefs.neighbors}" title="The nearest neighbors of this object within a predetermined radius.">Neighbors</a></td></tr>
                <tr><td class="s" align="right"><a target="_top" href="${master.hrefs.galaxyzoo}" title="Morphological classifications from Galaxy Zoo users">Galaxy Zoo</a></td></tr>
                <tr><td>&nbsp;</td></tr>
	            <tr><td class="s" align="right"><a target="_top" href="${master.hrefs.photoTag}" title="Find the most popular parameters for this image.">PhotoTag</a></td></tr>
	            <tr><td class="s" align="right"><a target="_top" href="${master.hrefs.field}" title="Field table entry for the field that this object was observed in.">Field</a></td></tr>
	            <tr><td class="s" align="right"><a target="_top" href="${master.hrefs.frame}" title="Frame table entry corresponding to this Field.">Frame</a></td></tr>
                <tr><td class="s" align="right"><a target="_top" href='${master.hrefs.photoObj}' title="Full Parameters for Imaging Observations."> PhotoObj</a></td></tr>	
	                
                <tr><td class="s" align="right"><a target="_top" href="${ master.hrefs.photoZ }" title="Photometric redshift for this object (from the Photoz table), if it exists.">PhotoZ</a></td></tr>
                 <%--<tr><td class="s" align="right"><a target="_top" href="${ hrefs.PhotozRF }" title="Random forest calculated phhotometric redshift for this object (from the PhotozRF table), if it exists.">PhotozRF</a></td></tr>--%>
	
                <tr><td align="right"><hr width="90"/></td></tr>
	            <tr><td><a class="med" target="_top" href='${master.hrefs.summary}#crossidtop' title="Photometric cross-identifications with other surveys, if there are any available.">Cross-ID</a><br></td></tr>
	
                <tr><td align="right"><hr width="90"/></td></tr>
	
	            <tr><td><a class="med" target="_top" href='${master.hrefs.summary}#spectro' title="Summary of spectroscopic parameters (if a spectrum was observed).">Spec Summary</a><br></td></tr>
                <tr><td class="s" align="right"><a target="_top" href="${master.hrefs.allSpec}" title="Find all the spectra observed for this object in the SpecObjAll table.">All Spectra</a></td></tr>
	            <c:if test="${ master.specID != null}">
	            	<tr><td class="s" align="right"><a target="_top" href="${master.hrefs.specFITS}" title="Go to the SAS for the spectrum FITS file.">FITS</a></td></tr>
	            	<tr><td class="s" align="right"><a target="_top" href='${master.hrefs.plate}' title="Browsable contents of the spectroscopic plate that this spectrum belongs to.">Plate</a></td></tr>
	                <tr><td class="s" align="right"><a target="_top" href='${master.hrefs.specObj}' title="Full Parameters for Spectroscopic Observations.">SpecObj</a></td></tr>	
	             	<tr><td class="s" align="right"><a target="_top" href='${master.hrefs.sppLines}' title="Stellar Parameter Pipeline indices for spectral lines observed for this spectrum.">sppLines</a></td></tr>
					<!-- release>4 -->
	            	<tr><td class="s" align="right"><a target="_top" href='${master.hrefs.galSpecLine}' title="Emission line measurements (from MPA-JHU spectroscopic re-analysis) for this spectrum.">galSpecLine</a></td></tr>
	             	<tr><td class="s" align="right"><a target="_top" href='${master.hrefs.galSpecIndx}' title="Index measurements (from the MPA-JHU spectroscopic catalogue) for this spectrum.">galSpecIndx</a></td></tr>
	             	<tr><td class="s" align="right"><a target="_top" href='${master.hrefs.galSpecInfo}' title="General information (for the MPA-JHU spectroscopic re-analysis) for this spectrum.">galSpecInfo</a></td></tr>
					<!-- release>7 -->
	             	<tr><td align="right"><hr width="90"/></td></tr>
                 	<tr><td><a class="med" target="_top" href='${master.hrefs.theParameters}' title="Summary of fits to spectroscopic parameters (if a spectrum was observed).">Fit Parameters</a><br /></td></tr>
                 	<tr><td class="s" align="right"><a target="_top" href='${master.hrefs.sppParams}' title="Stellar Parameter Pipeline atmospheric parameters for this spectrum.">sppParams</a></td></tr>                            
					<!-- release>8 -->
                 	<tr><td class="s" align="right"><a target="_top" href='${master.hrefs.stellarMassStarformingPort}' title="General information (for the Portsmouth spectroscopic re-analysis) for this spectrum, assuming a star forming model.">StarformingPort</a></td></tr>
                 	<tr><td class="s" align="right"><a target="_top" href='${master.hrefs.stellarMassPassivePort}' title="General information (for the (for the Portsmouth spectroscopic re-analysis) for this spectrum, assuming a passive model.">PassivePort</a></td></tr>
           		 	<tr><td class="s" align="right"><a target="_top" href='${master.hrefs.emissionLinesPort}' title="Emission line measurements (from Portsmouth spectroscopic re-analysis)">emissionLinesPort</a></td></tr>
                 	<tr><td class="s" align="right"><a target="_top" href='${master.hrefs.stellarMassPCAWiscBC03}' title="General information (for the Wisconsin spectroscopic re-analysis) for this spectrum, assuming BC03.">PCAWiscBC03</a></td></tr>
                 	<tr><td class="s" align="right"><a target="_top" href='${master.hrefs.stellarMassPCAWiscM11}' title="General information (for the Wisconsin spectroscopic re-analysis) for this spectrum, assuming M11.">PCAWiscM11</a></td></tr>
	            </c:if>  <!--  if (specId != null) -->
	            <tr><td align="right"><hr width="90"/></td></tr>
	        </c:if>
	        <!-- DR9不支持
        	<c:if test="${not empty master.apid }">
        		<tr><td align="left"><a class="med" href='${master.hrefs.summary}#irspec' title="Summary of infrared spectroscopy">IR Spec Summary</a></td></tr>                      
	              <tr><td class="s" align="right"><a target="_top" href="${master.hrefs.apogeeStar}" title="'Display contents of ApogeeStar table">ApogeeStar</a></td></tr> 
	              <tr><td class="s" align="right"><a target="_top" href="${master.hrefs.aspcapStar}" title="'Display contents of ASPCAPstar table">ASPCAPstar</a></td></tr>
	              <tr><td align="right"><hr width="90"/></td></tr>
        	</c:if>
        	 -->
	        <!--If the ID is a string, then it's an APOGEE object, print these also.  -->  
	
	
	     <tr><td><a class="med" target="_top" href='${master.hrefs.NED}' title="NASA Extragalactic Database matches for this RA,dec."> NED search</a></td></tr>
	     <tr><td><a class="med" target="_top" href='${master.hrefs.SIMBAD}' title="SIMBAD search results for this RA,dec."> SIMBAD search</a></td></tr>
	     <tr><td><a class="med" target="_top" href='${master.hrefs.ADS}' title="Astrophysical Data System literature search results for this RA,dec."> ADS search</a></td></tr>
	     <tr><td align="right"><hr width="90"/></td></tr>
	     <tr><td><a class="med" href='${master.hrefs.showBook}' title="Save and view notepad data on Explore objects.">Notes</a></td></tr>
	     <tr><td class="s" align="right" ><a href='${master.hrefs.saveBook}' title="Save this object in notepad."> Save in Notes</a></td></tr>
	     <tr><td class="s" align="right" ><a href='${master.hrefs.showBook}' title="View your saved notes, if any.">Show Notes</a></td></tr>
	     <tr><td align="right"><hr width="90"/></td></tr>
	     <tr><td><a class="med" href=# onclick="${master.hrefs.print}"> Print </a></td></tr>
	    </table>
		</div>
     
   <div id="content">
   <!-- 
       <asp:contentplaceholder id="OEContent" runat="server" /> -->
       <c:if test="${not empty requestScope.master.apid}">
 <div id="irspec">
        <h3>Infrared Spectra
          <span class="target">Targeted star: ${requestScope.apogeeCtrl.apogee_id}</span>
        </h3>
        <c:choose>
        	<c:when test="${requestScope.apogeeCtrl.isData == true }">
        		<table width="800">
		          <tr>
		            <td class="h">Instrument</td>
		            <td class="t"><b>APOGEE</b></td>
		          </tr>
		          <tr>
		            <td class="h">APOGEE ID</td>
		            <td class="t">${requestScope.apogeeCtrl.apogeeCtrl.apstar_id}</td>
		          </tr>
		        </table>

		        <table width="800">
		          <tr>
		            <td align="center" class="h" colspan="2" width="50%">Galactic Coordinates</td>
		            <td align="center" class="h" colspan="2" width="50%">RA,dec</td>
		          </tr>
		          <tr>
		            <td align="center" class="h">Longitude (L)</td>
		            <td align="center" class="h">Latitude (B)</td>
		            <td align="center" class="h">Decimal</td>
		            <td align="center" class="h">Sexagesimal</td>
		          </tr>
		          <tr>
		            <td align="center" class="t"><fmt:formatNumber value="${requestScope.apogeeCtrl.glon}" pattern="#.#####"></fmt:formatNumber></td>                        
		            <td align="center" class="t"><fmt:formatNumber value="${requestScope.apogeeCtrl.glat}" pattern="#.#####"></fmt:formatNumber></td>                     
		            <td align="center" class="t"><fmt:formatNumber value="${requestScope.apogeeCtrl.ra}" pattern="#.#####"></fmt:formatNumber>, <fmt:formatNumber value="${requestScope.apogeeCtrl.dec}" pattern="#.#####"></fmt:formatNumber></td>
		            <td align="center" class="t">
		            <span class="large">
		            	<%=Utilities.hmsC(((ApogeeControl)request.getAttribute("apogeeCtrl")).getRa()) + ", " + Utilities.dmsC(((ApogeeControl)request.getAttribute("apogeeCtrl")).getDec())%>
		            </span>
		            </td>
		          </tr>
		        </table>
            
		        <table>
		          <tr>
		            <td colspan="2">
		              <a href="${requestScope.apogeeCtrl.apogeeSpecImage }"><img src="${requestScope.apogeeCtrl.apogeeSpecImage }" width="780" height="195" alt="APOGEE infrared spectrum of ${requestScope.apogeeCtrl.apogee_id}" /></a>
		            </td>
		          </tr>
		          <tr>
		            <td class="irspeclink">
		              <a class="content" href="${requestScope.apogeeCtrl.spectrumLink }" target="_blank">Interactive spectrum<img src="../../images/new_window_black.png" alt=" (new window)" /></a>
		            </td>
		            <td class="irspeclink" align="right">
		              <a class="content" href="${requestScope.apogeeCtrl.fitsLink }" target="_blank">Download FITS<img src="../../images/new_window_black.png" alt=" (new window)" /></a>
		            </td>
		          </tr>
		       	 </table>
        	</c:when>
        	<c:otherwise>
        		<table cellpadding=2 cellspacing=2 border=0 width=625>
                  <tr><td class='nodatafound'>No Spectrum data found for this object</td></tr>
            	</table> 
        	</c:otherwise>
        </c:choose>
        
        
        <h3>Targeting Information</h3>
        <c:choose>
        	<c:when test="${requestScope.apogeeCtrl.isData }">
        		<table cellpadding="2" cellspacing="2" border="0" width="800">
		          <tr>
		            <td align="middle" class="h"><span>2MASS j</span></td>
		            <td align="middle" class="h"><span>2MASS h</span></td>
		            <td align="middle" class="h"><span>2MASS k</span></td>
		            <td align="middle" class="h"><span>j_err</span></td>
		            <td align="middle" class="h"><span>h_err</span></td>
		            <td align="middle" class="h"><span>k_err</span></td>
		          </tr>
		          <tr>
		            <td nowrap align="middle" class="t">${requestScope.apogeeCtrl.j }</td>
		            <td nowrap align="middle" class="t">${requestScope.apogeeCtrl.h }</td>
		            <td nowrap align="middle" class="t">${requestScope.apogeeCtrl.k }</td>
		            <td nowrap align="middle" class="t">${requestScope.apogeeCtrl.j_err }</td>
		            <td nowrap align="middle" class="t">${requestScope.apogeeCtrl.h_err }</td>
		            <td nowrap align="middle" class="t">${requestScope.apogeeCtrl.k_err }</td>
		          </tr>
		        </table>

		        <table cellpadding="2" cellspacing="2" border="0" width="800">
		          <tr>
		            <td align="middle" class="h"><span>4.5 micron magnitude</span></td>
		            <td align="middle" class="h"><span>4.5 micron magnitude error</span></td>
		            <td align="middle" class="h"><span>4.5 micron magnitude source</span></td>
		          </tr>
		          <tr>
		            <td nowrap align="middle" class="t">${requestScope.apogeeCtrl.mag_4_5 }</td>
		            <td nowrap align="middle" class="t">${requestScope.apogeeCtrl.mag_4_5_err }</td>
		            <td nowrap align="middle" class="t">${requestScope.apogeeCtrl.src_4_5 }</td>
		          </tr>
		        </table>

		        <table cellpadding="2" cellspacing="2" width="800">
		          <tr align="left">
		            <td valign="top" class="h">APOGEE target flags 1</td>
		            <td valign="top" class="b">${requestScope.apogeeCtrl.apogeeTarget1N }</td>
		          </tr>
		        </table>

		        <table cellpadding="2" cellspacing="2" width="800">
		          <tr align="left">
		            <td valign="top" class="h">APOGEE target flags 2</td>
		            <td valign="top" class="b">${requestScope.apogeeCtrl.apogeeTarget2N }</td>
		          </tr>
		        </table>
        	</c:when>
        	<c:otherwise>
        		<table cellpadding=2 cellspacing=2 border=0 width=625>
                  <tr><td class='nodatafound'>No Targeting data found for this object</td></tr>
            	</table>
        	</c:otherwise>
        </c:choose>

        
        <h3>Stellar Parameters</h3>
        <c:choose>
         	<c:when test="${requestScope.apogeeCtrl.isData }">
         		<table cellpadding="2" cellspacing="2" border="0" width="800">
		          <tr>
		            <td align="middle" class="h"><span>Avg v<sub>helio</sub> (km/s)</span></td>
		            <td align="middle" class="h"><span>Scatter in v<sub>helio</sub> (km/s)</span></td>
		            <td align="middle" class="h"><span>Best-fit temperature (K)</span></td>
		            <td align="middle" class="h"><span>Temp error</span></td>
		          </tr>
		          <tr>
		            <td nowrap align="middle" class="t">${requestScope.apogeeCtrl.vhelio_avg }</td>
		            <td nowrap align="middle" class="t">${requestScope.apogeeCtrl.vscatter }</td>
		            <td nowrap align="middle" class="t"><fmt:formatNumber value="${requestScope.apogeeCtrl.teff}" pattern="#."></fmt:formatNumber></td>
		            <td nowrap align="middle" class="t"><fmt:formatNumber value="${requestScope.apogeeCtrl.teff_err}" pattern="#.#"></fmt:formatNumber></td>
		          </tr>
		        </table>
                
		        <table cellpadding="2" cellspacing="2" border="0" width="800">  
		          <tr>
		            <td align="middle" class="h"><span>Surface Gravity log<sub>10</sub>(g)</span></td>
		            <td align="middle" class="h"><span>log(g) error</span></td>
		            <td align="middle" class="h"><span>Metallicity [Fe/H]</span></td>
		            <td align="middle" class="h"><span>Metal error</span></td>
		            <td align="middle" class="h"><span>[&alpha;/Fe]</span></td>
		            <td align="middle" class="h"><span>[&alpha;/Fe] error</span></td>
		          </tr>
		          <tr>
		            <td nowrap align="middle" class="t"><fmt:formatNumber value="${requestScope.apogeeCtrl.logg}" pattern="#.##"></fmt:formatNumber></td>
		            <td nowrap align="middle" class="t"><fmt:formatNumber value="${requestScope.apogeeCtrl.logg_err}" pattern="#.###"></fmt:formatNumber></td>
		            <td nowrap align="middle" class="t"><fmt:formatNumber value="${requestScope.apogeeCtrl.param_m_h}" pattern="#.#"></fmt:formatNumber></td>
		            <td nowrap align="middle" class="t"><fmt:formatNumber value="${requestScope.apogeeCtrl.param_m_h_err}" pattern="#.###"></fmt:formatNumber></td>
		            <td nowrap align="middle" class="t"><fmt:formatNumber value="${requestScope.apogeeCtrl.param_alpha_m}" pattern="#.##"></fmt:formatNumber></td>
		            <td nowrap align="middle" class="t"><fmt:formatNumber value="${requestScope.apogeeCtrl.param_alpha_m_err}" pattern="#.###"></fmt:formatNumber></td>
		          </tr>
		        </table>
                
		        <table cellpadding="2" cellspacing="2" width="800">
		          <tr align="left">
		            <td valign="top" class="h">Star flags</td>
		            <td valign="top" class="b">${requestScope.apogeeCtrl.apogeeStarFlagN }</td>
		          </tr>
		        </table>

		        <table cellpadding="2" cellspacing="2" width="800">
		          <tr align="left">
		            <td valign="top" class="h">Processing flags (ASPCAP)</td>
		            <td valign="top" class="b">${requestScope.apogeeCtrl.apogeeAspcapFlagN }</td>
		          </tr>
		        </table>
         	</c:when>
         	<c:otherwise>
         		<table cellpadding=2 cellspacing=2 border=0 width=625>
                  <tr><td class='nodatafound'>No Stellar Parameters found for this object</td></tr>
           		</table>
         	</c:otherwise> 
         </c:choose>
		
        <h3 class="sectionlabel">
          Visits (click to see visit spectrum)
          <a id="visits_are_shown" href="javascript:showSection('visits');javascript:showLink('visits_are_hidden');javascript:hideLink('visits_are_shown');" class="showinglink">
            Show
          </a>
          <a id="visits_are_hidden" href="javascript:hideSection('visits');javascript:showLink('visits_are_shown');javascript:hideLink('visits_are_hidden');" class="hidinglink">
            Hide
          </a>
        </h3>

        <div id="visits">
          <table cellpadding="2" cellspacing="2" border="0" width="800">
            <tr>
              <td align="middle" class="h"><span>visit_id</span></td>
              <td align="middle" class="h"><span>plate</span></td>
              <td align="middle" class="h"><span>mjd</span></td>
              <td align="middle" class="h"><span>fiberid</span></td>
              <td align="middle" class="h"><span>date</span></td>
              <td align="middle" class="h"><span>time (UTC)</span></td>
              <td align="middle" class="h"><span>vrel</span></td>
            </tr>
            
            <c:set var="cellClass" value="t"></c:set>
			<c:forEach var="v" items="${requestScope.apogeeCtrl.visits }">
				<tr>
	              <td nowrap align="middle" class="${cellClass }">
	                <a href="${apogeeCtrl.globals.apogeeSpectrumLink }?plateid=${v.plate}&mjd=${v.mjd}&fiber=${v.fiberid}" class="content" target="_blank">
	                  ${v.visit_id}&nbsp;<img src="../../images/new_window_black.png" alt=" (new window)" />
	                </a>
	              </td>
	              <td nowrap align="middle" class="${cellClass}">${v.plate }</td>
	              <td nowrap align="middle" class="${cellClass}">${v.mjd}</td>
	              <td nowrap align="middle" class="${cellClass}">${v.fiberid }</td>
	              <td nowrap align="middle" class="${cellClass}">${v.dateobs.substring(0,10)}</td>
	              <td nowrap align="middle" class="${cellClass}">${v.dateobs.substring(11,12)}</td>
	              <td nowrap align="middle" class="${cellClass}">${v.vrel}</td>  
	            </tr>
	            <c:set var="cellClass" value="${cellClass == 't'? 'b' : 't'}"></c:set> <!--  Alternating row colors --> 
			</c:forEach>
          </table>                          
        </div>  <!-- end of visits div -->
      </div>  <!-- end of irspec div -->
 </c:if><!-- master.apid not empty -->
   </div>              
    </div>
</form>
<script language="JavaScript" type="text/javascript" src="../js/wz_tooltip.js"></script>

<iframe id="test" name='test' width ="0" height="0" scrolling="no"  src="blank.html"/>
 	
</body>
</html>
