<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
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
	 <div id="headerAlert">
            <a href="http://www.sciserver.org/updates/" class="imgwithlink" target="_blank">
                <img src="../../images/sciserver_logo_usermsg.png" alt="logo" width="190" />
            </a>
            <p><a href="http://www.sciserver.org/updates/" target="_blank">Coming soon!<img src="../../images/new_window_cyan.png" alt=" (new window)" style="max-width:95%;margin:2%" /></a></p>
      </div>    
    
    <form method="post" action="<c:url value='/explore/summary.jsp'/>" id="form1">
<div class="aspNetHidden">
<input type="hidden" name="__VIEWSTATE" id="__VIEWSTATE" value="/wEPDwUJODU4OTk2Mjk4ZGSXbKLENKi4/Sb331LOg5WKLATOIywgHjwZAL+Zf45n0w==" />
</div>

<div class="aspNetHidden">

	<input type="hidden" name="__VIEWSTATEGENERATOR" id="__VIEWSTATEGENERATOR" value="ECFE01DC" />
</div>
    <div>        
      <table border="0" cellspacing="0" cellpadding="2" bgcolor="black">
	  <tr>
		<td width="40"><a href="http://skyserver.sdss.org/dr12/en" target="_top"><img src="images/sdss3_logo.gif" border=0 width="40" height="50"></a></td>
		<td class="title" align="left" width="68">&nbsp;&nbsp;FASTDS</td>
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
                
                        <tr><td align="left"><a class="med" href='summary.aspx?id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000#imaging' title="Parameters for Imaging Observations.">Imaging Summary</a></td></tr>	
        	            <tr><td class="s" align="right"><a target="_top" href="fitsimg.aspx?id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000" title="Get FITS images of the SDSS fields containing this object.">FITS</a></td></tr>
                        <tr><td class="s" align="right"><a href='javascript:gotochart(197.614455642896,18.438168853724);' title="Link to Finding Chart for this object.">Finding chart<img src="../../images/new_window_black.png" alt=" (new window)" /></a></td></tr>
	                    <tr><td class="s" align="right"><a target="_top" href="matches.aspx?id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000" title="Find other imaging observations for this object in the Match table.">Other Observations</a></td></tr>
	                    <tr><td class="s" align="right"><a target="_top" href="neighbors.aspx?id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000" title="The nearest neighbors of this object within a predetermined radius.">Neighbors</a></td></tr>
                        <tr><td class="s" align="right"><a target="_top" href="galaxyzoo.aspx?id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000" title="Morphological classifications from Galaxy Zoo users">Galaxy Zoo</a></td></tr>
                        <tr><td>&nbsp;</td></tr>
	                    <tr><td class="s" align="right"><a target="_top" href="DisplayResults.aspx?name=PhotoTag&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000" title="Find the most popular parameters for this image.">PhotoTag</a></td></tr>
	                    <tr><td class="s" align="right"><a target="_top" href="DisplayResults.aspx?name=Field&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000" title="Field table entry for the field that this object was observed in.">Field</a></td></tr>
	                    <tr><td class="s" align="right"><a target="_top" href="DisplayResults.aspx?name=Frame&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000" title="Frame table entry corresponding to this Field.">Frame</a></td></tr>
                        <tr><td class="s" align="right"><a target="_top" href='DisplayResults.aspx?name=PhotoObj&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000' title="Full Parameters for Imaging Observations."> PhotoObj</a></td></tr>	
                        
		                        <tr><td class="s" align="right"><a target="_top" href="DisplayResults.aspx?&name=photoZ&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000" title="Photometric redshift for this object (from the Photoz table), if it exists.">PhotoZ</a></td></tr>
                                
                        

                        <tr><td align="right"><hr width="90"/></td></tr>
	                    <tr><td><a class="med" target="_top" href='summary.aspx?id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000#crossidtop' title="Photometric cross-identifications with other surveys, if there are any available.">Cross-ID</a><br></td></tr>

                        <tr><td align="right"><hr width="90"/></td></tr>

	                    <tr><td><a class="med" target="_top" href='summary.aspx?id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000#spectro' title="Summary of spectroscopic parameters (if a spectrum was observed).">Spec Summary</a><br></td></tr>
                      <tr><td class="s" align="right"><a target="_top" href="allSpec.aspx?id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000" title="Find all the spectra observed for this object in the SpecObjAll table.">All Spectra</a></td></tr>
	                                            
                      <tr><td class="s" align="right"><a target="_top" href="fitsspec.aspx?&sid=0x28e84d919a006800&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172" title="Go to the SAS for the spectrum FITS file.">FITS</a></td></tr>
	                    <tr><td class="s" align="right"><a target="_top" href='plate.aspx?&name=Plate&plateId=0x28e800119a006800' title="Browsable contents of the spectroscopic plate that this spectrum belongs to.">Plate</a></td></tr>
                        <tr><td class="s" align="right"><a target="_top" href='DisplayResults.aspx?name=SpecObj&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000' title="Full Parameters for Spectroscopic Observations.">SpecObj</a></td></tr>	
	                    <tr><td class="s" align="right"><a target="_top" href='DisplayResults.aspx?name=sppLines&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000' title="Stellar Parameter Pipeline indices for spectral lines observed for this spectrum.">sppLines</a></td></tr>

	                    <tr><td class="s" align="right"><a target="_top" href='DisplayResults.aspx?name=galSpecLine&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000' title="Emission line measurements (from MPA-JHU spectroscopic re-analysis) for this spectrum.">galSpecLine</a></td></tr>
	                    <tr><td class="s" align="right"><a target="_top" href='DisplayResults.aspx?name=galSpecIndx&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000' title="Index measurements (from the MPA-JHU spectroscopic catalogue) for this spectrum.">galSpecIndx</a></td></tr>
	                    <tr><td class="s" align="right"><a target="_top" href='DisplayResults.aspx?name=galSpecInfo&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000' title="General information (for the MPA-JHU spectroscopic re-analysis) for this spectrum.">galSpecInfo</a></td></tr>

                        
                            <tr><td align="right"><hr width="90"/></td></tr>
    	                    <tr><td><a class="med" target="_top" href='parameters.aspx?id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000' title="Summary of fits to spectroscopic parameters (if a spectrum was observed).">Fit Parameters</a><br /></td></tr>
    	                    <tr><td class="s" align="right"><a target="_top" href='DisplayResults.aspx?name=sppParams&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000' title="Stellar Parameter Pipeline atmospheric parameters for this spectrum.">sppParams</a></td></tr>                            
   
                            
                                <tr><td class="s" align="right"><a target="_top" href='DisplayResults.aspx?name=stellarMassStarFormingPort&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000' title="General information (for the Portsmouth spectroscopic re-analysis) for this spectrum, assuming a star forming model.">StarformingPort</a></td></tr>
                                <tr><td class="s" align="right"><a target="_top" href='DisplayResults.aspx?name=stellarMassPassivePort&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000' title="General information (for the (for the Portsmouth spectroscopic re-analysis) for this spectrum, assuming a passive model.">PassivePort</a></td></tr>
	                            <tr><td class="s" align="right"><a target="_top" href='DisplayResults.aspx?name=emissionlinesPort&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000' title="Emission line measurements (from Portsmouth spectroscopic re-analysis)">emissionLinesPort</a></td></tr>
                                <tr><td class="s" align="right"><a target="_top" href='DisplayResults.aspx?name=stellarMassPCAWiscBC03&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000' title="General information (for the Wisconsin spectroscopic re-analysis) for this spectrum, assuming BC03.">PCAWiscBC03</a></td></tr>
                                <tr><td class="s" align="right"><a target="_top" href='DisplayResults.aspx?name=stellarMassPCAWiscM11&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000' title="General information (for the Wisconsin spectroscopic re-analysis) for this spectrum, assuming M11.">PCAWiscM11</a></td></tr>
                                
                                <tr><td class="s" align="right"><a target="_top" href='DisplayResults.aspx?name=stellarMassFSPSGranEarlyDust&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000' title="stellarMassFSPSGranEarlyDust">FSPSGranEarlyDust</a></td></tr>
                                <tr><td class="s" align="right"><a target="_top" href='DisplayResults.aspx?name=stellarMassFSPSGranEarlyNoDust&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000' title="stellarMassFSPSGranEarlyNoDust">FSPSGranEarlyNoDust</a></td></tr>
                                <tr><td class="s" align="right"><a target="_top" href='DisplayResults.aspx?name=stellarMassFSPSGranWideDust&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000' title="stellarMassFSPSGranWideDust">FSPSGranWideDust</a></td></tr>
                                <tr><td class="s" align="right"><a target="_top" href='DisplayResults.aspx?name=stellarMassFSPSGranWideNoDust&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000' title="stellarMassFSPSGranWideNoDust">FSPSGranWideNoDust</a></td></tr>
                            
                    <tr><td align="right"><hr width="90"/></td></tr>
                
                      <tr><td align="left"><a class="med" href='summary.aspx?id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000#irspec' title="Summary of infrared spectroscopy">IR Spec Summary</a></td></tr>                      
                      <tr><td class="s" align="right"><a target="_top" href="DisplayResults.aspx?name=apogeeStar&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000" title="'Display contents of ApogeeStar table">ApogeeStar</a></td></tr> 
                      <tr><td class="s" align="right"><a target="_top" href="DisplayResults.aspx?name=aspcapStar&id=1237668296598749280&spec=2947691243863304192&apid=apogee.apo25m.s.stars.4128.2M13102744+1826172&field=0x112d14c220880000" title="'Display contents of ASPCAPstar table">ASPCAPstar</a></td></tr>
                      <tr><td align="right"><hr width="90"/></td></tr>
                


	            <tr><td><a class="med" target="_top" href='http://nedwww.ipac.caltech.edu/cgi-bin/nph-objsearch?search_type=Near+Position+Search&in_csys=Equatorial&in_equinox=J2000.0&obj_sort=Distance+to+search+center&lon=197.6144556d&lat=18.4381689d&radius=1.0' title="NASA Extragalactic Database matches for this RA,dec."> NED search</a></td></tr>
	            <tr><td><a class="med" target="_top" href='http://simbad.u-strasbg.fr/sim-id.pl?protocol=html&Ident=13+10+27.4+%2B18+26+17.40&NbIdent=1&Radius=1.0&Radius.unit=arcmin&CooFrame=FK5&CooEpoch=2000&CooEqui=2000&output.max=all&o.catall=on&output.mesdisp=N&Bibyear1=1983&Bibyear2=2005&Frame1=FK5&Frame2=FK4&Frame3=G&Equi1=2000.0&Equi2=1950.0&Equi3=2000.0&Epoch1=2000.0&Epoch2=1950.0&Epoch3=2000.0' title="SIMBAD search results for this RA,dec."> SIMBAD search</a></td></tr>
	            <tr><td><a class="med" target="_top" href='http://adsabs.harvard.edu/cgi-bin/nph-abs_connect?db_key=AST&sim_query=YES&aut_xct=NO&aut_logic=OR&obj_logic=OR&author=&object=13+10+27.4+%2B18+26+17.40&start_mon=&start_year=&end_mon=&end_year=&ttl_logic=OR&title=&txt_logic=OR&text=&nr_to_return=100&start_nr=1&start_entry_day=&start_entry_mon=&start_entry_year=&min_score=&jou_pick=ALL&ref_stems=&data_and=ALL&group_and=ALL&sort=SCORE&aut_syn=YES&ttl_syn=YES&txt_syn=YES&aut_wt=1.0&obj_wt=1.0&ttl_wt=0.3&txt_wt=3.0&aut_wgt=YES&obj_wgt=YES&ttl_wgt=YES&txt_wgt=YES&ttl_sco=YES&txt_sco=YES&version=1' title="Astrophysical Data System literature search results for this RA,dec."> ADS search</a></td></tr>
	            <tr><td align="right"><hr width="90"/></td></tr>
	            <tr><td><a class="med" href='javascript:showNotes();' title="Save and view notepad data on Explore objects.">Notes</a></td></tr>
	            <tr><td class="s" align="right" ><a href='javascript:saveBook("0x112d14c220880060");' title="Save this object in notepad."> Save in Notes</a></td></tr>
	            <tr><td class="s" align="right" ><a href='javascript:showNotes();' title="View your saved notes, if any.">Show Notes</a></td></tr>
	            <tr><td align="right"><hr width="90"/></td></tr>
	            <tr><td><a class="med" href=# onclick="framePrint();"> Print </a></td></tr>
            </table>
        </div>
     
        <div id="content">
            

    
    

   <div class="content">
   
 <div id="metadata">
    <table class="content">
        <tr>
            <td colspan="2">
                <h1 id="sdssname">SDSS J131027.46+182617.4</h1>
                <h2 id="othernames">&nbsp;<input type="button" onclick=" findOtherNames(197.614455642896, 18.438168853724);" value="Look up common name" /></h2>
            </td>
        </tr>
        <tr>
            <td>
                <table width="620">
                    <tr>
                        <td align="center" class="h" colspan="2">Type</td>
                        <td align="center" class="h" colspan="2">SDSS Object ID</td>
                    </tr>
                    <tr>
                        <td class="t" align="center" colspan="2"><b>GALAXY</b></td>
                        <td class="t" align="center" colspan="2">1237668296598749280</td>
                    </tr>
                    <tr>
                        <td class="h" align="center" colspan="2">RA, Dec</td>
                        <td class="h" align="center" colspan="2">Galactic Coordinates (<i>l</i>, <i>b</i>)</td>
                    </tr>
                    <tr>
                        <td align="center" class="h">Decimal</td>
                        <td align="center" class="h">Sexagesimal</td>
                        <td align="center" class="h"><i>l</i></td>
                        <td align="center" class="h"><i>b</i></td>
                    </tr>
                    <tr>
                        <td align="center" class="t">
                            <script type="text/javascript" language="javascript">
                                var thera = new Number(197.614455642896);
                                var thedec = new Number(18.438168853724);
                                document.write(thera.toFixed(5) + ', ' + thedec.toFixed(5));
                            </script>
                        </td>
                        <td align="center" class="t">
                            <span class="large">13:10:27.46, +18:26:17.40</span>
                        </td>
                        <td align="center" class="t">
                            <script type="text/javascript" language="javascript">
                                var L = new Number(330.660789375215);
                                document.write(L.toFixed(5));
                            </script>
                        </td>
                        <td align="center" class="t">
                            <script type="text/javascript" language="javascript">
                                var B = new Number(80.2696372087722);
                                document.write(B.toFixed(5));
                            </script>
                        </td>
                    </tr>
                 </table>
              </td>
        </tr>
    </table>
       </div> <!-- end of metadata div -->


   
