<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
	        	<c:when test="${crossIDCtrl.isUSNO }">
		        	<table cellpadding=2 cellspacing=2 border=0 width=620>
		                <tr>
		                    <td align='middle' class='h'><span>Catalog</span></td>
		                    <td align='middle' class='h'><span>Proper motion (mas/yr)</span></td>
		                    <td align='middle' class='h'><span>PM angle (deg E)</span></td>
		                </tr>
		                <tr>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.usno}</td>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.properMotion}</td>
		                    <td nowrap align='middle' class='t'><fmt:formatNumber value="${crossIDCtrl.angle}" pattern="#.##" ></fmt:formatNumber></td>
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
	        	<c:when test="${crossIDCtrl.isFIRST }">
	        		<table cellpadding=2 cellspacing=2 border=0 width=620>
		                <tr>
		                    <td align='middle' class='h'><span>Catalog</span></td>
		                    <td align='middle' class='h'><span>Peak flux (mJy)</span></td>
		                    <td align='middle' class='h'><span>Major axis (arcsec)</span></td>
		                    <td align='middle' class='h'><span>Minor axis (arcsec)</span></td>
		                </tr>
		                <tr>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.first}</td>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.peakflux}</td>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.major}</td>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.minor}</td>
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
	        	<c:when test="${crossIDCtrl.isROSAT }">
	        		<table cellpadding=2 cellspacing=2 border=0 width=620>
		                <tr>
		                    <td align='middle' class='h'><span>Catalog</span></td>
		                    <td align='middle' class='h'><span>cps</span></td>
		                    <td align='middle' class='h'><span>hr1</span></td>
		                    <td align='middle' class='h'><span>hr2</span></td>
		                    <td align='middle' class='h'><span>ext</span></td>
		                </tr>
		                <tr>
		                     <td nowrap align='middle' class='t'>${crossIDCtrl.rosat}</td>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.cps}</td>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.hr1}</td>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.hr2}</td>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.ext}</td>
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
	        	<c:when test="crossIDCtrl.isRC3">
	        		<table cellpadding=2 cellspacing=2 border=0 width=620>
		                <tr>
		                    <td align='middle' class='h'><span>Catalog</span></td>
		                    <td align='middle' class='h'><span>Hubble type</span></td>
		                    <td align='middle' class='h'><span>21 cm magnitude</span></td>
		                    <td align='middle' class='h'><span>Neutral Hydrogen Index</span></td>
		                </tr>
		                <tr>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.rc3}</td>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.hubletype}</td>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.magnitude}</td>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.hydrogenIndex}</td>
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
	        	<c:when test="crossIDCtrl.is2MASS">
	        		<table cellpadding=2 cellspacing=2 border=0 width=620>
		                <tr>
		                    <td align='middle' class='h'><span>Catalog</span></td>
		                    <td align='middle' class='h'><span>J</span></td>
		                    <td align='middle' class='h'><span>H</span></td>
		                    <td align='middle' class='h'><span>K_s</span></td>
		                    <td align='middle' class='h'><span>phQual</span></td>
		                </tr>
		                <tr>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.twomass}</td>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.j}</td>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.h}</td>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.k}</td>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.phQual}</td>
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
	        	<c:when test="crossIDCtrl.isWISE">
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
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.wise}</td>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.wmag1}</td>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.wmag2}</td>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.wmag3}</td>
		                    <td nowrap align='middle' class='t'>${crossIDCtrl.wmag4}</td>
		                    <td nowrap align='middle' class='t'><a href=".\DisplayResults.aspx?cmd=${crossIDCtrl.linkQuery}&name=wise">Link</a></td>                   
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
