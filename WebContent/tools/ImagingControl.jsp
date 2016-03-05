<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.Map" %>
<c:if test="${not empty imagingCtrl.objID}">
	<div id="imaging">
	<h3>Imaging</h3>            
	
	<c:if test="${imagingCtrl.clean == 0}">
		<div class="warning">
	                <table><tr><td>
	                    <b>WARNING:</b> 
	                    This object's photometry may be unreliable. See the photometric 
	                    <em>flags</em> below.
	                </td></tr></table>
	   </div>
	</c:if>
	
	<table>
	    <tr>
	        <td>
	            <table cellpadding=2 cellspacing=2 border=0 width=420>
	                <tr align='left' >
	                    <td  valign='top' class='h'>
	                        <span title="SDSS flags" >
	                            <a href='${imagingCtrl.flagsLink}'>Flags <img src=../../images/offsite_black.png /></a>
	                        </span>
	                    </td>
	                    <td valign='top' class='t'>
	                        ${imagingCtrl.flag }
	                    </td>
	                </tr>
	            </table>
	         </td>
	     </tr>
	</table>
	
	<table>
	    <tr>
	        <td style="vertical-align:top">
	             <c:set var="link" value="javascript:showNavi(' + ${imagingCtrl.ra} + ',' + ${imagingCtrl.dec} + ',' + 0.2 + ');"></c:set>
	             <a href="${imagingCtrl.link}">
	                 <img alt="" src="${imagingCtrl.globals.wSGetJpegUrl + '?ra=' + imagingCtrl.ra + '&dec=' + imagingCtrl.dec + '&scale=0.2&width=200&height=200&opt=G' }" border="0" width="200" height="200" />
	             </a>
	         </td>
	        <td >
	            <table cellpadding=2 cellspacing=2 border=0 width=420>
	                <tr><td align='middle' class='h'><span></span></td></tr>
	                <tr><td nowrap align='middle' class='t'><b>Magnitudes</b></td></tr>
	            </table>
	            <table cellpadding=2 cellspacing=2 border=0 width=420>
	                <tr>
			        	<td align='middle' class='h'><span title="unit=<%=((Map)request.getAttribute("imagingCtrl")).get("u") %>">u</span></td>
			        	<td align='middle' class='h'><span title="unit=<%=((Map)request.getAttribute("imagingCtrl")).get("g") %>">g</span></td>
			        	<td align='middle' class='h'><span title="unit=<%=((Map)request.getAttribute("imagingCtrl")).get("r") %>">r</span></td>
			        	<td align='middle' class='h'><span title="unit=<%=((Map)request.getAttribute("imagingCtrl")).get("i") %>">i</span></td>
			        	<td align='middle' class='h'><span title="unit=<%=((Map)request.getAttribute("imagingCtrl")).get("z") %>">z</span></td>
	                </tr>
	                <tr>
	                	<td nowrap align='middle' class='t'> <fmt:formatNumber value="${imagingCtrl.u}" pattern="#.##"></fmt:formatNumber></td>
	                    <td nowrap align='middle' class='t'> <fmt:formatNumber value="${imagingCtrl.g}" pattern="#.##"></fmt:formatNumber></td>
	                    <td nowrap align='middle' class='t'> <fmt:formatNumber value="${imagingCtrl.r}" pattern="#.##"></fmt:formatNumber></td>
	                    <td nowrap align='middle' class='t'> <fmt:formatNumber value="${imagingCtrl.i}" pattern="#.##"></fmt:formatNumber></td>
	                    <td nowrap align='middle' class='t'> <fmt:formatNumber value="${imagingCtrl.z}" pattern="#.##"></fmt:formatNumber></td>
	               </tr>
	            </table>
	            <table cellpadding=2 cellspacing=2 border=0 width=420>
	                <tr><td align='middle' class='h'><span></span></td></tr>
	                <tr><td nowrap align='middle' class='t'><b>Magnitude uncertainties</b></td></tr>
	            </table>
	            <table cellpadding=2 cellspacing=2 border=0 width=420>
	                <tr>
	                	<td align='middle' class='h'><span title="unit=<%=((Map)request.getAttribute("imagingCtrl")).get("err_u") %>">err_u</span></td>
			        	<td align='middle' class='h'><span title="unit=<%=((Map)request.getAttribute("imagingCtrl")).get("err_g") %>">err_g</span></td>
			        	<td align='middle' class='h'><span title="unit=<%=((Map)request.getAttribute("imagingCtrl")).get("err_r") %>">err_r</span></td>
			        	<td align='middle' class='h'><span title="unit=<%=((Map)request.getAttribute("imagingCtrl")).get("err_i") %>">err_i</span></td>
			        	<td align='middle' class='h'><span title="unit=<%=((Map)request.getAttribute("imagingCtrl")).get("err_z") %>">err_z</span></td>
	                </tr>
	                <tr><td nowrap align='middle' class='t'> <fmt:formatNumber value="<%=((Map)request.getAttribute("imagingCtrl")).get("err_u") %>" pattern="#.##"></fmt:formatNumber></td>
	                    <td nowrap align='middle' class='t'> <fmt:formatNumber value="<%=((Map)request.getAttribute("imagingCtrl")).get("err_g") %>" pattern="#.##"></fmt:formatNumber></td>
	                    <td nowrap align='middle' class='t'> <fmt:formatNumber value="<%=((Map)request.getAttribute("imagingCtrl")).get("err_r") %>" pattern="#.##"></fmt:formatNumber></td>
	                    <td nowrap align='middle' class='t'> <fmt:formatNumber value="<%=((Map)request.getAttribute("imagingCtrl")).get("err_i") %>" pattern="#.##"></fmt:formatNumber></td>
	                    <td nowrap align='middle' class='t'> <fmt:formatNumber value="<%=((Map)request.getAttribute("imagingCtrl")).get("err_z") %>" pattern="#.##"></fmt:formatNumber></tr>
	              </table>       
	          </td>
	        </tr>
	        <tr>
	          <td colspan="2">
	            <table cellpadding=2 cellspacing=2 border=0 width=625>
	               <tr><td align='middle' class='h'><span title="unit=<%=((Map)request.getAttribute("imagingCtrl")).get("mjd") %>" >Image MJD</span></td>
	                   <td align='middle' class='h'><span title="unit=<%=((Map)request.getAttribute("imagingCtrl")).get("mode")%>" >mode</span></td>
	                   <td align='middle' class='h'><span title="unit=<%=((Map)request.getAttribute("imagingCtrl")).get("nDetect") %>" >Other observations</span></td>
	                   <td align='middle' class='h'><span title="unit=<%=((Map)request.getAttribute("imagingCtrl")).get("parentID") %>" >parentID</span></td>
	                   <td align='middle' class='h'><span title="unit=<%=((Map)request.getAttribute("imagingCtrl")).get("nChild") %>" >nChild</span></td>
	                   <td align='middle' class='h'><span title="unit=<%=((Map)request.getAttribute("imagingCtrl")).get("extinction_r") %>" >extinction_r</span></td>
	                   <td align='middle' class='h'><span title="unit=<%=((Map)request.getAttribute("imagingCtrl")).get("petroRad_r") %>" >PetroRad_r (arcsec)</span></td>
	               </tr>
	               <tr><td nowrap align='middle' class='t'>${imagingCtrl.mjdNum }</td>
	                   <td nowrap align='middle' class='t'>${imagingCtrl.mode }</td>
	                   <td nowrap align='middle' class='t'>${imagingCtrl.otherObs }</td>
	                   <td nowrap align='middle' class='t'>${imagingCtrl.parentID }</td>
	                   <td nowrap align='middle' class='t'>${imagingCtrl.nchild }</td>
	                   <td nowrap align='middle' class='t'>${imagingCtrl.extinction_r }</td>
	                   <td nowrap align='middle' class='t'>${imagingCtrl.petrorad_r }</td>
	               </tr>
	            </table>
	            <table cellpadding=2 cellspacing=2 border=0 width=625>
	                <tr>
	                    <td align='middle' class='h'><span title="unit=${imagingCtrl.columnUnit.Get("mjd") }" >Mjd-Date</span></td>
	                    <td align='middle' class='h'><span title="unit=${imagingCtrl.columnUnit.Get("z") }" >photoZ (KD-tree method)</span></td>
	                    <%--
	                    <td align='middle' class='h'><span title="unit=<%=columnUnit.Get('z') %>" >photoZ (RF method)</span></td>
	                    --%>
	                    <td align='middle' class='h'><span>Galaxy Zoo 1 morphology</span></td>
	                </tr>
	                <tr>
	                    <td nowrap align='middle' class='t'>${imagingCtrl.mjdDate }</td>
	                    <td nowrap align='middle' class='t'>${imagingCtrl.photoZ_KD }</td>
	                    <%--<td nowrap align='middle' class='t'><%=photoZ_RF %></td>--%>
	                    <td nowrap align='middle' class='t'>${imagingCtrl.galaxyZoo_Morph }</td>
	                </tr>
	            </table>                            
	          </td>
	        </tr>
	     </table>
	   </div>   
	  <%--  <!-- end of imaging div  -->--%>
</c:if>