<div id="imaging">
<h3>Imaging</h3>            


            <div class="warning">
                <table><tr><td>
                    <b>WARNING:</b> 
                    This object's photometry may be unreliable. See the photometric 
                    <em>flags</em> below.
                </td></tr></table>
            </div>



<table>
    <tr>
        <td>
            <table cellpadding=2 cellspacing=2 border=0 width=420>
                <tr align='left' >
                    <td  valign='top' class='h'>
                        <span title="SDSS flags" >
                            <a href='http://www.sdss.org/dr12/algorithms/photo_flags_recommend.php'>Flags <img src=../../images/offsite_black.png /></a>
                        </span>
                    </td>
                    <td valign='top' class='t'>
                        DEBLEND_DEGENERATE PSF_FLUX_INTERP DEBLENDED_AT_EDGE BAD_MOVING_FIT BINNED1 INTERP COSMIC_RAY NODEBLEND CHILD BLENDED 
                    </td>
                </tr>
            </table>
         </td>
     </tr>
</table>

<table>
    <tr>
        <td style="vertical-align:top">
             
             <a href="javascript:showNavi(197.614455642896,18.438168853724,0.2);">
                 <img alt="" src="http://skyservice.pha.jhu.edu/DR12/ImgCutout/getjpeg.aspx?ra=197.614455642896&dec=18.438168853724&scale=0.2&width=200&height=200&opt=G" border="0" width="200" height="200" />
             </a>
         </td>
        <td >
            <table cellpadding=2 cellspacing=2 border=0 width=420>
                <tr><td align='middle' class='h'><span></span></td></tr>
                <tr><td nowrap align='middle' class='t'><b>Magnitudes</b></td></tr>
            </table>
            <table cellpadding=2 cellspacing=2 border=0 width=420>
                <tr><td align='middle' class='h'><span title="unit=mag">u</span></td>
                    <td align='middle' class='h'><span title="unit=mag">g</span></td>
                    <td align='middle' class='h'><span title="unit=mag" >r</span></td>
                    <td align='middle' class='h'><span title="unit=mag" >i</span></td>
                    <td align='middle' class='h'><span title="unit=mag" >z</span></td></tr>
                <tr><td nowrap align='middle' class='t'> 16.52</td>
                    <td nowrap align='middle' class='t'> 14.95</td>
                    <td nowrap align='middle' class='t'> 14.00</td>
                    <td nowrap align='middle' class='t'> 13.32</td>
                    <td nowrap align='middle' class='t'> 13.14</td></tr>
            </table>
            <table cellpadding=2 cellspacing=2 border=0 width=420>
                <tr><td align='middle' class='h'><span></span></td></tr>
                <tr><td nowrap align='middle' class='t'><b>Magnitude uncertainties</b></td></tr>
            </table>
            <table cellpadding=2 cellspacing=2 border=0 width=420>
                <tr><td align='middle' class='h'><span title="unit=mag" >err_u</span></td>
                    <td align='middle' class='h'><span title="unit=mag" >err_g</span></td>
                    <td align='middle' class='h'><span title="unit=mag" >err_r</span></td>
                    <td align='middle' class='h'><span title="unit=mag" >err_i</span></td>
                    <td align='middle' class='h'><span title="unit=mag" >err_z</span></td></tr>
                <tr><td nowrap align='middle' class='t'> 0.01</td>
                    <td nowrap align='middle' class='t'> 0.00</td>
                    <td nowrap align='middle' class='t'> 0.00</td>
                    <td nowrap align='middle' class='t'> 0.02</td>
                    <td nowrap align='middle' class='t'> 0.00</td></tr>
              </table>       
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <table cellpadding=2 cellspacing=2 border=0 width=625>
               <tr><td align='middle' class='h'><span title="unit=days" >Image MJD</span></td>
                   <td align='middle' class='h'><span title="unit=" >mode</span></td>
                   <td align='middle' class='h'><span title="unit=" >Other observations</span></td>
                   <td align='middle' class='h'><span title="unit=" >parentID</span></td>
                   <td align='middle' class='h'><span title="unit=" >nChild</span></td>
                   <td align='middle' class='h'><span title="unit=mag" >extinction_r</span></td>
                   <td align='middle' class='h'><span title="unit=arcsec" >PetroRad_r (arcsec)</span></td>
               </tr>
               <tr><td nowrap align='middle' class='t'>53500</td>
                   <td nowrap align='middle' class='t'>PRIMARY</td>
                   <td nowrap align='middle' class='t'>0</td>
                   <td nowrap align='middle' class='t'>1237668296598749279</td>
                   <td nowrap align='middle' class='t'>0</td>
                   <td nowrap align='middle' class='t'>   0.06</td>
                   <td nowrap align='middle' class='t'>    18.23 &plusmn;      2.152</td>
               </tr>
            </table>
            <table cellpadding=2 cellspacing=2 border=0 width=625>
                <tr>
                    <td align='middle' class='h'><span title="unit=days" >Mjd-Date</span></td>
                    <td align='middle' class='h'><span title="unit=mag" >photoZ (KD-tree method)</span></td>
                    
                    <td align='middle' class='h'><span>Galaxy Zoo 1 morphology</span></td>
                </tr>
                <tr>
                    <td nowrap align='middle' class='t'>05/10/2005</td>
                    <td nowrap align='middle' class='t'>  0.043 &plusmn;   0.0161</td>
                    
                    <td nowrap align='middle' class='t'>Uncertain</td>
                </tr>
            </table>                            
          </td>
        </tr>
     </table>
   </div>   
  
 
   
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
            
            <table cellpadding=2 cellspacing=2 border=0 width=620>
                <tr>
                    <td align='middle' class='h'><span>Catalog</span></td>
                    <td align='middle' class='h'><span>Proper motion (mas/yr)</span></td>
                    <td align='middle' class='h'><span>PM angle (deg E)</span></td>
                </tr>
                <tr>
                    <td nowrap align='middle' class='t'>USNO</td>
                    <td nowrap align='middle' class='t'>293.07 &plusmn;   60.000</td>
                    <td nowrap align='middle' class='t'>-110.78</td>
                </tr>
            </table>
            
            <table cellpadding=2 cellspacing=2 border=0 width=620>
                <tr>
                    <td align='middle' class='h'><span>Catalog</span></td>
                    <td align='middle' class='h'><span>Peak flux (mJy)</span></td>
                    <td align='middle' class='h'><span>Major axis (arcsec)</span></td>
                    <td align='middle' class='h'><span>Minor axis (arcsec)</span></td>
                </tr>
                <tr>
                    <td nowrap align='middle' class='t'>FIRST</td>
                    <td nowrap align='middle' class='t'>    2.65 &plusmn;     0.15</td>
                    <td nowrap align='middle' class='t'>11.46</td>
                    <td nowrap align='middle' class='t'>7.17</td>
                </tr>
            </table>
            
             <table cellpadding=2 cellspacing=2 border=0 width=625>
                  <tr><td class='nodatafound'>No data found for this object in ROSAT</td></tr>
            </table>
            
            <table cellpadding=2 cellspacing=2 border=0 width=620>
                <tr>
                    <td align='middle' class='h'><span>Catalog</span></td>
                    <td align='middle' class='h'><span>Hubble type</span></td>
                    <td align='middle' class='h'><span>21 cm magnitude</span></td>
                    <td align='middle' class='h'><span>Neutral Hydrogen Index</span></td>
                </tr>
                <tr>
                    <td nowrap align='middle' class='t'>RC3</td>
                    <td nowrap align='middle' class='t'>.S?....</td>
                    <td nowrap align='middle' class='t'>15.43 &plusmn;  0.300</td>
                    <td nowrap align='middle' class='t'>0.98</td>
                </tr>
            </table>
            
            <table cellpadding=2 cellspacing=2 border=0 width=620>
                <tr>
                    <td align='middle' class='h'><span>Catalog</span></td>
                    <td align='middle' class='h'><span>J</span></td>
                    <td align='middle' class='h'><span>H</span></td>
                    <td align='middle' class='h'><span>K_s</span></td>
                    <td align='middle' class='h'><span>phQual</span></td>
                </tr>
                <tr>
                    <td nowrap align='middle' class='t'>2MASS</td>
                    <td nowrap align='middle' class='t'>13.66</td>
                    <td nowrap align='middle' class='t'>12.615</td>
                    <td nowrap align='middle' class='t'>12.399</td>
                    <td nowrap align='middle' class='t'>EEE</td>
                </tr>
            </table>
            
            <table cellpadding=2 cellspacing=2 border=0 width=620>
                <tr>
                    <td align='middle' class='h'><span>Catalog</span></td>
                    <td align='middle' class='h'><span>w1mag</span></td>
                    <td align='middle' class='h'><span>w2mag</span></td>
                    <td align='middle' class='h'><span>w3mag</span></td>
                    <td align='middle' class='h'><span>w4mag</span></td>
                    <td align='middle' class='h'><span>Full WISE data</span></td>
                </tr>
                <tr>
                    <td nowrap align='middle' class='t'>WISE</td>
                    <td nowrap align='middle' class='t'>10.68</td>
                    <td nowrap align='middle' class='t'>10.44</td>
                    <td nowrap align='middle' class='t'>6.259</td>
                    <td nowrap align='middle' class='t'>4.132</td>
                    <td nowrap align='middle' class='t'><a href=".\DisplayResults.aspx?cmd=select * from wise_xmatch x join wise_allsky a on x.wise_cntr=a.cntr 
                                                where x.sdss_objid=0x112d14c220880060&name=wise">Link</a></td>                   
                </tr>
            </table>
            
            
             
        </td>
    </tr>
