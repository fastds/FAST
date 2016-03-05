<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="edu.gzu.utils.Utilities"  %>
<%@ page import="org.fastds.model.ApogeeControl"  %>
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