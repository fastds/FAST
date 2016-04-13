<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
		<td width="40"><a href="${master.enUrl }" target="_top"><img src="../images/sdss3_logo.gif" border=0 width="40" height="50"></a></td>
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
        	<c:if test="${not empty master.apid }">
        		<tr><td align="left"><a class="med" href='${master.hrefs.summary}#irspec' title="Summary of infrared spectroscopy">IR Spec Summary</a></td></tr>                      
	              <tr><td class="s" align="right"><a target="_top" href="${master.hrefs.apogeeStar}" title="'Display contents of ApogeeStar table">ApogeeStar</a></td></tr> 
	              <tr><td class="s" align="right"><a target="_top" href="${master.hrefs.aspcapStar}" title="'Display contents of ASPCAPstar table">ASPCAPstar</a></td></tr>
	              <tr><td align="right"><hr width="90"/></td></tr>
        	</c:if>
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
       <c:if test="${not empty crossIDCtrl.objID}">
		<h3 class="sectionlabel" id="crossidtop">Cross-identifications
		    <a id="crossid_is_shown" href="javascript:showSection('crossid');javascript:showLink('crossid_is_hidden');javascript:hideLink('crossid_is_shown');" class="showinglink">
		      Show
		    </a>
		    <a id="crossid_is_hidden" href="javascript:hideSection('crossid');javascript:showLink('crossid_is_shown');javascript:hideLink('crossid_is_hidden');" class="hidinglink">
		       Hide
		    </a>
		</h3>

	<div id="crossid"> 
	<table class="content">
	    <tr>
	        <td>
	        <c:choose>
	        	<c:when test="${crossIDCtrl.USNO }">
		        	<table cellpadding=2 cellspacing=2 border=0 width=620>
		                <tr>
		                    <td align='center' class='h'><span>Catalog</span></td>
		                    <td align='center' class='h'><span>Proper motion (mas/yr)</span></td>
		                    <td align='center' class='h'><span>PM angle (deg E)</span></td>
		                </tr>
		                <tr>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.usno}</td>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.properMotion}</td>
		                    <td nowrap align='center' class='t'><fmt:formatNumber value="${crossIDCtrl.angle}" pattern="#.##" ></fmt:formatNumber></td>
		                </tr>
		            </table>
	        	</c:when>
	        	<c:otherwise>
	        		 <table cellpadding=2 cellspacing=2 border=0 width=625>
	                  <tr><td class='nodatafound'>No data found for this object in USNO</td></tr>
	           		 </table>
	        	</c:otherwise>
	        </c:choose>
	        <c:choose>
	        	<c:when test="${crossIDCtrl.FIRST }">
	        		<table cellpadding=2 cellspacing=2 border=0 width=620>
		                <tr>
		                    <td align='center' class='h'><span>Catalog</span></td>
		                    <td align='center' class='h'><span>Peak flux (mJy)</span></td>
		                    <td align='center' class='h'><span>Major axis (arcsec)</span></td>
		                    <td align='center' class='h'><span>Minor axis (arcsec)</span></td>
		                </tr>
		                <tr>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.first}</td>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.peakflux}</td>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.major}</td>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.minor}</td>
		                </tr>
	            	</table>
	        	</c:when>
	        	<c:otherwise>
	        		<table cellpadding=2 cellspacing=2 border=0 width=625>
	                  <tr><td class='nodatafound'>No data found for this object in FIRST</td></tr>
	           		</table>
	        	</c:otherwise>
	        </c:choose>
	        <c:choose>
	        	<c:when test="${crossIDCtrl.ROSAT }">
	        		<table cellpadding=2 cellspacing=2 border=0 width=620>
		                <tr>
		                    <td align='center' class='h'><span>Catalog</span></td>
		                    <td align='center' class='h'><span>cps</span></td>
		                    <td align='center' class='h'><span>hr1</span></td>
		                    <td align='center' class='h'><span>hr2</span></td>
		                    <td align='center' class='h'><span>ext</span></td>
		                </tr>
		                <tr>
		                     <td nowrap align='center' class='t'>${crossIDCtrl.rosat}</td>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.cps}</td>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.hr1}</td>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.hr2}</td>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.ext}</td>
		                </tr>
	            	</table>
	        	</c:when>
	        	<c:otherwise>
	        		<table cellpadding=2 cellspacing=2 border=0 width=625>
	                  <tr><td class='nodatafound'>No data found for this object in ROSAT</td></tr>
	           	 </table>
	        	</c:otherwise>
	        </c:choose>
	        <c:choose>
	        	<c:when test="crossIDCtrl.RC3">
	        		<table cellpadding=2 cellspacing=2 border=0 width=620>
		                <tr>
		                    <td align='center' class='h'><span>Catalog</span></td>
		                    <td align='center' class='h'><span>Hubble type</span></td>
		                    <td align='center' class='h'><span>21 cm magnitude</span></td>
		                    <td align='center' class='h'><span>Neutral Hydrogen Index</span></td>
		                </tr>
		                <tr>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.rc3}</td>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.hubletype}</td>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.magnitude}</td>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.hydrogenIndex}</td>
		                </tr>
	           		</table>
	        	</c:when>
	        	<c:otherwise>
	        		<table cellpadding=2 cellspacing=2 border=0 width=625>
	                  <tr><td class='nodatafound'>No data found for this object in RC3</td></tr>
	           		</table>
	        	</c:otherwise>
	        </c:choose>
	        <c:choose>
	        	<c:when test="crossIDCtrl.2MASS">
	        		<table cellpadding=2 cellspacing=2 border=0 width=620>
		                <tr>
		                    <td align='center' class='h'><span>Catalog</span></td>
		                    <td align='center' class='h'><span>J</span></td>
		                    <td align='center' class='h'><span>H</span></td>
		                    <td align='center' class='h'><span>K_s</span></td>
		                    <td align='center' class='h'><span>phQual</span></td>
		                </tr>
		                <tr>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.twomass}</td>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.j}</td>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.h}</td>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.k}</td>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.phQual}</td>
		                </tr>
	           		</table>
	        	</c:when>
	        	<c:otherwise>
	        		<table cellpadding=2 cellspacing=2 border=0 width=625>
	                  <tr><td class='nodatafound'>No data found for this object in 2MASS</td></tr>
	            	</table>
	        	</c:otherwise>
	        </c:choose>
	        <c:choose>
	        	<c:when test="crossIDCtrl.WISE">
	        		<table cellpadding=2 cellspacing=2 border=0 width=620>
		                <tr>
		                    <td align='center' class='h'><span>Catalog</span></td>
		                    <td align='center' class='h'><span>w1mag</span></td>
		                    <td align='center' class='h'><span>w2mag</span></td>
		                    <td align='center' class='h'><span>w3mag</span></td>
		                    <td align='center' class='h'><span>w4mag</span></td>
		                    <td align='center' class='h'><span>Full WISE data</span></td>
		                </tr>
		                <tr>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.wise}</td>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.wmag1}</td>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.wmag2}</td>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.wmag3}</td>
		                    <td nowrap align='center' class='t'>${crossIDCtrl.wmag4}</td>
		                    <td nowrap align='center' class='t'><a href=".\DisplayResults.aspx?cmd=${crossIDCtrl.linkQuery}&name=wise">Link</a></td>                   
		                </tr>
	           		</table>
	        	</c:when>
	        	<c:otherwise>
	        		<table cellpadding=2 cellspacing=2 border=0 width=625>
	                  <tr><td class='nodatafound'>No data found for this object in WISE</td></tr>
	            	</table>
	        	</c:otherwise>
	        </c:choose>
	        </td>
	    </tr>
	</table>
	</div>  	
</c:if>

   </div>              
    </div>
</form>
<script language="JavaScript" type="text/javascript" src="../js/wz_tooltip.js"></script>

<iframe id="test" name='test' width ="0" height="0" scrolling="no"  src="blank.html"/>
 	
</body>
</html>