</table>
</div>  
  
   
 <div id="spectro">

   <h3>Optical Spectra</h3>
      
    <div class="infobox">
        <table width="100%">
            <tr>
                <td width="60%">
                  <p><b> SpecObjID = 2947691243863304192</b></p>                
                </td>
                <td width="40%">
                    
                    <p><b>
                      <a class='content' href="http://dr12.sdss3.org/spectrumDetail?plateid=2618&mjd=54506&fiber=310"  target='_blank'>
                          Interactive spectrum<img src='../../images/new_window_black.png' alt=' (new window)' />
                       </a>
                    </b></p>
                </td>
            </tr>
        </table>
   </div>
   <table class="content">
        <tr>
            
	   	      <td>             
		            <a href="../../get/SpecById.ashx?id=2947691243863304192">
		                <img alt="" src="../../get/SpecById.ashx?id=2947691243863304192" width="316" height="253" border="0" align="left" />
		            </a>
              </td>
              <td>                     
                 <table cellpadding=2 cellspacing=2 border=0 width=300>
                   
                   <tr align='left' >
                       <td  valign='top' class='h'><span>Spectrograph</span></td>
                       <td valign='top' class='t'>SDSS</td>
                   </tr>
                   <tr align='left' >
                       <td  valign='top' class='h'><span>class</span></td>
                       <td valign='top' class='t'>GALAXY</td>
                   </tr>
                   <tr align='left' >
                       <td  valign='top' class='h'><span>Redshift (z)</span></td>
                       <td valign='top' class='t'>0.012</td>
                   </tr>
                   <tr align='left' >
                       <td  valign='top' class='h'><span>Redshift error</span></td>
                       <td valign='top' class='t'>0.00001</td>
                   </tr>
                   <tr align='left' >
                       <td  valign='top' class='h'><span>Redshift flags</span></td>
                       <td valign='top' class='t'>OK</td>
                   </tr>
                   <tr align='left' >
                       <td  valign='top' class='h'><span>survey</span></td>
                       <td valign='top' class='t'>sdss</td>
                   </tr>
                   <tr align='left' >
                       <td  valign='top' class='h'><span>programname</span></td>
                       <td valign='top' class='t'>legacy</td>
                   </tr>
                   <tr align='left' >
                       <td  valign='top' class='h'><span>primary</span></td>
                       <td valign='top' class='t'>1</td>
                   </tr>
                   <tr align='left' >
                       <td  valign='top' class='h'><span>Other spec</span></td>
                       <td valign='top' class='t'>0</td>
                   </tr>
                   <tr align='left' >
                       <td  valign='top' class='h'><span>sourcetype</span></td>
                       <td valign='top' class='t'>GALAXY</td>
                   </tr>
                   <tr align='left' >
                       <td  valign='top' class='h'><span>Velocity dispersion (km/s)</span></td>
                       <td valign='top' class='t'>69.11</td>
                   </tr>
                   <tr align='left' >
                       <td  valign='top' class='h'><span>veldisp_error</span></td>
                       <td valign='top' class='t'>6.176</td>
                   </tr>
                   <tr align='left' >
                       <td  valign='top' class='h'><span>targeting_flags</span></td>
                       <td valign='top' class='t'>GALAXY GALAXY_RED   </td>
                   </tr>
                     <tr align='left' >
                       <td  valign='top' class='h'><span>plate</span></td>
                       <td valign='top' class='t'>2618</td>
                   </tr>
                  <tr align='left' >
                      <td  valign='top' class='h'><span>mjd</span></td>
                      <td valign='top' class='t'>54506</td>
                  </tr>
                  <tr align='left' >
                      <td  valign='top' class='h'><span>fiberid</span></td>
                      <td valign='top' class='t'>310</td>
                  </tr>
                 </table>
	   	      </td>            
        </tr>   
    </table> 
</div>  <!-- end of spectro div -->
 
   
 <div id="irspec">
        <h3>Infrared Spectra
          <span class="target">Targeted star: 2M13102744+1826172</span>
        </h3>
        
        <table width="800">
          <tr>
            <td class="h">Instrument</td>
            <td class="t"><b>APOGEE</b></td>
          </tr>
          <tr>
            <td class="h">APOGEE ID</td>
            <td class="t">apogee.apo25m.s.stars.4128.2M13102744+1826172</td>
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
            <td align="center" class="t">330.65989</td>                        
            <td align="center" class="t">80.26965</td>                     
            <td align="center" class="t">197.61434, 18.43813</td>
            <td align="center" class="t"><span class="large">13:10:27.44, +18:26:17.27</span></td>
          </tr>
        </table>
            
        <table>
          <tr>
            <td colspan="2">
              <a href="http://dr12.sdss3.org/sas/dr12/apogee/spectro/redux/r5/stars/apo25m/4128/plots/apStar-r5-2M13102744%2b1826172.jpg"><img src="http://dr12.sdss3.org/sas/dr12/apogee/spectro/redux/r5/stars/apo25m/4128/plots/apStar-r5-2M13102744%2b1826172.jpg" width="780" height="195" alt="APOGEE infrared spectrum of 2M13102744+1826172" /></a>
            </td>
          </tr>
          <tr>
            <td class="irspeclink">
              <a class="content" href="http://dr12.sdss3.org/irSpectrumDetail?locid=4128&commiss=0&apogeeid=2M13102744+1826172" target="_blank">Interactive spectrum<img src="../../images/new_window_black.png" alt=" (new window)" /></a>
            </td>
            <td class="irspeclink" align="right">
              <a class="content" href="http://dr12.sdss3.org/sas/dr12/apogee/spectro/redux/r5/stars/apo25m/4128/apStar-r5-2M13102744%2b1826172.fits" target="_blank">Download FITS<img src="../../images/new_window_black.png" alt=" (new window)" /></a>
            </td>
          </tr>
        </table>
        
        <h3>Targeting Information</h3>
             
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
            <td nowrap align="middle" class="t">13.66</td>
            <td nowrap align="middle" class="t">12.615</td>
            <td nowrap align="middle" class="t">12.399</td>
            <td nowrap align="middle" class="t">0.067</td>
            <td nowrap align="middle" class="t">0.067</td>
            <td nowrap align="middle" class="t">0.072</td>
          </tr>
        </table>

        <table cellpadding="2" cellspacing="2" border="0" width="800">
          <tr>
            <td align="middle" class="h"><span>4.5 micron magnitude</span></td>
            <td align="middle" class="h"><span>4.5 micron magnitude error</span></td>
            <td align="middle" class="h"><span>4.5 micron magnitude source</span></td>
          </tr>
          <tr>
            <td nowrap align="middle" class="t">10.568</td>
            <td nowrap align="middle" class="t">0.02</td>
            <td nowrap align="middle" class="t">WISE</td>
          </tr>
        </table>

        <table cellpadding="2" cellspacing="2" width="800">
          <tr align="left">
            <td valign="top" class="h">APOGEE target flags 1</td>
            <td valign="top" class="b">APOGEE_CHECKED APOGEE_SEGUE_OVERLAP </td>
          </tr>
        </table>

        <table cellpadding="2" cellspacing="2" width="800">
          <tr align="left">
            <td valign="top" class="h">APOGEE target flags 2</td>
            <td valign="top" class="b"></td>
          </tr>
        </table>
        
        <h3>Stellar Parameters</h3>
              
        <table cellpadding="2" cellspacing="2" border="0" width="800">
          <tr>
            <td align="middle" class="h"><span>Avg v<sub>helio</sub> (km/s)</span></td>
            <td align="middle" class="h"><span>Scatter in v<sub>helio</sub> (km/s)</span></td>
            <td align="middle" class="h"><span>Best-fit temperature (K)</span></td>
            <td align="middle" class="h"><span>Temp error</span></td>
          </tr>
          <tr>
            <td nowrap align="middle" class="t">-301.674</td>
            <td nowrap align="middle" class="t">28.5636</td>
            <td nowrap align="middle" class="t">-9999</td>
            <td nowrap align="middle" class="t">-1.0</td>
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
            <td nowrap align="middle" class="t">-9999.00</td>
            <td nowrap align="middle" class="t">-1.000</td>
            <td nowrap align="middle" class="t">-9999.0</td>
            <td nowrap align="middle" class="t">-1.000</td>
            <td nowrap align="middle" class="t">-9999.00</td>
            <td nowrap align="middle" class="t">-1.000</td>
          </tr>
        </table>
                
        <table cellpadding="2" cellspacing="2" width="800">
          <tr align="left">
            <td valign="top" class="h">Star flags</td>
            <td valign="top" class="b">SUSPECT_BROAD_LINES SUSPECT_RV_COMBINATION PERSIST_JUMP_NEG PERSIST_LOW </td>
          </tr>
        </table>

        <table cellpadding="2" cellspacing="2" width="800">
          <tr align="left">
            <td valign="top" class="h">Processing flags (ASPCAP)</td>
            <td valign="top" class="b">SN_BAD ROTATION_BAD STAR_BAD TEFF_BAD SN_WARN ROTATION_WARN STAR_WARN TEFF_WARN </td>
          </tr>
        </table>
      

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

            
            <tr>
              <td nowrap align="middle" class="t">
                <a href="http://dr12.sdss3.org/irSpectrumDetail?plateid=5633&mjd=55932&fiber=296" class="content" target="_blank">
                  apogee.apo25m.s.r5.5633.55932.296&nbsp;<img src="../../images/new_window_black.png" alt=" (new window)" />
                </a>
              </td>
              <td nowrap align="middle" class="t">5633</td>
              <td nowrap align="middle" class="t">55932</td>
              <td nowrap align="middle" class="t">296</td>
              <td nowrap align="middle" class="t">2012-01-06</td>
              <td nowrap align="middle" class="t">10:52:21.716</td>
              <td nowrap align="middle" class="t">-339.963</td>
            </tr>
            
            <tr>
              <td nowrap align="middle" class="b">
                <a href="http://dr12.sdss3.org/irSpectrumDetail?plateid=5633&mjd=55965&fiber=281" class="content" target="_blank">
                  apogee.apo25m.s.r5.5633.55965.281&nbsp;<img src="../../images/new_window_black.png" alt=" (new window)" />
                </a>
              </td>
              <td nowrap align="middle" class="b">5633</td>
              <td nowrap align="middle" class="b">55965</td>
              <td nowrap align="middle" class="b">281</td>
              <td nowrap align="middle" class="b">2012-02-08</td>
              <td nowrap align="middle" class="b">11:35:19.424</td>
              <td nowrap align="middle" class="b">-264.735</td>
            </tr>
            
            <tr>
              <td nowrap align="middle" class="t">
                <a href="http://dr12.sdss3.org/irSpectrumDetail?plateid=5633&mjd=55990&fiber=284" class="content" target="_blank">
                  apogee.apo25m.s.r5.5633.55990.284&nbsp;<img src="../../images/new_window_black.png" alt=" (new window)" />
                </a>
              </td>
              <td nowrap align="middle" class="t">5633</td>
              <td nowrap align="middle" class="t">55990</td>
              <td nowrap align="middle" class="t">284</td>
              <td nowrap align="middle" class="t">2012-03-04</td>
              <td nowrap align="middle" class="t">09:15:38.880</td>
              <td nowrap align="middle" class="t">-315.483</td>
            </tr>
            
            <tr>
              <td nowrap align="middle" class="b">
                <a href="http://dr12.sdss3.org/irSpectrumDetail?plateid=5633&mjd=55991&fiber=284" class="content" target="_blank">
                  apogee.apo25m.s.r5.5633.55991.284&nbsp;<img src="../../images/new_window_black.png" alt=" (new window)" />
                </a>
              </td>
              <td nowrap align="middle" class="b">5633</td>
              <td nowrap align="middle" class="b">55991</td>
              <td nowrap align="middle" class="b">284</td>
              <td nowrap align="middle" class="b">2012-03-05</td>
              <td nowrap align="middle" class="b">08:37:54.709</td>
              <td nowrap align="middle" class="b">-316.837</td>
            </tr>
            
            <tr>
              <td nowrap align="middle" class="t">
                <a href="http://dr12.sdss3.org/irSpectrumDetail?plateid=5634&mjd=55992&fiber=282" class="content" target="_blank">
                  apogee.apo25m.s.r5.5634.55992.282&nbsp;<img src="../../images/new_window_black.png" alt=" (new window)" />
                </a>
              </td>
              <td nowrap align="middle" class="t">5634</td>
              <td nowrap align="middle" class="t">55992</td>
              <td nowrap align="middle" class="t">282</td>
              <td nowrap align="middle" class="t">2012-03-06</td>
              <td nowrap align="middle" class="t">07:22:26.810</td>
              <td nowrap align="middle" class="t">-9999</td>
            </tr>
            
            <tr>
              <td nowrap align="middle" class="b">
                <a href="http://dr12.sdss3.org/irSpectrumDetail?plateid=5633&mjd=55998&fiber=272" class="content" target="_blank">
                  apogee.apo25m.s.r5.5633.55998.272&nbsp;<img src="../../images/new_window_black.png" alt=" (new window)" />
                </a>
              </td>
              <td nowrap align="middle" class="b">5633</td>
              <td nowrap align="middle" class="b">55998</td>
              <td nowrap align="middle" class="b">272</td>
              <td nowrap align="middle" class="b">2012-03-12</td>
              <td nowrap align="middle" class="b">08:37:37.191</td>
              <td nowrap align="middle" class="b">-9999</td>
            </tr>
            
            <tr>
              <td nowrap align="middle" class="t">
                <a href="http://dr12.sdss3.org/irSpectrumDetail?plateid=5633&mjd=55999&fiber=272" class="content" target="_blank">
                  apogee.apo25m.s.r5.5633.55999.272&nbsp;<img src="../../images/new_window_black.png" alt=" (new window)" />
                </a>
              </td>
              <td nowrap align="middle" class="t">5633</td>
              <td nowrap align="middle" class="t">55999</td>
              <td nowrap align="middle" class="t">272</td>
              <td nowrap align="middle" class="t">2012-03-13</td>
              <td nowrap align="middle" class="t">09:28:31.190</td>
              <td nowrap align="middle" class="t">-329.791</td>
            </tr>
            
            <tr>
              <td nowrap align="middle" class="b">
                <a href="http://dr12.sdss3.org/irSpectrumDetail?plateid=5634&mjd=56019&fiber=277" class="content" target="_blank">
                  apogee.apo25m.s.r5.5634.56019.277&nbsp;<img src="../../images/new_window_black.png" alt=" (new window)" />
                </a>
              </td>
              <td nowrap align="middle" class="b">5634</td>
              <td nowrap align="middle" class="b">56019</td>
              <td nowrap align="middle" class="b">277</td>
              <td nowrap align="middle" class="b">2012-04-02</td>
              <td nowrap align="middle" class="b">07:38:21.648</td>
              <td nowrap align="middle" class="b">-313.135</td>
            </tr>
            
            <tr>
              <td nowrap align="middle" class="t">
                <a href="http://dr12.sdss3.org/irSpectrumDetail?plateid=5634&mjd=56021&fiber=277" class="content" target="_blank">
                  apogee.apo25m.s.r5.5634.56021.277&nbsp;<img src="../../images/new_window_black.png" alt=" (new window)" />
                </a>
              </td>
              <td nowrap align="middle" class="t">5634</td>
              <td nowrap align="middle" class="t">56021</td>
              <td nowrap align="middle" class="t">277</td>
              <td nowrap align="middle" class="t">2012-04-04</td>
              <td nowrap align="middle" class="t">08:27:12.091</td>
              <td nowrap align="middle" class="t">-9999</td>
            </tr>
            
            <tr>
              <td nowrap align="middle" class="b">
                <a href="http://dr12.sdss3.org/irSpectrumDetail?plateid=5634&mjd=56022&fiber=277" class="content" target="_blank">
                  apogee.apo25m.s.r5.5634.56022.277&nbsp;<img src="../../images/new_window_black.png" alt=" (new window)" />
                </a>
              </td>
              <td nowrap align="middle" class="b">5634</td>
              <td nowrap align="middle" class="b">56022</td>
              <td nowrap align="middle" class="b">277</td>
              <td nowrap align="middle" class="b">2012-04-05</td>
              <td nowrap align="middle" class="b">06:41:31.145</td>
              <td nowrap align="middle" class="b">-9999</td>
            </tr>
            
          </table>                          
        </div>  <!-- end of visits div -->
      </div>  <!-- end of irspec div -->

   </div>  

           
        </div>              
    </div>
    </form>
    <script language="JavaScript" type="text/javascript" src="../../wz_tooltip.js"></script>

    <iframe id="test" name='test' width ="0" height="0" scrolling="no"  src="../chart/blank.html"/>
	
</body>
</html>